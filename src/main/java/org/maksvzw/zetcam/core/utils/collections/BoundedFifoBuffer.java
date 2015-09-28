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
package org.maksvzw.zetcam.core.utils.collections;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * 
 * @param <E>
 * @author Lenny Knockaert
 */
public class BoundedFifoBuffer<E> extends AbstractCollection<E> implements RandomAccess
{
    private final int capacity;
    private final List<E> buffer;
    private int head;
    private int tail;
    private int modCount;
    
    public BoundedFifoBuffer() 
    {
        this(32);
    }
    
    public BoundedFifoBuffer(int capacity) 
    {
        if (capacity < 1)
            throw new IllegalArgumentException("The capacity of this buffer cannot be smaller than one");
        
        this.capacity = capacity + 1;
        this.buffer = new ArrayList<>(Collections.nCopies(this.capacity, null));
        this.head = 0;
        this.tail = 0;
        this.modCount = 0;
    }
    
    public BoundedFifoBuffer(Collection<? extends E> items) 
    {
        this(items.size());
        this.addAll(items);
    }
    
    public int capacity() {
        return this.capacity - 1;
    }
    
    public boolean isFull() {
        return ((this.tail + 1) % this.capacity) == this.head;
    }
    
    @Override
    public boolean isEmpty() {
        return this.tail == this.head;
    }
    
    @Override
    public int size()
    {
        int size = this.tail - this.head;
        if (this.tail < this.head)
            size += this.capacity;
        return size;
    }
    
    @Override
    public boolean contains(Object obj) 
    {
        if (obj == null)
            return false;
        
        for(int i = 0; i < this.size(); i++) 
            if (this.get(i).equals(obj))
                return true;
        return false;
    }
    
    public E get(int index) 
    {
        if (index < 0 || index >= this.size())
            throw new IndexOutOfBoundsException("Index: "+index+"; Size: "+this.size());
        
        return this.buffer.get((this.head + index) % this.capacity);
    }

    public E set(int index, E element) 
    {
        if (index < 0 || index >= this.size())
            throw new IndexOutOfBoundsException("Index: "+index+"; Size: "+this.size());
        if (element == null)
            throw new IllegalArgumentException("No element has been specified.");
        
        E oldElement = this.buffer.set((this.head + index) % this.capacity, element);
        this.modCount++;
        return oldElement;
    }
    
    public E write(E element) 
    {
        if (element == null)
            throw new IllegalArgumentException("No element has been specified.");
        if (this.isFull())
            throw new BufferOverflowException();
        
        E oldElement = this.buffer.set(this.tail, element);
        this.tail = (this.tail + 1) % this.capacity;
        this.modCount++;
        return oldElement;
    }
    
    public E read() 
    {
        if (this.isEmpty())
            throw new BufferUnderflowException();
        
        E element = this.buffer.set(this.head, null);
        this.head = (this.head + 1) % this.capacity;
        this.modCount++;
        return element;
    }
    
    @Override
    public boolean add(E element) 
    {
        this.write(element);
        return true;
    }
    
    @Override
    public final boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() 
    {
        this.buffer.clear();
        this.head = 0;
        this.tail = 0;
        this.modCount++;
    }
    
    @Override
    public Iterator<E> iterator() {
        return new BoundedFifoBufferIterator();
    }
    
    private class BoundedFifoBufferIterator implements Iterator<E> 
    {
        private int index;
        private final int size;
        private final int expectedModCount;
        
        public BoundedFifoBufferIterator() 
        {
            this.index = head;
            this.size = size();
            this.expectedModCount = modCount;
        }
        
        @Override
        public boolean hasNext() {
            return this.index < this.size;
        }

        @Override
        public E next() 
        {
            if (!this.hasNext()) 
                throw new NoSuchElementException();
            if (this.expectedModCount != modCount)
                throw new ConcurrentModificationException("The underlying collection has been modified outside of this iterator.");
            
            return get(this.index++);
        }
    }
}
