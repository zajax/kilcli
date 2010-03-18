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

import java.util.ArrayList;
import java.io.IOException;
import java.util.Random;
import terris.kilcli.resource.*;
import terris.kilcli.loader.*;
import terris.kilcli.thread.*;
import terris.kilcli.KilCli;


/**
 * MP3Thread for KilCli is the thread used to control the<br>
 * playing of mp3s<br>
 * Ver: 1.0.0
 */

public class MP3Thread extends Thread {
    private static ArrayList mp3s = new ArrayList();
    private static boolean stopped = false;
	private static int count = -1;
	private static Thread mp3;
	private static boolean empty = true;
	private static MP3ListLoader mll;
	private static boolean random = false;
	private static Random generator = new Random();

    private String fileName;

	/**
	 * Creates an MP3Thread with the given name
	 *
	 * @param str - Name of thread
	 * @param c - Count, track number to start playing on
	 */

    public MP3Thread(String str, int c) {
		super(str);
		count = c;
		if (mp3s.size() > 0) {
			fileName = (String)(mp3s.get(count));
		}
		stopped = false;
    }

	/**
	 * Creates an MP3Thread with the given name, to play<br>
	 * the specific file
	 *
	 * @param str - Name of thread
	 * @param track - The file name to play
	 */

    public MP3Thread(String str, String track) {
		super(str);
		fileName = track;
		stopped = false;
		new PlayThread(fileName, null).start();

    }

	/**
	 * Creates an MP3Thread with the given name, to play<br>
	 * the specific file
	 *
	 * @param str - Name of thread
	 * @param track - The file name to play
	 */

    public MP3Thread(String str, String playlist, int c, boolean r) {
		super(str);
		updateMP3s(playlist);
		count = c;
		random = r;
		if (!empty) {
			fileName = (String)(mp3s.get(count));
		}
		stopped = false;
    }

    /**
     * Runs the MP3Thread, plays once if only 1 song in ArrayList<br>
     * Loops playback if multiple entries are found
     */

    public void run() {
		if (empty) {
			mp3 = null;
			stopped = true;
		} else {
			mp3 = Thread.currentThread();
		}
		if ((mp3 != null) && !stopped) new PlayThread(fileName, this).start();
		if (random) {
			count = generator.nextInt(mp3s.size());
		} else {
			if (count == (mp3s.size() - 1)) {
				count = 0;
			} else {
				count++;
			}
		}
		if (mp3s.size() > 0) {
			fileName = (String)(mp3s.get(count));
		} else {
			count = -1;
		}
		if ((mp3s.size() == 1) || (mp3s.size() == 0)) {
			stopped = true;
		}
    }

	/**
	 * Updates the mp3 list that is used for playback
	 * from the specified file
	 */

	public void updateMP3s(String playlist) {
		mll = new MP3ListLoader();
		try {
			mp3s = mll.load(KilCli.getProfile(), playlist);
			empty = false;
		} catch (IOException ioe) {}
		if (mp3s.size() <= 0) {
			empty = true;
		}
	}

	/**
	 * Stops the currently playing MP3
	 */

	public static int stopMP3() {
		stopped = true;
		PlayThread.setStop(true);
		AudioPlayer.stop();
		return count;
	}

	/**
	 * Stops the current, MP3, skips to the next track, and plays that
	 */

	public void nextMP3() {
		if (count != -1) {
			PlayThread.setStop(true);
			AudioPlayer.stop();
			fileName = (String)(mp3s.get(count));
			stopped = false;
			run();
		}
	}

	/**
	 * Stops the current, MP3, skips to the prev track, and plays that
	 */

	public void prevMP3() {
		if (count != -1) {
			PlayThread.setStop(true);
			AudioPlayer.stop();
			if (count == 1) {
				count = mp3s.size() - 1;
			} else if (count == 0) {
				count = mp3s.size() - 2;
			} else {
				count -= 2;
			}
			fileName = (String)(mp3s.get(count));
			stopped = false;
			run();
		}
	}

	/**
	 * Prints the current playlist
	 */

	public static String printMP3() {
		String temp = "";
		if (mp3s.size() > 0) {
			for (int i = 0; i < mp3s.size(); i++) {
				temp = temp + mp3s.get(i) + "<br>";
			}
		} else {
			temp = "&lt;empty>";
		}
		return temp;
	}
}


