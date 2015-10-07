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

import java.util.Collections;
import java.util.EventObject;
import java.util.List;

/**
 * An event which indicates that a collection has undergone a change.
 * This event notifies listeners of ADD, REMOVE, REPLACE, MOVE and RESET changes
 * that have occurred and information related to them. 
 * This class cannot be instantiated except through the static factory method 
 * interface.
 * <p>
 * This implementation is inspired by and based on the implementation of the
 * NotifyCollectionChangeEventArgs class in .NET, but using the factory method 
 * instead of an array of confusing and pointless constructors.
 * @see https://msdn.microsoft.com/en-us/library/system.collections.specialized.notifycollectionchangedeventargs%28v=vs.110%29.aspx
 * </p>
 * 
 * @author Lenny
 */
public class CollectionChangeEvent extends EventObject
{
    /**
     * Creates a new CollectionChangedEvent that describes a reset change.
     * @param source the source that will publish this CollectionChangedEvent
     * @return a new CollectionChangedEvent that describes a reset change
     */
    public static CollectionChangeEvent createResetEvent(Object source) {
        return new CollectionChangeEvent(
                source, 
                CollectionChange.RESET, 
                null, 
                null, 
                -1, 
                -1);
    }
    
    /**
     * Creates a new CollectionChangedEvent that describes an addition change.
     * @param source the source that will publish this CollectionChangedEvent
     * @param newItem the item that has been added 
     * @return a new CollectionChangedEvent that describes an addition change
     */
    public static CollectionChangeEvent createAddEvent(Object source, Object newItem) {
        return createAddEvent(source, Collections.singletonList(newItem), -1);
    }
    
    /**
     * Creates a new CollectionChangedEvent that describes an addition change.
     * @param source the source that will publish this CollectionChangedEvent
     * @param newItem the item that has been added 
     * @param index the index where the item has been added
     * @return a new CollectionChangedEvent that describes an addition change
     */
    public static CollectionChangeEvent createAddEvent(Object source, Object newItem, int index) {
        return createAddEvent(source, Collections.singletonList(newItem), index);
    }
    
    /**
     * Creates a new CollectionChangedEvent that describes an addition change.
     * @param source the source that will publish this CollectionChangedEvent
     * @param newItems the items that have been added
     * @return a new CollectionChangedEvent that describes an addition change
     */
    public static CollectionChangeEvent createAddEvent(Object source, List newItems) {
        return createAddEvent(source, newItems, -1);
    }
    
    /**
     * Creates a new CollectionChangedEvent that describes an addition change.
     * @param source the source that will publish this CollectionChangedEvent
     * @param newItems the items that have been added
     * @param startIndex the starting index where the new items added
     * @return a new CollectionChangedEvent that describes an addition change
     */
    public static CollectionChangeEvent createAddEvent(Object source, List newItems, int startIndex) 
    {
        if (newItems == null)
            throw new IllegalArgumentException("No changed items have been specified.");
        if (startIndex < -1)
            throw new IllegalArgumentException("The specified starting index cannot be negative.");
        
        return new CollectionChangeEvent(source, CollectionChange.ADD, newItems, null, startIndex, -1);
    }
    
    /**
     * Creates a new CollectionChangedEvent that describes an removal change.
     * @param source the source that will publish this CollectionChangedEvent
     * @param oldItem the item that has been removed
     * @return a new CollectionChangedEvent that describes an removal change
     */
    public static CollectionChangeEvent createRemoveEvent(Object source, Object oldItem) {
        return createRemoveEvent(source, Collections.singletonList(oldItem), -1);
    }
    
    /**
     * Creates a new CollectionChangedEvent that describes an removal change.
     * @param source the source that will publish this CollectionChangedEvent
     * @param oldItem the item that has been removed
     * @param index the index where the item has been removed
     * @return a new CollectionChangedEvent that describes an removal change
     */
    public static CollectionChangeEvent createRemoveEvent(Object source, Object oldItem, int index) {
        return createRemoveEvent(source, Collections.singletonList(oldItem), index);
    }
    
    /**
     * Creates a new CollectionChangedEvent that describes an removal change.
     * @param source the source that will publish this CollectionChangedEvent
     * @param oldItems the items that have been removed
     * @return a new CollectionChangedEvent that describes an removal change
     */
    public static CollectionChangeEvent createRemoveEvent(Object source, List oldItems) {
        return createRemoveEvent(source, oldItems, -1);
    }
    
    /**
     * Creates a new CollectionChangedEvent that describes an removal change.
     * @param source the source that will publish this CollectionChangedEvent
     * @param oldItems the items that have been removed
     * @param startIndex the starting index where the items have been removed
     * @return a new CollectionChangedEvent that describes an removal change
     */
    public static CollectionChangeEvent createRemoveEvent(Object source, List oldItems, int startIndex)
    {
        if (oldItems == null)
            throw new IllegalArgumentException("No changed items have been specified.");
        if (startIndex < -1)
            throw new IllegalArgumentException("The specified starting index cannot be negative.");
        
        return new CollectionChangeEvent(source, CollectionChange.REMOVE, null, oldItems, -1, startIndex);
    }
    
    /**
     * Creates a new CollectionChangedEvent that describes a replacement change.
     * @param source the source that will publish this CollectionChangedEvent
     * @param newItem the new item that replaces the original
     * @param oldItem the original item that is replaced
     * @return a new CollectionChangedEvent that describes a replacement change
     */
    public static CollectionChangeEvent createReplaceEvent(Object source, Object newItem, Object oldItem) {
        return createReplaceEvent(source, Collections.singletonList(newItem), Collections.singletonList(oldItem), -1);
    }
    
    /**
     * Creates a new CollectionChangedEvent that describes a replacement change.
     * @param source the source that will publish this CollectionChangedEvent
     * @param newItem the new item that replaces the original
     * @param oldItem the original item that is replaced
     * @param index the index of the item being replaced
     * @return a new CollectionChangedEvent that describes a replacement change
     */
    public static CollectionChangeEvent createReplaceEvent(Object source, Object newItem, Object oldItem, int index) {
        return createReplaceEvent(source, Collections.singletonList(newItem), Collections.singletonList(oldItem), index);
    }
    
    /**
     * Creates a new CollectionChangedEvent that describes a replacement change.
     * @param source the source that will publish this CollectionChangedEvent
     * @param newItems the new items that replace the original items
     * @param oldItems the original items that are replaced.
     * @return a new CollectionChangedEvent that describes a replacement change
     */
    public static CollectionChangeEvent createReplaceEvent(Object source, List newItems, List oldItems) {
        return createReplaceEvent(source, newItems, oldItems, -1);
    }
    
    /**
     * Creates a new CollectionChangedEvent that describes a replacement change.
     * @param source the source that will publish this CollectionChangedEvent
     * @param newItems the new items that replace the original items
     * @param oldItems the original items that are replaced.
     * @param startIndex the starting index of the items being replaced
     * @return a new CollectionChangedEvent that describes a replacement change
     */
    public static CollectionChangeEvent createReplaceEvent(Object source, List newItems, List oldItems, int startIndex) 
    {
        if (newItems == null)
            throw new IllegalArgumentException("No new items have been specified.");
        if (oldItems == null)
            throw new IllegalArgumentException("No old items have been specified.");
        if (startIndex < -1)
            throw new IllegalArgumentException("The specified starting index cannot be negative.");
        
        return new CollectionChangeEvent(
                source, 
                CollectionChange.REPLACE, 
                newItems, 
                oldItems, 
                startIndex, 
                startIndex);
    }
    
    /**
     * Creates a new CollectionChangedEvent that describes a movement change.
     * @param source the source that will publish this CollectionChangedEvent
     * @param changedItem the item that is moved
     * @param index the new index for the item
     * @param oldIndex the old index of the item
     * @return a new CollectionChangedEvent that describes a movement change
     */
    public static CollectionChangeEvent createMoveEvent(Object source, Object changedItem, int index, int oldIndex) {
        return createMoveEvent(source, Collections.singletonList(changedItem), index, oldIndex);
    }
    
    /**
     * Creates a new CollectionChangedEvent that describes a movement change.
     * @param source the source that will publish this CollectionChangedEvent
     * @param changedItems the items that are moved
     * @param index the new index for the items
     * @param oldIndex the old index of the items
     * @return a new CollectionChangedEvent that describes a movement change
     */
    public static CollectionChangeEvent createMoveEvent(Object source, List changedItems, int index, int oldIndex) 
    {
        if (changedItems == null)
            throw new IllegalArgumentException("No changed items have been specified.");
        if (index < 0)
            throw new IllegalArgumentException("The specified index cannot be negative.");
        
        return new CollectionChangeEvent(
                source, 
                CollectionChange.MOVE, 
                changedItems, 
                changedItems, 
                index, 
                oldIndex);
    }
    
    private final CollectionChange change;
    private final List newItems;
    private final int newStartIndex;
    private final List oldItems;
    private final int oldStartIndex;
    
    /**
     * Constructs a new CollectionChangeEvent using the specified information.
     * @param change the change that caused this event
     * @param newItems the items that have been moved, added or that replace 
     * existing items.
     * @param oldItems the items that have been moved, removed or that are 
     * replaced.
     * @param newStartIndex the index at which the change occurred.
     * @param oldStartIndex the index at which the MOVE, REMOVE or REPLACE
     * action occurred
     */
    private CollectionChangeEvent(Object source, CollectionChange change, 
                                    List newItems, List oldItems, 
                                    int newStartIndex, int oldStartIndex) 
    {
        super(source);
        this.change = change;
        this.newItems = newItems == null ? Collections.emptyList() : Collections.unmodifiableList(newItems);
        this.oldItems = newItems == null ? Collections.emptyList() : Collections.unmodifiableList(oldItems);
        this.newStartIndex = newStartIndex < -1 ? -1 : newStartIndex;
        this.oldStartIndex = oldStartIndex < -1 ? -1 : oldStartIndex;
    }

    /**
     * Gets the change in the collection that caused this event.
     * @return the change in the collection that caused this event
     */
    public CollectionChange getChange() {
        return this.change;
    }
    
    /**
     * Gets the list of new items involved in the change.
     * @return the list of new items involved in the change
     */
    public List getNewItems() {
        return this.newItems;
    }
    
    /**
     * Gets the index at which the change occurred.
     * @return the index at which the change occurred
     */
    public int getNewStartIndex() {
        return this.newStartIndex;
    }
    
    /**
     * Gets the list of items affected by a REPLACE, REMOVE or MOVE action.
     * @return the list of items affected by a REPLACE, REMOVE or MOVE action 
     */
    public List getOldItems() {
        return this.oldItems;
    }

    /**
     * Gets the index at which a MOVE, REMOVE or REPLACE action occurred.
     * @return the index at which a MOVE, REMOVE or REPLACE action occurred
     */
    public int getOldStartIndex() {
        return this.oldStartIndex;
    }
}