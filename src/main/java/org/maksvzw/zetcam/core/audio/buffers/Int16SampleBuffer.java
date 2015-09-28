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
public final class Int16SampleBuffer extends ShortSampleBuffer
{
    private static final int MAXIMUM = 32767;
    private static final int MIDDLE = 0;
    private static final int MINIMUM = -32768;
    
    protected Int16SampleBuffer(IAudioSamples samples) {
        super(samples);
    }

    @Override
    protected Short mixSample(Short sample, Short sample2) 
    {
        final int mixedSample = Maths.clamp(sample + sample2, MINIMUM, MAXIMUM);
        return (short)mixedSample;
    }

    @Override
    protected Short scaleSample(Short sample, double scale) 
    {
        final int scaledSample = Maths.clamp((int)(sample * scale), MINIMUM, MAXIMUM);
        return (short)scaledSample;
    }

    @Override
    protected Short getSilentSample() {
        return (short)MIDDLE;
    }
}