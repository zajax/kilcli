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
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import javax.swing.plaf.basic.*;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JColorChooser;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URL;
import java.io.File;
import java.io.FileReader;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import terris.kilcli.resource.FontChooser;
import terris.kilcli.writer.CSSWriter;
import terris.kilcli.thread.KilCliThread;
import terris.kilcli.resource.KilCliText;

/**
 * HelpWindow for KilCli is the class used to display the<br>
 * help files<br>
 * Ver: 1.0.1
 */

public class HelpWindow extends KilCliInternalFrame implements HyperlinkListener {
    private KilCliText edit = new KilCliText(false);
    private JButton back;
    private JPanel urlPanel;
    private JTextField urlField;
    private JScrollPane jScroll;
    final JLabel statusBar = new JLabel(" ");
    private String url;
    private String url2;
	private static String helpAddress = "";
    private int stringSearchIndex = 0;
	private static String[] previousURLs = new String[50];
	private static String[] previousURLsTmp = new String[50];
	private static int previousCount = 0;
	private String cssFileName;
	private String thisPage = "";

	public void updateTheme() {
		popup = null;
		createMenu();
		repaint();
	}

	/**
	 * HelpWindow constructor, with no arguments creates<br>
	 * a help window in the center of the screen
	 */

    public HelpWindow() {
        super("Help Window",
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable

		urlPanel = new JPanel();
		urlPanel.setLayout(new BorderLayout());
		urlField = new JTextField();
		back = new JButton(" Back! ");
		back.setToolTipText("Goes back to the previous page");
		back.addActionListener(new ActionListener() {
			//function for when someone clicks OK
		    public void actionPerformed(ActionEvent e) {
				if (previousCount == 50) {
					previousCount = 0;
				}
				while (previousURLs[previousCount] == null) {
					previousCount--;
					statusBar.setText("Could not go back farther!");
				}
				urlField.setText(previousURLs[previousCount]);
				setPage(previousURLs[previousCount], true);
				previousCount++;
		    }
		});
		urlPanel.add(new JLabel(" Site: "), BorderLayout.WEST);
		urlPanel.add(urlField, BorderLayout.CENTER);
		urlPanel.add(back, BorderLayout.EAST);


		//get screen dimensions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		//set editor kit for TerrisText area
		HTMLEditorKit htmlEdit = new HTMLEditorKit();
		edit.setEditorKit(htmlEdit);
		StyleSheet css = new StyleSheet();
		cssFileName = System.getProperty("user.dir") + System.getProperty("file.separator") + "css" +
						System.getProperty("file.separator") + "helpwindow.css";
		File cssFile = new File(cssFileName);

		try {
			FileReader reader = new FileReader(cssFile);
			css.loadRules(reader, cssFile.toURL());
			reader.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		htmlEdit.setStyleSheet(css);
		HTMLDocument htmlDoc = new HTMLDocument(css);
		edit.setDocument(htmlDoc);

		//set background
		edit.setBackground(java.awt.Color.white);

		//set doublebuffer
        edit.setDoubleBuffered(true);

        edit.setContentType("text/html");
        //create scroll pane, add edit to it, set vertical scroll bars
        jScroll = new JScrollPane();
        jScroll.setViewportView(edit);
        jScroll.setVerticalScrollBarPolicy(22);

        //add scroll pane to window and set visible
        getContentPane().add(urlPanel, BorderLayout.NORTH);
        getContentPane().add(statusBar, BorderLayout.SOUTH);
        getContentPane().add(jScroll, BorderLayout.CENTER);
        jScroll.setVisible(true);

        urlField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				setPage(ae.getActionCommand(), false);
			}
		});

		edit.addHyperlinkListener(this);

        //...Then set the window size
        setSize((int)(screenSize.width * 0.5),(int)(screenSize.height * 0.5));

		refocus(edit);

        //Set the window's location.
        setLocation((int)(screenSize.width * 0.25), (int)(screenSize.height - (screenSize.height * 0.75)));

		//creates the right click menu
		createMenu();
		pl = new PopupListener();
		edit.addMouseListener(pl);
		created = true;
	}

	/**
	 * Detects a mouse clicking on a hyperlink in the help window
	 *
	 * @param even a HyperlinkEvent, activated by a mouse click
	 */

	public void hyperlinkUpdate(HyperlinkEvent he) {
    	HyperlinkEvent.EventType type = he.getEventType();
    	if (type == HyperlinkEvent.EventType.ENTERED) {
    		// Enter event.  Fill in the status bar.
    		if (statusBar != null) {
    	  		statusBar.setText(he.getURL().toString());
    		}
    	}
    	else if (type == HyperlinkEvent.EventType.EXITED) {
    		// Exit event.  Clear the status bar.
    		if (statusBar != null) {
    	  		statusBar.setText(" "); // Must be a space or it disappears
    		}
    	}
    	else if (type == HyperlinkEvent.EventType.ACTIVATED) {
    		// Jump event.  Get the URL, and, if it's not null, switch to that
    		// page in the main editor pane and update the "site url" label.
    		if (he instanceof HTMLFrameHyperlinkEvent) {
    	  		// Ahh, frame event; handle this separately.
    	  		HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent)he;
    	  		HTMLDocument doc = (HTMLDocument)edit.getDocument();
    	  		doc.processHTMLFrameHyperlinkEvent(evt);
    		} else {
    	    	setPage(he.getURL().toString(), false);
    	    	if (urlField != null) {
    	      		urlField.setText(he.getURL().toString());
    	    	}
    	  	}
    	}
	}

	public void addURL(String url) {
		if (thisPage.length() > 0) {
			System.arraycopy(previousURLs, 0, previousURLsTmp, 1, 49);
			previousURLs = previousURLsTmp;
        	previousURLs[0] = thisPage;
        	previousCount = 0;
		}
        thisPage = url;
	}

	public static void setHelpAddress(String ha) {
		helpAddress = ha;
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
	 * Sets the URL that is currently being displayed<br>
	 * in the help window
	 *
	 * @param input The help file being called
	 */

	public void setURL(String input) {
		Purge();
		input = input.toLowerCase();
		url2 = input.substring(5, input.length());
	  	//check for " " to remove from the URL
	  	while (stringSearchIndex != -1) {
			stringSearchIndex = url2.indexOf(" ", stringSearchIndex);
			if (stringSearchIndex != -1) {
		  		url2 = url2.substring(0, stringSearchIndex) + url2.substring(stringSearchIndex+1, url2.length());
		  	}
	  	}
	  	//reset search index
	  	stringSearchIndex = 0;
		url = helpAddress + url2 + ".html";
		urlField.setText(url);
		setPage(url, false);

	}


	public void setPage(String url, boolean skipAdding) {
		if (!urlField.getText().equals(url)) {
			urlField.setText(url);
		}
		try {
			try {
				File temp = new File("kilcli.log");
				edit.setPage(temp.toURL());
			} catch (Exception e) {}

			if ((edit.getPage() != null) && !skipAdding) {
				addURL(url);
			}
			statusBar.setText("Loading...");
			edit.setPage(new java.net.URL(url));
			statusBar.setText(url + " loaded successfully");
		} catch (IOException e) {
		    edit.setText("Help File not Found<br><br>Attempted to access: " + url);
			statusBar.setText("Error: " + e.getMessage());
		}
	}

	/**
	 * Opens the license agreement for viewing
	 */

	public void viewLicense() {
		File license = new File("license.txt");
		edit.setBackground(java.awt.Color.black);
		edit.setForeground(java.awt.Color.white);
		previousCount = 0;
		try {
			edit.setPage(license.toURL());
			urlField.setText(license.toURL().toString());
			statusBar.setText (" Viewing the KilCli License Agreement");
		} catch (IOException e) {
			edit.setText("License Agreement not Found<br><br>Attemped to access: " + license);
			statusBar.setText ("Error while viewing the KilCli license agreement");
		}
	}

	public void setCSS(String file) {
		FontChooser chooser = new FontChooser(KilCliThread.getKilCli());
		chooser.setVisible(true);
		Font f;
		Color c;
		String windowName = this.getTitle();

		// If we got a real font choice, then update our go button
		if (chooser.getNewFont() != null) {
			f = chooser.getNewFont();
			c = chooser.getNewColor();
			windowName = windowName.toLowerCase();
			int searchIndex = windowName.indexOf(" ");
			windowName = windowName.substring(0, searchIndex) + windowName.substring(searchIndex+1, windowName.length());
			windowName = windowName + ".css";
			CSSWriter.setFont(windowName, f.getFontName(), f.getSize(), f.getStyle(), c.getRed(), c.getGreen(), c.getBlue());

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

	public void backgroundColorChooser(String file) {
		JColorChooser chooser = new JColorChooser();
		chooser.setVisible(true);
		Color c;
		String windowName = this.getTitle();

		//If we get a real color choice, then update the background
		c = chooser.showDialog(KilCliThread.getKilCli(), "Background Color Selection", java.awt.Color.black);
		if (c != null) {
			windowName = windowName.toLowerCase();
			int searchIndex = windowName.indexOf(" ");
			windowName = windowName.substring(0, searchIndex) + windowName.substring(searchIndex+1, windowName.length());
			windowName = windowName + ".css";

			CSSWriter.setColor(windowName, c.getRed(), c.getGreen(), c.getBlue());

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

	/**
	 * Public visible copy method, copies selected text
	 */

	public void copy() {
		edit.copy();
	}

	/**
	 * Public visible selectAll method, selects all text in window
	 */

	public void selectAll() {
		edit.selectAll();
	}

}
