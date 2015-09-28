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
package org.maksvzw.zetcam.io.resources;

import com.google.common.cache.Cache;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.maksvzw.zetcam.core.MediaType;
import org.maksvzw.zetcam.core.images.Image;

/**
 *
 * @author Lenny Knockaert
 */
public abstract class ImageResource extends MediaResource
{
    private final Cache<String, BufferedImage> imageCache;
    private final Cache<String, BufferedImage> thumbnailCache;
    private final String cachedThumbnailName;
    private Dimension cachedThumbnailDimensions;
    
    public ImageResource(
            final String name,
            final Cache<String, BufferedImage> imageCache,
            final Cache<String, BufferedImage> thumbnailCache) 
    {
        super(name);
        
        if (imageCache == null)
            throw new IllegalArgumentException("No image cache has been specified.");
        if (thumbnailCache == null)
            throw new IllegalArgumentException("No image thumbnail cache has been specified.");
        
        this.imageCache = imageCache;
        this.thumbnailCache = thumbnailCache;
        this.cachedThumbnailName = name + "_thumbnail";
        this.cachedThumbnailDimensions = null;
    }

    @Override
    public final MediaType getType() {
        return MediaType.IMAGE;
    }
    
    public abstract Dimension getSize();
    
    public BufferedImage getImage() 
            throws ExecutionException, IOException 
    {
        return this.imageCache.get(this.getName(), new Callable() { 
            @Override
            public BufferedImage call() throws IOException {
                return loadImage();
            }
        });
    }
    
    protected abstract BufferedImage loadImage() throws IOException;
    
    public BufferedImage getThumbnail(Dimension thumbnailDimensions) 
            throws ExecutionException, IOException 
    {
        if (thumbnailDimensions == null)
            throw new IllegalArgumentException("No thumbnail dimensions have been specified.");
        
        if (!thumbnailDimensions.equals(this.cachedThumbnailDimensions)) {
            this.thumbnailCache.invalidate(this.cachedThumbnailName);
            this.cachedThumbnailDimensions = thumbnailDimensions;
        }

        return this.thumbnailCache.get(this.cachedThumbnailName, new Callable() { 
            @Override
            public BufferedImage call() throws ExecutionException, IOException {
                return loadThumbnail(cachedThumbnailDimensions);
            }
        });
    }
    
    protected BufferedImage loadThumbnail(Dimension thumbnailDimensions) 
            throws ExecutionException, IOException 
    {
        return Image.resize(this.getImage(), thumbnailDimensions);
    }
}