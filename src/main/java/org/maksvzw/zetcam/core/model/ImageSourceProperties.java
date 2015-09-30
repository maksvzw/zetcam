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
package org.maksvzw.zetcam.core.model;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import org.maksvzw.zetcam.core.MediaType;

/**
 *
 * @author Lenny Knockaert
 */
public class ImageSourceProperties extends MediaSourceProperties implements Serializable
{
    private final int width;
    private final int height;
    private final String formatName;
    
    public ImageSourceProperties(
            final String filePath, 
            final int width, 
            final int height, 
            final String formatName)
    {
        super(filePath);
        
        if (width <= 0 || height <= 0)
            throw new IllegalArgumentException("Invalid image dimensions have been specified.");
        if (formatName == null || formatName.isEmpty())
            throw new IllegalArgumentException("No format name has been specified.");
        
        this.width = width;
        this.height = height;
        this.formatName = formatName;
    }
    
    @Override
    public MediaType getType() {
        return MediaType.IMAGE;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public Dimension getDimensions() {
        return new Dimension(this.width, this.height);
    }
    
    public String getFormatName() {
        return this.formatName;
    }

    @Override
    public Map<String, String> getMetadata() {
        return Collections.emptyMap();
    }
}