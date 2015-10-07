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

import java.util.Iterator;
import java.util.function.Predicate;

/**
 *
 * @param <E>
 * @author Lenny
 */
public interface PointedList<E> extends Iterable<E>
{
    /**
     * Returns the number of elements in this list.
     * @return the number of elements in this list
     */
    int size();
    
    /**
     * Determines whether this list contains no elements.
     * @return true if this list contains no elements
     */
    boolean isEmpty();
    
    /**
     * Determines whether the current element is the first element in this list.
     * @return true if the current element is the first element in this list.
     */
    boolean atStart();
    
    /**
     * Determines whether the current element is the last element in this list.
     * @return true if the current element is the last element in this list.
     */
    boolean atEnd();
    
    /**
     * Returns the current element.
     * @return the current element
     */
    E getCurrent();

    /**
     * Returns the index of the current element.
     * @return the index of the current element
     */
    int getCurrentIndex();
    
    E peekFirst();
    E peekLast();
    E peekRight();
    E peekLeft();
    
    PointedList<E> moveLeft();
    PointedList<E> cycleLeft();
    PointedList<E> moveRight();
    PointedList<E> cycleRight();
    PointedList<E> moveTo(int index);
    PointedList<E> rewind();
    
    boolean find(E element);
    boolean findLast(E element);
    boolean find(Predicate<E> predicate);
    boolean findLast(Predicate<E> predicate);
    
    PointedList<E> prepend(E element);
    PointedList<E> append(E element);
    PointedList<E> insertBefore(E element);
    PointedList<E> insertAfter(E element);
    PointedList<E> replace(E element);
    
    boolean swapLeft();
    boolean swapRight();
    boolean swapLeftCycle();
    boolean swapRightCycle();
    boolean swap(int index);
    
    E deleteLeft();
    E deleteLeftCycle();
    E deleteRight();
    E deleteRightCycle();
    
    PointedList<E> clear();
    E[] toArray(E[] array);
    
    Iterator<E> leftsIterator();
    Iterator<E> rightsIterator();
    Iterator<E> descendingIterator();
}