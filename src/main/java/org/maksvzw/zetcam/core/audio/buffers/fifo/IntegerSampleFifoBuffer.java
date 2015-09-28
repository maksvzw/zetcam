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
package org.maksvzw.zetcam.core.audio.buffers.fifo;

import com.xuggle.xuggler.IAudioSamples;
import org.maksvzw.zetcam.core.audio.AudioFormat;

/**
 *
 * @author Lenny Knockaert
 */
public final class IntegerSampleFifoBuffer extends AudioSampleFifoBuffer<Integer>
{
    private final int[] tempBuf;
    
    protected IntegerSampleFifoBuffer(AudioFormat format, IAudioSamples samples) 
    {
        super(format, samples);
        this.tempBuf = new int[128];
    }

    @Override
    protected void get(IAudioSamples src, int srcIndex, IAudioSamples dst, int dstIndex, int length) 
    {
        int tempLength, srcLength = (int)(src.getNumSamples() - 1) * src.getChannels();
        while(length > 0) {
            tempLength = Math.min(srcLength - srcIndex, Math.min(this.tempBuf.length, length));
            src.get(srcIndex, this.tempBuf, 0, tempLength);
            dst.put(this.tempBuf, 0, dstIndex, tempLength);
            srcIndex += tempLength;
            dstIndex += tempLength;
            length -= tempLength;
            
            /* If we're at the end of the source buffer and the read request has
            not yet been satisfied, wrap around. */
            if (srcIndex >= srcLength) 
                srcIndex = 0;
        }
    }

    @Override
    protected void put(IAudioSamples dst, int dstIndex, IAudioSamples src, int srcIndex, int length) 
    {
        int tempLength, dstLength = (int)(dst.getNumSamples() - 1) * dst.getChannels();
        while(length > 0) {
            tempLength = Math.min(dstLength - dstIndex, Math.min(this.tempBuf.length, length));
            src.get(srcIndex, this.tempBuf, 0, tempLength);
            dst.put(this.tempBuf, 0, dstIndex, tempLength);
            srcIndex += tempLength;
            dstIndex += tempLength;
            length -= tempLength;
            
            /* If we're at the end of the source buffer and the read request has
            not yet been satisfied, wrap around. */
            if (dstIndex >= dstLength) 
                dstIndex = 0;
        }
    }
}