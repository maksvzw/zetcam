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

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import javax.swing.JPanel;

/**
 *
 * @author Lenny Knockaert
 */
public class HorizontalMenuBar extends JPanel
{
    private TextButton makeButton;
    private TextButton viewButton;
    private TextButton aboutButton;
    
    public HorizontalMenuBar()
    {
        super();
        this.initComponents();
    }
    
    private void initComponents() 
    {
        // The main panel shouldn't be opaque given that we need to set
        // a background color for this component.
        super.setOpaque(true);
        super.setLayout(new FlowLayout(FlowLayout.LEADING));
        
        this.makeButton = new TextButton("Make");
        this.makeButton.setSelected(true);
        this.viewButton = new TextButton("View");
        this.aboutButton = new TextButton("About");
        this.add(this.makeButton);
        this.add(this.viewButton);
        this.add(this.aboutButton);
    }
    
    public TextButton getMakeButton() {
        return this.makeButton;
    }
    
    public TextButton getViewButton() {
        return this.viewButton;
    }
    
    public TextButton getAboutButton() {
        return this.aboutButton;
    }
    
    @Override
    public void setOpaque(boolean opaque) { }
    
    @Override
    public void setLayout(LayoutManager layout) { }
}