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
package org.maksvzw.zetcam.core.settings.images;

import org.maksvzw.zetcam.core.settings.images.ImageDspSettings;
import java.io.Serializable;
import org.maksvzw.zetcam.core.images.filters.ImageFilter;
import org.maksvzw.zetcam.core.images.filters.GammaCorrectionFilter;

/**
 *
 * @author Lenny Knockaert
 */
public final class GammaCorrectionSettings extends ImageDspSettings implements  Serializable
{
    private float rGamma;
    private float gGamma;
    private float bGamma;
    
    public GammaCorrectionSettings() {
        this(1.0f);
    }
    
    public GammaCorrectionSettings(final float gamma) {
        this.setGamma(gamma);
    }
    
    public GammaCorrectionSettings(final float rGamma, final float gGamma, final float bGamma)
    {
        this.setRedGamma(rGamma);
        this.setGreenGamma(gGamma);
        this.setBlueGamma(bGamma);
    }

    @Override
    public boolean isEnabled() {
        return this.rGamma != 1.0f || this.gGamma != 1.0f || this.bGamma != 1.0f;
    }
    
    public float getGamma() {
        return this.rGamma;
    }
    
    public void setGamma(final float gamma) 
    {
        if (Float.compare(gamma, 0.0f) < 0 || Float.compare(gamma, 10.0f) > 0)
            throw new IllegalArgumentException("Invalid gamma factor specified. The value must be between 0.0 and 10.0");
        
        this.rGamma = gamma;
        this.gGamma = gamma;
        this.bGamma = gamma;
    }
    
    public float getRedGamma() {
        return this.rGamma;
    }
    
    public void setRedGamma(final float rGamma)
    {
        if (Float.compare(rGamma, 0.0f) < 0 || Float.compare(rGamma, 10.0f) > 0)
            throw new IllegalArgumentException("Invalid red gamma factor specified. The value must be between 0.0 and 10.0");
        
        this.rGamma = rGamma;
    }
    
    public float getGreenGamma() {
        return this.gGamma;
    }
    
    public void setGreenGamma(final float gGamma)
    {
        if (Float.compare(gGamma, 0.0f) < 0 || Float.compare(gGamma, 10.0f) > 0)
            throw new IllegalArgumentException("Invalid green gamma factor specified. The value must be between 0.0 and 10.0");
        
        this.gGamma = gGamma;
    }
    
    public float getBlueGamma() {
        return this.bGamma;
    }
    
     public void setBlueGamma(final float bGamma)
    {
        if (Float.compare(bGamma, 0.0f) < 0 || Float.compare(bGamma, 10.0f) > 0)
            throw new IllegalArgumentException("Invalid green gamma factor specified. The value must be between 0.0 and 10.0");
        
        this.bGamma = bGamma;
    }

    @Override
    public ImageFilter buildFilter() 
    {
        if (!this.isEnabled())
            return null;
        
        return new GammaCorrectionFilter(this.rGamma, this.gGamma, this.bGamma);
    }
}