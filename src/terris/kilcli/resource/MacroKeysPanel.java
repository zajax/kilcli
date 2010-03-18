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

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JPanel;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.GridBagConstraints;


/**
 * MacroKeysPanel for KilCli is the class used to represent the keys<br>
 * for a macro in the Macro Editor GUI<br>
 * Ver: 1.0.0 RC2
 */

public class MacroKeysPanel extends JPanel
{
    private JCheckBox altCheck = new JCheckBox();
    private JCheckBox ctrlCheck = new JCheckBox();
    private JCheckBox metaCheck = new JCheckBox();
    private JCheckBox shiftCheck = new JCheckBox();
	private JComboBox keysBox;
	private JTextField keysText = new JTextField(8);
	private ArrayList keys = new ArrayList();
	private ArrayList modifiers = new ArrayList();
	private String temp = "";
	private int modifiersIndex = -1;
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JPanel jPanel = new JPanel();


    public MacroKeysPanel(String macro) {
		modifiers.add("alt");
		modifiers.add("control");
		modifiers.add("meta");
		modifiers.add("shift");
		altCheck.setToolTipText("Alt");
		ctrlCheck.setToolTipText("Control");
		metaCheck.setToolTipText("Meta/Apple Key");
		shiftCheck.setToolTipText("Shift");
		StringTokenizer st = new StringTokenizer(macro);
		while (st.hasMoreTokens()) {
			temp = st.nextToken();
			modifiersIndex = modifiers.indexOf(temp);
			if (modifiersIndex == 0) {
				altCheck.setSelected(true);
			} else if (modifiersIndex == 1) {
				ctrlCheck.setSelected(true);
			} else if (modifiersIndex == 2) {
				metaCheck.setSelected(true);
			} else if (modifiersIndex == 3) {
				shiftCheck.setSelected(true);
			} else {
				keysText.setText(temp);
				break;
			}
		}
		this.setLayout(gridBagLayout1);
    	this.add(altCheck, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    	this.add(ctrlCheck, new GridBagConstraints(1, 0, 1, 2, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    	this.add(metaCheck, new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    	this.add(shiftCheck, new GridBagConstraints(3, 0, 1, 2, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    	jPanel.add(keysText);
    	this.add(jPanel, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	}

	public String getMacro() {
		String macro = "";
		if (altCheck.isSelected()) {
			macro = "alt ";
		}
		if (ctrlCheck.isSelected()) {
			macro += "control ";
		}
		if (metaCheck.isSelected()) {
			macro += "meta ";
		}
		if (shiftCheck.isSelected()) {
			macro += "shift ";
		}
		macro += keysText.getText().toUpperCase();
		return macro;
	}

	public String getText() {
		return keysText.getText();
	}

	public void setText(String text) {
		keysText.setText(text);
	}

}