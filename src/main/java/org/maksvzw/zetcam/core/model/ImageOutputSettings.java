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
import org.maksvzw.zetcam.core.settings.dsp.ContrastSettings;
import org.maksvzw.zetcam.core.settings.dsp.GammaCorrectionSettings;
import org.maksvzw.zetcam.core.settings.dsp.GaussianBlurSettings;
import org.maksvzw.zetcam.core.settings.dsp.GrayscaleSettings;
import org.maksvzw.zetcam.core.settings.dsp.ImageDspSettings;
import org.maksvzw.zetcam.core.settings.dsp.ResizeSettings;

/**
 *
 * @author Lenny Knockaert
 */
// TODO: separate settings per filter so this class is a combination
public class ImageOutputSettings extends MediaOutputSettings
{
    private final ContrastSettings cSettings;
    private final GammaCorrectionSettings gcSettings;
    private final GaussianBlurSettings gbSettings;
    private final GrayscaleSettings gsSettings;
    private final ResizeSettings rSettings;
    
    public ImageOutputSettings()
    {
        this.cSettings = new ContrastSettings();
        this.gcSettings = new GammaCorrectionSettings();
        this.gbSettings = new GaussianBlurSettings();
        this.gsSettings = new GrayscaleSettings();
        this.rSettings = new ResizeSettings();
    }
    
    @Override
    public MediaType getType() {
        return MediaType.IMAGE;
    }
    
    public ContrastSettings getContrastSettings() {
        return this.cSettings;
    }
    
    public GammaCorrectionSettings getGammaCorrectionSettings() {
        return this.gcSettings;
    }
    
    public GaussianBlurSettings getGaussianBlurSettings() {
        return this.gbSettings;
    }
    
    public GrayscaleSettings getGrayscaleSettings() {
        return this.gsSettings;
    }
    
    public ResizeSettings getResizeSettings() {
        return this.rSettings;
    }

    @Override
    public ImageFilter buildFilterChain() 
    {
        final Queue<ImageDspSettings> queue = new ArrayDeque<>();
        queue.offer(this.rSettings);
        queue.offer(this.gcSettings);
        queue.offer(this.cSettings);
        queue.offer(this.gbSettings);
        queue.offer(this.gsSettings);
        
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