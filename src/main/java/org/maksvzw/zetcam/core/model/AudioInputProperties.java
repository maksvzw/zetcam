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
package org.maksvzw.zetcam.core.model;

import com.xuggle.xuggler.IAudioSamples.Format;
import java.io.Serializable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import org.maksvzw.zetcam.core.MediaType;
import org.maksvzw.zetcam.core.audio.AudioFormat;

/**
 *
 * @author Lenny Knockaert
 */
public class AudioInputProperties extends MediaInputProperties implements Serializable
{
    private final String codecName;
    private final AudioFormat format;
    private final int bitRate;
    private final Duration startTime;
    private final Duration duration;
    private final Map<String, String> metadata;
    
    public AudioInputProperties(
            final String filePath,
            final String codecName,
            final int channels,
            final int sampleRate,
            final Format sampleFormat,
            final int bitRate,
            final Duration startTime,
            final Duration duration,
            final Map<String, String> metadata) 
    {
        super(filePath);
        
        if (codecName == null || codecName.isEmpty())
            throw new IllegalArgumentException("No codec name has been specified.");
        if (bitRate < 0)
            throw new IllegalArgumentException("Invalid bit rate has been specified.");
        if (startTime == null)
            throw new IllegalArgumentException("No start time has been specified.");
        if (duration == null)
            throw new IllegalArgumentException("No duration has been specified.");
        if (duration.isNegative())
            throw new IllegalArgumentException("Invalid duration has been specified.");
        
        this.codecName = codecName;
        this.format = new AudioFormat(sampleRate, sampleFormat, channels);
        this.bitRate = bitRate;
        this.startTime = startTime;
        this.duration = duration;
        
        if (metadata == null)
            this.metadata = Collections.emptyMap();
        else
            this.metadata = Collections.unmodifiableMap(metadata);
    }
    
    @Override
    public MediaType getType() {
        return MediaType.AUDIO;
    }
    
    public final String getCodecName() {
        return this.codecName;
    }
    
    public AudioFormat getFormat() {
        return this.format;
    }
    
    public final int getChannels() {
        return this.format.getChannels();
    }

    public final int getSampleRate() {
        return this.format.getSampleRate();
    }

    public final Format getSampleFormat() {
        return this.format.getSampleFormat();
    }
    
    public final int getBitRate() {
        return this.bitRate;
    }

    public final Duration getStartTime() {
        return this.startTime;
    }

    public final Duration getDuration() {
        return this.duration;
    }

    @Override
    public Map<String, String> getMetadata() {
        return this.metadata;
    }
}