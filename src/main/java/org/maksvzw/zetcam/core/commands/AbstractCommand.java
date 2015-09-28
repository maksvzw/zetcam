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

import java.util.EnumSet;
import java.util.Set;
import org.maksvzw.zetcam.core.utils.Bits;

/**
 *
 * @author Lenny Knockaert
 */
public abstract class AbstractCommand implements Command
{
    private boolean hasExecuted;
    private boolean hasFailed;
    private final String description;

    public AbstractCommand() 
    {
        this.hasExecuted = false;
        this.description = "";
    }
    
    public AbstractCommand(final String descr) 
    {
        if (descr == null || descr.isEmpty())
            throw new IllegalArgumentException("No command descriptions has been specified.");
        
        this.hasExecuted = false;
        this.hasFailed = false;
        this.description = descr;
    }
    
    @Override
    public boolean hasExecuted() {
        return this.hasExecuted;
    }
    
    void setExecuted(boolean hasExecuted) {
        this.hasExecuted = hasExecuted;
    }
    
    @Override
    public boolean hasFailed() {
        return this.hasFailed;
    }
    
    void setFailed(boolean hasFailed) {
        this.hasFailed = hasFailed;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
    
    @Override
    public void execute() 
    {
        if (this.hasExecuted())
            return;
        
        try {
            if (this.onExecute()) 
                this.hasExecuted = true;

            this.hasFailed = false;
        } 
        catch(Exception ex) {
            this.hasFailed = true;
            throw ex;
        }
    }
    
    protected abstract boolean onExecute();
}
