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
package org.maksvzw.zetcam.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.maksvzw.zetcam.gui.controls.TitleBar;
import org.maksvzw.zetcam.gui.components.ComponentResizer;
import org.maksvzw.zetcam.gui.controls.HorizontalMenuBar;

/**
 *
 * @author Lenny
 */
public class BaseWindow extends JFrame
{
    private JPanel topPanel;
    private TitleBar titleBar;
    private HorizontalMenuBar menuBar;
    private LineBorder windowBorder;
    private ComponentResizer resizer;
    
    protected BaseWindow()
    {
        super();
        this.initComponents();
    }
    
    protected BaseWindow(String title)
    {
        super(title);
        this.initComponents();
    }
    
    private void initComponents() 
    {
        final Font defaultFont = new Font("Arial", Font.BOLD, 14);
        
        // The following will remove the Windows/OSX title bar and borders, 
        // which causes only the root pane of the window to be drawn.
        this.setUndecorated(true);
        
        // Configure and add custom title bar.
        this.titleBar = new TitleBar();
        this.titleBar.setBackground(Color.DARK_GRAY);
        this.titleBar.setTitle(this.getTitle());
        this.titleBar.setTitleFont(defaultFont);
        this.titleBar.setTitleColor(Color.WHITE);
        
        this.titleBar.getMinimizeButton().setFont(defaultFont);
        this.titleBar.getCloseButton().setFont(defaultFont);
        this.titleBar.getCloseButton().setBackground(Color.RED.darker());
        this.titleBar.getCloseButton().setPressedBackground(Color.RED.darker().darker());
        this.titleBar.getCloseButton().setHoverBackground(Color.RED);
        
        this.menuBar = new HorizontalMenuBar();
        this.menuBar.setBackground(Color.GRAY.darker());
        this.menuBar.getMakeButton().setBackground(Color.GRAY);
        this.menuBar.getMakeButton().setHoverBackground(Color.GRAY.brighter());
        this.menuBar.getMakeButton().setPressedBackground(Color.GRAY);
        this.menuBar.getViewButton().setBackground(Color.GRAY);
        this.menuBar.getViewButton().setHoverBackground(Color.GRAY.brighter());
        this.menuBar.getViewButton().setPressedBackground(Color.GRAY);
        this.menuBar.getAboutButton().setBackground(Color.GRAY);
        this.menuBar.getAboutButton().setHoverBackground(Color.GRAY.brighter());
        this.menuBar.getAboutButton().setPressedBackground(Color.GRAY);
        
        this.topPanel = new JPanel();
        this.topPanel.setLayout(new BoxLayout(this.topPanel, BoxLayout.Y_AXIS));
        this.topPanel.add(this.titleBar);
        this.topPanel.add(this.menuBar);
        this.add(this.topPanel, BorderLayout.NORTH);
        //this.add(this.titleBar, BorderLayout.NORTH);
        
        // Defines and adds a new border to the root pane of the window.
        this.windowBorder = new LineBorder(Color.DARK_GRAY, 2);
        this.getRootPane().setBorder(windowBorder);
        
        // Configures resizer and registers itself to allow users to resize 
        // the window as they normally would.
        this.resizer = new ComponentResizer();
        this.resizer.setMinimumSize(this.getMinimumSize());
        this.resizer.setMaximumSize(this.getMaximumSize());
        this.resizer.setSnapSize(new Dimension(5,5));
        this.resizer.registerComponent(this);

        // Ensures that the window is always drawn with rounded corners.
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 5, 5));
            }
        });
    }
    
    @Override
    public synchronized void setExtendedState(int state)
    {
        // When a JFrame is undecorated and subsequently maximized, it will fill
        // the entire screen and be drawn over taskbar (wherever it may be
        // positioned). This is a well-known bug and still has not been fixed!
        if (this.isUndecorated() &&
            (state & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH)
        {
            Rectangle bounds;
            GraphicsConfiguration gc = null;
            
            // Finds the GraphicsDevice of the screen where the JFrame is located.
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Point p = this.getLocationOnScreen();

            for (GraphicsDevice gd : env.getScreenDevices()) {
                if (gd.getDefaultConfiguration().getBounds().contains(p)) {
                    gc = gd.getDefaultConfiguration();
                    break;
                }
            }
            
            // Determines the maximum usuable bounds.
            if (gc != null) {
                bounds = gc.getBounds();
                Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);

                bounds.x += screenInsets.left;
                bounds.y += screenInsets.top;
                bounds.height -= screenInsets.bottom;
                bounds.width -= screenInsets.right;
            }
            else {
                bounds = env.getMaximumWindowBounds();
            }
            
            // We will set the correct maximized bounds first and then let the 
            // state of the JFrame transition.
            super.setMaximizedBounds(bounds);
        }
        super.setExtendedState(state);
    }
}