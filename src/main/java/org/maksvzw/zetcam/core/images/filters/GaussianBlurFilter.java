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
    public GaussianBlurFilter(final float radius) 
    {
        super(new com.jhlabs.image.GaussianFilter());
        
        if (Float.compare(radius, 1.0f) < 0)
            throw new IllegalArgumentException("Invalid blur radius specified. It cannot be smaller than 1.");
        
        this.getImageOp().setRadius(radius);
    }
    
    @Override
    protected com.jhlabs.image.GaussianFilter getImageOp() {
        return (com.jhlabs.image.GaussianFilter)super.getImageOp();
    }
}