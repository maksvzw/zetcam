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

import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author Lenny Knockaert
 */
public final class AggregateUndoableCommand extends AbstractUndoableCommand
{
    private final UndoableCommand[] commands;
    
    public AggregateUndoableCommand(final Collection<UndoableCommand> c) {
        this("", c.toArray(new UndoableCommand[c.size()]));
    }
    
    public AggregateUndoableCommand(final UndoableCommand... cmds) {
        this("", cmds);
    }
    
    public AggregateUndoableCommand(final String descr, final Collection<UndoableCommand> c) {
        this(descr, c.toArray(new UndoableCommand[c.size()]));
    }
    
    public AggregateUndoableCommand(final String descr, final UndoableCommand... cmds) 
    {
        super(descr);
        
        if (cmds == null || cmds.length <= 0)
            throw new IllegalArgumentException("No command have been specified.");
        
        this.commands = Arrays.copyOf(cmds, cmds.length);
    }
    
    @Override
    protected boolean onExecute() 
    {
        boolean haveAllExecuted = false;
        for(int i = 0; i < this.commands.length; i++) {
            this.commands[i].execute();
            haveAllExecuted |= this.commands[i].hasExecuted();
        }
        return haveAllExecuted;
    }

    @Override
    protected boolean onUndo()
    {
        boolean haveAllBeenUndone = false;
        for(int i = this.commands.length - 1; i >= 0; i--) {
            this.commands[i].undo();
            haveAllBeenUndone |= this.commands[i].isUndone();
        }
        return haveAllBeenUndone;
    }
}