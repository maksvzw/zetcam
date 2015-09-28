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
    public static final ContrastFilter create(final float contrast) {
        return new ContrastFilter(new com.jhlabs.image.ContrastFilter(), contrast, 1.0f);
    } 
    
    public static final ContrastFilter create(
            final float contrast, 
            final float brightness)
    {
        return new ContrastFilter(new com.jhlabs.image.ContrastFilter(), contrast, brightness);
    } 
    
    private final com.jhlabs.image.ContrastFilter filter;
    
    private ContrastFilter(
            final com.jhlabs.image.ContrastFilter filter, 
            final float contrast, final float brightness) 
    {
        super(filter, true);
        
        this.filter = filter;
        this.setContrast(contrast);
        this.setBrightness(brightness);
    }
    
    public float getContrast() {
        return this.filter.getContrast();
    }
    
    public void setContrast(final float contrast) 
    {
        if (Float.compare(contrast, 0.0f) < 0 || Float.compare(contrast, 10.0f) > 0)
            throw new IllegalArgumentException("Invalid contrast factor has been specified. The value must be between 0.0 and 10.0");
        
        this.filter.setContrast(contrast);
    }
    
    public float getBrightness() {
        return this.filter.getBrightness();
    }
    
    public void setBrightness(final float brightness)
    {
        if (Float.compare(brightness, 0.0f) < 0 || Float.compare(brightness, 10.0f) > 0)
            throw new IllegalArgumentException("Invalid brightness factor has been specified. The value must be between 0.0 and 10.0");
        
        this.filter.setBrightness(brightness);
    }
}