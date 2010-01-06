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

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.JScrollPane;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import javax.swing.plaf.basic.*;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import terris.kilcli.io.*;
import terris.kilcli.writer.*;
import terris.kilcli.*;
import terris.kilcli.thread.*;


/**
 * AliasEditor for KilCli is the class used to edit the aliases<br>
 * through a gui<br>
 * Ver: 1.0.1
 */

public class AliasEditor extends JDialog {
    private JScrollPane jScroll = new JScrollPane();
    private JSplitPane	jSplit;
	private JTabbedPane tabbedpane;
	private JTextField text1;
	private JTextField text4;
	private JTextField text5;
	private JComboBox jComboBox1;
	private JComboBox jComboBox2;
	private JTextField text6;
	private JTextField text7;
	private JTextField text8;
	private JComboBox jComboBox3;
	private JComboBox jComboBox4;
	private JTextField[] aliasArray;
	private JTextField[] textArray;
	private JCheckBox[] delArray;
	private ArrayList aliases;

	/**
	 * Repaints the editor to allow for the new theme
	 */

	public void updateTheme() {
		repaint();
	}

	/**
	 * AliasEditor constructor, with no arguments creates<br>
	 * an events window in the default location
	 */

    public AliasEditor() {
        super(KilCliThread.getKilCli(), "Alias Editor", false);
        tabbedpane = new JTabbedPane();
        tabbedpane.setTabPlacement(JTabbedPane.TOP);
		JPanel bottom = new JPanel();
		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener() {
			//function for when someone clicks OK
		    public void actionPerformed(ActionEvent e) {
				int tmp = tabbedpane.getSelectedIndex();
				if (tmp == 0) {
					update();
					close();
				} else {
					create();
					close();
				}
		    }
		});

		JButton apply = new JButton("Apply");
		apply.addActionListener(new ActionListener() {
			//function for when someone clicks Apply
			public void actionPerformed(ActionEvent e) {
				int tmp = tabbedpane.getSelectedIndex();
				if (tmp == 1) {
					create();
				} else if (tmp == 0) {
					update();
				}
			}
		});
		JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			//function for when someone clicks Reset
			public void actionPerformed(ActionEvent e) {
				int tmp = tabbedpane.getSelectedIndex();
				if (tmp == 0) {
					jScroll = createEdit();
				} else if (tmp == 1) {
					clear();
				}
			}
		});
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			//function to clear fields;
		    public void actionPerformed(ActionEvent e) {
				close();
		    }
		});
		bottom.add(ok);
		bottom.add(apply);
		bottom.add(reset);
		bottom.add(cancel);

        jSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, tabbedpane, bottom);
        jSplit.setDividerSize(0);


		getContentPane().add(jSplit, BorderLayout.CENTER);


		String name;
		JPanel view;
		aliases = KilCli.getAliases();
		name = "Edit Aliases";
		jScroll = createEdit();
 		tabbedpane.add(name, jScroll);

		name = "New Alias";
		view = new JPanel();
		JPanel cell = new JPanel();
		view.setLayout(new GridLayout(3, 2, 0, 5));
		JLabel pix = new JLabel ("Alias Name: (text after alias flag)");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		text1 = new JTextField(20);
		cell.add(text1);
		view.add(cell);
		cell = new JPanel();
		pix = new JLabel("Text:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		text4 = new JTextField(20);
		cell.add(text4);
		view.add(cell);
		cell = new JPanel();
		tabbedpane.add(name, view);


		//get screen dimensions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //create scrollpane, add edit to scrollpane, set for verital bar only


        //set tabbed pane visible
		tabbedpane.setVisible(true);

        //...Then set the window size
        setSize((int)(screenSize.width * 0.50), (int)(screenSize.height * 0.35));

        //Set the window's location.
        setLocation((int)(screenSize.width * 0.25), (int)(screenSize.height * 0.25));
        jSplit.setDividerLocation((int)(this.getHeight() - 75));
        setVisible(true);

	}

	/**
	 * Clears the textfields in the new alias panel
	 */

	private void clear() {
		text1.setText("");
		text4.setText("");
	}

	/**
	 * Closes the editor window
	 */
	private void close() {
		this.dispose();
	}

	/**
	 * Creates a new alias based on info from text1 and text4
	 */
	private void create() {

		//verifty that text1 and text4 have test
		if ((text1.getText().length() > 0) && (text4.getText().length() > 0)) {
			AliasWriter.add(text1.getText(), text4.getText());
			KilCli.updateAliases();
			JOptionPane confirm = new JOptionPane();
			confirm.showMessageDialog(this, "Alias: " + text1.getText() + " created");
			clear();
			aliases = KilCli.getAliases();
			jScroll = createEdit();
		} else {
			new JOptionPane().showMessageDialog(this, "Bad input error, could not create alias");
		}
	}


	/**
	 * Update send all the information from the edit pane to AliasWriter
	 */
	private void update() {

		//Check for deletions
		for (int i = 0; i < (textArray.length); i++) {
			while ((i < delArray.length) && (delArray[i].isSelected())) {

				for (int j = i; j < aliasArray.length - 1; j++) {
					aliasArray[j] = aliasArray[j+1];
				}
				JTextField[] tmp = new JTextField[(aliasArray.length - 1)];
				System.arraycopy(aliasArray, 0, tmp, 0, aliasArray.length - 1);
				aliasArray = tmp;

				for (int j = i; j < textArray.length - 1; j++) {
					textArray[j] = textArray[j+1];
				}
				tmp = new JTextField[(textArray.length - 1)];
				System.arraycopy(textArray, 0, tmp, 0, textArray.length - 1);
				textArray = tmp;
				tmp = null;

				for (int j = i; j < delArray.length - 1; j++) {
					delArray[j] = delArray[j+1];
				}
				JCheckBox[] tmp3 = new JCheckBox[(delArray.length - 1)];
				System.arraycopy(delArray, 0, tmp3, 0, delArray.length - 1);
				delArray = tmp3;
				tmp3 = null;
			}
		}
		for (int i = 0; i < textArray.length; i++) {
			if ((aliasArray[i].getText().length() < 1) || (textArray[i].getText().length() < 1)) {
				new JOptionPane().showMessageDialog(this, "Error, blank alias or alias text, could not update");
				return;
			}
		}
		AliasWriter.update(aliasArray, textArray);
		KilCli.updateAliases();
		JOptionPane confirm = new JOptionPane();
		confirm.showMessageDialog(this, "Aliases Updated");
		jScroll = createEdit();
		aliases = KilCli.getAliases();
		jScroll = createEdit();
	}

	/**
	 * Creates a GridBagConstraints object with the specified grids, insets<br>
	 * and ipadx, filling in defaults for the other settings
	 *
	 * @return GridBagContraints object with necessary values
	 * @param gx - gridx value
	 * @param gy - gridy value
	 * @param i - insets value
	 * @param ix - ipadx value
	 */
	private GridBagConstraints createConstraints(int gx, int gy, Insets i, int ix) {
		GridBagConstraints temp = new GridBagConstraints();
		temp.gridx = gx;
		temp.gridy = gy;
		temp.gridwidth = 1;
		temp.gridheight = 1;
		temp.weightx = 0.0;
		temp.weighty = 0.0;
		temp.anchor = GridBagConstraints.CENTER;
		temp.fill = GridBagConstraints.NONE;
		temp.insets = i;
		temp.ipadx = ix;
		temp.ipady = 0;
		return temp;
	}

	/**
	 * Creates the edit pane
	 */
	private JScrollPane createEdit() {
        jScroll.setVerticalScrollBarPolicy(22);
		JPanel view = new JPanel();
		JPanel cell = new JPanel();
		JLabel pix = new JLabel();
		JCheckBox jCheck;

        view = new JPanel();
        GridBagLayout gridBagLayout1 = new GridBagLayout();
		view.setLayout(gridBagLayout1);

		cell = new JPanel();
		pix = new JLabel("Del");
		cell.add(pix);
		view.add(cell, createConstraints(0, 0, new Insets(0, 0, 0, 0), -9));
		cell = new JPanel();
		pix = new JLabel("Alias (word after flag)");
		cell.add(pix);
		view.add(cell, createConstraints(1, 0, new Insets(0, 0, 0, 0), 54));
		cell = new JPanel();
		pix = new JLabel("Alias Text");
		cell.add(pix);
		view.add(cell, createConstraints(2, 0, new Insets(0, 0, 0, 16), 13));
		int tmp = 1;
		aliasArray = new JTextField[(aliases.size() / 2)];
		textArray = new JTextField[(aliases.size() / 2)];
		delArray = new JCheckBox[(aliases.size() /2)];
		for (int i = 0; i < aliases.size(); i+=2) {
			cell = new JPanel();
			jCheck = new JCheckBox();
			delArray[tmp - 1] = jCheck;
			cell.add(jCheck);
			view.add(cell, createConstraints(0, tmp, new Insets(0, 0, 0, 0), 0));
			cell = new JPanel();
			text6 = new JTextField(15);
			text6.setText((String)(aliases.get(i)));
			aliasArray[tmp-1] = text6;
			cell.add(text6);
			view.add(cell, createConstraints(1, tmp, new Insets(0, 0, 0, 0), 0));
			cell = new JPanel();
			text7 = new JTextField(15);
			text7.setText((String)(aliases.get(i+1)));
			textArray[tmp-1] = text7;
			cell.add(text7);
			view.add(cell, createConstraints(2, tmp, new Insets(0, 0, 0, 0), 0));
			tmp++;
		}
		jScroll.setViewportView(view);
		return jScroll;
	}
}
