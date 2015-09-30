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
// TODO: If width is negative, resize to height and scale accordingly.
// If height is negative, resize to width and scale accordingly.
// If both values are negative, do nothing.
public final class ResizeFilter extends ImageFilter
{
    private final int width;
    private final int height;
    
    public ResizeFilter(final Dimension newDimension) {
        this((int)newDimension.getWidth(), (int)newDimension.getHeight());
    }
    
    public ResizeFilter(final int newWidth, final int newHeight) 
    {
        super();
        
        if (newWidth < 1)
            throw new IllegalArgumentException("Invalid width has been specified. The value cannot be smaller than one.");
        if (newHeight < 1)
            throw new IllegalArgumentException("Invalid width has been specified. The value cannot be smaller than one.");
        
        this.width = newWidth;
        this.height = newHeight;
    }

    @Override
    protected BufferedImage onFilter(BufferedImage src) {
        return Image.resize(src, this.width, this.height);
    }
}