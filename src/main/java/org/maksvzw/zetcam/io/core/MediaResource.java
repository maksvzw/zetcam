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
package org.maksvzw.zetcam.io.core;

import java.nio.file.Path;
import org.maksvzw.zetcam.core.MediaType;
import org.maksvzw.zetcam.core.model.MediaInputProperties;
import org.maksvzw.zetcam.core.utils.Paths;

/**
 *
 * @author Lenny Knockaert
 */
public class MediaResource 
{
    private final Path filePath;
    private final MediaInputProperties properties;
    
    public MediaResource(
            final Path filePath, 
            final MediaInputProperties properties) 
    {
        if (filePath == null || filePath.getNameCount() <= 0)
            throw new IllegalArgumentException("No resource file path has been specified.");
        if (properties == null)
            throw new IllegalArgumentException("No resource properties have been specified.");
        
        this.filePath = filePath;
        this.properties = properties;
    }
    
    public String getStorageName() {
        return Paths.getFileName(this.filePath.toString(), false);
    }
    
    public String getFileName() {
        return Paths.getFileName(this.filePath.toString(), true);
    }
    
    public String getFileExtension() {
        return Paths.getFileExtension(this.filePath.toString(), true);
    }
    
    public Path getFilePath() {
        return this.filePath;
    }
    
    public MediaType getType() {
        return this.properties.getType();
    }
    
    public MediaInputProperties getProperties() {
        return this.properties;
    }
}