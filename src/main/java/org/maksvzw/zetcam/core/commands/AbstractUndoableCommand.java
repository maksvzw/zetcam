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

import org.maksvzw.zetcam.core.utils.Bits;

/**
 *
 * @author Lenny Knockaert
 */
public abstract class AbstractUndoableCommand extends AbstractCommand implements UndoableCommand
{
    private boolean isUndone;
    
    public AbstractUndoableCommand() 
    {
        super();
        this.isUndone = false;
    }
    
    public AbstractUndoableCommand(final String descr) 
    {
        super(descr);
        this.isUndone = false;
    }
    
    @Override
    public boolean isUndoable() {
        return this.hasExecuted() && !this.isUndone;
    }

    @Override
    public boolean isUndone() {
        return this.isUndone;
    }
    
    void setUndone(boolean isUndone) {
        this.isUndone = isUndone;
    }

    @Override
    public void execute() 
    {
        if (this.hasExecuted())
            return;
        
        super.execute();
        
        if (this.hasExecuted())
            this.isUndone = false;
    }

    @Override
    public void undo() 
    {
        if (!this.isUndoable())
            return;
        
        try {
            if (this.onUndo()) {
                this.isUndone = true;
                this.setExecuted(false);
            }
            this.setFailed(false);
        }
        catch (Exception ex) {
            this.setFailed(true);
            throw ex;
        }
    }
    
    protected abstract boolean onUndo();
}