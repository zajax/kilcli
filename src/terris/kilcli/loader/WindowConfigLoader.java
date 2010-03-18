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
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Insets;
import java.awt.GraphicsEnvironment;

/**
 * WindowConfigLoader for KilCli is the class responsible for<br>
 * loading settings from the windowConfig.txt file<br>
 * Ver: 1.0.0
 */

public class WindowConfigLoader {
	private static String settings[] = new String[119];

	/**
	 * Loads the settings from the windowconfig.txt file
	 *
	 * @return String[] Array containing the settings
	 */

	public static String[] load(String profile) throws IOException{
		String line = "";
		int count = 0;
		StringTokenizer tokenizer;
		String slash = System.getProperty("file.separator");
		String fileName = "config" + slash + profile + slash + "windowConfig.txt";
		loadDefaults();
		File srcFile = new File(fileName);

		if (srcFile.isFile() && srcFile.canRead()) {
			try {
				BufferedReader inFile = new BufferedReader(new FileReader(srcFile));
    				while (inFile.ready()) {
					try {
						line = inFile.readLine();
					} catch (IOException ioe) {}
    				tokenizer = new StringTokenizer(line, "=");
    				tokenizer.nextToken();
    				settings[count] = tokenizer.nextToken();
    				count++;
				}
			} catch (FileNotFoundException fnfe) {}
		}
		return settings;
	}

	private static void loadDefaults() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
		screenSize.width -= (insets.left + insets.right);
		screenSize.height -= (insets.bottom + insets.top);
		String temp = "";

		//game defaults
		settings[0] = "false";
		settings[1] = "0";
		temp = "" + ((int)(screenSize.height - (screenSize.height * 0.6)));
		settings[2] = temp;
		temp = "" + ((int)(screenSize.width * 0.5));
		settings[3] = temp;
		temp = "" + ((int)(screenSize.height * 0.5) - 60);
		settings[4] = temp;

		//movement defaults
		settings[5] = "false";
		settings[6] = "1";
		settings[7] = "1";
		settings[8] = "123";
		settings[9] = "82";

		//status bar defaults
		settings[10] = "false";
		temp = "" + ((int)(screenSize.width * 0.40));
		settings[11] = temp;
		settings[12] = "0";
		temp = "" + ((int)(screenSize.width * 0.18));
		settings[13] = temp;
		temp = "" + ((int)(screenSize.height * 0.08));
		settings[14] = temp;

		//guild shouts defaults
		settings[15] = "false";
		settings[16] = "0";
		temp = "" + ((int)(screenSize.height - (screenSize.height * 0.895)));
		settings[17] = temp;
		temp = "" + ((int)(screenSize.width * 0.33));
		settings[18] = temp;
		temp = "" + ((int)(screenSize.height * 0.3));
		settings[19] = temp;

		//temple shouts defaults
		settings[20] = "false";
		temp = "" + ((int)(screenSize.width * 0.51));
		settings[21] = temp;
		temp = "" + ((int)(screenSize.height - (screenSize.height * 0.595)));
		settings[22] = temp;
		temp = "" + ((int)(screenSize.width * 0.45));
		settings[23] = temp;
		temp = "" + ((int)(screenSize.height * 0.25));
		settings[24] = temp;

		//tells defaults
		settings[25] = "false";
		temp = "" + ((int)(screenSize.width * 0.66));
		settings[26] = temp;
		temp = "" + ((int)(screenSize.height - (screenSize.height * 0.895)));
		settings[27] = temp;
		temp = "" + ((int)(screenSize.width * 0.33));
		settings[28] = temp;
		temp = "" + ((int)(screenSize.height * 0.3));
		settings[29] = temp;

		//chats defaults
		settings[30] = "false";
		temp = "" + ((int)(screenSize.width * 0.51));
		settings[31] = temp;
		temp = "" + ((int)(screenSize.height - (screenSize.height * 0.35)));
		settings[32] = temp;
		temp = "" + ((int)(screenSize.width * 0.45));
		settings[33] = temp;
		temp = "" + ((int)(screenSize.height * 0.25));
		settings[34] = temp;

		//shouts defaults
		settings[35] = "false";
		temp = "" + ((int)(screenSize.width * 0.33));
		settings[36] = temp;
		temp = "" + ((int)(screenSize.height - (screenSize.height * 0.895)));
		settings[37] = temp;
		temp = "" + ((int)(screenSize.width * 0.33));
		settings[38] = temp;
		temp = "" + ((int)(screenSize.height * 0.3));
 		settings[39] = temp;

		//command defaults
		settings[40] = "false";
		settings[41] = "90";
		temp = "" + ((int)(screenSize.height - (screenSize.height * 0.1)) - 50);
		settings[42] = temp;
		temp = "" + ((int)(screenSize.width * 0.49) - 90);
		settings[43] = temp;
		settings[44] = "45";

		//total window
		settings[45] = "false";
		temp = "" + insets.left;
		settings[46] = temp;
		temp = "" + insets.top;
		settings[47] = temp;
		temp = "" + (screenSize.width);
		settings[48] = temp;
		temp = "" + (screenSize.height);
		settings[49] = temp;

		//hhshouts defaults
		settings[50] = "true";
		temp = "" + ((int)(screenSize.width * 0.45));
		settings[51] = "5";
		settings[52] = "0";
		settings[53] = temp;
		temp = "" + ((int)(screenSize.height * 0.25));
		settings[54] = temp;

		//events defaults
		settings[55] = "true";
		temp = "" + ((int)(screenSize.width * 0.45));
		settings[56] = "15";
		settings[57] = "0";
		settings[58] = temp;
		temp = "" + ((int)(screenSize.height * 0.25));
		settings[59] = temp;

		//wails defaults
		settings[60] = "true";
		settings[61] = "0";
		settings[62] = "15";
		temp = "" + ((int)(screenSize.width * 0.45));
		settings[63] = temp;
		temp = "" + ((int)(screenSize.height * 0.25));
		settings[64] = temp;

		//title bars
		settings[65] = "true";
		settings[66] = "false";
		settings[67] = "false";
		settings[68] = "true";
		settings[69] = "true";
		settings[70] = "true";
		settings[71] = "true";
		settings[72] = "true";
		settings[73] = "true";
		settings[74] = "true";
		settings[75] = "true";
		settings[76] = "true";

		//hands defaults
		settings[77] = "false";
		temp = "" + ((int)(screenSize.width * 0.155));
		settings[78] = temp;
		settings[79] = "0";
		temp = "" + ((int)(screenSize.width * 0.20));
		settings[80] = temp;
		temp = "" + ((int)(screenSize.height * 0.08));
		settings[81] = temp;
		settings[82] = "false";

		//timers defaults
		settings[83] = "false";
		temp = "" + ((int)(screenSize.width * 0.70));
		settings[84] = temp;
		settings[85] = "0";
		temp = "" + ((int)(screenSize.width * 0.075));
		settings[86] = temp;
		temp = "" + ((int)(screenSize.height * 0.08));
		settings[87] = temp;
		settings[88] = "false";

		//effects defaults
		settings[89] = "false";
		temp = "" + ((int)(screenSize.width * 0.80));
		settings[90] = temp;
		settings[91] = "0";
		temp = "" + ((int)(screenSize.width * 0.075));
		settings[92] = temp;
		temp = "" + ((int)(screenSize.height * 0.08));
		settings[93] = temp;
		settings[94] = "false";

		//logons defaults
		settings[95] = "true";
		temp = "" + ((int)(screenSize.width * 0.51));
		settings[96] = temp;
		temp = "" + ((int)(screenSize.height - (screenSize.height * 0.35)));
		settings[97] = temp;
		temp = "" + ((int)(screenSize.width * 0.45));
		settings[98] = temp;
		temp = "" + ((int)(screenSize.height * 0.25));
		settings[99] = temp;

		//statushor defaults
		settings[100] = "true";
		settings[101] = "0";
		settings[102] = "0";
		settings[103] = "0";
		settings[104] = "0";
		settings[105] = "false";

		settings[106] = "true";

		//info defaults
		settings[107] = "false";
		temp = "" + ((int)(screenSize.width * 0.58));
		settings[108] = temp;
		settings[109] = "0";
		temp = "" + ((int)(screenSize.width * 0.075));
		settings[110] = temp;
		temp = "" + ((int)(screenSize.height * 0.08));
		settings[111] = temp;
		settings[112] = "false";

		//harden defaults
		settings[113] = "false";
		settings[114] = "0";
		settings[115] = "0";
		settings[116] = "100";
		settings[117] = "115";
		settings[118] = "false";
	}

}