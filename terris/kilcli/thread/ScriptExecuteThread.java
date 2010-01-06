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

import terris.kilcli.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * ScriptExecuteThread for KilCli is the class used to execute a<br>
 * script<br>
 * Ver: 1.0.1
 */

public class ScriptExecuteThread extends Thread {
    private String text;
    private Script script;
    private String target[];
    private static ArrayList scriptList = new ArrayList();

	/**
	 * Creates a ScriptExecuteThread to execute the given script
	 *
	 * @param str - script to execute
	 * @param script - The script objec to do the executing
	 */

    public ScriptExecuteThread(String str, Script sc) {
		super(str);
		StringTokenizer st = new StringTokenizer(str);
		text = st.nextToken();
		target = new String[5];
		for (int i = 0; i < 5; i++) {
			if (st.hasMoreTokens()) {
				target[i] = st.nextToken();
			}
		}
		script = sc;
		scriptList.add(script);
    }

	/**
	 * Creates a ScriptExecuteThread to execute the given script
	 *
	 * @param str - script to execute
	 * @param script - The script objec to do the executing
	 * @param target - string input given to the script, usually a target
	 */

    public ScriptExecuteThread(String str, Script sc, String[] t) {
		super(str);
		text = str;
		script = sc;
		target = t;
		scriptList.add(script);
    }

    /**
     * Runs the ScriptExecuteThread, executes specified script<br>
     * passing specified targets if needed
     */

    public void run() {
		if (target.length == 1) {
			script.runScript(text);
		} else {
			script.runScript(text, target);
		}
		script = null;
    }

    /**
     * Returns the current script list
     *
     * @return the scriptList of scripts currently being executed
     */

    public static ArrayList getList() {
		return scriptList;
	}

	/**
	 * Removes the specified script from the list
	 */

	public static void remove(Script sc) {
		int tmp = scriptList.indexOf(sc);
		if (tmp != - 1) {
			scriptList.remove(tmp);
		}
	}
}


