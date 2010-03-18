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

package terris.kilcli.thread;

import java.io.IOException;
import terris.kilcli.resource.*;
import java.util.ArrayList;

/**
 * PlayThread for KilCli is the class used to create a<br>
 * thread to play mp3s in<br>
 * Ver: 1.0.0
 */

public class PlayThread extends Thread {
    private MP3Thread mp3;
    private String fileName;
    private static boolean stop = false;
    private static ArrayList list = new ArrayList();

	/**
	 * Creates a PlayThread to play the given file name
	 *
	 * @param str - file name to play
	 * @param mp3In - The calling MP3Thread
	 */

    public PlayThread(String str, MP3Thread mp3IN) {
		super(str);
		fileName = str;
		mp3 = mp3IN;
    }

    /**
     * Runs the PlayThread, plays specified mp3 file<br>
     * Then starts the calling MP3Thread for playback control
     */

    public void run() {
		stop = false;
		if (list.indexOf(fileName) < 0) {
			list.add(fileName);
			if (fileName.endsWith(".mp3")) {
				AudioPlayer.playMP3(fileName);
			} else {
				AudioPlayer.playAudioFile(fileName);
			}
			if ((mp3 != null) && (!stop)) {
    			mp3.run();
			}
			if (list.size() > 0) {
				list.remove(list.indexOf(fileName));
			}
		}
    }

    public static void setStop(boolean s) {
		stop = s;
		list.clear();
	}
}


