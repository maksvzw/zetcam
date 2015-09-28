/*
 * Copyright (C) 2015 Lenny Knockaert <lknockx@gmail.com>
 *
 * This file is part of ZetCam
 *
 * ZetCam is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ZetCam is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.maksvzw.zetcam.core.audio.filters;

import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IRational;
import java.time.Duration;
import org.maksvzw.zetcam.core.audio.buffers.AudioSampleBuffer;

/**
 *
 * @author Lenny Knockaert
 */
public final class TrimFilter extends AudioFilter
{
    private Duration startTime;
    private long startSample;
    private Duration duration;
    private long numOfSamples;
    
    public TrimFilter(Duration newStartTime, Duration newDuration)
    {
        this.setNewStartTime(newStartTime);
        this.setNewDuration(newDuration);
    }
    
    public Duration getNewStartTime() {
        return this.startTime;
    }
    
    public void setNewStartTime(Duration startTime) 
    {
        if (startTime == null)
            throw new IllegalArgumentException("No start time has been specified.");
        
        this.startTime = startTime;
        this.startSample = 0;
    }
    
    public Duration getNewDuration() {
        return this.duration;
    }
    
    public void setNewDuration(Duration d) 
    {
        if (d == null)
            throw new IllegalArgumentException("No fade duration has been specified.");
        
        this.duration = d;
        this.numOfSamples = 0;
    }
    
    @Override
    protected IAudioSamples onFilter(IAudioSamples samples)
    {
        /** Convert start time and duration of the fade transition to 
         samples if that wasn't already the case. */
        if (this.startSample == 0 && this.startTime != Duration.ZERO) {
            this.startSample = IRational.rescale(
                    this.startTime.toNanos(),
                    1, samples.getSampleRate(),
                    1, 1000000000,
                    IRational.Rounding.ROUND_NEAR_INF);
        }
        
        if (this.numOfSamples == 0 && this.duration != Duration.ZERO) {
            this.numOfSamples = IRational.rescale(
                    this.duration.toNanos(),
                    1, samples.getSampleRate(),
                    1, 1000000000,
                    IRational.Rounding.ROUND_NEAR_INF);
        }
        
        /* Convert the start time and duration of this audio frame to samples. */
        final long numOfSamples = samples.getNumSamples();
        final long currentSample = IRational.sRescale(
                samples.getTimeStamp(), 
                samples.getTimeBase(), 
                IRational.make(1, samples.getSampleRate()));
        
        /* All samples before the new start sample or after the new duration 
        are simply discarded. */
        if ((currentSample + numOfSamples) < this.startSample ||
            (this.startSample + this.numOfSamples) < currentSample)
            return null;
        
        /* If there are samples at the start of the audio frame that should
        be trimmed, and similarly at the end of the audio frame, we will 
        silence them instead to maintain consistency of the decoded frames'
        sizes. */
        if (currentSample < this.startSample) {
            try (AudioSampleBuffer buffer = AudioSampleBuffer.wrap(samples)) {
                buffer.limit((int)(this.startSample - currentSample)).silenceAll();
            }
        }
        else if ((this.startSample + this.numOfSamples) < (currentSample + numOfSamples)) {
            final long diffSamples = (currentSample + numOfSamples) - (this.startSample + this.numOfSamples);
            try (AudioSampleBuffer buffer = AudioSampleBuffer.wrap(samples)) {
                buffer.position((int)(numOfSamples - diffSamples)).silenceAll();
            }
        }
        return super.filter(samples);
    }
}