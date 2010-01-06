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
import terris.kilcli.KilCli;
import terris.kilcli.resource.*;
import terris.kilcli.thread.*;


/**
 * SquelchEditor for KilCli is the class used to edit the highlight strings<br>
 * through a gui<br>
 * Ver: 1.0.0
 */

public class SquelchEditor extends JDialog {
    private JScrollPane jScroll = new JScrollPane();
    private JSplitPane jSplit;
	private JTabbedPane tabbedpane;
	private JTextField text1;
	private JTextField text5;
	private JComboBox jComboBox1;
	private JTextField text6;
	private JTextField text7;
	private JComboBox jComboBox4;
	private JTextField[] originalArray;
	private JTextField[] replaceArray;
	private JComboBox[] replyArray;
	private JCheckBox[] delArray;

	public void updateTheme() {
		repaint();
	}

	/**
	 * SquelchEditor constructor, with no arguments creates<br>
	 * a GUI to edit the client squelches
	 */

    public SquelchEditor() {
        super(KilCliThread.getKilCli(), "Client Squelch Editor", false);

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
				if (tmp == 0) {
					update();
				} else if (tmp == 1) {
					create();
				}
			}
		});
		JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			//function for when someone clicks Reset
			public void actionPerformed(ActionEvent e) {
				int tmp = tabbedpane.getSelectedIndex();
				if (tmp == 1) {
					clear();
				} else {
					jScroll = createEdit();
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

		String name = "Edit Client Squelches";
		jScroll = createEdit();
 		tabbedpane.add(name, jScroll);

		name = "New Client Squelch";
		JPanel view = new JPanel();
		JPanel cell = new JPanel();
		view.setLayout(new GridLayout(3, 2, 0, 5));
		JLabel pix = new JLabel ("Character Name:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		text1 = new JTextField(20);
		cell.add(text1);
		view.add(cell);
		cell = new JPanel();
		pix = new JLabel("Replaced With (can leave blank):");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		text5 = new JTextField(20);
		cell.add(text5);
		view.add(cell);
		cell = new JPanel();
		pix = new JLabel("Reply:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		jComboBox1 = new JComboBox();
		jComboBox1.addItem("True");
		jComboBox1.addItem("False");
		jComboBox1.setSelectedIndex(0);
		cell.add(jComboBox1);
		view.add(cell);
		tabbedpane.add(name, view);

		//get screen dimensions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();


        //set tabbed pane visible
		tabbedpane.setVisible(true);

        //...Then set the window size
        setSize((int)(screenSize.width * 0.60), (int)(screenSize.height * 0.35));

        //Set the window's location.
        setLocation((int)(screenSize.width * 0.25), (int)(screenSize.height * 0.25));
        jSplit.setDividerLocation((int)(this.getHeight() - 75));
        setVisible(true);

	}

	/**
	 * Method to close this frame
	 */
	private void close() {
		this.dispose();
	}

	/**
	 * Method that clears the New tab's textfields
	 */
	private void clear() {
		text1.setText("");
		jComboBox1.setSelectedIndex(0);
		text5.setText("");
	}

	/**
	 * Method to create a new Trigger from the New tab's info
	 */
	private void create() {
		if (text1.getText().length() > 0) {
			SquelchWriter.add(text1.getText(), text5.getText(), (String)(jComboBox1.getSelectedItem()));
			try {
				Squelch.load(KilCli.getProfile());
				JOptionPane confirm = new JOptionPane();
				confirm.showMessageDialog(this, "Squelch for: " + text1.getText() + " created");
				clear();
				jScroll = createEdit();

			} catch (IOException ioe) {}
		} else {
			new JOptionPane().showMessageDialog(this, "Bad input error, could not create text filter");
		}
	}

	/**
	 * Method to update the triggers from the Edit menu's info
	 */
	private void update() {
		for (int i = 0; i < (originalArray.length); i++) {
			while ((i < delArray.length) && (delArray[i].isSelected())) {

				for (int j = i; j < originalArray.length - 1; j++) {
					originalArray[j] = originalArray[j+1];
				}
				JTextField[] tmp = new JTextField[(originalArray.length - 1)];
				System.arraycopy(originalArray, 0, tmp, 0, originalArray.length - 1);
				originalArray = tmp;

				for (int j = i; j < replaceArray.length - 1; j++) {
					replaceArray[j] = replaceArray[j+1];
				}
				tmp = new JTextField[(replaceArray.length - 1)];
				System.arraycopy(replaceArray, 0, tmp, 0, replaceArray.length - 1);
				replaceArray = tmp;
				tmp = null;

				for (int j = i; j < replyArray.length - 1; j++) {
					replyArray[j] = replyArray[j+1];
				}
				JComboBox[] tmp2 = new JComboBox[(replyArray.length - 1)];
				System.arraycopy(replyArray, 0, tmp2, 0, replyArray.length - 1);
				replyArray = tmp2;

				for (int j = i; j < delArray.length - 1; j++) {
					delArray[j] = delArray[j+1];
				}
				JCheckBox[] tmp3 = new JCheckBox[(delArray.length - 1)];
				System.arraycopy(delArray, 0, tmp3, 0, delArray.length - 1);
				delArray = tmp3;
				tmp3 = null;
			}
		}
		for (int i = 0; i < originalArray.length; i++) {
			if (originalArray[i].getText().length() < 1) {
				new JOptionPane().showMessageDialog(this, "Error, blank character name, could not update");
				return;
			}
		}
		SquelchWriter.update(originalArray, replaceArray, replyArray);
		try {
			Squelch.load(KilCli.getProfile());
			JOptionPane confirm = new JOptionPane();
			confirm.showMessageDialog(this, "Squelches Updated");
			jScroll = createEdit();

		} catch (IOException ioe) {System.err.println(ioe);}
	}

	/**
	 * Method to create the Edit tab's information
	 */
	private JScrollPane createEdit() {
        jScroll.setVerticalScrollBarPolicy(22);
		JPanel view = new JPanel();
		JPanel cell = new JPanel();
		JLabel pix = new JLabel();
		JCheckBox jCheck;
        ArrayList squelches = Squelch.getSquelches();
        view = new JPanel();
        GridBagLayout gridBagLayout1 = new GridBagLayout();
		view.setLayout(gridBagLayout1);

        jScroll.setViewportView(view);
		cell = new JPanel();
		pix = new JLabel("Del");
		cell.add(pix);
		view.add(cell, createConstraints(0, 0, new Insets(0, 0, 0, 0), -9));
		cell = new JPanel();
		pix = new JLabel("Character Name");
		cell.add(pix);
		view.add(cell, createConstraints(1, 0, new Insets(0, 0, 0, 0), 54));
		cell = new JPanel();
		pix = new JLabel("Replace With");
		cell.add(pix);
		view.add(cell, createConstraints(2, 0, new Insets(0, 0, 0, 0), 54));
		cell = new JPanel();
		pix = new JLabel("Reply");
		cell.add(pix);
		view.add(cell, createConstraints(3, 0, new Insets(0, 0, 0, 0), 8));
		int tmp = 1;
		originalArray = new JTextField[(squelches.size() / 3)];
		replyArray = new JComboBox[(squelches.size() / 3)];
		replaceArray = new JTextField[(squelches.size() / 3)];
		delArray = new JCheckBox[(squelches.size() / 3)];
		for (int i = 0; i < squelches.size(); i+=3) {
			cell = new JPanel();
			jCheck = new JCheckBox();
			delArray[tmp - 1] = jCheck;
			cell.add(jCheck);
			view.add(cell, createConstraints(0, tmp, new Insets(0, 0, 0, 0), 0));
			cell = new JPanel();
			text6 = new JTextField(15);
			text6.setText((String)(squelches.get(i)));
			originalArray[tmp-1] = text6;
			cell.add(text6);
			view.add(cell, createConstraints(1, tmp, new Insets(0, 0, 0, 0), 0));
			cell = new JPanel();
			text7 = new JTextField(15);
			text7.setText((String)(squelches.get(i+1)));
			replaceArray[tmp-1] = text7;
			cell.add(text7);
			view.add(cell, createConstraints(2, tmp, new Insets(0, 0, 0, 0), 0));
			cell = new JPanel();
			jComboBox4 = new JComboBox();
			jComboBox4.addItem("True");
			jComboBox4.addItem("False");
			if (((String)(squelches.get(i+2))).equalsIgnoreCase("true")) {
				jComboBox4.setSelectedIndex(0);
			} else {
				jComboBox4.setSelectedIndex(1);
			}
			replyArray[tmp-1] = jComboBox4;
			cell.add(jComboBox4);
			view.add(cell, createConstraints(3, tmp, new Insets(0, 0, 0, 0), 0));
			tmp++;
		}
		jScroll.setViewportView(view);
		return jScroll;
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
}
