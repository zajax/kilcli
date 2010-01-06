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

package terris.kilcli.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * ConfigLoader for KilCli is the class responsible for<br>
 * loading the configuration options and colors<br>
 *
 * Ver: 1.0.2
 */

public class ConfigLoader {
	static String temp = "";


	private static String[] loadDefaults() {
		String[] settings = new String[87];
		settings[0] = "false";
		settings[1] = "false";
		settings[2] = "true";
		settings[3] = "true";
		settings[4] = "30";
		settings[5] = "30";
		settings[6] = "GShouts: ";
		settings[7] = "TShouts: ";
		settings[8] = "Tells: ";
		settings[9] = "HHShouts: ";
		settings[10] = "Shouts: ";
		settings[11] = "Chats: ";
		settings[12] = "Wails: ";
		settings[13] = "Events: ";
		settings[14] = "Quests: ";
		settings[15] = "50";
		settings[16] = "25";
		settings[17] = "50";
		settings[18] = "25";
		settings[19] = "true";
		settings[20] = "themes/Brown Sugar.thm";
		settings[21] = "true";
		settings[22] = "./sounds/startup.mp3";
		settings[23] = "true";
		settings[24] = "true";
		settings[25] = " ^R  INPUT   ----> ";
		settings[26] = "http://s88558327.onlinehome.us/webhelp/";
		settings[27] = "C";
		settings[28] = "E";
		settings[29] = "G";
		settings[30] = "H";
		settings[31] = "M";
		settings[32] = "Q";
		settings[33] = "T";
		settings[34] = "W";
		settings[35] = "t";
		settings[36] = "Logons: ";
		settings[37] = "L";
		settings[38] = "false";
		settings[39] = "./sounds/";
		settings[40] = "false";
		settings[41] = "127.0.0.1";
		settings[42] = "1080";
		settings[43] = "2048";
		settings[44] = "false";
		settings[45] = "false";
		settings[46] = "false";
		settings[47] = "false";
		settings[48] = "false";
		settings[49] = "false";
		settings[50] = "false";
		settings[51] = "false";
		settings[52] = "false";
		settings[53] = "false";
		settings[54] = "false";
		settings[55] = "false";
		settings[56] = "false";
		settings[57] = "false";
		settings[58] = "false";
		settings[59] = "false";
		settings[60] = "false";
		settings[61] = "false";
		settings[62] = "false";
		settings[63] = "";
		settings[64] = "Socks4";
		settings[65] = "";
		settings[66] = "";
		settings[67] = "30000";
		settings[68] = "";
		settings[69] = "false";
		settings[70] = "false";
		settings[71] = "false";
		settings[72] = "false";
		settings[73] = "false";
		settings[74] = "false";
		settings[75] = "false";
		settings[76] = "false";
		settings[77] = "false";
		settings[78] = "false";
		settings[79] = "@";
		settings[80] = "false";
		settings[81] = "false";
		settings[82] = "false";
		settings[83] = "false";
		settings[84] = "true";
		settings[85] = "t";
		settings[86] = "C";
		return settings;
	}

	private static String[] loadDefaultColors() {
		String[] colors = new String[24];
		colors[0]="ff0000";
		colors[1]="8b0000";
		colors[2]="00ff7f";
		colors[3]="006400";
		colors[4]="ffffff";
		colors[5]="696969";
		colors[6]="00ffff";
		colors[7]="008b8b";
		colors[8]="ee82ee";
		colors[9]="9400d3";
		colors[10]="0000ff";
		colors[11]="00008b";
		colors[12]="ffa500";
		colors[13]="ff8c00";
		colors[14]="ffff00";
		colors[15]="9acd32";
		colors[16]="ffc0cb";
		colors[17]="ff1493";
		colors[18]="808080";
		colors[19]="ff0000";
		colors[20]="808080";
		colors[21]="8b0000";
		colors[22]="a52a2a";
		colors[23]="8b4513";
		return colors;
	}

	/**
	 * Loads configuration from the config.txt file
	 *
	 * @return String[] with configuration options stored
	 */

	public static String[] load(String profile) throws IOException{
		String slash = System.getProperty("file.separator");
		String fileName = "config" + slash + profile + slash + "config.txt";
		String line = "";
		int count = 0;
		String[] settings;
		settings = loadDefaults();
		StringTokenizer tokenizer;
		File srcFile = new File(fileName);

		if (srcFile.isFile() && srcFile.canRead()) {
			try {
				BufferedReader inFile = new BufferedReader(new FileReader(srcFile));
				while (inFile.ready() && (count < settings.length)) {
					try {
						line = inFile.readLine();
					} catch (IOException ioe) {}
					tokenizer = new StringTokenizer(line, "=");
					tokenizer.nextToken();
					temp = tokenizer.nextToken();
					if (!temp.equals("null")) {
						settings[count] = temp;
					} else {
						settings[count] = "";
					}
					count++;
				}
			} catch (Exception fnfe) {}
		}
		count = 0;
		return settings;
	}

	/**
	 * Loads colors from the colors.txt file
	 *
	 * @return String[] with the colors stored
	 */

	public static String[] loadColors(String profile) throws IOException{
		String slash = System.getProperty("file.separator");
		String fileName = "config" + slash + profile + slash + "colors.txt";
		String line = "";
		int count = 0;
		StringTokenizer tokenizer;
		File srcFile = new File(fileName);
		String[] colors;
		colors = loadDefaultColors();

		if (srcFile.isFile() && srcFile.canRead()) {
			try {
				BufferedReader inFile = new BufferedReader(new FileReader(srcFile));
				while (inFile.ready()) {
					try {
						line = inFile.readLine();
					} catch (IOException ioe) {}
					tokenizer = new StringTokenizer(line, "=");
					tokenizer.nextToken();
					colors[count] = tokenizer.nextToken();
					count++;
				}
			} catch (FileNotFoundException fnfe) {}
		}
		count = 0;
		return colors;
	}

}