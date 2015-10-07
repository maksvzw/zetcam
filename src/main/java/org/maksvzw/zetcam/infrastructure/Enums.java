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

import java.util.EnumSet;
import java.util.Set;

/**
 * {@link java.lang.Enum} utilities class.
 * 
 * @author Lenny Knockaert
 */
public final class Enums 
{
    /**
     * Converts the given set of {@link E} objects to a 32-bit bitfield.
     * If there are more flags than a datatype has bits, a popular scheme in 
     * native APIs has been to add another bitfield differentiated only by its
     * version. This method will only set the bits of {@link E} flags with 
     * version 1. 
     * @param <E> the type of {@link java.lang.Enum}; this type must implement 
     * the {@link Flag} interface
     * @param flags the set of {@link E} flags
     * @return the bit field as a 32-bit integer
     */
    public static final <E extends Enum<E> & Flag<Integer>> int 
        toInt(final Set<E> flags) 
    {
        return toInt(flags, 1);
    }

    /**
     * Converts the {@link E} objects of the specified version in the given set 
     * to a 32-bit bitfield. 
     * If there are more flags than a datatype has bits, a popular scheme in 
     * native APIs has been to add another bitfield differentiated only by its
     * version.
     * @param <E> the type of {@link E}; this type must implement 
     * the {@link Flag} interface
     * @param flags the set of {@link E} flags
     * @param version the version of the flag bits to set
     * @return the bit field as a 32-bit integer
     */
    public static final <E extends Enum<E> & Flag<Integer>> int 
        toInt(final Set<E> flags, final int version) 
    {
        int bitField = 0;
        for(E flag : flags)
            bitField |= flag.value();
        return bitField;
    }
    
    /**
     * Converts one or more 32-bit bitfields to a set of {@link E} objects.
     * The position of the bitfield incremented by one determines its version.
     * Insert null in the positions of the bitfields you do not want to convert.
     * @param <E> the type of {@link java.lang.Enum}; this type must implement 
     * the {@link Flag} interface
     * @param c the class token of the type of {@link E}
     * @param bitFields a bitfield for each version of the flag bits
     * @return a new set of {@link E} flags
     */
    public static final <E extends Enum<E> & Flag<Integer>> EnumSet<E> 
        toSet(final Class<E> c, final int... bitFields) 
    {
        if (c == null)
            throw new IllegalArgumentException("No class has been specified.");
        if (bitFields == null)
            throw new IllegalArgumentException("No bit field has been specified.");
        
        EnumSet<E> flags = EnumSet.noneOf(c);
        for(E flag : c.getEnumConstants()) {
            for(int i = 0; i < bitFields.length; i++) {
                if (flag.version() != (i+1))
                    continue;
                if (Bits.isSet(bitFields[i], flag.value()))
                    flags.add(flag);
            }
        }
        return flags;
    }
       
    /**
     * Converts the given set of {@link E} objects to a 64-bit bitfield.
     * If there are more flags than a datatype has bits, a popular scheme in 
     * native APIs has been to add another bitfield differentiated only by its
     * version. This method will only set the bits of {@link E} flags with 
     * version 1. 
     * @param <E> the type of {@link java.lang.Enum}; this type must implement 
     * the {@link Flag} interface
     * @param flags the set of {@link E} flags
     * @return the bit field as a 64-bit integer
     */
    public static final <E extends Enum<E> & Flag<Long>> long 
        toLong(final Set<E> flags) 
    {
        return toLong(flags, 1);
    }
 
    /**
     * Converts the {@link E} objects of the specified version in the given set
     * to a 64-bit bitfield. 
     * If there are more flags than a datatype has bits, a popular scheme in 
     * native APIs has been to add another bitfield differentiated only by its
     * version.
     * @param <E> the type of {@link java.lang.Enum}; this type must implement 
     * the {@link Flag} interface
     * @param flags the set of {@link E} flags
     * @param version the version of the flag bits to set
     * @return the bit field as a 64-bit integer
     */
    public static final <E extends Enum<E> & Flag<Long>> long 
        toLong(final Set<E> flags, final int version) 
    {
        long bitField = 0;
        for(E flag : flags)
            if (flag.version() == version)
                bitField |= flag.value();
        
        return bitField;
    }
    
    /**
     * Converts one or more 64-bit bitfields to a set of {@link E} objects.
     * The position of the bitfield incremented by one determines its version.
     * Insert null in the positions of the bitfields you do not want to convert.
     * @param <E> the type of {@link java.lang.Enum}; this type must implement 
     * the {@link Flag} interface
     * @param c the class token of the type of {@link E}
     * @param bitFields a bitfield for each version of the flag bits
     * @return a new set of {@link E} flags
     */
    public static final <E extends Enum<E> & Flag<Long>> EnumSet<E> 
        toSet(final Class<E> c, final long... bitFields) 
    {
        EnumSet<E> flags = EnumSet.noneOf(c);
        for(E flag : c.getEnumConstants()) {
            for(int i = 0; i < bitFields.length; i++) {
                if (flag.version() != (i+1))
                    continue;
                if (Bits.isSet(bitFields[i], flag.value()))
                    flags.add(flag);
            }
        }
        return flags;
    }
       
    private Enums() { }
}