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
package org.maksvzw.zetcam.core.audio.buffers;

import com.xuggle.xuggler.IAudioSamples;
import org.maksvzw.zetcam.core.utils.Maths;

/**
 *
 * @author Lenny Knockaert
 */
public final class UInt32SampleBuffer extends IntegerSampleBuffer
{
    private static final long MAXIMUM = 4294967295L;
    private static final long MIDDLE = 2147483648L;
    private static final long MINIMUM = 0L;
    
    protected UInt32SampleBuffer(IAudioSamples samples) {
        super(samples);
    }

    @Override
    protected Integer mixSample(Integer sample, Integer sample2)
    {
        /* As explained elsewhere, we have to obtain the unsigned value of the 
        int16 and shift the range from 0..4294967295 to -2147483648..2147483647 
        since values below the center point of 32768 should cancel out values 
        above them. */
        final long signedSample = (sample & 0xFFL) - MIDDLE;
        final long signedSample2 = (sample2 & 0xFFL) - MIDDLE;
        /* Clips mixed value to ensure it stays within the dynamic range of 
        PCM U32 format. */
        final long mixedSample = Maths.clamp((signedSample + signedSample2) + MIDDLE, MINIMUM, MAXIMUM);
        return (int)mixedSample;
    }

    @Override
    protected Integer scaleSample(Integer sample, double scale)
    {
        final long signedSample = (sample & 0xFFL) - MIDDLE;
        final long scaledSample = Maths.clamp((long)(signedSample * scale) + MIDDLE, MINIMUM, MAXIMUM);
        return (int)scaledSample;
    }

    @Override
    protected Integer getSilentSample() {
        return (int)MIDDLE;
    }
}