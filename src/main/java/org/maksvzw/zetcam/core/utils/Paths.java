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
package org.maksvzw.zetcam.core.utils;

import java.io.File;

/**
 *
 * @author Lenny
 */
public final class Paths 
{
    /**
     * Defines the primary directory separator character.
     */
    public static final char DIRECTORY_SEPARATOR_CHAR = '\\';
    
    /**
     * Defines the secondary directory separator character.
     */
    public static final char ALT_DIRECTORY_SEPARATOR_CHAR = '/';
    
    /**
     * Gets the file extension of the specified file path.
     * @param filePath the file path to get the extension from
     * @return the file extension of the specified file path or an empty string
     * if the file path was a directory or had no file extension
     */
    public static final String getFileExtension(String filePath)
    {
        return getFileExtension(filePath, false);
    }
    
    /**
     * Gets the file extension of the specified file.
     * @param file the file to get the extension from
     * @return the file extension of the specified file or an empty string
     * if the file path was a directory or had no file extension
     */
    public static final String getFileExtension(File file) 
    {
        if (file == null)
            throw new IllegalArgumentException("No file has been specified.");
        
        return getFileExtension(file.getPath(), false);
    }
    
    /**
     * Gets the file extension of the specified file.
     * @param file the file to get the extension from
     * @param noDot whether or not to include the leading dot of the extension
     * @return the file extension of the specified file or an empty string
     * if the file path was a directory or had no file extension
     */
    public static final String getFileExtension(File file, boolean noDot)
    {
        if (file == null)
            throw new IllegalArgumentException("No file has been specified.");
        
        return getFileExtension(file.getPath(), noDot);
    }
    
    /**
     * Gets the file extension of the specified file path.
     * @param filePath the file path to get the extension from
     * @param noDot whether or not to include the leading dot of the extension
     * @return the file extension of the specified file path or an empty string
     * if the file path was a directory or had no file extension
     */
    public static final String getFileExtension(String filePath, boolean noDot) 
    {
        if (filePath == null || filePath.isEmpty())
            throw new IllegalArgumentException("No file path has been specified.");
       
        int length = filePath.length();
        int index = length;
        
        char current;
        for(int i = length - 1; 0 <= i; i--) {
            current = filePath.charAt(i);
            
            if (current == DIRECTORY_SEPARATOR_CHAR || 
                current == ALT_DIRECTORY_SEPARATOR_CHAR) {
                break;
            } else if (current == '.') {
                index = i;
            }
        }

        String extension = "";
        if (noDot)
            index++;
        
        for(; index < length; index++) 
            extension += filePath.charAt(index);
        return extension;
    }
    
    public static final String getFileName(File file) 
    {
        if (file == null)
            throw new IllegalArgumentException("No file has been specified.");
        
        return getFileName(file.getAbsolutePath(), false);
    }
    
    public static final String getFileName(String filePath) 
    {
        return getFileName(filePath, false);
    }
    
    public static final String getFileName(File file, boolean withExtension) 
    {
        if (file == null)
            throw new IllegalArgumentException("No file has been specified.");
        
        return getFileName(file.getAbsolutePath(), withExtension);
    }
    
    public static final String getFileName(String filePath, boolean withExtension) 
    {
        if (filePath == null || filePath.isEmpty())
            throw new IllegalArgumentException("No file path has been specified.");
       
        int length = filePath.length();
        int start = 0, end = length;
        
        char current;
        for(int i = length - 1; 0 <= i; i--) {
            current = filePath.charAt(i);
            
            if (current == DIRECTORY_SEPARATOR_CHAR || 
                current == ALT_DIRECTORY_SEPARATOR_CHAR) {
                start = i + 1;
                break;
            } else if (current == '.' && !withExtension) {
                end = i;
            }
        }
        
        String fileName = "";
        for(int i = start; i < end; i++) 
            fileName += filePath.charAt(i);
        
        return fileName;
    }
    
    private Paths() { }
}