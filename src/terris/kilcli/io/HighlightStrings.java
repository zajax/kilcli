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
 * HighlightStrings for KilCli is the class used to load, check for, and<br>
 * display highlight strings., and it controls the filter/replace text options<br>
 * Ver: 1.0.2
 */

public class HighlightStrings {

	private static ArrayList highlights = new ArrayList();
	private static ArrayList filters = new ArrayList();
	private static String slash = System.getProperty("file.separator");
	private static int count = 0;
	private static boolean listLoaded = false;

	/**
	 * Loads the info from highlights.txt and filters.txt
	 * @param profile, the profile to load from
	 */
	public static void load(String profile) throws IOException {
		String line = "";
		highlights.clear();
		filters.clear();
		File srcFile = new File("config" + slash + profile + slash + "highlights.txt");
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
				inFile = new BufferedReader(new FileReader(srcFile));
    			while (inFile.ready()) {
					try {
						line = inFile.readLine();
					} catch (IOException ioe) {}
    				StringTokenizer tokenizer = new StringTokenizer(line, "|");
					if (tokenizer.hasMoreTokens()) {
		    			highlights.add(tokenizer.nextToken());
					} else {
						highlights.add(null);
					}
					if (tokenizer.hasMoreTokens()) {
		    			highlights.add(tokenizer.nextToken());
					} else {
						highlights.add(null);
					}
					if (tokenizer.hasMoreTokens()) {
		    			highlights.add(tokenizer.nextToken());
					} else {
						highlights.add(null);
					}
					if (tokenizer.hasMoreTokens()) {
		    			highlights.add(tokenizer.nextToken());
					} else {
						highlights.add(null);
					}
    				if (tokenizer.hasMoreTokens()) {
    					highlights.add(tokenizer.nextToken());
					} else {
						highlights.add(null);
					}
					count++;
				}
			} catch (FileNotFoundException fnfe) {System.err.println(fnfe);}
		}

		srcFile = new File("config" + slash + profile + slash + "filters.txt");
		if (!srcFile.exists()) {
			//display some kind of message that the file doesn't exist
		} else if (!srcFile.isFile() || !srcFile.canRead()) {
			//display error that it can't read from the file
		} else {
			count = 0;
			try {
				BufferedReader inFile = new BufferedReader(new FileReader(srcFile));
				inFile = new BufferedReader(new FileReader(srcFile));
		    	String temp;
		    	while (inFile.ready()) {
					try {
						line = inFile.readLine();
					} catch (IOException ioe) {}
		    		StringTokenizer tokenizer = new StringTokenizer(line, "|");
					if (tokenizer.hasMoreTokens()) {
		    			filters.add(tokenizer.nextToken());
					} else {
						filters.add(null);
					}
					if (tokenizer.hasMoreTokens()) {
    					temp = tokenizer.nextToken();
    					if (!temp.equals("null")) {
    						filters.add(temp);
						} else {
							filters.add("");
						}
					} else {
						filters.add(null);
					}
					if (tokenizer.hasMoreTokens()) {
		    			filters.add(tokenizer.nextToken());
					} else {
						filters.add(null);
					}
					if (tokenizer.hasMoreTokens()) {
		    			filters.add(tokenizer.nextToken());
					} else {
						filters.add(null);
					}
		    		if (tokenizer.hasMoreTokens()) {
		    			filters.add(tokenizer.nextToken());
					} else {
						filters.add(null);
					}
					count++;
				}
			} catch (FileNotFoundException fnfe) {System.err.println(fnfe);}
		}

	}

	/**
	 * Analyzes a string for any highlights or filters<br>
	 * and takes appropriate actions when needed
	 * @param text - the string to be analyzed
	 * @return String - the new version of of the incoming text
	 */
	public static String analyze(String text) {
		if (text == null) {
			return text;
		}
		StringBuffer temp = new StringBuffer(text);
		String word;

		if ((highlights.size() > 1) && ((highlights.size() % 5) == 0)) {
			for (int i = 0; i < highlights.size(); i+=5) {
				temp = continueHighlightSearch(temp, (String)(highlights.get(i)), (String)(highlights.get(i+1)), (String)(highlights.get(i+2)), (String)(highlights.get(i+3)), (String)(highlights.get(i+4)), 0);
			}
		}

		if ((filters.size() > 1) && ((filters.size() % 5) == 0)) {
			for (int i = 0; i < filters.size(); i += 5) {
				temp = continueFilterSearch(temp, (String)(filters.get(i)), (String)(filters.get(i+1)), (String)(filters.get(i+2)), (String)(filters.get(i+3)), (String)(filters.get(i+4)), 0);

			}
		}
		return temp.toString();
	}

	/**
	 * The heart of the highlight search<br>
	 * Actually processes the string and takes actions when the highlight is found
	 * @param text - the original text
	 * @param word - the word/phrase to search for
	 * @param color - the color to highlight width
	 * @param caseSensitive - flag to use casesenstive search or not
	 * @param play - flag to play a sound file or not
	 * @param track - if play is true, then play this file
	 * @param start - the position in the text to start searching from
	 */
	private static StringBuffer continueHighlightSearch(StringBuffer text, String word, String color, String caseSensitive, String play, String track, int start) {
		while (start != -1) {
			if (caseSensitive.equalsIgnoreCase("true")) {
				start = text.indexOf(word, start);
				if (start != -1) {
					//we found the highlight
					text.insert(start, color);
					text.insert(start + word.length() + color.length(), "^N");
					//text = text.substring(0, start) + color + text.substring(start, start + word.length()) + "^N" + text.substring(start + word.length(), text.length());
					if (play.toLowerCase().equals("true") && (track != null)) {
						MP3Thread single = new MP3Thread("single", KilCli.getSoundDir() + track);
					}
					start += color.length() + word.length();
				}
			} else {
				word = word.toLowerCase();
				start = text.toString().toLowerCase().indexOf(word, start);
				if (start != -1) {
					//we found the highlight
					text.insert(start, color);
					text.insert(start + word.length() + color.length(), "^N");
					//text = text.substring(0, start) + color + text.substring(start, start + word.length()) + "^N" + text.substring(start + word.length(), text.length());
					if (play.toLowerCase().equals("true") && (track != null)) {
						MP3Thread single = new MP3Thread("single", KilCli.getSoundDir() + track);
					}
					start += color.length() + word.length();
				}
			}
		}
		return text;
	}

	/**
	 * The heart of the filter search<br>
	 * Actually processes the string and takes actions when the filter is found
	 * @param text - the original text
	 * @param word - the word/phrase to search for
	 * @param replace - the phrase to replace "word" with.
	 * @param caseSensitive - flag to use casesenstive search or not
	 * @param play - flag to play a sound file or not
	 * @param track - if play is true, then play this file
	 * @param start - the position in the text to start searching from
	 */
	private static StringBuffer continueFilterSearch(StringBuffer text, String word, String replace, String caseSensitive, String play, String track, int start) {
		while (start != -1) {
			if (caseSensitive.equalsIgnoreCase("true")) {
				start = text.indexOf(word, start);
				if (start != -1) {
					//we found the filter
					text.replace(start, start + word.length(), replace);
					//text = text.substring(0, start) + replace + text.substring(start + word.length(), text.length());
					if (play.toLowerCase().equals("true") && (track != null)) {
						MP3Thread single = new MP3Thread("single", KilCli.getSoundDir() + track);
					}
					start += replace.length() + 1;
				}
			} else {
				word = word.toLowerCase();
				start = text.toString().toLowerCase().indexOf(word, start);
				if (start != -1) {
					//we found the filter
					text.replace(start, start + word.length(), replace);
					//text = text.substring(0, start) + replace + text.substring(start + word.length(), text.length());
					if (play.toLowerCase().equals("true") && (track != null)) {
						MP3Thread single = new MP3Thread("single", KilCli.getSoundDir() + track);
					}
					start += replace.length() + 1;
				}
			}
		}
		return text;
	}

	/**
	 * Returns the ArrayList containing the highlight info
	 * @return ArrayList containing highlights as loaded by load()
	 */
	public static ArrayList getHighlights() {
		return highlights;
	}

	/**
	 * Returns the ArrayList containing the filter info
	 * @return ArrayList containing filters as loaded by load()
	 */
	public static ArrayList getFilters() {
		return filters;
	}
}