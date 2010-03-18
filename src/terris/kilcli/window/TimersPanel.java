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
 * TimersPanel for KilCli is the class used to create, update, and<br>
 * display the timers panel<br>
 * Ver: 1.0.1
 */

public class TimersPanel extends KilCliInternalFrame {
    private JLabel stun;
    private JLabel slow;
    private JLabel spam;
	private GridLayout grid;
	private static Object[] colors;
	private int stunTime = 0;
	private int slowTime = 0;
	private int spamTime = 0;



	public static void setColors(Object[] c) {
		colors = c;
	}

	public void updateTheme() {
		getContentPane().setBackground((java.awt.Color)(colors[0]));
		String temp = "<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;";
		if ((KilCli.getEffects().getEffectStatus(1) == 1)  && (stunTime > 0)) {
			temp += "<font color=#" + (String)(colors[18]) + ">St</font>/";
		} else {
			temp += "St/";
		}
		if ((KilCli.getEffects().getEffectStatus(0) == 1)  && (stunTime > 0)) {
			temp += "<font color=#" + (String)(colors[18]) + ">H</font>/";
		} else {
			temp += "H/";
		}
		if ((KilCli.getEffects().getEffectStatus(2) == 1)  && (stunTime > 0)) {
			temp += "<font color=#" + (String)(colors[18]) + ">E</font>:";
		} else {
			temp += "E:";
		}
		temp += " <font color=#" + (String)(colors[4]) + ">" + stunTime + " sec</font>";
		stun.setText(temp);
		temp = "<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;";
		if ((KilCli.getEffects().getEffectStatus(4) == 1) && (slowTime > 0)) {
			temp += "<font color=#" + (String)(colors[18]) + ">Sl</font>/";
		} else {
			temp += "Sl/";
		}
		if ((KilCli.getEffects().getEffectStatus(7) == 1) && (slowTime > 0)) {
			temp += "<font color=#" + (String)(colors[18]) + ">C</font>/";
		} else {
			temp += "C/";
		}
		if ((KilCli.getEffects().getEffectStatus(5) == 1) && (slowTime > 0)) {
			temp += "<font color=#" + (String)(colors[18]) + ">P</font>:";
		} else {
			temp += "P:";
		}
		temp += " <font color=#" + (String)(colors[4]) + ">" + slowTime + " sec</font>";
		slow.setText(temp);
		spam.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;Spam: <font color=#" + (String)(colors[4]) + ">" + spamTime + " sec</font>");
		popup = null;
		createMenu();
		repaint();
	}

	/**
	 * TimersPanel constructor, with arguments for X,Y location and<br>
	 * height,width, creates a panel in the specified location<br>
	 * with the specified dimensions
	 * @param locX The starting X location
	 * @param locY The starting Y location
	 * @param width The width of the chats window
	 * @param height The height of the chats window
	 * @param OldStyle Flag to use old no title bar at all style for the commandline<br>
	 * 			Only works for Win/Lin/Sol
	 */

    public TimersPanel(int locX, int locY, int width, int height, boolean oldStyle) {
        super("Timers Panel",
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable

		grid = new GridLayout(3, 1, 1, 1);
		//grid.setHgap(1);
        //get screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		//create labels
		stun = new JLabel();
		slow = new JLabel();
		spam = new JLabel();

		//set label text
		stun.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;St/H/E: <font color=#" + (String)(colors[4]) + ">" + stunTime + " sec</font>");
		slow.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;Sl/C/P: <font color=#" + (String)(colors[4]) + ">" + slowTime + " sec</font>");
		spam.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;Spam: <font color=#" + (String)(colors[4]) + ">" + spamTime + " sec</font>");
		//set panel's color
		getContentPane().setBackground((java.awt.Color)(colors[0]));
		//set panel layout
		getContentPane().setLayout(grid);
		//add labels to the panel
		getContentPane().add(stun);
		getContentPane().add(slow);
		getContentPane().add(spam);

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
	 * Updates the stun/held display
	 *
	 * @param timer Time count to be displayed
	 */

	public void UpdateStunTimer(int timer) {
		stunTime = timer;
		String temp = "<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;";
		if ((KilCli.getEffects().getEffectStatus(1) == 1) && (stunTime > 0)) {
			temp += "<font color=#" + (String)(colors[18]) + ">St</font>/";
		} else {
			temp += "St/";
		}
		if ((KilCli.getEffects().getEffectStatus(0) == 1) && (stunTime > 0)){
			temp += "<font color=#" + (String)(colors[18]) + ">H</font>/";
		} else {
			temp += "H/";
		}
		if ((KilCli.getEffects().getEffectStatus(2) == 1) && (stunTime > 0)) {
			temp += "<font color=#" + (String)(colors[18]) + ">E</font>:";
		} else {
			temp += "E:";
		}
		temp += " <font color=#" + (String)(colors[4]) + ">" + timer + " sec</font>";
		stun.setText(temp);

	}

	/**
	 * Updates the slow/confused display
	 *
	 * @param timer Time count to be displayed
	 */

	public void UpdateSlowTimer(int timer) {
		slowTime = timer;
		String temp = "<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;";
		if ((KilCli.getEffects().getEffectStatus(4) == 1) && (slowTime > 0)) {
			temp += "<font color=#" + (String)(colors[18]) + ">Sl</font>/";
		} else {
			temp += "Sl/";
		}
		if ((KilCli.getEffects().getEffectStatus(7) == 1) && (slowTime > 0)) {
			temp += "<font color=#" + (String)(colors[18]) + ">C</font>/";
		} else {
			temp += "C/";
		}
		if ((KilCli.getEffects().getEffectStatus(5) == 1) && (slowTime > 0)) {
			temp += "<font color=#" + (String)(colors[18]) + ">P</font>:";
		} else {
			temp += "P:";
		}
		temp += " <font color=#" + (String)(colors[4]) + ">" + timer + " sec</font>";
		slow.setText(temp);
	}

	/**
	 * Updates the spam timer display
	 *
	 * @param timer Time count to be displayed
	 */

	public void UpdateSpamTimer(int timer) {
		spam.setText("<html><body bgcolor=#" + (String)(colors[1]) + "><font color=#" + (String)(colors[2]) + ">&nbsp;Spam: <font color=#" + (String)(colors[4]) + ">" + timer + " sec");
		spamTime = timer;
	}
}
