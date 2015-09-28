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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import org.maksvzw.zetcam.core.MediaType;
import org.maksvzw.zetcam.core.utils.Paths;

/**
 *
 * @author Lenny Knockaert
 */
public abstract class MediaResource 
{
    private final String name;
    
    public MediaResource(String name) 
    {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("No resource name has been specified.");
        
        this.name = name;
    }
    
    public final String getName() {
        return this.name;
    }
    
    public abstract Path getPath();
    
    public abstract MediaType getType();
    
    public long size() throws IOException 
    {
        final Path resourcePath;
        if ((resourcePath = this.getPath()) == null)
            return 0;
        
        return Files.size(resourcePath);
    }
    
    public boolean exists() 
    {
        final Path resourcePath;
        if ((resourcePath = this.getPath()) == null)
            return false;
        
        return Files.exists(resourcePath, LinkOption.NOFOLLOW_LINKS);
    }
    
    public InputStream open() throws IOException 
    {
        final Path resourcePath;
        if ((resourcePath = this.getPath()) == null)
            return null;
        
        return Files.newInputStream(resourcePath, StandardOpenOption.READ);
    }
    
    public void copy(Path targetPath) throws IOException 
    {
        if (targetPath == null)
            throw new IllegalArgumentException("No target path has been specified.");
        
        final Path resourcePath;
        if ((resourcePath = this.getPath()) == null) 
        {
            final String fileName = Paths.getFileName(targetPath.getFileName().toString(), false);
            if (!this.getName().equals(fileName))
                throw new IllegalArgumentException("The specified target path must the same fileName.");
            
            try (InputStream inputStream = this.open()) {
                Files.copy(inputStream, targetPath, StandardCopyOption.ATOMIC_MOVE);
            }
        }
        else {
            if (!targetPath.endsWith(resourcePath.getFileName()))
                throw new IllegalArgumentException("The specified target path must the same fileName.");

            Files.copy(resourcePath, targetPath, StandardCopyOption.ATOMIC_MOVE);
        }
    }
    
    //public abstract void delete();
}