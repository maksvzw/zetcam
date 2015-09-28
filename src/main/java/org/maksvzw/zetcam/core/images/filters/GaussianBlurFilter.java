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

import com.jhlabs.image.GaussianFilter;

/**
 *
 * @author Lenny Knockaert
 */
public final class GaussianBlurFilter extends ImageFilterAdapter
{
    public static final GaussianBlurFilter create(final float radius) {
        return new GaussianBlurFilter(new GaussianFilter(), radius);
    } 
    
    private final GaussianFilter filter;
    
    private GaussianBlurFilter(final GaussianFilter filter, final float radius) 
    {
        super(filter, true);
        
        this.filter = filter;
        this.setRadius(radius);
    }
    
    public float getRadius() {
        return this.filter.getRadius();
    }
    
    public void setRadius(final float radius)
    {
        if (Float.compare(radius, 1.0f) < 0)
            throw new IllegalArgumentException("Invalid blur radius specified. It cannot be smaller than 1.");
        
        this.filter.setRadius(radius);
    }
}