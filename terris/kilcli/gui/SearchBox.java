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

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import terris.kilcli.window.GameWindow;

/**
 * SearchBox for KilCli is the dialog that appears when searching<br>
 * through the game window for a specific word/phrase<br>
 * Ver: 1.0.0
 */

public class SearchBox extends JDialog {
	JPanel panel1 = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JTextField searchField = new JTextField();
	JButton findButton = new JButton();
	JButton cancelButton = new JButton();
	JPanel jPanel1 = new JPanel();
	GameWindow caller;

	/**
	 * SearchBox constructor, searches a specific window for text<br>
	 * @param frame - owner
	 * @param title - title of the search box
	 * @param modal - is the dialog modal
	 * @param c - the GameWindow with text to search
	 */
	public SearchBox(Frame frame, String title, boolean modal, GameWindow c) {
    	super(frame, title, modal);
    	caller = c;
    	try {
    		jbInit();
    		pack();
    	} catch(Exception ex) {
      		ex.printStackTrace();
    	}
  	}

  	private void jbInit() throws Exception {
    	panel1.setLayout(gridBagLayout1);
    	findButton.setText("Find");
    	findButton.addActionListener(new SearchBox_findButton_actionAdapter(this));
    	cancelButton.setText("Cancel");
    	cancelButton.addActionListener(new SearchBox_cancelButton_actionAdapter(this));
    	searchField.setText("");
    	getContentPane().add(panel1);
    	panel1.add(searchField,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 113, 0));
    	panel1.add(cancelButton,  new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    	panel1.add(jPanel1,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 45, 24));
    	panel1.add(findButton,    new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 13, -2));
  	}

  	void findButton_actionPerformed(ActionEvent e) {
  		caller.searchFor(searchField.getText());
  	}

  	void cancelButton_actionPerformed(ActionEvent e) {
		this.dispose();
  	}

}

class SearchBox_findButton_actionAdapter implements java.awt.event.ActionListener {
  SearchBox adaptee;

  SearchBox_findButton_actionAdapter(SearchBox adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.findButton_actionPerformed(e);
  }
}

class SearchBox_cancelButton_actionAdapter implements java.awt.event.ActionListener {
  SearchBox adaptee;

  SearchBox_cancelButton_actionAdapter(SearchBox adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}

