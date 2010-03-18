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

package terris.kilcli.writer;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * CSSWriter for KilCli is the class responsible for<br>
 * rewriting the *window.css files<br>
 * Ver: 1.0.0
 */

public class CSSWriter {
	private static String slash = System.getProperty("file.separator");
	//private static String fileName = "css" + slash;
	private static PrintWriter outFile;
	private static String fontWeight = "";
	private static String fontStyle = "";

	/**
	 * Update a *window.css file
	 */

	public static void setFont(String window, String name, int size, int style, int red, int green, int blue) {
		String line = "";
		int searchIndex = 0;
		if (style == 3) {
			fontWeight = "bold";
			fontStyle = "italic";
		} else if (style == 2) {
			fontWeight = "normal";
			fontStyle = "italic";
		} else if (style == 1) {
			fontWeight = "bold";
			fontStyle = "normal";
		} else {
			fontWeight = "normal";
			fontStyle = "normal";
		}
		try {
			BufferedReader inFile = new BufferedReader(new FileReader(window));
			line = inFile.readLine();
			inFile.close();
			searchIndex = line.toLowerCase().indexOf("background-color:");
			line = line.substring(searchIndex + 19, line.length());
			outFile = new PrintWriter( new BufferedWriter(new FileWriter(window)));
		} catch (IOException ioe) {
			System.err.println(ioe);
		}
		String cssLine = "body { font-family: " + name +
							"; font-size: " + size +
							"pt; color: #" + intToHex(red) + intToHex(green) +
							intToHex(blue) + "; font-style: " + fontStyle +
							"; font-weight: " + fontWeight +
							"; background-color: #" + line;
		outFile.println(cssLine);
		outFile.flush();
		outFile.close();
	}

	public static void setColor(String window, int red, int green, int blue) {
		String line = "";
		int searchIndex = 0;
		try {
			BufferedReader inFile = new BufferedReader(new FileReader(window));
			line = inFile.readLine();
			inFile.close();
			searchIndex = line.indexOf("background-color:");
			line = line.substring(0, searchIndex);

			line = line + "background-color: #" + intToHex(red) + intToHex(green) + intToHex(blue) + ";}";
			outFile = new PrintWriter( new BufferedWriter(new FileWriter(window)));
			outFile.println(line);
			outFile.flush();
			outFile.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	private static String intToHex(int color) {
		if (color < 16) {
			return "0" + Integer.toString(color, 16);
		}
		return Integer.toString(color, 16);
	}
}