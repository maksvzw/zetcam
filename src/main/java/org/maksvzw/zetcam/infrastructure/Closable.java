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
package org.maksvzw.zetcam.infrastructure;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Lenny Knockaert
 */
public abstract class Closable implements AutoCloseable
{
    private AtomicBoolean isOpen = new AtomicBoolean(false);
    
    public final boolean isOpen() {
        return this.isOpen.get();
    }
    
    protected final void setOpen(boolean isOpen) {
        this.isOpen.set(isOpen);
    }
    
    protected void checkOpen() {
        if (this.isOpen.get())
            throw new IllegalStateException("This object has already been released from memory.");
    }
    
    @Override
    public final void close() throws Exception
    {
        if (!this.isOpen.get())
            return;
        
        this.release();
        this.isOpen.set(false);
    }
    
    protected abstract void release() throws Exception;
}
