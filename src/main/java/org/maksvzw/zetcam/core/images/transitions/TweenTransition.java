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
package org.maksvzw.zetcam.core.images.transitions;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 *
 * @author Lenny Knockaert
 */
public class TweenTransition implements ImageTransition
{
    private final BufferedImage srcImage;
    private final BufferedImage dstImage;
    private final int totalNumOfTransitions;
    private int currentTransitionStep;
    
    public TweenTransition(
            final BufferedImage src, 
            final BufferedImage dst, 
            final int numOfTransitions) 
    {
        if (src == null)
            throw new IllegalArgumentException("No source buffered image has been specified.");
        if (dst == null)
            throw new IllegalArgumentException("No destination buffered image has been specified.");
        if (numOfTransitions < 1)
            throw new IllegalArgumentException("No valid count has been specified. At least one tweened image has to be generated.");
        
        this.srcImage = src;
        this.dstImage = dst;
        this.totalNumOfTransitions = numOfTransitions;
        this.currentTransitionStep = 0;
    }
    
    @Override
    public int size() {
        return this.totalNumOfTransitions;
    }

    @Override
    public int remaining() {
        return this.totalNumOfTransitions - this.currentTransitionStep;
    }

    @Override
    public boolean hasRemaining() {
        return (this.totalNumOfTransitions - this.currentTransitionStep) > 0;
    }

    @Override
    public BufferedImage getSourceImage() {
        return this.srcImage;
    }

    @Override
    public BufferedImage getDestinationImage() {
        return this.dstImage;
    }
    
    @Override
    public BufferedImage nextTransition() 
    {
        if (!this.hasRemaining())
            return null;
        
        final float srcAlpha = (this.totalNumOfTransitions - this.currentTransitionStep)
                / ((float)this.totalNumOfTransitions + 1);

        final BufferedImage tweenedImage = new BufferedImage(
                    this.srcImage.getWidth(), 
                    this.srcImage.getHeight(), 
                    this.srcImage.getType());
        
        final Graphics2D g = tweenedImage.createGraphics();
        try {
            g.addRenderingHints(new RenderingHints(
                    RenderingHints.KEY_RENDERING, 
                    RenderingHints.VALUE_RENDER_QUALITY));

            g.setComposite(AlphaComposite.SrcOver.derive(srcAlpha));
            g.drawImage(this.srcImage, 0, 0, null);
            g.setComposite(AlphaComposite.SrcOver.derive(1.0f - srcAlpha));
            g.drawImage(this.dstImage, 0, 0, null);
            
            this.currentTransitionStep++;
            return tweenedImage;
        } 
        finally {
            g.dispose();
        }
    }
    
    @Override
    public void rewind() {
        this.currentTransitionStep = 0;
    }
}