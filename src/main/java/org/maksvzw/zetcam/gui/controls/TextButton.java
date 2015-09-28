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
package org.maksvzw.zetcam.gui.controls;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JButton;

/**
 *
 * @author Lenny
 */
public final class TextButton extends JButton 
{
    private Color pressedBackgroundColor;
    private Color hoverBackgroundColor;

    public TextButton(String text) 
    {
        super(text);
        super.setContentAreaFilled(false);
        super.setFocusPainted(false);
        super.setBorderPainted(false);
        
        // Defaults
        this.setBackground(Color.GRAY);
        this.setForeground(Color.WHITE);
        this.setPressedBackground(Color.GRAY.darker());
        this.setHoverBackground(Color.GRAY.brighter());
    }
    
    public Color getPressedBackground() {
        return this.pressedBackgroundColor;
    }
    
    public void setPressedBackground(Color color) {
        this.pressedBackgroundColor = color;
    }
    
    public Color getHoverBackground() {
        return this.hoverBackgroundColor;
    }
    
    public void setHoverBackground(Color color) {
        this.hoverBackgroundColor = color;
    }
    
    @Override
    public void setContentAreaFilled(boolean b) { }
    
    @Override
    public void setFocusPainted(boolean b) { }
    
    @Override
    public void setBorderPainted(boolean b) { }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        if (this.getModel().isPressed()) {
            g.setColor(this.pressedBackgroundColor);
        } else if (this.getModel().isRollover()) {
            g.setColor(this.hoverBackgroundColor);
        } else {
            g.setColor(getBackground());
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}
