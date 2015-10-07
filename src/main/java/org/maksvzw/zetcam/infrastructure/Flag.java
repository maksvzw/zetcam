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

import org.maksvzw.zetcam.core.Value;

/**
 * Represents a bit flag. 
 * <p>
 * This is a marker interface. A class that implements this interface indicates 
 * that {@link #value()} will always return a power of two.
 * </p>
 * @param <T> a type derived from {@link java.lang.Number} that implements the 
 * {@link java.lang.Comparable}.
 * 
 * @author Lenny Knockaert
 */
public interface Flag<T extends Number & Comparable<T>> extends Value<T>
{
    /**
     * Gets the bit value of this object.
     * @return the bit value of this object
     */
    @Override
    T value();
    
    /**
     * Gets the version of the bit value.
     * Due to the limited range of 32-bit and 64-bit integers, developers have
     * to resort to dirty hacks when they had no bits left. The new property 
     * usually had the name of the original appended with a number. 
     * @return the version of the bit field
     */
    default int version() {
        return 1;
    }
}