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
import java.awt.Dimension;
import java.io.Serializable;
import org.maksvzw.zetcam.core.images.filters.ImageFilter;
import org.maksvzw.zetcam.core.images.filters.ResizeFilter;

/**
 *
 * @author Lenny Knockaert
 */
public final class ResizeSettings extends ImageDspSettings implements Serializable
{
    private int width;
    private int height;
    private boolean isEnabled;
    
    public ResizeSettings() {
        this.setEnabled(false);
    }
    
    public ResizeSettings(final int newWidth, final int newHeight)
    {
        this.setNewWidth(newWidth);
        this.setNewHeight(newHeight);
        this.setEnabled(false);
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
    
    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
    
    public int getNewWidth() {
        return this.width;
    }
    
    public void setNewWidth(final int newWidth) 
    {
        if (newWidth < 1 || newWidth > 1920)
            throw new IllegalArgumentException("Invalid width has been specified. The value must be between 1 and 1920");

        this.width = newWidth;
    }
    
    public int getNewHeight() {
        return this.height;
    }
    
    public void setNewHeight(final int newHeight)
    {
        if (newHeight < 1 || newHeight > 1080)
            throw new IllegalArgumentException("Invalid width has been specified. The value must be between 1 and 1080.");
        
        this.height = newHeight;
    }
    
    public Dimension getNewDimensions() {
        return new Dimension(this.width, this.height);
    }
    
    public void setNewDimensions(final Dimension newDimensions) 
    {
        this.setNewWidth((int)newDimensions.getWidth());
        this.setNewHeight((int)newDimensions.getHeight());
    }

    @Override
    public ImageFilter buildFilter() 
    {
        if (!this.isEnabled())
            return null;
        
        return new ResizeFilter(this.width, this.height);
    }
}