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

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.JEditorPane;
import javax.swing.BorderFactory;
import javax.swing.text.BadLocationException;
import java.io.StringReader;
import java.io.IOException;
import java.lang.StringBuffer;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.io.StringWriter;
import terris.kilcli.io.*;
import terris.kilcli.loader.*;
import terris.kilcli.KilCli;
import terris.kilcli.thread.MP3Thread;
import javax.swing.text.html.HTML.Tag;
import java.text.CollationElementIterator;
import java.text.RuleBasedCollator;
import java.text.Collator;
import java.util.HashMap;

/**
 * KilCliText for KilCli is the class used as the text area<br>
 * for each display window.<br>
 * Ver: 1.0.0
 */

public class KilCliText extends JEditorPane{

    private int stringSearchIndex = 0;
    private int stringSearchIndex2;
	private int colored = 0;
	private boolean bold;
	private boolean italic;
	private char[] caps;
	private char[] lowers;
	private static String[] colors;
	private HTMLEditorKit kit;
	private boolean skipHighlight = false;
	private static HashMap colorCodes = new HashMap(60);
	private static int windowBuffer = 30000;
	private StringBuffer outputBuffer = new StringBuffer();
	private int last = 0;
	private boolean sizeChanged = false;
	private boolean useCopy = false;


	/**
	 * Creates a TerrisText object with arrays for lower and capital colors
	 */

    public KilCliText(){
		this(true);
    }

	public KilCliText(boolean useNewCopy) {
        super();
		useCopy = useNewCopy;
		try {
		    colors = ConfigLoader.loadColors(KilCli.getProfile());
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}

        //set contenttype, editable, and border
        setContentType("text/html");
        kit = (HTMLEditorKit) getEditorKit();
        setEditable(false);
        setBorder(BorderFactory.createEtchedBorder());
    	caps = new char[10];
    	lowers = new char[10];
    	caps[0] = 'R';
    	caps[1] = 'G';
    	caps[2] = 'W';
    	caps[3] = 'C';
    	caps[4] = 'V';
    	caps[5] = 'B';
    	caps[6] = 'Y';
    	caps[7] = 'O';
    	caps[8] = 'P';
    	caps[9] = 'T';
    	lowers[0] = 'r';
    	lowers[1] = 'g';
    	lowers[2] = 'w';
    	lowers[3] = 'c';
    	lowers[4] = 'v';
    	lowers[5] = 'b';
    	lowers[6] = 'y';
    	lowers[7] = 'o';
    	lowers[8] = 'p';
		lowers[9] = 't';
		createTable();
	}

    public boolean getScrollableTracksViewportWidth() {
      return true;
    }


	private static void createTable() {
		colorCodes.clear();
		colorCodes.put("+", new KilCliColor(-6, false, "<font size=+1>"));
		colorCodes.put("-", new KilCliColor(-7, false, "<font size=-1>"));
		//was code to display image, I think we might can it though
		//colorCodes.put("A", new KilCliColor(false, "<img src=\"http://", "\">"));
		colorCodes.put("B", new KilCliColor(10, false, "<font color=#" + colors[10] + ">"));
		colorCodes.put("C", new KilCliColor(6, false, "<font color=#" + colors[6] + ">"));
		colorCodes.put("E", new KilCliColor(-2, false, "<b>"));
		colorCodes.put("F", new KilCliColor(-1));
		colorCodes.put("G", new KilCliColor(2, false, "<font color=#" + colors[2] + ">"));
		colorCodes.put("H", new KilCliColor(-1));
		colorCodes.put("I", new KilCliColor(-4, false, "<i>"));
		colorCodes.put("K", new KilCliColor(18, false, "<font style=\"background-color: " + colors[18] + ";\" color=#" + colors[19] + ">"));
		colorCodes.put("N", new KilCliColor(-100, true, ""));
		colorCodes.put("O", new KilCliColor(12, false, "<font color=#" + colors[12] + ">"));
		colorCodes.put("P", new KilCliColor(16, false, "<font color=#" + colors[16] + ">"));
		colorCodes.put("R", new KilCliColor(0, false, "<font color=#" + colors[0] + ">"));
		colorCodes.put("T", new KilCliColor(22, false, "<font color=#" + colors[22] + ">"));
		colorCodes.put("V", new KilCliColor(8, false, "<font color=#" + colors[8] + ">"));
		colorCodes.put("W", new KilCliColor(4, false, "<font color=#" + colors[4] + ">"));
		// *ULTRA SOUND* "X" (handled in exception)
		colorCodes.put("Y", new KilCliColor(14, false, "<font color=#" + colors[14] + ">"));
		colorCodes.put("Z", new KilCliColor(-100, false, ""));
		colorCodes.put("b", new KilCliColor(11, false, "<font color=#" + colors[11] + ">"));
		colorCodes.put("c", new KilCliColor(7, false, "<font color=#" + colors[7] + ">"));
		colorCodes.put("e", new KilCliColor(-3, false, "</b>"));
		colorCodes.put("f", new KilCliColor(-1));
		colorCodes.put("g", new KilCliColor(3, false, "<font color=#" + colors[3] + ">"));
		colorCodes.put("i", new KilCliColor(-5, false, "</i>"));
		colorCodes.put("k", new KilCliColor(20, false, "<font style=\"background-color: " + colors[20] + ";\" color=#" + colors[21] + ">"));
		colorCodes.put("n", new KilCliColor(-100, true, ""));
		colorCodes.put("o", new KilCliColor(13, false, "<font color=#" + colors[13] + ">"));
		colorCodes.put("p", new KilCliColor(17, false, "<font color=#" + colors[17] + ">"));
		colorCodes.put("r", new KilCliColor(1, false, "<font color=#" + colors[1] + ">"));
		colorCodes.put("t", new KilCliColor(23, false, "<font color=#" + colors[23] + ">"));
		colorCodes.put("v", new KilCliColor(9, false, "<font color=#" + colors[9] + ">"));
		colorCodes.put("w", new KilCliColor(5, false, "<font color=#" + colors[5] + ">"));
		colorCodes.put("y", new KilCliColor(15, false, "<font color=#" + colors[15] + ">"));
		colorCodes.put("^", new KilCliColor(-1, false, "^"));
	}

	public static void setWindowBuffer(int newBuffer) {
		windowBuffer = newBuffer;
	}

	public static int getWindowBuffer() {
		return windowBuffer;
	}

	/**
	 * Updates the html color codes used by TerrisText instances
	 */

    public static void updateColors(String[] newColors) {
		colors = newColors;
		createTable();
	}

	/**
	 * Method to add html parsed text to the region
	 *
	 * @param html The text to be added
	 * @param scroll - if true, scroll to the end, if false, leave position where it is
	 */

	public void insertHTML (String h, boolean scroll) throws IOException {
		//check for highlight strings
		if (!skipHighlight) {
			h = HighlightStrings.analyze(h);
		}

		//adds color to the string
		colorize(h);

		//checks if we need to close a font tag
		while (colored > 0) {
			append("</font>");
			colored--;
		}

		//checks if we need to close a bold tag
		if (bold) {
			append("</b>");
		}

		//checks if we need to close an italic tag
		if (italic) {
			append("</i>");
		}

		//add html string to document
		try {
 			kit.read(new StringReader(outputBuffer.toString()), getDocument(), getDocument().getLength());
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
		clear();

		if (scroll) {
			scroll();
		}

		//reset variables
		colored = 0;
		bold = false;
		italic = false;
		stringSearchIndex = 0;
	}

	/**
	 * Method to turn ^* statements into colors/bold/italics/pictures
	 *
	 * @param html The string to be parsed
	 * @return String The string after parsing
	 */

	private void colorize(String html) {
		last = 0;
		char switchChar;
		KilCliColor tempColor;
		String prefix;

		stringSearchIndex = html.indexOf("^");
		//while the search index returns a valid number
		while (stringSearchIndex != -1) {
			prefix = "";
			//get the character after the ^
			switchChar = html.charAt(stringSearchIndex + 1);
			try {
				tempColor = (KilCliColor)(colorCodes.get(switchChar + ""));
				if (tempColor.getNumber() > -1){
					if ((colored > 0) && (!sizeChanged)) {
						prefix = "</font>";
						colored--;
					}
					append(html.substring(last, stringSearchIndex) + prefix + tempColor.getStartCommand());
					stringSearchIndex++;
					last = stringSearchIndex+1;
					colored++;
				} else if (tempColor.isNormal()) {
					if (bold) {
						prefix = "</b>";
						bold = false;
					}
					if (italic) {
						prefix += "</i>";
						italic = false;
					}
					while (colored > 0) {
						prefix += "</font>";
						colored--;
						sizeChanged = false;
					}

					append(html.substring(last, stringSearchIndex) + prefix);
					stringSearchIndex++;
					last = stringSearchIndex+1;
				} else if (tempColor.getNumber() == -1) {
					switch(switchChar) {
						//set text to be a random capital color
						case 'F':
							int rand = (int) (Math.random() * 10);
							tempColor = (KilCliColor)(colorCodes.get(caps[rand] + ""));
							if (colored > 0) {
								prefix = "</font>";
								colored--;
							}
							append(html.substring(last, stringSearchIndex) + prefix + "<font color=#" + colors[tempColor.getNumber()] + ">");
							stringSearchIndex++;
							last = stringSearchIndex+1;
							colored++;
							break;

						//set text to be a random color from full range
						case 'H':
							int rand2 = (int) (Math.random() * 20);
							if (rand2 < 10) {
								tempColor = (KilCliColor)(colorCodes.get(caps[rand2] + ""));
							} else {
								rand2 = rand2 - 10;
								tempColor = (KilCliColor)(colorCodes.get(lowers[rand2] + ""));
							}
							if (colored > 0) {
								prefix = "</font>";
								colored--;
							}
							append(html.substring(last, stringSearchIndex) + prefix + "<font color=#" + colors[tempColor.getNumber()] + ">");
							stringSearchIndex++;
							last = stringSearchIndex + 1;
							colored++;
							break;

						//set text to be a random dark color
						case 'f':
							int rand3 = (int) (Math.random() * 10);
							tempColor = (KilCliColor)(colorCodes.get(lowers[rand3] + ""));
							if (colored > 0) {
								prefix = "</font>";
								colored--;
							}
							append(html.substring(last, stringSearchIndex) + prefix + "<font color=#" + colors[tempColor.getNumber()] + ">");
							stringSearchIndex++;
							last = stringSearchIndex + 1;
							colored++;
							break;

						case '^':
							append(html.substring(last, stringSearchIndex) + tempColor.getStartCommand());
							stringSearchIndex+=2;
							last = stringSearchIndex;
						default:
							stringSearchIndex++;
							break;
					}

				} else {

					append(html.substring(last, stringSearchIndex));
					if (tempColor.isFontChange()) {
						//if (colored > 0) {
						//	append("</font>");
						//	colored--;
						//}
						colored++;
						sizeChanged = true;
					} else if (tempColor.isBold()) {
						bold = true;
					} else if (tempColor.isItalic()) {
						italic = true;
					} else if (tempColor.isAntiBold()) {
						bold = false;
					} else if (tempColor.isAntiItalic()) {
						italic = false;
					}
					append(tempColor.getStartCommand());

					stringSearchIndex++;
					last = stringSearchIndex + 1;
				}

			} catch (Exception e) {
				//key not found, Check if its X, otherwise increment
				if (switchChar == 'X') {

					//its one of the new wizards control thingies, so print out the line and let's see what's going on
					//^XS* = sound
					int endOfLine = html.indexOf("^N", stringSearchIndex);
					if ((endOfLine == -1) || (endOfLine > (stringSearchIndex + 8))) {
						endOfLine = html.indexOf(" ", stringSearchIndex);
					}
					if ((endOfLine == -1) || (endOfLine > (stringSearchIndex + 8))) {
						endOfLine = html.indexOf("<br>", stringSearchIndex);
					}
					String tmp = html.substring(stringSearchIndex, endOfLine);
					int tmpSearch = tmp.indexOf("S");
					if (tmpSearch != -1) {
						int tmpSearch2 = tmp.indexOf(" ", tmpSearch);
						if (tmpSearch2 == -1) {
							tmpSearch2 = tmp.indexOf("<br>", tmpSearch);
						}
						if (tmpSearch2 == -1) {
							tmpSearch2 = tmp.length();
						}
						String soundString = tmp.substring(tmpSearch+1, tmpSearch2);
						soundString = KilCli.getSoundDir() + soundString + ".wav";
						MP3Thread single = new MP3Thread("UltraSound", soundString);
					}
					append(html.substring(last, stringSearchIndex));
					last = endOfLine;
					stringSearchIndex++;
				} else {
					append(html.substring(last, stringSearchIndex));
					stringSearchIndex++;
					last = stringSearchIndex + 1;
				}
			}
			stringSearchIndex = html.indexOf("^", stringSearchIndex);
		}
		append(html.substring(last, html.length()));

	}

	private void append(String add) {
		stringSearchIndex2 = -1;
		while ((stringSearchIndex2 = add.indexOf("  ", stringSearchIndex2)) != -1) {
			add = add.substring(0, stringSearchIndex2) + " &nbsp;" + add.substring(stringSearchIndex2 + 2, add.length());
		}
		outputBuffer.append(add);
	}

	private void clear() {
		outputBuffer = new StringBuffer();
	}

	/**
	 * Toggles the value of skipHighlight, which is used to avoid
	 * sending a string to the highlight processor
	 *
	 * @param skip - the new value of skipHighlight
	 */

	public void setSkipHighlight(boolean skip) {
		skipHighlight = skip;
	}

	/**
	 * Sets caret position to be the end of the document
	 */

    public void scroll() {
        setCaretPosition(getDocument().getLength());
    }

    public void copy() {
		if (!useCopy) {
			super.copy();
			return;
		}
    	int p0 = getSelectionStart();
        int p1 = getSelectionEnd();
        if (p0 != p1) {
        	try {
				StringWriter out = new StringWriter();
				HTMLDocument temp = (HTMLDocument)getDocument();
	   		 	KilCliHTMLWriter w = new KilCliHTMLWriter(out, temp, p0, p1-p0);
	    		w.write();
				StringBuffer srcData = out.getBuffer();
				srcData.delete(0, 20);
				srcData.delete(srcData.length()-19, srcData.length());
				int searchIndex = srcData.indexOf("<br>");
				while (searchIndex != -1) {
					srcData.replace(searchIndex, searchIndex+4, "\n");
					searchIndex = srcData.indexOf("<br>", searchIndex-1);
				}
				searchIndex = srcData.indexOf("&#160;");
				while (searchIndex != -1) {
					srcData.replace(searchIndex, searchIndex+6, " ");
					searchIndex = srcData.indexOf("&#160;", searchIndex);
				}
				searchIndex = srcData.indexOf("&lt;");
				while (searchIndex != -1) {
					srcData.replace(searchIndex, searchIndex+4, "<");
					searchIndex = srcData.indexOf("&lt;", searchIndex);
				}
				searchIndex = srcData.indexOf("&gt;");
				while (searchIndex != -1) {
					srcData.replace(searchIndex, searchIndex+4, ">");
					searchIndex = srcData.indexOf("&gt;", searchIndex);
				}
				searchIndex = srcData.indexOf("&amp;");
				while (searchIndex != -1) {
					srcData.replace(searchIndex, searchIndex+5, "&");
					searchIndex = srcData.indexOf("&amp;", searchIndex);
				}
				searchIndex = srcData.indexOf("&quot;");
				while (searchIndex != -1) {
					srcData.replace(searchIndex, searchIndex+6, "\"");
					searchIndex = srcData.indexOf("&quot;", searchIndex);
				}
				searchIndex = srcData.indexOf("&frasl;");
				while (searchIndex != -1) {
					srcData.replace(searchIndex, searchIndex+7, "/");
					searchIndex = srcData.indexOf("&frasl;", searchIndex);
				}
				searchIndex = srcData.indexOf("</font>");
				while (searchIndex != -1) {
					srcData.delete(searchIndex, searchIndex+7);
					searchIndex = srcData.indexOf("</font>");
				}
				searchIndex = srcData.indexOf("<font");
				while (searchIndex != -1) {
					int searchIndex2 = srcData.indexOf(">", searchIndex);
					srcData.delete(searchIndex, searchIndex2+1);
					searchIndex = srcData.indexOf("<font", searchIndex);
				}
				searchIndex = srcData.indexOf("<b>");
				while (searchIndex != -1) {
					srcData.delete(searchIndex, searchIndex+3);
					searchIndex = srcData.indexOf("<b>", searchIndex);
				}
				searchIndex = srcData.indexOf("</b>");
				while (searchIndex != -1) {
					srcData.delete(searchIndex, searchIndex+4);
					searchIndex = srcData.indexOf("</b>", searchIndex);
				}
				searchIndex = srcData.indexOf("<i>");
				while (searchIndex != -1) {
					srcData.delete(searchIndex, searchIndex+3);
					searchIndex = srcData.indexOf("<i>", searchIndex);
				}
				searchIndex = srcData.indexOf("</i>");
				while (searchIndex != -1) {
					srcData.delete(searchIndex, searchIndex+4);
					searchIndex = srcData.indexOf("</i>", searchIndex);
				}
                StringSelection contents = new StringSelection(srcData.toString());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(contents, null);
            } catch (Exception ble) {
				System.err.println(ble);
				ble.printStackTrace();
            }
        }
	}

}
