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
public final class Maths 
{
    /**
     * Computes the greatest common divisor of a and b.
     * The the greatest common divisor is the largest positive integer that 
     * divides the values without a remainder.
     * @param a the first value
     * @param b the second value 
     * @return the greatest common divisor of a and b
     */
    public static final int gcd(int a, int b)
    {
        int temp;
        while (b > 0) {
            temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    
    /**
     * Computes the least common multiple of a and b.
     * The least common multiple is the smallest positive integer that is a 
     * multiple of both.
     * @param a the first value
     * @param b the second value 
     * @return the least common multiple of a and b
     */
    public static final long lcm(final int a, final int b)
    {
        return a * (b / gcd(a, b));
    }
    
    /**
     * Computes the greatest common divisor of a and b.
     * The the greatest common divisor is the largest positive integer that 
     * divides the values without a remainder.
     * @param a the first value
     * @param b the second value 
     * @return the greatest common divisor of a and b
     */
    public static final long gcd(long a, long b)
    {
        long tmp;
        while (b > 0) {
            tmp = b;
            b = a % b;
            a = tmp;
        }
        return a;
    }
    
    /**
     * Computes the least common multiple of a and b.
     * The least common multiple is the smallest positive integer that is a 
     * multiple of both.
     * @param a the first value
     * @param b the second value 
     * @return the least common multiple of a and b
     */
    public static final long lcm(final long a, final long b) {
        return a * (b / gcd(a, b));
    }
    
    /**
     * Clamps any type of value to the range defined by the given minimum and
     * maximum bounds.
     * @param <T> the type of {@link java.lang.Number} to clamp
     * @param val the value to clamp 
     * @param min the minimum bound 
     * @param max the maximum bound
     * @return the clamped value
     */
    public static final <T extends Number & Comparable> T clamp(
            final T val, 
            final T min, 
            final T max) 
    {
        if (val.compareTo(min) == -1) return min;
        if (val.compareTo(max) == 1) return max;
        return val;
    }
    
    /**
     * Determines whether how near b and c are to a.
     * @param a the value to test against
     * @param b the first value to test
     * @param c the second value to test
     * @return 1 if b is nearer to a than c, -1 if c is nearer and 0 if the 
     * distance for both values is the same
     */
    public static final int isNearer(final int a, final int b, final int c) 
    {
        int bDelta = Math.abs(a - b);
        int cDelta = Math.abs(a - c);
        if (bDelta < cDelta)
            return 1;
        if (bDelta > cDelta)
            return -1;
        return 0;
    }
    
    /**
     * Finds the value that is nearest to the preferred value in a sequence of 
     * values.
     * @param prefValue that value to test against
     * @param values the sequence of values to test
     * @return if found, a value of the given sequence that is nearest to the
     * preferred value; otherwise the preferred value is returned.
     */
    public static final int findNearest(final int prefValue, final int[] values) 
    {
        if (values.length == 0)
            return prefValue;
        
        int number, currDelta, minDelta;
        number = values[0];
        minDelta = Math.abs(number - prefValue);

        for(int i = 1; i < values.length; i++) {
            currDelta = Math.abs(values[i] - prefValue);
            if (currDelta < minDelta) {
                minDelta = currDelta;
                number = values[i];
            }
        }
        return number;
    }
    
    /**
     * Rounds the given value down to the nearest power of two.
     * @param x the value to round down
     * @return the given value rounded down to the nearest power of two
     */
    public static final int prevPow2(int x)
    {
        int power = 1;
        while ((x >>= 1) != 0) 
            power <<= 1;
        return power;
    }
    
    /**
     * Rounds the given value up to the nearest power of two.
     * @param x the value to round up
     * @return the given value rounded up to the nearest power of two
     */
    public static final int nextPow2(int x) 
    {
        int power = 2;
        while ((x >>= 1) != 0) 
            power <<= 1;
        return power;
    } 
    
    private static final float FLOAT_ANTI_DENORMAL = 1e-25f;

    /**
     * Zeroes out denormals in floating point values by adding and subtracting 
     * a small number. Implementation by Laurent de Soras.
     * <p>
     * For more information about this consult the IEEE 754 standard or visit 
     * https://www.securecoding.cert.org/confluence/display/java/NUM05-J.+Do+not+use+denormalized+numbers
     * </p>
     * @param x the floating point value to normalize
     * @return zero if, and only if, the value was a subnormal floating point 
     * value
     */
    public static final float normalizeToZero(float x) 
    {
        x += FLOAT_ANTI_DENORMAL;
        x -= FLOAT_ANTI_DENORMAL;
        return x;
    }
    
    /**
     * Performs linear interpolation.
     * @param a the first floating point value
     * @param b the second floating point value
     * @param x a value that linearly interpolates between a and b
     * @return the result of the linear interpolation
     */
    public static final float lerp(
            final float a, 
            final float b, 
            final float x) 
    {
        return (a + x) * (b - a);
    }
    
    /**
     * Performs cubic interpolation.
     * @param p1 the first floating point value
     * @param p2 the second floating point value
     * @param p3 the third floating point value
     * @param p4 the fourth floating point value
     * @param x a value that cubicly interpolates between p1..p4
     * @return the result of the cubic interpolation
     */
    public static final float curp(
            final float x, 
            final float p1, 
            final float p2,
            final float p3,
            final float p4) 
    {
        return p2 + 0.5f * x * (p3 - p1 +  
                x * (2.0f * p1 - 5.0f * p2 + 4.0f * p3 - p4 + 
                x * (3.0f * (p2 - p3) + p4 - p1)));
    }
    
    public static final float smoothStep(
            final float a, 
            final float b, 
            float x) 
    {
        if (x < a)
                return 0;
        if (x >= b)
                return 1;
        x = (x - a) / (b - a);
        return x*x * (3 - 2*x);
    }
    
    /**
     * Aligns the given value to the specified alignment offset.
     * This method rounds the given value up to the nearest value that is a 
     * multiple of the specified offset. 
     * <p>
     * Use this method to align buffer sizes to a supported word length of the 
     * processor or a word length that is suitable for all memory accesses. 
     * Alignment deals with the fact that some processors cannot address bytes 
     * individually from memory. If short, byte and integer values were stored
     * side by side in memory, then it would take 2 memory accesses and some bit
     * shifting to get the integer. E.g. | S1 S2 B1 I1 | I2 I3 I4 0 | 0 0 0 0 | ...
     * </p><p>
     * Fixing this problem requires padding of zero bits to properly align these
     * values. E.g. | S1 S2 B1 0 | I1 I2 I3 I4 | 0 0 0 0 | ... This method will
     * determine exactly how many bytes would be needed to include these padded
     * bits.
     * </p><p>
     * Modern general purpose computers generally have a word-size of either 4 
     * byte (32 bit) or 8 byte (64 bit). Classically, in early computers, memory
     * could only be addressed in words. This results in only being able to 
     * address memory at offsets which are multiples of the word-size. 
     * </p><p>
     * It should be noted, however, that modern processors do in fact have 
     * multiple word-sizes and can address memory down to individual bytes as 
     * well as up to at least their natural word size. Recent computers can 
     * operate on even larger memory chunks of 16 bytes and even a full cache 
     * line at once (typically 64 bytes) in a single operation using special 
     * instructions.
     * </p><p>
     * The consequences on processors that only support a single word-size 
     * (typically 4 bytes) vary. Some processors support it but at a steep 
     * performance cost of at least twice the aligned access time.
     * Other processors such as RISC processors like ARM will respond with an 
     * alignment fault.
     * </p>
     * @param x the value to align
     * @param a the offset to align to
     * @return the given value aligned to the given offset
     */
    public static final int align(final int x, final int a) {
        return (x + (a-1)) & ~(a-1);
    }
    
    /**
     * Aligns the given value to the specified alignment offset.
     * This method rounds the given value up to the nearest value that is a 
     * multiple of the specified offset. 
     * <p>
     * Use this method to align buffer sizes to a supported word length of the 
     * processor or a word length that is suitable for all memory accesses. 
     * Alignment deals with the fact that some processors cannot address bytes 
     * individually from memory. If short, byte and integer values were stored
     * side by side in memory, then it would take 2 memory accesses and some bit
     * shifting to get the integer. E.g. | S1 S2 B1 I1 | I2 I3 I4 0 | 0 0 0 0 | ...
     * </p><p>
     * Fixing this problem requires padding of zero bits to properly align these
     * values. E.g. | S1 S2 B1 0 | I1 I2 I3 I4 | 0 0 0 0 | ... This method will
     * determine exactly how many bytes would be needed to include these padded
     * bits.
     * </p><p>
     * Modern general purpose computers generally have a word-size of either 4 
     * byte (32 bit) or 8 byte (64 bit). Classically, in early computers, memory
     * could only be addressed in words. This results in only being able to 
     * address memory at offsets which are multiples of the word-size. 
     * </p><p>
     * It should be noted, however, that modern processors do in fact have 
     * multiple word-sizes and can address memory down to individual bytes as 
     * well as up to at least their natural word size. Recent computers can 
     * operate on even larger memory chunks of 16 bytes and even a full cache 
     * line at once (typically 64 bytes) in a single operation using special 
     * instructions.
     * </p><p>
     * The consequences on processors that only support a single word-size 
     * (typically 4 bytes) vary. Some processors support it but at a steep 
     * performance cost of at least twice the aligned access time.
     * Other processors such as RISC processors like ARM will respond with an 
     * alignment fault.
     * </p>
     * @param x the value to align
     * @param a the offset to align to
     * @return the given value aligned to the given offset
     */
    public static final long align(final long x, final int a) {
        return (x + (a-1)) & ~(a-1);
    }
    
    private Maths() { }
}