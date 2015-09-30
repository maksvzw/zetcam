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
package org.maksvzw.zetcam.core.audio;

import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IAudioSamples.Format;
import java.io.Serializable;
import java.nio.ByteOrder;
import java.util.Objects;

/**
 * This class specifies the format for raw, uncompressed PCM audio. 
 * 
 * @author Lenny
 */
public final class AudioFormat implements Serializable
{
    public static final AudioFormat of(final IAudioSamples samples) 
    {
        return new AudioFormat(
                samples.getSampleRate(), 
                samples.getFormat(), 
                samples.getChannels());
    }
    
    private final int sampleRate;
    private final Format sampleFormat;
    private final int sampleSize;
    private final int channels;
    private final boolean isPlanar;
    
    public AudioFormat(final int sampleRate, final Format sampleFormat, final int channels) 
    {
        if (sampleRate < 8000)
            throw new IllegalArgumentException("The specified sample rate cannot be smaller than 8000 Hz.");
        if (sampleFormat == null || sampleFormat == Format.FMT_NONE)
            throw new IllegalArgumentException("No sample format has been specified.");
        if (channels < 1)
            throw new IllegalArgumentException("The specified channel count cannot be smaller than one.");
        
        this.sampleRate = sampleRate;
        this.sampleFormat = sampleFormat;
        this.channels = channels;
        
        int bytes = 0;
        boolean planar = false;
        switch(sampleFormat) {
            case FMT_U8P:
                planar = true;
            case FMT_U8:
                bytes = 1;
                break;
            case FMT_S16P:
                planar = true;
            case FMT_S16:
                bytes = 2;
                break;
            case FMT_S32P:
            case FMT_FLTP:
                planar = true;
            case FMT_S32:
            case FMT_FLT:
                bytes = 4;
                break;
            case FMT_DBLP:
                planar = true;
            case FMT_DBL:
                bytes = 8;
                break;
        }
        
        this.sampleSize = bytes;
        this.isPlanar = planar;
    }
    
    /**
     * Gets the number of samples played or recorded per channel per second.
     * @return the number of samples played or recorded per channel per second
     */
    public int getSampleRate() {
        return this.sampleRate;
    }
    
    /**
     * Gets the sample format.
     * The sample format determines not only the size of each sample but also 
     * the arrangement of the samples.
     * @return the sample format
     */
    public Format getSampleFormat() {
        return this.sampleFormat;
    }

    /**
     * Gets the number of bytes in each sample.
     * @return the number of bytes in each sample
     */
    public int getSampleSize() {
        return this.sampleSize;
    }
    
    /**
     * Gets the number of audio channels.
     * @return the number of audio channels
     */
    public int getChannels() {
        return this.channels;
    }
    
    /**
     * Gets the number of sample frames played or recorded per second.
     * A sample frame consists of one sample per channel and since the sample 
     * rate is also defined per audio channel, this method will simply return 
     * the sample rate.
     * @return the number of sample frames played or recorded per second
     */
    public int getFrameRate() {
        return this.sampleRate;
    }
    
    /**
     * Gets the number of bytes in each sample frame.
     * A sample frame consists of a one sample per audio channel, regardless of 
     * whether the samples are interleaved or not.
     * @return the number of bytes in each sample frame
     */
    public int getFrameSize() {
        return this.sampleSize * this.channels;
    }
    
    /**
     * Determines whether each audio channel has a plane of samples or whether 
     * the samples for each audio channel are interleaved on a single plane. 
     * In the former case, a sample frame consists of each sample on the same 
     * position on each plane. In the latter case, a sample frame consists of a
     * series of consecutive samples for each channel on a single plane.
     * @return true if each audio channel has a plane of samples
     */
    public boolean isPlanar() {
        return this.isPlanar;
    }
    
    /**
     * Gets the number of bits played or recorded per second.
     * This method will return the bit rate of the raw, uncompressed PCM data
     * and shouldn't be confused with the bit rate of encoded PCM data.
     * @return the number of bits played or recorded per second
     */
    public int getBitRate() {
        return (this.sampleSize * 8) * this.channels * this.sampleRate;
    }
    
    /**
     * Determines the bit endianness of the samples. 
     * @return true if the samples are big-endian 
     */
    public boolean isBigEndian() {
        /* FFmpeg always employs the native endianness when decoding or encoding. */
        return ByteOrder.BIG_ENDIAN.equals(ByteOrder.nativeOrder());
    }
    
    /**
     * Gets the audio format for Java's sound system.
     * @return the audio format for Java's sound system
     */
    public javax.sound.sampled.AudioFormat toJavaFormat() 
    {
        javax.sound.sampled.AudioFormat.Encoding encoding;
        switch(this.sampleFormat) 
        {
            case FMT_U8P:
            case FMT_U8:
                encoding = javax.sound.sampled.AudioFormat.Encoding.PCM_UNSIGNED;
                break;
            case FMT_FLTP:
            case FMT_FLT:
            case FMT_DBLP:
            case FMT_DBL:
                encoding = javax.sound.sampled.AudioFormat.Encoding.PCM_FLOAT;
                break;
           default:
                encoding = javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
                break;
        }

        return new javax.sound.sampled.AudioFormat(
                encoding,
                this.sampleRate,
                this.sampleSize * 8,
                this.channels,
                this.getFrameSize(),
                this.sampleRate,
                this.isBigEndian());
    }
    
    @Override
    public boolean equals(Object obj) 
    {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (this.getClass() != obj.getClass())
            return false;
        
        final AudioFormat format = (AudioFormat)obj;
        return this.sampleRate == format.sampleRate &&
                this.sampleFormat == format.sampleFormat &&
                this.channels == format.channels;
    }

    @Override
    public int hashCode() 
    {
        int hash = 7;
        hash = 71 * hash + this.sampleRate;
        hash = 71 * hash + Objects.hashCode(this.sampleFormat);
        hash = 71 * hash + this.channels;
        return hash;
    }
    
    @Override
    public String toString() 
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Sample rate:\t");
        builder.append(this.sampleRate);
        builder.append(System.lineSeparator());
        builder.append("Sample format:\t");
        builder.append(this.sampleFormat.name());
        builder.append(System.lineSeparator());
        builder.append("Sample size:\t");
        builder.append(this.sampleSize);
        builder.append(System.lineSeparator());
        builder.append("Channels:\t");
        builder.append(this.channels);
        builder.append(System.lineSeparator());
        builder.append("Planar:\t");
        builder.append(this.isPlanar);
        builder.append(System.lineSeparator());
        return builder.toString();
    }
}