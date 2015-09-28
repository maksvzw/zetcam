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

/**
 *
 * @author Lenny Knockaert
 */
public class VolumeFilter extends AudioFilter
{
    private double volume;
    
    public VolumeFilter(double volume) 
    {
        this.setVolume(volume);
    }
    
    public double getVolume() {
        return this.volume;
    }
    
    public void setVolume(double volume) 
    {
        if (Double.compare(volume, 0.0) < 0 || Double.compare(volume, 3.0) > 0)
            throw new IllegalArgumentException("Volume multiplier cannot be smaller than zero or larger than three.");
        
        this.volume = volume;
    }
    
    @Override
    protected IAudioSamples onFilter(IAudioSamples samples) 
    {
        if (this.volume != 1.0) {
            try (AudioSampleBuffer buffer = AudioSampleBuffer.wrap(samples)) {
                buffer.scaleAll(this.volume);
            }
        }
        return super.filter(samples);
    }
}