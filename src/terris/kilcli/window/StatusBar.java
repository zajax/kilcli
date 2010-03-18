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
import javax.swing.JProgressBar;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import terris.kilcli.KilCli;
import terris.kilcli.resource.KilCliProgressBar;

/**
 * StatusBar for KilCli is the class used to create, update, and<br>
 * display the status bar<br>
 * Ver: 1.0.0
 */

public class StatusBar extends KilCliInternalFrame {
    private KilCliProgressBar HPBar;
    private KilCliProgressBar SPBar;
    private KilCliProgressBar ExpBar;
    private KilCliProgressBar Unbal;
    private JProgressBar SkinHPBar;
    private JProgressBar SkinSPBar;
    private JProgressBar SkinExpBar;
    private JProgressBar SkinUnbal;
	private GridLayout grid;
	private static Object[] colors;
	private static boolean skin = false;

	public static void setColors(Object[] c) {
		colors = c;
	}

	public void updateTheme() {

		getContentPane().setBackground((Color)(colors[0]));

		if (!(KilCli.getCurrentLookAndFeel().equals("com.l2fprod.gui.plaf.skin.SkinLookAndFeel"))) {
			if (skin) {
				skin = false;
				getContentPane().remove(SkinHPBar);
				getContentPane().remove(SkinSPBar);
				getContentPane().remove(SkinExpBar);
				getContentPane().remove(SkinUnbal);

    			getContentPane().add(HPBar);
    			getContentPane().add(SPBar);
    			getContentPane().add(ExpBar);
    			getContentPane().add(Unbal);
			}
			HPBar.setCustomFull((Color)(colors[5]));
			HPBar.setCustomEmpty((Color)(colors[6]));
			HPBar.setOtherForeground((Color)(colors[7]));
			HPBar.setOtherBackground((Color)(colors[8]));

			SPBar.setCustomFull((Color)(colors[5]));
			SPBar.setCustomEmpty((Color)(colors[6]));
			SPBar.setOtherForeground((Color)(colors[7]));
			SPBar.setOtherBackground((Color)(colors[8]));


			ExpBar.setCustomFull((Color)(colors[9]));
			ExpBar.setCustomEmpty((Color)(colors[10]));
			ExpBar.setOtherForeground((Color)(colors[11]));
			ExpBar.setOtherBackground((Color)(colors[12]));

			Unbal.setCustomFull((Color)(colors[13]));
			Unbal.setCustomEmpty((Color)(colors[14]));
			Unbal.setOtherForeground((Color)(colors[15]));
			Unbal.setOtherBackground((Color)(colors[16]));
		} else {
			if (!skin) {
				skin = true;
				getContentPane().remove(HPBar);
				getContentPane().remove(SPBar);
				getContentPane().remove(ExpBar);
				getContentPane().remove(Unbal);

				SkinHPBar = new JProgressBar(HPBar.getValue(), HPBar.getMaximum());
				SkinHPBar.setBorderPainted(true);
				SkinHPBar.setStringPainted(true);
				SkinHPBar.setSize(50, 10);
				SkinHPBar.setString("HPs: " + SkinHPBar.getValue() + "/" + SkinHPBar.getMaximum());
				SkinSPBar = new JProgressBar(SPBar.getValue(), SPBar.getMaximum());
				SkinSPBar.setBorderPainted(true);
				SkinSPBar.setStringPainted(true);
				SkinSPBar.setSize(50, 10);
				SkinSPBar.setString("SPs: " + SkinSPBar.getValue() + "/" + SkinSPBar.getMaximum());
				SkinExpBar = new JProgressBar(ExpBar.getValue(),100);
				SkinExpBar.setBorderPainted(true);
				SkinExpBar.setStringPainted(true);
				SkinExpBar.setSize(50, 10);
				SkinExpBar.setString("Exp: " + SkinExpBar.getValue() + "%");
				SkinUnbal = new JProgressBar(Unbal.getValue(),Unbal.getMaximum());
				SkinUnbal.setBorderPainted(true);
				SkinUnbal.setStringPainted(true);
				SkinUnbal.setSize(40, 2);
				SkinUnbal.setString(SkinUnbal.getValue() + "s");

    			getContentPane().add(SkinHPBar);
    			getContentPane().add(SkinSPBar);
    			getContentPane().add(SkinExpBar);
    			getContentPane().add(SkinUnbal);
			}
		}

		popup = null;
		createMenu();
		repaint();
	}

	/**
	 * StatusBar constructor, with arguments for X,Y location and<br>
	 * height,width, creates a panel in the specified location<br>
	 * with the specified dimensions
	 * @param locX The starting X location
	 * @param locY The starting Y location
	 * @param width The width of the chats window
	 * @param height The height of the chats window
	 * @param OldStyle Flag to use old no title bar at all style for the commandline<br>
	 * 			Only works for Win/Lin/Sol
	 */

    public StatusBar(int locX, int locY, int width, int height, boolean oldStyle) {
        super("Status Bar",
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable

		grid = new GridLayout(2, 2, 1, 1);
		getContentPane().setLayout(grid);
		setBackground((Color)(colors[0]));
        //get screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		//create labels
		HPBar = new KilCliProgressBar(0,1);
		HPBar.setType(3);
		HPBar.setTypeColor(4);
		HPBar.setCustomFull((Color)(colors[5]));
		HPBar.setCustomEmpty((Color)(colors[6]));
		HPBar.setOtherForeground((Color)(colors[7]));
		HPBar.setOtherBackground((Color)(colors[8]));
		HPBar.setSize(40, 2);
		HPBar.setString("HPs: 0/0");
		SkinHPBar = new JProgressBar(0,1);
		SkinHPBar.setBorderPainted(true);
		SkinHPBar.setStringPainted(true);
		SkinHPBar.setSize(40, 2);
		SkinHPBar.setString("HPs: 0/0");

		SPBar = new KilCliProgressBar(0,1);
		SPBar.setSize(40, 2);
		SPBar.setString("SPs: 0/0");
		SPBar.setType(3);
		SPBar.setTypeColor(4);
		SPBar.setCustomFull((Color)(colors[5]));
		SPBar.setCustomEmpty((Color)(colors[6]));
		SPBar.setOtherForeground((Color)(colors[7]));
		SPBar.setOtherBackground((Color)(colors[8]));
		SkinSPBar = new JProgressBar(0,1);
		SkinSPBar.setBorderPainted(true);
		SkinSPBar.setStringPainted(true);
		SkinSPBar.setSize(40, 2);
		SkinSPBar.setString("SPs: 0/0");

		ExpBar = new KilCliProgressBar(0, 100);
		ExpBar.setSize(40, 2);
		ExpBar.setType(3);
		ExpBar.setTypeColor(4);
		ExpBar.setCustomFull((Color)(colors[9]));
		ExpBar.setCustomEmpty((Color)(colors[10]));
		ExpBar.setOtherForeground((Color)(colors[11]));
		ExpBar.setOtherBackground((Color)(colors[12]));
		ExpBar.setString("Exp:");
		SkinExpBar = new JProgressBar(0,100);
		SkinExpBar.setBorderPainted(true);
		SkinExpBar.setStringPainted(true);
		SkinExpBar.setSize(40, 2);
		SkinExpBar.setString("Exp:");

		Unbal = new KilCliProgressBar(0, 0);
		Unbal.setSize(40, 2);
		Unbal.setType(3);
		Unbal.setString("0s");
		Unbal.setTypeColor(4);
		Unbal.setCustomFull((Color)(colors[13]));
		Unbal.setCustomEmpty((Color)(colors[14]));
		Unbal.setOtherForeground((Color)(colors[15]));
		Unbal.setOtherBackground((Color)(colors[16]));
		SkinUnbal = new JProgressBar(0,0);
		SkinUnbal.setBorderPainted(true);
		SkinUnbal.setStringPainted(true);
		SkinUnbal.setSize(40, 2);
		SkinUnbal.setString("0s");

		//set panel's color
		setBackground((Color)(colors[0]));

		//add labels to the panel
		if (!(KilCli.getCurrentLookAndFeel().equals("com.l2fprod.gui.plaf.skin.SkinLookAndFeel"))) {
    		getContentPane().add(HPBar);
    		getContentPane().add(SPBar);
    		getContentPane().add(ExpBar);
    		getContentPane().add(Unbal);
		} else {
			skin = true;
    		getContentPane().add(SkinHPBar);
    		getContentPane().add(SkinSPBar);
    		getContentPane().add(SkinExpBar);
    		getContentPane().add(SkinUnbal);
		}

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
	 * Updates the exp information in the bar
	 *
	 * @param exp Experience total to be displayed
	 */

	public void UpdateBar(String exp) {
		ExpBar.setValue((int)(Double.parseDouble(exp)));
		ExpBar.setString("Exp: " + exp + "%");
		SkinExpBar.setValue((int)(Double.parseDouble(exp)));
		SkinExpBar.setString("Exp: " + exp + "%");
	}

	/**
	 * Updates the unbalanced information in the bar
	 *
	 * @param x - the unbalanced time
	 */

	public void UpdateUnbal(int x) {
		if (((Unbal.getValue() == 0) && (x != 0)) || (x > Unbal.getMaximum())) {
			Unbal.setMaximum(x);
			SkinUnbal.setMaximum(x);
		}
		Unbal.setValue(x);
		Unbal.setString(x + "s");
		SkinUnbal.setValue(x);
		SkinUnbal.setString(x + "s");
	}

	/**
	 * Updates HP and SP display information
	 *
	 * @param hps Number of hps
	 * @param maxhps Max number of hps
	 * @param sps Number of sps
	 * @param maxsps Max number of sps
	 */

	public void UpdateStats(int hps, int maxhps, int sps, int maxsps) {
		HPBarUpdate(hps, maxhps);
		SPBarUpdate(sps, maxsps);
	}

	/**
	 * Updates the HPBar with the necessary colors and percentages
	 *
	 * @param hps Number of current hps
	 * @param maxhps Number of maximum hps, thus max size of bar
	 */

	private void HPBarUpdate(int hps, int maxhps) {
		HPBar.setMaximum(maxhps);
		HPBar.setValue(hps);
		HPBar.setString("HPs: " + hps + "/" + maxhps);
		SkinHPBar.setMaximum(maxhps);
		SkinHPBar.setValue(hps);
		SkinHPBar.setString("HPs: " + hps + "/" + maxhps);

	}

	/**
	 * Updates the SPBar with the necessary colors and percentages
	 *
	 * @param sps Number of current sps
	 * @param maxsps Number of maximum sps, thus max size of bar
	 */

	private void SPBarUpdate(int sps, int maxsps) {
		SPBar.setMaximum(maxsps);
		SPBar.setValue(sps);
		SPBar.setString("SPs: " + sps + "/" + maxsps);
		SkinSPBar.setMaximum(maxsps);
		SkinSPBar.setValue(sps);
		SkinSPBar.setString("SPs: " + sps + "/" + maxsps);
	}

}
