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
import org.maksvzw.zetcam.core.MediaType;
import org.maksvzw.zetcam.core.audio.filters.AudioFilter;
import org.maksvzw.zetcam.core.settings.audio.AudioDspSettings;
import org.maksvzw.zetcam.core.settings.audio.BalanceSettings;
import org.maksvzw.zetcam.core.settings.audio.FadeInSettings;
import org.maksvzw.zetcam.core.settings.audio.FadeOutSettings;
import org.maksvzw.zetcam.core.settings.audio.TrimSettings;
import org.maksvzw.zetcam.core.settings.audio.VolumeSettings;

/**
 *
 * @author Lenny Knockaert
 */
// => FadeSettings & TrimSettings need to know the start time & duration of the 
// source audio in order to validate new settings.
// => FadeFilter & TrimFilter need to know the format of the source audio in 
// order to calculate the sample numbers. Or the start time & duration are
// passed AS sample numbers to FadeFilter & TrimeFilter?
// => Or alternatively; FadeFilter has no subclasses, rather, FadeDirection is 
// promoted to full object status and is purely functional. Like this we don't 
// need to split up the filter nor its settings.
public class AudioSourceSettings extends MediaSourceSettings
{
    private final TrimSettings trimSettings;
    private final VolumeSettings volumeSettings;
    private final BalanceSettings balanceSettings;
    private final FadeInSettings fadeInSettings;
    private final FadeOutSettings fadeOutSettings;
    
    public AudioSourceSettings(final AudioSourceProperties srcProperties) 
    {
        this.trimSettings = new TrimSettings(
                srcProperties.getFormat(), 
                srcProperties.getStartTime(), 
                srcProperties.getDuration());
        
        this.volumeSettings = new VolumeSettings();
        this.balanceSettings = new BalanceSettings();
        
        this.fadeInSettings = new FadeInSettings(
                srcProperties.getFormat(), 
                srcProperties.getStartTime(), 
                srcProperties.getDuration());
        
        this.fadeOutSettings = new FadeOutSettings(
                srcProperties.getFormat(),
                srcProperties.getStartTime(), 
                srcProperties.getDuration());
    }
    
    @Override
    public MediaType getType() {
        return MediaType.AUDIO;
    }
    
    public TrimSettings getTrimSettings() {
        return this.trimSettings;
    }
    
    public VolumeSettings getVolumeSettings() {
        return this.volumeSettings;
    }
    
    public BalanceSettings getBalanceSettings() {
        return this.balanceSettings;
    }

    public FadeInSettings getFadeInSettings() {
        return this.fadeInSettings;
    }
    
    public FadeOutSettings getFadeOutSettings() {
        return this.fadeOutSettings;
    }
    
    @Override
    public AudioFilter buildFilterChain()
    {
        final Queue<AudioDspSettings> queue = new ArrayDeque<>();
        queue.offer(this.trimSettings);
        queue.offer(this.volumeSettings);
        queue.offer(this.balanceSettings);
        queue.offer(this.fadeInSettings);
        queue.offer(this.fadeOutSettings);
        
        AudioFilter tmpFlt, flt = null, firstFlt = null;
        AudioDspSettings settings;
        
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