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
import org.maksvzw.zetcam.core.audio.Audio;
import org.maksvzw.zetcam.core.audio.AudioFormat;
import org.maksvzw.zetcam.core.audio.buffers.AudioSampleBuffer;

/**
 *
 * @author Lenny Knockaert
 */
public final class TrimFilter extends AudioFilter
{
    private final long newStartSample;
    private final long newNumOfSamples;
    
    public TrimFilter(
            final AudioFormat audioFormat,
            final Duration newStartTime, 
            final Duration newDuration)
    {
        if (audioFormat == null)
            throw new IllegalArgumentException("No audio format has been specified.");
        if (newStartTime == null)
            throw new IllegalArgumentException("No start time has been specified.");
        if (newDuration == null)
            throw new IllegalArgumentException("No fade duration has been specified.");
        if (newDuration.compareTo(Duration.ZERO) <= 0)
            throw new IllegalArgumentException("Invalid fade duration has been specified. The value must be larger than zero.");
        
        /** Convert new start time and duration of the audio source to 
         sample numbers. */
        this.newStartSample = Audio.getNumOfSamples(audioFormat, newStartTime);
        this.newNumOfSamples = Audio.getNumOfSamples(audioFormat, newDuration);
    }
    
    @Override
    protected IAudioSamples onFilter(IAudioSamples samples)
    {
        /* Convert the start time and duration of this audio frame to samples. */
        final long numOfSamples = samples.getNumSamples();
        final long currentSample = IRational.sRescale(
                samples.getTimeStamp(), 
                samples.getTimeBase(), 
                IRational.make(1, samples.getSampleRate()));
        
        /* All samples before the new start sample or after the new duration 
        are simply discarded. */
        if ((currentSample + numOfSamples) < this.newStartSample ||
            (this.newStartSample + this.newNumOfSamples) < currentSample)
            return null;
        
        /* If there are samples at the start of the audio frame that should
        be trimmed, and similarly at the end of the audio frame, we will 
        silence them instead to maintain consistency of the decoded frames'
        sizes. */
        if (currentSample < this.newStartSample) {
            try (AudioSampleBuffer buffer = AudioSampleBuffer.wrap(samples)) {
                buffer.limit((int)(this.newStartSample - currentSample)).silenceAll();
            }
        }
        else if ((this.newStartSample + this.newNumOfSamples) < (currentSample + numOfSamples)) {
            final long diffSamples = (currentSample + numOfSamples) - (this.newStartSample + this.newNumOfSamples);
            try (AudioSampleBuffer buffer = AudioSampleBuffer.wrap(samples)) {
                buffer.position((int)(numOfSamples - diffSamples)).silenceAll();
            }
        }
        return super.filter(samples);
    }
}