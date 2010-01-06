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
import javax.swing.JSlider;
import java.awt.BorderLayout;
import java.awt.GridLayout;
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
import terris.kilcli.resource.*;
import terris.kilcli.thread.*;


/**
 * ConfigEditor for KilCli is the class used to edit the configuration<br>
 * through a gui<br>
 * Ver: 1.0.2
 */

public class ConfigEditor extends JDialog {
    private JScrollPane jScroll = new JScrollPane();
    private JScrollPane jScroll2 = new JScrollPane();
    private JScrollPane jScroll3 = new JScrollPane();
    private JScrollPane jScroll4 = new JScrollPane();
    private JScrollPane jScroll5 = new JScrollPane();
    private JScrollPane jScroll6 = new JScrollPane();
    private JScrollPane jScroll7 = new JScrollPane();
    private JScrollPane jScroll8 = new JScrollPane();
    private JScrollPane jScroll9 = new JScrollPane();
    private JSplitPane	jSplit;
	private JTabbedPane tabbedpane;
	private JTextField echoText;
	private JTextField help;
	private JTextField chats;
	private JTextField events;
	private JTextField gshouts;
	private JTextField hhshouts;
	private JTextField quests;
	private JTextField shouts;
	private JTextField tells;
	private JTextField tshouts;
	private JTextField wails;
	private JTextField logons;
	private JTextField proxyServer;
	private JTextField proxyPort;
	private JTextField startSoundFilename;
	private JTextField soundDir;
	private JTextField proxyUsername;
	private JTextField proxyPassword;
	private KilCliTextField aliasFlag;
	private ScriptPanel startupScript;
	private ScriptPanel logonScript;
	private JComboBox theme = new JComboBox();
	private JComboBox chatsBox = new JComboBox();
	private JComboBox eventsBox = new JComboBox();
	private JComboBox gshoutsBox = new JComboBox();
	private JComboBox hhshoutsBox = new JComboBox();
	private JComboBox questsBox = new JComboBox();
	private JComboBox shoutsBox = new JComboBox();
	private JComboBox tellsBox = new JComboBox();
	private JComboBox tshoutsBox = new JComboBox();
	private JComboBox wailsBox = new JComboBox();
	private JComboBox logonsBox = new JComboBox();
	private JComboBox proxyType = new JComboBox();
	private JComboBox kchatChatBox = new JComboBox();
	private JComboBox kchatTellBox = new JComboBox();
	private JCheckBox logging;
	private JCheckBox triggers;
	private JCheckBox echo;
	private JCheckBox HPWarning;
	private JCheckBox SPWarning;
	private JCheckBox enableSound;
	private JCheckBox startSound;
	private JCheckBox nicknames;
	private JCheckBox proxyEnabled;
	private JCheckBox oldStyleChats;
	private JCheckBox oldStyleCommand;
	private JCheckBox oldStyleEffects;
	private JCheckBox oldStyleEvents;
	private JCheckBox oldStyleGame;
	private JCheckBox oldStyleGShouts;
	private JCheckBox oldStyleHands;
	private JCheckBox oldStyleHHShouts;
	private JCheckBox oldStyleLogons;
	private JCheckBox oldStyleMovement;
	private JCheckBox oldStyleShouts;
	private JCheckBox oldStyleQuests;
	private JCheckBox oldStyleStatus;
	private JCheckBox oldStyleStatusHor;
	private JCheckBox oldStyleTells;
	private JCheckBox oldStyleTimers;
	private JCheckBox oldStyleTShouts;
	private JCheckBox oldStyleWails;
	private JCheckBox oldStyleInfo;
	private JCheckBox oldStyleHarden;
	private JCheckBox monitorHarden;
	private JCheckBox bindPageUpDown;
	private JCheckBox netMelee;
	private JCheckBox chatStamp;
	private JCheckBox gameStamp;
	private JCheckBox gshoutStamp;
	private JCheckBox hhshoutStamp;
	private JCheckBox logonStamp;
	private JCheckBox shoutStamp;
	private JCheckBox tellStamp;
	private JCheckBox tshoutStamp;
	private JCheckBox wailStamp;
	private JCheckBox chatToKchat;
	private JCheckBox numpadEnterSendCommand;
	private JCheckBox connectToKChat;

	private JSlider netBuffer;
	private JSlider textBuffer;
	private JSlider HPWarn;
	private JSlider SPWarn;

	private String[] config;

	public void updateTheme() {
		repaint();
	}

	/**
	 * ConfigEditor constructor, with no arguments creates<br>
	 * a configuration editor window in the default location
	 */

    public ConfigEditor() {
        super(KilCliThread.getKilCli(), "Config Editor", false);
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

		config = KilCli.getOptions();

        tabbedpane = new JTabbedPane();
        tabbedpane.setTabPlacement(JTabbedPane.TOP);
        jSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, tabbedpane, bottom);
        jSplit.setDividerSize(0);

		getContentPane().add(jSplit, BorderLayout.CENTER);

		chatsBox = addItems();
		eventsBox = addItems();
		gshoutsBox = addItems();
		hhshoutsBox = addItems();
		questsBox = addItems();
		shoutsBox = addItems();
		tellsBox = addItems();
		tshoutsBox = addItems();
		wailsBox = addItems();
		logonsBox = addItems();
		kchatTellBox = addItems();
		kchatChatBox = addItems();

		String name = "General Config";
		createGeneral();
		tabbedpane.add(name, jScroll);

		name = "Window Prefixes";
		createWindows();
		tabbedpane.add(name, jScroll2);

		name = "Text Routing";
		createTextRouting();
		tabbedpane.add(name, jScroll5);

		name = "Status Warnings";
		createStatus();
 		tabbedpane.add(name, jScroll3);

		name = "Sound Config";
		createSound();
		tabbedpane.add(name, jScroll4);

		name = "Window Title Bars";
		createTitleBars();
		tabbedpane.add(name, jScroll7);

		name = "Time Stamps";
		createTimeStamps();
		tabbedpane.add(name, jScroll8);

		name = "Proxy Settings";
		createProxy();
		tabbedpane.add(name, jScroll6);

		name = "KilCli Chat Settings";
		createChat();
		tabbedpane.add(name, jScroll9);


		//get screen dimensions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //set tabbed pane visible
		tabbedpane.setVisible(true);

        //...Then set the window size
        setSize((int)(screenSize.width * 0.50), (int)(screenSize.height * 0.40));

        //Set the window's location.
        setLocation((int)(screenSize.width * 0.25), (int)(screenSize.height * 0.25));
        jSplit.setDividerLocation((int)(this.getHeight() - 75));
        setVisible(true);

	}

	/**
	 * Fills out the items for a JComboBox.<br>
	 * Used for text-routing
	 */
	private JComboBox addItems() {
		JComboBox temp = new JComboBox();
		temp.addItem("Chats");
		temp.addItem("Events");
		temp.addItem("Game");
		temp.addItem("GShouts");
		temp.addItem("HHShouts");
		temp.addItem("Logons");
		temp.addItem("Quests");
		temp.addItem("Shouts");
		temp.addItem("Tells");
		temp.addItem("TShouts");
		temp.addItem("Wails");
		return temp;
	}

	/**
	 * Closes this editor
	 */
	private void close() {
		this.dispose();
	}

	/**
	 * Resets the panes to the last saved config
	 */
	private void clear() {
		createGeneral();
		createWindows();
		createStatus();
		createSound();
		createTextRouting();
		createProxy();
	}

	/**
	 * Gets the window letter from the selected item in a text-routing combobox
	 */
	private String checkBox(JComboBox box) {
		if (box.getSelectedIndex() == 0) {
			return "C";
		} else if (box.getSelectedIndex() == 1) {
			return "E";
		} else if (box.getSelectedIndex() == 2) {
			return "g";
		} else if (box.getSelectedIndex() == 3) {
			return "G";
		} else if (box.getSelectedIndex() == 4) {
			return "H";
		} else if (box.getSelectedIndex() == 5) {
			return "L";
		} else if (box.getSelectedIndex() == 6) {
			return "Q";
		} else if (box.getSelectedIndex() == 7) {
			return "M";
		} else if (box.getSelectedIndex() == 8) {
			return "t";
		} else if (box.getSelectedIndex() == 9) {
			return "T";
		} else if (box.getSelectedIndex() == 10) {
			return "W";
		}
		return "g";
	}

	/**
	 * Updates the config based on the info in all the panes
	 */
	private void update() {
		if (startSound.isSelected()) {
			config[0] = "true";
		} else {
			config[0] = "false";
		}
		if (logging.isSelected()) {
			config[1] = "true";
		} else {
			config[1] = "false";
		}
		if (HPWarning.isSelected()) {
			config[2] = "true";
		} else {
			config[2] = "false";
		}
		if (SPWarning.isSelected()) {
			config[3] = "true";
		} else {
			config[3] = "false";
		}
		config[4] = "" + HPWarn.getValue();
		config[5] = "" + SPWarn.getValue();
		config[6] = gshouts.getText();
		config[7] = tshouts.getText();
		config[8] = tells.getText();
		config[9] = hhshouts.getText();
		config[10] = shouts.getText();
		config[11] = chats.getText();
		config[12] = wails.getText();
		config[13] = events.getText();
		config[14] = quests.getText();
		if (oldStyleCommand.isSelected()) {
			config[19] = "true";
		} else {
			config[19] = "false";
		}
		if (triggers.isSelected()) {
			config[21] = "true";
		} else {
			config[21] = "false";
		}
		config[22] = startSoundFilename.getText();
		if (enableSound.isSelected()) {
			config[23] = "true";
		} else {
			config[23] = "false";
		}
		if (echo.isSelected()) {
			config[24] = "true";
		} else {
			config[24] = "false";
		}
		config[25] = echoText.getText();
		config[26] = help.getText();
		config[27] = checkBox(chatsBox);
		config[28] = checkBox(eventsBox);
		config[29] = checkBox(gshoutsBox);
		config[30] = checkBox(hhshoutsBox);
		config[31] = checkBox(shoutsBox);
		config[32] = checkBox(questsBox);
		config[33] = checkBox(tshoutsBox);
		config[34] = checkBox(wailsBox);
		config[35] = checkBox(tellsBox);
		config[36] = logons.getText();
		config[37] = checkBox(logonsBox);
		if (nicknames.isSelected()) {
			config[38] = "true";
		} else {
			config[38] = "false";
		}
		config[39] = soundDir.getText();
		if (proxyEnabled.isSelected()) {
			config[40] = "true";
		} else {
			config[40] = "false";
		}
		config[41] = proxyServer.getText();
		config[42] = proxyPort.getText();
		config[43] = "" + netBuffer.getValue();
		if (oldStyleCommand.isSelected()) {
			config[19] = "true";
		} else {
			config[19] = "false";
		}
		if (oldStyleChats.isSelected()) {
			config[44] = "true";
		} else {
			config[44] = "false";
		}
		if (oldStyleEffects.isSelected()) {
			config[45] = "true";
		} else {
			config[45] = "false";
		}
		if (oldStyleEvents.isSelected()) {
			config[46] = "true";
		} else {
			config[46] = "false";
		}
		if (oldStyleGame.isSelected()) {
			config[47] = "true";
		} else {
			config[47] = "false";
		}
		if (oldStyleGShouts.isSelected()) {
			config[48] = "true";
		} else {
			config[48] = "false";
		}
		if (oldStyleHands.isSelected()) {
			config[49] = "true";
		} else {
			config[49] = "false";
		}
		if (oldStyleHHShouts.isSelected()) {
			config[50] = "true";
		} else {
			config[50] = "false";
		}
		if (oldStyleLogons.isSelected()) {
			config[51] = "true";
		} else {
			config[51] = "false";
		}
		if (oldStyleMovement.isSelected()) {
			config[52] = "true";
		} else {
			config[52] = "false";
		}
		if (oldStyleShouts.isSelected()) {
			config[53] = "true";
		} else {
			config[53] = "false";
		}
		if (oldStyleQuests.isSelected()) {
			config[54] = "true";
		} else {
			config[54] = "false";
		}
		if (oldStyleStatus.isSelected()) {
			config[55] = "true";
		} else {
			config[55] = "false";
		}
		if (oldStyleStatusHor.isSelected()) {
			config[56] = "true";
		} else {
			config[56] = "false";
		}
		if (oldStyleTells.isSelected()) {
			config[57] = "true";
		} else {
			config[57] = "false";
		}
		if (oldStyleTimers.isSelected()) {
			config[58] = "true";
		} else {
			config[58] = "false";
		}
		if (oldStyleTShouts.isSelected()) {
			config[59] = "true";
		} else {
			config[59] = "false";
		}
		if (oldStyleWails.isSelected()) {
			config[60] = "true";
		} else {
			config[60] = "false";
		}
		if (bindPageUpDown.isSelected()) {
			config[61] = "true";
		} else {
			config[61] = "false";
		}
		if (oldStyleInfo.isSelected()) {
			config[62] = "true";
		} else {
			config[62] = "false";
		}
		config[63] = startupScript.getScript();
		config[64] = (String)(proxyType.getSelectedItem());
		config[65] = proxyUsername.getText();
		config[66] = proxyPassword.getText();
		config[67] = "" + textBuffer.getValue();
		config[68] = logonScript.getScript();
		if (netMelee.isSelected()) {
			config[69] = "true";
		} else {
			config[69] = "false";
		}
		if (chatStamp.isSelected()) {
			config[70] = "true";
		} else {
			config[70] = "false";
		}
		if (gameStamp.isSelected()) {
			config[71] = "true";
		} else {
			config[71] = "false";
		}
		if (gshoutStamp.isSelected()) {
			config[72] = "true";
		} else {
			config[72] = "false";
		}
		if (hhshoutStamp.isSelected()) {
			config[73] = "true";
		} else {
			config[73] = "false";
		}
		if (logonStamp.isSelected()) {
			config[74] = "true";
		} else {
			config[74] = "false";
		}
		if (shoutStamp.isSelected()) {
			config[75] = "true";
		} else {
			config[75] = "false";
		}
		if (tellStamp.isSelected()) {
			config[76] = "true";
		} else {
			config[76] = "false";
		}
		if (tshoutStamp.isSelected()) {
			config[77] = "true";
		} else {
			config[77] = "false";
		}
		if (wailStamp.isSelected()) {
			config[78] = "true";
		} else {
			config[78] = "false";
		}
		config[79] = aliasFlag.getText();
		if (oldStyleHarden.isSelected()) {
			config[80] = "true";
		} else {
			config[80] = "false";
		}
		if (monitorHarden.isSelected()) {
			config[81] = "true";
		} else {
			config[81] = "false";
		}

		if (chatToKchat.isSelected()) {
			config[82] = "true";
		} else {
			config[82] = "false";
		}
		if (numpadEnterSendCommand.isSelected()) {
			config[83] = "true";
		} else {
			config[83] = "false";
		}
		if (connectToKChat.isSelected()) {
			config[84] = "true";
		} else {
			config[84] = "false";
		}
		config[85] = checkBox(kchatTellBox);
		config[86] = checkBox(kchatChatBox);
		KilCli.updateOptions(config);
		new JOptionPane().showMessageDialog(this, "Config Updated");
	}

	/**
	 * Creates the window prefixes pane
	 */
	private void createWindows() {
		jScroll2.setVerticalScrollBarPolicy(22);
		JPanel view = new JPanel();
		JPanel cell = new JPanel();
		JLabel pix = new JLabel();
		view.setLayout(new GridLayout(10, 2, 0, 5));
		pix = new JLabel("Chats Prefix:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		chats = new JTextField(10);
		chats.setText(config[11]);
		cell.add(chats);
		view.add(cell);
		cell = new JPanel();
		pix = new JLabel("Events Prefix:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		events = new JTextField(10);
		events.setText(config[13]);
		cell.add(events);
		view.add(cell);
		cell = new JPanel();
		pix = new JLabel("GShouts Prefix:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		gshouts = new JTextField(10);
		gshouts.setText(config[6]);
		cell.add(gshouts);
		view.add(cell);
		cell = new JPanel();
		pix = new JLabel("HHShouts Prefix:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		hhshouts = new JTextField(10);
		hhshouts.setText(config[9]);
		cell.add(hhshouts);
		view.add(cell);
		cell = new JPanel();
		pix = new JLabel("Logons Prefix:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		logons = new JTextField(10);
		logons.setText(config[36]);
		cell.add(logons);
		view.add(cell);
		cell = new JPanel();
		pix = new JLabel("Quests Prefix:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		quests = new JTextField(10);
		quests.setText(config[14]);
		cell.add(quests);
		view.add(cell);
		cell = new JPanel();
		pix = new JLabel("Shouts Prefix:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		shouts = new JTextField(10);
		shouts.setText(config[10]);
		cell.add(shouts);
		view.add(cell);
		cell = new JPanel();
		pix = new JLabel("Tells Prefix:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		tells = new JTextField(10);
		tells.setText(config[8]);
		cell.add(tells);
		view.add(cell);
		cell = new JPanel();
		pix = new JLabel("TShouts Prefix:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		tshouts = new JTextField(10);
		tshouts.setText(config[7]);
		cell.add(tshouts);
		view.add(cell);
		cell = new JPanel();
		pix = new JLabel("Wails Prefix:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		wails = new JTextField(10);
		wails.setText(config[12]);
		cell.add(wails);
		view.add(cell);
		jScroll2.setViewportView(view);
	}

	/**
	 * Creates the General Config pane
	 */
	private void createGeneral() {
		jScroll.setVerticalScrollBarPolicy(22);
		JPanel view = new JPanel();
		JPanel cell = new JPanel();
		JLabel pix = new JLabel();
		view.setLayout(new GridLayout(15, 2, 0, 5));
		pix = new JLabel("Logging Enabled:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		logging = new JCheckBox();
		if (config[1].toLowerCase().equals("true")) {
			logging.setSelected(true);
		}
		cell.add(logging);
		view.add(cell);
		cell = new JPanel();
		pix = new JLabel("Triggers Enabled:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		triggers = new JCheckBox();
		if (config[21].toLowerCase().equals("true")) {
			triggers.setSelected(true);
		}
		cell.add(triggers);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Nicknames Enabled:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		nicknames = new JCheckBox();
		if (config[38].toLowerCase().equals("true")) {
			nicknames.setSelected(true);
		}
		cell.add(nicknames);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Numpad Enter Sends Command:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		numpadEnterSendCommand = new JCheckBox();
		if (config[83].toLowerCase().equals("true")) {
			numpadEnterSendCommand.setSelected(true);
		}
		cell.add(numpadEnterSendCommand);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Monitor for Harden Armour Status:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		monitorHarden = new JCheckBox();
		if (config[81].toLowerCase().equals("true")) {
			monitorHarden.setSelected(true);
		}
		cell.add(monitorHarden);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Alias Flag:");
		pix.setToolTipText("The character used to tell the command parser it is an alias.  Leave blank to parse all");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		aliasFlag = new KilCliTextField(2, false);
		aliasFlag.setText(config[79]);
		aliasFlag.setToolTipText("The character used to tell the command parser it is an alias.  Leave blank to parse all");
		cell.add(aliasFlag);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Bind Page Up/Down to Game Window:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		bindPageUpDown = new JCheckBox();
		if (config[61].equalsIgnoreCase("true")) {
			bindPageUpDown.setSelected(true);
		}
		cell.add(bindPageUpDown);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Show Net Melee Damage:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		netMelee = new JCheckBox();
		if (config[69].equalsIgnoreCase("true")) {
			netMelee.setSelected(true);
		}
		cell.add(netMelee);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Local Echo:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		echo = new JCheckBox();
		if (config[24].toLowerCase().equals("true")) {
			echo.setSelected(true);
		}
		cell.add(echo);
		view.add(cell);
		cell = new JPanel();
		pix = new JLabel("Echo Text:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		echoText = new JTextField(15);
		echoText.setText(config[25]);
		cell.add(echoText);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Startup Script:");
		cell.add(pix);
		view.add(cell);
		startupScript = new ScriptPanel(config[63]);
		view.add(startupScript);

		cell = new JPanel();
		pix = new JLabel("Logon Script:");
		cell.add(pix);
		view.add(cell);
		logonScript = new ScriptPanel(config[68]);
		view.add(logonScript);

		cell = new JPanel();
		pix = new JLabel("Help Address:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		help = new JTextField(25);
		help.setText(config[26]);
		help.setCaretPosition(0);
		cell.add(help);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Read Buffer Size:");
		pix.setToolTipText("<html>The size of the buffer used in reading data packets<br>Try 2048, increase slighly for a fast connection<br>reduce for a modem</html>");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		netBuffer = new JSlider(1024,7168,Integer.parseInt(config[43]));
		netBuffer.setToolTipText("<html>The size of the buffer used in reading data packets<br>Try 2048, increase slighly for a fast connection<br>reduce for a modem</html>");
		netBuffer.setSnapToTicks(true);
		netBuffer.setMinorTickSpacing(192);
		netBuffer.setMajorTickSpacing(1536);
		netBuffer.setPaintTicks(true);
		netBuffer.setPaintLabels(true);
		cell.add(netBuffer);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Window Text Buffer Size:");
		pix.setToolTipText("The max number of characters that can be in any of the display windows");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		textBuffer = new JSlider(20000,80000,Integer.parseInt(config[67]));
		textBuffer.setToolTipText("The max number of characters that can be in any of the display windows");
		textBuffer.setSnapToTicks(true);
		textBuffer.setMinorTickSpacing(5000);
		textBuffer.setMajorTickSpacing(15000);
		textBuffer.setPaintTicks(true);
		textBuffer.setPaintLabels(true);
		cell.add(textBuffer);
		view.add(cell);

		jScroll.setViewportView(view);
	}

	/**
	 * Creates the Status Warnings pane
	 */
	private void createStatus() {
		jScroll3.setVerticalScrollBarPolicy(22);
		JPanel view = new JPanel();
		JPanel cell = new JPanel();
		JLabel pix = new JLabel();
		view.setLayout(new GridLayout(4, 2, 0, 5));
		pix = new JLabel("HP Warning:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		HPWarning = new JCheckBox();
		if (config[2].toLowerCase().equals("true")) {
			HPWarning.setSelected(true);
		}
		cell.add(HPWarning);
		view.add(cell);
		cell = new JPanel();
		pix = new JLabel("HP Warning Level:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		HPWarn = new JSlider(0,60, Integer.parseInt(config[4]));
		HPWarn.setSnapToTicks(true);
		HPWarn.setMinorTickSpacing(5);
		HPWarn.setMajorTickSpacing(15);
		HPWarn.setPaintTicks(true);
		HPWarn.setPaintLabels(true);
		cell.add(HPWarn);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("SP Warning:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		SPWarning = new JCheckBox();
		if (config[3].toLowerCase().equals("true")) {
			SPWarning.setSelected(true);
		}
		cell.add(SPWarning);
		view.add(cell);
		cell = new JPanel();
		pix = new JLabel("SP Warning Level:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		SPWarn = new JSlider(0,60, Integer.parseInt(config[5]));
		SPWarn.setSnapToTicks(true);
		SPWarn.setMinorTickSpacing(5);
		SPWarn.setMajorTickSpacing(15);
		SPWarn.setPaintTicks(true);
		SPWarn.setPaintLabels(true);
		cell.add(SPWarn);
		view.add(cell);

		jScroll3.setViewportView(view);
	}

	/**
	 * Creates the sound config pane
	 */
	private void createSound() {
		jScroll4.setVerticalScrollBarPolicy(22);
		JPanel view = new JPanel();
		JPanel cell = new JPanel();
		JLabel pix = new JLabel();
		view.setLayout(new GridLayout(4, 2, 0, 5));
		pix = new JLabel("Enable Sound Globally:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		enableSound = new JCheckBox();
		if (config[23].toLowerCase().equals("true")) {
			enableSound.setSelected(true);
		}
		cell.add(enableSound);
		view.add(cell);
		cell = new JPanel();
		pix = new JLabel("Sound Folder:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		soundDir = new JTextField(15);
		soundDir.setText(config[39]);
		soundDir.setToolTipText("Requires the trailing '/' or '\\'");
		cell.add(soundDir);
		view.add(cell);
		cell = new JPanel();
		pix = new JLabel("Use Startup Sound:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		startSound = new JCheckBox();
		if (config[0].toLowerCase().equals("true")) {
			startSound.setSelected(true);
		}
		cell.add(startSound);
		view.add(cell);
		cell = new JPanel();
		pix = new JLabel("Start Sound Filename:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		startSoundFilename = new JTextField(15);
		startSoundFilename.setText(config[22]);
		cell.add(startSoundFilename);
		view.add(cell);
		jScroll4.setViewportView(view);
	}

	/**
	 * Creates the Window Title Bars pane
	 */
	private void createTitleBars() {
		jScroll7.setVerticalScrollBarPolicy(22);
		JPanel view = new JPanel();
		JPanel cell = new JPanel();
		JLabel pix = new JLabel();
		view.setLayout(new GridLayout(20, 2, 0, 5));
		cell = new JPanel();
		pix = new JLabel("Old Style Chats Window:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		oldStyleChats = new JCheckBox();
		oldStyleChats.setSelected(config[44].equalsIgnoreCase("true"));
		cell.add(oldStyleChats);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Old Style Command Line:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		oldStyleCommand = new JCheckBox();
		oldStyleCommand.setSelected(config[19].equalsIgnoreCase("true"));
		cell.add(oldStyleCommand);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Old Style Effects Panel:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		oldStyleEffects = new JCheckBox();
		oldStyleEffects.setSelected(config[45].equalsIgnoreCase("true"));
		cell.add(oldStyleEffects);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Old Style Events Window:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		oldStyleEvents = new JCheckBox();
		oldStyleEvents.setSelected(config[46].equalsIgnoreCase("true"));
		cell.add(oldStyleEvents);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Old Style Game Window:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		oldStyleGame = new JCheckBox();
		oldStyleGame.setSelected(config[47].equalsIgnoreCase("true"));
		cell.add(oldStyleGame);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Old Style GShouts Window:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		oldStyleGShouts = new JCheckBox();
		oldStyleGShouts.setSelected(config[48].equalsIgnoreCase("true"));
		cell.add(oldStyleGShouts);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Old Style Hands Panel:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		oldStyleHands = new JCheckBox();
		oldStyleHands.setSelected(config[49].equalsIgnoreCase("true"));
		cell.add(oldStyleHands);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Old Style Harden Armour Panel:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		oldStyleHarden = new JCheckBox();
		oldStyleHarden.setSelected(config[80].equalsIgnoreCase("true"));
		cell.add(oldStyleHarden);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Old Style HHShouts Window:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		oldStyleHHShouts = new JCheckBox();
		oldStyleHHShouts.setSelected(config[50].equalsIgnoreCase("true"));
		cell.add(oldStyleHHShouts);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Old Style Info Panel:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		oldStyleInfo = new JCheckBox();
		oldStyleInfo.setSelected(config[62].equalsIgnoreCase("true"));
		cell.add(oldStyleInfo);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Old Style Logons Window:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		oldStyleLogons = new JCheckBox();
		oldStyleLogons.setSelected(config[51].equalsIgnoreCase("true"));
		cell.add(oldStyleLogons);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Old Style Movement Panel:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		oldStyleMovement = new JCheckBox();
		oldStyleMovement.setSelected(config[52].equalsIgnoreCase("true"));
		cell.add(oldStyleMovement);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Old Style Shouts Window:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		oldStyleShouts = new JCheckBox();
		oldStyleShouts.setSelected(config[53].equalsIgnoreCase("true"));
		cell.add(oldStyleShouts);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Old Style Quests Window:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		oldStyleQuests = new JCheckBox();
		oldStyleQuests.setSelected(config[54].equalsIgnoreCase("true"));
		cell.add(oldStyleQuests);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Old Style Status Bar:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		oldStyleStatus = new JCheckBox();
		oldStyleStatus.setSelected(config[55].equalsIgnoreCase("true"));
		cell.add(oldStyleStatus);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Old Style Horizontal Status Bar:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		oldStyleStatusHor = new JCheckBox();
		oldStyleStatusHor.setSelected(config[56].equalsIgnoreCase("true"));
		cell.add(oldStyleStatusHor);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Old Style Tells Window:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		oldStyleTells = new JCheckBox();
		oldStyleTells.setSelected(config[57].equalsIgnoreCase("true"));
		cell.add(oldStyleTells);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Old Style Timers Panel:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		oldStyleTimers = new JCheckBox();
		oldStyleTimers.setSelected(config[58].equalsIgnoreCase("true"));
		cell.add(oldStyleTimers);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Old Style TShouts Window:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		oldStyleTShouts = new JCheckBox();
		oldStyleTShouts.setSelected(config[59].equalsIgnoreCase("true"));
		cell.add(oldStyleTShouts);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Old Style Wails Window:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		oldStyleWails = new JCheckBox();
		oldStyleWails.setSelected(config[60].equalsIgnoreCase("true"));
		cell.add(oldStyleWails);
		view.add(cell);
		jScroll7.setViewportView(view);
	}

	/**
	 * Sets the text-routing combo box to have the correct item selected
	 */
	private int windowBoxIndexSelection(char window) {
		if (window == 'C') {
			return 0;
		} else if (window == 'E') {
			return 1;
		} else if (window == 'g') {
			return 2;
		} else if (window == 'G') {
			return 3;
		} else if (window == 'H') {
			return 4;
		} else if (window == 'L') {
			return 5;
		} else if (window == 'Q') {
			return 6;
		} else if (window == 'M') {
			return 7;
		} else if (window == 't') {
			return 8;
		} else if (window == 'T') {
			return 9;
		} else if (window == 'W') {
			return 10;
		}
		return 2;
	}

	/**
	 * Creates the Time Stamps pane
	 */
	private void createTimeStamps() {
		jScroll8.setVerticalScrollBarPolicy(22);
		JPanel view = new JPanel();
		JPanel cell = new JPanel();
		JLabel pix = new JLabel();
		view.setLayout(new GridLayout(9, 2, 0, 5));
		pix = new JLabel("Chat Window Time Stamp:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		chatStamp = new JCheckBox();
		if (config[70].toLowerCase().equals("true")) {
			chatStamp.setSelected(true);
		}
		cell.add(chatStamp);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Game Window Time Stamp:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		gameStamp = new JCheckBox();
		if (config[71].toLowerCase().equals("true")) {
			gameStamp.setSelected(true);
		}
		cell.add(gameStamp);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("GShouts Window Time Stamp:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		gshoutStamp = new JCheckBox();
		if (config[72].toLowerCase().equals("true")) {
			gshoutStamp.setSelected(true);
		}
		cell.add(gshoutStamp);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("HHShouts Window Time Stamp:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		hhshoutStamp = new JCheckBox();
		if (config[73].toLowerCase().equals("true")) {
			hhshoutStamp.setSelected(true);
		}
		cell.add(hhshoutStamp);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Logons Window Time Stamp:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		logonStamp = new JCheckBox();
		if (config[74].toLowerCase().equals("true")) {
			logonStamp.setSelected(true);
		}
		cell.add(logonStamp);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Shouts Window Time Stamp:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		shoutStamp = new JCheckBox();
		if (config[75].toLowerCase().equals("true")) {
			shoutStamp.setSelected(true);
		}
		cell.add(shoutStamp);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Tells Window Time Stamp:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		tellStamp = new JCheckBox();
		if (config[76].toLowerCase().equals("true")) {
			tellStamp.setSelected(true);
		}
		cell.add(tellStamp);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("TShouts Window Time Stamp:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		tshoutStamp = new JCheckBox();
		if (config[77].toLowerCase().equals("true")) {
			tshoutStamp.setSelected(true);
		}
		cell.add(tshoutStamp);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Wails Window Time Stamp:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		wailStamp = new JCheckBox();
		if (config[78].toLowerCase().equals("true")) {
			wailStamp.setSelected(true);
		}
		cell.add(wailStamp);
		view.add(cell);

		jScroll8.setViewportView(view);
	}

	/**
	 * Creates the text routing pane
	 */
	private void createTextRouting() {
		jScroll5.setVerticalScrollBarPolicy(22);
		JPanel view = new JPanel();
		JPanel cell = new JPanel();
		JLabel pix = new JLabel();
		view.setLayout(new GridLayout(10, 2, 0, 0));
		pix = new JLabel("Chats Window Text:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		cell.add(chatsBox);
		chatsBox.setSelectedIndex(windowBoxIndexSelection(config[27].charAt(0)));
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Events Window Text:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		cell.add(eventsBox);
		eventsBox.setSelectedIndex(windowBoxIndexSelection(config[28].charAt(0)));
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("GShouts Window Text:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		cell.add(gshoutsBox);
		gshoutsBox.setSelectedIndex(windowBoxIndexSelection(config[29].charAt(0)));

		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("HHShouts Window Text:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		cell.add(hhshoutsBox);
		hhshoutsBox.setSelectedIndex(windowBoxIndexSelection(config[30].charAt(0)));
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Logons Window Text:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		cell.add(logonsBox);
		logonsBox.setSelectedIndex(windowBoxIndexSelection(config[37].charAt(0)));
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Quests Window Text:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		cell.add(questsBox);
		questsBox.setSelectedIndex(windowBoxIndexSelection(config[32].charAt(0)));
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Shouts Window Text:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		cell.add(shoutsBox);
		shoutsBox.setSelectedIndex(windowBoxIndexSelection(config[31].charAt(0)));
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Tells Window Text:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		cell.add(tellsBox);
		tellsBox.setSelectedIndex(windowBoxIndexSelection(config[35].charAt(0)));
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("TShouts Window Text:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		cell.add(tshoutsBox);
		tshoutsBox.setSelectedIndex(windowBoxIndexSelection(config[33].charAt(0)));
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Wails Window Text:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		cell.add(wailsBox);
		wailsBox.setSelectedIndex(windowBoxIndexSelection(config[34].charAt(0)));
		view.add(cell);

		jScroll5.setViewportView(view);
	}

	/**
	 * Creates the Proxy pane
	 */
	private void createProxy() {
		jScroll6.setVerticalScrollBarPolicy(22);
		JPanel view = new JPanel();
		JPanel cell = new JPanel();
		view.add(cell);
		view.add(cell);
		JLabel pix = new JLabel();
		view.setLayout(new GridLayout(7, 2, 0, 5));
		pix = new JLabel("Enable Proxy:");
		pix.setToolTipText("Requires a reconnect to enable/disable proxy use");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		proxyEnabled = new JCheckBox();
		proxyEnabled.setToolTipText("Requires a reconnect to enable/disable proxy use");
		if (config[40].toLowerCase().equals("true")) {
			proxyEnabled.setSelected(true);
		}
		cell.add(proxyEnabled);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Proxy Type:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		proxyType = new JComboBox();
		proxyType.addItem("Socks4/4A");
		proxyType.addItem("Socks5");
		proxyType.addItem("HTTP Tunnel");
		proxyType.setSelectedItem(config[64]);
		cell.add(proxyType);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Proxy Server:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		proxyServer = new JTextField(13);
		proxyServer.setText(config[41]);
		cell.add(proxyServer);
		view.add(cell);


		cell = new JPanel();
		pix = new JLabel("Proxy Port:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		proxyPort = new JTextField(5);
		proxyPort.setText(config[42]);
		cell.add(proxyPort);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Proxy Username:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		proxyUsername = new JTextField(13);
		proxyUsername.setText(config[65]);
		cell.add(proxyUsername);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Proxy Password:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		proxyPassword = new JTextField(13);
		proxyPassword.setText(config[66]);
		cell.add(proxyPassword);
		view.add(cell);

		jScroll6.setViewportView(view);
	}

	/**
	 * Creates the Proxy pane
	 */
	private void createChat() {
		jScroll9.setVerticalScrollBarPolicy(22);
		JPanel view = new JPanel();
		JPanel cell = new JPanel();
		view.add(cell);
		view.add(cell);
		JLabel pix = new JLabel();
		view.setLayout(new GridLayout(4, 2, 0, 5));
		pix = new JLabel("Route Chat commands to Kchats:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		chatToKchat = new JCheckBox();
		chatToKchat.setToolTipText("Sends \"chat <message>\" commands to the KilCli Chat Server");
		if (config[82].equalsIgnoreCase("true")) {
			chatToKchat.setSelected(true);
		}
		cell.add(chatToKchat);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Connect to KChats on Login:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		connectToKChat = new JCheckBox();
		if (config[84].toLowerCase().equals("true")) {
			connectToKChat.setSelected(true);
		}
		cell.add(connectToKChat);
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Send KChat Tell to:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		cell.add(kchatTellBox);
		kchatTellBox.setSelectedIndex(windowBoxIndexSelection(config[85].charAt(0)));
		view.add(cell);

		cell = new JPanel();
		pix = new JLabel("Send KChat Chat to:");
		cell.add(pix);
		view.add(cell);
		cell = new JPanel();
		cell.add(kchatChatBox);
		kchatChatBox.setSelectedIndex(windowBoxIndexSelection(config[86].charAt(0)));
		view.add(cell);

		jScroll9.setViewportView(view);
	}
}
