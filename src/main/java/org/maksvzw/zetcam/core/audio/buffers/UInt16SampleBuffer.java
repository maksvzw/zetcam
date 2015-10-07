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
import org.maksvzw.zetcam.infrastructure.Maths;

/**
 *
 * @author Lenny Knockaert
 */
public class UInt16SampleBuffer extends ShortSampleBuffer
{
    private static final int MAXIMUM = 65536;
    private static final int MIDDLE = 32768;
    private static final int MINIMUM = 0;
    
    protected UInt16SampleBuffer(IAudioSamples samples) {
        super(samples);
    }

    @Override
    protected Short mixSample(Short sample, Short sample2) 
    {
        /* As explained elsewhere, we have to obtain the unsigned value of the 
        int16 and shift the range from 0..65535 to -32768..32767 since values 
        below the center point of 32768 should cancel out values above them. */
        final int signedSample = (sample & 0xFF) - MIDDLE;
        final int signedSample2 = (sample2 & 0xFF) - MIDDLE;
        /* Clips mixed value to ensure it stays within the dynamic range of 
        PCM U16 format. */
        final int mixedSample = Maths.clamp((signedSample + signedSample2) + MIDDLE, MINIMUM, MAXIMUM);
        return (short)mixedSample;
    }

    @Override
    protected Short scaleSample(Short sample, double scale) 
    {
        final int signedSample = (sample & 0xFF) - MIDDLE;
        final int scaledSample = Maths.clamp((int)(signedSample * scale) + MIDDLE, MINIMUM, MAXIMUM);
        return (short)scaledSample;
    }

    @Override
    protected Short getSilentSample() {
        return (short)MIDDLE;
    }
}