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
package org.maksvzw.zetcam.infrastructure;

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
     * Gets the name without the path from the specified file path.
     * @param filePath the file path to extract the name from
     * @return the name without the path from the specified file path
     */
    public static final String getName(String filePath) 
    {
        return getFileName(filePath, true);
    }
    
    /**
     * Gets the name without the path and extension from the specified file path.
     * @param filePath the file path to extract the name from
     * @return the name without the path and extension from the specified file path
     */
    public static final String getBaseName(String filePath) 
    {
        return getFileName(filePath, false);
    }
    
    private static String getFileName(String filePath, boolean withExtension) 
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
    
    /**
     * Gets the extension (without the dot) of the specified file path.
     * @param filePath the file path to extract the extension from
     * @return the extension (without the dot) of the specified file path
     */
    public static final String getExtension(String filePath) 
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
                index = i + 1;
                break;
            }
        }

        String extension = "";
        for(; index < length; index++) 
            extension += filePath.charAt(index);
        return extension;
    }

    private Paths() { }
}