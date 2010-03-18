/* KilCli, an OGC mud client program
 * Copyright (C) 2002, 2003 Jason Baumeister
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

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import terris.kilcli.KilCli;


/**
 * HighlightSoundPanel for KilCli is the class used to represent the<br>
 * sound textfield/button in the Highlight Editor<br>
 * Ver: 1.0.0 RC2
 */

public class HighlightSoundPanel extends JPanel
{
	private JButton browse = new JButton("...");
	private JTextField soundText = new JTextField(12);
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JPanel jPanel = new JPanel();


    public HighlightSoundPanel(String sound) {
		browse.setToolTipText("Browse for sound file to play");
		browse.addActionListener(new ActionListener() {
			//function for when someone clicks ...
		    public void actionPerformed(ActionEvent e) {
				browse();
		    }
		});
		jPanel.setLayout(gridBagLayout1);
		soundText.setText(sound);

	    this.add(jPanel);
	    jPanel.add(soundText, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0 ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	    jPanel.add(browse, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0 ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	}

	public String getSound() {
		return soundText.getText();
	}

	public void setText(String text) {
		soundText.setText(text);
	}

	private void browse() {
    	String fileName = "";
    	File soundDir = new File(KilCli.getSoundDir());
    	JFileChooser chooser = new JFileChooser();
    	String[] tempArray = new String[4];
    	tempArray[0] = "au";
    	tempArray[1] = "mp3";
    	tempArray[2] = "ogg";
    	tempArray[3] = "wav";
    	ExampleFileFilter filter = new ExampleFileFilter(tempArray);
    	filter.setDescription("Sound Files");
    	chooser.setCurrentDirectory(soundDir);
    	chooser.setFileFilter(filter);
    	int returnVal = chooser.showOpenDialog(null);
    	if(returnVal == JFileChooser.APPROVE_OPTION) {
           	fileName = chooser.getSelectedFile().getName();
			soundText.setText(fileName);
    	}
	}

}