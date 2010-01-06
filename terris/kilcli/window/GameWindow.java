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
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.JScrollPane;
import javax.swing.text.Style;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;
import javax.swing.text.html.HTML;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JViewport;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import javax.swing.plaf.basic.*;
import javax.swing.Scrollable;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.io.FileReader;
import java.io.File;
import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.ArrayList;
import java.text.DateFormat;
import terris.kilcli.resource.*;
import terris.kilcli.gui.SearchBox;
import terris.kilcli.KilCli;
import terris.kilcli.resource.FontChooser;
import terris.kilcli.thread.KilCliThread;
import terris.kilcli.thread.RemoveThread;
import terris.kilcli.writer.CSSWriter;

/**
 * GameWindow for KilCli is the class used to display the<br>
 * main game window<br>
 * Ver: 1.0.1
 */

public class GameWindow extends KilCliInternalFrame implements AdjustmentListener {
    private KilCliText edit = new KilCliText();
    private JScrollPane jScroll;
	private String cssFileName = "";
	private String name;
	private int scrollBackCount = 0;
	private int scrollValue = 0;
	private int scrollOldMax = 0;
	private boolean scrolledBack = false;
	private int location = 0;
	private int scrollLast = 0;
	private JScrollBar bar;
	private boolean timeStamp = false;
	private DateFormat dateFormat = DateFormat.getTimeInstance();
	private JCheckBoxMenuItem timeStampCheck;
	private boolean limitChars = true;
	private ArrayList writeBuffer = new ArrayList();

	public void updateTheme() {
		popup = null;
		createMenu();
		repaint();
	}

	/**
	 * GameWindow constructor, creates a game window in the specified location<br>
	 * with the specified dimensions
	 * @param name The name of the internal window
	 * @param file The css file name to load
	 * @param locX The starting X location
	 * @param locY The starting Y location
	 * @param width The width of the chats window
	 * @param height The height of the chats window
	 * @param OldStyle Flag to use old no title bar at all style for the commandline<br>
	 */

    public GameWindow(String name, String file, int locX, int locY, int width, int height, boolean oldStyle) {
		this(name, file, locX, locY, width, height, oldStyle, true);
	}

	public GameWindow(String name, String file, int locX, int locY, int width, int height, boolean oldStyle, boolean limit) {
		super(name,
		    true, //resizable
		    true, //closable
		    true, //maximizable
             true);//iconifiable

        this.name = name;
		limitChars = limit;
		//set editor kit for TerrisText Area
		HTMLEditorKit htmlEdit = new HTMLEditorKit();
		edit.setEditorKit(htmlEdit);
		StyleSheet css = new StyleSheet();
		File cssFile = new File(file);
		cssFileName = file;

		try {
			FileReader reader = new FileReader(cssFile);
			css.loadRules(reader, cssFile.toURL());
			reader.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		htmlEdit.setStyleSheet(css);
		htmlEdit.install(edit);
		edit.setDocument(htmlEdit.createDefaultDocument());

		//set background
		edit.setBackground(java.awt.Color.black);

		//set doublebuffer
        edit.setDoubleBuffered(true);

		//create scroll pane, add edit to it, set for verital scroll bar
        jScroll = new JScrollPane();
        jScroll.setViewportView(edit);
        jScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScroll.setVerticalScrollBarPolicy(22);
        bar = jScroll.getVerticalScrollBar();
		bar.addAdjustmentListener(this);

		refocus(edit);

		//add scroll pane to the window, and set visible
        getContentPane().add(jScroll);
        jScroll.setVisible(true);

        //...Then set the window size
        setSize(width, height);

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
		created = true;

	}

	public void adjustmentValueChanged(AdjustmentEvent e) {
		/*System.out.println("e.getValue()=" + e.getValue());
		System.out.println("bar.getMaximum()=" + bar.getMaximum());
		System.out.println("bar.getVisibleAmount()=" + bar.getVisibleAmount());
		System.out.println("bar.getValue()=" + bar.getValue());
        System.out.println("adjusting=" + e.getValueIsAdjusting());
		*/

		if (scrollOldMax > bar.getMaximum()) {
			//text was just deleted
			scrollLast = e.getValue();
			scrollOldMax = bar.getMaximum();
			//scrollValue = bar.getMaximum() - bar.getVisibleAmount();
			bar.setValue(scrollOldMax);
			scrolledBack = false;

			return;
		}

		if (e.getValue() < scrollLast) {
			//scrolling up
			scrolledBack = true;
			scrollLast = e.getValue();
			scrollOldMax = bar.getMaximum();
		} else if (scrolledBack) {
			scrollValue = bar.getMaximum() - bar.getVisibleAmount();
			if (e.getValue() >= scrollValue * .98) {
				scrolledBack = false;
				if (!(getTitle().equals(name))) {
					scrollBackCount = 0;
					setTitle(name);
				}
			}
			scrollLast = e.getValue();
			scrollOldMax = bar.getMaximum();
		} else {
			scrolledBack = false;
			scrollLast = e.getValue();
			scrollOldMax = bar.getMaximum();
		}


		/*
		//if ((e.getValue() >= scrollValue) || (e.getValue() >= scrollValue2)) {
		if (!scrolledBack && (e.getValue() >= scrollLast)) {
			scrollLast = e.getValue();
		} else if (e.getValue() >= scrollValue) {// || (e.getValue() >= scrollValue2)) {
			scrolledBack = false;
			if (!(getTitle().equals(name))) {
				scrollBackCount = 0;
				setTitle(name);
			}
			scrollLast = e.getValue();
		} else {
			scrolledBack = true;
		}*/

	}


	/**
	 * Purges the contents of the window
	 */

	public void Purge() {
		try {
			edit.getDocument().remove(0, edit.getDocument().getLength());
		} catch (Exception e) {}
		System.runFinalization();
		System.gc();
	}

	/**
	 * Adds a string to the internal write buffer.  Does not write directly<br>
	 * for thread safety.
	 * @param input The string to be written
	 */

	public void Write(String input) {
		writeBuffer.add(input);
		if (writeBuffer.size() == 1) {
			writeBuffer(!scrolledBack);
		}
	}

	/**
	 * Writes out the complete buffer to the window
	 */
	private void writeBuffer(boolean scroll) {

		while (writeBuffer.size() > 0) {
			String input = (String)(writeBuffer.get(0));
			try {
				if (input.length() > 0) {
					if (timeStamp) {
						input = "[" + dateFormat.format(Calendar.getInstance().getTime()) + "] > " + input;
					}
					//check if we have passed the max # of characters for this text area
					if ((limitChars) && (edit.getDocument().getLength() > KilCliText.getWindowBuffer())) {
						SwingUtilities.invokeLater(new RemoveThread(edit));
					}

					if (scroll) {
						edit.insertHTML(input, true);
					} else {
						scrollBackCount++;
						edit.insertHTML(input, false);
						setTitle(name + "- SCROLLED BACK - " + scrollBackCount);
					}
				}
			} catch (Exception e) {}
			writeBuffer.remove(0);
		}
	}

	/**
	 * Public visible copy method, copies selected text
	 */

	public void copy() {
		edit.copy();
	}

	/**
	 * Public visible selectAll method, selects all the text
	 */

	public void selectAll() {
		edit.selectAll();
	}

	public void setStamp(boolean newStamp) {
		timeStamp = newStamp;
		timeStampCheck.setState(timeStamp);
	}

	public void toggleStamp() {
		timeStamp = !timeStamp;
		KilCli.setStamp(this, timeStamp);
		timeStampCheck.setState(timeStamp);
	}

	public void skipHighlight(boolean skip) {
		edit.setSkipHighlight(skip);
	}

	public void setCSS(String file) {
		FontChooser chooser = new FontChooser(KilCliThread.getKilCli());
		chooser.setVisible(true);
		Font f;
		Color c;

		// If we got a real font choice, then update the foreground stuff
		if (chooser.getNewFont() != null) {
			f = chooser.getNewFont();
			c = chooser.getNewColor();
			CSSWriter.setFont(cssFileName, f.getFontName(), f.getSize(), f.getStyle(), c.getRed(), c.getGreen(), c.getBlue());

			StyleSheet css = new StyleSheet();
			File cssFile = new File(file);

			try {
				FileReader reader = new FileReader(cssFile);
				css.loadRules(reader, cssFile.toURL());
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
			HTMLEditorKit htmlEdit = new HTMLEditorKit();
			htmlEdit.setStyleSheet(css);
			htmlEdit.install(edit);
			edit.setDocument(htmlEdit.createDefaultDocument());

		}

	}

	public void scrollToBottom() {
		edit.scroll();
	}

	public void backgroundColorChooser(String file) {
		JColorChooser chooser = new JColorChooser();
		chooser.setVisible(true);
		Color c;

		//If we get a real color choice, then update the background
		c = chooser.showDialog(KilCliThread.getKilCli(), "Background Color Selection", java.awt.Color.black);
		if (c != null) {
			CSSWriter.setColor(cssFileName, c.getRed(), c.getGreen(), c.getBlue());

			StyleSheet css = new StyleSheet();
			File cssFile = new File(file);

			try {
				FileReader reader = new FileReader(cssFile);
				css.loadRules(reader, cssFile.toURL());
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
			HTMLEditorKit htmlEdit = new HTMLEditorKit();
			htmlEdit.setStyleSheet(css);
			htmlEdit.install(edit);
			edit.setDocument(htmlEdit.createDefaultDocument());
		}
	}

	public void pageUp() {
		JViewport vp = jScroll.getViewport();
	    Component view;
	    if (vp != null && (view = vp.getView()) != null) {
			Rectangle visRect = vp.getViewRect();
			Dimension vSize = view.getSize();
			int amount;

			amount = ((Scrollable)view).getScrollableBlockIncrement(visRect, 1, -1);
			visRect.y += (amount * -1);
			if ((visRect.y + visRect.height) > vSize.height) {
				visRect.y = Math.max(0, vSize.height - visRect.height);
			} else if (visRect.y < 0) {
				visRect.y = 0;
			}
			vp.setViewPosition(visRect.getLocation());
		}
	}

	public void pageDown() {
		JViewport vp = jScroll.getViewport();
	    Component view;
	    if (vp != null && (view = vp.getView()) != null) {
			Rectangle visRect = vp.getViewRect();
			Dimension vSize = view.getSize();
			int amount;

			amount = ((Scrollable)view).getScrollableBlockIncrement(visRect, 1, 1);
			visRect.y += (amount * 1);
			if ((visRect.y + visRect.height) > vSize.height) {
				visRect.y = Math.max(0, vSize.height - visRect.height);
			} else if (visRect.y < 0) {
				visRect.y = 0;
			}
			vp.setViewPosition(visRect.getLocation());
		}
	}

	public void search() {
		SearchBox search = new SearchBox(KilCliThread.getKilCli(), "Search " + name + " for...", false, this);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        search.setLocation(screenSize.width/2 - 100,screenSize.height/2 - 50);
		search.setVisible(true);
	}

	public void searchFor(String txt) {
		boolean fromTop = false;
		try {
			edit.getSelectedText().length();
			fromTop = false;
			location = edit.getSelectionStart();
		} catch (Exception e) {
			fromTop = true;
			location = 0;
		}

		try {
			if (fromTop) {
				location = edit.getDocument().getText(0, edit.getDocument().getLength()).toLowerCase().indexOf(txt.toLowerCase());
			} else {
				location = edit.getDocument().getText(0, edit.getDocument().getLength()).toLowerCase().indexOf(txt.toLowerCase(), location+1);
			}

			if (location > -1) {
				edit.select(location, location + txt.length());
			} else {
				new JOptionPane().showMessageDialog(this, txt + " not found in " + name);
			}
		} catch (Exception ee) {}

	}

	private void getDefinition() {
		String temp = edit.getSelectedText();
		if ((temp != null) && (temp.length() > 0)) {
			KilCli.getDefinition(temp);
		} else {
			new JOptionPane().showMessageDialog(this, "No text selected for definition");
		}
	}

	/**
	 * Creates the right click menu with<br>
	 * options for Copy, Purge, Remove/Restore title bar, and close
	 */

	public void createMenu() {
        //Create the popup menu.
        popup = new JPopupMenu();

		//brings up the fontchooser
		menuItem = new JMenuItem("Set Window Font");
		menuItem.addActionListener(new ActionListener() {
			//function to reload css
			public void actionPerformed(ActionEvent e) {
				setCSS(cssFileName);
			}
		});
		popup.add(menuItem);

		//brings up the colorchooser
		menuItem = new JMenuItem("Set Background Color");
		menuItem.addActionListener(new ActionListener() {
			//function to reload css
			public void actionPerformed(ActionEvent e) {
				backgroundColorChooser(cssFileName);
			}
		});
		popup.add(menuItem);

		//brings up the prompt to find text
		menuItem = new JMenuItem("Search For...");
		menuItem.addActionListener(new ActionListener() {
			//brings up prompt for text
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});
		popup.add(menuItem);


        //creates the copy item
        menuItem = new JMenuItem("Copy...");
        menuItem.addActionListener(new ActionListener() {
			//function to copy selected text
			public void actionPerformed(ActionEvent e) {
				copy();
			}
        });
        popup.add(menuItem);

        //creates the select all item
        menuItem = new JMenuItem("Select All...");
        menuItem.addActionListener(new ActionListener() {
			//function to select all text
			public void actionPerformed(ActionEvent e) {
				selectAll();
			}
        });
        popup.add(menuItem);

        //creates the purge item
        menuItem = new JMenuItem("Purge Contents...");
        menuItem.addActionListener(new ActionListener() {
			//function to purge window
			public void actionPerformed(ActionEvent e) {
				Purge();
			}
        });
        popup.add(menuItem);

		//creates the remove title bar item
        menuItem = new JMenuItem("Remove Title Bar...");
        menuItem.addActionListener(new ActionListener() {
			//function to remove the title bar from the window
			public void actionPerformed(ActionEvent e) {
				removeBar();
			}
        });
        popup.add(menuItem);

        //creates the restore title bar item
        menuItem = new JMenuItem("Restore Title Bar...");
        menuItem.addActionListener(new ActionListener() {
			//function to remove the title bar from the window
			public void actionPerformed(ActionEvent e) {
				restoreBar();
			}
        });
        popup.add(menuItem);

        //create the dictionary item
        menuItem = new JMenuItem("Get Definition...");
        menuItem.addActionListener(new ActionListener() {
			//function to set the help window to the dictionary reference page
			public void actionPerformed(ActionEvent e) {
				getDefinition();
			}
		});
		popup.add(menuItem);

        //creates the toggle time stamp item
        timeStampCheck = new JCheckBoxMenuItem("Time Stamps...");
        timeStampCheck.addActionListener(new ActionListener() {
			//function to toggle time stamp on/off
			public void actionPerformed(ActionEvent e) {
				toggleStamp();
			}
		});
		popup.add(timeStampCheck);

        //creates the close window item
        menuItem = new JMenuItem("Close Window...");
        menuItem.addActionListener(new ActionListener() {
			//function to close window
			public void actionPerformed(ActionEvent e) {
				try {
					setClosed(true);
				} catch (Exception ee) {}
			}
        });
        popup.add(menuItem);

        //Add listener to components that can bring up popup menus.
    	MouseListener dwa = new PopupListener();
    	edit.addMouseListener(dwa);
	}

}
