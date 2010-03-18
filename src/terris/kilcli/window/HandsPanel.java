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
 * HandsPanel for KilCli is the class used to create, update, and<br>
 * display the hands panel<br>
 * Ver: 1.0.1
 */

public class HandsPanel extends KilCliInternalFrame {
	public JLabel Left;
	public JLabel Right;
	public GridLayout grid;
	private static Object[] colors;
	private String leftString = "";
	private String rightString = "";


	public static void setColors(Object[] c) {
		colors = c;
	}

	public void updateTheme() {
		getContentPane().setBackground((java.awt.Color)(colors[0]));
		Right.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;Right Hand: <font color=#" + (String)(colors[3]) + ">" + rightString);
		Left.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;Left Hand: <font color=#" + (String)(colors[3]) + ">" + leftString);
		popup = null;
		createMenu();
		repaint();
	}

	/**
	 * HandsPanel constructor, with arguments for X,Y location and<br>
	 * height,width, creates a panel in the specified location<br>
	 * with the specified dimensions
	 * @param locX The starting X location
	 * @param locY The starting Y location
	 * @param width The width of the hands panel
	 * @param height The height of the hands panel
	 * @param OldStyle Flag to use old no title bar at all style for the commandline<br>
	 * 			Only works for Win/Lin/Sol
	 */

    public HandsPanel(int locX, int locY, int width, int height, boolean oldStyle) {
        super("Hands Panel",
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable

		grid = new GridLayout(2, 1, 1, 1);
		//grid.setHgap(1);
        //get screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		//create labels
		Left = new JLabel();
		Right = new JLabel();

		//set label text
		Right.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;Right Hand: ");
		Left.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;Left Hand: ");

		//set panel's color
		getContentPane().setBackground((java.awt.Color)(colors[0]));
		//set panel layout
		getContentPane().setLayout(grid);
		//add labels to the panel
		getContentPane().add(Right);
		getContentPane().add(Left);

		//set window size
		setSize(width, height);

        //Set the window's location.
		setLocation(locX, locY);

		refocus();

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
	 * Updates right hand display
	 *
	 * @param right Right hand contents
	 */

	public void UpdateRight(String right) {
		Right.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;Right Hand: <font color=#" + (String)(colors[3]) + ">" + right);
		rightString = right;
	}

	/**
	 * Updates left hand display
	 *
	 * @param left Left hand contents
	 */

	public void UpdateLeft(String left) {
		Left.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;Left Hand: <font color=#" + (String)(colors[3]) + ">" + left);
		leftString = left;
	}

	/**
	 * Returns what is in the right hand
	 *
	 * @return rightString - contents of the right hand
	 */

	public String getRight() {
		return rightString;
	}

	/**
	 * Returns what is in the left hand
	 *
	 * @return leftString - contents of the left hand
	 */

	public String getLeft() {
		return leftString;
	}
}
