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
import java.lang.StringBuffer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import terris.kilcli.thread.*;
import terris.kilcli.*;

/**
 * Squelch for KilCli is the class used to check for, and<br>
 * squelch other players<br>
 * Ver: 1.0.0
 */

public class Squelch {

	private static ArrayList squelches = new ArrayList();
	private static int[] squelchCount;
	private static String slash = System.getProperty("file.separator");
	private static int count = 0;

	/**
	 * Loads the info from squelches.txt
	 * @param profile, the profile to load from
	 */
	public static void load(String profile) throws IOException {
		String line = "";
		squelches.clear();
		File srcFile = new File("config" + slash + profile + slash + "squelches.txt");
		if (!srcFile.exists()) {
			//display some kind of message that the file doesn't exist
		} else if (!srcFile.isFile() || !srcFile.canRead()) {
			//display error that it can't read from the file
		} else {
			count = 0;
			try {
				BufferedReader inFile = new BufferedReader(new FileReader(srcFile));
				inFile = new BufferedReader(new FileReader(srcFile));
    			while (inFile.ready()) {
					try {
						line = inFile.readLine();
					} catch (IOException ioe) {}
    				StringTokenizer tokenizer = new StringTokenizer(line, "|");
					if (tokenizer.hasMoreTokens()) {
		    			squelches.add(tokenizer.nextToken());
					} else {
						squelches.add(null);
					}
					if (tokenizer.hasMoreTokens()) {
						String temp = tokenizer.nextToken();
		    			if (temp.equals("null")) {
							squelches.add(null);
						} else {
							squelches.add(temp);
						}
					} else {
						squelches.add(null);
					}
					if (tokenizer.hasMoreTokens()) {
		    			squelches.add(tokenizer.nextToken());
					} else {
						squelches.add(null);
					}
					count++;
				}
			} catch (FileNotFoundException fnfe) {System.err.println(fnfe);}
		}
		int[] tempCount = new int[count];
		if (squelchCount != null) {
			int size = Math.min(tempCount.length, squelchCount.length);
			System.arraycopy(squelchCount, 0, tempCount, 0, size);
		}
		squelchCount = tempCount;

	}

	/**
	 * Analyzes a string for any squelches<br>
	 * and takes appropriate actions when needed
	 * @param text - the string to be analyzed
	 * @return String - the new version of of the incoming text
	 */
	public static String analyze(String text, boolean canReply) {
		if (text == null) {
			return text;
		}
		StringBuffer temp = new StringBuffer(text);
		String word;

		if ((squelches.size() > 1) && ((squelches.size() % 3) == 0)) {
			for (int i = 0; i < squelches.size(); i+=3) {
				word = (String)(squelches.get(i));
				if (canReply) {
					temp = squelchFilterSearch(temp, word, (String)(squelches.get(i+1)), (String)(squelches.get(i+2)), 0, i/3);
				} else {
					temp = squelchFilterSearch(temp, word, (String)(squelches.get(i+1)), (String)(squelches.get(i+2)), 0, -1);
				}
			}
		}

		return temp.toString();
	}

	/**
	 * Filter search to remove the entire line
	 */
	 private static StringBuffer squelchFilterSearch(StringBuffer text, String word, String replace, String reply, int start, int squelchNum) {
		int start2 = -1;
		try {
			if ((text.length() + start + 4) > word.length()) {
				//take a page from boyer-moore and analyze from the back
				for(int i = word.length(); i > -1; i--) {
					if (i == word.length()) {
						if (text.charAt(start + i + 2) != '^') {
							i = -1;
						}
					} else if (text.charAt(start + i + 2) != word.charAt(i)) {
						i = -1;
					} else {
						if (i == 0) {
							start2 = text.indexOf("<br>", start);
							if (start2 == -1) {
								text.delete(start, text.length());
								text.insert(start, replace);
							} else {
								text.delete(start, start2);
								text.insert(start, replace);
							}
							if ((reply.equalsIgnoreCase("true")) && (squelchNum != -1)) {
								//send reply
								if (squelchCount[squelchNum] < 5) {
									//increment counter
									squelchCount[squelchNum] = squelchCount[squelchNum] + 1;
									KilCli.checkSendText("tell " + word + " You are currently being squelched. Warnings left: " + (5 - squelchCount[squelchNum]));
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			//shouldn't be out of bounds, call this strange
		}
		return text;
	 }

	/**
	 * Returns the ArrayList containing the squelch info
	 * @return ArrayList containing squelches as loaded by load()
	 */
	public static ArrayList getSquelches() {
		return squelches;
	}

}