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
package org.maksvzw.zetcam.io;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import org.maksvzw.zetcam.io.resources.AudioResource;
import org.maksvzw.zetcam.io.resources.ImageResource;
import org.maksvzw.zetcam.core.utils.Disposable;

/**
 *
 * @author Lenny Knockaert
 */
// TODO: Strategy pattern for loading audio/image externally as well as 
// internally with FileSystem.
public final class PersistenceManager extends Disposable
{
    private static final String RESOURCE_DIR = "resources/";
    private static final String AUDIO_RESOURCE_DIR = "resources/audio/";
    private static final String IMAGE_RESOURCE_DIR = "resources/image/";
    
    private final Path projectPath;
    private final FileSystem projectFileSystem;
    
    public PersistenceManager(Path projectPath) throws IOException
    {
        if (projectPath == null)
            throw new IllegalArgumentException("No project file has been specified.");
        /*if (Paths.getFileExtension(projectFile).equalsIgnoreCase(".zip"))
            throw new IllegalArgumentException("Invalid project file has been specified.");*/
        
        this.projectPath = projectPath.toAbsolutePath();
        
        /* Convert file system path to URI with "jar:file:/" protocol. */
        final String path = this.projectPath.toUri().toString();
        final URI projectUri = URI.create(path.replace("file:///","jar:file:/"));
        
        /* Configure Zip File System (ZPFS) properties. */
        final HashMap<String, String> env = new HashMap<>();
        env.put("encoding", "utf-8");
        
        if (!projectPath.toFile().exists())
            env.put("create", "true");
        else
            env.put("create", "false");
        
        /* Open the specified zip file as a logical file system. */
        this.projectFileSystem = FileSystems.newFileSystem(projectUri, env);
        /* Create directory structure if the zip file has just been created.*/
        if (env.get("create").equals("true")) {
            Files.createDirectory(this.projectFileSystem.getPath(RESOURCE_DIR));
            Files.createDirectory(this.projectFileSystem.getPath(AUDIO_RESOURCE_DIR));
            Files.createDirectory(this.projectFileSystem.getPath(IMAGE_RESOURCE_DIR));
        }
    }
    
    public Path getProjectPath() {
        return this.projectPath;
    }
    
    public FileSystem getProjectFileSystem() {
        return this.projectFileSystem;
    }
    
    public ImageResource getImageResource(String name) {
        return null;
    }
    
    public AudioResource getAudioResource(String name) {
        return null;
    }
    
    @Override
    protected void release() throws IOException {
        this.projectFileSystem.close();
    }
}