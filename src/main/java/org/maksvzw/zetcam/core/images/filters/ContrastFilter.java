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

/**
 *
 * @author Lenny Knockaert
 */
public final class ContrastFilter extends ImageFilterAdapter
{
    public ContrastFilter(final float contrast, final float brightness) 
    {
        super(new com.jhlabs.image.ContrastFilter());
        
        if (Float.compare(contrast, 0.0f) < 0 || Float.compare(contrast, 10.0f) > 0)
            throw new IllegalArgumentException("Invalid contrast factor has been specified. The value must be between 0.0 and 10.0");
        if (Float.compare(brightness, 0.0f) < 0 || Float.compare(brightness, 10.0f) > 0)
            throw new IllegalArgumentException("Invalid brightness factor has been specified. The value must be between 0.0 and 10.0");
  
        this.getImageOp().setContrast(contrast);
        this.getImageOp().setBrightness(brightness);
    }
    
    @Override 
    protected com.jhlabs.image.ContrastFilter getImageOp() {
        return (com.jhlabs.image.ContrastFilter)super.getImageOp();
    }
}