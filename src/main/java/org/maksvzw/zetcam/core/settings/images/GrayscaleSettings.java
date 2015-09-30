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
import java.io.Serializable;
import org.maksvzw.zetcam.core.images.filters.ImageFilter;
import org.maksvzw.zetcam.core.images.filters.GrayscaleFilter;

/**
 *
 * @author Lenny Knockaert
 */
public final class GrayscaleSettings extends ImageDspSettings implements Serializable
{
    private boolean isEnabled;
    
    public GrayscaleSettings() 
    { 
        this.setEnabled(false);
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
    
    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
    
    @Override
    public ImageFilter buildFilter() 
    {
        if (!this.isEnabled)
            return null;
        
        return new GrayscaleFilter();
    }
}