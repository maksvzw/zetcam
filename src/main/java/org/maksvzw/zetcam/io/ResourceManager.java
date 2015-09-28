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
package org.maksvzw.zetcam.io;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import org.maksvzw.zetcam.io.resources.AudioResource;
import org.maksvzw.zetcam.io.resources.ImageFileResource;
import org.maksvzw.zetcam.io.resources.ImageResource;
import org.maksvzw.zetcam.io.resources.InMemoryImageResource;
import org.maksvzw.zetcam.core.utils.Disposable;

/**
 *
 * @author Lenny Knockaert
 */
public final class ResourceManager extends Disposable
{
    private final ConcurrentMap<String, ImageResource> imageResourceCache;
    private final ConcurrentMap<String, AudioResource> audioResourceCache;
    private final Cache<String, BufferedImage> imageCache;
    private final Cache<String, BufferedImage> thumbNailCache;

    public ResourceManager()
    {
        this.imageResourceCache = new ConcurrentHashMap<>();
        this.audioResourceCache = new ConcurrentHashMap<>();
        
        this.imageCache = CacheBuilder.newBuilder()
                        .maximumSize(128)
                        .expireAfterAccess(5, TimeUnit.MINUTES)
                        .build();
        
        this.thumbNailCache = CacheBuilder.newBuilder()
                        .maximumSize(512)
                        .expireAfterAccess(150, TimeUnit.SECONDS)
                        .build();
    }
    
    public int getNumOfResources() {
        return this.imageResourceCache.size() + this.audioResourceCache.size();
    }
    
    public int getNumOfImageResources() {
        return this.imageResourceCache.size();
    }

    public ImageResource getImageResource(String name) 
    {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("No resource name has been specified.");
        
        return this.imageResourceCache.get(name);
    }
    
    public ImageResource newImageResource(String name, BufferedImage image) 
    {
        ImageResource resource = this.getImageResource(name);
        if (resource != null)
            return resource;
        
        resource = new InMemoryImageResource(name, image, this.imageCache, this.thumbNailCache);
        this.imageResourceCache.put(name, resource);
        return resource;
    }

    public ImageResource newImageResource(String name, Path imagePath) 
            throws FileNotFoundException, IOException
    {
        ImageResource resource = this.getImageResource(name);
        if (resource != null)
            return resource;
        
        resource = new ImageFileResource(name, imagePath, this.imageCache, this.thumbNailCache);
        this.imageResourceCache.put(name, resource);
        return resource;
    }
    
    public int getNumOfAudioResources() {
        return this.audioResourceCache.size();
    }
    
    public AudioResource getAudioResource(String name) 
    {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("No resource name has been specified.");
        
        return this.audioResourceCache.get(name);
    }

    public AudioResource newAudioResource(String name, Path audioPath) 
            throws FileNotFoundException, Exception
    {
        AudioResource resource = this.getAudioResource(name);
        if (resource != null)
            return resource;
        
        resource = new AudioResource(audioPath);
        this.audioResourceCache.put(name, resource);
        return resource;
    }

    public void invalidateResource(String name) 
    {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("No resource name has been specified.");
        
        this.imageCache.invalidate(name);
        this.thumbNailCache.invalidate(name);
        this.imageResourceCache.remove(name);
        this.audioResourceCache.remove(name);
    }
    
    public void invalidateAllImageResources() 
    {
        this.imageCache.invalidateAll();
        this.thumbNailCache.invalidateAll();
        this.imageResourceCache.clear();
    }
    
    public void invalidateAllAudioResources() {
        this.audioResourceCache.clear();
    }
    
    public void invalidateAll() 
    {
        this.invalidateAllImageResources();
        this.invalidateAllAudioResources();
    }

    @Override
    protected void release() {
        this.invalidateAll();
    }
}