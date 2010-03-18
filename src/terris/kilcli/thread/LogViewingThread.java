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

import java.io.File;
import terris.kilcli.gui.*;

/**
 * LogViewingThread for KilCli is the class used to view<br>
 * a log file in
 * Ver: 0.3.6
 */

public class LogViewingThread extends Thread {
    private LogViewer log;
    private File logFile;
    private String logFileName;
	private int calledFrom = -1;

	/**
	 * Creates a LogViewingThread to view the given log file in
	 *
	 * @param str - The name of the thread
	 * @param file - the log file to be viewed
	 * @param c - the calledFrom flag
	 */

    public LogViewingThread(String str, File file, int c) {
		super(str);
		logFileName = str;
		logFile = file;
		calledFrom = c;

    }

    /**
     * Runs the LogViewingThread, creates a LogViewer<br>
     * and initializes the process
     */

    public void run() {
		log = new LogViewer(logFileName, logFile, calledFrom);
	    //try to create a log file
		log.initialize();
    }

}


