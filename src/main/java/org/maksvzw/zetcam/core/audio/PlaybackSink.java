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

import org.maksvzw.zetcam.core.audio.AudioSink;
import org.maksvzw.zetcam.core.audio.AudioFormat;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IAudioSamples.Format;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author Lenny Knockaert
 */
public class PlaybackSink extends AudioSink
{
    private SourceDataLine line;
    private byte[] playbackBuf;
    
    public PlaybackSink() throws LineUnavailableException 
    {
        this(new AudioFormat(44100, Format.FMT_S16, 2));
    }
    
    private PlaybackSink(AudioFormat format) throws LineUnavailableException 
    {
        super(format);
        
        try {
            this.line = AudioSystem.getSourceDataLine(format.toJavaFormat());
            this.line.open();
            this.line.start();
        }
        catch (LineUnavailableException ex) 
        {
            /* Select the most common and most compatible PCM format to 
            resample to. */
            /*outFormat = new AudioFormat(44100, Format.FMT_S16, 2);
            
            this.line = AudioSystem.getSourceDataLine(outFormat.toJavaFormat());
            this.line.open();
            this.line.start();*/
            throw ex;
        }
    }
    
    @Override
    protected void onAudioSamples(IAudioSamples samples) 
    {
        final int bytesToPlay = samples.getSize();
        if (this.playbackBuf == null || this.playbackBuf.length < bytesToPlay)
            this.playbackBuf = new byte[bytesToPlay];
        
        samples.get(0, this.playbackBuf, 0, bytesToPlay);
        this.line.write(this.playbackBuf, 0, bytesToPlay);
    }

    @Override
    public void close()
    {
        if (this.line != null) {
            this.line.close();
            this.line = null;
        }
        super.close();
    }
}