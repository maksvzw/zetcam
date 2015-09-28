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

import com.xuggle.mediatool.IMediaListener;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaGeneratorAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IError;
import com.xuggle.xuggler.IStream;
import java.io.File;

/**
 *
 * @author Lenny Knockaert
 */
/* 
AudioSource implements IMediaGenerator
        -> open();
        -> read();
        -> seek();
        -> rewind();
        -> close();
*/
public class AudioSource extends MediaGeneratorAdapter implements AutoCloseable
{
    private final File audioFile;
    private boolean isOpen;
    private boolean eof;
    private IMediaReader reader;
    
    public AudioSource(File audioFile) 
    {
        if (audioFile == null || !audioFile.isFile())
            throw new IllegalArgumentException("No audio file has been specified.");

        this.audioFile = audioFile;
    }
    
    public boolean isOpen() {
        return this.isOpen;
    }
    
    public boolean EOF() {
        return this.eof;
    }
    
    public void open() 
    {
        if (this.isOpen)
            return;
        
        this.reader = ToolFactory.makeReader(this.audioFile.getAbsolutePath());
        if (this.reader == null)
            throw new IllegalArgumentException("No audio file has been specified.");
            
        for(IMediaListener listener : this.getListeners())
            this.reader.addListener(listener);
        
        final IContainer container;
        final IStream stream;
        try {
            this.reader.open();

            container = this.reader.getContainer();
            if (container.getNumStreams() != 1)
                throw new IllegalArgumentException("No audio file has been specified.");

            stream = container.getStream(0);
            if (stream.getStreamCoder().getCodecType() != ICodec.Type.CODEC_TYPE_AUDIO)
                throw new IllegalArgumentException("No audio file has been specified.");
        }
        catch (IllegalArgumentException ex) 
        {
            this.reader.close();
            throw ex;
        }
        this.isOpen = true;
    }
    
    public boolean read() 
    {
        if (!this.isOpen)
            throw new IllegalStateException("This audio source has not yet been opened.");
        if (this.eof)
            return false;
        
        IError err;
        if ((err = this.reader.readPacket()) == null)
            return true;
        
        if (err.getType() == IError.Type.ERROR_EOF) {
            this.eof = true;
            return false;
        }
        throw new RuntimeException(err.getDescription());
    }
    
    public void readAll() 
    {
        while(this.read())
            ;
    }
    
    @Override
    public boolean addListener(IMediaListener listener) 
    {
        if (listener == null || this.getListeners().contains(listener))
            return false;
        
        if (!super.addListener(listener))
            return false;
        
        if (this.isOpen)
            this.reader.addListener(listener);
        
        return true;
    }
    
    @Override
    public boolean removeListener(IMediaListener listener) 
    {
        if (listener == null || !super.removeListener(listener))
            return false;
            
        if (this.isOpen)
            this.reader.removeListener(listener);
        
        return true;
    }

    @Override
    public void close() 
    {
        if (!this.isOpen)
            return;
        
        if (this.reader != null) {
            for(IMediaListener listener : this.getListeners())
                this.reader.removeListener(listener);
            
            this.reader.close();
            this.reader = null;
        }
        
        this.isOpen = false;
        this.eof = false;
    }
}