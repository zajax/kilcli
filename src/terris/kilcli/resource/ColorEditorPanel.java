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

package terris.kilcli.resource;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * ColorEditorPanel for KilCli is the class used to represent an<br>
 * individual color in the Color Editor, giving it a label, the color code<br>
 * preview button, and the preview label<br>
 * Ver: 1.0.0
 */

public class ColorEditorPanel extends JPanel {

	private JTextField text1;
	private JTextField text2;
	private JLabel example = new JLabel();


	public ColorEditorPanel(String label, String color) {
		this.setLayout(new GridLayout(1, 4, 0, 1));
		JLabel pix = new JLabel();
		JPanel cell = new JPanel();
		pix = new JLabel(label);
		cell.add(pix);
		this.add(cell);
		cell = new JPanel();
		text1 = new JTextField(6);
		text1.setDocument(new HexDocument(6));
		text1.setText(color);
		cell.add(text1);
		this.add(cell);
		cell = new JPanel();
		JButton preview = new JButton("Preview");
		preview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				example.setText("<html><font color=#" + text1.getText() + ">Example Text");
				repaint();
			}
		});
		cell.add(preview);
		this.add(cell);
		cell = new JPanel();
		example.setText("<html><font color=#" + color + ">Example Text");
		cell.add(example);
		this.add(cell);
	}

	public ColorEditorPanel(String label, String label2, String color, String color2) {
		this.setLayout(new GridLayout(2, 4, 0, 1));
		JLabel pix = new JLabel();
		JPanel cell = new JPanel();
		pix = new JLabel(label);
		cell.add(pix);
		this.add(cell);
		cell = new JPanel();
		text1 = new JTextField(6);
		text1.setDocument(new HexDocument(6));
		text1.setText(color);
		cell.add(text1);
		this.add(cell);
		cell = new JPanel();
		this.add(cell);
		cell = new JPanel();
		this.add(cell);

		cell = new JPanel();
		pix = new JLabel(label2);
		cell.add(pix);
		this.add(cell);
		cell = new JPanel();
		text2 = new JTextField(6);
		text2.setDocument(new HexDocument(6));
		text2.setText(color2);
		cell.add(text2);
		this.add(cell);
		cell = new JPanel();
		JButton preview = new JButton("Preview");
		preview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				example.setText("<html><font style=\"background-color: " + text1.getText() + ";\" color=#" + text2.getText() + ">Example Text");
				repaint();
			}
		});
		cell.add(preview);
		this.add(cell);
		cell = new JPanel();
		example.setText("<html><font style=\"background-color: " + color + ";\" color=#" + color2 + ">Example Text");
		cell.add(example);
		this.add(cell);
	}

	public String getText() {
		return text1.getText();
	}

	public String getText2() {
		return text2.getText();
	}


}
