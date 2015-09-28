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

import java.awt.image.BufferedImage;
import javax.swing.JButton;

/**
 * @author Lenny
 */

// TODO: finish class, get some icons and test
// http://stackoverflow.com/questions/6735891/creating-custom-jbutton-from-images-containing-transparent-pixels

public final class ImageButton extends JButton 
{
    private BufferedImage icon;
    private BufferedImage pressedIcon;
    private BufferedImage hoverIcon;
    private BufferedImage disabledIcon;
    
    public ImageButton() 
    {
        super();
        super.setContentAreaFilled(false);
        super.setFocusPainted(false);
        super.setBorderPainted(false);
        
    }
    
    @Override
    public void setContentAreaFilled(boolean b) { }
    
    @Override
    public void setFocusPainted(boolean b) { }
    
    @Override
    public void setBorderPainted(boolean b) { }
}
