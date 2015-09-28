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

import org.maksvzw.zetcam.core.audio.AudioFormat;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.xuggler.IAudioResampler;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IAudioSamples.Format;
import org.maksvzw.zetcam.core.audio.buffers.AudioSampleBuffer;

/**
 *
 * @author Lenny Knockaert
 */
public abstract class AudioSink extends MediaListenerAdapter implements AutoCloseable
{ 
    private final AudioFormat dstFormat;
    private IAudioResampler resampler;
    private boolean isClosed;
    
    public AudioSink(AudioFormat audioFormat) 
    {
        if (audioFormat == null)
            throw new IllegalArgumentException("No audio format has been specified.");
        
        this.dstFormat = audioFormat;
        this.isClosed = false;
    }
    
    public boolean isClosed() {
        return this.isClosed;
    }
    
    @Override
    public final void onAudioSamples(IAudioSamplesEvent iase) 
    {
        if (this.isClosed)
            return;
        
        final IAudioSamples inputSamples = iase.getAudioSamples();
        final int inputChannels = inputSamples.getChannels(),
                inputRate = inputSamples.getSampleRate();
        final Format inputFormat = inputSamples.getFormat();
        
        if (this.resampler != null && 
            (inputRate != this.resampler.getInputRate() || 
             inputFormat != this.resampler.getInputFormat() ||
             inputChannels != this.resampler.getInputChannels()))
        {
            this.resampler.delete();
            this.resampler = null;
        }
        
        if (this.resampler == null && 
            (inputRate != this.dstFormat.getSampleRate() ||
            inputFormat != this.dstFormat.getSampleFormat() ||
            inputChannels != this.dstFormat.getChannels()))
        {
            this.resampler = IAudioResampler.make(
                    this.dstFormat.getChannels(), inputChannels, 
                    this.dstFormat.getSampleRate(), inputRate, 
                    this.dstFormat.getSampleFormat(), inputFormat);
        }
        
        if (this.resampler != null) {
            final int numOfInputSamples = (int)inputSamples.getNumSamples();
            final int numOfOutputSamples = (int)this.resampler.getMinimumNumSamplesRequiredInOutputSamples(inputSamples);
            
            try (AudioSampleBuffer outputBuffer = AudioSampleBuffer.allocate(
                    this.dstFormat, numOfOutputSamples)) 
            {
                final IAudioSamples outputSamples = outputBuffer.getDataCached();
                this.resampler.resample(outputSamples, inputSamples, numOfInputSamples);
                this.onAudioSamples(outputSamples);
            }
        } 
        else {
            this.onAudioSamples(inputSamples);
        }
    }
    
    protected abstract void onAudioSamples(IAudioSamples samples);
    
    @Override
    public void close() 
    {
        if (this.isClosed)
            return;
        
        if (this.resampler != null) {
            this.resampler.delete();
            this.resampler = null;
        }
        
        this.isClosed = true;
    }
}