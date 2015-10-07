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
public class Int32SampleBuffer extends IntegerSampleBuffer
{
    private static final long MAXIMUM = 2147483647;
    private static final long MIDDLE = 0;
    private static final long MINIMUM = -2147483648;
    
    protected Int32SampleBuffer(IAudioSamples samples) {
        super(samples);
    }

    @Override
    protected Integer mixSample(Integer sample, Integer sample2)
    {
        final long mixedSample = Maths.clamp((long)sample + sample2, MINIMUM, MAXIMUM);
        return (int)mixedSample;
    }

    @Override
    protected Integer scaleSample(Integer sample, double scale) 
    {
        final long scaledSample = Maths.clamp((long)(sample * scale), MINIMUM, MAXIMUM);
        return (int)scaledSample;
    }

    @Override
    protected Integer getSilentSample() {
        return (int)MIDDLE;
    }
}