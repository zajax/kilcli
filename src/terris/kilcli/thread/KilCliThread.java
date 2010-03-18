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

package terris.kilcli.thread;

import terris.kilcli.*;
import terris.kilcli.io.*;
import terris.kilcli.gui.*;
import terris.kilcli.resource.*;
import java.util.StringTokenizer;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.BoxLayout;
import javax.swing.UIManager;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.SwingUtilities;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Dialog;
import com.l2fprod.gui.plaf.skin.Skin;
import com.l2fprod.gui.plaf.skin.CompoundSkin;
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;
import com.jgoodies.plaf.plastic.Plastic3DLookAndFeel;
import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import com.jgoodies.plaf.plastic.PlasticTheme;
import com.jgoodies.plaf.plastic.PlasticXPLookAndFeel;
import com.jgoodies.clearlook.ClearLookManager;
import com.jgoodies.clearlook.ClearLookMode;
import terris.kilcli.theme.*;


/**
 * KilCliThread for KilCli is the class used to create the<br>
 * thread the main program runs in<br>
 * Ver: 0.3.8
 */

public class KilCliThread extends Thread {
	private static KilCli mainFrame;
    private static int tmp = -11;
    private static JFrame menu;
    private static StringTokenizer tokenizer;
    private static String laf = "javax.swing.plaf.metal.MetalLookAndFeel";
    private static final String mac = "com.sun.java.swing.plaf.mac.MacLookAndFeel";
    private static final String windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    private static final String gtk = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
    private static String themeString = "Default";
    private static PlasticTheme theme = new PlasticTheme();
    private static int game = 0;

    public KilCliThread(String str) {
		super(str);
    }

	public KilCliThread(String str, int t) {
		super(str);
		tmp = t;
	}
    /**
     * Run method of KilCli thread, creates a KilCli object and executes it
     */

    public void run() {
		if (tmp != -11) {
			play();
		} else {
			//ClearLookManager.setMode(ClearLookMode.valueOf("On"));
       		//ClearLookManager.setPolicy("com.jgoodies.clearlook.DefaultClearLookPolicy");
			String slash = System.getProperty("file.separator");
			File srcFile = new File("config" + slash + "display.txt");
			if (!srcFile.exists()) {
				//display some kind of message that the file doesn't exist
			} else if (!srcFile.isFile() || !srcFile.canRead()) {
				//display error that it can't read from the file
			} else {
				try {
					BufferedReader inFile = new BufferedReader(new FileReader(srcFile));
					try {
						tokenizer = new StringTokenizer(inFile.readLine(), "|");
						inFile.close();
					} catch (IOException ioe) {
						System.err.println(ioe);
						ioe.printStackTrace();
					}

					if (tokenizer.hasMoreTokens()) {
						laf = tokenizer.nextToken();
					}
					if (tokenizer.hasMoreTokens()) {
						themeString = tokenizer.nextToken();
					}
					if (tokenizer.hasMoreTokens()) {
						game = Integer.parseInt(tokenizer.nextToken());
					}
					tokenizer = null;
					try {
						if (laf.equals("com.l2fprod.gui.plaf.skin.SkinLookAndFeel")) {
							SkinLookAndFeel.setSkin(SkinLookAndFeel.loadThemePack(themeString));
						} else if (laf.startsWith("com.jgoodies")) {
							File file = new File(themeString);
							InputStream in = new FileInputStream(file);
							theme = new CustomTheme(in);
							PlasticLookAndFeel.setMyCurrentTheme(theme);
						} else if (laf.equalsIgnoreCase(mac)) {
							UIManager.setLookAndFeel(laf);
    	    				SwingUtilities.updateComponentTreeUI(menu);
						} else if (laf.equalsIgnoreCase(gtk)) {
							UIManager.setLookAndFeel(laf);
    	    				SwingUtilities.updateComponentTreeUI(menu);
						} else if (laf.equalsIgnoreCase(windows)) {
							UIManager.setLookAndFeel(laf);
    	    				SwingUtilities.updateComponentTreeUI(menu);
						} else {
							if (themeString.toLowerCase().equals("default")) {
								theme = new PlasticTheme();
							} else if (themeString.toLowerCase().equals("contrast")) {
								theme = new ContrastTheme();
							} else {
								File file = new File(themeString);
								InputStream in = new FileInputStream(file);
								theme = new CustomTheme(in);
								in.close();
							}

							MetalLookAndFeel.setCurrentTheme(theme);
						}
						UIManager.setLookAndFeel(laf);
						//SwingUtilities.updatecomponentTreeUI(this);
					} catch (Exception e) {System.out.println("error changing l&f");
						e.printStackTrace();
						//SMTPClient.sendError("displayTXT", e);
					}
				} catch (FileNotFoundException fnfe) {}
			}

			menu = new JFrame("KilCli Menu");
			menu.setResizable(false);
			Image icon = Toolkit.getDefaultToolkit().getImage("kilcli.jpg");
			menu.setIconImage(icon);
			JPanel box = new JPanel();
        	JLabel label = new JLabel("<html>&nbsp;&nbsp;What do you wish to do?<br></html>");
			box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        	box.add(label);

        	final int numButtons = 5;
        	JRadioButton[] radioButtons = new JRadioButton[numButtons];
        	final ButtonGroup group = new ButtonGroup();

        	JButton playButton = null;

        	final String terrisCommand = "terris";
        	final String cosrinCommand = "cosrin";
        	final String wolfCommand = "wolf";
        	final String logCommand = "log";
        	final String quitCommand = "quit";

        	radioButtons[0] = new JRadioButton("Play Legends of Terris");
        	radioButtons[0].setActionCommand(terrisCommand);

        	radioButtons[1] = new JRadioButton("Play Legends of Cosrin");
        	radioButtons[1].setActionCommand(cosrinCommand);

        	radioButtons[2] = new JRadioButton("Play Wolfenburg");
        	radioButtons[2].setActionCommand(wolfCommand);

        	radioButtons[3] = new JRadioButton("View an existing log file");
        	radioButtons[3].setActionCommand(logCommand);

        	radioButtons[4] = new JRadioButton("Quit");
        	radioButtons[4].setActionCommand(quitCommand);

        	for (int i = 0; i < numButtons; i++) {
        	    group.add(radioButtons[i]);
        	}
        	radioButtons[game].setSelected(true);

			playButton = new JButton("Do It!");
        	playButton.addActionListener(new ActionListener() {
        	    public void actionPerformed(ActionEvent e) {
					if (tmp == -11) {
						String command = group.getSelection().getActionCommand();
						if (command == terrisCommand) {
							tmp = 0;
						} else if (command == cosrinCommand) {
							tmp = 1;
						} else if (command == wolfCommand) {
							tmp = 2;
						} else if (command == logCommand) {
							tmp = 3;
						} else {
							tmp = 4;
						}
					}
					reconnect(null);
				}

        	});

        	for (int i = 0; i < numButtons; i++) {
        	    box.add(radioButtons[i]);
        	}

        	JPanel pane = new JPanel();
        	pane.setLayout(new BorderLayout());
        	pane.add(box, BorderLayout.NORTH);
        	pane.add(playButton, BorderLayout.SOUTH);
        	menu.getRootPane().setDefaultButton(playButton);
        	menu.getContentPane().add(pane);
        	menu.pack();
        	menu.setSize(menu.getWidth() + 15, menu.getHeight() + 15);
        	menu.addWindowListener(new WindowAdapter() {
        	    public void windowClosing(WindowEvent e) {
        	        System.exit(0);
        	    }
        	});

        	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        	Dimension labelSize = menu.getPreferredSize();
        	menu.setLocation(screenSize.width/2 - (labelSize.width/2), screenSize.height/2 - (labelSize.height/2));
        	menu.setVisible(true);
		}
	}

	private static void play() {
		if (tmp == 3) {
			String fileName = "";
			File file;
			File kilcliDir = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "logs" + System.getProperty("file.separator"));
			JFileChooser chooser = new JFileChooser();
			// Note: source for ExampleFileFilter can be found in FileChooserDemo,
			// under the demo/jfc directory in the Java 2 SDK, Standard Edition.
			ExampleFileFilter filter = new ExampleFileFilter();
			filter.addExtension("txt");
			filter.setDescription("KilCli Log Files");
			chooser.setCurrentDirectory(kilcliDir);
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				fileName = chooser.getSelectedFile().getName();
			    file = chooser.getSelectedFile();
			    new LogViewingThread(fileName, file, 0).start();
 		   } else {
				quit(null);
			}
		} else if (tmp == 4) {
			System.exit(0);
		} else {
			mainFrame = new KilCli(tmp, laf, theme, themeString);
			mainFrame.initialize();
		}
	}
    public static void quit(JFrame frame) {
    	if (frame != null) {
			frame.dispose();
		}
		tmp = -11;
		new KilCliThread("restart").start();
	}

	public static void reconnect(JFrame frame) {
		if (frame != null) {
			frame.dispose();
		} else {
			menu.dispose();
		}
		new KilCliThread("restart", tmp).start();
	}

	public static KilCli getKilCli() {
		return mainFrame;
	}
}
