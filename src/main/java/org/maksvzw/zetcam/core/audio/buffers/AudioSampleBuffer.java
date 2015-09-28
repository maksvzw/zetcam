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
package org.maksvzw.zetcam.core.audio.buffers;

import com.xuggle.xuggler.IAudioSamples;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import org.maksvzw.zetcam.core.audio.Audio;
import org.maksvzw.zetcam.core.audio.AudioFormat;

/**
 *
 * @author Lenny Knockaert
 * @param <T>
 */
public abstract class AudioSampleBuffer<T extends Number & Comparable<T>>
    implements AutoCloseable
{
    public static final AudioSampleBuffer<? extends Number> wrap(final IAudioSamples samples) 
    {
        switch(samples.getData().getType()) 
        {
            case IBUFFER_UINT8:
                return new UInt8SampleBuffer(samples.copyReference());
            case IBUFFER_SINT8:
                return new Int8SampleBuffer(samples.copyReference());
            case IBUFFER_UINT16:
                return new UInt16SampleBuffer(samples.copyReference());
            case IBUFFER_SINT16:
                return new Int16SampleBuffer(samples.copyReference());
            case IBUFFER_UINT32:
                return new UInt32SampleBuffer(samples.copyReference());
            case IBUFFER_SINT32:
                return new Int32SampleBuffer(samples.copyReference());
            case IBUFFER_FLT32:
                return new FloatSampleBuffer(samples.copyReference());
            case IBUFFER_DBL64:
                return new DoubleSampleBuffer(samples.copyReference());
            default:
                throw new UnsupportedOperationException();
        }
    }
        
    public static final AudioSampleBuffer<? extends Number> allocate(
            final AudioFormat audioFormat, 
            final int numOfSamples)
    {
        return allocate(audioFormat, numOfSamples, 0);
    }
        
    public static final AudioSampleBuffer<? extends Number> allocate(
            final AudioFormat audioFormat,
            final int numOfSamples, 
            final long ptsInSamples)
    {
        final IAudioSamples samples = Audio.allocateSamples(
                audioFormat, numOfSamples, ptsInSamples);
        
        switch(samples.getData().getType()) {
            case IBUFFER_UINT8:
                return new UInt8SampleBuffer(samples).silenceAll().rewind();
            case IBUFFER_SINT8:
                return new Int8SampleBuffer(samples);
            case IBUFFER_UINT16:
                return new UInt16SampleBuffer(samples).silenceAll().rewind();
            case IBUFFER_SINT16:
                return new Int16SampleBuffer(samples);
            case IBUFFER_UINT32:
                return new UInt32SampleBuffer(samples).silenceAll().rewind();
            case IBUFFER_SINT32:
                return new Int32SampleBuffer(samples);
            case IBUFFER_FLT32:
                return new FloatSampleBuffer(samples);
            case IBUFFER_DBL64:
                return new DoubleSampleBuffer(samples);
            default:
                samples.delete();
                throw new UnsupportedOperationException();
        }
    }
    
    private final IAudioSamples samples;
    private final int channels;
    private int limit;
    private int index;
    private boolean isClosed;
    
    protected AudioSampleBuffer(IAudioSamples samples) 
    {
        if (samples == null)
            throw new IllegalArgumentException("No audio samples have been specified.");
        if (!samples.isComplete())
            throw new IllegalArgumentException("No valid audio samples have been specified.");
        
        this.samples = samples;
        this.channels = samples.getChannels();
        this.clear();
        this.isClosed = false;
    }
    
    /**
     * Determines whether or not this audio buffer is closed.
     * This audio buffer is closed once close() has been called. 
     * Any subsequent method call after this stream has been closed 
     * will throw an {@link java.lang.IllegalStateException}.
     * @return true if this audio buffer is closed.
     */
    public final boolean isClosed() {
        return this.isClosed;
    }
    
    /**
     * Gets the underlying raw samples of this audio buffer.
     * Ownership of the wrapping object is passed to the caller.
     * @return the underlying raw samples of this audio buffer.
     */
    public final IAudioSamples getData() {
        return this.samples.copyReference();
    }
    
    /**
     * Gets the underlying raw samples of this audio buffer.
     * Ownership of the returned object is NOT passed to the caller. Do not 
     * call the {@link com.xuggle.xuggler.IAudioSamples.delete()} method of 
     * the returned object.
     * @return the underlying raw samples of this audio buffer.
     */
    public final IAudioSamples getDataCached() {
        return this.samples;
    }
    
    /**
     * Gets the capacity of this buffer, in samples (per channel).
     * @return the capacity of this buffer, in samples (per channel)
     */
    public final int capacity() {
        return (int)this.samples.getNumSamples();
    }
    
    /**
     * Gets the limit of this buffer, in samples (per channel).
     * @return the limit of this buffer, in samples (per channel)
     */
    public final int limit() {
        return this.limit / this.channels;
    }
    
    /**
     * Sets the limit of this buffer, in samples (per channel).
     * @param newLimit the new limit, in samples (per channel)
     * @return this audio buffer
     */
    public final AudioSampleBuffer<T> limit(final int newLimit) 
    {
        if (this.isClosed)
            throw new IllegalStateException("This audio sample buffer has been closed.");
        if (newLimit < 0 || newLimit > this.capacity())
            throw new IndexOutOfBoundsException("Index: "+newLimit+"; Size: "+this.capacity());
        
        this.limit = newLimit * this.channels;
        return this;
    }
    
    /**
     * Gets the current position of this buffer, in samples (per channel).
     * @return the position of this buffer, in samples (per channel)
     */
    public final int position() {
        return this.index / this.channels;
    }
    
    /**
     * Sets the current position of the buffer, in samples (per channel).
     * @param newPosition the new position, in samples (per channel)
     * @return this audio buffer
     */
    public final AudioSampleBuffer<T> position(final int newPosition) 
    {
        if (this.isClosed)
            throw new IllegalStateException("This audio sample buffer has been closed.");
        
        final int newIndex = newPosition * this.channels;
        
        if (newIndex < 0 || newIndex > this.limit()) 
            throw new IndexOutOfBoundsException("Index: "+newIndex+"; Size: "+this.limit());
        
        this.index = newIndex + this.channels;
        return this;
    }
    
    /**
     * Gets the number of channels of this buffer.
     * @return the number of channels of this buffer
     */
    public final int channels() {
        return this.channels;
    }
    
    /**
     * Gets the current channel of this buffer.
     * @return the current channel of this buffer.
     */
    public final int channel() {
        return this.index % this.channels;
    }
    
    /**
     * Sets the current channel of this buffer.
     * @param newChannel the new channel
     * @return this audio buffer
     */
    public final AudioSampleBuffer<T> channel(final int newChannel) 
    {
        if (this.isClosed)
            throw new IllegalStateException("This audio sample buffer has been closed.");
        
        final int newIndex = this.index - (this.index % this.channels) + newChannel;
        
        if (newChannel < 0 || newChannel > this.channels) 
            throw new IndexOutOfBoundsException("Index: "+newChannel+"; Size: "+this.channels);
        
        this.index = newIndex;
        return this;
    }
    
    /**
     * Determines whether or not there are any elements between the current 
     * position and the limit.
     * @return true if, and only if, there is at least one sample (per channel)
     * remaining in this buffer
     */
    public final boolean hasRemaining() {
        return this.index < this.limit;
    }
    
    /**
     * Gets the number of samples (per channel) between the current position 
     * and the limit.
     * @return the number of samples (per channel) remaining in this buffer
     */
    public final int remaining() {
        return (this.limit - this.index) / this.channels;
    }

    public final T get() 
    {
        if (this.isClosed)
            throw new IllegalStateException("This audio sample buffer has been closed.");
        if (!this.hasRemaining())
            throw new BufferUnderflowException();
        
        T sample = this.getSample(this.samples, this.index);
        this.index++;
        return sample;
    }
    
    protected abstract T getSample(IAudioSamples samples, int index);
    
    public final AudioSampleBuffer<T> get(AudioSampleBuffer<T> dst) 
    {
        if (this.isClosed)
            throw new IllegalStateException("This audio sample buffer has been closed.");
        if (this.remaining() < dst.remaining())
            throw new BufferUnderflowException();
        
        this.checkAudioBuffer(dst);
        final int length = dst.limit - dst.index;
        this.copySamples(this.samples, this.index, dst.samples, dst.index, length);
        this.index += length;
        dst.index += length;
        return this;
    }

    public final AudioSampleBuffer<T> put(T sample) 
    {
        if (this.isClosed)
            throw new IllegalStateException("This audio sample buffer has been closed.");
        if (sample == null)
            throw new IllegalArgumentException("No sample has been specified.");
        if (!this.hasRemaining())
            throw new BufferOverflowException();
        
        this.putSample(this.samples, this.index, sample);
        this.index++;
        return this;
    }
    
    protected abstract void putSample(IAudioSamples samples, int index, T sample);
    
    public final AudioSampleBuffer<T> put(AudioSampleBuffer<T> src)
    {
        if (this.isClosed)
            throw new IllegalStateException("This audio sample buffer has been closed.");
        if (this.remaining() < src.remaining())
            throw new BufferOverflowException();
        
        this.checkAudioBuffer(src);
        final int length = src.limit - src.index;
        this.copySamples(src.samples, src.index, this.samples, this.index, length);
        this.index += length;
        src.index += length;
        return this;
    }
    
    protected abstract void copySamples(IAudioSamples src, int srcIndex, IAudioSamples dst, int dstIndex, int length);
    
    private void checkAudioBuffer(AudioSampleBuffer<T> buf) 
    {
        if (buf == null)
            throw new IllegalArgumentException("No audio sample buffer has been specified.");
        if (buf.isClosed)
            throw new IllegalArgumentException("The specified audio sample buffer has already been closed.");
        
        if (this.samples.getFormat() != buf.samples.getFormat() || 
            this.samples.getChannels() != buf.samples.getChannels() || 
            this.samples.getSampleRate() != buf.samples.getSampleRate())
            throw new IllegalArgumentException("Incompatible audio samples are specified.");
    }
    
    public final AudioSampleBuffer<T> mix(T sample) 
    {
        if (this.isClosed)
            throw new IllegalStateException("This audio sample buffer has been closed.");
        if (sample == null)
            throw new IllegalArgumentException("No sample has been specified.");
        if (!this.hasRemaining())
            throw new BufferOverflowException();
        
        final T origSample = this.getSample(this.samples, this.index);
        final T mixedSample = this.mixSample(origSample, sample);
        this.putSample(this.samples, this.index, mixedSample);
        this.index++;
        return this;
    }
    
    protected abstract T mixSample(T sample, T sample2);
    
    public final AudioSampleBuffer<T> mix(AudioSampleBuffer<T> src) 
    {
        if (this.isClosed)
            throw new IllegalStateException("This audio sample buffer has been closed.");
        if (this.remaining() < src.remaining())
            throw new BufferOverflowException();
        
        this.checkAudioBuffer(src);
        final int length = src.limit - src.index;
        this.mixSamples(src.samples, src.index, this.samples, this.index, length);
        this.index += length;
        src.index += length;
        return this;
    }
    
    protected abstract void mixSamples(IAudioSamples src, int srcIndex, IAudioSamples dst, int dstIndex, int length);
    
    public final AudioSampleBuffer<T> scale(double scale)
    {
        if (this.isClosed)
            throw new IllegalStateException("This audio sample buffer has been closed.");
        if (!this.hasRemaining())
            throw new BufferOverflowException();

        final T sample = this.getSample(this.samples, this.index);
        final T scaledSample = this.scaleSample(sample, scale);
        this.putSample(this.samples, this.index, scaledSample);
        this.index++;
        return this;
    }
    
    protected abstract T scaleSample(T sample, double scale);
    
    public final AudioSampleBuffer<T> scaleAll(double scale)
    {
        if (this.isClosed)
            throw new IllegalStateException("This audio sample buffer has been closed.");

        this.scaleSamples(this.samples, this.index, this.limit, scale);
        this.index = this.limit;
        return this;
    }
    
    protected abstract void scaleSamples(IAudioSamples samples, int index, int length, double scale);

    public final AudioSampleBuffer<T> silence() 
    {
        if (this.isClosed)
            throw new IllegalStateException("This audio sample buffer has been closed.");
        if (!this.hasRemaining())
            throw new BufferOverflowException();
        
        this.putSample(this.samples, this.index, this.getSilentSample());
        this.index++;
        return this;
    }
    
    protected abstract T getSilentSample();
    
    public final AudioSampleBuffer<T> silenceAll() 
    {
        if (this.isClosed)
            throw new IllegalStateException("This audio sample buffer has been closed.");

        if (this.hasRemaining()) {
            this.fillSamples(this.samples, this.index, this.limit, this.getSilentSample());
            this.index = this.limit;
        }
        return this;
    }

    public final AudioSampleBuffer<T> fill(T sample) 
    {
        if (this.isClosed)
            throw new IllegalStateException("This audio sample buffer has been closed.");
        if (sample == null)
            throw new IllegalArgumentException("No sample has been specified.");
        
        if (this.hasRemaining()) {
            this.fillSamples(this.samples, this.index, this.limit, sample);
            this.index = this.limit;
        }
        return this; 
    }
    
    protected abstract void fillSamples(IAudioSamples samples, int index, int length, T sample);

    /**
     * Rewinds this audio buffer.
     * The sample and channel positions are set to zero. 
     * <p>
     * After a sequence of bulk write or <code>put</code> operations, invoke 
     * this method to prepare for a sequence of bulk read or <code>get</code> 
     * operations, assuming that the limit has already been set appropriately.
     * </p>
     * @return this audio buffer
     */
    public final AudioSampleBuffer<T> rewind()
    {
        this.index = 0;
        return this;
    }
    
    /**
     * Flips this audio buffer.
     * The limit is set to the current sample position and then the sample and 
     * channel positions are set to zero. 
     * <p>
     * After a sequence of bulk write or <code>put</code> operations, invoke this 
     * method to prepare for a sequence of bulk read or <code>get</code> operations.
     * </p>
     * @return this audio buffer
     */
    public final AudioSampleBuffer<T> flip() 
    {
        this.limit = this.index;
        this.index = 0;
        return this;
    }
    
    /**
     * Clears this audio buffer. 
     * The sample and channel positions are set to zero and the limit is set to
     * the capacity. 
     * Invoke this method before using a sequence of bulk write or <code>put
     * </code> operations to fill this buffer.
     * <p>
     * This method does not actually erase the data in the buffer, but it is 
     * named as if it did because it will most often bee used in situations 
     * where that might as well be the case.
     * </p>
     * @return this buffer
     */
    public final AudioSampleBuffer<T> clear()
    {
        this.index = 0;
        this.limit = this.capacity() * this.channels;
        return this;
    }
    
    /**
     * Closes this audio buffer.
     * This method has to be called if, and only if, this buffer is direct in 
     * order to release its unmanaged resources.
     * <p>
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