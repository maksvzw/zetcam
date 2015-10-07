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
public final class FloatSampleBuffer extends AudioSampleBuffer<Float>
{
    private static final float MAXIMUM = 1.0f;
    private static final float MIDDLE = 0.0f;
    private static final float MINIMUM = -1.0f;
    
    private final float[] tempBuf;
    private float[] mixBuf;
    
    protected FloatSampleBuffer(IAudioSamples samples) 
    {
        super(samples);
        this.tempBuf = new float[128];
    }

    @Override
    protected Float getSample(IAudioSamples samples, int index) 
    {
        samples.get(index, this.tempBuf, 0, 1);
        return this.tempBuf[0];
    }

    @Override
    protected void putSample(IAudioSamples samples, int index, Float sample) 
    {
        this.tempBuf[0] = sample;
        samples.put(this.tempBuf, 0, index, 1);
    }

    @Override
    protected void copySamples(IAudioSamples src, int srcIndex, IAudioSamples dst, int dstIndex, int length) 
    {
        int tempLength;
        while(length > 0) {
            tempLength = Math.min(this.tempBuf.length, length);
            src.get(srcIndex, this.tempBuf, 0, tempLength);
            dst.put(this.tempBuf, 0, dstIndex, tempLength);
            srcIndex += tempLength;
            dstIndex += tempLength;
            length -= tempLength;
        }
    }
    
    @Override
    protected Float mixSample(Float sample, Float sample2) {
        return Maths.clamp(sample + sample2, MINIMUM, MAXIMUM);
    }
    
    @Override
    protected final void mixSamples(IAudioSamples src, int srcIndex, IAudioSamples dst, int dstIndex, int length) 
    {
        if (this.mixBuf == null)
            this.mixBuf = new float[this.tempBuf.length];
        
        int tempLength;
        while(length > 0) {
            tempLength = Math.min(this.tempBuf.length, length);
            src.get(srcIndex, this.tempBuf, 0, tempLength);
            dst.get(srcIndex, this.mixBuf, 0, tempLength);
            for(int i = 0; i < tempLength; i++) 
                this.mixBuf[i] = this.mixSample(this.tempBuf[i], this.mixBuf[i]);
            dst.put(this.mixBuf, 0, dstIndex, tempLength);
            srcIndex += tempLength;
            dstIndex += tempLength;
            length -= tempLength;
        }
    }

    @Override
    protected Float scaleSample(Float sample, double scale) {
        return Maths.clamp((float)(sample * scale), MINIMUM, MAXIMUM);
    }
    
    @Override
    protected void scaleSamples(IAudioSamples samples, int index, int length, double scale) 
    {
        int tempLength;
        while(length > 0) {
            tempLength = Math.min(length, this.tempBuf.length);
            samples.get(index, this.tempBuf, 0, tempLength);
            for(int i = 0; i < tempLength; i++)
                this.tempBuf[i] = this.scaleSample(this.tempBuf[i], scale);
            samples.put(this.tempBuf, 0, index, tempLength);
            length -= tempLength;
            index += tempLength;
        }
    }

    @Override
    protected Float getSilentSample() {
        return MIDDLE;
    }
    
    @Override
    protected final void fillSamples(IAudioSamples samples, int index, int length, Float sample) 
    {
        int tempLength;
        while(length > 0) {
            tempLength = Math.min(length, this.tempBuf.length);
            samples.get(index, this.tempBuf, 0, tempLength);
            for(int i = 0; i < tempLength; i++)
                this.tempBuf[i] = sample;
            samples.put(this.tempBuf, 0, index, tempLength);
            length -= tempLength;
            index += tempLength;
        }
    }
}