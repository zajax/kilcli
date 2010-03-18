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
 * MP3ListLoader for KilCli is the class used to load the<br>
 * list of mp3s from the mp3list.txt file<br>
 * Ver: 0.3.7
 */

public class MP3ListLoader {
	private ArrayList mp3s;
	private String line;
	private String slash = System.getProperty("file.separator");
	private String playList = "mp3list.txt";

	/**
	 * Loads list of mp3s into an ArrayList from the specified file<br>
	 *
	 * @return ArrayList Containing mp3s to be played
	 */

	public ArrayList load(String profile, String playlist) throws IOException {
		mp3s = new ArrayList();
		StringTokenizer tokenizer;
		File srcFile = new File("config" + slash + profile + slash + "playlists" + slash + playlist);
		if (!srcFile.exists())
		{
			//display some kind of message that the file doesn't exist
		} else if (!srcFile.isFile() || !srcFile.canRead()) {
			//display error that it can't read from the file
		} else {
			try {
				BufferedReader inFile = new BufferedReader(new FileReader(srcFile));
    			while (inFile.ready()) {
					try {
						line = inFile.readLine();
					} catch (IOException ioe) {System.out.println("error reading file");}
					File tempFile = new File(line);
					checkFile(tempFile, line);
				}
			} catch (FileNotFoundException fnfe) {System.out.println("error finding file");
			}
		}
		return mp3s;
	}

	private void checkFile(File tempFile, String path) {
		if (tempFile.isDirectory()) {
			String[] listOfFiles = tempFile.list();
			for (int i = 0; i < listOfFiles.length; i++) {
				File tempFile2 = new File(line + slash + listOfFiles[i]);
				if (tempFile2.isDirectory()) {
					checkFile(tempFile2, path + slash + listOfFiles[i]);
				} else {
					mp3s.add(path + slash + listOfFiles[i]);
				}
			}
		} else {
			mp3s.add(path);
		}
	}
}