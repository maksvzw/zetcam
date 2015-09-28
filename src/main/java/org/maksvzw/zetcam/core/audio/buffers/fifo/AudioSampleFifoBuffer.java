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
package org.maksvzw.zetcam.core.audio.buffers.fifo;

import com.xuggle.xuggler.IAudioSamples;
import java.nio.BufferUnderflowException;
import org.maksvzw.zetcam.core.audio.Audio;
import org.maksvzw.zetcam.core.audio.AudioFormat;

/**
 *
 * @author Lenny Knockaert
 * @param <T>
 */
public abstract class AudioSampleFifoBuffer<T extends Number & Comparable<T>>
    implements AutoCloseable
{
    public static final AudioSampleFifoBuffer<? extends Number> allocate(
            final AudioFormat audioFormat,
            final int numOfSamples) 
    {
        return allocate(audioFormat, numOfSamples, 0);
    }
    
    public static final AudioSampleFifoBuffer<? extends Number> allocate(
            final AudioFormat audioFormat, 
            final int numOfSamples, 
            final long ptsInSamples) 
    {
        final IAudioSamples samples = Audio.allocateSamples(
                audioFormat, numOfSamples, ptsInSamples);
        
        switch(samples.getData().getType()) {
            case IBUFFER_UINT8:
            case IBUFFER_SINT8:
                return new ByteSampleFifoBuffer(audioFormat, samples);
            case IBUFFER_UINT16:
            case IBUFFER_SINT16:
                return new ShortSampleFifoBuffer(audioFormat, samples);
            case IBUFFER_UINT32:
            case IBUFFER_SINT32:
                return new IntegerSampleFifoBuffer(audioFormat, samples);
            case IBUFFER_FLT32:
                return new FloatSampleFifoBuffer(audioFormat, samples);
            case IBUFFER_DBL64:
                return new DoubleSampleFifoBuffer(audioFormat, samples);
            default:
                samples.delete();
                throw new UnsupportedOperationException();
        }
    }
    
    private final AudioFormat format;
    private IAudioSamples samples;
    private long head;
    private long tail;
    private boolean isClosed;
    
    protected AudioSampleFifoBuffer(AudioFormat format, IAudioSamples samples) 
    {
        if (format == null)
            throw new IllegalArgumentException("No audio format has been specified.");
        if (samples == null)
            throw new IllegalArgumentException("No audio samples have been specified.");
        if (!samples.isComplete())
            throw new IllegalArgumentException("No valid audio samples have been specified.");
        
        this.format = format;
        this.samples = samples;
        this.clear();
        this.isClosed = false;
    }
    
    /**
     * Determines whether or not this audio FIFO buffer is closed.
     * This audio FIFO buffer is closed once close() has been called. 
     * Any subsequent method call after this stream has been closed 
     * will throw an {@link java.lang.IllegalStateException}.
     * @return true if this audio FIFO buffer is closed.
     */
    public final boolean isClosed() {
        return this.isClosed;
    }
    
    /**
     * Gets the audio format of this buffer.
     * @return the audio format of this buffer
     */
    public final AudioFormat getFormat() {
        return this.format;
    }
    
    /**
     * Gets the capacity of this audio FIFO buffer, in samples (per channel).
     * @return the capacity of this audio FIFO buffer, in samples (per channel)
     */
    public final int capacity() {
        return (int)this.samples.getNumSamples() - 1;
    }
    
     /**
     * Gets the delay of this audio FIFO buffer, in samples (per channel).
     * If the delay is smaller than zero, this buffer resides in a state of 
     * underrun and <code>n = Math.abs(this.delay())</code> samples (per 
     * channel) have been read that weren't available.
     * <p>
     * If the delay is zero, this buffer is empty. If the delay is greater than 
     * zero and smaller than the capacity of this buffer, there are <code>n = 
     * this.delay()</code> number of samples that are available for reading and
     * <code>n = this.capacity() - this.delay()</code> samples that are available 
     * for writing.
     * </p><p>
     * If the delay is equal to or greater than the capacity of this buffer, 
     * this buffer resides in a state of overrun and <code>n = this.delay() -
     * this.capacity()</code> samples have been overwritten.
     * </p>
     * @return the delay of this audio FIFO buffer, in samples (per channel)
     */
    public int delay() {
        return (int)((this.tail - this.head) / this.samples.getChannels());
    }
    
    public final AudioSampleFifoBuffer<T> get(IAudioSamples dst, int index, int length) 
    {
        this.checkAudioSamples(dst, index, length);

        if (this.delay() < length)
            throw new BufferUnderflowException();
        
        if (length > 0) {
            /* IAudioSamples and IBuffer classes only accommodate interleaved formats;
            apparently Xuggler interleaves planar formats on decoding/encoding. The 
            index and length arguments are expected to be in samples per channel 
            as expected of all public interfaces so we have to convert them. */
            final int dstIndex = index * dst.getChannels(), 
                    dstLength = length * dst.getChannels(),
                    srcIndex = (int)(this.head % (this.capacity() * this.samples.getChannels()));

            this.get(this.samples, srcIndex, dst, dstIndex, dstLength);
            this.head += dstLength;
        }
        return this;
    }
    
    protected abstract void get(IAudioSamples src, int srcIndex, IAudioSamples dst, int dstIndex, int length);
    
    public final AudioSampleFifoBuffer<T> put(IAudioSamples src, int index, int length) 
    {
        this.checkAudioSamples(src, index, length);
        
        /* IAudioSamples and IBuffer classes only accommodate interleaved formats;
        apparently Xuggler interleaves planar formats on decoding/encoding. The 
        index and length arguments are expected to be in samples per channel 
        as expected of all public interfaces so we have to convert them. */
        final int srcIndex = index * src.getChannels(), 
                srcLength = length * src.getChannels(),
                dstIndex = (int)(this.tail % (this.capacity() * this.samples.getChannels()));
        
        final int delay = this.delay(), capacity = this.capacity();
        if ((delay + length) > capacity) {
            /* Choose the size of new buffer. Either a standard doubling of the 
            size or a size large enough to satisfy the pending write, whichever 
            is larger. */
            final long numOfSamples = Math.max((capacity * 2) + 1, capacity + length + 1);
            
            /* Create larger buffer of samples. */
            final IAudioSamples newSamples = Audio.allocateSamples(
                    this.format, numOfSamples, 0);
            
            /* Copy remaining samples from this buffer to the new buffer. */
            this.get(newSamples, 0, delay);
            /* Release the old buffer. */
            this.samples.delete();
            /* Replace the old buffer with the new buffer. */
            this.samples = newSamples;
            /* Reset read and write counters. */
            this.head = 0;
            this.tail = delay * this.samples.getChannels();
        }

        this.put(this.samples, dstIndex, src, srcIndex, srcLength);
        this.tail += srcLength;
        return this;
    }
    
    protected abstract void put(IAudioSamples dst, int dstIndex, IAudioSamples src, int srcIndex, int length);
    
    private void checkAudioSamples(IAudioSamples samples, int index, int length) 
    {
        if (samples == null)
            throw new IllegalArgumentException("No audio samples have been specified.");
        
        if (this.samples.getFormat() != samples.getFormat() || 
            this.samples.getChannels() != samples.getChannels() || 
            this.samples.getSampleRate() != samples.getSampleRate())
            throw new IllegalArgumentException("Incompatible audio samples are specified.");
        
        final long numOfSamples = samples.getNumSamples();
        if (index < 0 || index > numOfSamples)
            throw new IndexOutOfBoundsException("Index: "+index+"; Size: "+numOfSamples);
        if (length < 0)
            throw new IndexOutOfBoundsException("Index: "+length+"; Size: "+numOfSamples);
        if ((index + length) > numOfSamples)
            throw new IndexOutOfBoundsException("Index: "+(index + length)+"; Size: "+numOfSamples);
    }
    
    public AudioSampleFifoBuffer<T> clear()
    {
        this.head = this.tail = 0;
        return this;
    }
    
    /**
     * Closes this audio FIFO buffer.
     * <p>
     * This method has to be called if, and only if, this buffer is direct in 
     * order to release its unmanaged resources.
     * </p><p>
     * Any subsequent method call after this audio buffer has been closed will 
     * throw an {@link java.lang.IllegalStateException}.
     * <p>
     */
    @Override
    public void close() 
    {
        if (this.isClosed)
            return; 
        
        this.clear();
        this.samples.delete();
        this.isClosed = true;
    }
}