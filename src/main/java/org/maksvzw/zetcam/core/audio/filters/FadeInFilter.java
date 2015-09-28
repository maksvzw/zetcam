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

import java.time.Duration;

/**
 *
 * @author Lenny Knockaert
 */
public final class FadeInFilter extends FadeFilter
{
    public FadeInFilter(Duration startTime, Duration duration, CurveType curveType) {
        super(startTime, duration, curveType);
    }

    @Override
    public FadeDirection getDirection() {
        return FadeDirection.IN;
    }

    @Override
    protected boolean shouldIgnoreSamples(long fadeStartSample, long fadeNumOfSamples, long currentSample, long numOfSamples) {
        return (fadeStartSample + fadeNumOfSamples) < currentSample; // after fade-in
    }

    @Override
    protected boolean shouldSilenceSamples(long fadeStartSample, long fadeNumOfSamples, long currentSample, long numOfSamples) {
        return (currentSample + numOfSamples) < fadeStartSample; // before fade-in
    }

    @Override
    protected long getCurrentFadeSample(long fadeStartSample, long fadeNumOfSamples, long currentSample) {
        return currentSample - fadeStartSample;
    }

    @Override
    protected int getDirectionSignum() {
        return 1;
    }
}