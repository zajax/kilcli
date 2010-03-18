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
import terris.kilcli.writer.*;

/**
 * LogThread for KilCli is the class used to create a<br>
 * thread to write logs files in<br>
 * Ver: 0.3.6
 */

public class LogThread extends Thread {
    private static LogWriter log;
    private static int gameNumber;

	/**
	 * Creates a LogThread to write
	 *
	 * @param str - name of the thread
	 * @param gn - gameNumber being logged
	 */

    public LogThread(String str, int gn) {
		super(str);
		gameNumber = gn;

    }

    /**
     * Runs the LogThread, creates the needed .txt file
     */

    public void run() {
		log = new LogWriter();
	    //try to create a log file
	    try {
	    	log.createFile(gameNumber);
		} catch (IOException ioe) {}
    }

    /**
     * Passes the given text to the log object
     *
     * @param txt - the text to be logged
     */

    public static void log(String txt) {
		log.log(txt);
	}

    /**
     * Passes the given input to the log object
     *
     * @param txt - the command to be logged
     */

	public static void logCommand(String txt) {
		log.logCommand(txt);
	}

    /**
     * Closes out the logger
     */

	public static void close() {
		log.close();
	}
}


