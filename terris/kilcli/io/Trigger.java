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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import terris.kilcli.thread.*;
import terris.kilcli.*;
import terris.kilcli.resource.*;

/**
 * Trigger for KilCli is the class used to load, check for, and<br>
 * execute triggers.<br>
 * Ver: 1.0.1
 */

public class Trigger {

	private static ArrayList triggers = new ArrayList();
	private static String slash = System.getProperty("file.separator");
	private static int count = 0;
	private static int[][] targets = new int[1][1];
	private static boolean listLoaded = false;
	private static String tempString = "";
	private static int searchIndex = 0;
	private static boolean analyzeInventory;
	private static boolean reset = false;
	private static final BASE64Decoder decoder  = new BASE64Decoder();
	private static ArrayList encryptedTells = new ArrayList();

	public static void load(String profile) throws IOException {
		String line = "";
		triggers.clear();
		File srcFile = new File("config" + slash + profile + slash + "triggers.txt");
		if (!srcFile.exists())
		{
			//display some kind of message that the file doesn't exist
		} else if (!srcFile.isFile() || !srcFile.canRead())
        {
			//display error that it can't read from the file
		} else {
			count = 0;
			try {
				BufferedReader inFile = new BufferedReader(new FileReader(srcFile));
    			int tmp = 0;
    			int tmp2 = 0;
    			while (inFile.ready()) {
					try {
						inFile.readLine();
						tmp++;
					} catch (IOException ioe2) {}
				}
				inFile.close();
				targets = new int[tmp][5];
				inFile = new BufferedReader(new FileReader(srcFile));
				tmp2 = 0;
    			while (inFile.ready()) {
					try {
						line = inFile.readLine();
					} catch (IOException ioe) {}
    				StringTokenizer tokenizer = new StringTokenizer(line, "|");
    				triggers.add(tokenizer.nextToken());
    				triggers.add(tokenizer.nextToken());
    				triggers.add(tokenizer.nextToken());
    				triggers.add(tokenizer.nextToken());
					if (tokenizer.hasMoreTokens()) {
						while (tmp2 < 5) {
							if (tokenizer.hasMoreTokens()) {
								try {
									targets[count][tmp2] = Integer.parseInt(tokenizer.nextToken());
								} catch (Exception e) {
									targets[count][tmp2] = -1;
								}
							} else {
								targets[count][tmp2] = -1;
							}
							tmp2++;
						}
					} else {
						targets[count][tmp2] = -1;
					}
					count++;
					tmp2 = 0;
				}
			} catch (FileNotFoundException fnfe) {}
		}
	}

	private static String stripCarots(String text) {
		if (text.charAt(0) == '[') {
			text = text.substring(3, text.length());
		}
		int stringSearch = text.indexOf("^");
		while (stringSearch != -1) {
			text = text.substring(0, stringSearch) + text.substring(stringSearch + 2, text.length());
			stringSearch = text.indexOf("^", stringSearch);
		}
		return text;
	}

	public static void setAnalyzeInventory(boolean ai) {
		analyzeInventory = ai;
		reset = ai;
	}

	public static void analyze(String text) {

		if (text == null) {
			return;
		}
		if (KilCli.getCheckHarden()) {
			checkHardenArmour(text);
		}
		text = encryptAnalyze(text);
		count = 0;

		String[] targetString = new String[5];
		for (int i = 0; i < triggers.size(); i = i + 4) {
			boolean found = false;
			tempString = (String)(triggers.get(i));
			if (tempString.toLowerCase().equals("true")) {
				tempString = (String)(triggers.get(i+1));
				if (tempString.toLowerCase().equals("true")) {
					tempString = (String)(triggers.get(i+2));
					if (tempString.charAt(tempString.length() - 1) == '*') {
						found = text.startsWith(tempString.substring(0, tempString.length() - 2));
					} else {
						found = (text.indexOf(tempString) != -1);
					}
					if (found) {
						//we found the trigger
						tempString = (String)(triggers.get(i+3));
						i = triggers.size();

						if (targets[count][0] != -1) {
							StringTokenizer tokenizer = new StringTokenizer(text);

							int tokenCount = 0;
							String token;
							while (tokenizer.hasMoreTokens()) {
								token = tokenizer.nextToken();
								for (int k = 0; k < 5; k++) {
									if (targets[count][k] == tokenCount) {
										targetString[k] = stripCarots(token);
									}
								}
								tokenCount++;
							}

							new ScriptExecuteThread(tempString, new Script(), targetString).start();
						} else {
							new ScriptExecuteThread(tempString, new Script()).start();
						}
					}
				} else {
					tempString = (String)(triggers.get(i+2));
					tempString = tempString.toLowerCase();
					if (tempString.charAt(tempString.length() - 1) == '*') {
						found = text.toLowerCase().startsWith(tempString.substring(0, tempString.length() - 2));
					} else {
						found = (text.toLowerCase().indexOf(tempString) != -1);
					}
					if (found) {
						//we found the trigger
						tempString = (String)(triggers.get(i+3));
						i = triggers.size();

						if (targets[count][0] != -1) {
							StringTokenizer tokenizer = new StringTokenizer(text);

							int tokenCount = 0;
							String token;
							while (tokenizer.hasMoreTokens()) {
								token = tokenizer.nextToken();
								for (int k = 0; k < 5; k++) {
									if ((targets[count][k] == tokenCount) && (tokenizer.hasMoreTokens())) {
										targetString[k] = stripCarots(token);
									}
								}
								tokenCount++;
							}

							new ScriptExecuteThread(tempString, new Script(), targetString).start();
						} else {
							new ScriptExecuteThread(tempString, new Script()).start();
						}
					}
				}
			}
			count++;
		}
	}

	private static void checkHardenArmour(String text) {
		if (analyzeInventory) {
			if (reset) {
				KilCli.getHarden().resetMagicCount();
				reset = false;
			}
			searchIndex = text.indexOf("in your left hand");
			int endIndex = 0;
			if (searchIndex > -1) {
				endIndex = text.lastIndexOf("You are carrying a ", searchIndex);
				String name = text.substring(endIndex+21, searchIndex);
				name = name.substring(0, name.indexOf("^Y(")-1);
				KilCli.checkItemForHarden(name);
			}

			searchIndex = text.indexOf("Your ^Yarmour^N consists of:");
			if (searchIndex > -1) {
				endIndex = text.indexOf("Your ^YJewelry^N consists of:", searchIndex);
				StringTokenizer st = new StringTokenizer(text.substring(searchIndex+28, endIndex), ",");
				String temp;
				while (st.hasMoreTokens()) {
					temp = st.nextToken().trim();
					if (temp.length() > 11) {
						temp = temp.substring(2, temp.indexOf("^Y(")-1).trim();
						if (temp.startsWith("<br>")) {
							temp = temp.substring(4, temp.length()).trim();
						}
						if (temp.startsWith("^G")) {
							temp = temp.substring(2, temp.length());
						}
						KilCli.checkItemForHarden(temp);
					}
				}


			}
			searchIndex = text.indexOf("Your key ring contains:");
			if (searchIndex > -1) {
				analyzeInventory = false;
			}
		}
		searchIndex = text.indexOf("You feel your ^C");
		while (searchIndex > -1) {
			//System.out.println("You feel detected");
			String temp = text.substring(searchIndex+16, text.indexOf("^N lose its", searchIndex));
			if (!temp.equals(KilCli.getRight())) {
				//System.out.println("should remove:" + temp);
				//something lost harden

				KilCli.getHarden().decrementMagicCount(temp);
			}
			searchIndex = text.indexOf("You feel your ^C", searchIndex+50);
		}

	}

	private static String encryptAnalyze(String input) {
		if ((input.startsWith("[t]^G")) && (input.indexOf("^N Tells you: '") > -1)) {
			int nameIndex = input.indexOf("^N");
			int endEncryptIndex = 0;
			int nameSearch = 0;
			if (input.indexOf("|e*|") > 0) {
				endEncryptIndex = input.indexOf("|*e|'");
				//entire message in this tell
				if (endEncryptIndex > 0) {
					input = input.substring(0, nameIndex+14) + "E* - '" + fromBase64(input.substring(nameIndex+19, endEncryptIndex)) + "'";
					input = KilCli.characterTranslate(input);
				} else {
					//encrypted message overflow
					//get name, save it, get message, save it
					encryptedTells.add(input.substring(2, nameIndex));
					encryptedTells.add(input.substring(nameIndex+19, input.length() - 1));
					input = input.substring(0, nameIndex+15) + "&lt;part of encrypted message>'";
				}
			} else if ((nameIndex > 2) && (encryptedTells.indexOf(input.substring(2, nameIndex)) > -1)) {
				nameSearch = encryptedTells.indexOf(input.substring(2, nameIndex));
				endEncryptIndex = input.indexOf("|*e|'");
				if (endEncryptIndex > 0) {
					input = input.substring(0, nameIndex+14) + "E* - '" + fromBase64((String)(encryptedTells.get(nameSearch+1)) + input.substring(nameIndex+16, endEncryptIndex)) + "'";
					input = KilCli.characterTranslate(input);
					//delete the stored info
					//the tell has ended
					encryptedTells.remove(nameSearch);
					encryptedTells.remove(nameSearch);
				} else {
					//if the tell didn't end, then it didn't match the protocol we created
					//so delete what is in the buffer and output an error message.
					encryptedTells.remove(nameSearch);
					encryptedTells.remove(nameSearch);
				}
			}
		}
		return input;
	}

    private static String fromBase64(String s) {
        return new String(decoder.decodeBuffer(s));
    }

	public static void enable(int t, String e) {
		triggers.set((t-1) * 4, e);
	}

	public static String print() {
		String trig = "";
		int tmp = 1;
		for (int i = 0; i < triggers.size(); i = i + 4) {
			trig = trig + "<b>" + tmp + ". Trigger Text:</b> " + triggers.get(i+2) + " ^N<b>Enabled:</b> " + triggers.get(i) + ", <b>Case Sensitive:</b> " + triggers.get(i+1) + ", <b>Script:</b> " + triggers.get(i+3) + "<br>";
			tmp++;
		}
		return trig;
	}

	public static ArrayList getTriggers() {
		return triggers;
	}

	public static int[][] getTargets() {
		return targets;
	}
}