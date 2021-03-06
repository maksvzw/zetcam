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
package org.maksvzw.zetcam.infrastructure.collections;

import java.util.Collection;

/**
 *
 * @param <E>
 * @author Lenny Knockaert
 */
public class CircularFifoBuffer<E> extends BoundedFifoBuffer<E>
{
    public CircularFifoBuffer() 
    {
        super(32);
    }
    
    public CircularFifoBuffer(int capacity) 
    {
        super(capacity);
    }
    
    public CircularFifoBuffer(Collection<? extends E> items) 
    {
        super(items);
    }

    @Override
    public E write(E element) 
    {
        if (this.isFull())
            this.read();
        
        return super.write(element);
    }
}