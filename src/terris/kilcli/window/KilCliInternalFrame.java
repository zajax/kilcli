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
import javax.swing.KeyStroke;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import javax.swing.plaf.basic.*;
import javax.swing.JFrame;
import javax.swing.Scrollable;
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
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import terris.kilcli.io.*;
import terris.kilcli.KilCli;
import terris.kilcli.resource.FontChooser;
import terris.kilcli.thread.KilCliThread;
import terris.kilcli.writer.CSSWriter;

/**
 * KilCliInternalFrame is the class uesd for the basic
 * structure of the internal windows
 * Ver: 1.0.0
 */

public class KilCliInternalFrame extends JInternalFrame {
	public JPopupMenu popup;
	public JMenuItem menuItem;
	public boolean titleBar = true;
	public static boolean created = false;
	public boolean oldStyle = false;
	public DragWindowAdapter dwa;
	public PopupListener pl;

	public static boolean getCreated() {
		return created;
	}


	public KilCliInternalFrame(String name, boolean one, boolean two, boolean three, boolean four) {
		super(name,
		   	true, //resizable
		   	true, //closable
		   	true, //maximizable
           	true);//iconifiable
	}

	/**
	 * Removes the title bar from the window
	 *
	 */

	public void removeBar() {
		putClientProperty("JInternalFrame.isPalette",Boolean.TRUE);
		titleBar = false;
	}

	/**
	 * Removes the title bar the old way
	 *
	 */

	public void removeBarOld() {
		//remove the title bar
		ComponentUI frameUI = getUI();
		if (frameUI instanceof BasicInternalFrameUI) {
			((BasicInternalFrameUI) frameUI).setNorthPane(null);
		}
	}

	public boolean isOldStyle() {
		return oldStyle;
	}

	/**
	 * Restores the title bar to its original state
	 *
	 */

	public void restoreBar() {
		putClientProperty("JInternalFrame.isPalette",Boolean.FALSE);
		titleBar = true;
		repaint();
	}

	public void refocus() {
		refocus(getContentPane());
	}

	public void refocus(Component c) {
		//hack to get mac os 9 compatiblity
		c.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				try {
					if ((e.getKeyChar() > 32) && (e.getKeyChar() < 300)) {
						KilCli.append(new Character(e.getKeyChar()).toString(), -1);
						KilCli.commandFocus();
					}
				} catch (Exception error) {
					System.out.println("key exception caught");
				}
			}
		});
	}
	/**
	 * Returns the status of the title bar
	 * @return titleBar true/false
	 */

	public boolean checkBar() {
		return titleBar;
	}

	/**
	 * Updates the window position
	 *
	 * @param x - the new x location of the window
	 * @param y - the new y location of the window
	 * @param width - the new width of the window
	 * @param height - the new height of the window
	 * @param open - a string to say if the window is visible
	 * @param bar - a string to say if the title bar is visible
	 */

	public void updatePosition(int x, int y, int width, int height, String open, String bar) {
		setLocation(x, y);
		setSize(width, height);

		if (open.toLowerCase().equals("true")) {
			//if window is closed
			if (!isClosed()) {
				try {
					//set gamewindow closed flag to true
					setClosed(true);
				} catch (Exception ee) {
					System.err.println(ee);
					ee.printStackTrace();
				}
			}
		} else {
			if (isClosed()) {
				try {
					//set gamewindow closed flag to false
					setClosed(false);
				} catch (Exception ee) {
					System.err.println(ee);
					ee.printStackTrace();
				}
				//set game window visible
				setVisible(true);
				//add game to desktop
				KilCli.getDesktop().add(this);
				moveToFront();
			}
		}

		if (bar.toLowerCase().equals("true")) {
			if (!checkBar()) {
				restoreBar();
			}
		} else {
			if (checkBar()) {
				removeBar();
			}
		}

	}

	public void createMenu() {
        //Create the popup menu.
        popup = new JPopupMenu();

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
				} catch (Exception ee) {
					System.err.println(ee);
					ee.printStackTrace();
				}
			}
        });
        popup.add(menuItem);
	}



	class PopupListener extends MouseAdapter {
	    public void mousePressed(MouseEvent e) {
	        maybeShowPopup(e, 0);
	    }

	    public void mouseReleased(MouseEvent e) {
	        maybeShowPopup(e, 1);
	    }

	    private void maybeShowPopup(MouseEvent e, int i) {
	        if (e.isPopupTrigger()) {
	            popup.show(e.getComponent(), e.getX(), e.getY());
	        } else {
        		if(i == 1 && e.getClickCount() > 1) {
        		    KilCli.commandFocus();
        		}
			}
	    }
	}

    class DragWindowAdapter extends MouseAdapter implements MouseMotionListener {
		private JInternalFrame m_msgWnd;
		private int m_mousePrevX, m_mousePrevY;
		private int m_frameX, m_frameY;
		private boolean resizing = false;
		private int lastX = -1;
		private int lastY = -1;

		public DragWindowAdapter(JInternalFrame mw) {
			m_msgWnd = mw;
		}
		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
			maybeShowPopup(e, 0);
			m_mousePrevX = e.getX();
			m_mousePrevY = e.getY();
			m_frameX = 0;
			m_frameY = 0;
		}
		public void mouseDragged(MouseEvent e) {
			if (resizing) {
				return;
			}
			if (e.getX() >= (m_msgWnd.getWidth() * .985)) {
				resizing = true;
				return;
			}
			if (e.getY() >= (m_msgWnd.getHeight() * .985)) {
				resizing = true;
				return;
			}

			int X = e.getX();
			int Y = e.getY();
			int MsgX = m_msgWnd.getX();
			int MsgY = m_msgWnd.getY();
			int moveX = X - m_mousePrevX;  // Negative if move left
			int moveY = Y - m_mousePrevY;  // Negative if move down
			if (moveX == 0 && moveY == 0) return;
			m_mousePrevX = X - moveX;
			m_mousePrevY = Y - moveY;
			//System.out.println("mouseDragged x,y = (" + X + "," + Y + ") diff (" + moveX + "," + moveY + ") MsgX/MsgY = " + MsgX + "," + MsgY);
			// mouseDragged caused by setLocation() on frame.
			if ((m_frameX == MsgX && m_frameY == MsgY) && ((m_frameX != 0) && (m_frameY !=0))) {
				m_frameX = 0;
				m_frameY = 0;
				return;
			}

			int newFrameX = MsgX + moveX;
			// '-' would cause wrong
			int newFrameY = MsgY + moveY;
			lastX = X;
			lastY = Y;
			m_frameX = newFrameX;
			m_frameY = newFrameY;
			m_msgWnd.setLocation(newFrameX, newFrameY);
		}
		public void mouseMoved(MouseEvent e) {}

	    public void mouseReleased(MouseEvent e) {
			resizing = false;
	        maybeShowPopup(e, 1);
	    }

	    private void maybeShowPopup(MouseEvent e, int released) {
	        if (e.isPopupTrigger()) {
	            popup.show(e.getComponent(), e.getX(), e.getY());
	        } else {
				if (released == 1) {
					if (e.getClickCount() > 1) {
						KilCli.commandFocus();
					}
				}
			}
	    }
	}

}