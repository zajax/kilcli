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
 */

package terris.kilcli;

import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.SwingUtilities;
import javax.swing.JInternalFrame;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.Action;
import javax.swing.text.DefaultEditorKit;
import javax.swing.ButtonGroup;
import javax.swing.AbstractAction;
import javax.swing.LookAndFeel;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.MenuListener;

import java.awt.event.InputEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.io.File;
import java.awt.Component;
import java.awt.Image;
import java.net.URL;

import com.l2fprod.gui.plaf.skin.Skin;
import com.l2fprod.gui.plaf.skin.CompoundSkin;
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;

import com.jgoodies.plaf.plastic.Plastic3DLookAndFeel;
import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import com.jgoodies.plaf.plastic.PlasticTheme;
import com.jgoodies.plaf.plastic.PlasticXPLookAndFeel;

import edu.stanford.ejalbert.*;

import terris.kilcli.loader.*;
import terris.kilcli.io.*;
import terris.kilcli.resource.*;
import terris.kilcli.thread.*;
import terris.kilcli.window.*;
import terris.kilcli.writer.*;
import terris.kilcli.theme.*;
import terris.kilcli.gui.*;

/**
 * KilCli for KilCli is the "main" section for KilCli<br>
 * Ver: 1.0.2
 */

public class KilCli extends JFrame implements ActionListener {
	private static final String VERSION = "KilCli - 1.0.2";

	//connection variables
	private static boolean loggedIn = false;
	private static SendReceive sendReceive;
	private static ChatIO chatIO;
	private static int gameNumber = 0;

	//gui & window variables
	private static JDesktopPane desktop;
	private static GameWindow game;
	private static GameWindow guildshouts;
	private static GameWindow shouts;
	private static CommandLine command;
	private static StatusBar status;
	private static GameWindow tells;
	private static GameWindow chats;
	private static GameWindow templeshouts;
	private static HelpWindow help;
	private static MovementPanel movement;
	private static GameWindow hhshouts;
	private static GameWindow events;
	private static GameWindow wails;
	private static HandsPanel hands;
	private static TimersPanel timers;
	private static EffectsPanel effects;
	private static HardenPanel harden;
    private static JMenu themesMenu = null;
    private static JMenu kunststoffMenu = null;
    private static JMenu plastic3DMenu = null;
    private static JMenu plasticXPMenu = null;
    private static JMenu plasticMenu = null;
    private static GameWindow logons;
    private static StatusBarHor statusHor;
    private static InfoPanel info;

	//IO variables
	private static String input = "";
	private static int stringSearchIndex;
	private static int stringSearchIndex2;
	private static String lowerCase;
	private static String previewString;
	private static char previewChar;
	private static boolean skipOutput;
	private static boolean skipSend = false;
	private static boolean aliasFound = false;
	private static String tempSendString;
	private static int sendStringIndex;
	private static String tempInput = "";
	private static String alias;
	private static boolean skipEcho = false;
	private static boolean error = false;

	//status bar variables
	private static int maxHPs = 0;
	private static int HPs = 0;
	private static int maxSPs = 0;
	private static int SPs = 0;
	private static long percentHP;
	private static long percentSP;
	private static boolean HPWarning;
	private static boolean SPWarning;
	private static String goldString = "0";
	private static String expString = "0";
	private static String dirString = "000000000000";
	private static int timer = 0;
	private static int spamCount = 0;
	private static int stunTime = 0;
	private static int slowTime = 0;
	private static Timer unbalanced;
	private static Timer spamTimer;
	private static Timer stunned;
	private static Timer slowed;

	//config variables
	private static boolean logging = false;
	private static StringTokenizer st;
	private static ArrayList aliases;
	private static ArrayList macros;
	private static ArrayList nicknames;
	private static String[] options = new String[87];
	private static String[] colors;
	private static String[] settings;
	private static boolean sounds = true;
	private static String profile = "";
	private static boolean checkHarden = false;
	private static boolean chatToKchat = false;

	//arrow key command recall variables
	private static String[] lastCommands;
	private static String[] lastCommandsTmp;
	private static int commandCount = -2;
	private static int commandDirection = 2;

	//system vars
	private static String[] characters = new String[3];
	private static int quitFlag = -1;
	private static String name;
	private static LogThread log;
	private static MP3Thread mp3 = null;
	public static int count = -1;
	private static int hardenedItem = 0;
	private static ButtonGroup themesMenuGroup = new ButtonGroup();
	private static ButtonGroup kunststoffGroup = new ButtonGroup();
	private static ButtonGroup skinsGroup = new ButtonGroup();
	private static ButtonGroup plastic3DGroup = new ButtonGroup();
	private static ButtonGroup plasticXPGroup = new ButtonGroup();
	private static ButtonGroup plasticGroup = new ButtonGroup();
    private static String currentLookAndFeel;
    private static PlasticTheme theme;
    private static Hashtable textRouting = new Hashtable();
    private static final String mac = "com.sun.java.swing.plaf.mac.MacLookAndFeel";
    private static final String windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    private static final String gtk = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
	private static final BASE64Decoder decoder  = new BASE64Decoder();
	private static ArrayList encryptedTells = new ArrayList();

	/**
	 * The main constructor.  Creates the timers, desktop, and other background tasks.<br>
	 * gets everything ready to function.
	 */

    public KilCli(int gn, String laf, PlasticTheme th, String themeString) {
        super(VERSION + " : Profile = " + profile);

        gameNumber = gn;
        name = "" + gameNumber;
        currentLookAndFeel = laf;
		this.theme = th;
		options[20] = themeString;
		setOriginalLookAndFeel();


		Image icon = Toolkit.getDefaultToolkit().getImage("kilcli.jpg");
		setIconImage(icon);

        //create the virtual desktop and make it the content pane
        desktop = new JDesktopPane();
        this.setContentPane(desktop);

        //Make dragging faster:
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

		//set the profiles
		setProfile("default");

		if ((options[0].toLowerCase().equals("true")) && (options[23].toLowerCase().equals("true"))) {
			MP3Thread startup = new MP3Thread("start", options[22]);
		}

		if (options[23].toLowerCase().equals("true")) {
			//enable sounds
			sounds = true;
		}

    	//initialize the command storage array (for use with up/down arrows)
    	lastCommands = new String[50];
    	lastCommandsTmp = new String[50];

    	//unbalanced timer
    	unbalanced = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timer--;
				if (!status.isClosed()) {
					status.UpdateUnbal(timer);

				}
				if (!statusHor.isClosed()) {
					statusHor.UpdateUnbal(timer);
				}

				if (timer <= 0) {
					unbalanced.stop();
				}
				if (timer < 0) {
					timer = 0;
					if (!status.isClosed()) {
						status.UpdateUnbal(timer);
					}
					if (!statusHor.isClosed()) {
						statusHor.UpdateUnbal(timer);
					}
				}
			}
		});
		unbalanced.setRepeats(true);

    	//spam timer
    	spamTimer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spamCount--;
				if (!timers.isClosed()) {
					timers.UpdateSpamTimer(spamCount);
				}
				if (spamCount <= 0) {
					spamTimer.stop();
				}
				if (spamCount < 0) {
					spamCount = 0;
					if (!timers.isClosed()) {
						timers.UpdateSpamTimer(spamCount);
					}
				}
			}
		});
		spamTimer.setRepeats(true);

    	//stunned, held, entangled, webbed timer
    	stunned = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stunTime--;
				if (!timers.isClosed()) {
					timers.UpdateStunTimer(stunTime);
				}
				if (stunTime <= 0) {
					stunned.stop();
				}
				if (stunTime < 0) {
					stunTime = 0;
					if (!timers.isClosed()) {
						timers.UpdateStunTimer(stunTime);
					}
				}
			}
		});
		stunned.setRepeats(true);

    	//slowed, confused, pacified timer
    	slowed = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				slowTime--;
				if (!timers.isClosed()) {
					timers.UpdateSlowTimer(slowTime);
				}
				if (slowTime <= 0) {
					slowed.stop();
				}
				if (slowTime < 0) {
					slowTime = 0;
					if (!timers.isClosed()) {
						timers.UpdateSlowTimer(slowTime);
					}
				}
			}
		});
		slowed.setRepeats(true);

        //Quit this app when the big window closes.
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
				int n = JOptionPane.showConfirmDialog(KilCliThread.getKilCli(), "Are you sure you wish to quit?", "Quit?", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					exit("quit");
				}
			}
        });

		checkTheme(currentLookAndFeel, theme);

  		//set window size based on new profile
        //...Then set the window size
        this.setSize(Integer.parseInt(settings[48]), Integer.parseInt(settings[49]));

        //Set the window's location.
        this.setLocation(Integer.parseInt(settings[46]), Integer.parseInt(settings[47]));

		//Set close operation
		this.setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
		//create the menubar
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

        //create a disconnect item
        mi = new JMenuItem("Disconnect");
		//set a listener to know when disconnect has been selected
		mi.addActionListener(new ActionListener() {
			//function to exit program
		    public void actionPerformed(ActionEvent e) {
				disconnect("quit", 0);
		    }
		});
		//add exit item to file menu
        file.add(mi);

		mi= new JMenuItem("Reconnect");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reconnect();
			}
		});
		file.add(mi);
		file.addSeparator();

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
				exit("quit");
            }
        });
        //add exit item to file menu
        file.add(mi);

		//the edit menu for cut/copy/paste/etc
        JMenu edit = new JMenu("Edit");
        edit.setMnemonic(KeyEvent.VK_E);
		mi = new JMenuItem("Cut");
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        //creates "cut" menu that gets text from highlighted window
        mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JInternalFrame tmp = desktop.getSelectedFrame();
				if (tmp.equals(command)) {
					command.cut();
				} else if (desktop.getSelectedFrame() instanceof GameWindow) {
					GameWindow tmp2 = (GameWindow)tmp;
					tmp2.copy();
				}
			}
		});
		edit.add(mi);

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

		mi = new JMenuItem("Paste");
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        //creates a "paste" menu that pastes into the commandLine
        mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				command.paste();
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

		//create a windows menu
        JMenu windows = new JMenu("Windows");

		//now add options to open/close all the windows
        windows.add(windowsMenuItem("Game Window", game));
        windows.add(windowsMenuItem("Shouts Window", shouts));
        windows.add(windowsMenuItem("Guild Shouts Window", guildshouts));
        windows.add(windowsMenuItem("Temple Shouts Window", templeshouts));
        windows.add(windowsMenuItem("HH Shouts Window", hhshouts));
        windows.add(windowsMenuItem("Wails Window", wails));
        windows.add(windowsMenuItem("Tells Window", tells));
        windows.add(windowsMenuItem("Chats Window", chats));
        windows.add(windowsMenuItem("Events Window", events));
		windows.add(windowsMenuItem("Logons Window", logons));
		//windows.add(windowsMenuItem("Quests Window", quests));
        windows.addSeparator();
        windows.add(windowsMenuItem("Help Window", help));
        windows.addSeparator();
        windows.add(windowsMenuItem("Status Bar", status));
        windows.add(windowsMenuItem("Horizontal Status Bar", statusHor));
        windows.add(windowsMenuItem("Movement Panel", movement));
        windows.add(windowsMenuItem("Hands Panel", hands));
        windows.add(windowsMenuItem("Timers Panel", timers));
        windows.add(windowsMenuItem("Effects Panel", effects));
        windows.add(windowsMenuItem("Info Panel", info));
        windows.add(windowsMenuItem("Harden Armour Panel", harden));
        windows.addSeparator();
        windows.add(windowsMenuItem("Command Line", command));

		//Menu for removing title bars from windows in groups
        JMenu titleBars = new JMenu("Remove Title Bars");

        //Item for the most common windows
        mi = new JMenuItem("Comm & Status Bars");
        mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				command.removeBar();
				status.removeBar();
				hands.removeBar();
				timers.removeBar();
				movement.removeBar();
				effects.removeBar();
				statusHor.removeBar();
				info.removeBar();
				harden.removeBar();
			}
		});
		titleBars.add(mi);

		//Item to remove the bars from all windows
        mi = new JMenuItem("All Title Bars");
        mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chats.removeBar();
				game.removeBar();
				guildshouts.removeBar();
				help.removeBar();
				shouts.removeBar();
				tells.removeBar();
				templeshouts.removeBar();
				command.removeBar();
				status.removeBar();
				movement.removeBar();
				hhshouts.removeBar();
				hhshouts.removeBar();
				hhshouts.removeBar();
				hands.removeBar();
				timers.removeBar();
				effects.removeBar();
				logons.removeBar();
				statusHor.removeBar();
				events.removeBar();
				info.removeBar();
				harden.removeBar();
			}
		});
		titleBars.add(mi);

		//Menu for restoring title bars to windows in groups
        JMenu titleBars2 = new JMenu("Restore Title Bars");

        //Restore the most commonly removed bars
        mi = new JMenuItem("Comm & Status Bars");
        mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				command.restoreBar();
				status.restoreBar();
				hands.restoreBar();
				timers.restoreBar();
				movement.restoreBar();
				effects.restoreBar();
				statusHor.restoreBar();
				info.restoreBar();
				harden.restoreBar();
			}
		});
		titleBars2.add(mi);

		//restore all bars
        mi = new JMenuItem("All Title Bars");
        mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chats.restoreBar();
				game.restoreBar();
				guildshouts.restoreBar();
				help.restoreBar();
				shouts.restoreBar();
				tells.restoreBar();
				templeshouts.restoreBar();
				command.restoreBar();
				status.restoreBar();
				movement.restoreBar();
				hhshouts.restoreBar();
				hhshouts.restoreBar();
				hhshouts.restoreBar();
				hands.restoreBar();
				timers.restoreBar();
				effects.restoreBar();
				logons.restoreBar();
				statusHor.restoreBar();
				events.restoreBar();
				info.restoreBar();
				harden.restoreBar();
			}
		});
		titleBars2.add(mi);


		//The display menu, used for keeping track of windows and themes
		JMenu display = new JMenu("Display Options");
		display.setMnemonic(KeyEvent.VK_D);
		display.add(windows);
		display.addSeparator();
		display.add(titleBars);
		display.add(titleBars2);
		display.addSeparator();
		display.add(skinsMenu());


		//created configuration menu
		JMenu config = new JMenu("Configuration");
		config.setMnemonic(KeyEvent.VK_C);

		//Creates an AliasEditor for graphical editing of aliases
		mi = new JMenuItem("Alias Options");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AliasEditor alias = new AliasEditor();
			}
		});
		config.add(mi);

		//Creates a SquelchEditor for graphica editing of squelches
		mi = new JMenuItem("Client Side Squelches");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SquelchEditor squelch = new SquelchEditor();
			}
		});
		config.add(mi);

		//Creates a ColorEditor for graphical editing of colors
		mi = new JMenuItem("Color Options");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ColorEditor co = new ColorEditor();
			}
		});
		config.add(mi);

		//Creates a ConfigEditor for graphical editing of the configuration
		mi = new JMenuItem("Config Options");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConfigEditor c = new ConfigEditor();
			}
		});
		config.add(mi);

		//Creates a HighlightEditor for graphical editing of the highlight strings
		mi = new JMenuItem("Highlight Strings");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HighlightEditor h = new HighlightEditor();
			}
		});
		config.add(mi);

		//Creates a MacroEditor for graphical editing of macros
		mi = new JMenuItem("Macro Options");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MacroEditor macro = new MacroEditor();
			}
		});
		config.add(mi);

		//Creates a NicknameEditor for graphical editing of nicknames
		mi = new JMenuItem("Nickname Options");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NicknamesEditor nicks = new NicknamesEditor();
			}
		});
		config.add(mi);

		//Creates a FilterEditor for graphical editing of text filters
		mi = new JMenuItem("Text Filtering Options");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FilterEditor filter = new FilterEditor();
			}
		});
		config.add(mi);

		//Creates a TriggerEditor for graphical editing of triggers
		mi = new JMenuItem("Trigger Options");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TriggerEditor trig = new TriggerEditor();
			}
		});
		config.add(mi);

		config.addSeparator();

		//The profiles menu, used to create a new profile or load
		//an existing one
		JMenu profile = new JMenu ("Profiles");

		mi = new JMenuItem("Create New Profile");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newProfile();
			}
		});
		profile.add(mi);

		mi = new JMenuItem("Copy Profile to New Profile");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyProfile();
			}
		});
		profile.add(mi);

		//gets a list of all available profiles and adds them as options to the menu
		JMenu listProfiles = listProfiles();
		profile.add(listProfiles);
		config.add(profile);

		//Menu for editing/running scripts
		JMenu scriptMenu = new JMenu("Scripts");
		scriptMenu.setMnemonic(KeyEvent.VK_S);

		mi = new JMenuItem("Script Editor");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ScriptEditingThread("Script Editor").start();
			}
		});
		scriptMenu.add(mi);

		mi = new JMenuItem("Run Script");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(KilCliThread.getKilCli(), "Enter the name of the script to run:");
			    if ((name != null) && (name.length() > 0)) {
			    	new ScriptExecuteThread(name, new Script()).start();
				}
			}
		});
		scriptMenu.add(mi);

		//The help menu with many web browser links
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		helpMenu.add(webpageMenuItem("KilCli Homepage", "http://www.kilcli.com"));
		helpMenu.add(webpageMenuItem("KilCli Support Board", "http://forums.kilcli.com"));
		helpMenu.addSeparator();
		helpMenu.add(webpageMenuItem("Legends of Terris Homepage", "http://www.legendsofterris.com"));
		helpMenu.add(webpageMenuItem("Terris Message Board", "http://boards.legendsofterris.com"));
		helpMenu.addSeparator();
		helpMenu.add(webpageMenuItem("Legends of Cosrin Homepage", "http://www.legendsofcosrin.com"));
		helpMenu.add(webpageMenuItem("Cosrin Message Board", "http://boards.legendsofcosrin.com"));
		helpMenu.addSeparator();
		helpMenu.add(webpageMenuItem("Wolfenburg Homepage", "http://www.wolfenburg.com"));
		helpMenu.add(webpageMenuItem("Wolfenburg Message Board", "http://boards.wolfenburg.com"));
		helpMenu.addSeparator();

		mi = new JMenuItem("License Agreement");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				license();
			}
		});
		helpMenu.add(mi);
		helpMenu.addSeparator();

		mi = new JMenuItem("About");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				about();
			}
		});
		helpMenu.add(mi);


        //add file and purge menus to menu bar
        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(display);
        menuBar.add(config);
        menuBar.add(scriptMenu);
        menuBar.add(helpMenu);

        return menuBar;
	}

	/**
	 * Creates a new menu item that opens the specified URL
	 *
	 * @param name - the name of the menu item
	 * @param url - the URL to open
	 * @return JMenuItem - the menu item with the given name, that opens the specified URL
	 */
	private JMenuItem webpageMenuItem(String name, String url) {
		JMenuItem tmp = new JMenuItem(name);
		final String finalURL = url;
		tmp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					BrowserLauncher.openURL(finalURL);
				} catch (Exception e5) {}
			}
		});
		return tmp;
	}

	/**
	 * Method to create the Display-> Windows Menu
	 *
	 * @param name - the name of the window
	 * @param frame - the internal frame object of that window
	 * @return JMenuItem - the object that is the Windows menu
	 */

	private JMenuItem windowsMenuItem(String name, JInternalFrame frame) {
    	JMenuItem tmp;
    	final String n = name;
    	//create a frame menu item item
        tmp = new JMenuItem(name);
		//set a listener to know when the internal frame has been selected
		tmp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkWindow(n);
			}
		});
		return tmp;
	}

	/**
	 * Method used by the Windows Menu to open/close internal windows
	 *
	 * @param name - the name of the window that needs to be closed/opened
	 */

	private void checkWindow(String name) {
		JInternalFrame f = null;
		if (name.equals("Game Window")) {
			f = game;
		} else if (name.equals("Shouts Window")) {
			f = shouts;
		} else if (name.equals("Guild Shouts Window")) {
			f = guildshouts;
		} else if (name.equals("Temple Shouts Window")) {
			f = templeshouts;
		} else if (name.equals("HH Shouts Window")) {
			f = hhshouts;
		} else if (name.equals("Wails Window")) {
			f = wails;
		} else if (name.equals("Tells Window")) {
			f = tells;
		} else if (name.equals("Chats Window")) {
			f = chats;
		} else if (name.equals("Events Window")) {
			f = events;
		} else if (name.equals("Logons Window")) {
			f = logons;
		} else if (name.equals("Help Window")) {
			f = help;
		} else if (name.equals("Status Bar")) {
			f = status;
		} else if (name.equals("Horizontal Status Bar")) {
			f = statusHor;
		} else if (name.equals("Movement Panel")) {
			f = movement;
		} else if (name.equals("Hands Panel")) {
			f = hands;
		} else if (name.equals("Timers Panel")) {
			f = timers;
		} else if (name.equals("Info Panel")) {
			f = info;
		} else if (name.equals("Effects Panel")) {
			f = effects;
		} else if (name.equals("Command Line")) {
			f = command;
		} else if (name.equals("Harden Armour Panel")) {
			f = harden;
		}
		if (f != null) {
			//if frame is closed
			if (f.isClosed()) {
				try {
					//set frame closed flag to false
					f.setClosed(false);
				} catch (Exception ee) {}
				//set frame visible
				f.setVisible(true);
				//add frame to desktop
				desktop.add(f);
				f.moveToFront();
			} else {
				//otherwise, close window
				try {
					f.setClosed(true);
				} catch (Exception ee) {}
			}
		}
		f = null;
	}


	/**
	 * Creates the Display->Skins Menu
	 *
	 * @return JMenu - the JMenu that will function as the skins menu
	 */

	private JMenu skinsMenu() {
		JMenuItem mi;
		JRadioButtonMenuItem mi2;
		JMenu skins = new JMenu("Skins");
		themesMenu = (JMenu) skins.add(new JMenu("Metal L&F"));
		kunststoffMenu = (JMenu) skins.add(new JMenu("Kunststoff"));
		plastic3DMenu = (JMenu) skins.add(new JMenu("Plastic 3D"));
		plasticXPMenu = (JMenu) skins.add(new JMenu("Plastic XP"));
		plasticMenu = (JMenu) skins.add(new JMenu("Plastic"));

		// *** now back to adding color/font themes to the theme menu
		mi = createThemesMenuItem(themesMenu, "Default", new PlasticTheme(), "Default", "javax.swing.plaf.metal.MetalLookAndFeel");
		if ((currentLookAndFeel.equals("javax.swing.plaf.metal.MetalLookAndFeel")) && (options[20].toLowerCase().equals("default"))) {
			mi.setSelected(true);
		}

		mi = createThemesMenuItem(themesMenu, "High Contrast", new ContrastTheme(), "Contrast", "javax.swing.plaf.metal.MetalLookAndFeel");
		if ((currentLookAndFeel.equals("javax.swing.plaf.metal.MetalLookAndFeel")) && (options[20].toLowerCase().equals("contrast"))) {
			mi.setSelected(true);
		}

		mi = createThemesMenuItem(kunststoffMenu, "Default", new PlasticTheme(), "Default", "com.incors.plaf.kunststoff.KunststoffLookAndFeel");
		if ((currentLookAndFeel.equals("com.incors.plaf.kunststoff.KunststoffLookAndFeel")) && (options[20].toLowerCase().equals("default"))) {
			mi.setSelected(true);
		}

		mi = createThemesMenuItem(kunststoffMenu, "High Contrast", new ContrastTheme(), "Contrast", "com.incors.plaf.kunststoff.KunststoffLookAndFeel");
		if ((currentLookAndFeel.equals("com.incors.plaf.kunststoff.KunststoffLookAndFeel")) && (options[20].toLowerCase().equals("contrast"))) {
			mi.setSelected(true);
		}

		JMenu skinMenu = new JMenu("Skin L&F");

		//run through the files in the themes folder
		//and add necessary information to the skins menus
		File srcFile = new File("themes");
		String[] files = srcFile.list();
		for (int i = 0; i < files.length; i++) {

			//if it is a themepack, add it to the skin l&f menu
			File file = new File("themes" + System.getProperty("file.separator") + files[i]);
			if (file.toString().endsWith(".zip")) {
				mi2 = new JRadioButtonMenuItem(files[i].toString().substring(0, files[i].toString().indexOf("theme")));
				if ((currentLookAndFeel.equals("com.l2fprod.gui.plaf.skin.SkinLookAndFeel")) && (options[20].toLowerCase().equals(file.toString()))) {
					mi2.setSelected(true);
				}
				mi2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int tmp = e.toString().lastIndexOf('=') + 1;
						String tmpString = e.toString().substring(tmp, (e.toString().length() - 1));
						tmpString = "themes" + System.getProperty("file.separator") + tmpString + "themepack.zip";
						setSkin(tmpString);
					}
				});
				skinsGroup.add(mi2);
				skinMenu.add(mi2);

			//otherwise, we want to add it to Metal, Kunststoff, and Plastic *
			} else if ((file.toString().endsWith(".thm")) || (file.toString().endsWith(".theme"))) {
				try {
					InputStream in = new FileInputStream(file);
					mi = createThemesMenuItem(themesMenu, file.toString(), new CustomTheme(in), file.toString(), "javax.swing.plaf.metal.MetalLookAndFeel");
					if ((currentLookAndFeel.equals("javax.swing.plaf.metal.MetalLookAndFeel")) && (options[20].toLowerCase().equals(file.toString()))) {
						mi.setSelected(true);
					}
					in.close();
					in = new FileInputStream(file);
					mi = createThemesMenuItem(kunststoffMenu, file.toString(), new CustomTheme(in), file.toString(), "com.incors.plaf.kunststoff.KunststoffLookAndFeel");
					if ((currentLookAndFeel.equals("com.incors.plaf.kunststoff.KunststoffLookAndFeel")) && (options[20].toLowerCase().equals(file.toString()))) {
						mi.setSelected(true);
					}
					in.close();
					in = new FileInputStream(file);
					mi = createThemesMenuItem(plasticXPMenu, file.toString(), new CustomTheme(in), file.toString(), "com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
					if ((currentLookAndFeel.equals("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel")) && (options[20].toLowerCase().equals(file.toString()))) {
						mi.setSelected(true);
					}
					in.close();
					in = new FileInputStream(file);
					mi = createThemesMenuItem(plastic3DMenu, file.toString(), new CustomTheme(in), file.toString(), "com.jgoodies.plaf.plastic.Plastic3DLookAndFeel");
					if ((currentLookAndFeel.equals("com.jgoodies.plaf.plastic.Plastic3DLookAndFeel")) && (options[20].toLowerCase().equals(file.toString()))) {
						mi.setSelected(true);
					}
					in.close();
					in = new FileInputStream(file);
					mi = createThemesMenuItem(plasticMenu, file.toString(), new CustomTheme(in), file.toString(), "com.jgoodies.plaf.plastic.PlasticLookAndFeel");
					if ((currentLookAndFeel.equals("com.jgoodies.plaf.plastic.PlasticLookAndFeel")) && (options[20].toLowerCase().equals(file.toString()))) {
						mi.setSelected(true);
					}
					in.close();

				} catch (Exception e) {
					System.err.println("couldn't find theme file: " + e);
					//SMTPClient.sendError(name, e);
				}
			}

		}
		skins.add(skinMenu);

		mi = createThemesMenuItem(skins, "GTK L&F", new PlasticTheme(), "gtk", gtk);
		mi = createThemesMenuItem(skins, "Macintosh L&F", new PlasticTheme(), "mac", mac);
		mi = createThemesMenuItem(skins, "Windows L&F", new PlasticTheme(), "windows", windows);

		return skins;
	}

    /**
     * Creates a JRadioButtonMenuItem for the Themes menu
     */
    private JMenuItem createThemesMenuItem(JMenu menu, String label, PlasticTheme theme, String name, String plaf) {
        JRadioButtonMenuItem mi = (JRadioButtonMenuItem) menu.add(new JRadioButtonMenuItem(label));
		themesMenuGroup.add(mi);
		//this.theme = theme;
		mi.addActionListener(new ChangeThemeAction(this, theme, name, plaf));
		if ((plaf.equals(mac)) || (plaf.equals(gtk)) || (plaf.equals(windows))) {
			mi.setEnabled(isAvailableLookAndFeel(plaf));
		}
		return mi;
    }

	/**
	 * Method called when setting the L&F right after starting program
	 */

	private void setOriginalLookAndFeel() {
		updateLookAndFeel(currentLookAndFeel);
		if (currentLookAndFeel.equals("com.l2fprod.gui.plaf.skin.SkinLookAndFeel")) {
			setSkin(options[20]);
		}
		checkTheme(currentLookAndFeel, theme);
	}


	/**
	 * If a skin l&f has been selected, this method updates the display for it
	 *
	 * @param s - the name of the skin l&f skin to be applied.
	 */

	private void setSkin(String s) {
		try {
			Skin skin = null;
			//save the skin as the current one
			options[20] = s;
			//set the skin
			SkinLookAndFeel.setSkin(SkinLookAndFeel.loadThemePack(s));

			//make sure to update interior windows
			checkTheme("com.l2fprod.gui.plaf.skin.SkinLookAndFeel", theme);
			checkTitleBars();
		} catch (Exception e) {
			System.err.println("error: " + e);
			//SMTPClient.sendError(name, e);
		}
	}

	/**
	 * Method to get the name of a new profile,<br>
	 * create the needed folders/file,<br>
	 * and switch to that profile
	 */

	private void newProfile() {
		JOptionPane option = new JOptionPane();
		String p = option.showInputDialog(this, "What is the name of the new profile?", "Create New Profile", 3);
		if ((p == null) || (p.length() < 1)) {
			return;
		}
		File srcFile = new File("config" + System.getProperty("file.separator") + p);
		if ((srcFile.exists()) && (srcFile.isDirectory())) {
			option.showMessageDialog(this, "That profile name already exists, try a new one");
			newProfile();
		} else {
			srcFile.mkdir();
			srcFile = new File("config" + System.getProperty("file.separator") + p + System.getProperty("file.separator") + "playlists");
			srcFile.mkdir();
			try {
				srcFile = new File("config" + System.getProperty("file.separator") + p + System.getProperty("file.separator") + "alias.txt");
				srcFile.createNewFile();
				srcFile = new File("config" + System.getProperty("file.separator") + p + System.getProperty("file.separator") + "triggers.txt");
				srcFile.createNewFile();
				srcFile = new File("config" + System.getProperty("file.separator") + p + System.getProperty("file.separator") + "macros.txt");
				srcFile.createNewFile();
				srcFile = new File("config" + System.getProperty("file.separator") + p + System.getProperty("file.separator") + "config.txt");
				srcFile.createNewFile();
				srcFile = new File("config" + System.getProperty("file.separator") + p + System.getProperty("file.separator") + "colors.txt");
				srcFile.createNewFile();
				srcFile = new File("config" + System.getProperty("file.separator") + p + System.getProperty("file.separator") + "nicknames.txt");
				srcFile.createNewFile();
				srcFile = new File("config" + System.getProperty("file.separator") + p + System.getProperty("file.separator") + "highlights.txt");
				srcFile.createNewFile();
				srcFile = new File("Config" + System.getProperty("file.separator") + p + System.getProperty("file.separator") + "filters.txt");
				srcFile.createNewFile();
				ConfigWriter.setProfile(p);
				ConfigWriter.configWrite(options);
				ConfigWriter.colorsWrite(colors);
				this.setJMenuBar(createBar());
				this.setVisible(false);
				this.setVisible(true);
			} catch (Exception e) {}
			setProfile(p);
			option = null;
			p = null;
		}
	}

	private void copyProfile() {
		JOptionPane option = new JOptionPane();
		File srcFile = new File("config");
		String[] files = srcFile.list();
		ArrayList tmpFiles = new ArrayList();
		for (int i = 0; i < files.length; i++) {
			File file = new File("config" + System.getProperty("file.separator") + files[i]);
			if (file.isDirectory()) {
				tmpFiles.add(files[i]);
			}
		}
		Object tmpFrom = option.showInputDialog(this, "What profile do you want to copy?", "Copy Profile", JOptionPane.QUESTION_MESSAGE, null, tmpFiles.toArray(), "default");
		if (tmpFrom == null) {
			return;
		}
		String copyFrom = tmpFrom.toString();
		File source = new File("config" + System.getProperty("file.separator") + copyFrom);
		String p = option.showInputDialog(this, "What is hte name of the new Profile?", "Copy Profile", 3);
		if ((p == null) || (p.length() < 1)) {
			return;
		}
		File destination = new File("config" + System.getProperty("file.separator") + p);
		destination.mkdir();
		try {
			copyFile(source, destination, true);
			setProfile(p);
		} catch (Exception e) {
			e.printStackTrace();
			gameWrite("Error while copying profile");
		}
	}

    private static void copyFile(File src, File dest, boolean forceCopy) throws IOException {
        if(src.isDirectory()) {
            if(!dest.isDirectory()) {
                throw new IOException("copyFile: Cannot copy directory onto single file");
            }
            File files[] = src.listFiles();
            for(int i = 0; i < files.length; i++) {
                copyFile(files[i], dest, forceCopy);
            }

        } else {
            if(dest.isDirectory()) {
                dest = new File(dest, src.getName());
            }
            if(!forceCopy && dest.exists()) {
                return;
            }
            if(dest.exists() && !dest.canWrite()) {
                return;
            }
            long lastModified = src.lastModified();
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(src));
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dest));
            try {
                byte buffer[] = new byte[0x10000];
                do {
                    int bytesRead = in.read(buffer);
                    if(bytesRead == -1) {
                        break;
                    }
                    out.write(buffer, 0, bytesRead);
                } while(true);
            }
            finally {
                in.close();
                out.close();
                dest.setLastModified(lastModified);
            }
        }
    }

    private static void copyFile(String name, File dest, boolean forceCopy) throws IOException {
        copyFile(new File(name), dest, forceCopy);
    }

    private static void copyFile(String name, File dest) throws IOException {
        copyFile(name, dest, true);
    }

    private static void copyFile(File src, File dest) throws IOException {
        copyFile(src, dest, true);
    }
	/**
	 * Method to list all the available profiles,<br>
	 * creating a menu item for them
	 *
	 * @return JMenuItem containing a possible
	 */
	private JMenu listProfiles() {
		JMenu profiles = new JMenu("Profiles");
		File srcFile = new File("config");
		String[] files = srcFile.list();
		for (int i = 0; i < files.length; i++) {
			File file = new File("config" + System.getProperty("file.separator") + files[i]);
			if (file.isDirectory()) {
				JMenuItem mi = new JMenuItem(files[i]);
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int tmp = e.toString().lastIndexOf('=') + 1;
						String tmpString = e.toString().substring(tmp, (e.toString().length() - 1));
						setProfile(tmpString);
						new JOptionPane().showMessageDialog(null, "Profile successfully changed to: " + tmpString, "Confirmation!", 1);
					}
				});
				profiles.add(mi);
			}
		}
		return profiles;
	}

	/**
	 * Method to change what the current profile is
	 *
	 * @param p - a string containing the name of the new profile
	 */

	private void setProfile(String p) {
		if (profile.equals(p)) {
			return;
		}
		profile = p;
		//update the profile in the writers
		AliasWriter.setProfile(profile);
		ConfigWriter.setProfile(profile);
		MacroWriter.setProfile(profile);
		TriggerWriter.setProfile(profile);
		HighlightWriter.setProfile(profile);
		FilterWriter.setProfile(profile);
		SquelchWriter.setProfile(profile);
		if ((game != null) && (getWidth() != 0)) {
        	WindowConfigWriter.getSettings(getX(), getY(), getWidth(), getHeight(), game, movement, status, guildshouts, templeshouts, tells, chats, shouts, command, hhshouts, events, wails, hands, timers, effects, logons, statusHor, info, harden);
		}
		WindowConfigWriter.setProfile(profile);
		NicknamesWriter.setProfile(profile);
		//reload aliases, macros, nicknames, and config
		updateAliases();
		updateConfig();
		updateNicknames();
		updateMacros();
		try {
			//load the triggers
			Trigger.load(profile);

			//load the highlight strings
			HighlightStrings.load(profile);

			//load the squelches
			Squelch.load(profile);

			if (game == null) {
				//load the window settings
				settings = WindowConfigLoader.load(profile);
			} else {
				settings = WindowConfigLoader.load(profile);
				updateWindowPositions();
			}

		} catch (Exception e) {
			System.err.println("Set Profile: " + e);
			//SMTPClient.sendError(name, e);
		}

		this.setTitle(VERSION + " : Profile = " + profile);
	}


	/**
	 * Method to update window positions and settings
	 * after changing profiles
	 */

	private void updateWindowPositions() {
		game.updatePosition(Integer.parseInt(settings[1]), Integer.parseInt(settings[2]), Integer.parseInt(settings[3]), Integer.parseInt(settings[4]), settings[0], settings[65]);
		movement.updatePosition(Integer.parseInt(settings[6]), Integer.parseInt(settings[7]), Integer.parseInt(settings[8]), Integer.parseInt(settings[9]), settings[5], settings[66]);
		status.updatePosition(Integer.parseInt(settings[11]), Integer.parseInt(settings[12]), Integer.parseInt(settings[13]), Integer.parseInt(settings[14]), settings[10], settings[67]);
		guildshouts.updatePosition(Integer.parseInt(settings[16]), Integer.parseInt(settings[17]), Integer.parseInt(settings[18]), Integer.parseInt(settings[19]), settings[15], settings[68]);
		templeshouts.updatePosition(Integer.parseInt(settings[21]), Integer.parseInt(settings[22]), Integer.parseInt(settings[23]), Integer.parseInt(settings[24]), settings[20], settings[69]);
		tells.updatePosition(Integer.parseInt(settings[26]), Integer.parseInt(settings[27]), Integer.parseInt(settings[28]), Integer.parseInt(settings[29]), settings[25], settings[70]);
		chats.updatePosition(Integer.parseInt(settings[31]), Integer.parseInt(settings[32]), Integer.parseInt(settings[33]), Integer.parseInt(settings[34]), settings[30], settings[71]);
		shouts.updatePosition(Integer.parseInt(settings[36]), Integer.parseInt(settings[37]), Integer.parseInt(settings[38]), Integer.parseInt(settings[39]), settings[35], settings[72]);
		command.updatePosition(Integer.parseInt(settings[41]), Integer.parseInt(settings[42]), Integer.parseInt(settings[43]), Integer.parseInt(settings[44]), settings[40], settings[73]);
		hhshouts.updatePosition(Integer.parseInt(settings[51]), Integer.parseInt(settings[52]), Integer.parseInt(settings[53]), Integer.parseInt(settings[54]), settings[50], settings[74]);
		events.updatePosition(Integer.parseInt(settings[56]), Integer.parseInt(settings[57]), Integer.parseInt(settings[58]), Integer.parseInt(settings[59]), settings[55], settings[75]);
		wails.updatePosition(Integer.parseInt(settings[61]), Integer.parseInt(settings[62]), Integer.parseInt(settings[63]), Integer.parseInt(settings[64]), settings[60], settings[76]);
		hands.updatePosition(Integer.parseInt(settings[78]), Integer.parseInt(settings[79]), Integer.parseInt(settings[80]), Integer.parseInt(settings[81]), settings[77], settings[82]);
		timers.updatePosition(Integer.parseInt(settings[84]), Integer.parseInt(settings[85]), Integer.parseInt(settings[86]), Integer.parseInt(settings[87]), settings[83], settings[88]);
		effects.updatePosition(Integer.parseInt(settings[90]), Integer.parseInt(settings[91]), Integer.parseInt(settings[92]), Integer.parseInt(settings[93]), settings[89], settings[94]);
		logons.updatePosition(Integer.parseInt(settings[96]), Integer.parseInt(settings[97]), Integer.parseInt(settings[98]), Integer.parseInt(settings[99]), settings[95], settings[106]);
		info.updatePosition(Integer.parseInt(settings[108]), Integer.parseInt(settings[109]), Integer.parseInt(settings[110]), Integer.parseInt(settings[111]), settings[107], settings[112]);
		harden.updatePosition(Integer.parseInt(settings[114]), Integer.parseInt(settings[115]), Integer.parseInt(settings[116]), Integer.parseInt(settings[117]), settings[113], settings[118]);
		statusHor.updatePosition(Integer.parseInt(settings[101]), Integer.parseInt(settings[102]), Integer.parseInt(settings[103]), Integer.parseInt(settings[104]), settings[100], settings[105]);
		hands.updatePosition(Integer.parseInt(settings[78]), Integer.parseInt(settings[79]), Integer.parseInt(settings[80]), Integer.parseInt(settings[81]), settings[77], settings[82]);
		timers.updatePosition(Integer.parseInt(settings[84]), Integer.parseInt(settings[85]), Integer.parseInt(settings[86]), Integer.parseInt(settings[87]), settings[83], settings[88]);
		effects.updatePosition(Integer.parseInt(settings[90]), Integer.parseInt(settings[91]), Integer.parseInt(settings[92]), Integer.parseInt(settings[93]), settings[89], settings[94]);
		movement.updatePosition(Integer.parseInt(settings[6]), Integer.parseInt(settings[7]), Integer.parseInt(settings[8]), Integer.parseInt(settings[9]), settings[5], settings[66]);
		status.updatePosition(Integer.parseInt(settings[11]), Integer.parseInt(settings[12]), Integer.parseInt(settings[13]), Integer.parseInt(settings[14]), settings[10], settings[67]);

	}

	/**
	 * Method to display the license agreement in the help window
	 */

	private void license() {
		if (help.isClosed()) {
			try {
				//set helpwindow closed flag to false
				help.setClosed(false);
			} catch (Exception ee) {}
			//set help window visible
			help.setVisible(true);
			//add help to desktop
			desktop.add(help);
			help.moveToFront();
		} else {
			help.setVisible(true);
		}
		help.viewLicense();
	}

	/**
	 * Method to display the Help->About Window
	 */

	private void about() {
		JOptionPane confirm = new JOptionPane();
		String tmp = "<html><b><font size=+2>" + VERSION + "</font></b><br><hr>" +
			"<p><b>KilCli</b></p>" +
			"<p><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </b><font SIZE=\"3\">Copyright © " +
			"2002 - 2004 Jason Baumeister<br>" +
			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;All Rights Reserved.</font></p>" +
			"<b>" +
			"<p>Terris, Cosrin, Wolfenburg</p>" +
			"</b><font SIZE=\"3\">" +
			"<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Copyright © 1995-2002 " +
			"Doug Goldner and Paul Barnett<br>" +
			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;All Rights Reserved.</p>" +
			"</font>" +
			"<p><b>Credits</b></p>" +
			"<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font size=\"3\">Tritonus, used under " +
			"LGPL - www.tritonus.org</font></p>" +
			"<p><font size=\"3\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;JavaLayerME, used " +
			"under LGPL - " +
			"www.javazoom.net/javalayer/javalayerme.html</font></p>" +
			"<p><font size=\"3\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;JOrbis, used under " +
			"LGPL - " +
			"www.jcraft.com/jorbis/index.html</font></p>" +
			"<p><font size=\"3\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Skin Look And Feel, " +
			"used under LGPL - www.l2fprod.com</font></p>" +
			"<p><font size=\"3\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Kunststoff Look And " +
			"Feel, used under LGPL - www.incors.org</font></p>" +
			"<p><font size=\"3\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;jEdit Syntax Highlighting " +
			", Copyright 1998-1999 <br>" +
			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Slava Pestov, Artur Biesiadowski, Clancy Malcolm, Jonathan Revusky, <br>" +
			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Juha Lindfors and Mike Dillon. - http://syntax.jedit.org/</font></p>" +
			"<p><font size=\"3\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;JGoodies Looks 1.1" +
			", used under BSD license - " +
			"www.jgoodies.com</font></p>" +
			"<p><font size=\"3\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Some portions Copyright - Sun Microsystems " +
			", used with permission - " +
			"www.sun.com</font></p>" +
			"<p><font size=\"3\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Nexus, "+
			"Used under LGPL, Copyright 1997 - Hewlett Packard Company</font></p>" +
			"<hr><br>Licenses:<br>" +
			"LGPL - http://opensource.org/licenses/lgpl-license.php<br>" +
			"<hr><br>Visit www.KilCli.com for updates";
		confirm.showMessageDialog(this, tmp, "About KilCli", 1);
		tmp = null;
	}

	/**
	 * Creates the various windows, i.e Game, Shouts, command line, status bar, Chats, etc,<br>
	 * makes them visible, and adds them to the desktop
     */

    protected void createFrames() {

		String cssFolder = System.getProperty("user.dir") + System.getProperty("file.separator") + "css" + System.getProperty("file.separator");

		//creates the windows
        game = new GameWindow("Game Window", cssFolder + "gamewindow.css", Integer.parseInt(settings[1]), Integer.parseInt(settings[2]), Integer.parseInt(settings[3]), Integer.parseInt(settings[4]), options[47].equalsIgnoreCase("true"));
        movement = new MovementPanel(Integer.parseInt(settings[6]), Integer.parseInt(settings[7]), Integer.parseInt(settings[8]), Integer.parseInt(settings[9]), options[52].equalsIgnoreCase("true"));
        status = new StatusBar(Integer.parseInt(settings[11]), Integer.parseInt(settings[12]), Integer.parseInt(settings[13]), Integer.parseInt(settings[14]), options[55].equalsIgnoreCase("true"));
        guildshouts = new GameWindow("Guild Shouts Window", cssFolder + "guildshoutswindow.css", Integer.parseInt(settings[16]), Integer.parseInt(settings[17]), Integer.parseInt(settings[18]), Integer.parseInt(settings[19]), options[48].equalsIgnoreCase("true"));
        templeshouts = new GameWindow("Temple Shouts Window", cssFolder + "templeshoutswindow.css", Integer.parseInt(settings[21]), Integer.parseInt(settings[22]), Integer.parseInt(settings[23]), Integer.parseInt(settings[24]), options[59].equalsIgnoreCase("true"));
        tells = new GameWindow("Tells Window", cssFolder + "tellswindow.css", Integer.parseInt(settings[26]), Integer.parseInt(settings[27]), Integer.parseInt(settings[28]), Integer.parseInt(settings[29]), options[57].equalsIgnoreCase("true"));
        chats = new GameWindow("Chats Window", cssFolder + "chatswindow.css", Integer.parseInt(settings[31]), Integer.parseInt(settings[32]), Integer.parseInt(settings[33]), Integer.parseInt(settings[34]), options[44].equalsIgnoreCase("true"));
        shouts = new GameWindow("Shouts Window", cssFolder + "shoutswindow.css", Integer.parseInt(settings[36]), Integer.parseInt(settings[37]), Integer.parseInt(settings[38]), Integer.parseInt(settings[39]), options[53].equalsIgnoreCase("true"));
        command = new CommandLine(Integer.parseInt(settings[41]), Integer.parseInt(settings[42]), Integer.parseInt(settings[43]), Integer.parseInt(settings[44]), options[19].equalsIgnoreCase("true"));
        hhshouts = new GameWindow("HHShouts Window", cssFolder + "hhshoutswindow.css", Integer.parseInt(settings[51]), Integer.parseInt(settings[52]), Integer.parseInt(settings[53]), Integer.parseInt(settings[54]), options[50].equalsIgnoreCase("true"));
        events = new GameWindow("Events Window", cssFolder + "eventswindow.css", Integer.parseInt(settings[56]), Integer.parseInt(settings[57]), Integer.parseInt(settings[58]), Integer.parseInt(settings[59]), options[46].equalsIgnoreCase("true"));
        wails = new GameWindow("Wails Window", cssFolder + "wailswindow.css", Integer.parseInt(settings[61]), Integer.parseInt(settings[62]), Integer.parseInt(settings[63]), Integer.parseInt(settings[64]), options[60].equalsIgnoreCase("true"));
        hands = new HandsPanel(Integer.parseInt(settings[78]), Integer.parseInt(settings[79]), Integer.parseInt(settings[80]), Integer.parseInt(settings[81]), options[49].equalsIgnoreCase("true"));
        timers = new TimersPanel(Integer.parseInt(settings[84]), Integer.parseInt(settings[85]), Integer.parseInt(settings[86]), Integer.parseInt(settings[87]), options[58].equalsIgnoreCase("true"));
        effects = new EffectsPanel(Integer.parseInt(settings[90]), Integer.parseInt(settings[91]), Integer.parseInt(settings[92]), Integer.parseInt(settings[93]), options[45].equalsIgnoreCase("true"));
		logons = new GameWindow("Logons Window", cssFolder + "logonswindow.css", Integer.parseInt(settings[96]), Integer.parseInt(settings[97]), Integer.parseInt(settings[98]), Integer.parseInt(settings[99]), options[51].equalsIgnoreCase("true"));
        help = new HelpWindow();
        statusHor = new StatusBarHor(Integer.parseInt(settings[101]), Integer.parseInt(settings[102]), Integer.parseInt(settings[103]), Integer.parseInt(settings[104]), options[56].equalsIgnoreCase("true"));
		info = new InfoPanel(Integer.parseInt(settings[108]), Integer.parseInt(settings[109]), Integer.parseInt(settings[110]), Integer.parseInt(settings[111]), options[62].equalsIgnoreCase("true"));
		harden = new HardenPanel(Integer.parseInt(settings[114]), Integer.parseInt(settings[115]), Integer.parseInt(settings[116]), Integer.parseInt(settings[117]), options[80].equalsIgnoreCase("true"));

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

			//check if we have to remove the movement panel title bar
			if (settings[66].toLowerCase().equals("false")) {
				movement.removeBar();
			}

			//check the movement window setting
	        if (settings[5].toLowerCase().equals("true")) {
        		movement.setClosed(true);
			} else {
				movement.setVisible(true);
				desktop.add(movement);
			}

			//check if we have to remove the status bar title bar
			if (settings[67].toLowerCase().equals("false")) {
				status.removeBar();
			}

			//check the status window setting
	        if (settings[10].toLowerCase().equals("true")) {
        		status.setClosed(true);
			} else {
				status.setVisible(true);
				desktop.add(status);
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

			//check if we have to remove the Command Line title bar
			if (settings[73].toLowerCase().equals("false")) {
				command.removeBar();
			}

			//check the command line setting
	        if (settings[40].toLowerCase().equals("true")) {
        		command.setClosed(true);
			} else {
				command.setVisible(true);
				desktop.add(command);
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

			//check if we have to remove the Hands title bar
			if (settings[82].toLowerCase().equals("false")) {
				hands.removeBar();
			}

			//check the hands line setting
	        if (settings[77].toLowerCase().equals("true")) {
        		hands.setClosed(true);
			} else {
				hands.setVisible(true);
				desktop.add(hands);
			}

			//check if we have to remove the Timers title bar
			if (settings[88].toLowerCase().equals("false")) {
				timers.removeBar();
			}

			//check the timers line setting
	        if (settings[83].toLowerCase().equals("true")) {
        		timers.setClosed(true);
			} else {
				timers.setVisible(true);
				desktop.add(timers);
			}

			//check if we have to remove the Effects title bar
			if (settings[94].toLowerCase().equals("false")) {
				effects.removeBar();
			}

			//check the effects line setting
	        if (settings[89].toLowerCase().equals("true")) {
        		effects.setClosed(true);
			} else {
				effects.setVisible(true);
				desktop.add(effects);
			}

			//check the logons line setting
			if (settings[95].toLowerCase().equals("true")) {
				logons.setClosed(true);
			} else {
				logons.setVisible(true);
				desktop.add(logons);
			}

			//check the hor. status bar line setting
			if (settings[100].toLowerCase().equals("true")) {
				statusHor.setClosed(true);
			} else {
				statusHor.setVisible(true);
				desktop.add(statusHor);
			}

			//check if we have to remove the Logons title bar
			if (settings[106].toLowerCase().equals("false")) {
				logons.removeBar();
			}

			//check if we have to remove the StatusBarHor title bar
			if (settings[105].toLowerCase().equals("false")) {
				statusHor.removeBar();
			}

			//check if we have to remove the Wails title bar
			if (settings[112].toLowerCase().equals("false")) {
				info.removeBar();
			}

			//check the wails line setting
	        if (settings[107].toLowerCase().equals("true")) {
        		info.setClosed(true);
			} else {
				info.setVisible(true);
				desktop.add(info);
			}

			if (settings[113].toLowerCase().equals("true")) {
				harden.setClosed(true);
			} else {
				harden.setVisible(true);
				desktop.add(harden);
			}

			if (settings[118].toLowerCase().equals("false")) {
				harden.removeBar();
			}

			help.setClosed(true);

		} catch (Exception e) {}

        //adds action listener to the textfield, to catch enter keys
        //command.textfield.addActionListener(this);
        try {
			//sets the command line window to be selected
            command.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
    }

	/**
	 * Creates Windows, calls connect routine, calls run routine
	 */

	public void initialize() {
		//create windows
		try {
			createFrames();
		} catch (Exception e) {
			System.err.println("Initialize: " + e);
			e.printStackTrace();
			SMTPClient.sendError("initialize", e);
		}

		if (options[63].length() > 0) {
			new ScriptExecuteThread(options[63], new Script()).start();
		}
		checkStamps();
		//outputs an initial Welcome message
		gameWrite("Welcome to " + VERSION + "<br>");
		HelpWindow.setHelpAddress(options[26]);
		sendReceive = new SendReceive(gameNumber);
		sendReceive.setTrigger(options[21].equalsIgnoreCase("true"));
		ChatIO.setTrigger(options[21].equalsIgnoreCase("true"));
		sendReceive.setNetBufferSize(Integer.parseInt(options[43]));
		sendReceive.start();
		if (options[69].equalsIgnoreCase("true")) {
			sendReceive.setNetMelee(true);
		}
	}


	/**
	 * Method to reconnect to remote server
	 * disconnects if needed, then reconnects
	 */

	public void reconnect() {
		disconnect("quit", 1);
		KilCliThread.reconnect(this);
	}

	/**
	 * Sets the value of the loggedIn variable
	 *
	 * @param li - boolean value to be assigned to loggedIn
	 */

	public static void setLoggedIn(boolean li) {
		loggedIn = li;
		Runnable doSetProfile = null;
		if (loggedIn) {
			if (options[84].equalsIgnoreCase("true")) {
				chatIO = new ChatIO();
				chatIO.start();
			}
			File srcFile = new File("config");
			String[] files = srcFile.list();
			for (int i = 0; i < files.length; i++) {
				File file = new File("config" + System.getProperty("file.separator") + files[i]);
				if (file.isDirectory()) {
					if (file.getName().equals(getUsedCharacter())) {
 						doSetProfile = new Runnable() {
 						    public void run() {
         						KilCliThread.getKilCli().setProfile(getUsedCharacter());
								game.scrollToBottom();
								if (getLogonScript().length() > 0) {
									new ScriptExecuteThread(getLogonScript(), new Script()).start();
								}
								sendReceive.hardenReset();
     						}
 						};
						SwingUtilities.invokeLater(doSetProfile);
					}
				}
			}
			if (doSetProfile == null) {
				if (getLogonScript().length() > 0) {
						new ScriptExecuteThread(getLogonScript(), new Script()).start();
				}
				sendReceive.hardenReset();
			}
		}
	}

	/**
	 * Method to log text that has been sent to be
	 * displayed to the user
	 *
	 * @param tempInput - the string to be logged
	 */

	public static void log(String tempInput) {
		//if the user enabled logging
		if (logging) {
			//and if we are logged in & the control character didn't just tell us to skip this line
			if (loggedIn && !skipOutput) {
				//log the line
				log.log(tempInput);
			}
		}
		skipOutput = false;
	}

	/**
	 * Output line to the Guild Shouts Window (if open)
	 *
	 * @param input String that is attempting to be outputted
	 */

	public static void guildShoutsWrite(String input) {
		input = Squelch.analyze(input, false);
		input = encryptAnalyze(input);
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
	 * Output line to the Logons Window (if open)
	 *
	 * @param input String that is attempting to be outputted
	 */

	public static void logonsWrite(String input) {
		input = encryptAnalyze(input);
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
	 * Output line to the Shouts Window (if open)
	 *
	 * @param input String that is attempting to be outputted
	 */
	public static void shoutsWrite(String input) {
		input = Squelch.analyze(input, false);
		input = encryptAnalyze(input);
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

	public static void tellsWrite(String input) {
		input = Squelch.analyze(input, true);
		input = encryptAnalyze(input);
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

	private static String encryptAnalyze(String input) {
		if ((input.startsWith("^G")) && (input.indexOf("^N Tells you: '") > -1)) {
			int nameIndex = input.indexOf("^N");
			int endEncryptIndex = 0;
			int nameSearch = 0;
			if (input.indexOf("|e*|") > 0) {
				endEncryptIndex = input.indexOf("|*e|'");
				//entire message in this tell
				if (endEncryptIndex > 0) {
					input = input.substring(0, nameIndex+14) + "E* - '" + fromBase64(input.substring(nameIndex+19, endEncryptIndex)) + "'";
					input = characterTranslate(input);
				} else {
					//encrypted message overflow
					//get name, save it, get message, save it
					encryptedTells.add(input.substring(2, nameIndex));
					encryptedTells.add(input.substring(nameIndex+19, input.length() - 1));
					input = input.substring(0, nameIndex+15) + "&lt;part of encrypted message>'";
				}
			} else if ((nameIndex > 2) && (encryptedTells.indexOf(input.substring(2, nameIndex)) > -1)) {
				nameSearch = encryptedTells.indexOf(input.substring(2, nameIndex));
				endEncryptIndex = input.indexOf("|*e|'");
				if (endEncryptIndex > 0) {
					input = input.substring(0, nameIndex+14) + "E* - '" + fromBase64((String)(encryptedTells.get(nameSearch+1)) + input.substring(nameIndex+16, endEncryptIndex)) + "'";
					input = characterTranslate(input);
					//delete the stored info
					//the tell has ended
					encryptedTells.remove(nameSearch);
					encryptedTells.remove(nameSearch);
				} else {
					//if the tell didn't end, then it didn't match the protocol we created
					//so delete what is in the buffer and output an error message.
					gameWrite("Bad encrypted tell sequence!");
					encryptedTells.remove(nameSearch);
					encryptedTells.remove(nameSearch);
				}
			}
		}
		return input;
	}

    private static String fromBase64(String s) {
        return new String(decoder.decodeBuffer(s));
    }

	/**
	 * Output line to the Temple Shouts Window (if open)
	 *
	 * @param input String that is attempting to be outputted
	 */

	public static void templeShoutsWrite(String input) {
		input = Squelch.analyze(input, false);
		input = encryptAnalyze(input);
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

	public static void chatsWrite(String input) {
		input = Squelch.analyze(input, false);
		input = encryptAnalyze(input);
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

	public static void hhShoutsWrite(String input) {
		input = Squelch.analyze(input, false);
		input = encryptAnalyze(input);
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
	public static void wailsWrite(String input) {
		input = Squelch.analyze(input, false);
		input = encryptAnalyze(input);
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

	public static void questsWrite(String input) {
		input = encryptAnalyze(input);
		if (game.isClosed()) {
			//input lost, no windows open
		} else {
			game.Write(options[14] + input);
		}
	}

	/**
	 * Output line to the Events Window (if open)
	 *
	 * @param input String that is attempting to be outputted
	 */

	public static void eventsWrite(String input) {
		input = encryptAnalyze(input);
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

	public static void gameWrite(String input) {
		input = encryptAnalyze(input);
		//input = Squelch.analyze(input);
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

	public static String pickWindow(String input) {

		//get the second character of the line
		previewChar = input.charAt(1);
		previewChar = textRouting(previewChar);
		if (previewChar != input.charAt(1)) {
			StringBuffer buf = new StringBuffer(input);
			buf.setCharAt(1, previewChar);
			input = buf.toString();
		}

		if (previewChar < 72) {
			//get the second character of the line to pick proper window
			switch(previewChar) {

				//if its C, print to the Chats window
				case 'C':
					chatsWrite(input.substring(3, input.length()));
					skipOutput = true;
					sendReceive.setSkipOutput(skipOutput);
					break;

				//if its E, print to the events window
				case 'E':
					eventsWrite(input.substring(3, input.length()));
					skipOutput = true;
					sendReceive.setSkipOutput(skipOutput);
					break;

				//if its G, print to the GShouts window
				case 'G':
					guildShoutsWrite(input.substring(3, input.length()));
					skipOutput = true;
					sendReceive.setSkipOutput(skipOutput);
					break;

				default:
					break;
			}
		} else if (previewChar < 82) {

			switch(previewChar) {

				//if its H, print to the hhshouts window
				case 'H':
					hhShoutsWrite(input.substring(3, input.length()));
					skipOutput = true;
					sendReceive.setSkipOutput(skipOutput);
					break;

				//if its L, print to the logons window
				case 'L':
					logonsWrite(input.substring(3, input.length()));
					skipOutput = true;
					sendReceive.setSkipOutput(skipOutput);
					break;

				//if its M, print to the shouts window
				case 'M':
					shoutsWrite(input.substring(3, input.length()));
					skipOutput = true;
					sendReceive.setSkipOutput(skipOutput);
					break;

				//if its Q, print to the quests window
				case 'Q':
					questsWrite(input.substring(3, input.length()));
					skipOutput = true;
					sendReceive.setSkipOutput(skipOutput);
					break;

				default:
					break;
			}
		} else if (previewChar < 122) {

			switch(previewChar) {
				//if its T, print to the TShouts window
				case 'T':
					templeShoutsWrite(input.substring(3, input.length()));
					skipOutput = true;
					sendReceive.setSkipOutput(skipOutput);
					break;

				//if its W, print to the wails window
				case 'W':
					wailsWrite(input.substring(3, input.length()));
					skipOutput = true;
					sendReceive.setSkipOutput(skipOutput);
					break;

				//if its g, its the faked "print to game" option
				case 'g':
					input = input.substring(3, input.length());
					break;

				//if its t, print to the tells window
				case 't':
					tellsWrite(input.substring(3, input.length()));
					skipOutput = true;
					sendReceive.setSkipOutput(skipOutput);
					break;

				//otherwise do nothing
				default:
					break;
			}
		}
		return input;
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
		//kchat tells
		textRouting.put(new Integer('p'), options[85]);
		//kchat chats
		textRouting.put(new Integer('x'), options[86]);
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


	/**
	 * Handles Terris server control characters
	 *
	 * @param input The string from the server
	 * @return String The input string after going through any necessary translations
	 */

	public static String controlChar(String input) {
		if (input.length() < 2) {
			return input;
		}
		try {
			//get the second character of the line
			previewChar = input.charAt(1);

			if (previewChar < 77) {
				//test to see if we know what to do with the second character
				switch(previewChar) {

					//if its A, its and affect
					case 'A':
						try {
							char tmp = input.charAt(2);
							effects.addEffect(tmp);
						} catch (Exception stupidTongues) {}
						skipOutput = true;
						sendReceive.setSkipOutput(skipOutput);
						break;

					//if its D, do the direction thing
					case 'D':
						//update movement panel if window open
						if (!movement.isClosed()) {
							movement.UpdateDirs(input);
						}
						dirString = input;
						skipOutput = true;
						sendReceive.setSkipOutput(skipOutput);
						break;

					//if its E, don't output anything
					case 'E':
						skipOutput = true;
						sendReceive.setSkipOutput(skipOutput);
						break;

					//if its L, its the left hand
					case 'L':
						input = input.substring(2, input.length());

						if (checkHarden) {
							if (input.indexOf("Magiced") > -1) {
								if ((hardenedItem == 0) || (hardenedItem == 1)) {
									hardenedItem+=2;
								}
							} else {
								if ((hardenedItem == 2) || (hardenedItem == 3)) {
									hardenedItem-=2;
									if ((getLastCommand().toLowerCase().indexOf("wear") > -1) || (getLastCommand().toLowerCase().indexOf("don") > -1)) {
										checkItemForHarden(hands.getLeft());
									}
								}
							}
						}

						if (!hands.isClosed()) {
							hands.UpdateLeft(input);
						}

						skipOutput = true;
						sendReceive.setSkipOutput(skipOutput);
						break;

					default:
						break;
				}
			} else if (previewChar < 84) {

				switch(previewChar) {

					case 'P':
						skipOutput = true;
						sendReceive.setSkipOutput(skipOutput);
						break;

					//if its Q, do stuff
					case 'Q':
						input = input.substring(3, input.length());
						if (input.length() > 9) {
							goldString = input.substring(0, 9);
							st = new StringTokenizer(input.substring(9, input.length()));
							expString = st.nextToken();
							timer = Integer.parseInt(st.nextToken());
							if (timer > 0) {
								timer--;
								unbalanced.start();
							}
							if (!status.isClosed()) {
								status.UpdateBar(expString);
								statusHor.UpdateUnbal(timer);
							}

							if (!statusHor.isClosed()) {
								statusHor.UpdateBar(expString);
								statusHor.UpdateUnbal(timer);
							}

							if (!info.isClosed()) {
								info.updateGold(Integer.parseInt(goldString.trim()));
							}
						}

						sendReceive.addBlank();
						skipOutput = true;
						sendReceive.setSkipOutput(skipOutput);
						sendReceive.resetCheckAttack();
						break;

					//if its R, its the right hand
					case 'R':
						input = input.substring(2, input.length());
						if (checkHarden) {
							if (input.indexOf("Magiced") > -1) {
								if ((hardenedItem == 0) || (hardenedItem == 2)) {
									hardenedItem++;
								}
							} else {
								if ((hardenedItem == 1) || (hardenedItem == 3)) {
									hardenedItem--;
									if ((getLastCommand().toLowerCase().indexOf("wear") > -1) || (getLastCommand().toLowerCase().indexOf("don") > -1)) {
										checkItemForHarden(hands.getRight());
									}
								}
							}
						}
						if (!hands.isClosed()) {
							hands.UpdateRight(input);
						}
						skipOutput = true;
						sendReceive.setSkipOutput(skipOutput);
						break;

					//if its S, check the HPs and SPs to see if a warning is needed
					case 'S':
						//get Integers from the string
						st = new StringTokenizer(input.substring(3, input.length()));
						HPs = Integer.parseInt(st.nextToken());
						maxHPs = Integer.parseInt(st.nextToken());
						SPs = Integer.parseInt(st.nextToken());
						maxSPs = Integer.parseInt(st.nextToken());

						//create percentages of max HPs and SPs
						if (options[2].toLowerCase().equals("true")) {
							percentHP = java.lang.Math.round((float)((float)HPs / (float)maxHPs) * (float)100.0);
						}
						if (options[3].toLowerCase().equals("true")) {
							percentSP = java.lang.Math.round((float)((float)SPs / (float)maxSPs) * (float)100.0);
						}

						//test if we should give an HP or SP warning
						if (percentHP < Integer.parseInt(options[4])) {
							HPWarning = true;
						}

						if (percentSP < Integer.parseInt(options[5])) {
							SPWarning = true;
						}

						//Update stats in statusbar, if its open
						if (!status.isClosed()) {
							status.UpdateStats(HPs, maxHPs, SPs, maxSPs);
						}

						if (!statusHor.isClosed()) {
							statusHor.UpdateStats(HPs, maxHPs, SPs, maxSPs);
						}

						//print HP warning, if needed
						if (HPWarning) {
							gameWrite("^RWARNING!! ^GOnly ^Y" + percentHP + "% ^Nhit points left!");
						}
						//print SP warning, if needed
						if (SPWarning) {
							gameWrite("^RWARNING!! ^GOnly ^Y" + percentSP + "% ^Nspell points left!");
						}

						//don't output anything else
						skipOutput = true;
						sendReceive.setSkipOutput(skipOutput);

						//reset HP and SP warning flags
						HPWarning = false;
						SPWarning = false;
						break;

					default:
						break;
				}
			} else if (previewChar < 122) {

				switch(previewChar) {
					//if its W, its a staff member playing a stupid fucking sound
					case 'W':
						String file = input.substring(2, input.length() - 1);
						File sound = new File(options[39] + file + ".mp3");
						if (!sound.isFile()) {
							sound = new File(options[39] + file + ".wav");
							if (!sound.isFile()) {
								sound = new File(options[39] + file + ".ogg");
							}
						}
						if ((sounds) && (sound.isFile())) {
							new PlayThread(sound.getAbsolutePath(), null).start();
						}
						skipOutput = true;
						sendReceive.setSkipOutput(skipOutput);
						break;
					case 'e':
						skipOutput = true;
						sendReceive.setSkipOutput(skipOutput);
						break;

					//if its a, an effect wore off
					case 'a':
						try {
							char tmp = input.charAt(2);
							effects.removeEffect(tmp);
						} catch (Exception stupidTongues) {}
						skipOutput = true;
						sendReceive.setSkipOutput(skipOutput);
						break;


					//if its s, drop the first 2 characters and treat the rest of the line normally
					case 's':
						input = input.substring(2, input.length());
						break;

					case 'p':
						skipOutput = true;
						sendReceive.setSkipOutput(skipOutput);
						break;

					//otherwise do nothing
					default:
						//input = input;
						break;
				}
			}
		} catch (Exception e) {
			SMTPClient.sendError("controlChar", e);
		}

		return input;
	}

	public static void checkItemForHarden(String item) {
		if (item.startsWith("Slightly magiced")) {
			harden.incrementMagicCount(item.substring(17, item.length()), 0);
			return;
		} else if (item.startsWith("Strongly Magiced")) {
			harden.incrementMagicCount(item.substring(17, item.length()), 2);
			return;
		} else if (item.startsWith("Divinely Magiced")) {
			harden.incrementMagicCount(item.substring(17, item.length()), 3);
			return;
		} else if (item.startsWith("Magiced by the Immortals")) {
			harden.incrementMagicCount(item.substring(25, item.length()), 4);
			return;
		} else if (item.startsWith("Magiced")) {
			harden.incrementMagicCount(item.substring(8, item.length()), 1);
			return;
		}
	}

	/**
	 * Preps writes to display: \,/,[ properly
	 *
	 * @param input The input string to be translated
	 * @return String The input string after being translated
	 */

	public static String characterTranslate(String input) {
		StringBuffer temp = new StringBuffer(input);
		//check for "/" to display them properly (mega-wiz compatible)
		stringSearchIndex = temp.indexOf("&fs.");

		while (stringSearchIndex != -1) {
			temp.replace(stringSearchIndex, stringSearchIndex+4, "/");
			stringSearchIndex = temp.indexOf("&fs.", stringSearchIndex);
		}

		//reset search index
		stringSearchIndex = temp.indexOf("&bs.");

		//check for "\" to display them properly (mega-wiz compatible)
		while (stringSearchIndex != -1) {
			temp.replace(stringSearchIndex, stringSearchIndex+4, "\\");
			stringSearchIndex = temp.indexOf("&bs.", stringSearchIndex);
		}

		//reset search index
		stringSearchIndex = temp.indexOf("&lsb.");

		//check for "[" to display them properly (mega-wiz compatible)
		while (stringSearchIndex != -1) {
			temp.replace(stringSearchIndex, stringSearchIndex+5, "[");
			stringSearchIndex = temp.indexOf("&lsb.", stringSearchIndex);
		}
		//reset search index
		stringSearchIndex = 0;
		return temp.toString();
	}

	/**
	 * User hit enter while in the textfield
	 *
	 * @param evt ActionEvent created by textfield
	 */

	public void actionPerformed(ActionEvent evt) {
    }

	/**
	 * Method to process a string to take correct
	 * actions before sending (if needed) information
	 * to the remote server
	 */

	public void scriptText(String temp) {
		//if we are logging and logged in, log this
		if (logging) {
	        if (loggedIn) {
	        	log.logCommand(options[25] + temp);
			}
		}

		//check if the current command is the same as the last one
		if (!(temp.equals(lastCommands[0]))) {
			//add the command to the command history array
			System.arraycopy(lastCommands, 0, lastCommandsTmp, 1, 49);
			lastCommands = lastCommandsTmp;
	        lastCommands[0] = temp;
		}
	    commandCount = -2;
	    commandDirection = 2;

		//check if we have an alias
		if ((options[79].length() < 1) || (temp.charAt(0) == options[79].charAt(0))) {

			StringTokenizer tokenizer = new StringTokenizer(temp);
			String name = tokenizer.nextToken();

			//only chop off the flag if we have a flag to chop off!!!
			if (options[79].length() != 0) {
				name = name.substring(1, name.length());
			}

			int vars = tokenizer.countTokens();

			//check possible alias against alias array
			for (int i = 0; i < aliases.size(); i = i + 2) {
				String tempString = (String)(aliases.get(i));

				//if you have a match, reassemble the string
				if (tempString.equalsIgnoreCase(name)) {
					temp = (String)(aliases.get(i+1));
					i = aliases.size() - 2;
					stringSearchIndex = temp.indexOf("&0");
					int j = 0;
					while (stringSearchIndex != -1) {

						if (vars > 0) {
							String replacement = tokenizer.nextToken();
							while (stringSearchIndex != -1) {
								temp = temp.substring(0, stringSearchIndex) + replacement + temp.substring(stringSearchIndex+2, temp.length());
								stringSearchIndex = temp.indexOf("&" + j);
							}
							vars--;
						}
						j++;
						stringSearchIndex = temp.indexOf("&" + j);
					}
					aliasFound = true;
				}
			}

			//if we aren't using an alias flag, make sure we always find an alias
			if (options[79].length() < 1) {
				aliasFound = true;
			}

			//if no alias was found, note it
			if (!aliasFound) {
				gameWrite("Alias: " + name + " not found");
			}

			//reset aliasFound flag
			aliasFound = false;
		}

		//reset stringSearchIndex
        stringSearchIndex = 0;

		if (options[38].equalsIgnoreCase("true")) {
			StringBuffer text = new StringBuffer(temp);
			String tempNick;
			for (int i = 0; i < nicknames.size(); i+=2) {
				tempNick = (String)(nicknames.get(i));
				stringSearchIndex = 0;
				while (true) {
					stringSearchIndex = text.indexOf(tempNick, stringSearchIndex);
					if (stringSearchIndex > -1) {
						text.replace(stringSearchIndex, stringSearchIndex+tempNick.length(), (String)(nicknames.get(i+1)));
					} else {
						break;
					}
				}
			}
			temp = text.toString();
			stringSearchIndex = 0;
		}

		if (temp.charAt(0) == '#') {
			if (options[24].toLowerCase().equals("true")) {
        	   	gameWrite(options[25] + temp);
			}
			systemCommand(temp);
			error = false;
		} else {
        	// So you want to quit?
        	if ("quit".equalsIgnoreCase(temp) || "qq".equalsIgnoreCase(temp)) {
        	    // Let's be nice
        	    //send quit command to game
        	    gameWrite("<b>Goodbye!</b>");
				exit(temp);
        	} else {
				checkSendText(temp);
			}
		}
    }

	/**
	 * Checks the string that will be sent to the server
	 *
	 * @param text The string waiting to be sent
	 */

	public static void checkSendText(String text) {
		lowerCase = text.toLowerCase();
		String chat = "kchat ";
		if (chatToKchat) {
			chat = "chat ";
		}
		if (lowerCase.startsWith("etell ")) {
			text = chat + "/tell " + text.substring(6, text.length());
			lowerCase = text.toLowerCase();
		}

		//check to see if we have to enable the spam timer
        if ((lowerCase.startsWith("whofull")) || (lowerCase.startsWith("kl")) || (lowerCase.startsWith("qwf")) || (lowerCase.startsWith("where"))) {
			spamCount = 62;
			timers.UpdateSpamTimer(spamCount);
			spamTimer.start();
		} else if (lowerCase.startsWith(chat)) {
			text = text.substring(chat.length(), text.length());
			chatIO.write(text);
			//local echo
			StringBuffer temp = new StringBuffer(characterTranslate(text));
			if (options[24].equalsIgnoreCase("true")) {
				//set the initial search index
				stringSearchIndex = text.indexOf("<");
				//check to see if we need to convert any < (to avoid the html parser)
				while (stringSearchIndex != -1) {
					temp.replace(stringSearchIndex, stringSearchIndex+1, "&lt;");
					stringSearchIndex = temp.indexOf("<", stringSearchIndex);
				}
				//reset search index
				stringSearchIndex = 0;

				game.skipHighlight(true);
				gameWrite(options[25] + chat + temp.toString());
				game.skipHighlight(false);
			}
			return;
		} else if (lowerCase.startsWith("tchat ")) {
			text = text.substring(1, text.length());
		} else if (text.length() > 4) {
           	//So you want help?
           	if ("help".equalsIgnoreCase(text.substring(0, 4)) && (options[26].length() > 0)) {
				if (help.isClosed()) {
					try {
						//set gamewindow closed flag to false
						help.setClosed(false);
					} catch (Exception ee) {}
					//set game window visible
					help.setVisible(true);
					//add game to desktop
					desktop.add(help);
					help.moveToFront();
				} else {
					help.setVisible(true);
				}
				gameWrite("<font color=#ff0000> Getting Help on: " + text.substring(4, text.length()) + "</font>");
				// And clear the TextField
           		command.textfield.setText("");
				HelpThread ht = new HelpThread(text, help);
				ht.start();
				skipSend = true;
			} else if ("help".equalsIgnoreCase(text.substring(0, 4)) && (options[26].length() == 0)) {
				spamCount = 62;
				timers.UpdateSpamTimer(spamCount);
				spamTimer.start();
			}
		}

		StringBuffer temp = new StringBuffer(text);
		stringSearchIndex = temp.indexOf("/");
        //check to see if we need to convert any / (mega-wizard compatible)
        while (stringSearchIndex != -1) {
			temp.replace(stringSearchIndex, stringSearchIndex+1, "&fs.");
			stringSearchIndex = temp.indexOf("/", stringSearchIndex+3);
		}
		//search for "\"
		stringSearchIndex = temp.indexOf("\\", stringSearchIndex);

        //check to see if we need to convert any \ (mega-wizard compatible)
        while (stringSearchIndex != -1) {
			temp.replace(stringSearchIndex, stringSearchIndex+1, "&bs.");
			stringSearchIndex = temp.indexOf("\\", stringSearchIndex+3);
		}

		//search for "["
		stringSearchIndex = temp.indexOf("[");

		//check to see if we need to convert any [ (mega-wiz compatible)
		while (stringSearchIndex != -1) {
			temp.replace(stringSearchIndex, stringSearchIndex+1, "&lsb.");
			stringSearchIndex = temp.indexOf("[", stringSearchIndex+3);
		}
		//reset search index
		stringSearchIndex = 0;

		//if we didn't say not to send
		if (!skipSend) {
			// OK, let's just send the message then
			synchronized (sendReceive) {
	        	sendReceive.write(temp.toString());

				//local echo
				temp = new StringBuffer(characterTranslate(temp.toString()));
				if (options[24].equalsIgnoreCase("true")) {
					//set the initial search index
					stringSearchIndex = temp.indexOf("<");
					//check to see if we need to convert any < (to avoid the html parser)
					while (stringSearchIndex != -1) {
						temp.replace(stringSearchIndex, stringSearchIndex+1, "&lt;");
						stringSearchIndex = temp.indexOf("<", stringSearchIndex);
					}
					//reset search index
					stringSearchIndex = 0;

					game.skipHighlight(true);
           			if (sendReceive.blankBeforeEcho()) {
						gameWrite("<br>" + options[25] + temp.toString());
					} else {
           				gameWrite(options[25] + temp.toString());
					}
					game.skipHighlight(false);
				}
			}
		} else {
			//reset skipSend
           	skipSend = false;
           	skipEcho = false;
		}
	}

	/**
	 * Updates the macros by loading from text file and reassigning keyboard actions
	 */

	public static void updateMacros() {
		try {
			macros = MacroLoader.load(profile);
		} catch (IOException ioe) {
			System.err.println("Error loading Macros: " + ioe);
			SMTPClient.sendError("macros", ioe);
		}
		if (desktop != null) {
			desktop.resetKeyboardActions();
			upDownArrows();
			if (options[61].equalsIgnoreCase("true")) {
				bindPageUpDown();
			}
			for (int i = 0; i < macros.size(); i = i + 2) {
				final String i0 = ((String)(macros.get(i)));
				final int i1 = i + 1;
				if (((String)(macros.get(i1))).equals("COMMANDCLEAR")) {
					commandLineClear(i0);
				} else if (((String)(macros.get(i1))).equals("COMMANDFOCUS")) {
					commandLineFocus(i0);
				} else if (((String)(macros.get(i1))).equals("LASTCOMMAND")) {
					lastCommand(i0);
				} else if (((String)(macros.get(i1))).equals("SECONDLASTCOMMAND")) {
					secondLastCommand(i0);
				} else {
					final KeyStroke keystroke = KeyStroke.getKeyStroke(i0);
					desktop.registerKeyboardAction((new ActionListener() {
						public void actionPerformed(ActionEvent actionEvent) {
							String tempString = (String)(macros.get(i1));
							if (!((i0.startsWith("alt")) || (i0.startsWith("shift")) || (i0.startsWith("control")) || (i0.startsWith("meta")) || (i0.startsWith("button1")))) {
								if (command.textfield.hasFocus()) {
									command.consume();
								}
							}
							MacroParser.parse(tempString);
						}
					}), keystroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
				}
			}
		}
	}

	/**
	 * Method to add an listener to bring focus to the command line
	 */

	private static void commandLineFocus(String keys) {
		final String i0 = keys;
		KeyStroke key = KeyStroke.getKeyStroke(keys);
		ActionListener keyActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (!((i0.startsWith("alt")) || (i0.startsWith("shift")) || (i0.startsWith("control")) || (i0.startsWith("meta")) || (i0.startsWith("button1")))) {
					if (command.textfield.hasFocus()) {
						command.consume();
					}
				}
				commandFocus();
			}
		};

		//adds the listener to the desktop
		desktop.registerKeyboardAction(keyActionListener, key, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	/**
	 * Method to add an listener to bring focus to the command line
	 */

	private static void lastCommand(String keys) {
		final String i0 = keys;
		KeyStroke key = KeyStroke.getKeyStroke(keys);
		ActionListener keyActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (!((i0.startsWith("alt")) || (i0.startsWith("shift")) || (i0.startsWith("control")) || (i0.startsWith("meta")) || (i0.startsWith("button1")))) {
					if (command.textfield.hasFocus()) {
						command.consume();
					}
				}
				KilCliThread.getKilCli().scriptText(getLastCommand());
			}
		};

		//adds the listener to the desktop
		desktop.registerKeyboardAction(keyActionListener, key, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	/**
	 * Method to add an listener to bring focus to the command line
	 */

	private static void secondLastCommand(String keys) {
		final String i0 = keys;
		KeyStroke key = KeyStroke.getKeyStroke(keys);
		ActionListener keyActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (!((i0.startsWith("alt")) || (i0.startsWith("shift")) || (i0.startsWith("control")) || (i0.startsWith("meta")) || (i0.startsWith("button1")))) {
					if (command.textfield.hasFocus()) {
						command.consume();
					}
				}
				KilCliThread.getKilCli().scriptText(getSecondLastCommand());
			}
		};

		//adds the listener to the desktop
		desktop.registerKeyboardAction(keyActionListener, key, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	/**
	 * Method to add an listener to bring focus to the command line
	 */

	public static void commandFocus() {
		try {
			command.moveToFront();
			command.setSelected(true);
			command.textfield.requestFocus();
		} catch (Exception e) {
			e.printStackTrace();
			SMTPClient.sendError("focus", e);
		}
	}


	/**
	 * Method to clear the command line
	 */

	 private static void commandLineClear(String keys) {

		 final String i0 = keys;
		 KeyStroke key = KeyStroke.getKeyStroke(keys);
		 ActionListener keyActionListener = new ActionListener() {
			 public void actionPerformed(ActionEvent actionEvent) {
				if (!((i0.startsWith("alt")) || (i0.startsWith("shift")) || (i0.startsWith("control")) || (i0.startsWith("meta")) || (i0.startsWith("button1")))) {
					if (command.textfield.hasFocus()) {
						command.consume();
					}
				}
				command.textfield.setText("");
				commandFocus();
			}
		};

		//adds the listener to the desktop

		desktop.registerKeyboardAction(keyActionListener, key, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	private static void bindPageUpDown() {
		//if pageup is pressed, game window pages up
		KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0);
		ActionListener keyActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				game.pageUp();
			}
		};

		//if ctrl-home is pressed, text focus goes to the command line
		KeyStroke key2 = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0);
		ActionListener keyActionListener2 = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				game.pageDown();
			}
		};
		//adds the listener to the desktop

		desktop.registerKeyboardAction(keyActionListener, key, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		desktop.registerKeyboardAction(keyActionListener2, key2, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}


	/**
	 * Appends txt to the command line
	 *
	 * @param txt - the text to be appended
	 * @param pos - the position of the cursor
	 */

	public static void append(String txt, int pos) {
		command.append(txt, pos);
	}

	/**
	 * Method to add handlers for the up/down arrows
	 * to the desktop
	 */

	private static void upDownArrows() {
		//up/down arrows, and numpad movement keystrokes
		KeyStroke up = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
		KeyStroke down = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);

		//actions for the up arrow
		ActionListener upActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {

		  		//check if there is text in the command line
		  		String tmp = command.textfield.getText();
		  		//if there is, we want to see if it matches anything
		  		//in the stored array for quick recall
		  		if ((tmp.length() > 0) && (commandCount == -2)) {
					for (int i = 0; i < 50; i++) {
						if (lastCommands[i].startsWith(tmp)) {
							command.textfield.setText(lastCommands[i]);
							i = 60;
						}
					}
					commandCount = 0;
				} else {
					//otherwise, we are just going around in a circle
					//in the stored array
					if (commandCount == -2) {
						commandCount = 0;
					}

		  			if (commandDirection == 0) {

						if (commandCount < 48) {
							commandCount = commandCount + 2;
						} else if (commandCount == 49) {
							commandCount = 1;
						}
					}
		  			command.textfield.setText(lastCommands[commandCount]);
					if (commandCount == 49) {
						commandCount = 0;
					} else {
						commandCount++;
					}
					commandDirection = 1;
				}
			}
		};

		//actions for the down arrow
		ActionListener downActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
		  		//check if there is text in the command line
		  		String tmp = command.textfield.getText();
		  		//if there is, we want to see if it matches anything
		  		//in the stored array for quick recall
		  		if ((tmp.length() > 0) && (commandCount == -2)) {
					for (int i = 0; i < 50; i++) {
						if (lastCommands[i].startsWith(tmp)) {
							command.textfield.setText(lastCommands[i]);
							i = 60;
						}
					}
					commandCount = 0;
				} else {
					//otherwise, we are just going around in a circle
					//in the stored array
					if (commandCount == -2) {
						commandCount = 0;
					}
		  			if (commandDirection == 2) {
						commandCount = 49;
					}
		  			if (commandDirection == 1) {
						if (commandCount > 1) {
							commandCount = commandCount - 2;
						} else if (commandCount == 0) {
							commandCount = 48;
						}
					}
		  			command.textfield.setText(lastCommands[commandCount]);
					if (commandCount == 0) {
						commandCount = 49;
					} else {
						commandCount--;
					}
					commandDirection = 0;
				}
			}
		};


		//add keyboard actions to desktop
		desktop.registerKeyboardAction(upActionListener, up, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		desktop.registerKeyboardAction(downActionListener, down, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	public static void getDefinition(String word) {
		if (help.isClosed()) {
			try {
				//set gamewindow closed flag to false
				help.setClosed(false);
			} catch (Exception ee) {}
			//set game window visible
			help.setVisible(true);
			//add game to desktop
			desktop.add(help);
			help.moveToFront();
		} else {
			help.setVisible(true);
		}
		HelpThread ht = new HelpThread(word.toLowerCase(), help, 1);
		ht.start();
	}

	public static void getSpelling(String word) {
		if (help.isClosed()) {
			try {
				//set gamewindow closed flag to false
				help.setClosed(false);
			} catch (Exception ee) {}
			//set game window visible
			help.setVisible(true);
			//add game to desktop
			desktop.add(help);
			help.moveToFront();
		} else {
			help.setVisible(true);
		}
		HelpThread ht = new HelpThread(word.toLowerCase(), help, 2);
		ht.start();
	}

	public static void getThesaurus(String word) {
		if (help.isClosed()) {
			try {
				//set gamewindow closed flag to false
				help.setClosed(false);
			} catch (Exception ee) {}
			//set game window visible
			help.setVisible(true);
			//add game to desktop
			desktop.add(help);
			help.moveToFront();
		} else {
			help.setVisible(true);
		}
		HelpThread ht = new HelpThread(word.toLowerCase(), help, 3);
		ht.start();
	}

	public static HardenPanel getHarden() {
		return harden;
	}

	public static boolean getCheckHarden() {
		return checkHarden;
	}

	public static String getRight() {
		return hands.getRight();
	}

	public static String getLeft() {
		return hands.getLeft();
	}

	public static void checkStun() {
		sendReceive.checkStun();
	}

	public static void checkSlow() {
		sendReceive.checkSlow();
	}

	public static void startStun(int time) {
		if (time > stunTime) {
			stunTime = time;
			stunned.restart();
		}
	}

	public static void startSlow(int time) {
		if (time > slowTime) {
			slowTime = time;
			slowed.restart();
		}
	}

	public static void setCharacter(String name, int number) {
		name = name.substring(0, 1).toUpperCase() + name.substring(1, name.length()).toLowerCase();
		characters[number] = name;
	}

	public static String getUsedCharacter() {
		return characters[0];
	}

	public static void setUsedCharacter(int number) {
		characters[0] = characters[number];
	}

	public static int getGameNumber() {
		return gameNumber;
	}

	public static String getLastCommand() {
		return lastCommands[0];
	}

	public static String getSecondLastCommand() {
		return lastCommands[1];
	}

	public static String getVersion() {
		return VERSION;
	}

	public static String getLogonScript() {
		return options[68];
	}

	/**
	 * Returns the quitFlag saying if this was a disconnect, reconnect, or quit
	 * @return 0 - if disconnect, 1 if reconnect, 2 if a full quit
	 */
	public static int getQuitFlag() {
		return quitFlag;
	}

	/**
	 * Updates aliases by loading from text file and storing in alias array
	 */

	public static void updateAliases() {
		try {
			aliases = AliasLoader.load(profile);
		} catch (IOException ioe) {}
	}

	/**
	 * Returns the aliases ArrayList
	 *@return The ArrayList containing alias information
	 */

	public static ArrayList getAliases() {
		return aliases;
	}

	/**
	 * Returns the macros ArrayList
	 *@return The ArrayList containing macro information
	 */

	public static ArrayList getMacros() {
		return macros;
	}

	/**
	 * Updates nicknames by loading from text file and storing in alias array
	 */

	public static void updateNicknames() {
		try {
			nicknames = NicknamesLoader.load(profile);
		} catch (IOException ioe) {}
	}

	/**
	 * Returns the nicknames ArrayList
	 *@return The ArrayList containing nickname information
	 */

	public static ArrayList getNicknames() {
		return nicknames;
	}

	/**
	 * Returns the config String[]
	 *@return The String[] containing configuration information
	 */

	public static String[] getOptions() {
		return options;
	}

	/**
	 * Returns the string class name of the current look <br>
	 * and feel
	 * @return The string holding the name of the current look and feel
	 */
	public static String getCurrentLookAndFeel() {
		return currentLookAndFeel;
	}

	/**
	 * Returns the value of the local echo configuration option
	 *
	 * @return The string that is added before each echoed command
	 */
	public static String getLocalEcho() {
		return options[25];
	}

	/**
	 * Method to compare the current config with the new config<br>
	 * and make any necessary changes
	 *
	 * @param String[] o - the new array of config settings
	 */
	public static void updateOptions(String[] o) {
		options[0] = o[0];
		if (logging && (!o[1].toLowerCase().startsWith("true"))) {
			log.close();
			logging = false;
		} else if (!logging && (o[1].toLowerCase().startsWith("true"))) {
			//create a logwriter
		    log = new LogThread("Logging", gameNumber);
		    log.start();
			logging = true;
		}
		options[1] = o[1];
		options[2] = o[2];
		options[3] = o[3];
		options[4] = o[4];
		options[5] = o[5];
		options[6] = o[6];
		options[7] = o[7];
		options[8] = o[8];
		options[9] = o[9];
		options[10] = o[10];
		options[11] = o[11];
		options[12] = o[12];
		options[13] = o[13];
		options[14] = o[14];
		options[15] = o[15];
		options[16] = o[16];
		options[17] = o[17];
		options[18] = o[18];
		options[19] = o[19];
		//options[20] = o[20];

		options[21] = o[21];
		if (sendReceive != null) {
			sendReceive.setTrigger(o[21].equalsIgnoreCase("true"));
			ChatIO.setTrigger(o[21].equalsIgnoreCase("true"));
		}
		options[22] = o[22];
		options[23] = o[23];
		if (options[23].toLowerCase().equals("true")) {
			sounds = true;
		} else {
			sounds = false;
		}
		Script.setSound(sounds);
		options[24] = o[24];
		options[25] = o[25];
		options[26] = o[26];
		if ((help != null) && (help.getCreated())) {
			help.setHelpAddress(o[26]);
		}
		options[27] = o[27];
		options[28] = o[28];
		options[29] = o[29];
		options[30] = o[30];
		options[31] = o[31];
		options[32] = o[32];
		options[33] = o[33];
		options[34] = o[34];
		options[35] = o[35];
		options[36] = o[36];
		options[37] = o[37];
		options[38] = o[38];
		options[39] = o[39];
		options[40] = o[40];
		options[41] = o[41];
		options[42] = o[42];
		options[43] = o[43];
		options[44] = o[44];
		options[45] = o[45];
		options[46] = o[46];
		options[47] = o[47];
		options[48] = o[48];
		options[49] = o[49];
		options[50] = o[50];
		options[51] = o[51];
		options[52] = o[52];
		options[53] = o[53];
		options[54] = o[54];
		options[55] = o[55];
		options[56] = o[56];
		options[57] = o[57];
		options[58] = o[58];
		options[59] = o[59];
		options[60] = o[60];
		options[61] = o[61];
		options[62] = o[62];
		options[63] = o[63];
		options[64] = o[64];
		options[65] = o[65];
		options[66] = o[66];
		options[67] = o[67];
		KilCliText.setWindowBuffer(Integer.parseInt(o[67]));
		options[68] = o[68];
		options[69] = o[69];
		options[70] = o[70];
		options[71] = o[71];
		options[72] = o[72];
		options[73] = o[73];
		options[74] = o[74];
		options[75] = o[75];
		options[76] = o[76];
		options[77] = o[77];
		options[78] = o[78];
		options[79] = o[79];
		options[80] = o[80];
		options[81] = o[81];
		checkHarden = options[81].equalsIgnoreCase("true");
		options[82] = o[82];
		chatToKchat = options[82].equalsIgnoreCase("true");
		options[83] = o[83];
		options[84] = o[84];
		options[85] = o[85];
		options[86] = o[86];
		CommandLine.numpadEnterSendsCommand(options[83].equalsIgnoreCase("true"));
		if (sendReceive != null) {
			sendReceive.setNetMelee(options[69].equalsIgnoreCase("true"));
			sendReceive.setNetBufferSize(Integer.parseInt(o[43]));
			checkTitleBars();
			checkStamps();
		}
		createTextRouting();
		updateMacros();
		o = null;
	}

	/**
	 * Method to return the colors array
	 *
	 * @return String[] - the array containing color information
	 */

	public static String[] getColors() {
		return colors;
	}

	/**
	 * Method to update only the color array
	 *
	 * @param String[] c - the new string array containing color information
	 */

	public static void updateColors(String[] c) {
		colors = c;
		KilCliText.updateColors(colors);
	}

	/**
	 * Updates config and colors by loading from text files and storing in config and colors arrays
	 */

	public void updateConfig() {
		try {
			colors = ConfigLoader.loadColors(profile);
			String[] tmp = ConfigLoader.load(profile);
			updateOptions(tmp);
		} catch (IOException ioe) {}
		KilCliText.updateColors(colors);
	}

	/**
	 * Inserts the given text into the commandLine
	 *
	 * @param txt - The text to be inserted
	 */

	public static void insertCommand(String txt) {
		command.textfield.setText(txt);
	}

	/**
	 * Returns the current value of HPs
	 *
	 * @return HPs - integer value of current HPs
	 */

	public static int getHPs() {
		return HPs;
	}

	/**
	 * Returns the current value of SPs
	 *
	 * @return SPs - integer value of current sPs
	 */

	public static int getSPs() {
		return SPs;
	}

	/**
	 * Returns the desktop
	 *
	 * @return desktop - the JDesktopPane that holds the windows
	 */
	public static JDesktopPane getDesktop() {
		return desktop;
	}

	/**
	 * Returns the string of available directions
	 *
	 * @return dirString - The string used to represent the available directions
	 */

	public static String getDirs() {
		return dirString;
	}

	/**
	 * Returns the current value of the unbalanced timer
	 *
	 * @return timer - integer value of the unbalanced timer
	 */

	public static int getUnbalanced() {
		return timer;
	}

	/**
	 * Returns the current gold held in string format
	 *
	 * @return goldString - string representation of current gold
	 */

	public static String getGold() {
		return goldString;
	}

	/**
	 * Returns the current exp total in string format
	 *
	 * @return expString - string representation of current exp
	 */

	public static String getExp() {
		return expString;
	}

	/**
	 * Returns the name of the current profile
	 *
	 * @return profile - the string with the current profile name
	 */

	public static String getProfile() {
		return profile;
	}

	/**
	 * Returns the current SoundDir config setting
	 *
	 * @return String containing the SoundDir settings
	 */

	public static String getSoundDir() {
		return options[39];
	}

	/**
	 * Sets the current mp3 thread
	 *
	 * @param newmp3 - The new mp3 thread to be used by the main
	 * KilCli program
	 */

	public static void setMP3(MP3Thread newmp3) {
		mp3 = newmp3;
	}

	/**
	 * Returns the use proxy config setting
	 *
	 * @return true/false. True if using proxy, false if not
	 */

	public static String getProxy() {
		return options[40];
	}

	/**
	 * Returns the use proxy type
	 *
	 * @return Type stored in the proxy type config
	 */

	public static String getProxyType() {
		return options[64];
	}

	/**
	 * Returns the use proxy username setting
	 *
	 * @return Username stored in the proxy username config
	 */

	public static String getProxyUsername() {
		if (options[65].length() < 1) {
			return null;
		}
		return options[65];
	}

	/**
	 * Returns the use proxy password setting
	 *
	 * @return password stored in the proxy password config
	 */

	public static String getProxyPassword() {
		if (options[66].length() < 1) {
			return null;
		}
		return options[66];
	}

	/**
	 * Returns the use proxy address setting
	 *
	 * @return Address stored in the proxy address config
	 */

	public static String getProxyHost() {
		return options[41];
	}

	/**
	 * Returns the use proxy port setting
	 *
	 * @return Port stored in the proxy port config
	 */

	public static int getProxyPort() {
		return Integer.parseInt(options[42]);
	}

	/**
	 * Fetches the effects panel when needed
	 *
	 * @return EffectsPanel - the current effects panel
	 */

	public static EffectsPanel getEffects() {
		return effects;
	}

	/**
	 * Updates the battle stance in the Info Panel
	 *
	 * @param stance - the new battle stance
	 */

	public static void updateStance(String stance) {
		info.updateStance(stance);
	}

	/**
	 * Checks to see what theme is saved in the config file<br>
	 * and changes to it
	 */

	private void checkTheme(String plaf, PlasticTheme t) {
		theme = t;
		//determine if it is a metal l&f
		if (!(currentLookAndFeel.startsWith("com.jgoodies")) && (currentLookAndFeel != mac) && (currentLookAndFeel != gtk) && (currentLookAndFeel != windows)) {
			MetalLookAndFeel.setCurrentTheme(t);
		}

		//check if its a plastic l&f
		if (currentLookAndFeel.startsWith("com.jgoodies")) {
			PlasticLookAndFeel.setMyCurrentTheme(t);
		}

		//if it is a different theme we need to update our l&f
		//if (!currentLookAndFeel.equals(plaf)) {
			updateLookAndFeel(plaf);
		//}

		//prepare to load the *.colors file
		File srcFile;
		if (options[20].indexOf("themes" + System.getProperty("file.separator")) == -1) {
			srcFile = new File("themes" + System.getProperty("file.separator") + options[20].toLowerCase() + ".colors");
		} else {
			srcFile = new File(options[20] + ".colors");
		}
		//create the object array to store color information
		Object[] c = new Object[19];
		//prepare temp variables
		int tmpInt = 0;
		int tmpInt2 = 0;
		int tmpInt3 = 0;
		String line = "";

		//make sure the file exists
		if (srcFile.exists()) {
			int count = 0;
			try {

				//read in the lines of the file, 1 by 1, getting necessary information
				//from each of them
				BufferedReader inFile = new BufferedReader(new FileReader(srcFile));
				line = inFile.readLine();
				StringTokenizer st = new StringTokenizer(line,",");
				tmpInt = Integer.parseInt(st.nextToken());
				tmpInt2 = Integer.parseInt(st.nextToken());
				tmpInt3 = Integer.parseInt(st.nextToken());
				c[0] = new java.awt.Color(tmpInt, tmpInt2, tmpInt3);
				count++;
				c[1] = inFile.readLine();
				count++;
				c[2] = inFile.readLine();
				count++;
				c[3] = inFile.readLine();
				count++;
				c[4] = inFile.readLine();
				count++;
				line = inFile.readLine();
				st = new StringTokenizer(line,",");
				tmpInt = Integer.parseInt(st.nextToken());
				tmpInt2 = Integer.parseInt(st.nextToken());
				tmpInt3 = Integer.parseInt(st.nextToken());
				c[5] = new java.awt.Color(tmpInt, tmpInt2, tmpInt3);
				count++;
				line = inFile.readLine();
				st = new StringTokenizer(line,",");
				tmpInt = Integer.parseInt(st.nextToken());
				tmpInt2 = Integer.parseInt(st.nextToken());
				tmpInt3 = Integer.parseInt(st.nextToken());
				c[6] = new java.awt.Color(tmpInt, tmpInt2, tmpInt3);
				count++;
				line = inFile.readLine();
				st = new StringTokenizer(line,",");
				tmpInt = Integer.parseInt(st.nextToken());
				tmpInt2 = Integer.parseInt(st.nextToken());
				tmpInt3 = Integer.parseInt(st.nextToken());
				c[7] = new java.awt.Color(tmpInt, tmpInt2, tmpInt3);
				count++;
				line = inFile.readLine();
				st = new StringTokenizer(line,",");
				tmpInt = Integer.parseInt(st.nextToken());
				tmpInt2 = Integer.parseInt(st.nextToken());
				tmpInt3 = Integer.parseInt(st.nextToken());
				c[8] = new java.awt.Color(tmpInt, tmpInt2, tmpInt3);
				count++;
				line = inFile.readLine();
				st = new StringTokenizer(line,",");
				tmpInt = Integer.parseInt(st.nextToken());
				tmpInt2 = Integer.parseInt(st.nextToken());
				tmpInt3 = Integer.parseInt(st.nextToken());
				c[9] = new java.awt.Color(tmpInt, tmpInt2, tmpInt3);
				count++;
				line = inFile.readLine();
				st = new StringTokenizer(line,",");
				tmpInt = Integer.parseInt(st.nextToken());
				tmpInt2 = Integer.parseInt(st.nextToken());
				tmpInt3 = Integer.parseInt(st.nextToken());
				c[10] = new java.awt.Color(tmpInt, tmpInt2, tmpInt3);
				count++;
				line = inFile.readLine();
				st = new StringTokenizer(line,",");
				tmpInt = Integer.parseInt(st.nextToken());
				tmpInt2 = Integer.parseInt(st.nextToken());
				tmpInt3 = Integer.parseInt(st.nextToken());
				c[11] = new java.awt.Color(tmpInt, tmpInt2, tmpInt3);
				count++;
				line = inFile.readLine();
				st = new StringTokenizer(line,",");
				tmpInt = Integer.parseInt(st.nextToken());
				tmpInt2 = Integer.parseInt(st.nextToken());
				tmpInt3 = Integer.parseInt(st.nextToken());
				c[12] = new java.awt.Color(tmpInt, tmpInt2, tmpInt3);
				count++;
				line = inFile.readLine();
				st = new StringTokenizer(line,",");
				tmpInt = Integer.parseInt(st.nextToken());
				tmpInt2 = Integer.parseInt(st.nextToken());
				tmpInt3 = Integer.parseInt(st.nextToken());
				c[13] = new java.awt.Color(tmpInt, tmpInt2, tmpInt3);
				count++;
				line = inFile.readLine();
				st = new StringTokenizer(line,",");
				tmpInt = Integer.parseInt(st.nextToken());
				tmpInt2 = Integer.parseInt(st.nextToken());
				tmpInt3 = Integer.parseInt(st.nextToken());
				c[14] = new java.awt.Color(tmpInt, tmpInt2, tmpInt3);
				count++;
				line = inFile.readLine();
				st = new StringTokenizer(line,",");
				tmpInt = Integer.parseInt(st.nextToken());
				tmpInt2 = Integer.parseInt(st.nextToken());
				tmpInt3 = Integer.parseInt(st.nextToken());
				c[15] = new java.awt.Color(tmpInt, tmpInt2, tmpInt3);
				count++;
				line = inFile.readLine();
				st = new StringTokenizer(line,",");
				tmpInt = Integer.parseInt(st.nextToken());
				tmpInt2 = Integer.parseInt(st.nextToken());
				tmpInt3 = Integer.parseInt(st.nextToken());
				c[16] = new java.awt.Color(tmpInt, tmpInt2, tmpInt3);
				count++;
				line = inFile.readLine();
				st = new StringTokenizer(line,",");
				tmpInt = Integer.parseInt(st.nextToken());
				tmpInt2 = Integer.parseInt(st.nextToken());
				tmpInt3 = Integer.parseInt(st.nextToken());
				c[17] = new java.awt.Color(tmpInt, tmpInt2, tmpInt3);
				count++;
				c[18] = inFile.readLine();
				count++;

				//now change the theme to what you just loaded
				changeTheme(c);
			} catch (Exception e) {
				System.err.println("error reading from .colors file " + srcFile + " on line: " + count);
				System.err.println(line);
				e.printStackTrace();
				//SMTPClient.sendError(name, e);
			}
		} else {

			//if we couldn't find the *.colors file, just use defaults
			c[0] = new java.awt.Color(222, 222, 222);
			c[1] = "DEDEDE";
			c[2] = "515663";
			c[3] = "0000C8";
			c[4] = "FF0000";
			c[5] = java.awt.Color.green;
			c[6] = java.awt.Color.red;
			c[7] = java.awt.Color.white;
			c[8] = java.awt.Color.red;
			c[9] = java.awt.Color.green;
			c[10] = java.awt.Color.red;
			c[11] = java.awt.Color.white;
			c[12] = java.awt.Color.red;
			c[13] = java.awt.Color.green;
			c[14] = java.awt.Color.red;
			c[15] = java.awt.Color.white;
			c[16] = java.awt.Color.red;
			c[17] = new java.awt.Color(204, 204, 204);
			c[18] = "CCCCCC";
			changeTheme(c);
		}

		//determine if it is a metal l&f
		if (!(currentLookAndFeel.startsWith("com.jgoodies")) && (currentLookAndFeel != mac) && (currentLookAndFeel != gtk) && (currentLookAndFeel != windows)) {
			MetalLookAndFeel.setCurrentTheme(t);
		}

		//check if its a plastic l&f
		if (currentLookAndFeel.startsWith("com.jgoodies")) {
			PlasticLookAndFeel.setMyCurrentTheme(t);
		}
		updateLookAndFeel(plaf);

	}

	/**
	 * Method to change the theme on interior windows
	 *
	 * @param c - an array of objects used for colors
	 */

	private static void changeTheme(Object[] c) {
		//REMEMBER TO:
		//check if the window exists and has been initialized
		status.setColors(c);
		if ((status != null) && (status.getCreated())) {
			status.updateTheme();
		}
		statusHor.setColors(c);
		if ((statusHor != null) && (statusHor.getCreated())) {
			statusHor.updateTheme();
		}

		hands.setColors(c);
		if ((hands != null) && (hands.getCreated())) {
			hands.updateTheme();
		}
		timers.setColors(c);
		if ((timers != null) && (timers.getCreated())) {
			timers.updateTheme();
		}
		movement.setColors(c);
		if ((movement != null) && (movement.getCreated())) {
			movement.updateTheme();
		}
		if ((game != null) && (game.getCreated())) {
			game.updateTheme();
		}
		if ((guildshouts != null) && (guildshouts.getCreated())) {
			guildshouts.updateTheme();
		}
		if ((templeshouts != null) && (templeshouts.getCreated())) {
			templeshouts.updateTheme();
		}
		if ((tells != null) && (tells.getCreated())) {
			tells.updateTheme();
		}
		if ((shouts != null) && (shouts.getCreated())) {
			shouts.updateTheme();
		}
		if ((chats != null) && (chats.getCreated())) {
			chats.updateTheme();
		}
		if ((wails != null) && (wails.getCreated())) {
			wails.updateTheme();
		}
		if ((events != null) && (events.getCreated())) {
			events.updateTheme();
		}
		if ((command != null) && (command.getCreated())) {
			command.updateTheme();
		}
		if ((help != null) && (help.getCreated())) {
			help.updateTheme();
		}
		if ((hhshouts != null) && (hhshouts.getCreated())) {
			hhshouts.updateTheme();
		}
		effects.setColors(c);
		if ((effects != null) && (effects.getCreated())) {
			effects.updateTheme();
		}
		info.setColors(c);
		if ((info != null) && (info.getCreated())) {
			info.updateTheme();
		}
		harden.setColors(c);
		if ((harden != null) && (harden.getCreated())) {
			harden.updateTheme();
		}
	}

	/**
	 * Analyzes a system command (command preceded by '#') entered into the commandLine
	 *
	 * @param text - the command entered
	 */

	private void systemCommand(String text) {
		//reset error flag
		error = false;
		StringTokenizer tokenizer = new StringTokenizer(text, ".");

		//if the user is trying to run a script
        if (text.toLowerCase().startsWith("#script")) {

			//find the space
			stringSearchIndex = text.indexOf(' ');

			//get the script name
			text = text.substring(stringSearchIndex + 1, text.length());

			//try to execute script
			new ScriptExecuteThread(text, new Script()).start();

		} else if (tokenizer.nextToken().equalsIgnoreCase("#kilcli")) {
			try {
				st = new StringTokenizer(tokenizer.nextToken());
				String commandString = st.nextToken();
				if (commandString.equalsIgnoreCase("print")) {
					if (st.hasMoreTokens()) {
						commandString = st.nextToken();
						if (commandString.equalsIgnoreCase("scripts")) {
							File scriptDir = new File("./scripts");
							String [] listOfFiles = new String[0];
							if (scriptDir.isDirectory()) {
								listOfFiles = scriptDir.list();
							}
							gameWrite("<br><u><b>List of available scripts:</b></u>");
							for (int x = 0; x < listOfFiles.length; x++ ){
								if (x == (listOfFiles.length - 1)) {
									gameWrite(listOfFiles[x] + "<br>");
								} else {
									gameWrite(listOfFiles[x]);
								}
							}
						} else if (commandString.equalsIgnoreCase("triggers")) {
							gameWrite("<br><b><u>Current trigger setup:</u></b>");
							gameWrite("EnableTriggers=" + options[21]);
							gameWrite(Trigger.print());
						} else if (commandString.equalsIgnoreCase("stats")) {
							gameWrite(sendReceive.getAvgDamage());
						} else {
							gameWrite("<br><b>Error! Unknown print command: </b>" + text);
							gameWrite("<u><b>Valid print commands are:</b></u>");
							gameWrite("scripts");
							gameWrite("stats");
							gameWrite("triggers");
							error = true;
						}
					} else {
						gameWrite("<br><b>Error!, Unknown print command: </b>" + text);
						gameWrite("<u><b>Valid print commands are:</b></u>");
						gameWrite("scripts");
						gameWrite("stats");
						gameWrite("triggers");
						error = true;
					}
				} else if (commandString.equalsIgnoreCase("chat")) {
					if (st.hasMoreTokens()) {
						commandString = st.nextToken();
						if (commandString.equalsIgnoreCase("reconnect")) {
							if ((chatIO != null) && (chatIO.isRunning())) {
								gameWrite("already connected to KilCli Chat Server");
							} else {
								if (chatIO == null) {
									chatIO = new ChatIO();
								}
								chatIO.start();
							}
						} else {
							gameWrite("<br><b>Error! Unknown chat command: </b>" + text);
							gameWrite("<u><b>Valid chat commands are:</b></u>");
							gameWrite("reconnect");
							error = true;
						}
					} else {
						gameWrite("<br><b>Error! Unknown chat command: </b>" + text);
						gameWrite("<u><b>Valid chat commands are:</b></u>");
						gameWrite("reconnect");
						error = true;
					}
				} else if (commandString.equalsIgnoreCase("stats")) {
					if (st.hasMoreTokens()) {
						commandString = st.nextToken();
						if (commandString.equalsIgnoreCase("print")) {
							gameWrite(sendReceive.getAvgDamage());
						} else if (commandString.equalsIgnoreCase("reset")) {
							sendReceive.resetDamageCount();
							gameWrite("Damage Stats reset!");
						} else if (commandString.equalsIgnoreCase("hardenreset")) {
							sendReceive.hardenReset();
							gameWrite("Resetting Harden Armour");
						} else {
							gameWrite("<br><b>Error! Unknown stats command: </b>" + text);
							gameWrite("<u><b>Valid stats commands are:</b></u>");
							gameWrite("hardenreset");
							gameWrite("print");
							gameWrite("reset");
							error = true;
						}
					} else {
						gameWrite("<br><b>Error! Unknown stats command: </b>" + text);
						gameWrite("<u><b>Valid stats commands are:</b></u>");
						gameWrite("print");
						gameWrite("reset");
						error = true;
					}
				} else if (commandString.equalsIgnoreCase("config")) {
					String property = null;
					String value = null;
					if (text.length() > 14) {
						property = text.substring(15, text.length());
						st = new StringTokenizer(property, "=");
						property = st.nextToken();
						if ((text.indexOf("=") != -1) && (text.indexOf("=") != text.length() - 1)) value = st.nextToken();
					} else {
						error = true;
					}
					if ((value != null) && !error) {
						if (property.toLowerCase().equals("startsound")) {
							options[0] = value;
						} else if (property.toLowerCase().equals("log")) {
							if (logging && (!value.toLowerCase().startsWith("true"))) {
								log.close();
								logging = false;
							} else if (!logging && (value.toLowerCase().startsWith("true"))) {
								//create a logwriter
								log = new LogThread("Logging", gameNumber);
								log.start();
								logging = true;
							}
							options[1] = value;

						} else if (property.equalsIgnoreCase("hpwarning")) {
							options[2] = value;
						} else if (property.equalsIgnoreCase("spwarning")) {
							options[3] = value;
						} else if (property.equalsIgnoreCase("hpwarninglevel")) {
							options[4] = value;
						} else if (property.equalsIgnoreCase("spwarninglevel")) {
							options[5] = value;
						} else if (property.equalsIgnoreCase("gshoutsprefix")) {
							options[6] = value;
						} else if (property.equalsIgnoreCase("tshoutsprefix")) {
							options[7] = value;
						} else if (property.equalsIgnoreCase("tellsprefix")) {
							options[8] = value;
						} else if (property.equalsIgnoreCase("hhshoutsprefix")) {
							options[9] = value;
						} else if (property.equalsIgnoreCase("shoutsprefix")) {
							options[10] = value;
						} else if (property.equalsIgnoreCase("chatsprefix")) {
							options[11] = value;
						} else if (property.equalsIgnoreCase("wailsprefix")) {
							options[12] = value;
						} else if (property.equalsIgnoreCase("eventsprefix")) {
							options[13] = value;
						} else if (property.equalsIgnoreCase("questsprefix")) {
							options[14] = value;
						} else if (property.equalsIgnoreCase("oldstylecommandline")) {
							options[19] = value;
							checkTitleBars();
						} else if (property.equalsIgnoreCase("theme")) {
							options[20] = value;
							checkTheme(currentLookAndFeel, theme);
						} else if (property.equalsIgnoreCase("enabletriggers")) {
							options[21] = value;
							sendReceive.setTrigger(value.equalsIgnoreCase("true"));
							ChatIO.setTrigger(value.equalsIgnoreCase("true"));
						} else if (property.equalsIgnoreCase("startupsoundfile")) {
							options[22] = value;
						} else if (property.equalsIgnoreCase("enablesound")) {
							options[23] = value;
							if (options[23].toLowerCase().equals("true")) {
								sounds = true;
							} else {
								sounds = false;
							}
							Script.setSound(sounds);
						} else if (property.equalsIgnoreCase("localecho")) {
							options[24] = value;
						} else if (property.equalsIgnoreCase("echoprefix")) {
							options[25] = value;
						} else if (property.equalsIgnoreCase("helpaddress")) {
							options[26] = value;
							help.setHelpAddress(value);
						} else if (property.equalsIgnoreCase("chatstext")) {
							options[27] = value;
						} else if (property.equalsIgnoreCase("eventstext")) {
							options[28] = value;
						} else if (property.equalsIgnoreCase("gshoutstext")) {
							options[29] = value;
						} else if (property.equalsIgnoreCase("hhshoutstext")) {
							options[30] = value;
						} else if (property.equalsIgnoreCase("shoutstext")) {
							options[31] = value;
						} else if (property.equalsIgnoreCase("queststext")) {
							options[32] = value;
						} else if (property.equalsIgnoreCase("tellstext")) {
							options[35] = value;
						} else if (property.equalsIgnoreCase("tshoutstext")) {
							options[33] = value;
						} else if (property.equalsIgnoreCase("wailstext")) {
							options[34] = value;
						} else if (property.equalsIgnoreCase("logonsprefix")) {
							options[36] = value;
						} else if (property.equalsIgnoreCase("logonstext")) {
							options[37] = value;
						} else if (property.equalsIgnoreCase("enablenicknames")) {
							options[38] = value;
						} else if (property.equalsIgnoreCase("sounddir")) {
							options[39] = value;
						} else if (property.equalsIgnoreCase("proxyenabled")) {
							options[40] = value;
						} else if (property.equalsIgnoreCase("proxyserver")) {
							options[41] = value;
						} else if (property.equalsIgnoreCase("proxyport")) {
							options[42] = value;
						} else if (property.equalsIgnoreCase("readbuffer")) {
							options[43] = value;
							if (sendReceive != null) {
								sendReceive.setNetBufferSize(Integer.parseInt(options[43]));
							}
						} else if (property.equalsIgnoreCase("oldstylechats")) {
							options[44] = value;
							checkTitleBars();
						} else if (property.equalsIgnoreCase("oldstyleeffects")) {
							options[45] = value;
							checkTitleBars();
						} else if (property.equalsIgnoreCase("oldstyleevents")) {
							options[46] = value;
							checkTitleBars();
						} else if (property.equalsIgnoreCase("oldstylegame")) {
							options[47] = value;
							checkTitleBars();
						} else if (property.equalsIgnoreCase("oldstylegshouts")) {
							options[48] = value;
							checkTitleBars();
						} else if (property.equalsIgnoreCase("oldstylehands")) {
							options[49] = value;
							checkTitleBars();
						} else if (property.equalsIgnoreCase("oldstylehhshouts")) {
							options[50] = value;
							checkTitleBars();
						} else if (property.equalsIgnoreCase("oldstylelogons")) {
							options[51] = value;
							checkTitleBars();
						} else if (property.equalsIgnoreCase("oldstylemovement")) {
							options[52] = value;
							checkTitleBars();
						} else if (property.equalsIgnoreCase("oldstyleshouts")) {
							options[53] = value;
							checkTitleBars();
						} else if (property.equalsIgnoreCase("oldstylequests")) {
							options[54] = value;
							checkTitleBars();
						} else if (property.equalsIgnoreCase("oldstylestatus")) {
							options[55] = value;
							checkTitleBars();
						} else if (property.equalsIgnoreCase("oldstylestatushor")) {
							options[56] = value;
							checkTitleBars();
						} else if (property.equalsIgnoreCase("oldstyletells")) {
							options[57] = value;
							checkTitleBars();
						} else if (property.equalsIgnoreCase("oldstyletimers")) {
							options[58] = value;
							checkTitleBars();
						} else if (property.equalsIgnoreCase("oldstyletshouts")) {
							options[59] = value;
							checkTitleBars();
						} else if (property.equalsIgnoreCase("oldstylewails")) {
							options[60] = value;
							checkTitleBars();
						} else if (property.equalsIgnoreCase("bingpageupdown")) {
							options[61] = value;
						} else if (property.equalsIgnoreCase("oldstyleinfo")) {
							options[62] = value;
						} else if (property.equalsIgnoreCase("startupscript")) {
							options[63] = value;
							checkTitleBars();
						} else if (property.equalsIgnoreCase("proxytype")) {
							options[64] = value;
						} else if (property.equalsIgnoreCase("proxyusername")) {
							options[65] = value;
						} else if (property.equalsIgnoreCase("proxypassword")) {
							options[66] = value;
						} else if (property.equalsIgnoreCase("textbuffer")) {
							options[67] = value;
							KilCliText.setWindowBuffer(Integer.parseInt(value));
						} else if (property.equalsIgnoreCase("logonscript")) {
							options[68] = value;
						} else if (property.equalsIgnoreCase("netmelee")) {
							options[69] = value;
							if (sendReceive != null) {
								sendReceive.setNetMelee(options[69].equalsIgnoreCase("true"));
							}
						} else if (property.equalsIgnoreCase("chatstamp")) {
							options[70] = value;
							checkStamps();
						} else if (property.equalsIgnoreCase("gamestamp")) {
							options[71] = value;
							checkStamps();
						} else if (property.equalsIgnoreCase("gshoutstamp")) {
							options[72] = value;
							checkStamps();
						} else if (property.equalsIgnoreCase("hhshoutstamp")) {
							options[73] = value;
							checkStamps();
						} else if (property.equalsIgnoreCase("logonstamp")) {
							options[74] = value;
							checkStamps();
						} else if (property.equalsIgnoreCase("shoutstamp")) {
							options[75] = value;
							checkStamps();
						} else if (property.equalsIgnoreCase("tellstamp")) {
							options[76] = value;
							checkStamps();
						} else if (property.equalsIgnoreCase("tshoutstamp")) {
							options[77] = value;
							checkStamps();
						} else if (property.equalsIgnoreCase("wailstamp")) {
							options[78] = value;
							checkStamps();
						} else if (property.equalsIgnoreCase("aliasflag")) {
							options[79] = value;
						} else if (property.equalsIgnoreCase("oldstyleharden")) {
							options[80] = value;
							checkTitleBars();
						} else if (property.equalsIgnoreCase("monitorharden")) {
							options[81] = value;
							checkHarden = value.equalsIgnoreCase("true");
						} else if (property.equalsIgnoreCase("chattokchat")) {
							options[82] = value;
							chatToKchat = value.equalsIgnoreCase("true");
						} else if (property.equalsIgnoreCase("numpadentersendcommand")) {
							options[83] = value;
							CommandLine.numpadEnterSendsCommand(options[83].equalsIgnoreCase("true"));
						} else if (property.equalsIgnoreCase("connecttokchat")) {
							options[84] = value;
						} else if (property.equalsIgnoreCase("kchattell")) {
							options[85] = value;
							createTextRouting();
						} else if (property.equalsIgnoreCase("kchatchat")) {
							options[86] = value;
							createTextRouting();
						} else {
							gameWrite("<br><b>Error! Bad configuration property name:</b> " + property);
							gameWrite("<u><b>Valid property names are:</b></u>");
							gameWrite("AliasFlag");
							gameWrite("BindPageUpDown");
							gameWrite("ChatsPrefix");
							gameWrite("ChatStamp");
							gameWrite("ChatsText");
							gameWrite("ChatToKChat");
							gameWrite("ConnectToKChat");
							gameWrite("EchoPrefix");
							gameWrite("EnableNicknames");
							gameWrite("EnableSound");
							gameWrite("EnableTriggers");
							gameWrite("EventsPrefix");
							gameWrite("EventsText");
							gameWrite("GameStamp");
							gameWrite("GShoutsPrefix");
							gameWrite("GShoutStamp");
							gameWrite("GShoutsText");
							gameWrite("HelpAddress");
							gameWrite("HHShoutsPrefix");
							gameWrite("HHShoutStamp");
							gameWrite("HHShoutsText");
							gameWrite("HPWarning");
							gameWrite("HPWarningLevel");
							gameWrite("KChatChat");
							gameWrite("KChatTell");
							gameWrite("LocalEcho");
							gameWrite("Log");
							gameWrite("LogonsPrefix");
							gameWrite("LogonScript");
							gameWrite("LogonStamp");
							gameWrite("LogonsText");
							gameWrite("MonitorHarden");
							gameWrite("NetMelee");
							gameWrite("OldStyleChats");
							gameWrite("OldStyleCommandLine");
							gameWrite("OldStyleEffects");
							gameWrite("OldStyleEvents");
							gameWrite("OldStyleGame");
							gameWrite("OldStyleGShouts");
							gameWrite("OldStyleHands");
							gameWrite("OldStyleHarden");
							gameWrite("OldStyleHHShouts");
							gameWrite("OldStyleInfo");
							gameWrite("OldstyleLogons");
							gameWrite("OldStyleMovement");
							gameWrite("OldStyleShouts");
							gameWrite("OldStyleQuests");
							gameWrite("OldStyleStatus");
							gameWrite("OldStyleStatusHor");
							gameWrite("OldStyleTells");
							gameWrite("OldStyleTimers");
							gameWrite("OldStyleTShouts");
							gameWrite("OldStyleWails");
							gameWrite("ProxyEnabled");
							gameWrite("ProxyPassword");
							gameWrite("ProxyPort");
							gameWrite("ProxyServer");
							gameWrite("ProxyType");
							gameWrite("ProxyUsername");
							gameWrite("QuestsPrefix");
							gameWrite("QuestsText");
							gameWrite("ReadBuffer");
							gameWrite("ShoutsPrefix");
							gameWrite("ShoutStamp");
							gameWrite("ShoutsText");
							gameWrite("SoundDir");
							gameWrite("SPWarning");
							gameWrite("SPWarningLevel");
							gameWrite("StartupScript");
							gameWrite("StartSound");
							gameWrite("StartupSoundFile");
							gameWrite("TellsPrefix");
							gameWrite("TellStamp");
							gameWrite("TellsText");
							gameWrite("TextBuffer");
							gameWrite("Theme");
							gameWrite("TShoutsPrefix");
							gameWrite("TShoutStamp");
							gameWrite("TShoutsText");
							gameWrite("WailsPrefix");
							gameWrite("WailStamp");
							gameWrite("WailsText<br>");
							error = true;
						}
						if (!error) {
							if (sendReceive != null) {
								sendReceive.setNetBufferSize(Integer.parseInt(options[43]));
								checkTitleBars();
							}
							updateMacros();
						}
					} else {
						gameWrite("<br><b>Error!</b> No configuration value given!");
						gameWrite("Proper format is : #kilcli.config (name)=(value)<br>");
						error = true;
					}

					if (!error) {
						gameWrite("Setting " + property + " to " + value + "<br>");
					}

				} else if (commandString.equalsIgnoreCase("colors")) {
					String property = null;
					String value = null;
					if (text.length() > 14) {
						property = text.substring(15, text.length());
						st = new StringTokenizer(property, "=");
						property = st.nextToken();
						if ((text.indexOf("=") != -1) && (text.indexOf("=") != text.length() - 1)) value = st.nextToken();
					} else {
						error = true;
					}
					if ((value != null) && (value.length() == 6) && !error) {
						if (property.equals("R")) {
							colors[0] = value;
						} else if (property.equals("r")) {
							colors[1] = value;
						} else if (property.equals("G")) {
							colors[2] = value;
						} else if (property.equals("g")) {
							colors[3] = value;
						} else if (property.equals("W")) {
							colors[4] = value;
						} else if (property.equals("w")) {
							colors[5] = value;
						} else if (property.equals("C")) {
							colors[6] = value;
						} else if (property.equals("c")) {
							colors[7] = value;
						} else if (property.equals("V")) {
							colors[8] = value;
						} else if (property.equals("v")) {
							colors[9] = value;
						} else if (property.equals("B")) {
							colors[10] = value;
						} else if (property.equals("b")) {
							colors[11] = value;
						} else if (property.equals("O")) {
							colors[12] = value;
						} else if (property.equals("o")) {
							colors[13] = value;
						} else if (property.equals("Y")) {
							colors[14] = value;
						} else if (property.equals("y")) {
							colors[15] = value;
						} else if (property.equals("P")) {
							colors[16] = value;
						} else if (property.equals("p")) {
							colors[17] = value;
						} else if (property.equals("K1")) {
							colors[18] = value;
						} else if (property.equals("K2")) {
							colors[19] = value;
						} else if (property.equals("k1")) {
							colors[20] = value;
						} else if (property.equals("k2")) {
							colors[21] = value;
						} else {
							gameWrite("<br><b>Error! Bad color name:</b> " + property);
							gameWrite("<u><b>Valid color names are:</b></u>");
							gameWrite("R, r, G, g, W, w");
							gameWrite("C, c, V, v, B, b");
							gameWrite("O, o, Y, y, P, p");
							gameWrite("K1, K2, k1, k2<br>");
							error = true;
						}
					} else {
						gameWrite("<br><b>Error! Bad value given:</b> " + value);
						gameWrite("Valid color values must contain exactly <b><u>6</u></b> characters<br>");
						//skipSend = true;
						error = true;
					}

					if (!error) {
						gameWrite("Setting " + property + " to " + value + "<br>");
						KilCliText.updateColors(colors);
					}

				} else if (commandString.equalsIgnoreCase("mp3")) {
					String property = null;
					if (text.length() > 11) {
						property = text.substring(12, text.length());
					} else {
						error = true;
					}
					if ((property != null) && !error) {
						if (property.trim().equalsIgnoreCase("play")) {
							if (sounds) {
								if (count == -1) {
									gameWrite("No play list loaded, cannot play");
								} else {
									mp3 = new MP3Thread("rocky", count);
									mp3.start();
								}
							} else {
								gameWrite("<br>You <b>must</b> enable sound before you can play");
								gameWrite("You can do this by entering:");
								gameWrite("#kilcli.config EnableSound=true<br>");
							}
						} else if ((property.length() > 4) && (property.toLowerCase().startsWith("playtrack"))) {
							if (property.indexOf(" ") != -1) {
								if (sounds) {
									String value = property.substring(property.indexOf(" ") + 1, property.length());
									if ((value != null) && (value.length() > 0)) {
										MP3Thread single = new MP3Thread("single", value);
									}
								} else {
									gameWrite("<br>You <b>must</b> enable sound before you can play");
									gameWrite("You can do this by entering:");
									gameWrite("#kilcli.config EnableSound=true<br>");
								}
							} else {
								gameWrite("<br><b>Error! No track specified.</b>");
								gameWrite("Proper usage is:");
								gameWrite("#kilcli.mp3 playTrack (filename)<br>");
							}
						} else if (property.equalsIgnoreCase("stop")) {
							count = MP3Thread.stopMP3();
						} else if (property.equalsIgnoreCase("next")) {
							if (mp3 != null) {
								mp3.nextMP3();
							}
						} else if (property.equalsIgnoreCase("prev")) {
							if (mp3 != null) {
								mp3.prevMP3();
							}
						} else if (property.equalsIgnoreCase("print")) {
							gameWrite("<u><b>MP3s in current playlist:</b></u>");
							gameWrite(MP3Thread.printMP3());
						} else {
							error = true;
						}
					}
					if (error) {
						gameWrite("<br><b>Error! Bad MP3 command given:</b> " + property);
						gameWrite("<u><b>Valid MP3 commands are:</b></u>");
						gameWrite("play");
						gameWrite("playTrack (filename)");
						gameWrite("stop");
						gameWrite("next");
						gameWrite("prev");
						gameWrite("print<br>");
					}

				} else if (commandString.equalsIgnoreCase("trigger")) {
					String property = null;
					if (text.length() > 15) {
						property = text.substring(16, text.length());
					} else {
						error = true;
					}
					if ((property != null) && !error) {
						if (property.equalsIgnoreCase("print")) {
							gameWrite("<br><b><u>Current trigger setup:</u></b>");
							gameWrite("EnableTriggers=" + options[21]);
							gameWrite(Trigger.print());
						} else if (property.length() > 7) {
							if (property.substring(0, 7).equalsIgnoreCase("enabled")) {
								property = property.substring(7, property.length());
								StringTokenizer token = new StringTokenizer(property);
								if (token.hasMoreTokens()) {
									String temp = token.nextToken();
									int tmp = Integer.parseInt(temp);
									if (token.hasMoreTokens()) {
										temp = token.nextToken();
										Trigger.enable(tmp, temp);
										gameWrite("Updated Trigger number: " + tmp + ". Enabled set to: " + temp);
										gameWrite("Use: '#kilcli.print triggers' to verify the changes");
									} else {
										gameWrite("Proper usage is:");
										gameWrite("#kilcli.trigger enabled (TriggerNumber) (true|false)");
									}
								} else {
									gameWrite("Proper usage is:");
									gameWrite("#kilcli.trigger enabled (TriggerNumber) (true|false)");
								}
							} else {
								error = true;
							}
						} else {
							error = true;
						}
					}
					if (error) {
						gameWrite("<br><b>Error! Bad trigger command given:</b> " + property);
						gameWrite("<u>Valid trigger commands are:</u>");
						gameWrite("enabled (trigger#) (true|false)");
						gameWrite("print");
					}
				} else if (commandString.equalsIgnoreCase("script")) {
					String property = null;
					if (text.length() > 14) {
						property = text.substring(15, text.length());
						error = false;
					} else {
						error = true;
					}
					if ((property != null) && !error) {
						if (property.equalsIgnoreCase("list")) {
							ArrayList tmp = ScriptExecuteThread.getList();
							gameWrite("<br><b><u>List of Scripts Currently Executing:</u></b>");
							for (int i = 0; i < tmp.size(); i++) {
								gameWrite((i+1) + ". " + (Script)(tmp.get(i)));
							}
							gameWrite("");
						} else if (property.equalsIgnoreCase("variables")) {
							gameWrite("<br><b><u>List of current script variables:</u></b>");
							gameWrite(Script.getVariables() + "<br>");
						} else if (property.toLowerCase().startsWith("quit")) {
							ArrayList tmp = ScriptExecuteThread.getList();
							property = property.substring(5, property.length());
							Script tmpScript = (Script)(tmp.get((Integer.parseInt(property) - 1)));
							tmpScript.quit();
							gameWrite("<br><b>Quitting Script: </b>" + tmpScript + "<br>");
							tmpScript = null;
						} else if (property.substring(0, 3).equalsIgnoreCase("set")) {
							property = property.substring(3, property.length());
							StringTokenizer token = new StringTokenizer(property);
							if (token.hasMoreTokens()) {
								String temp = token.nextToken();
								if (token.hasMoreTokens()) {
									String temp2 = token.nextToken();
									Script.setVariable(temp, temp2);
								} else {
									gameWrite("Proper usage is:");
									gameWrite("#kilcli.script set (VariableName) (Value)");
								}
							} else {
								gameWrite("Proper usage is:");
								gameWrite("#kilcli.script set (VariableName) (Value)");
							}
						} else {
							error = true;
						}
					}
					if (error) {
						gameWrite("<br><b>Error! Bad script command given:</b> " + property);
						gameWrite("<u>Valid script commands are:</u>");
						gameWrite("quit (script#)");
						gameWrite("list");
						gameWrite("variables");
						gameWrite("set (VariableName) (Value)<br>");
					}
				} else {
					gameWrite("<br><b>Error! Unknown system command:</b> " + text);
					gameWrite("<u>Valid commands are:</u>");
					gameWrite("#kilcli.print (item)");
					gameWrite("#kilcli.config (property)=(value)");
					gameWrite("#kilcli.colors (color)=(value)");
					gameWrite("#kilcli.mp3 (command)");
					gameWrite("#kilcli.trigger (command)");
					gameWrite("#kilcli.script (command)");
					gameWrite("#kilcli.status (command)");
					error = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				gameWrite("<br><b>Error! Unknown system command:</b> " + text);
				gameWrite("<u>Valid commands are:</u>");
				gameWrite("#kilcli.print (item)");
				gameWrite("#kilcli.config (property)=(value)");
				gameWrite("#kilcli.colors (color)=(value)");
				gameWrite("#kilcli.mp3 (command)");
				gameWrite("#kilcli.trigger (command)");
				gameWrite("#kilcli.script (command)");
				gameWrite("#kilcli.stats (command)");
				error = true;
			}

		} else {
			gameWrite("<br><b>Error! Unknown command: " + text);
			error = true;
		}

	}

	/**
	 * Disconnects from remote server, saves window positions, but does not close program
	 */

	public boolean disconnect(String quit, int qflag) {
		quitFlag = qflag;
		boolean flag = true;

		if (!(quit.equals("true"))) {
			if (chatIO != null) {
				chatIO.disconnect("");
			}
			if (loggedIn) {
				flag = sendReceive.disconnect(quit);
			}
			if (logging) {
				log.close();
			}
        	WindowConfigWriter.getSettings(getX(), getY(), getWidth(), getHeight(), game, movement, status, guildshouts, templeshouts, tells, chats, shouts, command, hhshouts, events, wails, hands, timers, effects, logons, statusHor, info, harden);
        	try {
        	   	ConfigWriter.configWrite(options);
        	    ConfigWriter.colorsWrite(colors);
        	    ConfigWriter.displayWrite(currentLookAndFeel, options[20], gameNumber);
			} catch (Exception eeee) {}
		}
		sendReceive.stopQuitCountDown();
		loggedIn = false;
		skipOutput = false;
		skipSend = false;
		System.runFinalization();
		System.gc();
		gameWrite("<br>Player Disconnected");
		return flag;
	}

	/**
	 * Disconnects from remote server,
	 * saves window positions,
	 * and closes program
	 */

	public void exit(String quit) {
		if (disconnect(quit, 2)) {
			KilCliThread.quit(this);
		} else {
			System.err.println("Error while disconnecting");
			KilCliThread.quit(this);
		}
	}

    /**
     * Sets the current L&F on each demo module
     */
    public void updateLookAndFeel(String plaf) {
		try {
			currentLookAndFeel = plaf;
		    UIManager.setLookAndFeel(plaf);
    	    SwingUtilities.updateComponentTreeUI(this);
    	    repaint();
		} catch (Exception ex) {
		    System.err.println("Failed loading L&F: " + currentLookAndFeel);
		    System.err.println(ex);
		    //SMTPClient.sendError(name, ex);
		}
	}

    /**
     * A utility function that layers on top of the LookAndFeel's
     * isSupportedLookAndFeel() method. Returns true if the LookAndFeel
     * is supported. Returns false if the LookAndFeel is not supported
     * and/or if there is any kind of error checking if the LookAndFeel
     * is supported.
     *
     * The L&F menu will use this method to detemine whether the various
     * L&F options should be active or inactive.
     *
     */
     protected boolean isAvailableLookAndFeel(String laf) {
         try {
             Class lnfClass = Class.forName(laf);
             LookAndFeel newLAF = (LookAndFeel)(lnfClass.newInstance());
             return newLAF.isSupportedLookAndFeel();
         } catch(Exception e) { // If ANYTHING weird happens, return false
             return false;
         }
     }


	private static void checkTitleBars() {
	   	KilCliThread.getKilCli().checkTheme(currentLookAndFeel, theme);
	   	switchTitleBar(command, options[19].equalsIgnoreCase("true"));
		switchTitleBar(chats, options[44].equalsIgnoreCase("true"));
		switchTitleBar(effects, options[45].equalsIgnoreCase("true"));
		switchTitleBar(events, options[46].equalsIgnoreCase("true"));
		switchTitleBar(game, options[47].equalsIgnoreCase("true"));
		switchTitleBar(guildshouts, options[48].equalsIgnoreCase("true"));
		switchTitleBar(hands, options[49].equalsIgnoreCase("true"));
		switchTitleBar(hhshouts, options[50].equalsIgnoreCase("true"));
		switchTitleBar(logons, options[51].equalsIgnoreCase("true"));
		switchTitleBar(movement, options[52].equalsIgnoreCase("true"));
		switchTitleBar(shouts, options[53].equalsIgnoreCase("true"));
		//switchTitleBar(quests, options[54].equalsIgnoreCase("true"));
		switchTitleBar(status, options[55].equalsIgnoreCase("true"));
		switchTitleBar(statusHor, options[56].equalsIgnoreCase("true"));
		switchTitleBar(tells, options[57].equalsIgnoreCase("true"));
		switchTitleBar(timers, options[58].equalsIgnoreCase("true"));
		switchTitleBar(templeshouts, options[59].equalsIgnoreCase("true"));
		switchTitleBar(wails, options[60].equalsIgnoreCase("true"));
		switchTitleBar(info, options[62].equalsIgnoreCase("true"));
		switchTitleBar(harden, options[80].equalsIgnoreCase("true"));
	}

	private static void switchTitleBar(KilCliInternalFrame window, boolean remove) {
		if (remove) {
			window.removeBarOld();
			window.setVisible(false);
			window.setVisible(true);
		}
	}

	private static void checkStamps() {
		if (game == null) {
			return;
		}
		chats.setStamp(options[70].equalsIgnoreCase("true"));
		game.setStamp(options[71].equalsIgnoreCase("true"));
		guildshouts.setStamp(options[72].equalsIgnoreCase("true"));
		hhshouts.setStamp(options[73].equalsIgnoreCase("true"));
		logons.setStamp(options[74].equalsIgnoreCase("true"));
		shouts.setStamp(options[75].equalsIgnoreCase("true"));
		tells.setStamp(options[76].equalsIgnoreCase("true"));
		templeshouts.setStamp(options[77].equalsIgnoreCase("true"));
		wails.setStamp(options[78].equalsIgnoreCase("true"));
	}

	public static void setStamp(KilCliInternalFrame frame, boolean stamp) {
		if (frame == chats) {
			options[70] = new Boolean(stamp).toString();
		} else if (frame == game) {
			options[71] = new Boolean(stamp).toString();
		} else if (frame == guildshouts) {
			options[72] = new Boolean(stamp).toString();
		} else if (frame == hhshouts) {
			options[73] = new Boolean(stamp).toString();
		} else if (frame == logons) {
			options[74] = new Boolean(stamp).toString();
		} else if (frame == shouts) {
			options[75] = new Boolean(stamp).toString();
		} else if (frame == tells) {
			options[76] = new Boolean(stamp).toString();
		} else if (frame == templeshouts) {
			options[77] = new Boolean(stamp).toString();
		} else if (frame == wails) {
			options[78] = new Boolean(stamp).toString();
		}
	}

	/**
	 * Action triggered when changing themes
	 */

    class ChangeThemeAction extends AbstractAction {
	KilCli kilcli;
	PlasticTheme theme;
	String name;
	String plaf;
        protected ChangeThemeAction(KilCli kilcli, PlasticTheme theme, String name, String plaf) {
            super("ChangeTheme");
	    	this.kilcli = kilcli;
	    	this.theme = theme;
	    	this.name = name;
	    	this.plaf = plaf;
        }

        public void actionPerformed(ActionEvent e) {
	    	if (name != null) {
	    		kilcli.options[20] = name;
			}
	    	kilcli.checkTheme(plaf, theme);
	    	kilcli.checkTitleBars();
		}
    }

	/**
	 * Main method of kilcli, calls the splash window
	 */

    public static void main(String[] args) {
		// Create a new output stream for the standard output.
		PrintStream	stdout	= null;
		try {
	    	stdout = new PrintStream (new FileOutputStream ("kilcli.log"));
	    } catch (Exception e) {
	    	//Couldn't open the file.
	    	System.err.println ("Redirect:  Unable to open output file!");
	    }

		// Create new output stream for the standard error output.
		PrintStream	stderr	= null;
		try {
	    	stderr = new PrintStream (new FileOutputStream ("kilclierr.log"));
	    } catch (Exception e) {
	    	// Couldn't open the file.
	    	System.err.println ("Redirect:  Unable to open error file!");
	    }
	    System.setOut (stdout);
		System.setErr (stderr);


		if ((args.length > 0) && (args[0].equals("log"))) {
			String slash = System.getProperty("file.separator");
			File srcFile = new File("config" + slash + "display.txt");
			StringTokenizer tokenizer;
			String laf = "";
			String themeString = "";

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

						if (tokenizer.hasMoreTokens()) {
							laf = tokenizer.nextToken();
						}
						if (tokenizer.hasMoreTokens()) {
							themeString = tokenizer.nextToken();
						}
					} catch (IOException ioe) {}

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
						} else if (laf.equalsIgnoreCase(gtk)) {
							UIManager.setLookAndFeel(laf);
						} else if (laf.equalsIgnoreCase(windows)) {
							UIManager.setLookAndFeel(laf);
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
				} catch (Exception fnfe) {}
			}
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
			    new LogViewingThread(fileName, file, -1).start();
			}
		} else {
			SplashWindow splash = new SplashWindow("kilcli.jpg", new JFrame(), 2000);
		}
    }
}
