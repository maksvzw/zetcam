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
import org.maksvzw.zetcam.core.images.filters.GaussianBlurFilter;

/**
 *
 * @author Lenny Knockaert
 */
public class GaussianBlurSettings extends ImageDspSettings implements  Serializable
{
    private float blurRadius;
    private boolean isEnabled;
    
    public GaussianBlurSettings() {
        this(0.0f);
    }
    
    public GaussianBlurSettings(final float blurRadius) 
    {
        this.setBlurRadius(blurRadius);
        this.setEnabled(false);
    }
    
    @Override
    public boolean isEnabled() {
        return this.blurRadius > 0;
    }
    
    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
    
    public float getBlurRadius() {
        return this.blurRadius;
    }
    
    public void setBlurRadius(final float blurRadius)
    {
        if (Float.compare(blurRadius, 1.0f) < 0)
            throw new IllegalArgumentException("Invalid blur radius specified. It cannot be smaller than 1.");
        
        this.blurRadius = blurRadius;
    }
    
    @Override
    public ImageFilter buildFilter() {
        return GaussianBlurFilter.create(this.blurRadius);
    }
}