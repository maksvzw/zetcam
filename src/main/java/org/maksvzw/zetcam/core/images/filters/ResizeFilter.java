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

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import org.maksvzw.zetcam.core.images.Image;

/**
 *
 * @author Lenny Knockaert
 */
public final class ResizeFilter extends ImageFilter
{
    public static final ResizeFilter create(final int width, final int height) {
        return new ResizeFilter(width, height);
    }
    
    private int width;
    private int height;
    
    private ResizeFilter(final int width, final int height) 
    {
        super();
        
        this.setWidth(width);
        this.setHeight(height);
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(int width) 
    {
        if (width < 1)
            throw new IllegalArgumentException("Invalid width has been specified. The value cannot be smaller than one.");
        
        this.width = width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(int height) 
    {
        if (height < 1)
            throw new IllegalArgumentException("Invalid width has been specified. The value cannot be smaller than one.");
        
        this.height = height;
    }
    
    public Dimension getSize() {
        return new Dimension(this.width, this.height);
    }
    
    public void setSize(Dimension d) 
    {
        if (d == null)
            throw new IllegalArgumentException("No dimensions have been specified.");
        
        this.setWidth((int)d.getWidth());
        this.setHeight((int)d.getHeight());
    }
    
    @Override
    public boolean supportsInPlaceFiltering() {
        return false;
    }

    @Override
    protected BufferedImage onFilter(BufferedImage src) 
    {
        return Image.resize(src, this.width, this.height);
    }
}