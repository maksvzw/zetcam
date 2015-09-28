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

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 * @author Lenny
 */
// http://docs.oracle.com/javase/tutorial/i18n/
// http://docs.oracle.com/javase/tutorial/i18n/resbundle/propfile.html
public class MainWindow extends BaseWindow
{
    //private final Camera camera;
    private final Webcam camera;
    private WebcamPanel surface;
    
    public MainWindow()
    {
        super("Web Camera");

        this.camera = Webcam.getDefault();
        this.camera.setViewSize(new Dimension(640, 480));
        this.initComponents();
    }
    
    private void initComponents() 
    {
        this.surface = new WebcamPanel(this.camera, false);
        this.surface.setDoubleBuffered(true);
        this.surface.setFillArea(true);
        this.add(this.surface, BorderLayout.CENTER);
        this.pack();
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                try {
                    
                } catch (Exception ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    surface.stop();
                } catch (Exception ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            @Override
            public void windowIconified(WindowEvent e)
            {
                try {
                    surface.pause();
                } catch (Exception ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            @Override
            public void windowDeiconified(WindowEvent e) {
                try {
                    surface.resume();
                } catch (Exception ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                surface.setPreferredSize(getSize());
                //surface.setSize(getSize());
            }
        });
    }
}