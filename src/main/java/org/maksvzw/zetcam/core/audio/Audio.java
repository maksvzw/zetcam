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
package org.maksvzw.zetcam.core.audio;

import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IRational;
import java.time.Duration;

/**
 *
 * @author Lenny Knockaert
 */
public final class Audio
{
    public static final IAudioSamples allocateSamples(
        final AudioFormat audioFormat, 
        final long numOfSamples)
    {
        return allocateSamples(audioFormat, numOfSamples, 0);
    }
    
    public static final IAudioSamples allocateSamples(
            final AudioFormat audioFormat, 
            final long numOfSamples, 
            final long ptsInSamples)
    {
        final IAudioSamples samples = IAudioSamples.make(
                numOfSamples, 
                audioFormat.getChannels(), 
                audioFormat.getSampleFormat());

        /* Set time properties of IMediaData interface. The time stamp must be
        in time base of 1/(sample rate) which means the time stamp actually
        represents the number of samples passed since the start of the signal. */
        samples.setTimeBase(IRational.make(1, audioFormat.getSampleRate()));
        samples.setTimeStamp(ptsInSamples);
        
        /* Set presentation time stamp of IAudioSamples interface. This 
        interface expects the time stamp to be in a time base of 1/1000000 
        which means it actually represents the number of microseconds passed
        since the start of the signal. If we already know how many samples 
        have passed, we can simply rescale the value to a 1/1000000 time base. */
        final long pts = IRational.rescale(
                        ptsInSamples, 
                        1, 1000000,
                        1, audioFormat.getSampleRate(),
                        IRational.Rounding.ROUND_NEAR_INF);
        
        samples.setComplete(
                true, 
                numOfSamples, 
                audioFormat.getSampleRate(), 
                audioFormat.getChannels(), 
                audioFormat.getSampleFormat(), 
                pts);
        
        return samples;
    }
    
    public static final long getNumOfSamples(final AudioFormat audioFormat, final Duration d) 
    {
        return IRational.rescale(
                    d.toNanos(),
                    1, audioFormat.getSampleRate(),
                    1, 1000000000,
                    IRational.Rounding.ROUND_NEAR_INF);
    }
}