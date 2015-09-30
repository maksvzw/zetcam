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

import java.io.Serializable;
import java.util.Map;
import org.maksvzw.zetcam.core.MediaType;
import org.maksvzw.zetcam.core.utils.Paths;

/**
 *
 * @author Lenny Knockaert
 */
public abstract class MediaSourceProperties implements Serializable
{
    private final String fileName;
    
    public MediaSourceProperties(final String filePath) {
        if (filePath == null || filePath.isEmpty())
            throw new IllegalArgumentException("No file path has been specified.");
        
        this.fileName = Paths.getFileName(filePath, true);
    }
    
    public abstract MediaType getType();
    
    public String getFileName() {
        return this.fileName;
    }
    
    public String getFileExtension() {
        return Paths.getFileExtension(this.fileName, true);
    }
    
    public abstract Map<String, String> getMetadata();
}