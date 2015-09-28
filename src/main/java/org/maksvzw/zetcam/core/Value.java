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
package org.maksvzw.zetcam.core;

/**
 * Represents a numerical value. 
 * 
 * @param <T> a type derived from {@link java.lang.Number} that implements the 
 * {@link java.lang.Comparable}.
 * 
 * @author Lenny Knockaert
 */
public interface Value<T extends Number & Comparable<T>>
{
    /**
     * Gets the value of this object.
     * @return the value of this object
     */
    T value();
}