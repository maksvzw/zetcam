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
public final class Int8SampleBuffer extends ByteSampleBuffer
{
    private static final int MAXIMUM = 127;
    private static final int MIDDLE = 0;
    private static final int MINIMUM = -128;
    
    protected Int8SampleBuffer(IAudioSamples samples) {
        super(samples);
    }

    @Override
    protected Byte mixSample(Byte sample, Byte sample2)
    {
        final int mixedSample = Maths.clamp((int)sample + sample2, MINIMUM, MAXIMUM);
        return (byte)mixedSample;
    }

    @Override
    protected Byte scaleSample(Byte sample, double scale) 
    {
        final int scaledSample = Maths.clamp((int)(sample * scale), MINIMUM, MAXIMUM);
        return (byte)scaledSample;
    }

    @Override
    protected Byte getSilentSample() {
        return (byte)MIDDLE;
    }
}