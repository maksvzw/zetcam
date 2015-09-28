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
package org.maksvzw.zetcam.core.settings.dsp;

import java.io.Serializable;
import org.maksvzw.zetcam.core.images.filters.ImageFilter;
import org.maksvzw.zetcam.core.images.filters.ContrastFilter;

/**
 *
 * @author Lenny Knockaert
 */
public final class ContrastSettings extends ImageDspSettings implements Serializable
{
    private float brightness;
    private float contrast;
    
    public ContrastSettings() {
        this.setContrast(1.0f);
    }
    
    public ContrastSettings(final float contrast, final float brightness)
    {
        this.setContrast(contrast);
        this.setBrightness(brightness);
    }
    
    @Override
    public boolean isEnabled() {
        return this.contrast != 1.0f || this.brightness != 1.0f;
    }
    
    public float getContrast() {
        return this.contrast;
    }
    
    public void setContrast(final float contrastFactor) 
    {
        if (Float.compare(contrastFactor, 0.0f) < 0 || Float.compare(contrastFactor, 10.0f) > 0)
            throw new IllegalArgumentException("Invalid contrast factor has been specified. The value must be between 0.0 and 10.0");
        
        this.contrast = contrastFactor;
    }
    
    public float getBrightness() {
        return this.brightness;
    }
    
    public void setBrightness(final float brightnessFactor) 
    {
        if (Float.compare(brightnessFactor, 0.0f) < 0 || Float.compare(brightnessFactor, 10.0f) > 0)
            throw new IllegalArgumentException("Invalid brightness factor specified. The value must be between 0.0 and 10.0");
        
        this.brightness = brightnessFactor;
    }

    @Override
    public ImageFilter buildFilter() {
        return ContrastFilter.create(this.contrast, this.brightness);
    }
}