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
package org.maksvzw.zetcam.io.resources;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IError;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import org.maksvzw.zetcam.core.audio.AudioFormat;
import org.maksvzw.zetcam.core.MediaType;

/**
 *
 * @author Lenny Knockaert
 */
public final class AudioResource extends MediaResource
{
    private Path resourcePath;
    private final String codecName;
    private final String codecLongName;
    private final AudioFormat format;
    private final int bitRate;
    private final int bitRateTolerance;
    private final Duration startTime;
    private final Duration duration;
    
    public AudioResource(final Path audioPath) 
            throws FileNotFoundException, Exception
    {
        super(audioPath.getFileName().toString().split(".")[0]);
        
        if (audioPath == null)
            throw new IllegalArgumentException("No audio file path has been specified.");
        if (!Files.exists(audioPath, LinkOption.NOFOLLOW_LINKS))
            throw new FileNotFoundException();
        
        this.resourcePath = audioPath;

        final IMediaReader reader = this.getReader();
        if (reader == null)
            throw new IllegalArgumentException("The specified resource file is not a media file.");
        
        try {
            reader.open();
            
            final IContainer container = reader.getContainer();
            if (container.getNumStreams() != 1)
                throw new IllegalArgumentException("The specified resource file is not an audio file.");

            final IStream stream = container.getStream(0);
            final IStreamCoder coder = stream.getStreamCoder();
            if (coder.getCodecType() != ICodec.Type.CODEC_TYPE_AUDIO)
                throw new IllegalArgumentException("The specified resource file is not an audio file.");
            
            this.codecName = coder.getCodec().getName();
            this.codecLongName = coder.getCodec().getLongName();
            
            this.format = new AudioFormat(
                    coder.getSampleRate(),
                    coder.getSampleFormat(),
                    coder.getChannels());
            this.bitRate = coder.getBitRate();
            this.bitRateTolerance = coder.getBitRateTolerance();

            final IRational timeBase = stream.getTimeBase();
            this.duration = toDuration(stream.getDuration(), timeBase);
            this.startTime = toDuration(stream.getStartTime(), timeBase);
        } 
        finally {
            reader.close();
        }
    }
    
    private static Duration toDuration(long value, IRational timeBase) 
    {
        long microsecs = IRational.rescale(
                value, 
                1, 1000000,
                timeBase.getNumerator(), 
                timeBase.getDenominator(),
                IRational.Rounding.ROUND_NEAR_INF);

        return Duration.ofNanos(microsecs * 1000);
    }

    @Override
    public final MediaType getType() {
        return MediaType.AUDIO;
    }
    
    public final Path getPath() {
        return this.resourcePath;
    }

    public final String getCodecName() {
        return this.codecName;
    }

    public final String getCodecLongName() {
        return this.codecLongName;
    }
    
    public final AudioFormat getFormat() {
        return this.format;
    }

    public final int getChannels() 
    {
        if (this.format == null)
            return 0;
        
        return this.format.getChannels();
    }

    public final int getSampleRate() 
    {
        if (this.format == null)
            return 0;
        
        return this.format.getSampleRate();
    }

    public final IAudioSamples.Format getSampleFormat() 
    {
        if (this.format == null)
            return IAudioSamples.Format.FMT_NONE;
        
        return this.format.getSampleFormat();
    }
    
    public final int getBitRate() {
        return this.bitRate;
    }

    public final int getBitRateTolerance() {
        return this.bitRateTolerance;
    }

    public final Duration getStartTime() {
        return this.startTime;
    }

    public final Duration getDuration() {
        return this.duration;
    }
    
    @Override
    public void copy(Path targetPath) throws IOException 
    {
        if (targetPath == null)
            throw new IllegalArgumentException("No target path has been specified.");
        if (!targetPath.endsWith(this.resourcePath.getFileName()))
            throw new IllegalArgumentException("The specified target path must the same fileName.");

        Files.copy(this.resourcePath, targetPath, StandardCopyOption.ATOMIC_MOVE);
        this.resourcePath = targetPath;
    }
    
    public final IMediaReader getReader() throws Exception 
    {
        try (InputStream inputStream = this.open()) {
            final IContainer container = IContainer.make();

            int ret;
            if ((ret = container.open(inputStream, null)) < 0)
                throw new RuntimeException(IError.make(ret).getDescription());

            return ToolFactory.makeReader(container);
        }
    }
    
    @Override
    public String toString() 
    {
        StringBuilder builder = new StringBuilder();
        builder.append(this.resourcePath);
        builder.append(System.lineSeparator());
        builder.append("Codec:\t");
        builder.append(this.codecLongName);
        builder.append(System.lineSeparator());
        builder.append("Start time:\t");
        builder.append(this.startTime.toMillis());
        builder.append(" ms");
        builder.append(System.lineSeparator());
        builder.append("Duration:\t");
        builder.append(this.duration);
        builder.append(System.lineSeparator());
        builder.append("Sample rate:\t");
        builder.append(this.format.getSampleRate());
        builder.append(System.lineSeparator());
        builder.append("Sample format:\t");
        builder.append(this.format.getSampleFormat());
        builder.append(System.lineSeparator());
        builder.append("Channels:\t");
        builder.append(this.format.getChannels());
        builder.append(System.lineSeparator());
        builder.append("Bit rate:\t");
        builder.append(this.bitRate);
        builder.append(System.lineSeparator());
        builder.append("Bit rate tolerance:\t");
        builder.append(this.bitRateTolerance);
        builder.append(System.lineSeparator());
        return builder.toString();
    }
}