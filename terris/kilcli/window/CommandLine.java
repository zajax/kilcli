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
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.JPanel;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import javax.swing.plaf.basic.*;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.Action;
import javax.swing.JOptionPane;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import terris.kilcli.resource.*;
import terris.kilcli.thread.KilCliThread;
import terris.kilcli.KilCli;

/**
 * CommandLine for KilCli is the class used to display the<br>
 * commandline responsible for getting user input<br>
 * Ver: 1.0.2
 */

public class CommandLine extends KilCliInternalFrame {
    public KilCliTextField textfield;
    private JPanel mover  = new JPanel();
    private JPanel mover2 = new JPanel();
    private static ArrayList keysToBlock = new ArrayList();
    private boolean consume = false;
    private static boolean numpadEnter = false;

	public void updateTheme() {
		popup = null;
		createMenu();
		repaint();
	}

	public static boolean getCreated() {
		return created;
	}


	/**
	 * CommandLine constructor, with arguments for X,Y location and<br>
	 * height,width, creates a command line in the specified location<br>
	 * with the specified dimensions
	 * @param locX The starting X location
	 * @param locY The starting Y location
	 * @param width The width of the chats window
	 * @param height The height of the chats window
	 * @param OldStyle Flag to use old no title bar at all style for the commandline<br>
	 * 			Only works for Win/Lin/Sol
	 */

    public CommandLine(int locX, int locY, int width, int height, boolean oldStyle) {
        super("Command Line",
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable

        //get screen dimensions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		//create a new textfield and limit it to 123 characters
		textfield = new KilCliTextField(123);
		textfield.setColumns(123);

		//hack to get mac os 9 compatiblity
		textfield.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				try {
					if (consume) {
						e.consume();
						consume = false;
					} else {
						char c = e.getKeyChar();
						if (c == KeyEvent.CHAR_UNDEFINED) {
							e.setKeyChar('\'');
						}
					}
				} catch (Exception error) { System.out.println("key exception caught"); }
			}

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					if (e.isActionKey()) {
						consume = true;
						e.consume();
					}
					if (e.getKeyLocation() == 4) {
						//numpad enter
						if ((numpadEnter) && (textfield.getText().length() > 0)) {
							sendCommand();
						} else {
							sendLastCommand();
						}
					} else {
						sendCommand();
					}
				}
			}
		});

		mover.setToolTipText("Click and Drag to move Command Line");
		DragWindowAdapter drag = new DragWindowAdapter(this);
		mover.addMouseListener(drag);
		mover.addMouseMotionListener(drag);

		mover2.setToolTipText("Click and Drag to move Command Line");
		mover2.addMouseListener(drag);
		mover2.addMouseMotionListener(drag);

		getContentPane().add(mover, BorderLayout.WEST);
		getContentPane().add(mover2, BorderLayout.EAST);
        //add textfield to the window
        getContentPane().add(textfield);

		//set window size
		setSize(width, height);
		this.oldStyle = oldStyle;

		//check oldStyle flag to remove title bar completely ***Only for Win/Lin/Sol
		if (oldStyle) {
			//remove the title bar
			ComponentUI frameUI = getUI();
			if (frameUI instanceof BasicInternalFrameUI) {
				((BasicInternalFrameUI) frameUI).setNorthPane(null);
			}
		}

        //Set the window's location.
		setLocation(locX, locY);

		//creates the right click menu
		createMenu();
		created = true;

	}

	public static void numpadEnterSendsCommand(boolean flag) {
		numpadEnter = flag;
	}

	private void sendCommand() {
		if (textfield.getText().length() > 0) {
			KilCliThread.getKilCli().scriptText(textfield.getText());
			textfield.setText("");
		}
	}

	private void sendLastCommand() {
		KilCliThread.getKilCli().scriptText(KilCli.getLastCommand());
	}

	/**
	 * Sets the flag to consume the next keyevent<br>
	 * (used by macros that don't have alt/control/meta/etc)<br>
	 * So extra characters do not appear in the command line
	 */
	public void consume() {
		consume = true;
	}

	/**
	 * Public visible copy method, copies selected text
	 */

	public void copy() {
		textfield.copy();
	}

	/**
	 * Public visible cut method, cuts selected text
	 */

	public void cut() {
		textfield.cut();
	}

	/**
	 * Public visible paste method, pastes from clipboard
	 */

	public void paste() {
		textfield.paste();
	}

	/**
	 * Public visible selectAll method, selects all text in area
	 */

	public void selectAll() {
		textfield.selectAll();
	}

	/**
	 * Appends text to the end of the command line
	 * @param txt - String of text to be appended
	 * @param pos - the position of the cursor
	 */

	public void append(String txt, int pos) {
		int newPos = -1;
		if (pos != -1) {
			newPos = pos + textfield.getText().length();
		}
		textfield.setText(textfield.getText() + txt);
		if (newPos != -1) {
			textfield.setCaretPosition(newPos);
		}
	}

	private void spellCheck() {
		String temp = textfield.getSelectedText();
		if ((temp != null) && (temp.length() > 0)) {
			KilCli.getSpelling(temp);
		} else {
			new JOptionPane().showMessageDialog(this, "No word selected to spell check");
		}
	}

	private void useThesaurus() {
		String temp = textfield.getSelectedText();
		if ((temp != null) && (temp.length() > 0)) {
			KilCli.getThesaurus(temp);
		} else {
			new JOptionPane().showMessageDialog(this, "No word selected to spell check");
		}
	}
	/**
	 * Creates the right click menu with<br>
	 * options for Copy, Purge, Remove/Restore title bar, and close
	 */

	public void createMenu() {
        //Create the popup menu.
		popup = new JPopupMenu();
		popup.add("Command Line Options");
		popup.addSeparator();

        //creates the copy item
        menuItem = new JMenuItem("Copy...");
        menuItem.addActionListener(new ActionListener() {
			//function to copy selected text
			public void actionPerformed(ActionEvent e) {
				copy();
			}
        });
        popup.add(menuItem);

        //creates the cut item
        menuItem = new JMenuItem("Cut...");
        menuItem.addActionListener(new ActionListener() {
			//function to copy selected text
			public void actionPerformed(ActionEvent e) {
				cut();
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

        //creates the paste item
        menuItem = new JMenuItem("Paste...");
        menuItem.addActionListener(new ActionListener() {
			//function to paste text
			public void actionPerformed(ActionEvent e) {
				paste();
			}
        });
        popup.add(menuItem);

        //creates the purge item
        menuItem = new JMenuItem("Spell Check...");
        menuItem.addActionListener(new ActionListener() {
			//function to purge window
			public void actionPerformed(ActionEvent e) {
				spellCheck();
			}
        });
        popup.add(menuItem);

        //creates the purge item
        menuItem = new JMenuItem("Use Thesaurus...");
        menuItem.addActionListener(new ActionListener() {
			//function to purge window
			public void actionPerformed(ActionEvent e) {
				useThesaurus();
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
    	this.addMouseListener(dwa);
    	textfield.addMouseListener(dwa);
	}

}
