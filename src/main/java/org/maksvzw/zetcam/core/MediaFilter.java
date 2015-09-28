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
package org.maksvzw.zetcam.core;

import org.maksvzw.zetcam.core.MediaType;

/**
 *
 * @author Lenny Knockaert
 * @param <T>
 */
public abstract class MediaFilter<T>
{
    private MediaFilter<T> nextFilter;
    private MediaFilter<T> prevFilter;
    
    public MediaFilter() {
        this.nextFilter = null;
    }
    
    public abstract MediaType getType();
    
    public final MediaFilter<T> next() {
        return this.nextFilter;
    }
    
    public final MediaFilter<T> prev() {
        return this.prevFilter;
    }
    
    public final void setNext(MediaFilter<T> filter)
    {
        if (filter == null)
            throw new IllegalArgumentException("No "+this.getType().name().toLowerCase()+" filter has been specified.");
        if (filter.getType() != this.getType())
            throw new IllegalArgumentException("The specified filter is not an "+this.getType().name().toLowerCase()+" filter.");
        
        if (this.nextFilter != null)
            this.nextFilter.prevFilter = null;
        
        this.nextFilter = filter;
        this.nextFilter.prevFilter = this;
    }
    
    public final boolean hasNext() {
        return this.nextFilter != null;
    }
    
    public T filter(final T media)
    {
        if (media == null)
            throw new IllegalArgumentException("No source media has been specified.");
        
        final T dstMedia = this.onFilter(media);
        
        if (this.nextFilter != null)
            return this.nextFilter.filter(dstMedia);
        
        return dstMedia;
    }
    
    protected abstract T onFilter(final T media);
    
    public void reset() 
    {
        if (this.nextFilter != null)
            this.nextFilter.reset();
    }
}