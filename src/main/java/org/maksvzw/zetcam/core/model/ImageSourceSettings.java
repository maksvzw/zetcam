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

import java.util.ArrayDeque;
import java.util.Queue;
import org.maksvzw.zetcam.core.images.filters.ImageFilter;
import org.maksvzw.zetcam.core.MediaType;
import org.maksvzw.zetcam.core.settings.images.ContrastSettings;
import org.maksvzw.zetcam.core.settings.images.GammaCorrectionSettings;
import org.maksvzw.zetcam.core.settings.images.GaussianBlurSettings;
import org.maksvzw.zetcam.core.settings.images.GrayscaleSettings;
import org.maksvzw.zetcam.core.settings.images.ImageDspSettings;
import org.maksvzw.zetcam.core.settings.images.ResizeSettings;

/**
 *
 * @author Lenny Knockaert
 */
// TODO: Take input properties into account for resize filter
// => ResizeSettings needs to know the dimensions of the source image to validate 
// new settings.
// => ResizeFilter needs to know the dimensions of the source image to calculate 
// the scale factors.
public class ImageSourceSettings extends MediaSourceSettings
{
    private final ContrastSettings contrastSettings;
    private final GammaCorrectionSettings gammaSettings;
    private final GaussianBlurSettings blurSettings;
    private final GrayscaleSettings grayscaleSettings;
    private final ResizeSettings resizeSettings;
    
    public ImageSourceSettings()
    {
        this.contrastSettings = new ContrastSettings();
        this.gammaSettings = new GammaCorrectionSettings();
        this.blurSettings = new GaussianBlurSettings();
        this.grayscaleSettings = new GrayscaleSettings();
        this.resizeSettings = new ResizeSettings();
    }
    
    @Override
    public MediaType getType() {
        return MediaType.IMAGE;
    }
    
    public ContrastSettings getContrastSettings() {
        return this.contrastSettings;
    }
    
    public GammaCorrectionSettings getGammaCorrectionSettings() {
        return this.gammaSettings;
    }
    
    public GaussianBlurSettings getGaussianBlurSettings() {
        return this.blurSettings;
    }
    
    public GrayscaleSettings getGrayscaleSettings() {
        return this.grayscaleSettings;
    }
    
    public ResizeSettings getResizeSettings() {
        return this.resizeSettings;
    }

    @Override
    public ImageFilter buildFilterChain() 
    {
        final Queue<ImageDspSettings> queue = new ArrayDeque<>();
        queue.offer(this.resizeSettings);
        queue.offer(this.gammaSettings);
        queue.offer(this.contrastSettings);
        queue.offer(this.blurSettings);
        queue.offer(this.grayscaleSettings);
        
        ImageFilter tmpFlt, flt = null, firstFlt = null;
        ImageDspSettings settings;
        
        while(!queue.isEmpty()) 
        {
            settings = queue.poll();
            if (!settings.isEnabled())
                continue;
            
            if (firstFlt == null) {
                firstFlt = settings.buildFilter();
                flt = firstFlt;
            } else {
                tmpFlt = settings.buildFilter();
                flt.setNext(tmpFlt);
                flt = tmpFlt;
            }
        }
        return firstFlt;
    }
}