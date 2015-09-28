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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import javax.imageio.ImageIO;

/**
 *
 * @author Lenny Knockaert
 */
public final class InMemoryImageResource extends ImageResource
{
    private final BufferedImage image;

    public InMemoryImageResource(
            final String name, 
            final BufferedImage image,
            final Cache<String, BufferedImage> imageCache,
            final Cache<String, BufferedImage> thumbnailCache)
    {
        super(name, imageCache, thumbnailCache);
        
        if (image == null)
            throw new IllegalArgumentException("No image has been specified.");
        if (thumbnailCache == null)
            throw new IllegalArgumentException("No image thumbnail cache has been specified.");
        
        this.image = image;
    }

    @Override
    public Dimension getSize() 
    {
        return new Dimension(
                this.image.getWidth(),
                this.image.getHeight()
        );
    }
    
    @Override
    public Path getPath() {
        return null;
    }
    
    @Override
    public BufferedImage getImage() {
        return this.image;
    }
    
    @Override
    protected BufferedImage loadImage() { 
        return null;
    } 

    @Override
    public InputStream open() throws IOException
    {
        final ByteArrayOutputStream output = new ByteArrayOutputStream() {
            @Override
            public synchronized byte[] toByteArray() {
                return this.buf;
            }
        };
        ImageIO.write(this.image, "png", output);
        return new ByteArrayInputStream(output.toByteArray(), 0, output.size()); 
    }
}