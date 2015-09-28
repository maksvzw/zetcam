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
package org.maksvzw.zetcam.core.audio.filters;

import com.xuggle.xuggler.IAudioResampler;
import com.xuggle.xuggler.IAudioSamples;
import org.maksvzw.zetcam.core.audio.AudioFormat;
import org.maksvzw.zetcam.core.audio.buffers.AudioSampleBuffer;

/**
 *
 * @author Lenny Knockaert
 */
public class ResamplingFilter extends AudioFilter
{
    private AudioFormat dstFormat;
    private IAudioResampler resampler;
    
    public ResamplingFilter(final AudioFormat audioFormat) 
    {
        if (audioFormat == null)
            throw new IllegalArgumentException("No audio format has been specified.");
        
        this.dstFormat = audioFormat;
    }
    
    public AudioFormat getOutputFormat() {
        return this.dstFormat;
    }
    
    public void setOutputFormat(AudioFormat audioFormat)
    {
        if (audioFormat == null)
            throw new IllegalArgumentException("No audio format has been specified.");
        if (audioFormat.equals(this.dstFormat))
            return;
        
        this.dstFormat = audioFormat;
        this.releaseResampler();
    }
    
    @Override
    protected IAudioSamples onFilter(IAudioSamples samples)
    {
        final int inputChannels = samples.getChannels(),
                inputRate = samples.getSampleRate();
        final IAudioSamples.Format inputFormat = samples.getFormat();
        
        if (this.resampler != null && 
            (inputRate != this.resampler.getInputRate() || 
             inputFormat != this.resampler.getInputFormat() ||
             inputChannels != this.resampler.getInputChannels()))
        {
            this.releaseResampler();
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
            final int numOfInputSamples = (int)samples.getNumSamples();
            final int numOfOutputSamples = (int)this.resampler.getMinimumNumSamplesRequiredInOutputSamples(samples);
            
            try (AudioSampleBuffer outputBuffer = AudioSampleBuffer.allocate(
                    this.dstFormat, numOfOutputSamples)) 
            {
                final IAudioSamples outputSamples = outputBuffer.getDataCached(); // TODO: FIX ME !!!!!!
                this.resampler.resample(outputSamples, samples, numOfInputSamples);
                return super.filter(outputSamples);
            }
        } 
        else {
            return super.filter(samples);
        }
    }
    
    @Override
    public void reset()
    {
        this.releaseResampler();
        super.reset();
    }
    
    private void releaseResampler() {
        if (this.resampler != null) {
            this.resampler.delete();
            this.resampler = null;
        }
    }
}