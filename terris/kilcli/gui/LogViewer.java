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

package terris.kilcli.gui;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JInternalFrame;
import javax.swing.JComponent;
import javax.swing.text.DefaultEditorKit;
import javax.swing.UIManager;
import javax.swing.*;

import java.io.IOException;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.awt.Component;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;


import terris.kilcli.loader.*;
import terris.kilcli.io.*;
import terris.kilcli.resource.*;
import terris.kilcli.thread.*;
import terris.kilcli.window.*;
import terris.kilcli.writer.*;
import terris.kilcli.theme.*;
import terris.kilcli.gui.*;

/**
 * LogViewer for KilCli is the GUI used to view log files<br>
 * Ver: 1.0.1
 */

public class LogViewer extends JFrame implements ActionListener{

	//gui & window variables
	private JDesktopPane desktop;
	private GameWindow game;
	private GameWindow guildshouts;
	private GameWindow shouts;
	private GameWindow tells;
	private GameWindow chats;
	private GameWindow templeshouts;
	private GameWindow hhshouts;
	private GameWindow events;
	private GameWindow wails;
	private GameWindow logons;

	//IO variables
	private String input = "";
	private int stringSearchIndex;
	private int stringSearchIndex2;
	private String tempString1;
	private String previewString;
	private char previewChar;
	private boolean skipOutput;
	private String line = "";

	private String logFileName = "";
	private String[] settings;
	private static String[] options;
	private File srcFile;
	private int calledFrom = 0;
	private static Hashtable textRouting = new Hashtable();
	private BufferedReader inFile;
	private boolean paused = false;
	private JMenuItem resume;
	private JMenuItem pause;
	private JMenuItem reload;

	/**
	 * The "main" constructor.  Creates the frames, sets up the windows,<br>
	 * gets everything ready to "work".
	 */

    public LogViewer(String logName, File logFile, int c) {
        super("KilCli Log Viewer");
		Image icon = Toolkit.getDefaultToolkit().getImage("kilcli.jpg");
		setIconImage(icon);
		calledFrom = c;

        //Quit this app when the big window closes.
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
				exit();
			}
        });

		logFileName = logName;
		srcFile = logFile;

		ConfigLoader configLoader = new ConfigLoader();
		try {
		    options = configLoader.load("default");
		} catch (Exception e) {}

    	createTextRouting();

    	try {
    		settings = WindowConfigLoader.load("default");
		} catch (Exception e) {}

        //...Then set the window size
        setSize(Integer.parseInt(settings[48]), Integer.parseInt(settings[49]));

        //Set the window's location.
        setLocation(Integer.parseInt(settings[46]), Integer.parseInt(settings[47]));


        //create the virtual desktop and make it the content pane
        desktop = new JDesktopPane(); //a specialized layered pane
        setContentPane(desktop);

        //Make dragging faster:
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

		//set the menu bar
		setJMenuBar(createBar());

        //make it visible
        setVisible(true);
    }


	/**
	 * Creates the title bar menus
	 *
	 * @return JMenuBar - the menu bar with all needed menus
	 */

	protected JMenuBar createBar() {
		//create a menubar
        JMenuBar menuBar = new JMenuBar();
		JMenuItem mi;

		//create a file menu
        JMenu file = new JMenu("File");
		//set file menu to open with Alt-F
        file.setMnemonic(KeyEvent.VK_F);

        mi = new JMenuItem("View Log File");
        //set a listener to know when view log file has been selected
        mi.addActionListener(new ActionListener() {
			//function to view log file
			public void actionPerformed(ActionEvent e) {
				try {
					Runtime.getRuntime().exec("java -jar KilCli.jar log");
				} catch (Exception eeee) {
					eeee.printStackTrace();
				}
			}
		});
		//add view log file to file menu
		file.add(mi);
		file.addSeparator();

		//create an exit item
        mi = new JMenuItem("Exit");
        //set a listener to know when exit has been selected
        mi.addActionListener(new ActionListener() {
           	//function to exit program
            public void actionPerformed(ActionEvent e) {
				exit();
            }
        });
        //add exit item to file menu
        file.add(mi);

		//the edit menu for cut/copy/paste/etc
        JMenu edit = new JMenu("Edit");
        edit.setMnemonic(KeyEvent.VK_E);
		mi = new JMenuItem("Copy");
        //creates a "copy" menu that gets text from highlighted window
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (desktop.getSelectedFrame() instanceof GameWindow) {
					GameWindow tmp = (GameWindow)desktop.getSelectedFrame();
					tmp.copy();
				}
			}
		});
		edit.add(mi);

		mi = new JMenuItem("Select All");
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        //creates a "select all" menu that selects all the text of the highlighted window
        mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (desktop.getSelectedFrame() instanceof GameWindow) {
					GameWindow tmp = (GameWindow)desktop.getSelectedFrame();
					tmp.selectAll();
				}
			}
		});
		edit.add(mi);

		JMenu view = new JMenu("View");
		view.setMnemonic(KeyEvent.VK_V);
		resume = new JMenuItem("Resume");
		resume.setEnabled(false);
		resume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resume.setEnabled(false);
				pause.setEnabled(true);
				paused = false;
				try {
					read();
				} catch (IOException ioe) {}
			}
		});
		view.add(resume);

		pause = new JMenuItem("Pause");
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pause.setEnabled(false);
				resume.setEnabled(true);
				paused = true;
			}
		});
		view.add(pause);

		reload = new JMenuItem("Reload");
		//reload.setEnabled(false);
		reload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paused = true;
				pause.setEnabled(true);
				resume.setEnabled(false);
				game.Purge();
				guildshouts.Purge();
				templeshouts.Purge();
				tells.Purge();
				shouts.Purge();
				chats.Purge();
				hhshouts.Purge();
				events.Purge();
				wails.Purge();
				logons.Purge();
				try {
					paused = false;
					load();
				} catch (IOException ioe) {}
			}
		});
		view.add(reload);

        //add file and purge menus to menu bar
        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(view);

        return menuBar;
	}

	/**
	 * Creates Windows, calls connect routine, calls run routine
	 */

	public void initialize() {
		//create windows
		createFrames();
		//outputs an initial Welcome message
		gameWrite("Now Viewing: " + logFileName + "<br>");
		try {
			load();
		} catch (IOException ioe) {}
	}

	/**
	 * Reads through the file displaying text as appropriate
	 */
	private void load() throws IOException {
		if (!srcFile.exists()) {
			//error
		} else if (!srcFile.isFile() || !srcFile.canRead()) {
			//error
		} else {
			try {
				inFile = new BufferedReader(new FileReader(srcFile));
				read();
			} catch (FileNotFoundException fnfe) {}
		}
	}

	public void actionPerformed(ActionEvent evt) {
		exit();
	}

	private void read() throws IOException {
    	while (inFile.ready() && !paused) {
			try {
				line = inFile.readLine();
				line = inputLineCheck(line);
				if (!skipOutput) {
					gameWrite(input);
				}
				input = "";
			} catch (IOException ioe) {}
		}
	}

	/**
	 * Method to check a line input from the game.<br>
	 * to see what should be done with it
	 */

	public String inputLineCheck(String tempInput) {
		//set the initial search index
		stringSearchIndex = tempInput.indexOf("<");
		//check to see if we need to convert any < (to avoid the html parser)
		while (stringSearchIndex != -1) {
			stringSearchIndex = tempInput.indexOf("<", stringSearchIndex);
		    if (stringSearchIndex != -1) {
				tempString1 = tempInput.substring(0, stringSearchIndex);
				tempInput = tempString1 + "&lt;" + tempInput.substring(stringSearchIndex + 1, tempInput.length());
			}
		}
		//reset search index
		stringSearchIndex = 0;

		//get the first character of the line
		if (tempInput.length() > 0) {
			previewChar = tempInput.charAt(0);

			//check if line has characters that need to be translated
			tempInput = characterTranslate(tempInput);

			//if that character is a '['...
			if (previewChar == 91) {
				//get the second character
				previewChar = tempInput.charAt(1);
				//call the window picker
				tempInput = pickWindow(tempInput);
			}

			//if we haven't been told to ignore this line
			if (!skipOutput) {
				//if this is not the first line of the packet
				input = tempInput;
			}
		}

		//reset flags and temp vars
		skipOutput = false;
		previewString = "";
		tempInput = "";
		return input;
	}

	/**
	 * Creates the various windows, i.e Game, Shouts, command line, status bar, Chats, etc,<br>
	 * makes them visible, and adds them to the desktop
     */

    protected void createFrames() {
		String cssFolder = System.getProperty("user.dir") + System.getProperty("file.separator") + "css" + System.getProperty("file.separator");


		//creates the windows
       	game = new GameWindow("Game Window", cssFolder + "gamewindow.css", Integer.parseInt(settings[1]), Integer.parseInt(settings[2]), Integer.parseInt(settings[3]), Integer.parseInt(settings[4]), options[47].equalsIgnoreCase("true"), false);
        guildshouts = new GameWindow("Guild Shouts Window", cssFolder + "guildshoutswindow.css", Integer.parseInt(settings[16]), Integer.parseInt(settings[17]), Integer.parseInt(settings[18]), Integer.parseInt(settings[19]), options[48].equalsIgnoreCase("true"), false);
        templeshouts = new GameWindow("Temple Shouts Window", cssFolder + "templeshoutswindow.css", Integer.parseInt(settings[21]), Integer.parseInt(settings[22]), Integer.parseInt(settings[23]), Integer.parseInt(settings[24]), options[59].equalsIgnoreCase("true"), false);
        tells = new GameWindow("Tells Window", cssFolder + "tellswindow.css", Integer.parseInt(settings[26]), Integer.parseInt(settings[27]), Integer.parseInt(settings[28]), Integer.parseInt(settings[29]), options[57].equalsIgnoreCase("true"), false);
        chats = new GameWindow("Chats Window", cssFolder + "chatswindow.css", Integer.parseInt(settings[31]), Integer.parseInt(settings[32]), Integer.parseInt(settings[33]), Integer.parseInt(settings[34]), options[44].equalsIgnoreCase("true"), false);
        shouts = new GameWindow("Shouts Window", cssFolder + "shoutswindow.css", Integer.parseInt(settings[36]), Integer.parseInt(settings[37]), Integer.parseInt(settings[38]), Integer.parseInt(settings[39]), options[53].equalsIgnoreCase("true"), false);
        hhshouts = new GameWindow("HHShouts Window", cssFolder + "hhshoutswindow.css", Integer.parseInt(settings[51]), Integer.parseInt(settings[52]), Integer.parseInt(settings[53]), Integer.parseInt(settings[54]), options[50].equalsIgnoreCase("true"), false);
        events = new GameWindow("Events Window", cssFolder + "eventswindow.css", Integer.parseInt(settings[56]), Integer.parseInt(settings[57]), Integer.parseInt(settings[58]), Integer.parseInt(settings[59]), options[46].equalsIgnoreCase("true"), false);
        wails = new GameWindow("Wails Window", cssFolder + "wailswindow.css", Integer.parseInt(settings[61]), Integer.parseInt(settings[62]), Integer.parseInt(settings[63]), Integer.parseInt(settings[64]), options[60].equalsIgnoreCase("true"), false);
   		logons = new GameWindow("Logons Window", cssFolder + "logonswindow.css", Integer.parseInt(settings[96]), Integer.parseInt(settings[97]), Integer.parseInt(settings[98]), Integer.parseInt(settings[99]), options[51].equalsIgnoreCase("true"), false);

        try {

			//check if we have to remove the game window title bar
			if (settings[65].toLowerCase().equals("false")) {
				game.removeBar();
			}

			//check the game window setting
	        if (settings[0].toLowerCase().equals("true")) {
        		game.setClosed(true);
			} else {
				game.setVisible(true);
				desktop.add(game);
			}

			//check if we have to remove the GShouts title bar
			if (settings[68].toLowerCase().equals("false")) {
				guildshouts.removeBar();
			}

			//check the guildshouts window setting
	        if (settings[15].toLowerCase().equals("true")) {
        		guildshouts.setClosed(true);
			} else {
				guildshouts.setVisible(true);
				desktop.add(guildshouts);
			}

			//check if we have to remove the TShouts title bar
			if (settings[69].toLowerCase().equals("false")) {
				templeshouts.removeBar();
			}

			//check the templeshouts window setting
	        if (settings[20].toLowerCase().equals("true")) {
        		templeshouts.setClosed(true);
			} else {
				templeshouts.setVisible(true);
				desktop.add(templeshouts);
			}

			//check if we have to remove the Tells title bar
			if (settings[70].toLowerCase().equals("false")) {
				tells.removeBar();
			}

			//check the tells window setting
	        if (settings[25].toLowerCase().equals("true")) {
        		tells.setClosed(true);
			} else {
				tells.setVisible(true);
				desktop.add(tells);
			}

			//check if we have to remove the Chats title bar
			if (settings[71].toLowerCase().equals("false")) {
				chats.removeBar();
			}

			//check the chats window setting
	        if (settings[30].toLowerCase().equals("true")) {
        		chats.setClosed(true);
			} else {
				chats.setVisible(true);
				desktop.add(chats);
			}

			//check if we have to remove the Shouts title bar
			if (settings[72].toLowerCase().equals("false")) {
				shouts.removeBar();
			}

			//check the shouts window setting
	        if (settings[35].toLowerCase().equals("true")) {
        		shouts.setClosed(true);
			} else {
				shouts.setVisible(true);
				desktop.add(shouts);
			}

			//check if we have to remove the HHShouts title bar
			if (settings[74].toLowerCase().equals("false")) {
				hhshouts.removeBar();
			}

			//check the hhshouts line setting
	        if (settings[50].toLowerCase().equals("true")) {
        		hhshouts.setClosed(true);
			} else {
				hhshouts.setVisible(true);
				desktop.add(hhshouts);
			}

			//check if we have to remove the Events title Bar
			if (settings[75].toLowerCase().equals("false")) {
				events.removeBar();
			}

			//check the events line setting
	        if (settings[55].toLowerCase().equals("true")) {
        		events.setClosed(true);
			} else {
				events.setVisible(true);
				desktop.add(events);
			}

			//check if we have to remove the Wails title bar
			if (settings[76].toLowerCase().equals("false")) {
				wails.removeBar();
			}

			//check the wails line setting
	        if (settings[60].toLowerCase().equals("true")) {
        		wails.setClosed(true);
			} else {
				wails.setVisible(true);
				desktop.add(wails);
			}

			//check the logons line setting
			if (settings[95].toLowerCase().equals("true")) {
				logons.setClosed(true);
			} else {
				logons.setVisible(true);
				desktop.add(logons);
			}

			//check if we have to remove the Logons title bar
			if (settings[106].toLowerCase().equals("false")) {
				logons.removeBar();
			}

		} catch (Exception e) {}

    }


	/**
	 * Output line to the Guild Shouts Window (if open)
	 *
	 * @param input String that is attempting to be outputted
	 */

	public void guildShoutsWrite(String input) {
		//if guildshouts window is closed...
		if (guildshouts.isClosed()) {
			//test shouts window, if closed...
			if (shouts.isClosed()) {
				//test game window, if closed....
				if (game.isClosed()) {
					//input lost, no windows open
				} else {
					//otherwise, write to game
					game.Write(options[6] + input);
				}
			} else {
				//otherwise, write to shouts
				shouts.Write(options[6] + input);
			}
		} else {
			//otherwise, write to guildshouts
			guildshouts.Write(input);
		}
	}

	/**
	 * Output line to the Shouts Window (if open)
	 *
	 * @param input String that is attempting to be outputted
	 */
	public void shoutsWrite(String input) {
		//if shouts window is closed...
		if (shouts.isClosed()) {
			//test game window, if closed...
			if (game.isClosed()) {
				//input lost, no windows open
			} else {
				//otherwise, write to game
				game.Write(options[10] + input);
			}
		} else {
			//otherwise, write to shouts
			shouts.Write(input);
		}
	}

	/**
	 * Output line to the Tells Window (if open)
	 *
	 * @param input String that is attempting to be outputted
	 */

	public void tellsWrite(String input) {
		//if tells window is closed...
		if (tells.isClosed()) {
			//test game window, if closed...
			if (game.isClosed()) {
				//input lost, no windows open
			} else {
				//otherwise, write to game
				game.Write(options[8] + input);
			}
		} else {
			//otherwise, write to tells
			tells.Write(input);
		}

	}

	/**
	 * Output line to the Temple Shouts Window (if open)
	 *
	 * @param input String that is attempting to be outputted
	 */

	public void templeShoutsWrite(String input) {
		//if templeshouts window is closed...
		if (templeshouts.isClosed()) {
			//test shouts window, if closed...
			if (shouts.isClosed()) {
				//test game window, if closed....
				if (game.isClosed()) {
					//input lost, no windows open
				} else {
					//otherwise, write to game
					game.Write(options[7] + input);
				}
			} else {
				//otherwise, write to shouts
				shouts.Write(options[7] + input);
			}
		} else {
			//otherwise, write to templeshouts
			templeshouts.Write(input);
		}
	}

	/**
	 * Output line to the Chats Window (if open)
	 *
	 * @param input String that is attempting to be outputted
	 */

	public void chatsWrite(String input) {
		//if chats window is closed...
		if (chats.isClosed()) {
			//test shouts window, if closed...
			if (shouts.isClosed()) {
				//test game window, if closed....
				if (game.isClosed()) {
					//input lost, no windows open
				} else {
					//otherwise, write to game
					game.Write(options[11] + input);
				}
			} else {
				//otherwise, write to shouts
				shouts.Write(options[11] + input);
			}
		} else {
			//otherwise, write to chats
			chats.Write(input);
		}
	}

	/**
	 * Output line to the HH Shouts Window (if open)
	 *
	 * @param input String that is attempting to be outputted
	 */

	public void hhShoutsWrite(String input) {
		//if hhshouts window is closed...
		if (hhshouts.isClosed()) {
			//test shouts window, if closed...
			if (shouts.isClosed()) {
				//test game window, if closed...
				if (game.isClosed()) {
					//input lost, no windows open
				} else {
					//otherwise write to game
					game.Write(options[9] + input);
				}
			} else {
				//otherwise, write to shouts
				shouts.Write(options[9] + input);
			}
		} else {
			//otherwise, write to hhshouts
			hhshouts.Write(input);
		}
	}

	/**
	 * Output line to the Wails Window (if open)
	 *
	 * @param input String that is attempting to be outputted
	 */
	public void wailsWrite(String input) {
		//if wails window is closed...
		if (wails.isClosed()) {
			//test shouts window, if closed...
			if (shouts.isClosed()) {
				//test game window, if closed...
				if (game.isClosed()) {
					//input lost, no windows open
				} else {
					//otherwise write to game
					game.Write(options[12] + input);
				}
			} else {
				//otherwise, write to shouts
				shouts.Write(options[12] + input);
			}
		} else {
			//otherwise, write to hhshouts
				wails.Write(input);
		}
	}

	/**
	 * Output line to the Quests Window
	 *
	 * @param input String that is attempting to be outputted
	 */

	public void questsWrite(String input) {
		if (game.isClosed()) {
			//input lost, no windows open
		} else {
			game.Write(options[14] + input);
		}
	}

	/**
	 * Output line to the Logons Window (if open)
	 *
	 * @param input String that is attempting to be outputted
	 */

	public void logonsWrite(String input) {
		//if logons window is closed...
		if (logons.isClosed()) {
			//test game window, if closed....
			if (game.isClosed()) {
				//input lost, no windows open
			} else {
				//otherwise, write to game
				game.Write(options[36] + input);
			}
		} else {
			//otherwise, write to logons
			logons.Write(input);
		}
	}

	/**
	 * Output line to the Events Window (if open)
	 *
	 * @param input String that is attempting to be outputted
	 */

	public void eventsWrite(String input) {
		//if events window is closed...
		if (events.isClosed()) {
			//test game window, if closed...
			if (game.isClosed()) {
				//input lost, no windows open
			} else {
				//otherwise write to game
				game.Write(options[13] + input);
			}

		} else {
			//otherwise, write to events
			events.Write(input);
		}
	}

	/**
	 * Output line to the Main Window (if open)
	 *
	 * @param input String that is attempting to be outputted
	 */

	public void gameWrite(String input) {
		//if game window is closed...
		if (game.isClosed()) {
			//input lost, game window not open
		} else {
			//otherwise, write to game
			game.Write(input);
		}
	}


	/**
     * Determines what window to try and print to, based on control sequence
     *
     * @param input The String to be outputted
     * @return String The input string after going through any translation for output
     */

	public String pickWindow(String input) {

		//get the second character of the line
		previewChar = input.charAt(1);
		previewChar = textRouting(previewChar);

		if (previewChar < 72) {
			//get the second character of the line to pick proper window
			switch(previewChar) {

				//if its C, print to the Chats window
				case 'C':
					input = input.substring(3, input.length());
					chatsWrite(input);
					skipOutput = true;
					break;

				//if its E, print to the events window
				case 'E':
					input = input.substring(3, input.length());
					eventsWrite(input);
					skipOutput = true;
					break;

				//if its G, print to the GShouts window
				case 'G':
					input = input.substring(3, input.length());
					guildShoutsWrite(input);
					skipOutput = true;
					break;

				default:
					break;
			}
		} else if (previewChar < 82) {

			switch(previewChar) {

				//if its H, print to the hhshouts window
				case 'H':
					input = input.substring(3, input.length());
					hhShoutsWrite(input);
					skipOutput = true;
					break;

				//if its M, print to the shouts window
				case 'M':
					input = input.substring(3, input.length());
					shoutsWrite(input);
					skipOutput = true;
					break;

				//if its Q, print to the quests window
				case 'Q':
					input = input.substring(3, input.length());
					questsWrite(input);
					skipOutput = true;
					break;

				default:
					break;
			}
		} else if (previewChar < 122) {

			switch(previewChar) {
				//if its T, print to the TShouts window
				case 'T':
					input = input.substring(3, input.length());
					templeShoutsWrite(input);
					skipOutput = true;
					break;

				//if its W, print to the wails window
				case 'W':
					input = input.substring(3, input.length());
					wailsWrite(input);
					skipOutput = true;
					break;

				//if its t, print to the tells window
				case 't':
					input = input.substring(3, input.length());
					tellsWrite(input);
					skipOutput = true;
					break;

				//otherwise do nothing
				default:
					break;
			}
		}
		return input;
	}

	/**
	 * Checks if the text is supposed to be sent to a different
	 * window than normal
	 *
	 * @param tempchar The character that represents the standard window
	 * @return The character of the window that text should be sent to
	 */

	private static char textRouting(char tempchar) {
		try {
			return ((String)(textRouting.get(new Integer(tempchar)))).charAt(0);
		} catch (Exception e) {
			//System.err.println("Bad text routing.  Tempchar=" + tempchar);

		}
		return tempchar;
	}

	private static void createTextRouting() {
		textRouting.clear();
		//chats
		textRouting.put(new Integer('C'), options[27]);
		//events
		textRouting.put(new Integer('E'), options[28]);
		//gshouts
		textRouting.put(new Integer('G'), options[29]);
		//hhshouts
		textRouting.put(new Integer('H'), options[30]);
		//logons
		textRouting.put(new Integer('L'), options[37]);
		//shouts
		textRouting.put(new Integer('M'), options[31]);
		//quests
		textRouting.put(new Integer('Q'), options[32]);
		//tshouts
		textRouting.put(new Integer('T'), options[33]);
		//wails
		textRouting.put(new Integer('W'), options[34]);
		//tells
		textRouting.put(new Integer('t'), options[35]);
	}

	/**
	 * Preps writes to display: \,/,[ properly
	 *
	 * @param input The input string to be translated
	 * @return String The input string after being translated
	 */

	public String characterTranslate(String input) {
		//check for "/" to display them properly (mega-wiz compatible)
		while (stringSearchIndex != -1) {
			stringSearchIndex = input.indexOf("&fs.", stringSearchIndex);
			if (stringSearchIndex != -1) {
				input = input.substring(0, stringSearchIndex) + "/" + input.substring(stringSearchIndex + 4, input.length());
			}
		}
		//reset search index
		stringSearchIndex = 0;

		//check for "\" to display them properly (mega-wiz compatible)
		while (stringSearchIndex != -1) {
			stringSearchIndex = input.indexOf("&bs.", stringSearchIndex);
			if (stringSearchIndex != -1) {
		  		input = input.substring(0, stringSearchIndex) + "\\" + input.substring(stringSearchIndex + 4, input.length());
			}

		}
		//reset search index
		stringSearchIndex = 0;

		//check for "[" to display them properly (mega-wiz compatible)
		while (stringSearchIndex != -1) {
			stringSearchIndex = input.indexOf("&lsb", stringSearchIndex);
			if (stringSearchIndex != -1) {
		 		input = input.substring(0, stringSearchIndex) + "[" + input.substring(stringSearchIndex + 5, input.length());
			}

		}
		//reset search index
		stringSearchIndex = 0;
		return input;
	}

	/**
	 * Closes the log viewer
	 */
	public void exit() {
		if (calledFrom == 0) {
			KilCliThread.quit(this);
		} else if (calledFrom == -1) {
			System.exit(0);
		} else {
			setVisible(false);
			game = null;
			guildshouts = null;
			shouts = null;
			tells = null;
			chats = null;
			templeshouts = null;
			hhshouts = null;
			events = null;
			wails = null;
			logons = null;
			desktop = null;
			this.dispose();
		}
	}

}
