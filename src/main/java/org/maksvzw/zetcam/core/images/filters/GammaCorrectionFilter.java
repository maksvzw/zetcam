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
package org.maksvzw.zetcam.core.images.filters;

import com.jhlabs.image.GammaFilter;

/**
 *
 * @author Lenny Knockaert
 */
public final class GammaCorrectionFilter extends ImageFilterAdapter
{
    public static final GammaCorrectionFilter create(final float gamma) {
        return new GammaCorrectionFilter(new GammaFilter(), gamma, gamma, gamma);
    }
    
    public static final GammaCorrectionFilter create(
            final float rGamma, 
            final float gGamma, 
            final float bGamma) 
    {
        return new GammaCorrectionFilter(new GammaFilter(), rGamma, gGamma, bGamma);
    }
    
    private final GammaFilter filter;
    
    private GammaCorrectionFilter(
            final GammaFilter filter, 
            final float rGamma, 
            final float gGamma, 
            final float bGamma) 
    {
        super(filter, true);
        
        this.filter = filter;
        this.setGamma(rGamma, gGamma, bGamma);
    }
    
    public float getGamma() {
        return this.filter.getGamma();
    }
    
    public void setGamma(final float gamma) 
    {
        if (Double.compare(gamma, 0.0) < 0 || Double.compare(gamma, 10.0) > 0)
            throw new IllegalArgumentException("Invalid gamma factor specified. The value must be between 0.0 and 10.0");
        
        this.filter.setGamma(gamma);
    }
    
    public void setGamma(final float rGamma, final float gGamma, final float bGamma) 
    {
        if (Float.compare(rGamma, 0.0f) < 0 || Float.compare(rGamma, 10.0f) > 0)
            throw new IllegalArgumentException("Invalid red gamma factor specified. The value must be between 0.0 and 10.0");
        if (Float.compare(gGamma, 0.0f) < 0 || Float.compare(gGamma, 10.0f) > 0)
            throw new IllegalArgumentException("Invalid green gamma factor specified. The value must be between 0.0 and 10.0");
        if (Float.compare(bGamma, 0.0f) < 0 || Float.compare(bGamma, 10.0f) > 0)
            throw new IllegalArgumentException("Invalid blue gamma factor specified. The value must be between 0.0 and 10.0");
        
        this.filter.setGamma(rGamma, gGamma, bGamma);
    }
}