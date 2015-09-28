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
import org.maksvzw.zetcam.core.audio.buffers.AudioSampleBuffer;
import org.maksvzw.zetcam.core.utils.Maths;

/**
 *
 * @author Lenny Knockaert
 */
public final class BalanceFilter extends AudioFilter
{
    private double balanceValue;
    
    public BalanceFilter(final double balanceValue) 
    {
        this.setBalanceValue(balanceValue);
    }
    
    public double getBalanceValue() {
        return this.balanceValue;
    }
    
    public void setBalanceValue(double balanceValue) 
    {
        if (Double.compare(balanceValue, -1.0f) < 0 ||
            Double.compare(balanceValue, 1.0f) > 0)
            throw new IllegalArgumentException("Invalid balance value has been specified.");
        
        this.balanceValue = balanceValue;
    }
    
    @Override
    protected IAudioSamples onFilter(IAudioSamples samples) 
    {
        double balanceGain;
        try (AudioSampleBuffer buffer = AudioSampleBuffer.wrap(samples)) {
            while(buffer.hasRemaining()) {
                balanceGain = Maths.clamp(1.0f + Math.pow(-1.0f, buffer.channel()) * this.balanceValue, 0.0, 1.0);
                buffer.scale(balanceGain);
            }
        }
        return super.filter(samples);
    }
}