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

import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IRational;
import java.time.Duration;
import org.maksvzw.zetcam.core.audio.Audio;
import org.maksvzw.zetcam.core.audio.AudioFormat;
import org.maksvzw.zetcam.core.audio.buffers.AudioSampleBuffer;
import org.maksvzw.zetcam.infrastructure.Maths;

/**
 *
 * @author Lenny Knockaert
 */
public final class FadeFilter extends AudioFilter 
{
    private final FadeDirection direction;
    private final CurveType curveType;
    private final long fadeStartSample;
    private final long fadeNumOfSamples;
    
    public FadeFilter(
            final AudioFormat audioFormat,
            final FadeDirection direction,
            final Duration fadeStartTime, 
            final Duration fadeDuration, 
            final CurveType curveType)
    {
        if (audioFormat == null)
            throw new IllegalArgumentException("No audio format has been specified.");
        if (direction == null)
            throw new IllegalArgumentException("No fade direction has been specified.");
        if (fadeStartTime == null)
            throw new IllegalArgumentException("No fade start time has been specified.");
        if (fadeDuration == null)
            throw new IllegalArgumentException("No fade duration has been specified.");
        if (curveType == null)
            throw new IllegalArgumentException("No curve type has been specified.");
        
        this.direction = direction;
        this.curveType = curveType;
        
        /** Convert start time and duration of the fade transition to 
         sample numbers. */
        this.fadeStartSample = Audio.getNumOfSamples(audioFormat, fadeStartTime);
        this.fadeNumOfSamples = Audio.getNumOfSamples(audioFormat, fadeDuration); 
    }
    
    @Override
    protected IAudioSamples onFilter(IAudioSamples samples) 
    {
        /* Convert the start time and duration of this audio frame to samples. */
        final long numOfSamples = samples.getNumSamples();
        final long currentSample = IRational.sRescale(
                samples.getTimeStamp(), 
                samples.getTimeBase(), 
                IRational.make(1, samples.getSampleRate()));
        
        /* No fade transition is to be applied after a fade-in or before a fade-out.*/
        if (this.direction.shouldIgnoreSamples(
                this.fadeStartSample, 
                this.fadeNumOfSamples, 
                currentSample, 
                numOfSamples))
        {
            return super.filter(samples);
        }
        
        try (AudioSampleBuffer buffer = AudioSampleBuffer.wrap(samples)) 
        {
            /* The samples must be silenced before a fade-in and after a fade-out. */
            if (this.direction.shouldSilenceSamples(
                    this.fadeStartSample, 
                    this.fadeNumOfSamples, 
                    currentSample, 
                    numOfSamples))
            {
                buffer.silenceAll();
                return super.filter(samples);
            } 
            
            /* The samples must be scaled in a specific direction. */
            final int dir = this.direction.getDirectionSignum();
            final long start = this.direction.getCurrentFadeSample(
                    this.fadeStartSample, 
                    this.fadeNumOfSamples, 
                    currentSample);
 
            double gain; 
            while(buffer.hasRemaining()) {
                gain = calculateFadeGain(start + buffer.position() * dir, this.fadeNumOfSamples);
                buffer.scale(gain);
            }
        }
        return super.filter(samples);
    }
    
    private double calculateFadeGain(long index, long range) 
    {
        double gain = Maths.clamp(1.0 * index / range, 0.0, 1.0);
        switch(this.curveType) {
            case QSIN:
                gain = Math.sin(gain * Math.PI / 2.0);
                break;
            case IQSIN:
                gain = 0.636943 * Math.asin(gain);
                break;
            case ESIN:
                gain = 1.0 - Math.cos(Math.PI / 4.0 * (Math.pow(2.0*gain - 1, 3) + 1));
                break;
            case HSIN:
                gain = (1.0 - Math.cos(gain * Math.PI)) / 2.0;
                break;
            case IHSIN:
                gain = 0.318471 * Math.acos(1 - 2 * gain);
                break;
            case EXP:
                gain = Math.pow(0.1, (1 - gain) * 5.0);
                break;
            case LOG:
                gain = Maths.clamp(0.0868589 * Math.log(100000 * gain), 0.0, 1.0);
                break;
            case PAR:
                gain = 1 - Math.sqrt(1 - gain);
            case IPAR:
                gain = (1 - (1 - gain) * (1 - gain));
                break;
            case QUA:
                gain *= gain;
                break;
            case CUB:
                gain = gain * gain * gain;
                break;
            case SQU:
                gain = Math.sqrt(gain);
                break;
            case CBR:
                gain = Math.cbrt(gain);
                break;
            case DESE:
                if (gain <= 0.5)
                    gain = Math.pow(2 * gain, 1/3.0) / 2;
                else
                    gain = 1 - Math.pow(2 * (1 - gain), 1/3.0) / 2;
                break;
            case DESI:
                if (gain <= 0.5)
                    gain = Math.pow(2 * gain, 3) / 2;
                else
                    gain = 1 - Math.pow(2 * (1 - gain), 3) / 2;
                break;
        }
        return gain;
    }
}