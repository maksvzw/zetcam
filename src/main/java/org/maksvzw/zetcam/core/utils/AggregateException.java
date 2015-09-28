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
package org.maksvzw.zetcam.core.utils;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an exception with caused by one or more exceptions that occur 
 * during application execution.
 * One would usually need this kind of exception when processing a series of
 * isolated, equivalent steps where an exception thrown by one step should not 
 * prevent the execution of the other steps. In such a case, one would catch 
 * and store the exception into a temporarily list in order to construct this
 * aggregate exception when all the steps have been handled.
 * 
 * @author Lenny
 */
public class AggregateException extends RuntimeException
{
    private final List<Throwable> innerCauses;
    
    /**
     * Creates a new aggregate exception using the specified message and the 
     * specified causes.
     * @param message the message detailing this exception
     * @param causes the causes for this exception
     * @throws IllegalArgumentException if the specified causes are null or 
     * empty
     */
    public AggregateException(String message, Iterable<Throwable> causes) 
    {
        super(message);
        
        if (causes == null) 
            throw new IllegalArgumentException("No causes have been specified.");
        
        List<Throwable> innerCauses = new ArrayList<>();
        
        for(Throwable cause : causes) {
            if (cause != null)
                innerCauses.add(cause);
        }

        if (innerCauses.size() < 1)
            throw new IllegalArgumentException("No causes have been specified.");

        this.innerCauses = Collections.unmodifiableList(innerCauses);
    }
    
    /**
     * Returns the cause of this throwable or null if the cause is nonexistent 
     * or unknown. 
     * (The cause is the throwable that caused this throwable to get thrown.)
     * @return the cause of this throwable or null if the cause is nonexistent 
     * or unknown.
     */
    @Override
    public Throwable getCause() {
        return this.innerCauses.get(0);
    }
    
    /**
     * Returns the causes of this throwable or null if the causes are unknown or
     * nonexistent.
     * (The causes are the throwables that caused this throwable to get thrown.)
     * @return the causes of this throwable or null if the cause is nonexistent 
     * or unknown.
     */
    public List<Throwable> getCauses() {
        return this.innerCauses;
    }
    
    /**
     * Prints this throwable and its backtrace to the specified print stream.
     * @param err PrintStream to use for output
     */
    @Override
    public void printStackTrace(PrintStream err) 
    {
        super.printStackTrace(err);

        int currentIndex = -1;
        for (Throwable cause : this.innerCauses) {
            err.append("\n");
            err.append("  Inner throwable #");
            err.append(Integer.toString(++currentIndex));
            err.append(": ");
            cause.printStackTrace(err);
            err.append("\n");
        }
    }

    /**
     * Prints this throwable and its backtrace to the specified print writer.
     * @param err PrintWriter to use for output
     */
    @Override
    public void printStackTrace(PrintWriter err) 
    {
        super.printStackTrace(err);

        int currentIndex = -1;
        for (Throwable throwable : this.innerCauses) {
            err.append("\n");
            err.append("  Inner throwable #");
            err.append(Integer.toString(++currentIndex));
            err.append(": ");
            throwable.printStackTrace(err);
            err.append("\n");
        }
    }
}