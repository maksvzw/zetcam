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
package org.maksvzw.zetcam;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IError;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import org.maksvzw.zetcam.core.audio.PlaybackSink;

/**
 *
 * @author Lenny Knockaert
 */
public class ZipTest
{
    public static void run(Path archivePath) 
    {
        final Map<String, String> env = new HashMap<>();
        env.put("encoding", "utf-8");
        env.put("create", "false");
        
        final URI archiveUri = URI.create("jar:file:" + archivePath.toUri().getRawPath());
        try (FileSystem zpfs = FileSystems.newFileSystem(archiveUri, env)) 
        {
            final Path audioPath = zpfs.getPath("test.wav");
            final InputStream inputStream = Files.newInputStream(audioPath, StandardOpenOption.READ);
            final IContainer container = IContainer.make();

            int ret;
            if ((ret = container.open(inputStream, null)) < 0)
                throw new RuntimeException(IError.make(ret).getDescription());
            
            final IMediaReader reader = ToolFactory.makeReader(container);
            if (!reader.isOpen())
                reader.open();
            
            reader.addListener(new PlaybackSink());
            
            while(reader.readPacket() == null)
                ;
            
            reader.close();
        } 
        catch (IOException ex) {
            Logger.getLogger(ZipTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(ZipTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
