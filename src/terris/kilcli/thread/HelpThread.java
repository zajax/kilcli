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

import terris.kilcli.window.*;

/**
 * HelpThread for KilCli is the class used to create a<br>
 * thread to view external help files in<br>
 * Ver: 1.0.1
 */

public class HelpThread extends Thread {
    private String file;
    private HelpWindow help;
	private int setPage = 0;

	/**
	 * Creates a LogThread to write
	 *
	 * @param str - name of the thread
	 * @param gn - gameNumber being logged
	 */

    public HelpThread(String str, HelpWindow win) {
		super(str);
		file = str;
		help = win;
    }

    public HelpThread(String str, HelpWindow win, int page) {
		super(str);
		file = str;
		help = win;
		setPage = page;
	}

    /**
     * Runs the LogThread, creates the needed .txt file
     */

    public void run() {
		if (setPage == 1) {
			StringBuffer buf = new StringBuffer(file);
			int search = buf.indexOf(" ");
			while (search > -1) {
				buf.replace(search, search+1, "%20");
				search = buf.indexOf(" ", search);
			}
			file = buf.toString();
			help.setPage("http://dictionary.reference.com/search?q=" + file, false);
		} else if (setPage == 2) {
			StringBuffer buf = new StringBuffer(file);
			int search = buf.indexOf(" ");
			while (search > -1) {
				buf.replace(search, search+1, "%20");
				search = buf.indexOf(" ", search);
			}
			file = buf.toString();
			help.setPage("http://www.spellcheck.net/cgi-bin/spell.exe?action=CHECKWORD&string=" + file, false);
		} else if (setPage == 3) {
			StringBuffer buf = new StringBuffer(file);
			int search = buf.indexOf(" ");
			while (search > -1) {
				buf.replace(search, search+1, "%20");
				search = buf.indexOf(" ", search);
			}
			file = buf.toString();
			help.setPage("http://thesaurus.reference.com/search?q=" + file, false);
		} else {
			help.setURL(file);
		}
    }

}


