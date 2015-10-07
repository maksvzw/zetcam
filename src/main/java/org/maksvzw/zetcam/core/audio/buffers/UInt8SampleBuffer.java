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
public final class UInt8SampleBuffer extends ByteSampleBuffer
{
    private static final int MAXIMUM = 255;
    private static final int MIDDLE = 128;
    private static final int MINIMUM = 0;
    
    protected UInt8SampleBuffer(IAudioSamples samples) {
        super(samples);
    }

    @Override
    protected Byte mixSample(Byte sample, Byte sample2) 
    {
        /* As explained elsewhere, we have to obtain the unsigned value of the 
        byte and shift the range from 0..255 to -128..127 since values below 
        the center point of 128 should cancel out values above them. */
        final int signedSample = (sample & 0xFF) - MIDDLE;
        final int signedSample2 = (sample2 & 0xFF) - MIDDLE;
        /* Clips mixed value to ensure it stays within the dynamic range of 
        PCM U8 format. */
        final int mixedSample = Maths.clamp((signedSample + signedSample2) + MIDDLE, MINIMUM, MAXIMUM);
        return (byte)mixedSample;
    }

    @Override
    protected Byte scaleSample(Byte sample, double scale) 
    {
        final int signedSample = (sample & 0xFF) - MIDDLE;
        final int scaledSample = Maths.clamp((int)(signedSample * scale) + MIDDLE, MINIMUM, MAXIMUM);
        return (byte)scaledSample;
    }

    @Override
    protected Byte getSilentSample() {
        return (byte)MIDDLE;
    }
}