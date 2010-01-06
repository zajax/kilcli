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

/**
 * InfoPanel for KilCli is the class used to create, update, and<br>
 * display the gold and battle stance information<br>
 * Ver: 1.0.0
 */

public class InfoPanel extends KilCliInternalFrame {
    private JLabel gold;
    private JLabel stance;
    private static JLabel ping;
	private GridLayout grid;
	private static Object[] colors;
	private int goldTotal = 0;
	private String stanceString = "Normal";
	private static long pingTime = 0;


	public static void setColors(Object[] c) {
		colors = c;
	}

	public void updateTheme() {
		getContentPane().setBackground((java.awt.Color)(colors[0]));
		gold.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;Gold: <font color=#" + (String)(colors[4]) + ">" + goldTotal + "</font>");
		stance.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;Stance: <font color=#" + (String)(colors[4]) + ">" + stanceString + "</font>");
		ping.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;Ping: <font color=#" + (String)(colors[4]) + ">" + pingTime + "ms</font>");
		popup = null;
		createMenu();
		repaint();
	}

	/**
	 * InfoPanel constructor, with arguments for X,Y location and<br>
	 * height,width, creates a panel in the specified location<br>
	 * with the specified dimensions
	 * @param locX The starting X location
	 * @param locY The starting Y location
	 * @param width The width of the chats window
	 * @param height The height of the chats window
	 * @param OldStyle Flag to use old no title bar at all style for the commandline<br>
	 * 			Only works for Win/Lin/Sol
	 */

    public InfoPanel(int locX, int locY, int width, int height, boolean oldStyle) {
        super("Info Panel",
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable

		grid = new GridLayout(3, 1, 1, 1);
		//grid.setHgap(1);
        //get screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		//create labels
		gold = new JLabel();
		stance = new JLabel();
		ping = new JLabel();
		ping.setToolTipText("Approximate roundtrip time for last successful command");

		//set label text
		gold.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;Gold: <font color=#" + (String)(colors[4]) + ">" + goldTotal + "</font>");
		stance.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;Stance: <font color=#" + (String)(colors[4]) + ">" + stanceString + "</font>");
		ping.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;Ping: <font color=#" + (String)(colors[4]) + ">" + pingTime + "ms</font>");
		//set panel's color
		getContentPane().setBackground((java.awt.Color)(colors[0]));
		//set panel layout
		getContentPane().setLayout(grid);
		//add labels to the panel
		getContentPane().add(gold);
		getContentPane().add(stance);
		getContentPane().add(ping);

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
	 * Updates the gold display
	 *
	 * @param g - new gold total
	 */

	public void updateGold(int g) {
		gold.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;Gold: <font color=#" + (String)(colors[4]) + ">" + g + "</font>");
		goldTotal = g;
	}

	/**
	 * Updates the stance display
	 *
	 * @param battleStance - the new battle stance
	 */

	public void updateStance(String battleStance) {
		battleStance = battleStance.substring(0,battleStance.indexOf("^"));
		stance.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;Stance: <font color=#" + (String)(colors[4]) + ">" + battleStance + "</font>");
		stanceString = battleStance;
	}

	/**
	 * Updates the ping display
	 *
	 * @param battleStance - the new battle stance
	 */

	public static void updatePing(long newPing) {
        ping.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;Ping: <font color=#" + (String)(colors[4]) + ">" + newPing + "ms</font>");
        pingTime = newPing;
	}


}
