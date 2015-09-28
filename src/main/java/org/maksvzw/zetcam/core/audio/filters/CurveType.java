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
package org.maksvzw.zetcam.core.audio.filters;

/**
 * Curve types for fade transitions.
 * 
 * @author Lenny Knockaert
 */
public enum CurveType 
{
    /** select triangular, linear slope (default) */
    TRI,
    /** select quarter of sine wave */
    QSIN,
    /** select inverted quarter of sine wave */
    IQSIN,
    /** select exponential sine wave*/
    ESIN,
    /** select half of sine wave */
    HSIN,
    /** select inverted half of sine wave */
    IHSIN,
    /** select logarithmic */
    LOG,
    /** select parabola */
    PAR,
    /** select inverted parabola */
    IPAR,
    /** select quadratic */
    QUA,
    /** select cubic */
    CUB,
    /** select square root */
    SQU,
    /** select cubic root */
    CBR,
    /** select exponential */
    EXP,
    /** select double-exponential seat */
    DESE,
    /** select double-exponential sigmoid */
    DESI;
}