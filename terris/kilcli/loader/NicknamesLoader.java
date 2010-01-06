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
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


/**
 * NicknamesLoader for KilCli is the class used to load/reload<br>
 * the nicknames settings, from the alias.txt file<br>
 * Ver: 1.0.0
 */

public class NicknamesLoader {

	/**
	 * Loads aliases from the alias.txt file
	 *
	 * @return ArrayList Containing all aliases
	 */

	public static ArrayList load(String profile) throws IOException {
		StringTokenizer tokenizer;
		String line = "";
		ArrayList nicknames = new ArrayList();
		int count = 0;
		String slash = System.getProperty("file.separator");

		File srcFile = new File("config" + slash + profile + slash + "nicknames.txt");
		if (!srcFile.exists())
		{
			//display some kind of message that the file doesn't exist
		} else if (!srcFile.isFile() || !srcFile.canRead())
        {
			//display error that it can't read from the file
		} else {
			try {
				BufferedReader inFile = new BufferedReader(new FileReader(srcFile));
    			while (inFile.ready()) {
					try {
						line = inFile.readLine();
					} catch (IOException ioe) {}
    				tokenizer = new StringTokenizer(line, "|");
    				if (tokenizer.hasMoreTokens()) {
    					nicknames.add(tokenizer.nextToken());
					} else {
						//nicknames.add(null);
						break;
					}
					if (tokenizer.hasMoreTokens()) {
    					nicknames.add(tokenizer.nextToken());
					} else {
						nicknames.remove(nicknames.size()-1);
						break;
					}
				}
			} catch (FileNotFoundException fnfe) {}
		}
		return nicknames;
	}
}