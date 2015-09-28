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
package org.maksvzw.zetcam.core.utils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Lenny Knockaert
 */
public abstract class Disposable implements AutoCloseable
{
    private AtomicBoolean isClosed = new AtomicBoolean(false);
    
    public final boolean isClosed() {
        return this.isClosed.get();
    }
    
    protected void checkClosed() {
        if (this.isClosed.get())
            throw new IllegalStateException("This object has already been released from memory.");
    }
    
    @Override
    public final void close() throws Exception
    {
        if (this.isClosed.get())
            return;
        
        this.release();
        this.isClosed.set(true);
    }
    
    protected abstract void release() throws Exception;
}