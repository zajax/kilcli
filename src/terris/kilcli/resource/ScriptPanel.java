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
 * ScriptPanel for KilCli is the class used to represent the<br>
 * script textfield/button in the Config Editor<br>
 * Ver: 1.0.0
 */

public class ScriptPanel extends JPanel
{
	private JButton browse = new JButton("...");
	private JTextField scriptText = new JTextField(12);
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JPanel jPanel = new JPanel();


    public ScriptPanel(String script) {
		browse.setToolTipText("Browse for script file to run");
		browse.addActionListener(new ActionListener() {
			//function for when someone clicks ...
		    public void actionPerformed(ActionEvent e) {
				browse();
		    }
		});
		jPanel.setLayout(gridBagLayout1);
		scriptText.setText(script);

	    this.add(jPanel);
	    jPanel.add(scriptText, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0 ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	    jPanel.add(browse, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0 ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	}

	public String getScript() {
		return scriptText.getText();
	}

	public void setText(String text) {
		scriptText.setText(text);
	}

	private void browse() {
    	String fileName = "";
    	File scriptsDir = new File("./scripts/");
    	JFileChooser chooser = new JFileChooser();
    	chooser.setCurrentDirectory(scriptsDir);
    	int returnVal = chooser.showOpenDialog(null);
    	if(returnVal == JFileChooser.APPROVE_OPTION) {
           	fileName = chooser.getSelectedFile().getName();
			scriptText.setText(fileName);
    	}
	}

}