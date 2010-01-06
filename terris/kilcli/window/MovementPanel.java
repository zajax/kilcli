/* KilCli, an OGC mud client program
 * Copyright (C) 2002 - 2004 Jason Baumeister
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the project nor the names of its contributors
 *  may be used to endorse or promote products derived from this software
 *  without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE PROJECT AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE PROJECT OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 */

package terris.kilcli.window;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import javax.swing.plaf.basic.*;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import terris.kilcli.KilCli;

/**
 * MovementPanel for KilCli is the class used to display/update<br>
 * the movement panel window<br>
 * Ver: 1.0.0
 */

public class MovementPanel extends KilCliInternalFrame {
    private JLabel NW;
    private JLabel N;
    private JLabel NE;
    private JLabel U;
    private JLabel W;
    private JLabel blank;
	private JLabel E;
	private JLabel blank2;
	private JLabel SW;
	private JLabel S;
	private JLabel SE;
	private JLabel D;
	private GridLayout grid;
	private char previewChar;
	private static Object[] colors;
	private String dirString = "111111111111";



	public static void setColors(Object[] c) {
		colors = c;
	}

	public void updateTheme() {
		getContentPane().setBackground((java.awt.Color)(colors[0]));
		UpdateDirs(dirString);
		popup = null;
		createMenu();
		repaint();
	}

	/**
	 * MovementPanel constructor, with arguments for X,Y location and<br>
	 * height,width, creates a panel in the specified location<br>
	 * with the specified dimensions
	 * @param locX The starting X location
	 * @param locY The starting Y location
	 * @param width The width of the chats window
	 * @param height The height of the chats window
	 * @param OldStyle Flag to use old no title bar at all style for the commandline<br>
	 * 			Only works for Win/Lin/Sol
	 */

    public MovementPanel(int locX, int locY, int width, int height, boolean oldStyle) {
        super("Movement Panel",
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable

		grid = new GridLayout(3, 4, 1, 0);

        //get screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		//create labels
		NW = new JLabel();
		N = new JLabel();
		NE = new JLabel();
		U = new JLabel();
		W = new JLabel();
		blank = new JLabel("<html><body bgcolor=#" + (String)(colors[1]) + ">");
		blank2 = new JLabel("<html><body bgcolor=#" + (String)(colors[1]) + ">");
		E = new JLabel();
		SW = new JLabel();
		S = new JLabel();
		SE = new JLabel();
		D = new JLabel();

		//set label text
		N.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;&nbsp;N");
		NE.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">NE");
		E.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;&nbsp;E");
		SE.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">SE");
		S.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;&nbsp;S");
		SW.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;SW");
		W.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;&nbsp;W");
		NW.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;NW");
		U.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">U");
		D.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">D");

		//set panel's color
		getContentPane().setBackground((java.awt.Color)(colors[0]));
		//set panel layout
		getContentPane().setLayout(grid);
		//add labels to the panel
		getContentPane().add(NW);
		getContentPane().add(N);
		getContentPane().add(NE);
		getContentPane().add(U);
		getContentPane().add(W);
		getContentPane().add(blank);
		getContentPane().add(E);
		getContentPane().add(blank2);
		getContentPane().add(SW);
		getContentPane().add(S);
		getContentPane().add(SE);
		getContentPane().add(D);

		//set window size
		setSize(width, height);

		refocus();
        //Set the window's location.
		setLocation(locX, locY);

		//check oldStyle flag to remove title bar completely ***Only for Win/Lin/Sol
		if (oldStyle) {
			//remove the title bar
			ComponentUI frameUI = getUI();
			if (frameUI instanceof BasicInternalFrameUI) {
				((BasicInternalFrameUI) frameUI).setNorthPane(null);
			}
		}

		//creates the right click menu
		createMenu();
    	dwa = new DragWindowAdapter(this);
	    this.addMouseListener(dwa);
	    this.addMouseMotionListener(dwa);
		created = true;
	}

	/**
	 * Updates the visible directions
	 *
	 * @param dirs String of 1s and 0s representing directions
	 */
	public void UpdateDirs(String dirs) {
		dirString = dirs;
		for (int i=2; i < dirs.length(); i++) {
			previewChar = dirs.charAt(i);
			if (i < 7) {
				if (i < 4) {
					if (i==2) {
						if (previewChar == '1') {
							N.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;&nbsp;N");
						} else {
							N.setText("<html><body bgcolor=#" + (String)(colors[1]) + ">");
						}
					} else {
						if (previewChar == '1') {
							NE.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">NE");
						} else {
							NE.setText("<html><body bgcolor=#" + (String)(colors[1]) + ">");
						}
					}
				} else {
					if (i==4) {
						if (previewChar == '1') {
							E.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;E");
						} else {
							E.setText("<html><body bgcolor=#" + (String)(colors[1]) + ">");
						}
					} else if (i==5) {
						if (previewChar == '1') {
							SE.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">SE");
						} else {
							SE.setText("<html><body bgcolor=#" + (String)(colors[1]) + ">");
						}
					} else {
						if (previewChar == '1') {
							S.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;&nbsp;S");
						} else {
							S.setText("<html><body bgcolor=#" + (String)(colors[1]) + ">");
						}
					}
				}
			} else {
				if (i < 10) {
					if (i==7) {
						if (previewChar == '1') {
							SW.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;SW");
						} else {
							SW.setText("<html><body bgcolor=#" + (String)(colors[1]) + ">");
						}
					} else if (i==8) {
						if (previewChar == '1') {
							W.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;&nbsp;W");
						} else {
							W.setText("<html><body bgcolor=#" + (String)(colors[1]) + ">");
						}
					} else {
						if (previewChar == '1') {
							NW.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;NW");
						} else {
							NW.setText("<html><body bgcolor=#" + (String)(colors[1]) + ">");
						}
					}
				} else {
					if (i==10) {
						if (previewChar == '1') {
							U.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">U");
						} else {
							U.setText("<html><body bgcolor=#" + (String)(colors[1]) + ">");
						}
					} else if (i==11) {
						if (previewChar == '1') {
							D.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">D");
						} else {
							D.setText("<html><body bgcolor=#" + (String)(colors[1]) + ">");
						}
					}
				}
			}
		}
	}
}
