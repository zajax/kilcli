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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import terris.kilcli.window.*;

/**
 * WindowConfigWriter for KilCli is the class responsible for<br>
 * the creation and modification of the windowConfig.txt files <br>
 * Ver: 1.0.1
 */

public class WindowConfigWriter {
	private static String fileName;
	private static File dstFile;
	private static PrintWriter outFile;
	private static String[] settings = new String[119];
	private static String slash = System.getProperty("file.separator");

	/**
	 * Sets the profile name to know which windowConfig.txt file to write to
	 *
	 * @param profile - the name of the current profile
	 */
	public static void setProfile(String profile) {
		fileName = "config" + slash + profile + slash + "windowConfig.txt";
	}

	/**
	 * Writes settings from array to text file
	 *
	 * @param settings String[] containing settings to be written
	 */

	private static void write(String[] s) throws IOException{
		dstFile = new File(fileName);
		if (!dstFile.isFile()) {
			try {
				dstFile.createNewFile();
			} catch (IOException ioe) {
				System.err.println("could not create file " + fileName + "!!!");
			}
		}
		if (!dstFile.canWrite()) {
			System.err.println("Could not write to " + fileName);
		} else {
			PrintWriter outFile = new PrintWriter( new BufferedWriter(new FileWriter(dstFile)));
			outFile.println("GameClosed=" + settings[0]);
			outFile.println("GameX=" + settings[1]);
			outFile.println("GameY=" + settings[2]);
			outFile.println("GameWidth=" + settings[3]);
			outFile.println("GameHeigth=" + settings[4]);
			outFile.println("MovementClosed=" + settings[5]);
			outFile.println("MovementX=" + settings[6]);
			outFile.println("MovementY=" + settings[7]);
			outFile.println("MovementWidth=" + settings[8]);
			outFile.println("MovementHeight=" + settings[9]);
			outFile.println("StatusClosed=" + settings[10]);
			outFile.println("StatusX=" + settings[11]);
			outFile.println("StatusY=" + settings[12]);
			outFile.println("StatusWidth=" + settings[13]);
			outFile.println("StatusHeigth=" + settings[14]);
			outFile.println("GuildShoutsClosed=" + settings[15]);
			outFile.println("GuildShoutsX=" + settings[16]);
			outFile.println("GuildShoutsY=" + settings[17]);
			outFile.println("GuildShoutsWidth=" + settings[18]);
			outFile.println("GuildShoutsHeight=" + settings[19]);
			outFile.println("TempleShoutsClosed=" + settings[20]);
			outFile.println("TempleShoutsX=" + settings[21]);
			outFile.println("TempleShoutsY=" + settings[22]);
			outFile.println("TempleShoutsWidth=" + settings[23]);
			outFile.println("TempleShoutsHeigth=" + settings[24]);
			outFile.println("TellsClosed=" + settings[25]);
			outFile.println("TellsX=" + settings[26]);
			outFile.println("TellsY=" + settings[27]);
			outFile.println("TellsWidth=" + settings[28]);
			outFile.println("TellsHeight=" + settings[29]);
			outFile.println("ChatsClosed=" + settings[30]);
			outFile.println("ChatsX=" + settings[31]);
			outFile.println("ChatsY=" + settings[32]);
			outFile.println("ChatsWidth=" + settings[33]);
			outFile.println("ChatsHeigth=" + settings[34]);
			outFile.println("ShoutsClosed=" + settings[35]);
			outFile.println("ShoutsX=" + settings[36]);
			outFile.println("ShoutsY=" + settings[37]);
			outFile.println("ShoutsWidth=" + settings[38]);
			outFile.println("ShoutsHeight=" + settings[39]);
			outFile.println("CommandClosed=" + settings[40]);
			outFile.println("CommandX=" + settings[41]);
			outFile.println("CommandY=" + settings[42]);
			outFile.println("CommandWidth=" + settings[43]);
			outFile.println("CommandHeight=" + settings[44]);
			outFile.println("WindowClosed=" + settings[45]);
			outFile.println("WindowX=" + settings[46]);
			outFile.println("WindowY=" + settings[47]);
			outFile.println("WindowWidth=" + settings[48]);
			outFile.println("WindowHeigth=" + settings[49]);
			outFile.println("HHShoutsClosed=" + settings[50]);
			outFile.println("HHShoutsX=" + settings[51]);
			outFile.println("HHShoutsY=" + settings[52]);
			outFile.println("HHShoutsWidth=" + settings[53]);
			outFile.println("HHShoutsHeigth=" + settings[54]);
			outFile.println("EventsClosed=" + settings[55]);
			outFile.println("EventsX=" + settings[56]);
			outFile.println("EventsY=" + settings[57]);
			outFile.println("EventsWidth=" + settings[58]);
			outFile.println("EventsHeigth=" + settings[59]);
			outFile.println("WailsClosed=" + settings[60]);
			outFile.println("WailsX=" + settings[61]);
			outFile.println("WailsY=" + settings[62]);
			outFile.println("WailsWidth=" + settings[63]);
			outFile.println("WailsHeigth=" + settings[64]);
			outFile.println("GameBar=" + settings[65]);
			outFile.println("MovementBar=" + settings[66]);
			outFile.println("StatusBarBar=" + settings[67]);
			outFile.println("GuildShoutsBar=" + settings[68]);
			outFile.println("TempleShoutsBar=" + settings[69]);
			outFile.println("TellsBar=" + settings[70]);
			outFile.println("ChatsBar=" + settings[71]);
			outFile.println("ShoutsBar=" + settings[72]);
			outFile.println("CommandLineBar=" + settings[73]);
			outFile.println("HHShoutsBar=" + settings[74]);
			outFile.println("EventsBar=" + settings[75]);
			outFile.println("WailsBar=" + settings[76]);
			outFile.println("HandsClosed=" + settings[77]);
			outFile.println("HandsX=" + settings[78]);
			outFile.println("HandsY=" + settings[79]);
			outFile.println("HandsWidth=" + settings[80]);
			outFile.println("HandsHeight=" + settings[81]);
			outFile.println("HandsBar=" + settings[82]);
			outFile.println("TimersClosed=" + settings[83]);
			outFile.println("TimersX=" + settings[84]);
			outFile.println("TimersY=" + settings[85]);
			outFile.println("TimersWidth=" + settings[86]);
			outFile.println("TimersHeight=" + settings[87]);
			outFile.println("TimersBar=" + settings[88]);
			outFile.println("EffectsClosed=" + settings[89]);
			outFile.println("EffectsX=" + settings[90]);
			outFile.println("EffectsY=" + settings[91]);
			outFile.println("EffectsWidth=" + settings[92]);
			outFile.println("EffectsHeight=" + settings[93]);
			outFile.println("EffectsBar=" + settings[94]);
			outFile.println("LogonsClosed=" + settings[95]);
			outFile.println("LogonsX=" + settings[96]);
			outFile.println("LogonsY=" + settings[97]);
			outFile.println("LogonsWidth=" + settings[98]);
			outFile.println("LogonsHeight=" + settings[99]);
			outFile.println("StatusHorClosed=" + settings[100]);
			outFile.println("StatusHorX=" + settings[101]);
			outFile.println("StatusHorY=" + settings[102]);
			outFile.println("StatusHorWidth=" + settings[103]);
			outFile.println("StatusHorHeight=" + settings[104]);
			outFile.println("StatusHorBar=" + settings[105]);
			outFile.println("LogonsBar=" + settings[106]);
			outFile.println("InfoClosed=" + settings[107]);
			outFile.println("InfoX=" + settings[108]);
			outFile.println("InfoY=" + settings[109]);
			outFile.println("InfoWidth=" + settings[110]);
			outFile.println("InfoHeight=" + settings[111]);
			outFile.println("InfoBar=" + settings[112]);
			outFile.println("HardenClosed=" + settings[113]);
			outFile.println("HardenX=" + settings[114]);
			outFile.println("HardenY=" + settings[115]);
			outFile.println("HardenWidth=" + settings[116]);
			outFile.println("HardenHeight=" + settings[117]);
			outFile.println("HardenBar=" + settings[118]);
			outFile.close();
		}
	}

	/**
	 * Gets the settings from the respective windows and loads them into the array
	 *
	 * @param frameX X-part of large window location
	 * @param frameY Y-part of large window loaction
	 * @param frameWidth Width of large window
	 * @param frameHeight Height of large window
	 * @param game GameWindow to extract settings from
	 * @param movement MovementPanel to extract settings from
	 * @param status StatusBar to extract settings from
	 * @param guildShouts GuildShoutsWindow to extract settings from
	 * @param templeShouts TempleShoutsWindow to extract settings from
	 * @param tells TellsWindow to extract settings from
	 * @param chats ChatsWindow to extract settings from
	 * @param shouts ShoutsWindow to extract settings from
	 * @param commandLine CommandLine to extract settings from
	 * @param hhshouts HHShoutsWindow to extract settings from
	 * @param events EventsWindow to extract settings from
	 * @param wails WailsWindow to extract settings from
	 * @param info - InfoPanel to extract settings from
	 */

	public static void getSettings(int frameX, int frameY, int frameWidth, int frameHeight, GameWindow game, MovementPanel movement, StatusBar status, GameWindow guildshouts, GameWindow templeshouts, GameWindow tells, GameWindow chats, GameWindow shouts, CommandLine commandLine, GameWindow hhshouts, GameWindow events, GameWindow wails, HandsPanel hands, TimersPanel timers, EffectsPanel effects, GameWindow logons, StatusBarHor statusHor, InfoPanel info, HardenPanel harden) {
		settings[0] = "" + game.isClosed();
		settings[1] = "" + game.getX();
		settings[2] = "" + game.getY();
		settings[3] = "" + game.getWidth();
		settings[4] = "" + game.getHeight();
		settings[5] = "" + movement.isClosed();
		settings[6] = "" + movement.getX();
		settings[7] = "" + movement.getY();
		settings[8] = "" + movement.getWidth();
		settings[9] = "" + movement.getHeight();
		settings[10] = "" + status.isClosed();
		settings[11] = "" + status.getX();
		settings[12] = "" + status.getY();
		settings[13] = "" + status.getWidth();
		settings[14] = "" + status.getHeight();
		settings[15] = "" + guildshouts.isClosed();
		settings[16] = "" + guildshouts.getX();
		settings[17] = "" + guildshouts.getY();
		settings[18] = "" + guildshouts.getWidth();
		settings[19] = "" + guildshouts.getHeight();
		settings[20] = "" + templeshouts.isClosed();
		settings[21] = "" + templeshouts.getX();
		settings[22] = "" + templeshouts.getY();
		settings[23] = "" + templeshouts.getWidth();
		settings[24] = "" + templeshouts.getHeight();
		settings[25] = "" + tells.isClosed();
		settings[26] = "" + tells.getX();
		settings[27] = "" + tells.getY();
		settings[28] = "" + tells.getWidth();
		settings[29] = "" + tells.getHeight();
		settings[30] = "" + chats.isClosed();
		settings[31] = "" + chats.getX();
		settings[32] = "" + chats.getY();
		settings[33] = "" + chats.getWidth();
		settings[34] = "" + chats.getHeight();
		settings[35] = "" + shouts.isClosed();
		settings[36] = "" + shouts.getX();
		settings[37] = "" + shouts.getY();
		settings[38] = "" + shouts.getWidth();
		settings[39] = "" + shouts.getHeight();
		settings[40] = "" + commandLine.isClosed();
		settings[41] = "" + commandLine.getX();
		settings[42] = "" + commandLine.getY();
		settings[43] = "" + commandLine.getWidth();
		settings[44] = "" + commandLine.getHeight();
		settings[45] = "false";
		settings[46] = "" + frameX;
		settings[47] = "" + frameY;
		settings[48] = "" + frameWidth;
		settings[49] = "" + frameHeight;
		settings[50] = "" + hhshouts.isClosed();
		settings[51] = "" + hhshouts.getX();
		settings[52] = "" + hhshouts.getY();
		settings[53] = "" + hhshouts.getWidth();
		settings[54] = "" + hhshouts.getHeight();
		settings[55] = "" + events.isClosed();
		settings[56] = "" + events.getX();
		settings[57] = "" + events.getY();
		settings[58] = "" + events.getWidth();
		settings[59] = "" + events.getHeight();
		settings[60] = "" + wails.isClosed();
		settings[61] = "" + wails.getX();
		settings[62] = "" + wails.getY();
		settings[63] = "" + wails.getWidth();
		settings[64] = "" + wails.getHeight();
		settings[65] = "" + game.checkBar();
		settings[66] = "" + movement.checkBar();
		settings[67] = "" + status.checkBar();
		settings[68] = "" + guildshouts.checkBar();
		settings[69] = "" + templeshouts.checkBar();
		settings[70] = "" + tells.checkBar();
		settings[71] = "" + chats.checkBar();
		settings[72] = "" + shouts.checkBar();
		settings[73] = "" + commandLine.checkBar();
		settings[74] = "" + hhshouts.checkBar();
		settings[75] = "" + events.checkBar();
		settings[76] = "" + wails.checkBar();
		settings[77] = "" + hands.isClosed();
		settings[78] = "" + hands.getX();
		settings[79] = "" + hands.getY();
		settings[80] = "" + hands.getWidth();
		settings[81] = "" + hands.getHeight();
		settings[82] = "" + hands.checkBar();
		settings[83] = "" + timers.isClosed();
		settings[84] = "" + timers.getX();
		settings[85] = "" + timers.getY();
		settings[86] = "" + timers.getWidth();
		settings[87] = "" + timers.getHeight();
		settings[88] = "" + timers.checkBar();
		settings[89] = "" + effects.isClosed();
		settings[90] = "" + effects.getX();
		settings[91] = "" + effects.getY();
		settings[92] = "" + effects.getWidth();
		settings[93] = "" + effects.getHeight();
		settings[94] = "" + effects.checkBar();
		settings[95] = "" + logons.isClosed();
		settings[96] = "" + logons.getX();
		settings[97] = "" + logons.getY();
		settings[98] = "" + logons.getWidth();
		settings[99] = "" + logons.getHeight();
		settings[100] = "" + statusHor.isClosed();
		settings[101] = "" + statusHor.getX();
		settings[102] = "" + statusHor.getY();
		settings[103] = "" + statusHor.getWidth();
		settings[104] = "" + statusHor.getHeight();
		settings[105] = "" + statusHor.checkBar();
		settings[106] = "" + logons.checkBar();
		settings[107] = "" + info.isClosed();
		settings[108] = "" + info.getX();
		settings[109] = "" + info.getY();
		settings[110] = "" + info.getWidth();
		settings[111] = "" + info.getHeight();
		settings[112] = "" + info.checkBar();
		settings[113] = "" + harden.isClosed();
		settings[114] = "" + harden.getX();
		settings[115] = "" + harden.getY();
		settings[116] = "" + harden.getWidth();
		settings[117] = "" + harden.getHeight();
		settings[118] = "" + harden.checkBar();
		try {
			write(settings);
		} catch (IOException ioe) {
			game.Write("help me!!!");
		}
	}
}