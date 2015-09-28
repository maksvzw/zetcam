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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * @author Lenny
 */

// TODO: Use ComponentMover implementation later. It's especially useful because
// it enables any component to be moved (such as a component representing a frame
// in a timeline).

public class TitleBar extends JPanel 
{
    private JPanel pnlTitle;
    private JLabel lblIcon;
    private JLabel lblTitle;
    
    private JPanel pnlButtons;
    private TextButton btnMinimize;
    private TextButton btnMaximize;
    private TextButton btnClose;
    
    private int pX, pY;
    
    public TitleBar() 
    {
        super();
        this.initComponents();
    }
    
    private void initComponents() 
    {
        // The main panel shouldn't be opaque given that we need to set
        // a background color for this component.
        super.setOpaque(true);
        super.setLayout(new BorderLayout());

        // The title panel should be transparent and we use a BoxLayout to 
        // position its components in a single row.
        this.pnlTitle = new JPanel();
        this.pnlTitle.setOpaque(false);
        this.pnlTitle.setLayout(new BoxLayout(this.pnlTitle, BoxLayout.LINE_AXIS));
        // This transparent border will function as the margin between the 
        // buttons panel and the main panel.
        this.pnlTitle.setBorder(new EmptyBorder(3, 0, 3, 5));
        
        // The Icon label will display the icon in the far left corner.
        this.lblIcon = new JLabel();
        this.lblIcon.setMinimumSize(new Dimension(16, 16));
        // The Title label will display the title text next to the icon.
        this.lblTitle = new JLabel();

        // Due to the nature of the BoxLayout, each new component will
        // be placed directly after the next. Therefore we need to add
        // invisible area's to provide proper padding.
        this.pnlTitle.add(Box.createRigidArea(new Dimension(5, 0)));
        this.pnlTitle.add(this.lblIcon);
        this.pnlTitle.add(Box.createRigidArea(new Dimension(5, 0)));
        this.pnlTitle.add(this.lblTitle);

        // The buttons panel should also be transparent and we will use a 
        // GridLayout, defined as a single row with three columns, in order
        // to position each button correctly.
        this.pnlButtons = new JPanel();
        this.pnlButtons.setOpaque(false);
        GridLayout gl = new GridLayout(1, 3);
        gl.setHgap(3);
        this.pnlButtons.setLayout(gl);
        // This transparent border will function as the margin between the 
        // buttons panel and the main panel.
        this.pnlButtons.setBorder(new EmptyBorder(3, 0, 5, 5));
        
        // Create custom buttons to allow users to minimize, maximize or close
        // the parent window.
        this.btnMinimize = new TextButton("_");
        this.btnMinimize.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                JFrame root = getRootFrame();
                root.setExtendedState(JFrame.ICONIFIED);
            }
        });
        
        this.btnMaximize = new TextButton("■");
        this.btnMaximize.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                JFrame root = getRootFrame();
                if ((root.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH)
                    root.setExtendedState(JFrame.NORMAL);
                else
                    root.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
        
        this.btnClose = new TextButton("x");
        this.btnClose.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                JFrame root = getRootFrame();
                root.dispatchEvent(new WindowEvent(root, WindowEvent.WINDOW_CLOSING));
            }
        });
        
        this.pnlButtons.add(this.btnMinimize);
        this.pnlButtons.add(this.btnMaximize);
        this.pnlButtons.add(this.btnClose);
        
        // The title panel will be displayed on the lefthand side of TitleBar,
        // while the buttons are displayed on the righthand side. 
        this.add(this.pnlTitle, BorderLayout.WEST);
        this.add(this.pnlButtons, BorderLayout.EAST);
        
        // The following event listeners will enable users to move the parent
        // window by dragging on the title bar.
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                pX = me.getX();
                pY = me.getY();
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() 
        {
            @Override
            public void mouseDragged(MouseEvent me) {
                JFrame root = getRootFrame();
                
                // Disallow dragging when the window is maximized.
                if ((root.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH)
                    return;

                root.setLocation(
                        root.getLocation().x + me.getX() - pX,
                        root.getLocation().y + me.getY() - pY
                );
            }
        });
    }
    
    private JFrame getRootFrame() {
        return (JFrame)SwingUtilities.getWindowAncestor(this);
    }
    
    public String getTitle() {
        return this.lblTitle.getText();
    }
    
    public void setTitle(String title) {
        this.lblTitle.setText(title);
    }
    
    public Font getTitleFont() {
        return this.lblTitle.getFont();
    }
    
    public void setTitleFont(Font font) {
        this.lblTitle.setFont(font);
    }
    
    public Color getTitleColor() {
        return this.lblTitle.getForeground();
    }
    
    public void setTitleColor(Color color) {
        this.lblTitle.setForeground(color);
    }
    
    public Icon getIcon() {
        return this.lblIcon.getIcon();
    }
    
    public void setIcon(Icon icon) {
        this.lblIcon.setIcon(icon);
    }
    
    public TextButton getMinimizeButton() {
        return this.btnMinimize;
    }
    
    public TextButton getMaximizeButton() {
        return this.btnMaximize;
    }
    
    public TextButton getCloseButton() {
        return this.btnClose;
    }
    
    @Override
    public void setOpaque(boolean opaque) { }
    
    @Override
    public void setLayout(LayoutManager layout) { }
}