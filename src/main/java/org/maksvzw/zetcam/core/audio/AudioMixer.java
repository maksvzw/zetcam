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
import com.xuggle.mediatool.IMediaListener;
import com.xuggle.mediatool.MediaGeneratorAdapter;
import com.xuggle.mediatool.event.AudioSamplesEvent;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IAudioSamples.Format;
import org.maksvzw.zetcam.core.audio.buffers.AudioSampleBuffer;
import org.maksvzw.zetcam.core.audio.buffers.fifo.AudioSampleFifoBuffer;

/**
 *
 * @author Lenny Knockaert
 */
// TODO: refactor!!
public final class AudioMixer extends MediaGeneratorAdapter implements AutoCloseable
{
    private final AudioFormat dstFormat;
    private final int maxNumOfInputs;
    private int numOfInputs;
    private int numOfActiveInputs;
    private double dropOutTransition;
    private DurationDeterminationMethod durationMethod;
    private long nextPts;
    private double scaleNorm;
    private boolean isClosed;
    
    private final InputContext[] inputCtx;

    public AudioMixer() 
    {
        this.dstFormat = new AudioFormat(44100, Format.FMT_S16, 2);
        this.maxNumOfInputs = 3;
        this.numOfInputs = 0;
        this.numOfActiveInputs = 0;
        this.dropOutTransition = 2.0;
        this.durationMethod = DurationDeterminationMethod.LONGEST;
        this.nextPts = 0;
        this.scaleNorm = this.numOfActiveInputs;
        this.isClosed = false;
        
        this.inputCtx = new InputContext[this.maxNumOfInputs];
        for(int i = 0; i < this.maxNumOfInputs; i++) {
            this.inputCtx[i] = new InputContext(this.dstFormat);
            this.inputCtx[i].fifo = AudioSampleFifoBuffer.allocate(this.dstFormat, 8192);
            this.inputCtx[i].state = InputState.OFF;
            this.inputCtx[i].scale = 1.0;
        }
    }

    public AudioFormat getOutputFormat() {
        return this.dstFormat;
    }
    
    public int getMaxNumInputs() {
        return this.maxNumOfInputs;
    }
    
    public int getNumInputs() {
        return this.numOfInputs;
    }
    
    public int getNumActiveInputs() {
        this.calculateActiveInputs();
        return this.numOfActiveInputs;
    }
    
    public double getDropOutTransition() {
        return this.dropOutTransition;
    }
    
    public DurationDeterminationMethod getDurationMethod() {
        return this.durationMethod;
    }
    
    public void read(int numOfSamples) 
    {
        /* Ensure each active input has buffered enough samples to satisfy 
        the request. */
        int maxAvailableSamples = 0;
        for(int i = 0; i < this.maxNumOfInputs; i++) {
            if (this.inputCtx[i].state != InputState.ON)
                continue;
            
            while(this.inputCtx[i].fifo.delay() < numOfSamples && 
                 this.inputCtx[i].source.read()) 
            { }
            
            if (this.inputCtx[i].fifo.delay() <= 0)
                this.inputCtx[i].state = InputState.INACTIVE;
            else
                maxAvailableSamples = Math.max(
                        maxAvailableSamples, this.inputCtx[i].fifo.delay());
        }

        /* Samples of each input are scaled in order to balance the full 
         volume range between active inputs and to handle volume transitions
         when EOF is encountered on an input but mixing continues with the
         remaining inputs. */
        this.calculateActiveInputs();
        this.calculateScales(numOfSamples);
        
        /* Allocate output buffer and configure its time stamps.  */
        try(AudioSampleBuffer outBuffer = AudioSampleBuffer.allocate(
                this.dstFormat, numOfSamples, this.nextPts);
            AudioSampleBuffer tempBuffer = AudioSampleBuffer.allocate(
                this.dstFormat, numOfSamples))
        {
            final IAudioSamples tempSamples = tempBuffer.getDataCached();

            int numOfSamplesToRead;
            for(int i = 0; i < this.maxNumOfInputs; i++) {
                /* Do not consider closed inputs or inactive inputs with  
                empty FIFO buffers. */
                if (this.inputCtx[i].state != InputState.ON)
                    continue;

                numOfSamplesToRead = Math.min(numOfSamples, this.inputCtx[i].fifo.delay());
                tempBuffer.clear().limit(numOfSamplesToRead);
                
                /* Read samples from FIFO into a temporary buffer. */
                this.inputCtx[i].fifo.get(tempSamples, 0, numOfSamplesToRead);

                /* Apply scaling factor to each sample. */
                tempBuffer.scaleAll(this.inputCtx[i].scale).rewind();
                /* Mix samples in the temporary buffer with samples in the output buffer. */
                outBuffer.mix(tempBuffer).rewind();
            }
            this.nextPts += numOfSamples;
            this.raiseAudioSamplesEvent(outBuffer.getDataCached());
        }
    }
    
    private void raiseAudioSamplesEvent(IAudioSamples samples) 
    {
        AudioSamplesEvent iase = new AudioSamplesEvent(this, samples, 0);
        for(IMediaListener listener : this.getListeners())
            listener.onAudioSamples(iase);
    }
    
    public void link(int index, AudioSource audioSource) 
    {
        if (index < 0 || index > this.maxNumOfInputs)
            throw new IndexOutOfBoundsException("Index: "+index+"; Size: "+this.maxNumOfInputs);
        if (audioSource == null)
            throw new IllegalArgumentException("No audio source has been specified.");
        
        if (this.inputCtx[index].state == InputState.ON)
            throw new IllegalStateException("Input link #"+index+" has an active audio source.");
        if (this.inputCtx[index].state == InputState.INACTIVE)
            this.unlink(index);
        
        audioSource.addListener(this.inputCtx[index]);
        if (!audioSource.isOpen())
            audioSource.open();
        
        this.inputCtx[index].source = audioSource;
        this.inputCtx[index].state = InputState.ON;
        this.inputCtx[index].scale = 1.0;
        this.numOfInputs++;
    }
    
    public void unlink(int index) 
    {
        if (index < 0 || index > this.maxNumOfInputs)
            throw new IndexOutOfBoundsException("Index: "+index+"; Size: "+this.maxNumOfInputs);
        
        if (this.inputCtx[index].state == InputState.OFF)
            return;
        
        this.inputCtx[index].source.removeListener(this.inputCtx[index]);
        this.inputCtx[index].source.close();
        this.inputCtx[index].source = null;
        this.inputCtx[index].state = InputState.OFF;
        this.inputCtx[index].scale = 0.0;
        this.numOfInputs--;
    }
    
    private boolean calculateActiveInputs() 
    {
        int activeInputs = 0;
        for (int i = 0; i < this.maxNumOfInputs; i++)
            if (this.inputCtx[i].state == InputState.ON)
                activeInputs++;
        this.numOfActiveInputs = activeInputs;
        
        if (this.numOfActiveInputs <= 0 ||
            (this.durationMethod == DurationDeterminationMethod.FIRST && this.inputCtx[0].state != InputState.ON) ||
            (this.durationMethod == DurationDeterminationMethod.SHORTEST && this.numOfActiveInputs != this.numOfInputs))
        {
            return false;
        }
        return true;
    }
    
    private void calculateScales(int numOfSamples)
    {
        if (this.scaleNorm > this.numOfActiveInputs)
            this.scaleNorm -= numOfSamples / (this.dropOutTransition * this.dstFormat.getSampleRate());

        this.scaleNorm = Math.max(this.scaleNorm, this.numOfActiveInputs);

        for (int i = 0; i < this.maxNumOfInputs; i++) {
            if (this.inputCtx[i].state == InputState.ON)
                this.inputCtx[i].scale = 1.0f / this.scaleNorm;
            else
                this.inputCtx[i].scale = 0.0f;
        }
    }

    @Override
    public void close() 
    {
        if (this.isClosed)
            return;
        
        for(int i = 0; i < this.maxNumOfInputs; i++) {
            this.inputCtx[i].fifo.close();
            this.inputCtx[i].fifo = null;
            this.unlink(i);
        }
        this.isClosed = true;
    }
    
    private static class InputContext extends AudioSink
    {
        public AudioSampleFifoBuffer fifo;
        public AudioSource source;
        public InputState state;
        public double scale;

        public InputContext(AudioFormat audioFormat) {
            super(audioFormat);
        }

        @Override
        protected void onAudioSamples(IAudioSamples samples) {
            this.fifo.put(samples, 0, (int)samples.getNumSamples());
        }
    }
    
    private enum InputState 
    {
        OFF,
        ON,
        INACTIVE
    }
}