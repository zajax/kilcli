/* KilCli, an OGC mud client program
 * Copyright (C) 2002, 3 Jason Baumeister
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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * ConfigWriter for KilCli is the class responsible for<br>
 * the updating the configuration and colors<br>
 * Ver: 1.0.2
 */

public class ConfigWriter {
	private static String fileName;
	private static String colorFileName;
	private static File dstFile;
	private static PrintWriter outFile;
	private static String slash = System.getProperty("file.separator");

	/**
	 * Sets the profile name to know which config.txt & colors.txt files to write to
	 *
	 * @param profile - the name of the current profile
	 */
	public static void setProfile(String profile) {
		fileName = "config" + slash + profile + slash + "config.txt";
		colorFileName = "config" + slash + profile + slash + "colors.txt";
	}

	/**
	 * Writes the configuration settings to the config.txt<br>
	 * file.
	 *
	 * @param settings The array that stores config settings
	 */

	public static void configWrite(String[] settings) throws IOException{

		dstFile = new File(fileName);
		if (!dstFile.isFile()) {
			try {
				dstFile.createNewFile();
			} catch (IOException ioe) {}
		}
		if (!dstFile.canWrite()) {
			//print some kind of error
		} else {
			for (int i = 0; i < settings.length; i++) {
				if (settings[i].length() < 1) {
					settings[i] = "null";
				}
			}
			PrintWriter outFile = new PrintWriter( new BufferedWriter(new FileWriter(dstFile)));
			outFile.println("StartSound=" + settings[0]);
			outFile.println("Log=" + settings[1]);
			outFile.println("HPWarning=" + settings[2]);
			outFile.println("SPWarning=" + settings[3]);
			outFile.println("HPWarningLevel=" + settings[4]);
			outFile.println("SPWarningLevel=" + settings[5]);
			outFile.println("GShoutsPrefix=" + settings[6]);
			outFile.println("TShoutsPrefix=" + settings[7]);
			outFile.println("TellsPrefix=" + settings[8]);
			outFile.println("HHShoutsPrefix=" + settings[9]);
			outFile.println("ShoutsPrefix=" + settings[10]);
			outFile.println("ChatsPrefix=" + settings[11]);
			outFile.println("WailsPrefix=" + settings[12]);
			outFile.println("EventsPrefix=" + settings[13]);
			outFile.println("QuestsPrefix=" + settings[14]);
			outFile.println("HPYellowLevel=" + settings[15]);
			outFile.println("HPRedLevel=" + settings[16]);
			outFile.println("SPYellowLevel=" + settings[17]);
			outFile.println("SPRedLevel=" + settings[18]);
			outFile.println("OldStyleCommandLine=" + settings[19]);
			outFile.println("Theme=" + settings[20]);
			outFile.println("EnableTriggers=" + settings[21]);
			outFile.println("StartupSoundFile=" + settings[22]);
			outFile.println("EnableSound=" + settings[23]);
			outFile.println("LocalEcho=" + settings[24]);
			outFile.println("EchoPrefix=" + settings[25]);
			outFile.println("HelpAddress=" + settings[26]);
			outFile.println("ChatsText=" + settings[27]);
			outFile.println("EventsText=" + settings[28]);
			outFile.println("GShoutsText=" + settings[29]);
			outFile.println("HHShoutsText=" + settings[30]);
			outFile.println("ShoutsText=" + settings[31]);
			outFile.println("QuestsText=" + settings[32]);
			outFile.println("TShoutsText=" + settings[33]);
			outFile.println("WailsText=" + settings[34]);
			outFile.println("TellsText=" + settings[35]);
			outFile.println("LogonsPrefix=" + settings[36]);
			outFile.println("LogonsText=" + settings[37]);
			outFile.println("EnableNickNames=" + settings[38]);
			outFile.println("SoundDir=" + settings[39]);
			outFile.println("ProxyEnabled=" + settings[40]);
			outFile.println("ProxyServer=" + settings[41]);
			outFile.println("ProxyPort=" + settings[42]);
			outFile.println("ReadBuffer=" + settings[43]);
			outFile.println("OldStyleChats=" + settings[44]);
			outFile.println("OldStyleEffects=" + settings[45]);
			outFile.println("OldStyleEvents=" + settings[46]);
			outFile.println("OldStyleGame=" + settings[47]);
			outFile.println("OldStyleGShouts=" + settings[48]);
			outFile.println("OldStyleHands=" + settings[49]);
			outFile.println("OldStyleHHShouts=" + settings[50]);
			outFile.println("OldstyleLogons=" + settings[51]);
			outFile.println("OldStyleMovement=" + settings[52]);
			outFile.println("OldStyleShouts=" + settings[53]);
			outFile.println("OldStyleQuests=" + settings[54]);
			outFile.println("OldStyleStatus=" + settings[55]);
			outFile.println("OldStyleStatusHor=" + settings[56]);
			outFile.println("OldStyleTells=" + settings[57]);
			outFile.println("OldStyleTimers=" + settings[58]);
			outFile.println("OldStyleTShouts=" + settings[59]);
			outFile.println("OldStyleWails=" + settings[60]);
			outFile.println("BindPageUpDown=" + settings[61]);
			outFile.println("OldStyleInfo=" + settings[62]);
			outFile.println("StartupScript=" + settings[63]);
			outFile.println("proxyType=" + settings[64]);
			outFile.println("proxyUsername=" + settings[65]);
			outFile.println("proxyPassword=" + settings[66]);
			outFile.println("TextBuffer=" + settings[67]);
			outFile.println("LogonScript=" + settings[68]);
			outFile.println("NetMelee=" + settings[69]);
			outFile.println("ChatStamp=" + settings[70]);
			outFile.println("GameStamp=" + settings[71]);
			outFile.println("GShoutStamp=" + settings[72]);
			outFile.println("HHShoutStamp=" + settings[73]);
			outFile.println("LogonStamp=" + settings[74]);
			outFile.println("ShoutStamp=" + settings[75]);
			outFile.println("TellStamp=" + settings[76]);
			outFile.println("TShoutStamp=" + settings[77]);
			outFile.println("WailStamp=" + settings[78]);
			outFile.println("AliasFlag=" + settings[79]);
			outFile.println("OldStyleHarden=" + settings[80]);
			outFile.println("MonitorHarden=" + settings[81]);
			outFile.println("ChatToKChats=" + settings[82]);
			outFile.println("NumpadEnterSendCommand=" + settings[83]);
			outFile.println("ConnectToKChat=" + settings[84]);
			outFile.println("KChatTell=" + settings[85]);
			outFile.println("KChatChat=" + settings[86]);
			outFile.close();

		}
	}

	/**
	 * Writes the color settings to the colors.txt<br>
	 * file.
	 *
	 * @param settings The array that stores color settings
	 */

	public static void colorsWrite(String[] colors) throws IOException{

		dstFile = new File(colorFileName);
		if (!dstFile.isFile()) {
			try {
				dstFile.createNewFile();
			} catch (IOException ioe) {}
		}
		if (!dstFile.canWrite()) {
			//print some kind of error
		} else {
			PrintWriter outFile = new PrintWriter( new BufferedWriter(new FileWriter(dstFile)));
			outFile.println("R=" + colors[0]);
			outFile.println("r=" + colors[1]);
			outFile.println("G=" + colors[2]);
			outFile.println("g=" + colors[3]);
			outFile.println("W=" + colors[4]);
			outFile.println("w=" + colors[5]);
			outFile.println("C=" + colors[6]);
			outFile.println("c=" + colors[7]);
			outFile.println("V=" + colors[8]);
			outFile.println("v=" + colors[9]);
			outFile.println("B=" + colors[10]);
			outFile.println("b=" + colors[11]);
			outFile.println("O=" + colors[12]);
			outFile.println("o=" + colors[13]);
			outFile.println("Y=" + colors[14]);
			outFile.println("y=" + colors[15]);
			outFile.println("P=" + colors[16]);
			outFile.println("p=" + colors[17]);
			outFile.println("K1=" + colors[18]);
			outFile.println("K2=" + colors[19]);
			outFile.println("k1=" + colors[20]);
			outFile.println("k2=" + colors[21]);
			outFile.println("T=" + colors[22]);
			outFile.println("t=" + colors[23]);
			outFile.close();

		}
	}

	/**
	 * Writes skin and theme settings to the display.txt<br>
	 * file.
	 *
	 * @param claf - The current look and feel.
	 * @param theme - The current theme.
	 */

	public static void displayWrite(String claf, String theme, int game) throws IOException {
		dstFile = new File("config" + slash + "display.txt");
		if (!dstFile.isFile()) {
			try {
				dstFile.createNewFile();
			} catch (IOException ioe) {}
		}
		if (!dstFile.canWrite()) {
			//print some kind of error
		} else {
			PrintWriter outFile = new PrintWriter( new BufferedWriter(new FileWriter(dstFile)));
			outFile.println(claf + "|" + theme + "|" + game);
			outFile.close();
		}
	}

}