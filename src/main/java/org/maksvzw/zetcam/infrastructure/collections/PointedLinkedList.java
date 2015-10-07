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

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * 
 * @param <E> 
 * @author Lenny
 */
// TODO: finish Javadoc documentation
public class PointedLinkedList<E> implements PointedList<E>, Serializable
{
    private Node<E> head;
    private Node<E> tail;
    private Node<E> current;
    private int currentIndex;
    private int size;
    private transient int modCount;
    
    public PointedLinkedList() 
    {
        this.head = null;
        this.tail = null;
        this.current = null;
        this.currentIndex = 0;
        this.size = 0;
        this.modCount = 0;
    }
    
    public PointedLinkedList(Iterable<E> elements)
    {
        this();
        for(E element : elements)
            this.append(element);
        /* The current element should be the last element, so we should cycle 
        back to the start of this list. */
        this.cycleRight();
    }
    
    /**
     * Returns the number of elements in this list.
     * @return the number of elements in this list
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Determines whether this list contains no elements.
     * @return true if this list contains no elements
     */
    @Override
    public boolean isEmpty() {
        return this.size <= 0;
    }

    /**
     * Determines whether the current element is the first element in this list.
     * @return true if the current element is the first element in this list.
     */
    @Override
    public boolean atStart() {
        return this.current != null && this.current == this.head;
    }

    /**
     * Determines whether the current element is the last element in this list.
     * @return true if the current element is the last element in this list.
     */
    @Override
    public boolean atEnd() {
        return this.current != null && this.current == this.tail;
    }
    
    /**
     * Returns the current element.
     * @return the current element
     */
    @Override
    public E getCurrent() 
    {
        if (this.isEmpty())
            return null;
        
        return this.current.item;
    }

    /**
     * Returns the index of the current element.
     * @return the index of the current element
     */
    @Override
    public int getCurrentIndex() {
        return this.currentIndex;
    }
    
    @Override
    public E peekFirst() 
    {
        if (this.isEmpty())
            return null;
        
        return this.head.item;
    }
    
    @Override
    public E peekLast() 
    {
        if (this.isEmpty())
            return null;
        
        return this.tail.item;
    }

    @Override
    public E peekRight() 
    {
        if (this.isEmpty() || this.atEnd())
            return null;
        
        return this.current.right.item;
    }

    @Override
    public E peekLeft() 
    {
        if (this.isEmpty() || this.atStart())
            return null;
        
        return this.current.left.item;
    }

    @Override
    public PointedLinkedList<E> moveLeft() 
    {
        if (this.isEmpty())
            throw new IllegalStateException("This list is empty.");
        if (this.atStart())
            throw new IllegalStateException("Cannot move left of the focus.");
        
        this.current = this.current.left;
        this.currentIndex--;
        return this;
    }

    @Override
    public PointedLinkedList<E> cycleLeft() 
    {
        if (this.isEmpty())
            return this;
        
        if (this.atStart()) {
            this.current = this.tail;
            this.currentIndex = this.size() - 1;
        } else {
            this.current = this.current.left;
            this.currentIndex--;
        }
        return this;
    }

    @Override
    public PointedLinkedList<E> moveRight() 
    {
        if (this.isEmpty())
            throw new IllegalStateException("This list is empty.");
        if (this.atEnd())
            throw new IllegalStateException("Cannot move right of the focus.");

        this.current = this.current.right;
        this.currentIndex++;
        return this;
    }

    @Override
    public PointedLinkedList<E> cycleRight() 
    {
        if (this.isEmpty())
            return this;
        
        if (this.atEnd()) {
            this.current = this.head;
            this.currentIndex = 0;
        } else {  
            this.current = this.current.right;
            this.currentIndex++;
        }
        return this;
    }

    @Override
    public PointedLinkedList<E> moveTo(int index) 
    {
        if (this.isEmpty())
            throw new IllegalStateException("This list is empty.");
        if (index < 0 || index >= this.size)
            throw new IndexOutOfBoundsException("Index: "+index+", Size: "+this.size);
        
        while(this.currentIndex < index)
            this.moveRight();
        while(index < this.currentIndex)
            this.moveLeft();
        return this;
    }
    
    @Override
    public PointedLinkedList<E> rewind()
    {
        this.current = this.head;
        this.currentIndex = 0;
        return this;
    }

    @Override
    public boolean find(E element) 
    {
        int i = 0;
        for(Node<E> node = this.head; node != null; node = node.right) {
            if (element.equals(node.item)) {
               this.current = node;
               this.currentIndex = i;
               return true;
            }
            i++;
        }
        return false;
    }
    
    @Override
    public boolean findLast(E element)
    {
        int i = this.size - 1;
        for(Node<E> node = this.tail; node != null; node = node.left) {
            if (element.equals(node.item)) {
               this.current = node;
               this.currentIndex = i;
               return true;
            }
            i--;
        }
        return false;
    }

    @Override
    public boolean find(Predicate<E> predicate) 
    {
        int i = 0;
        for(Node<E> node = this.head; node != null; node = node.right) {
            if (predicate.test(node.item)) {
               this.current = node;
               this.currentIndex = i;
               return true;
            }
            i++;
        }
        return false;
    }
    
    @Override
    public boolean findLast(Predicate<E> predicate) 
    {
        int i = this.size - 1;
        for(Node<E> node = this.tail; node != null; node = node.left) {
            if (predicate.test(node.item)) {
               this.current = node;
               this.currentIndex = i;
               return true;
            }
            i--;
        }
        return false;
    }

    @Override
    public PointedLinkedList<E> prepend(E element) 
    {
        if (element == null)
            throw new IllegalArgumentException("No new element has been specified.");
        
        this.linkFirst(element);
        this.current = this.head;
        this.currentIndex = 0;
        return this;
    }
    
    @Override
    public PointedLinkedList<E> append(E element)
    {
        if (element == null)
            throw new IllegalArgumentException("No new element has been specified.");
        
        this.linkLast(element);
        this.current = this.tail;
        this.currentIndex = this.size - 1;
        return this;
    }
    
    @Override
    public PointedLinkedList<E> insertBefore(E element) 
    {
        if (element == null)
            throw new IllegalArgumentException("No new element has been specified.");
        
        if (this.atStart()) {
            this.prepend(element);
        } else {
            linkBefore(element, this.current);
            this.current = this.current.left;
        }
        return this;
    }

    @Override
    public PointedLinkedList<E> insertAfter(E element) 
    {
        if (element == null)
            throw new IllegalArgumentException("No new element has been specified.");
        
        if (this.atEnd()) {
            this.append(element);
        } else {
            linkBefore(element, this.current.right);
            this.current = this.current.right;
            this.currentIndex++;
        }
        return this;
    }

    @Override
    public PointedLinkedList<E> replace(E element) 
    {
        if (element == null)
            throw new IllegalArgumentException("No new element has been specified.");
        if (this.isEmpty())
            throw new IllegalArgumentException("Cannot replace the current item because this list is empty.");
        
        this.current.item = element;
        return this;
    }
    
    @Override
    public boolean swapLeft() 
    {
        if (this.isEmpty())
            throw new IllegalStateException("This list is empty.");
        if (this.atStart())
            return false;
        
        Node<E> previous = this.current;
        this.current = this.current.left;
        this.currentIndex--;
        this.swapItems(previous, this.current);
        return true;
    }
    
    @Override
    public boolean swapLeftCycle() 
    {
        if (this.isEmpty())
            throw new IllegalStateException("This list is empty.");
        
        Node<E> previous = this.current;
        if (this.atStart()) {
            if (this.atEnd())
                return false;
            this.current = this.tail;
            this.currentIndex = this.size - 1;
        } else {
            this.current = this.current.left;
            this.currentIndex--;
        }
        this.swapItems(previous, this.current);
        return true;
    }
    
    @Override
    public boolean swapRight()
    {
        if (this.isEmpty())
            throw new IllegalStateException("This list is empty.");
        if (this.atEnd())
            return false;
        
        Node<E> previous = this.current;
        this.current = this.current.right;
        this.currentIndex++;
        this.swapItems(previous, this.current);
        return true;
    }

    @Override
    public boolean swapRightCycle() 
    {
        if (this.isEmpty())
            throw new IllegalStateException("This list is empty.");
        
        Node<E> previous = this.current;
        if (this.atEnd()) {
            if (this.atStart())
                return false;
            this.current = this.head;
            this.currentIndex = 0;
        } else {
            this.current = this.current.right;
            this.currentIndex++;
        }
        this.swapItems(previous, this.current);
        return true;
    }
    
    @Override
    public boolean swap(int index) 
    {
        if (this.isEmpty())
            throw new IllegalStateException("This list is empty.");
        if (index < 0 || index >= this.size)
            throw new IndexOutOfBoundsException("Index: "+index+", Size: "+this.size);
        
        if (this.currentIndex != index) {
            Node<E> previous = this.current;
            this.moveTo(index);
            this.swapItems(previous, this.current);
        }
        return true;
    }
    
    @Override
    public E deleteLeft() 
    {
        if (this.isEmpty())
            throw new IllegalStateException("This list is empty.");
        
        Node<E> previous = this.current;
        // If the current element is the first element in this list, move to  
        // the right instead.
        if (this.atStart()) {
            // If this element is the only element in this list, reset our 
            // pointer data.
            if (this.atEnd()) {
                this.current = null;
                this.currentIndex = -1;
            } else {
                this.current = this.current.right;
            }
        }
        // Otherwise, move left of the current element in this list.
        else {
            this.current = this.current.left;
            this.currentIndex--;
        }
        return this.unlink(previous);
    }

    @Override
    public E deleteLeftCycle() 
    {
        if (this.isEmpty())
            throw new IllegalStateException("This list is empty.");
        
        Node<E> previous = this.current;
        // If the current element is the first element in this list, move to  
        // the last element instead. 
        if (this.atStart()) {
            // If this element is the only element in this list, reset our 
            // pointer data.
            if (this.atEnd()) {
                this.current = null;
                this.currentIndex = -1;
            } else {
                this.current = this.tail;
                this.currentIndex = this.size - 1;
            }
        // Otherwise, move left of the current element in this list.
        } else {
            this.current = this.current.left;
            this.currentIndex--;
        }
        return this.unlink(previous);
    }

    @Override
    public E deleteRight() 
    {
        if (this.isEmpty())
            throw new IllegalStateException("This list is empty.");
        
        Node<E> previous = this.current;
        // If the current element is the first element in this list, move to  
        // the left instead.
        if (this.atEnd()) {
            // If this element is the only element in this list, reset our 
            // pointer data.
            if (this.atStart()) {
                this.current = null;
                this.currentIndex = -1;
            } else {
                this.current = this.current.left;
                this.currentIndex--;
            }
        // Otherwise, move right of the current element in this list.
        } else {
            this.current = this.current.right;
        }
        return this.unlink(previous);
    }

    @Override
    public E deleteRightCycle()
    {
        if (this.isEmpty())
            throw new IllegalStateException("This list is empty.");
        
        Node<E> previous = this.current;
        // If the current element is the first element in this list, move to  
        // the first element instead.
        if (this.atEnd()) {
            // If this element is the only element in this list, reset our 
            // pointer data.
            if (this.atStart()) {
                this.current = null;
                this.currentIndex = -1;
            } else {
                this.current = this.head;
                this.currentIndex = 0;
            }
        // Otherwise, move right of the current element in this list.
        } else {
            this.current = this.current.right;
        }
        return this.unlink(previous);
    }
    
    @Override
    public PointedLinkedList<E> clear() 
    {
        this.head = null;
        this.tail = null;
        this.current = null;
        this.currentIndex = -1;
        this.size = 0;
        this.modCount++;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E[] toArray(E[] array)
    {
        if (array == null)
            throw new IllegalArgumentException("No array has been specified.");
        
        E[] result;
        Class<?> elementClass = array.getClass().getComponentType();
        // If the given array is too small to contain all the elements in this
        // list, allocate a new array with sufficient capacity. We can now do 
        // this since the given array reveals which class the type parameter 
        // should be or should be a subclass of.
        if (array.length < this.size) 
            result = (E[])Array.newInstance(elementClass, this.size);
        else 
            result = array;
        
        int i = 0;
        for(Node<E> node = this.head; node != null; node = node.right) {
            if (!elementClass.isAssignableFrom(node.item.getClass())) 
                throw new ArrayStoreException("Not all of the elements in this list are assignable from "+elementClass.getName()+".");
            
            result[i++] = node.item;
        }
        return result;
    }
    
    @Override
    public Iterator<E> leftsIterator() 
    {
        if (this.isEmpty())
            return new DescendingIterator();
        
        return new DescendingIterator(this.current.left);
    }

    @Override
    public Iterator<E> rightsIterator() 
    {
        if (this.isEmpty())
            return new AscendingIterator();
        
        return new AscendingIterator(this.current.right);
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new DescendingIterator();
    }

    @Override
    public Iterator<E> iterator() {
        return new AscendingIterator();
    }
    
    private void linkFirst(E element) 
    {
        final Node<E> first = this.head;
        final Node<E> newNode = new Node<>(element, null, first);
        
        this.head = newNode;
        if (first == null)
            this.tail = newNode;
        else
            first.left = newNode;
        
        this.size++;
        this.modCount++;
    }
    
    private void linkLast(E element)
    {
        final Node<E> last = this.tail;
        final Node<E> newNode = new Node<>(element, last, null);

        this.tail = newNode;
        if (last == null)
            this.head = newNode;
        else
            last.right = newNode;
        
        this.size++;
        this.modCount++;
    }
    
    private void linkBefore(E element, Node<E> nextNode)
    {
        final Node<E> prevNode = nextNode.left;
        final Node<E> newNode = new Node<>(element, prevNode, nextNode);
        
        nextNode.left = newNode;
        if (prevNode == null)
            this.head = newNode;
        else
            prevNode.right = newNode;
        
        this.size++;
        this.modCount++;
    }
    
    private E unlink(Node<E> node) 
    {
        final E element = node.item;
        node.item = null;
        final Node<E> prevNode = node.left;
        final Node<E> nextNode = node.right;
        
        if (prevNode == null) {
            this.head = nextNode;
        }
        else {
            prevNode.right = nextNode;
            node.left = null;
        }
        if (nextNode == null) {
            this.tail = prevNode;
        }
        else {
            nextNode.left = prevNode;
            node.right = null;
        }
        this.size--;
        this.modCount++;
        return element;
    }
    
    private void swapItems(Node<E> firstNode, Node<E> secondNode) 
    {
        final E firstNodeItem = firstNode.item;
        firstNode.item = secondNode.item;
        secondNode.item = firstNodeItem;
        this.modCount++;
    }
    
    ///////////////////////////////
    ///     Iterator Classes    ///
    ///////////////////////////////
    
    private abstract class PointedLinkedListIterator implements Iterator<E> 
    {
        private Node<E> nextNode;
        private final int expectedModCount;
        
        public PointedLinkedListIterator(Node<E> startNode) 
        {
            this.nextNode = startNode;
            this.expectedModCount = modCount;
        }
        
        @Override
        public boolean hasNext() {
            return this.nextNode != null;
        }
        
        @Override
        public E next() 
        {
            if (this.expectedModCount != modCount)
                throw new ConcurrentModificationException("The underlying collection has been modified outside of this iterator.");
            if (!this.hasNext()) 
                throw new NoSuchElementException();
            
            E element = this.nextNode.item;
            this.nextNode = this.getNextNode(this.nextNode);
            return element;
        }
        
        protected abstract Node<E> getNextNode(Node<E> lastNode);
    }
    
    private class AscendingIterator extends PointedLinkedListIterator
    {
        public AscendingIterator()
        {
            this(head);
        }
        
        public AscendingIterator(Node<E> startNode) 
        {
            super(startNode);
        }

        @Override
        protected Node<E> getNextNode(Node<E> lastNode) {
            return lastNode.right;
        }
    }
    
    private class DescendingIterator extends PointedLinkedListIterator
    {
        public DescendingIterator()
        {
           this(tail); 
        }
        
        public DescendingIterator(Node<E> startNode)
        {
            super(startNode);
        }

        @Override
        protected Node<E> getNextNode(Node<E> lastNode) {
            return lastNode.left;
        }
    }
    
    ///////////////////////////////////////
    ///     Double-Linked Node Class    ///
    ///////////////////////////////////////
    
    private static class Node<E> implements Serializable
    {
        public Node<E> left;
        public Node<E> right;
        public E item;

        public Node(E element, Node<E> left, Node<E> right) 
        {
            this.item = element;
            this.left = left;
            this.right = right;
        }
    }
}