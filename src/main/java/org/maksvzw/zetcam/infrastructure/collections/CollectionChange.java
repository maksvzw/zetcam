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
package org.maksvzw.zetcam.infrastructure.collections;

/**
 * Defines an enumeration of changes that can occur in collections of items.
 * 
 * @author Lenny
 */
public enum CollectionChange 
{
    /**
     * Indicates that one or more items were added to the collection.
     */
    ADD,
    
    /**
     * Indicates that one or more items were removed from the collection.
     */
    REMOVE,
    
    /**
     * Indicates that one or more items were replaced in the collection.
     */
    REPLACE,
    
    /**
     * Indicates that one or more items were moved within the collection.
     */
    MOVE,
    
    /**
     * Indicates that the content of the collection has changed dramatically.
     */
    RESET
}