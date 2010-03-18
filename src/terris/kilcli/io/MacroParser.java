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

package terris.kilcli.io;

import javax.swing.Timer;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import terris.kilcli.*;
import terris.kilcli.thread.*;
import terris.kilcli.resource.*;

/**
 * MacroParser for KilCli is the class used to evalute and execute<br>
 * commands from a macro<br>
 * Ver: 1.0.0 RC2
 */

public class MacroParser {

	private static String storage;
	private static boolean storageSend;
	private static String storage2;

	private static Timer pause = new Timer(250, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			pause.stop();
			parse2(storage, storageSend, true);
		}
	});

	private static Timer pause2 = new Timer(500, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			pause2.stop();
			parse2(storage, storageSend, true);

		}
	});

	private static Timer pause3 = new Timer(1000, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			pause3.stop();
			parse2(storage, storageSend, true);

		}
	});

	/**
	 * Visible macro parsing interface<br>
	 * Give it a string, and it'll do the rest
	 *
	 * @param input - the macro string to be parsed
	 */

	public static void parse(String input) {
		pause.setRepeats(false);
		pause2.setRepeats(false);
		pause3.setRepeats(false);

		//check for the '?' in order to replace
		String replace = "";
		int stringSearchIndex = input.indexOf("?");

		while (stringSearchIndex != -1) {
			if (stringSearchIndex > 0) {
				if (input.charAt(stringSearchIndex-1) != '\\') {
					replace = (String)JOptionPane.showInputDialog(KilCliThread.getKilCli(),"Replace ? with:","Macro ? Replacement",JOptionPane.PLAIN_MESSAGE,null,null,"");
					if (stringSearchIndex+1 != input.length()) {
						input = input.substring(0, stringSearchIndex) + replace + input.substring(stringSearchIndex+1, input.length());
					} else {
						input = input.substring(0, stringSearchIndex) + replace;
					}
					break;
				} else {
					input = input.substring(0, stringSearchIndex-1) + input.substring(stringSearchIndex, input.length());
					stringSearchIndex = input.indexOf("?", stringSearchIndex);
				}
			} else {
				replace = (String)JOptionPane.showInputDialog(KilCliThread.getKilCli(),"Replace ? with:","Macro ? Replacement",JOptionPane.PLAIN_MESSAGE,null,null,"");
				if (stringSearchIndex+1 != input.length()) {
					input = input.substring(0, stringSearchIndex) + replace + input.substring(stringSearchIndex+1, input.length());
				} else {
					input = input.substring(0, stringSearchIndex) + replace;
				}

				break;
			}
		}

		if (stringSearchIndex != -1) {
			stringSearchIndex = input.indexOf("?", stringSearchIndex + replace.length());
			while (stringSearchIndex != -1) {
				if (stringSearchIndex > 0) {
					if (input.charAt(stringSearchIndex-1) != '\\') {
						if (stringSearchIndex+1 != input.length()) {
							input = input.substring(0, stringSearchIndex) + replace + input.substring(stringSearchIndex+1, input.length());
						} else {
							input = input.substring(0, stringSearchIndex) + replace;
						}
					} else {
						input = input.substring(0, stringSearchIndex-1) + input.substring(stringSearchIndex, input.length());
					}
				} else {
					if (stringSearchIndex+1 != input.length()) {
						input = input.substring(0, stringSearchIndex) + replace + input.substring(stringSearchIndex+1, input.length());
					} else {
						input = input.substring(0, stringSearchIndex) + replace;
					}
				}
				stringSearchIndex = input.indexOf("?", stringSearchIndex + replace.length());
			}
		}

		stringSearchIndex = input.indexOf("&");
		while (stringSearchIndex != -1) {
			if ((stringSearchIndex == 0) || (input.charAt(stringSearchIndex-1) != '\\')) {
				String temp = input.substring(0, stringSearchIndex);
				if (stringSearchIndex+1 != input.length()) {
					input = input.substring(stringSearchIndex+1, input.length());
				} else {
					input = "";
				}
				if (parse2(temp, true, false)) {
					storage2 = input;
					return;
				}
				stringSearchIndex = input.indexOf("&");
			} else {
				input = input.substring(0, stringSearchIndex-1) + input.substring(stringSearchIndex, input.length());
				stringSearchIndex = input.indexOf("&", stringSearchIndex);
			}

		}
		if (input.length() > 0) {
			if (parse2(input, false, false)) {
				storage2 = "";
			}
		}
	}

	/**
	 * Private macro parsing interface. Deals with<br>
	 * the delays and sending/appending the text
	 *
	 * @param txt - the string to be analyzed
	 * @param send - if true, sends to game, otherwise appends to command line
	 * @param parse - if true, calls the parse function with the contents of storage2
	 * used after the timer delays
	 */

	private static boolean parse2(String txt, boolean send, boolean parse) {
		if (txt.length() > 0) {

			//check for delays
			if (txt.charAt(0) == '~') {
				txt = txt.substring(1,txt.length());
				storage = txt;
				storageSend = send;
				pause3.start();
				return true;
			}
			if (txt.charAt(0) == '%') {
				txt = txt.substring(1,txt.length());
				storage = txt;
				storageSend = send;
				pause2.start();
				return true;
			}
			if (txt.charAt(0) == '#') {
				if ((txt.indexOf("script") != 1) && (txt.indexOf("kilcli") != 1)) {
					txt = txt.substring(1,txt.length());
					storage = txt;
					storageSend = send;
					pause.start();
					return true;
				}
			}

			if (send) {
				KilCliThread.getKilCli().scriptText(txt);
			} else {
				int stringSearchIndex = txt.indexOf("@");
				while (stringSearchIndex != -1) {
					if ((stringSearchIndex == 0) || (txt.charAt(stringSearchIndex-1) != '\\')) {
						txt = txt.substring(0, stringSearchIndex) + txt.substring(stringSearchIndex+1, txt.length());
						break;
					} else {
						txt = txt.substring(0, stringSearchIndex-1) + txt.substring(stringSearchIndex, txt.length());
					}
					stringSearchIndex = txt.indexOf("@", stringSearchIndex);
				}
				KilCli.append(txt, stringSearchIndex);
			}
		}
		if (parse) {
			if (storage2.length() > 0) {
				parse(storage2);
			}
		}
		return false;
	}
}