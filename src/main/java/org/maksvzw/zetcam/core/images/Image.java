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
package org.maksvzw.zetcam.core.images;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.maksvzw.zetcam.core.utils.Paths;

/**
 *
 * @author Lenny Knockaert
 */
public final class Image 
{
    public static final boolean isImage(File imageFile) 
            throws FileNotFoundException, IOException 
    {
        return isImage(imageFile.toPath());
    }
    
    public static final boolean isImage(Path imagePath) 
            throws FileNotFoundException, IOException 
    {
        if (imagePath == null)
            throw new IllegalArgumentException("No image file has been specified.");
        if (!Files.exists(imagePath, LinkOption.NOFOLLOW_LINKS))
            throw new FileNotFoundException(imagePath.toString());
        
        /* Probe file's content to check whether it really is an image. */
        final String mimeType = Files.probeContentType(imagePath);
        final String type = mimeType.split("/")[0];
        if (!type.equalsIgnoreCase("image"))
            return true;
        
        return false;
    }
    
    public static final Dimension getDimensions(File imageFile) throws IOException {
        return getDimensions(imageFile.toPath());
    }
    
    public static final Dimension getDimensions(Path imagePath) throws IOException 
    {
        if (imagePath == null)
            throw new IllegalArgumentException("No image file has been specified.");
        
        final String suffix = Paths.getFileExtension(imagePath.toString(), true);
        final Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
        if (!iter.hasNext())
            throw new IOException("'"+imagePath + "' is not a known image file.");
        
        final ImageReader reader = iter.next();
        
        try (InputStream inputStream = Files.newInputStream(imagePath, LinkOption.NOFOLLOW_LINKS);
             ImageInputStream imageStream = ImageIO.createImageInputStream(inputStream))
        {
            reader.setInput(imageStream);
            final int width = reader.getWidth(reader.getMinIndex());
            final int height = reader.getHeight(reader.getMinIndex());
            return new Dimension(width, height);
        }
        finally {
            reader.dispose();
        }
    }
    
    public static final ByteArrayOutputStream getOutputStream(
            final BufferedImage image) throws IOException 
    {
        return getOutputStream(image, "png");
    }
    
    public static final ByteArrayOutputStream getOutputStream(
            final BufferedImage image, String formatName) throws IOException 
    {
        if (image == null)
            throw new IllegalArgumentException("No buffered image has been specified.");
        if (formatName == null || formatName.isEmpty())
            formatName = "png";
        
        final ByteArrayOutputStream output = new ByteArrayOutputStream() {
            @Override
            public synchronized byte[] toByteArray() {
                return this.buf;
            }
        };

        ImageIO.write(image, formatName, output);
        return output;
    }
    
    public static final ByteArrayInputStream getInputStream(
            final BufferedImage image) throws IOException 
    {
        return getInputStream(image, "png");
    }
    
    public static final ByteArrayInputStream getInputStream(
            final BufferedImage image, String formatName) throws IOException
    {
        if (image == null)
            throw new IllegalArgumentException("No buffered image has been specified.");
        
        final ByteArrayOutputStream outputStream = getOutputStream(image, formatName);
        return new ByteArrayInputStream(outputStream.toByteArray(), 0, outputStream.size()); 
    }
    
    public static final BufferedImage duplicate(final BufferedImage image)
    {
        if (image == null)
            throw new IllegalArgumentException("No buffered image has been specified.");
        
        return new BufferedImage(
                image.getColorModel(), 
                image.copyData(null), 
                image.isAlphaPremultiplied(), 
                null);
    }
    
    public static final BufferedImage convert(
            final BufferedImage image, 
            final int newType) 
    {
        if (image == null)
            throw new IllegalArgumentException("No buffered image has been specified.");

        final BufferedImage convertedImage = new BufferedImage(
                image.getWidth(), 
                image.getHeight(),
                newType);
        
        final Graphics2D g = convertedImage.createGraphics();
        try {
            g.addRenderingHints(new RenderingHints(
                    RenderingHints.KEY_RENDERING, 
                    RenderingHints.VALUE_RENDER_QUALITY));
            g.drawImage(image, 0, 0, null);
        } finally {
            g.dispose();
        }
        return convertedImage;
    }
    
    public static final BufferedImage resize(
            final BufferedImage image,
            final Dimension newDimensions)
    {
        return resize(image, (int)newDimensions.getWidth(), (int)newDimensions.getHeight());
    }
    
    public static final BufferedImage resize(
            final BufferedImage image, 
            final int newWidth, 
            final int newHeight)
    {
        if (image == null)
            throw new IllegalArgumentException("No buffered image has been specified.");
        if (newWidth < 1 || newHeight < 1)
            throw new IllegalArgumentException("The specified width and height cannot be smaller than one pixel.");
        if (image.getWidth() == newWidth && image.getHeight() == newHeight)
            return image;
        
        final BufferedImage resizedImage = new BufferedImage(
                newWidth, 
                newHeight,
                image.getType());
        
        final Graphics2D g = resizedImage.createGraphics();
        try {
            g.addRenderingHints(new RenderingHints(
                    RenderingHints.KEY_RENDERING, 
                    RenderingHints.VALUE_RENDER_QUALITY));
            g.drawImage(image, 0, 0, newWidth, newHeight, null);
        } finally {
            g.dispose();
        }
        return resizedImage;
    }
    
    public static final BufferedImage createCompatibleImage(
            final BufferedImage image)
    {
        return createCompatibleImage(image, null);
    }
    
    public static final BufferedImage createCompatibleImage(
            final BufferedImage image, 
            ColorModel cm) 
    {
        if (cm == null)
            cm = image.getColorModel();
        
        return new BufferedImage(
                cm, 
                cm.createCompatibleWritableRaster(image.getWidth(), image.getHeight()),
                cm.isAlphaPremultiplied(),
                null);
    }
    
    public static final BufferedImage createAndFill(
            final int width, 
            final int height, 
            final int imageType, 
            final Color color) 
    {
        if (width < 1 || height < 1)
            throw new IllegalArgumentException("The specified width and height cannot be smaller than one pixel.");
        if (color == null)
            throw new IllegalArgumentException("No color has been specified.");
        
        final BufferedImage image = new BufferedImage(
                    width, 
                    height, 
                    imageType);
        
        final Graphics2D g = image.createGraphics();
        try {
            g.addRenderingHints(new RenderingHints(
                    RenderingHints.KEY_RENDERING, 
                    RenderingHints.VALUE_RENDER_QUALITY));
            g.setColor(color);
            g.fillRect(0, 0, width, height);
        } finally {
            g.dispose();
        }
        return image;
    }
    
    private Image() { }
}