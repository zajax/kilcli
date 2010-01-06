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
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.JScrollPane;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import javax.swing.plaf.basic.*;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import terris.kilcli.KilCli;
import terris.kilcli.resource.*;
import terris.kilcli.thread.*;

/**
 * ColorEditor for KilCli is the class used to edit the color<br>
 * configuration through a gui<br>
 * Ver: 1.0.0
 */

public class ColorEditor extends JDialog {
    private JScrollPane jScroll = new JScrollPane();
    private JScrollPane jScroll2 = new JScrollPane();
    private JScrollPane jScroll3 = new JScrollPane();
    private JSplitPane	jSplit;
	private JTabbedPane tabbedpane;
	private ColorEditorPanel R;
	private ColorEditorPanel r;
	private ColorEditorPanel G;
	private ColorEditorPanel g;
	private ColorEditorPanel W;
	private ColorEditorPanel w;
	private ColorEditorPanel C;
	private ColorEditorPanel c;
	private ColorEditorPanel V;
	private ColorEditorPanel v;
	private ColorEditorPanel B;
	private ColorEditorPanel b;
	private ColorEditorPanel O;
	private ColorEditorPanel o;
	private ColorEditorPanel Y;
	private ColorEditorPanel y;
	private ColorEditorPanel P;
	private ColorEditorPanel p;
	private ColorEditorPanel K;
	private ColorEditorPanel k;
	private ColorEditorPanel T;
	private ColorEditorPanel t;
	private String[] errorMsgs;

	private String[] colorConfig;

	public void updateTheme() {
		repaint();
	}

	/**
	 * ColorEditor constructor, with no arguments creates<br>
	 * a GUI to edit the colors
	 */

    public ColorEditor() {
        super(KilCliThread.getKilCli(), "Color Editor", false);
		JPanel bottom = new JPanel();
		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener() {
			//function for when someone clicks OK
		    public void actionPerformed(ActionEvent e) {
				update();
				close();
		    }
		});
		JButton apply = new JButton("Apply");
		apply.addActionListener(new ActionListener() {
			//function for when someone clicks Apply
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			//function for when someone clicks Reset
			public void actionPerformed(ActionEvent e) {
				clear();
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

		colorConfig = KilCli.getColors();
		errorMsgs = new String[colorConfig.length];
		errorMsgs[0] = "Red";
		errorMsgs[1] = "Dark Red";
		errorMsgs[2] = "Green";
		errorMsgs[3] = "Dark Green";
		errorMsgs[4] = "White";
		errorMsgs[5] = "Dark White";
		errorMsgs[6] = "Cyan";
		errorMsgs[7] = "Dark Cyan";
		errorMsgs[8] = "Violet";
		errorMsgs[9] = "Dark Violet";
		errorMsgs[10] = "Blue";
		errorMsgs[11] = "Dark Blue";
		errorMsgs[12] = "Orange";
		errorMsgs[13] = "Dark Orange";
		errorMsgs[14] = "Yellow";
		errorMsgs[15] = "Dark Yellow";
		errorMsgs[16] = "Pink";
		errorMsgs[17] = "Dark Pink";
		errorMsgs[18] = "Highlight (K)";
		errorMsgs[19] = "Highlight (K)";
		errorMsgs[20] = "Highlight (k)";
		errorMsgs[21] = "Highlight (k)";
		errorMsgs[22] = "Brown";
		errorMsgs[23] = "Dark Brown";

        tabbedpane = new JTabbedPane();
        tabbedpane.setTabPlacement(JTabbedPane.TOP);
        jSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, tabbedpane, bottom);
        jSplit.setDividerSize(0);

		getContentPane().add(jSplit, BorderLayout.CENTER);


		String name = "Normal Colors";
		createColors();
		tabbedpane.add(name, jScroll);

		name = "Dark Colors";
		createDarks();
		tabbedpane.add(name, jScroll2);

		name = "Highlight Colors";
		createHighlights();
		tabbedpane.add(name, jScroll3);


		//get screen dimensions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //set tabbed pane visible
		tabbedpane.setVisible(true);

        //...Then set the window size
        setSize((int)(screenSize.width * 0.50), (int)(screenSize.height * 0.40));

        //Set the window's location.
        setLocation((int)(screenSize.width * 0.25), (int)(screenSize.height * 0.25));
        jSplit.setDividerLocation((int)(this.getHeight() * 0.8));
        setVisible(true);

	}

	/**
	 * Resets all the colors to the last saved set
	 */
	private void clear() {
		createColors();
		createDarks();
	}

	/**
	 * Closes the editor
	 */
	private void close() {
		this.dispose();
	}

	/**
	 * Gets the values from the text fields and updates the colors in the KilCli program
	 */
	private void update() {
		colorConfig[0] = R.getText();
		colorConfig[1] = r.getText();
		colorConfig[2] = G.getText();
		colorConfig[3] = g.getText();
		colorConfig[4] = W.getText();
		colorConfig[5] = w.getText();
		colorConfig[6] = C.getText();
		colorConfig[7] = c.getText();
		colorConfig[8] = V.getText();
		colorConfig[9] = v.getText();
		colorConfig[10] = B.getText();
		colorConfig[11] = b.getText();
		colorConfig[12] = O.getText();
		colorConfig[13] = o.getText();
		colorConfig[14] = Y.getText();
		colorConfig[15] = y.getText();
		colorConfig[16] = P.getText();
		colorConfig[17] = p.getText();
		colorConfig[18] = K.getText();
		colorConfig[19] = K.getText2();
		colorConfig[20] = k.getText();
		colorConfig[21] = k.getText2();
		colorConfig[22] = T.getText();
		colorConfig[23] = t.getText();
		for (int i = 0; i < 24; i++) {
			if (colorConfig[i].length() != 6) {
				new JOptionPane().showMessageDialog(this, "Error! Bad color code (not 6 characters) in " + errorMsgs[i] + " color. Update failed");
				return;
			}
		}

		KilCli.updateColors(colorConfig);
		JOptionPane confirm = new JOptionPane();
		confirm.showMessageDialog(this, "Colors Updated");
	}

	/**
	 * Creates the normal colors pane
	 */
	private void createColors() {
		jScroll.setVerticalScrollBarPolicy(22);
		JPanel view = new JPanel();
		view.setLayout(new GridLayout(10, 1, 0, 1));

		R = new ColorEditorPanel("Red Text (R):", colorConfig[0]);
		view.add(R);

		G = new ColorEditorPanel("Green Text (G):", colorConfig[2]);
		view.add(G);

		W = new ColorEditorPanel("White Text (W):", colorConfig[4]);
		view.add(W);

		C = new ColorEditorPanel("Cyan Text (C):", colorConfig[6]);
		view.add(C);

		V = new ColorEditorPanel("Violet Text (V):", colorConfig[8]);
		view.add(V);

		B = new ColorEditorPanel("Blue Text (B):", colorConfig[10]);
		view.add(B);

		O = new ColorEditorPanel("Orange Text (O):", colorConfig[12]);
		view.add(O);

		Y = new ColorEditorPanel("Yellow Text (Y):", colorConfig[14]);
		view.add(Y);

		P = new ColorEditorPanel("Pink Text (P):", colorConfig[16]);
		view.add(P);

		T = new ColorEditorPanel("Brown Text (T):", colorConfig[22]);
		view.add(T);

		jScroll.setViewportView(view);
	}

	/**
	 * Creates the dark colors pane
	 */
	private void createDarks() {
		jScroll2.setVerticalScrollBarPolicy(22);
		JPanel view = new JPanel();
		view.setLayout(new GridLayout(10, 1, 0, 1));

		r = new ColorEditorPanel("Red Text (r):", colorConfig[1]);
		view.add(r);

		g = new ColorEditorPanel("Green Text (g):", colorConfig[3]);
		view.add(g);

		w = new ColorEditorPanel("White Text (w):", colorConfig[5]);
		view.add(w);

		c = new ColorEditorPanel("Cyan Text (c):", colorConfig[7]);
		view.add(c);

		v = new ColorEditorPanel("Violet Text (v):", colorConfig[9]);
		view.add(v);

		b = new ColorEditorPanel("Blue Text (b):", colorConfig[11]);
		view.add(b);

		o = new ColorEditorPanel("Orange Text (o):", colorConfig[13]);
		view.add(o);

		y = new ColorEditorPanel("Yellow Text (y):", colorConfig[15]);
		view.add(y);

		p = new ColorEditorPanel("Pink Text (p):", colorConfig[17]);
		view.add(p);

		t = new ColorEditorPanel("Brown Text (t):", colorConfig[23]);
		view.add(t);


		jScroll2.setViewportView(view);
	}

	/**
	 * Creates the highlight colors pane
	 */
	private void createHighlights() {
		jScroll3.setVerticalScrollBarPolicy(22);
		JPanel view = new JPanel();
		JPanel cell = new JPanel();
		JLabel pix = new JLabel();
		view.setLayout(new GridLayout(2, 1, 0, 1));

		K = new ColorEditorPanel("Background (K):", "Text (K):", colorConfig[18], colorConfig[19]);
		view.add(K);

		k = new ColorEditorPanel("Background (k):", "Text(k):", colorConfig[20], colorConfig[21]);
		view.add(k);

		jScroll3.setViewportView(view);
	}

}
