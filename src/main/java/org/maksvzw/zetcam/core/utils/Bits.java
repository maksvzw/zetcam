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

import java.nio.ByteOrder;

/**
 * Bit manipulation utilities class.
 * 
 * @author Lenny Knockaert
 */
public final class Bits 
{
    private static final ByteOrder NATIVE_ORDER = ByteOrder.nativeOrder();

    /**
     * Sets the n-th bit in the given bit field.
     * @param bitField the bit field to change
     * @param n the n-th bit [1, 32] to set
     * @return result of the bitwise operation
     */
    public static final int setBit(final int bitField, final int n) {
        return (bitField | (1 << (n-1)));
    }
    
    /**
     * Sets the n-th bit in the given bit field.
     * @param bitField the bit field to change
     * @param n the n-th bit [1, 64] to set
     * @return result of the bitwise operation
     */
    public static final long setBit(final long bitField, final int n) {
        return (bitField | (1 << (n-1)));
    }
    
    /**
     * Unsets the n-th bit in the given bit field.
     * @param bitField the bit field to change
     * @param n the n-th bit [1, 32] to unset
     * @return result of the bitwise operation
     */
    public static final int unsetBit(final int bitField, final int n) {
        return (bitField & ~(1 << (n-1)));
    }
    
    /**
     * Unsets the n-th bit in the given bit field.
     * @param bitField the bit field to change
     * @param n the n-th bit [1, 64] to unset
     * @return result of the bitwise operation
     */
    public static final long unsetBit(final long bitField, final int n) {
        return (bitField & ~(1 << (n-1)));
    }
    
    /**
     * Determines whether the n-th bit is set in the given bit field. 
     * @param bitField the bit field to check
     * @param n the n-th bit [1, 32] to test
     * @return true if the n-th bit is set
     */
    public static final boolean isBitSet(final int bitField, final int n)
    {
        final int bit = 1 << (n-1);
        return (bitField & bit) == bit;
    }
    
    /**
     * Determines whether the n-th bit is set in the given bit field. 
     * @param bitField the bit field to check
     * @param n the n-th bit [1, 64] to test
     * @return true if the n-th bit is set
     */
    public static final boolean isBitSet(final long bitField, final int n) 
    {
        final int bit = 1 << (n-1);
        return (bitField & bit) == bit;
    }
    
    /**
     * Sets all bits of the given mask in the given bit field. 
     * @param bitField the bit field to change
     * @param mask the bit mask to set
     * @return the result of the bitwise operation
     */
    public static final int set(final int bitField, final int mask) {
        return (bitField | mask);
    }
    
    /**
     * Sets all bits of the given mask in the given bit field. 
     * @param bitField the bit field to change
     * @param mask the bit mask to set
     * @return the result of the bitwise operation
     */
    public static final long set(final long bitField, final long mask) {
        return (bitField | mask);
    }
    
    /**
     * Unsets all bits of the given mask in the given bit field. 
     * @param bitField the bit field to change
     * @param mask the bit mask to unset
     * @return the result of the bitwise operation
     */
    public static final int unset(final int bitField, final int mask) {
        return (bitField & ~mask);
    }
    
    /**
     * Unsets all bits of the given mask in the given bit field. 
     * @param bitField the bit field to change
     * @param mask the bit mask to unset
     * @return the result of the bitwise operation
     */
    public static final long unset(final long bitField, final long mask) {
        return (bitField & ~mask);
    }
    
    /**
     * Determines whether all bits of the given mask are set in the given 
     * bit field.
     * @param bitField the bit field to check
     * @param mask the bit mask to test
     * @return true if all bits of the given mask are set
     */
    public static final boolean isSet(final int bitField, final int mask) {
        return (bitField & mask) == mask;
    }
    
    /**
     * Determines whether all bits of the given mask are set in the given 
     * bit field.
     * @param bitField the bit field to check
     * @param mask the bit mask to test
     * @return true if all bits of the given mask are set
     */
    public static final boolean isSet(final long bitField, final long mask) {
        return (bitField & mask) == mask;
    }
    
    /**
     * Reads the first two bytes of the given byte array as a 16-bit integer.
     * This method uses the native byte order to interpret multi-byte values. 
     * @param bytes the byte array to read from
     * @return a 16-bit integer
     */
    public static final short getShort(final byte[] bytes) {
        return getShort(bytes, 0, NATIVE_ORDER);
    }
    
    /**
     * Reads the next two bytes of the given byte array as a 16-bit integer.
     * This method uses the native byte order to interpret multi-byte values.
     * @param bytes the byte array to read from
     * @param offset the offset in the byte array to start reading
     * @return a 16-bit integer
     */
    public static final short getShort(final byte[] bytes, final int offset) {
        return getShort(bytes, offset, NATIVE_ORDER);
    }
    
    /**
     * Reads the next two bytes of the given byte array as a 16-bit integer.
     * @param bytes the byte array to read from
     * @param offset the offset in the byte array to start reading
     * @param order the order of the byte array 
     * @return a 16-bit integer
     */
    public static final short getShort(final byte[] bytes, final int offset, 
            final ByteOrder order)
    {
        if (order == ByteOrder.LITTLE_ENDIAN)
            return getShortL(bytes, offset);
        else 
            return getShortB(bytes, offset);
    }
    
    private static short getShortL(final byte[] bytes, final int offset)
    {
        return (short)((bytes[offset+1] << 8)   | 
                        (bytes[offset] & 0xFF));
    }
    
    private static short getShortB(final byte[] bytes, final int offset)
    {
        return (short)((bytes[offset] << 8)     | 
                        (bytes[offset+1] & 0xFF));
    }
    
    /**
     * Writes a 16-bit integer to the first two bytes of the given byte array.
     * This method uses the native byte order to translate multi-byte values.
     * @param value the 16-bit integer to set
     * @param bytes the byte array to write to
     */
    public static final void setShort(final short value, final byte[] bytes) {
        setShort(value, bytes, 0, NATIVE_ORDER);
    }
    
    /**
     * Writes a 16-bit integer to the next two bytes of the given byte array.
     * This method uses the native byte order to translate multi-byte values.
     * @param value the 16-bit integer to set
     * @param bytes the byte array to write to
     * @param offset the offset in the byte array to start writing
     */
    public static final void setShort(final short value, final byte[] bytes, final int offset) {
        setShort(value, bytes, offset, NATIVE_ORDER);
    }
    
    /**
     * Writes a 16-bit integer to the next two bytes of the given byte array.
     * @param value the 16-bit integer to set
     * @param bytes the byte array to write to
     * @param offset the offset in the byte array to start writing
     * @param order the order of the byte array
     */
    public static final void setShort(final short value, final byte[] bytes, 
            final int offset, final ByteOrder order) 
    {
        if (order == ByteOrder.LITTLE_ENDIAN)
            setShortL(value, bytes, offset);
        else 
            setShortB(value, bytes, offset);
    }
    
    private static void setShortL(final short value, final byte[] bytes, final int offset) 
    {
        bytes[offset+1] = (byte)(value >> 8);
        bytes[offset] = (byte)value;
    }
    
    private static void setShortB(final short value, final byte[] bytes, final int offset) 
    {
        bytes[offset] = (byte)(value >> 8);
        bytes[offset+1] = (byte)value;
    }
    
    /**
     * Reads the first four bytes of the given byte array as a 32-bit integer.
     * This method uses the native byte order to interpret multi-byte values.
     * @param bytes the byte array to read from
     * @return a 32-bit integer
     */
    public static final int getInt(final byte[] bytes) {
        return getInt(bytes, 0, NATIVE_ORDER);
    }

    /**
     * Reads the next four bytes of the given byte array as a 32-bit integer.
     * This method uses the native byte order to interpret multi-byte values.
     * @param bytes the byte array to read from
     * @param offset the offset in the byte array to start reading
     * @return a 32-bit integer
     */
    public static final int getInt(final byte[] bytes, final int offset) {
        return getInt(bytes, offset, NATIVE_ORDER);
    }
    
    /**
     * Reads the next four bytes of the given byte array as a 32-bit integer.
     * @param bytes the byte array to read from
     * @param offset the offset in the byte array to start reading
     * @param order the order of the byte array
     * @return a 32-bit integer
     */
    public static final int getInt(final byte[] bytes, final int offset, 
            final ByteOrder order) 
    {
        if (order == ByteOrder.LITTLE_ENDIAN)
            return getIntL(bytes, offset);
        else 
            return getIntB(bytes, offset);
    }
    
    private static int getIntL(final byte[] bytes, final int offset)
    {
        return ((bytes[offset+3]            << 24)  | 
                ((bytes[offset+2] & 0xFF)   << 16)  |
                ((bytes[offset+1] & 0xFF)   << 8)   | 
                (bytes[offset] & 0xFF)          );
    }
    
    private static int getIntB(final byte[] bytes, final int offset) 
    {
        return ((bytes[offset]              << 24)  | 
                ((bytes[offset+1] & 0xFF)   << 16)  |
                ((bytes[offset+2] & 0xFF)   << 8)   | 
                (bytes[offset+3] & 0xFF)        );
    }
    
    /**
     * Writes a 32-bit integer to the first four bytes of the given byte array.
     * This method uses the native byte order to translate multi-byte values.
     * @param value the 32-bit integer to set
     * @param bytes the byte array to write to
     */
    public static final void setInt(final int value, final byte[] bytes) {
        setInt(value, bytes, 0, NATIVE_ORDER);
    }
    
    /**
     * Writes a 32-bit integer to the next four bytes of the given byte array.
     * This method uses the native byte order to translate multi-byte values.
     * @param value the 32-bit integer to set
     * @param bytes the byte array to write to
     * @param offset the offset in the byte array to start writing
     */
    public static final void setInt(final int value, final byte[] bytes, final int offset) {
        setInt(value, bytes, offset, NATIVE_ORDER);
    }
    
    /**
     * Writes a 32-bit integer to the next four bytes of the given byte array.
     * @param value the 32-bit integer to set
     * @param bytes the byte array to write to
     * @param offset the offset in the byte array to start writing
     * @param order the order of the byte array
     */
    public static final void setInt(final int value, final byte[] bytes, 
            final int offset, final ByteOrder order) 
    {
        if (order == ByteOrder.LITTLE_ENDIAN)
            setIntL(value, bytes, offset);
        else 
            setIntB(value, bytes, offset);
    }
    
    private static void setIntL(final int value, final byte[] bytes, final int offset) 
    {
        bytes[offset+3] = (byte)(value >> 24);
        bytes[offset+2] = (byte)(value >> 16);
        bytes[offset+1] = (byte)(value >> 8);
        bytes[offset] = (byte)value;
    }
    
    private static void setIntB(final int value, final byte[] bytes, final int offset) 
    {
        bytes[offset] = (byte)(value >> 24);
        bytes[offset+1] = (byte)(value >> 16);
        bytes[offset+2] = (byte)(value >> 8);
        bytes[offset+3] = (byte)value;
    }
    
    /**
     * Reads the first eight bytes of the given byte array as a 64-bit integer.
     * This method uses the native byte order to interpret multi-byte values.
     * @param bytes the byte array to read from
     * @return a 64-bit integer
     */
    public static final long getLong(final byte[] bytes) {
        return getLong(bytes, 0, NATIVE_ORDER);
    }
    
    /**
     * Reads the next eight bytes of the given byte array as a 64-bit integer.
     * This method uses the native byte order to interpret multi-byte values.
     * @param bytes the byte array to read from
     * @param offset the offset in the byte array to start reading
     * @return a 64-bit integer
     */
    public static final long getLong(final byte[] bytes, final int offset) {
        return getLong(bytes, offset, NATIVE_ORDER);
    }
    
    /**
     * Reads the next eight bytes of the given byte array as a 64-bit integer.
     * @param bytes the byte array to read from
     * @param offset the offset in the byte array to start reading
     * @param order the order of the byte array
     * @return a 64-bit integer
     */
    public static final long getLong(final byte[] bytes, final int offset, 
            final ByteOrder order)
    {
        if (order == ByteOrder.LITTLE_ENDIAN)
            return getLongL(bytes, offset);
        else 
            return getLongB(bytes, offset);
    }
    
    private static long getLongL(final byte[] bytes, final int offset) 
    {
        return (((long)bytes[offset+7]          << 56)  | 
                (((long)bytes[offset+6] & 0xFF) << 48)  |
                (((long)bytes[offset+5] & 0xFF) << 40)  | 
                (((long)bytes[offset+4] & 0xFF) << 32)  |
                (((long)bytes[offset+3] & 0xFF) << 24)  |
                (((long)bytes[offset+2] & 0xFF) << 16)  |
                (((long)bytes[offset+1] & 0xFF) << 8)   |
                ((long)bytes[offset] & 0xFF)        );
    }
    
    private static long getLongB(final byte[] bytes, final int offset) 
    {
        return (((long)bytes[offset]            << 56)  | 
                (((long)bytes[offset+1] & 0xFF) << 48)  |
                (((long)bytes[offset+2] & 0xFF) << 40)  | 
                (((long)bytes[offset+3] & 0xFF) << 32)  |
                (((long)bytes[offset+4] & 0xFF) << 24)  |
                (((long)bytes[offset+5] & 0xFF) << 16)  |
                (((long)bytes[offset+6] & 0xFF) << 8)   |
                ((long)bytes[offset+7] & 0xFF)      );
    }
    
    /**
     * Writes a 64-bit integer to the first eight bytes of the given byte array.
     * This method uses the native byte order to translate multi-byte values.
     * @param value the 64-bit integer to set
     * @param bytes the byte array to write to
     */
    public static final void setLong(final long value, final byte[] bytes) {
        setLong(value, bytes, 0, NATIVE_ORDER);
    }
    
    /**
     * Writes a 64-bit integer to the next eight bytes of the given byte array.
     * This method uses the native byte order to translate multi-byte values.
     * @param value the 64-bit integer to set
     * @param bytes the byte array to write to
     * @param offset the offset in the byte array to start writing
     */
    public static final void setLong(final long value, final byte[] bytes, final int offset) {
        setLong(value, bytes, offset, NATIVE_ORDER);
    }
    
    /**
     * Writes a 64-bit integer to the next eight bytes of the given byte array.
     * @param value the 64-bit integer to set
     * @param bytes the byte array to write to
     * @param offset the offset in the byte array to start writing
     * @param order the byte order of the array
     */
    public static final void setLong(final long value, final byte[] bytes, 
            final int offset, final ByteOrder order) 
    {
        if (order == ByteOrder.LITTLE_ENDIAN)
            setLongL(value, bytes, offset);
        else 
            setLongB(value, bytes, offset);
    }
    
    private static void setLongL(final long value, final byte[] bytes, final int offset) 
    {
        bytes[offset+7] = (byte)(value >> 56);
        bytes[offset+6] = (byte)(value >> 48);
        bytes[offset+5] = (byte)(value >> 40);
        bytes[offset+4] = (byte)(value >> 32);
        bytes[offset+3] = (byte)(value >> 24);
        bytes[offset+2] = (byte)(value >> 16);
        bytes[offset+1] = (byte)(value >> 8);
        bytes[offset] = (byte)value;
    }
    
    private static void setLongB(final long value, final byte[] bytes, final int offset) 
    {
        bytes[offset] = (byte)(value >> 56);
        bytes[offset+1] = (byte)(value >> 48);
        bytes[offset+2] = (byte)(value >> 40);
        bytes[offset+3] = (byte)(value >> 32);
        bytes[offset+4] = (byte)(value >> 24);
        bytes[offset+5] = (byte)(value >> 16);
        bytes[offset+6] = (byte)(value >> 8);
        bytes[offset+7] = (byte)value;
    }
    
    /**
     * Reads the first four bytes of the given byte array as a 32-bit floating 
     * point value.
     * This method uses the native byte order to interpret multi-byte values.
     * @param bytes the byte array to read from
     * @return a 32-bit floating point
     */
    public static final float getFloat(final byte[] bytes) {
        return getFloat(bytes, 0, NATIVE_ORDER);
    }
    
    /**
     * Reads the next four bytes of the given byte array as a 32-bit floating 
     * point value.
     * This method uses the native byte order to interpret multi-byte values.
     * @param bytes the byte array to read from
     * @param offset the offset in the byte array to start reading
     * @return a 32-bit floating point
     */
    public static final float getFloat(final byte[] bytes, final int offset) {
        return getFloat(bytes, offset, NATIVE_ORDER);
    }
    
    /**
     * Reads the next four bytes of the given byte array as a 32-bit floating 
     * point value.
     * @param bytes the byte array to read from
     * @param offset the offset in the byte array to start reading
     * @param order the order of the byte array
     * @return a 32-bit floating point
     */
    public static final float getFloat(final byte[] bytes, final int offset, 
            final ByteOrder order) 
    {
        if (order == ByteOrder.LITTLE_ENDIAN)
            return Float.intBitsToFloat(getIntL(bytes, offset));
        else 
            return Float.intBitsToFloat(getIntB(bytes, offset));
    }
    
    /**
     * Writes a 32-bit floating point value to the first four bytes of the given
     * byte array.
     * This method uses the native byte order to translate multi-byte values.
     * @param value the 32-bit floating point value to set
     * @param bytes the byte array to write to
     */
    public static final void setFloat(final float value, final byte[] bytes) {
        setFloat(value, bytes, 0, NATIVE_ORDER);
    }
    
    /**
     * Writes a 32-bit floating point value to the next four bytes of the given
     * byte array.
     * This method uses the native byte order to translate multi-byte values.
     * @param value the 32-bit floating point value to set
     * @param bytes the byte array to write to
     * @param offset the offset in the byte array to start writing
     */
    public static final void setFloat(final float value, final byte[] bytes, final int offset) {
        setFloat(value, bytes, offset, NATIVE_ORDER);
    }
    
    /**
     * Writes a 32-bit floating point value to the next four bytes of the given
     * byte array.
     * @param value the 32-bit floating point value to set
     * @param bytes the byte array to write to
     * @param offset the offset in the byte array to start writing
     * @param order the byte order of the array
     */
    public static final void setFloat(final float value, final byte[] bytes, 
            final int offset, final ByteOrder order) 
    {
        if (order == ByteOrder.LITTLE_ENDIAN)
            setIntL(Float.floatToRawIntBits(value), bytes, offset);
        else 
            setIntB(Float.floatToRawIntBits(value), bytes, offset);
    }
    
    /**
     * Reads the first eight bytes of the given byte array as a 64-bit floating 
     * point value.
     * This method uses the native byte order to interpret multi-byte values.
     * @param bytes the byte array to read from
     * @return a 64-bit floating point
     */
    public static final double getDouble(final byte[] bytes) {
        return getDouble(bytes, 0, NATIVE_ORDER);
    }
    
    /**
     * Reads the next eight bytes of the given byte array as a 64-bit floating 
     * point value.
     * This method uses the native byte order to interpret multi-byte values.
     * @param bytes the byte array to read from
     * @param offset the offset in the byte array to start reading
     * @return a 64-bit floating point
     */
    public static final double getDouble(final byte[] bytes, final int offset) {
        return getDouble(bytes, offset, NATIVE_ORDER);
    }
    
    /**
     * Reads the next eight bytes of the given byte array as a 64-bit floating 
     * point value.
     * @param bytes the byte array to read from
     * @param offset the offset in the byte array to start reading
     * @param order the byte order of the array
     * @return a 64-bit floating point
     */
    public static final double getDouble(final byte[] bytes, final int offset, 
            final ByteOrder order) 
    {
        if (order == ByteOrder.LITTLE_ENDIAN)
            return Double.longBitsToDouble(getLongL(bytes, offset));
        else 
            return Double.longBitsToDouble(getLongB(bytes, offset));
    }
    
    /**
     * Writes a 64-bit floating point value to the next four bytes of the given
     * byte array.
     * This method uses the native byte order to translate multi-byte values.
     * @param value the 64-bit floating point value to set
     * @param bytes the byte array to write to
     */
    public static final void setDouble(final double value, final byte[] bytes) {
        setDouble(value, bytes, 0, NATIVE_ORDER);
    }
    
    /**
     * Writes a 64-bit floating point value to the next four bytes of the given
     * byte array.
     * This method uses the native byte order to translate multi-byte values.
     * @param value the 64-bit floating point value to set
     * @param bytes the byte array to write to
     * @param offset the offset in the byte array to start writing
     */
    public static final void setDouble(final double value, final byte[] bytes, final int offset) {
        setDouble(value, bytes, offset, NATIVE_ORDER);
    }
    
    /**
     * Writes a 64-bit floating point value to the next four bytes of the given
     * byte array.
     * @param value the 64-bit floating point value to set
     * @param bytes the byte array to write to
     * @param offset the offset in the byte array to start writing
     * @param order the order of the byte array
     */
    public static final void setDouble(final double value, final byte[] bytes, 
            final int offset, final ByteOrder order) 
    {
        if (order == ByteOrder.LITTLE_ENDIAN)
            setLongL(Double.doubleToRawLongBits(value), bytes, offset);
        else 
            setLongB(Double.doubleToRawLongBits(value), bytes, offset);
    }
    
    private Bits() { }
}