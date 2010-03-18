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
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.net.InetAddress;
import terris.kilcli.KilCli;
import terris.kilcli.resource.HardenIndicator;

/**
 * HardenPanel for KilCli is the class used to create, update, and<br>
 * display the gold and battle stance information<br>
 * Ver: 1.0.1
 */

public class HardenPanel extends KilCliInternalFrame {
	private GridLayout grid;
	private static Object[] colors;
	private HardenIndicator[] indicators = new HardenIndicator[10];
	private String[] items = new String[10];

	public static void setColors(Object[] c) {
		colors = c;
	}

	public void updateTheme() {
		getContentPane().setBackground((java.awt.Color)(colors[0]));
		popup = null;
		createMenu();
		repaint();
	}

	/**
	 * HardenPanel constructor, with arguments for X,Y location and<br>
	 * height,width, creates a panel in the specified location<br>
	 * with the specified dimensions
	 * @param locX The starting X location
	 * @param locY The starting Y location
	 * @param width The width of the chats window
	 * @param height The height of the chats window
	 * @param OldStyle Flag to use old no title bar at all style for the commandline<br>
	 * 			Only works for Win/Lin/Sol
	 */

    public HardenPanel(int locX, int locY, int width, int height, boolean oldStyle) {
        super("Harden Armour Panel",
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable

		grid = new GridLayout(5, 2, 1, 1);
		for (int i = 0; i < indicators.length; i++) {
			indicators[i] = new HardenIndicator();
    		DragWindowAdapter dwa = new DragWindowAdapter(this);
	    	indicators[i].addMouseListener(dwa);
	    	indicators[i].addMouseMotionListener(dwa);
		}

		JPanel panel = new JPanel();
		//set panel's color
		panel.setBackground((java.awt.Color)(colors[0]));
		//set panel layout
		panel.setLayout(grid);
		//add labels to the panel
		for (int i = 0; i < indicators.length; i++) {
			panel.add(indicators[i]);
			items[i] = "";
		}

		getContentPane().add(panel);
		//set window size
		setSize(width, height);

		refocus(panel);

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
		panel.addMouseListener(dwa);
	    panel.addMouseMotionListener(dwa);
		created = true;

	}

	public void resetMagicCount() {
		for (int i = 0; i < 10; i++) {
			if (indicators[i].getLevel() != -1) {
				indicators[i].setName("empty");
				indicators[i].setLevel(-1);
				indicators[i].setVisible(false);
				indicators[i].setVisible(true);
			}
			items[i] = "";
		}
	}

	public void incrementMagicCount(String name, int s) {
		for (int i = 0; i < 10; i++) {
			if (items[i].equals("")) {
				indicators[i].setName(name.trim());
				indicators[i].setLevel(s);
				indicators[i].setVisible(false);
				indicators[i].setVisible(true);
				items[i] = name;
				break;
			}
		}
	}

	public void decrementMagicCount(String name) {
		for (int i = 0; i < 10; i++) {
			if (items[i].equals(name)) {
				items[i] = "";
				indicators[i].setName("empty");
				indicators[i].setLevel(-1);
				indicators[i].setVisible(false);
				indicators[i].setVisible(true);
				break;
			}
		}

	}

	public String[] getItems() {
		return items;
	}

}
