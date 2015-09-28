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
import org.maksvzw.zetcam.core.audio.buffers.AudioSampleBuffer;
import org.maksvzw.zetcam.core.utils.Maths;

/**
 *
 * @author Lenny Knockaert
 */
public abstract class FadeFilter extends AudioFilter 
{
    private Duration startTime;
    private long startSample;
    private Duration duration;
    private long numOfSamples;
    private CurveType curveType;

    public FadeFilter(
            Duration startTime, 
            Duration duration, 
            CurveType curveType/*,
            FadeDirection direction*/)
    {
        this.setStartTime(startTime);
        this.setDuration(duration);
        this.setCurveType(curveType);
    }
    
    public Duration getStartTime() {
        return this.startTime;
    }
    
    public void setStartTime(Duration startTime) 
    {
        if (startTime == null)
            throw new IllegalArgumentException("No start time has been specified.");
        
        this.startTime = startTime;
        this.startSample = 0;
    }
    
    public Duration getDuration() {
        return this.duration;
    }
    
    public void setDuration(Duration d) 
    {
        if (d == null)
            throw new IllegalArgumentException("No fade duration has been specified.");
        
        this.duration = d;
        this.numOfSamples = 0;
    }
    
    public CurveType getCurveType() {
        return this.curveType;
    }
    
    public void setCurveType(CurveType curve) 
    {
        if (curve == null)
            throw new IllegalArgumentException("No curve type has been specified.");
        
        this.curveType = curve;
    }
    
    public abstract FadeDirection getDirection();

    @Override
    protected IAudioSamples onFilter(IAudioSamples samples) 
    {
        /** Convert start time and duration of the fade transition to 
         samples if that wasn't already the case. */
        if (this.startSample == 0 && this.startTime != Duration.ZERO) {
            this.startSample = IRational.rescale(
                    this.startTime.toNanos(),
                    1, samples.getSampleRate(),
                    1, 1000000000,
                    IRational.Rounding.ROUND_NEAR_INF);
        }
        
        if (this.numOfSamples == 0 && this.duration != Duration.ZERO) {
            this.numOfSamples = IRational.rescale(
                    this.duration.toNanos(),
                    1, samples.getSampleRate(),
                    1, 1000000000,
                    IRational.Rounding.ROUND_NEAR_INF);
        }
        
        /* Convert the start time and duration of this audio frame to samples. */
        final long numOfSamples = samples.getNumSamples();
        final long currentSample = IRational.sRescale(
                samples.getTimeStamp(), 
                samples.getTimeBase(), 
                IRational.make(1, samples.getSampleRate()));
        
        /* No fade transition is to be applied after a fade-in or before a fade-out.*/
        if (this.shouldIgnoreSamples(
                this.startSample, 
                this.numOfSamples, 
                currentSample, 
                numOfSamples))
        {
            return super.filter(samples);
        }
        
        try (AudioSampleBuffer buffer = AudioSampleBuffer.wrap(samples)) 
        {
            /* The samples must be silenced before a fade-in and after a fade-out. */
            if (this.shouldSilenceSamples(
                    this.startSample, 
                    this.numOfSamples, 
                    currentSample, 
                    numOfSamples))
            {
                buffer.silenceAll();
                return super.filter(samples);
            } 
            
            /* The samples must be scaled accordingly. */
            final long start = this.getCurrentFadeSample(this.startSample, this.numOfSamples, currentSample);
            final int dir = this.getDirectionSignum();

            double gain; 
            while(buffer.hasRemaining()) {
                gain = calculateFadeGain(start + buffer.position() * dir, this.numOfSamples);
                buffer.scale(gain);
            }
        }
        return super.filter(samples);
    }
    
    protected abstract boolean shouldIgnoreSamples(long fadeStartSample, long fadeNumOfSamples, long currentSample, long numOfSamples);
    
    protected abstract boolean shouldSilenceSamples(long fadeStartSample, long fadeNumOfSamples, long currentSample, long numOfSamples);
    
    protected abstract long getCurrentFadeSample(long fadeStartSample, long fadeNumOfSamples, long currentSample);
    
    protected abstract int getDirectionSignum();
    
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