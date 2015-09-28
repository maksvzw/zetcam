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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import org.maksvzw.zetcam.core.images.Image;

/**
 *
 * @author Lenny Knockaert
 */
public final class ImageFileResource extends ImageResource
{
    private final Path imagePath;
    private final Dimension imageSize;
            
    public ImageFileResource(
            final Path imagePath,
            final Cache<String, BufferedImage> imageCache,
            final Cache<String, BufferedImage> thumbnailCache) 
            throws FileNotFoundException, IOException 
    {
        this(   imagePath.getFileName().toString().split(".")[0],
                imagePath, 
                imageCache, 
                thumbnailCache);
    }
    
    public ImageFileResource(
            final String name, 
            final Path imagePath,
            final Cache<String, BufferedImage> imageCache,
            final Cache<String, BufferedImage> thumbnailCache) 
            throws FileNotFoundException, IOException
    {
        super(name, imageCache, thumbnailCache);
        
        if (imagePath == null)
            throw new IllegalArgumentException("No image file has been specified.");
        if (!Image.isImage(imagePath))
            throw new IOException("'"+imagePath + "' is not a known image file.");

        this.imagePath = imagePath;
        /* Gets the dimensions of the image without loading it into memory. */
        this.imageSize = Image.getDimensions(imagePath);
    }
    
    @Override
    public final Path getPath() {
        return this.imagePath;
    }
    
    @Override
    public Dimension getSize() {
        return this.imageSize;
    }
    
    @Override
    protected BufferedImage loadImage() throws IOException
    {
        try (InputStream inputStream = this.open()) {
            return ImageIO.read(inputStream);
        }
    }
}