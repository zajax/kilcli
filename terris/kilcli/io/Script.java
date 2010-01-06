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
import java.io.StringReader;
import java.io.StreamTokenizer;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Hashtable;
import java.lang.Math;
import terris.kilcli.*;
import terris.kilcli.thread.*;
import terris.kilcli.resource.*;

/**
 * Script for KilCli is the class used to execute a script<br>
 * Ver: 1.0.1
 */

public class Script {

	private String slash = System.getProperty("file.separator");
	private String line;
	private int delay = 0;
	private StringTokenizer tokenizer;
	private boolean readNext = true;
	private Timer pause;
	private Timer pause2;
	private BufferedReader inFile;
	private MP3Thread mp3 = new MP3Thread("test", "test", 0, false);
	private static String playlist = "";
	private static boolean sounds = true;
	private String[] targets;
	private String name = "";
	private String origName = "";
	private ArrayList scriptStack = new ArrayList();
	private StreamTokenizer st;
	private static Hashtable variables = new Hashtable();
	private static ArrayList effects = new ArrayList();
	private static ArrayList directions = new ArrayList();
	private String temp = "";
	private String tempDir = "";
	private String tempLine = "";
	private String flag = "";
	private File srcFile;
	private int effectsIndex = 0;
	private int num1, num2, numLines;
	private Double d;
	private String part1, part2, part3;
	private int count = 0;

	/**
	 * Creates the script object, initializes effects/directions,
	 * sets up the timers
	 */

	public Script() {

		createEffects();
		createDirections();

		//timer to pause script execution for .5 seconds
		pause = new Timer(500, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pause.stop();
				readNext = true;
				try {
					executeScript();
				} catch (IOException ioe) {}
			}
		});
		pause.setRepeats(false);

		//timer to pause script execution for .25 seconds
		pause2 = new Timer(250, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pause2.stop();
				readNext = true;
				try {
					executeScript();
				} catch (IOException ioe) {}
			}
		});
		pause2.setRepeats(false);

	}

	/**
	 * Method to add effects to the effects array
	 * Used in processing script commands
	 */

	public static void createEffects() {
		if (effects.size() < 1) {
			effects.add("held");
			effects.add("stunned");
			effects.add("entangled");
			effects.add("webbed");
			effects.add("slowed");
			effects.add("pacified");
			effects.add("sleeping");
			effects.add("confused");
			effects.add("deaf");
			effects.add("mute");
			effects.add("clumsy");
			effects.add("weakened");
			effects.add("vulnerable");
			effects.add("seeinvisible");
			effects.add("sanctuaried");
			effects.add("protected");
			effects.add("accurate");
			effects.add("hasted");
			effects.add("strong");
			effects.add("flying");
			effects.add("invisible");
			effects.add("hidden");
			effects.add("poisonresist");
			effects.add("entangleresist");
			effects.add("fireresist");
			effects.add("coldresist");
			effects.add("gilled");
			effects.add("waterwalking");
			effects.add("levitating");
			effects.add("telepathic");
		}
	}

	/**
	 * Method to add directions to the directions array
	 * Used in processing script commands
	 */

	private static void createDirections() {
		if (directions.size() < 1) {
			directions.add("N");
			directions.add("NE");
			directions.add("E");
			directions.add("SE");
			directions.add("S");
			directions.add("SW");
			directions.add("W");
			directions.add("NW");
			directions.add("U");
			directions.add("D");
		}
	}

	/**
	 * Takes a script object and turns it inot a string
	 * Just the name of the script by itself
	 * or name (parent) for nested scripts
	 */

	public String toString() {
		if (origName.length() > 0) {
			return origName + " (" + name + ")";
		}
		return name;
	}

	/**
	 * Loads the named script and executes
	 *
	 * @param scriptName - the name of the script to load
	 */

	public void runScript(String scriptName) {
		name = scriptName;
		readNext = true;
		loadScript(name, 0);
	}

	/**
	 * Loads the specified script, sets up the stream tokenizer
	 * and calls executeScript();
	 *
	 * @param scriptName - the name of the script to be loaded
	 * @param c - the line number to skip to in the file
	 */

	public void loadScript(String scriptName, int c) {
		srcFile = new File("scripts" + slash + scriptName);
		if (!srcFile.exists()) {

			//display some kind of message that the file doesn't exist
			KilCli.gameWrite("^RError!!!! Script not found: " + scriptName);
			quit();
		} else if (!srcFile.isFile() || !srcFile.canRead()) {

			//display error that it can't read from the file
			KilCli.gameWrite("^RError!!!! Cannot read script: " + scriptName);
		} else {
			try {
				inFile = new BufferedReader(new FileReader(srcFile));

				st = new StreamTokenizer(inFile);
				//end of line is significant as there is no character (ie a semicolon) to denote EOL
				st.eolIsSignificant( true );

				//define operators as ordinary characters
				st.ordinaryChar('/');
				st.ordinaryChar('+');
				st.ordinaryChar('-');
				st.ordinaryChar('*');
				st.ordinaryChar('(');
				st.ordinaryChar(')');
				st.ordinaryChar('%');
				st.ordinaryChar('=');
				st.ordinaryChar('^');
				st.ordinaryChar(':');
				st.ordinaryChar('|');
				st.ordinaryChar(32);
				st.quoteChar('"');
				//allows variable names with numbers in them

				for (int i = 1; i < c; i++) {
					st.nextToken();
					while ((st.ttype != StreamTokenizer.TT_EOL) && (st.ttype != StreamTokenizer.TT_EOF)) {
						st.nextToken();
					}
				}

				executeScript();
			} catch (IOException fnfe) {}
		}
	}

	/**
	 * Method to run a script with the specified targets passed to it
	 *
	 * @param scriptName - the name of the script to be loaded
	 * @param t - the string[] of targets to be used
	 */

	public void runScript(String scriptName, String[] t) {
		name = scriptName;
		readNext = true;
		targets = t;
		loadScript(name, 0);
	}

	/**
	 * Method to check if the specified direction is available
	 *
	 * @param tempDirections - the direction string
	 * @param temp - the direction to check
	 *
	 * @return int - 1 if present, 0 if not
	 */

	private int dirCheck(String tempDirections, String temp) {
		int i = 0;
		if (tempDirections != null) {
			i = Integer.parseInt(tempDirections.charAt(directions.indexOf(temp) + 2) + "");
		} else {
			i = 0;
		}
		return i;
	}

	/**
	 * Method to execute the currently loaded script
	 */

	private void executeScript() throws IOException{

    	//while we can read and we have been told to keep reading
    	while (readNext && inFile.ready()) {
			//read the line
			try {
				nextLine();
			} catch (evalError ee) {
				KilCli.gameWrite("<br><b>Script Error!!</b>");
				KilCli.gameWrite("Error on line number: " + st.lineno() + " - " + line);
				ee.printStackTrace();
				KilCli.gameWrite("Quitting script<br>");
				quit();
			} catch (Exception e) {
				KilCli.gameWrite("<br><b>Script Error!!</b>");
				KilCli.gameWrite("Error on line number: " + st.lineno() + " - " + line);
				int tmp = e.toString().lastIndexOf(" ");
				KilCli.gameWrite("Error created on: " + e.toString().substring(tmp+1, e.toString().length()));
				KilCli.gameWrite("Quitting script<br>");
				quit();
			}
		}
		//if we reached the end of the file, see if there is anything on the script stack to go back to
		if (!inFile.ready() && readNext) {
			if (scriptStack.size() > 0) {
				name = (String)(scriptStack.get(scriptStack.size() - 2));
				count = Integer.parseInt((String)(scriptStack.get(scriptStack.size() - 1)));
				scriptStack.remove(scriptStack.size() - 2);
				scriptStack.remove(scriptStack.size() - 1);
				loadScript(name, count);

			} else {
				quit();
			}
		}
	}

	/**
	 * Method to see if we will have to pause
	 * at the end of executing this line
	 *
	 * @param c - the current line
	 * @return String - The line without the last character, if it was a pause
	 */

	private String checkPause(String c) {
		if (c.charAt(c.length() - 1) == '#') {
			delay = 2;
			c = c.substring(0, c.length() - 1);
		} else if (c.charAt(c.length() - 1) == '%') {
			delay = 1;
			c = c.substring(0, c.length() -1);
		}
		return c;
	}

	/**
	 * Method that checks if we were told to pause or not
	 */
	private void checkPause2() {
		if (delay == 1) {
			delay = 0;
			readNext = false;
			pause.start();
		} else if (delay == 2) {
			delay = 0;
			readNext = false;
			pause2.start();
		}
	}

	/**
	 * Sets if sound is enabled or not
	 *
	 * @param s - the new value of sounds
	 */

	public static void setSound(boolean s) {
		sounds = s;
	}

	/**
	 * If it was an mp3 command, this method handles the line
	 *
	 * @param random - boolean to determine if random order is used for the playlist
	 */

	private void mp3Script(boolean random) {
		if (random == true) {
			line = line.substring(0, line.length() - 5);
		}
		//loads the playlist and starts playing in a new thread
		if (line.toLowerCase().substring(line.length()-4, line.length()).equals(".kpl")) {
			if (!playlist.equals(line)) {
				playlist = line;
				int tmp = mp3.stopMP3();
				MP3Thread list = new MP3Thread("list", line, 0, random);
				KilCli.setMP3(list);
				list.start();
			}
		} else {
			MP3Thread single = new MP3Thread("single", line);
		}
	}

	/**
	 * Function that converts any &X's into the passed targets
	 *
	 * @param text - the text to check for needed conversion
	 */

	private String convert(String text) {
		try {
			if (text.charAt(0) == '&') {
				text = targets[Integer.parseInt(text.charAt(1) + "")];
			}
		} catch (Exception e) {

		}
		return text;
	}

	/**
	 * Exits the script
	 */

	public void quit() {
		readNext = false;
		try {
			inFile.close();
		} catch (Exception e) {
			KilCli.gameWrite("Error closing file");
		}
		ScriptExecuteThread.remove(this);
	}

	/**
	 * Function to parse the if statements and take appropriate actions
	 * when returning an int
	 *
	 * @param part - the portion of the command to be checked
	 * @return int - the result of translating the if statement into an integer
	 */

	private int ifParser(String part) {

		if (part.equalsIgnoreCase("HPs")) {
			return KilCli.getHPs();
		} else if (part.equalsIgnoreCase("SPs")) {
			return KilCli.getSPs();
		} else if (part.equalsIgnoreCase("Unbalanced")) {
			return KilCli.getUnbalanced();
		} else if (part.equalsIgnoreCase("Exp")) {
		    temp = KilCli.getExp();
		    return (int)(Double.parseDouble(temp));
		} else if (part.equalsIgnoreCase("Gold")) {
			temp = KilCli.getGold().trim();
			return Integer.parseInt(temp);
		} else if (part.toLowerCase().startsWith("dir.")) {
			temp = part.substring(4, part.length());
			tempDir = KilCli.getDirs();
			return dirCheck(tempDir, temp);
		}
		effectsIndex = effects.indexOf(part.toLowerCase());
		if (effectsIndex > -1) {
			return KilCli.getEffects().getEffectStatus(effectsIndex);
		}
		return Integer.parseInt(part);

	}

	/**
	 * Function to parse the if statements and take appropriate actions
	 * when returning a string
	 *
	 * @param part - the portion of the command to be checked
	 * @return String - the result of translating the if statement into a string
	 */

	private String ifParser2(String part) {
		if (part.equalsIgnoreCase("RightHand")) {
			return KilCli.getRight();
		} else if (part.equalsIgnoreCase("LeftHand")) {
			return KilCli.getLeft();
		} else if (part.equalsIgnoreCase("CharacterName")) {
			return KilCli.getUsedCharacter();
		}
		return part;
	}

	/**
	 * Loads either the next token or the entire line
	 *
	 * @param wholeLine - if true, loads whole line, if false loads next token
	 * @return String - The loaded token(s)
	 */

	private String getTokens(boolean wholeLine) {
		tempLine = "";
		boolean again = true;
		try {
			if (wholeLine) {
				while ((st.ttype != StreamTokenizer.TT_EOL) && (st.ttype != StreamTokenizer.TT_EOF)) {
					tempLine = tempLine + nextToken();
				}
				if (tempLine.length() > 0) {
					tempLine = checkPause(tempLine);
				}
			} else {
				tempLine = nextToken();
			}
		} catch (Exception e) {
			e.printStackTrace();
			//SMTPClient.sendError("gettokens", e);
		}
		return tempLine;
	}

	/**
	 * Analyzes the next token
	 *
	 * @return String - the results of what was loaded
	 */

	private String nextToken() throws Exception {
		st.nextToken();
		//if the next token is a word, check if it was a variable
		if (st.ttype == StreamTokenizer.TT_WORD) {
			if ( variables.containsKey(st.sval)) {
				d = (Double)(variables.get(st.sval));
				return d.intValue() + "";	//gets the variable from the hastable and returns it
			} else {
				//otherwise just return the word
				return st.sval;
			}

		//if we just loaded a number, turn the number
		} else if (st.ttype == StreamTokenizer.TT_NUMBER) {
			d = new Double(st.nval);
			return d.intValue() + "";
		//if we loaded a specified string, return the string without "'s
		} else if (st.ttype == '"') {
			if (st.sval.length() == 0) {
				return "\"";
			} else {
				return convert(st.sval);
			}
		//if it was a space, return it
		} else if (st.ttype == 32) {
			return " ";
		//if it is equals, check the next character
		//handles comparisons of ==, ==| or =
		} else if (st.ttype == '=') {
			st.nextToken();
			if (st.ttype == '=') {
				st.nextToken();
				if (st.ttype == '|') {
					return "==|";
				} else {
					st.pushBack();
					return "==";
				}
			} else {
				st.pushBack();
				return "=";
			}
		//checks for <, <>, or <>|
		} else if (st.ttype == '<') {
			st.nextToken();
			if (st.ttype == '>') {
				st.nextToken();
				if (st.ttype == '|') {
					return "<>|";
				} else {
					st.pushBack();
					return "<>";
				}
			} else {
				st.pushBack();
				return "<";
			}

		//coverts the string if needed
		} else if (st.ttype == '&') {
			temp = nextToken();
			if (temp.length() == 1) {
				if ((temp.charAt(0) > 47) && (temp.charAt(0) < 54)) {
					return convert('&' + temp);
				} else {
					st.pushBack();
					return (char)(st.ttype) + "";
				}
			} else {
				st.pushBack();
				return (char)(st.ttype) + "";
			}
		} else if ((st.ttype != StreamTokenizer.TT_EOL) && (st.ttype != StreamTokenizer.TT_EOF)) {
			return (char)(st.ttype) + "";
		}

		//otherwise return nothing
		return "";
	}

	/**
	 * Evaluates the next line in the script file
	 * Checks the flag and then takes appropriate actions
	 * for the rest of the line
	 */
	private void nextLine() throws evalError {
		temp = "";
		tempDir = "";
		part1 = "";
		part2 = "";
		part3 = "";
		line = "";

		try {
			flag = nextToken();
			st.nextToken();
	    	if (st.ttype == ':') {
				//Check the flag character (first character in the line)
				if (flag.equals("G")) {
					line = getTokens(true);
					if (line.length() > 0) {
						KilCliThread.getKilCli().scriptText(line);
					}
				} else if (flag.equals("O")) {
					line = getTokens(true);
					if (line.length() > 0) {
						KilCli.gameWrite(line);
					}
				} else if (flag.equals("C")) {
					//this is a command line, so we have lots of options
					temp = getTokens(false);
					if (temp.equalsIgnoreCase("if")) {
						//load all the parts of the line
						st.nextToken();
						part1 = getTokens(false);
						st.nextToken();
						part2 = getTokens(false);
						st.nextToken();
						part3 = getTokens(false);
						st.nextToken();
						//how many lines are skipped if this if statement fails
						if (st.ttype == '|') {
							st.nextToken();
							d = new Double(st.nval);
							numLines = d.intValue();

							if (part2.length() == 1) {
								try {
									num1 = ifParser(part1.trim());
								} catch (Exception e) {
									KilCli.gameWrite("Bad script statement, cannot translate " + part1 + " into an integer for comparison");
									KilCli.gameWrite("Do you need to use \" \"?");
									KilCli.gameWrite("Perhaps a bad variable name");
									KilCli.gameWrite("Part1=" + part1);
									KilCli.gameWrite("Part2=" + part2);
									KilCli.gameWrite("Part3=" + part3);
									KilCli.gameWrite("Line #: " + st.lineno());
									quit();
								}
								try {
									num2 = ifParser(part3.trim());
								} catch (Exception e) {
									KilCli.gameWrite("Bad script statement, cannot translate " + part3 + " into an integer for comparison");
									KilCli.gameWrite("Do you need to use \" \"?");
									KilCli.gameWrite("Perhaps a bad variable name");
									KilCli.gameWrite("Part1=" + part1);
									KilCli.gameWrite("Part2=" + part2);
									KilCli.gameWrite("Part3=" + part3);
									KilCli.gameWrite("Line #: " + st.lineno());
									quit();
								}
							}

							//basic > comparison between part1 and part3
							if (part2.equals(">")) {
								if (num1 <= num2) {
									try {
										int i = st.lineno() + numLines;
										while (st.lineno() <= i) {
											st.nextToken();
											while ((st.ttype != StreamTokenizer.TT_EOL) && (st.ttype != StreamTokenizer.TT_EOF)) {
												st.nextToken();
											}
										}
									} catch (Exception e) {}
								} else {
									while ((st.ttype != StreamTokenizer.TT_EOL) && (st.ttype != StreamTokenizer.TT_EOF)) {
										st.nextToken();
									}
								}

							//basic < comparison between part1 and part3
							} else if (part2.equals("<")) {
								if (num1 >= num2) {
									try {
										int i = st.lineno() + numLines;
										while (st.lineno() <= i) {
											st.nextToken();
											while ((st.ttype != StreamTokenizer.TT_EOL) && (st.ttype != StreamTokenizer.TT_EOF)) {
												st.nextToken();
											}
										}
									} catch (Exception e) {}
								} else {
									while ((st.ttype != StreamTokenizer.TT_EOL) && (st.ttype != StreamTokenizer.TT_EOF)) {
										st.nextToken();
									}
								}

							//basic = comparison between part1 and part3
							} else if (part2.equals("=")) {
								if (num1 != num2) {
									try {
										int i = st.lineno() + numLines;
										while (st.lineno() <= i) {
											st.nextToken();
											while ((st.ttype != StreamTokenizer.TT_EOL) && (st.ttype != StreamTokenizer.TT_EOF)) {
												st.nextToken();
											}
										}
									} catch (Exception e) {}
								} else {
									while ((st.ttype != StreamTokenizer.TT_EOL) && (st.ttype != StreamTokenizer.TT_EOF)) {
										st.nextToken();
									}
								}

							//string comparison, if part1.equals(part3)
							} else if (part2.equals("==")) {
								part1 = ifParser2(part1.trim());
								part3 = ifParser2(part3.trim());
								if (!part1.trim().equals(part3.trim())) {
									try {
										int i = st.lineno() + numLines;
										while (st.lineno() <= i) {
											st.nextToken();
											while ((st.ttype != StreamTokenizer.TT_EOL) && (st.ttype != StreamTokenizer.TT_EOF)) {
												st.nextToken();
											}
										}
									} catch (Exception e) {}
								} else {
									while ((st.ttype != StreamTokenizer.TT_EOL) && (st.ttype != StreamTokenizer.TT_EOF)) {
										st.nextToken();
									}
								}

							//string comparison, if part1 exists in part3
							} else if (part2.equals("<>")) {
								part1 = ifParser2(part1.trim());
								part3 = ifParser2(part3.trim());
								if (part1.trim().indexOf(part3.trim()) == -1) {
									try {
										int i = st.lineno() + numLines;
										while (st.lineno() <= i) {
											st.nextToken();
											while ((st.ttype != StreamTokenizer.TT_EOL) && (st.ttype != StreamTokenizer.TT_EOF)) {
												st.nextToken();
											}
										}
									} catch (Exception e) {}
								} else {
									while ((st.ttype != StreamTokenizer.TT_EOL) && (st.ttype != StreamTokenizer.TT_EOF)) {
										st.nextToken();
									}
								}
							//string comparison, equals ignore case
							} else if (part2.equals("==|")) {
								part1 = ifParser2(part1.trim());
								part3 = ifParser2(part3.trim());
								if (!part1.trim().equalsIgnoreCase(part3.trim())) {
									try {
										int i = st.lineno() + numLines;
										while (st.lineno() <= i) {
											st.nextToken();
											while ((st.ttype != StreamTokenizer.TT_EOL) && (st.ttype != StreamTokenizer.TT_EOF)) {
												st.nextToken();
											}
										}
									} catch (Exception e) {}
								} else {
									while ((st.ttype != StreamTokenizer.TT_EOL) && (st.ttype != StreamTokenizer.TT_EOF)) {
										st.nextToken();
									}
								}

							//string comparison, part1 is in part3, ignore case
							} else if (part2.equals("<>|")) {
								part1 = ifParser2(part1.trim());
								part3 = ifParser2(part3.trim());
								if (part1.trim().toLowerCase().indexOf(part3.trim().toLowerCase()) == -1) {
									try {
										int i = st.lineno() + numLines;
										while (st.lineno() <= i) {
											st.nextToken();
											while ((st.ttype != StreamTokenizer.TT_EOL) && (st.ttype != StreamTokenizer.TT_EOF)) {
												st.nextToken();
											}
										}
									} catch (Exception e) {}
								} else {
									while ((st.ttype != StreamTokenizer.TT_EOL) && (st.ttype != StreamTokenizer.TT_EOF)) {
										st.nextToken();
									}
								}
							} else {
								//otherwise error
								KilCli.gameWrite("Bad script statement, unknown operator (Part2)");
								KilCli.gameWrite("Part1=" + part1);
								KilCli.gameWrite("Part2=" + part2);
								KilCli.gameWrite("Part3=" + part3);
								KilCli.gameWrite("Line #: " + st.lineno());
								quit();
							}
						} else {
							//otherwise error
							KilCli.gameWrite("Bad script statement, number of tokens doesn't match");
							KilCli.gameWrite("Do you need to use \" \" around a section?");
							KilCli.gameWrite("Part1=" + part1);
							KilCli.gameWrite("Part2=" + part2);
							KilCli.gameWrite("Part3=" + part3);
							KilCli.gameWrite("Line #: " + st.lineno());
							quit();
						}
					//check for other special command conditions
					} else if (temp.equalsIgnoreCase("skipoutput")) {
						SendReceive.setScriptSkip(true);
						st.nextToken();
						while ((st.ttype != StreamTokenizer.TT_EOL) && (st.ttype != StreamTokenizer.TT_EOF)) {
							st.nextToken();
						}
					} else if (temp.equalsIgnoreCase("quit")) {
						quit();
					} else if (temp.equalsIgnoreCase("script")) {
						st.nextToken();
						line = getTokens(true);
						if (line.length() > 0) {
							try {
								if (inFile.ready()) {
									scriptStack.add(name);
									scriptStack.add("" + st.lineno());
								}
							} catch (Exception e) {
								System.err.println("inFile went away");
								e.printStackTrace();
								SMTPClient.sendError("inFile", e);
							}
							if (origName.length() < 1) {
								origName = name;
							}

							runScript(line);
						}
					} else {
						checkPause(temp);
						getTokens(true);
					}
				//Puts a string in the command line
				} else if (flag.equals("P")) {
					line = getTokens(true);
					if (line.length() > 0) {
						KilCli.insertCommand(line);
					}
				//plays a sound
				} else if (flag.equals("S")) {
					if (sounds) {
						line = getTokens(true);
						if (line.length() > 0) {
							if (line.toLowerCase().indexOf("|true") > 0) {
								mp3Script(true);
							} else {
								mp3Script(false);
							}
						}
					}
				//Math operations, saves to the hashtable
				} else if (flag.equals("M")) {

					try {
						//acquires the first token
						st.nextToken( );
						try {
							statement( );
							}
						catch( evalError q ) {
							q.printError();
						}
					} catch (IOException ioe) {
						System.err.println(ioe);
						//SMTPClient.sendError("Script", ioe);
					}
				}
			} else {
				KilCli.gameWrite("Bad script statement, unknown initial flag");
				KilCli.gameWrite("Initial flag=" + flag);
				KilCli.gameWrite("Valid flags are: M, S, P, G, C, and O");
				KilCli.gameWrite("Line #: " + st.lineno());
				quit();
			}
			//check if we need to pause
			checkPause2();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns all variables currently known to the scripting engine<br>
	 * @return string - containing all scripting variables
	 */
	public static String getVariables() {
		return variables.toString();
	}

	/**
	 * Sets a variable to the given value.
	 *
	 * @param name - the variable to set
	 * @param value - the value to set the variable to
	 */
	public static void setVariable(String name, String value) {
		try {
			double test = Double.parseDouble(value);
			variables.put(name, new Double(test));
			KilCli.gameWrite("Variable: " + name + " set to: " + value);
		} catch (Exception e) {
			KilCli.gameWrite("Bad value given.  Value=" + value);
		}
	}

	/**
	 * statement -
	 *
	 * This function parses this grammar's two keywords and either prints
	 * as desired or stores data to a variable.
	 *
	 */
	public void statement() throws IOException, evalError {
		if(st.sval != null) {
			String key = st.sval;
			st.nextToken( );
			//stores the value into a hashtable
			try {
				variables.put( key, new Double( expr( ) ) );
			}
			catch(evalError q) {
				throw q;
			}
		}
	}

	/**
	 * expr -
	 *
	 * This function parses the addition and subtraction operators via
	 * indirect recusion.
	 *
	 */
	public double expr() throws IOException, evalError {

		double t1;
		double t2;
		try {
			t1 = term();
		}
		catch (evalError q) {
			throw q;
		}

		int op;

		while( st.ttype == '+'  || st.ttype == '-' ) {
			op = st.ttype;
			try {
				t2 = term();
			}
			catch (evalError q) {
				throw q;
			}
			if( op == '+' )
				t1 += t2;
			else
				t1 -= t2;
		}

		return t1;
	}

	/**
	 * term -
	 *
	 * This function parses the exponentiation, multiplication, and division
	 * operators via indirect recursion.
	 *
	 */
	public double term() throws IOException, evalError {
		double t1;
		double t2;

		try {
			t1 = factor();
		}
		catch(evalError q) {
			throw q;
		}

		int op;
		st.nextToken( );

		//exponentiation operator providing for variable bases and variable
		//powers
		if( st.ttype == '^' )
			t1 = Math.pow(t1, expr());

		while(st.ttype == '*'  || st.ttype == '/' || st.ttype == '%') {
			op = st.ttype;
			try {
				t2 = factor();
			}
			catch(evalError q) {
				throw q;
			}
			if( op == '*' ) {
				t1 *= t2;
			} else if (op == '/') {
				t1 /= t2;
			} else {
				t1 %= t2;
			}

			st.nextToken( );
		}

		return t1;
	}

	/**
	 * factor -
	 *
	 * This function is the lowest level or "base case" of the indirect
	 * recursion. It evaluates operators which receieve the highest
	 * precedence: parenthesis and unary minus.
	 *
	 */
	public double factor( ) throws IOException, evalError {
		st.nextToken();

		if( st.ttype == '(' ) {
			double temp = expr( );
			//st.nextToken();
			if(st.ttype == ')')
				return temp;		//returns the expression surrounded by parenthesis
			else
				throw new evalError( "Syntax Error: Unmatched parenthesis" );
		}
		else if(st.ttype == ')')
				throw new evalError( "Syntax Error: Unmatched parenthesis" );
		else if(st.ttype == st.TT_NUMBER)
			return st.nval;			//returns the number hit at the lowest level
		else if(st.ttype == st.TT_WORD) {
			if(variables.containsKey(st.sval)) {
				Double d = (Double) variables.get (st.sval);
				return d.doubleValue();	//gets the variable from the hastable and returns it
			}
			else {
				//if the variable is previously undefined:
				throw new evalError( "Undefined variable " + st.sval + " in line " + st.lineno() );
			}
		}
		else if(st.ttype == '-')
			return -1 * factor();			//unary minus implementation
		//else
			//throw new evalError("Unexpected: " + (char)st.ttype + " in line " + st.lineno() );
		return -1;
	}

	//exception class declaration
	class evalError extends Throwable {
		//member variable declarations
		String errorMessage;

		/**
		 * evalError -
		 *
		 * Exception class constructor that takes a string
		 * as an argument.
		 */
		evalError(String error) {
			errorMessage = error;
		}

		/**
		 * printError -
		 *
		 * Prints out the contents of the exception error.
		 */
		void printError() {
			System.out.println(errorMessage);
		}
	}

}