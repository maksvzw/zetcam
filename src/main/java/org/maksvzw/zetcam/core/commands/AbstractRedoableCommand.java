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
package org.maksvzw.zetcam.core.commands;

/**
 *
 * @author Lenny Knockaert
 */
public abstract class AbstractRedoableCommand extends AbstractUndoableCommand implements RedoableCommand
{
    public AbstractRedoableCommand() {
        super();
    }
    
    public AbstractRedoableCommand(final String descr) {
        super(descr);
    }
    
    @Override
    public boolean isRedoable() {
        return this.isUndone();
    }

    @Override
    public void undo()
    {
        if (!this.isUndoable())
            return;
        
        super.undo();
        
        if (!this.hasExecuted())
            this.setExecuted(true);
    }
    
    
    @Override
    public void redo()
    {
        if (!this.isRedoable())
            return;
        
        try {
            if (this.onRedo())
                this.setUndone(false);
            
            this.setFailed(false);
        }
        catch (Exception ex) {
            this.setFailed(true);
            throw ex;
        }
    }
    
    protected abstract boolean onRedo();
}