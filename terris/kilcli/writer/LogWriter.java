/* KilCli, an OGC mud client program
 * Copyright (C) 2002, 3 Jason Baumeister
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

/**
 * LogWriter for KilCli is the class responsible for<br>
 * the creation and modification of the log files<br>
 * Ver: 1.0.0
 */

public class LogWriter {
	private int count = 0;
	private String fileName;
	private File dstFile;
	private PrintWriter outFile;
	private boolean created = false;
	private String[] logBuffer;
	private int logCount = 0;
	private String slash = System.getProperty("file.separator");

	/**
	 * Creates a logWriter object, with a size 10 string[]
	 */

	public LogWriter() {
		logBuffer = new String[10];
	}

	/**
	 * Logs string to buffer, then calls write, or increments counter
	 *
	 * @param txt The string to be logged
	 */

	public void log(String txt) {
		logBuffer[logCount] = txt;
		if (logCount == (logBuffer.length - 1)) {
			write();
			logCount = 0;
		} else {
			logCount++;
		}
	}

	/**
	 * Method for logging commands sent to the game
	 *
	 * @param txt String to be logged
	 */

	public void logCommand(String txt) {
		logBuffer[logCount] = "";
		if (logCount == (logBuffer.length - 1)) {
			write();
			logCount = 0;
		} else {
			logCount++;
		}
		logBuffer[logCount] = txt;
		if (logCount == (logBuffer.length - 1)) {
			write();
			logCount = 0;
		} else {
			logCount++;
		}
	}

	/**
	 * Writes information to the log file
	 */

	private void write() {
		try {
			outFile = new PrintWriter( new BufferedWriter(new FileWriter(fileName, true)));
		} catch (Exception e) {
			System.out.println("error while writing to log file");
		}
		for (int i = 0; i < logBuffer.length; i++) {
			outFile.println(logBuffer[i]);
		}
		outFile.close();
	}

	/**
	 * Method called before program exit.  Writes any leftover info that's in log buffer
	 */

	public void close() {
		for (int i = logCount; i < logBuffer.length; i++) {
			logBuffer[i] = "";
		}
		write();
	}

	/**
	 * Creates a new log file for use during that session
	 */

	public void createFile(int gn) throws IOException {
		Calendar cal = Calendar.getInstance();
		if (gn == 0) {
			fileName = "." + slash + "logs" + slash + "terris" + slash + "KilCli-log-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.YEAR) + ".txt";
		} else if (gn == 1) {
			fileName = "." + slash + "logs" + slash + "cosrin" + slash + "KilCli-log-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.YEAR) + ".txt";
		} else if (gn == 2) {
			fileName = "." + slash + "logs" + slash + "wolfenburg" + slash + "KilCli-log-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.YEAR) + ".txt";
		} else {
			fileName = "." + slash + "logs" + slash + "KilCli-log-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.YEAR) + ".txt";
		}
		dstFile = new File(fileName);
		if (!dstFile.createNewFile()) {
			while (!created) {
				count++;
				if (gn == 0) {
					fileName = "." + slash + "logs" + slash + "terris" + slash + "KilCli-log-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.YEAR) + "(" + count + ").txt";
				} else if (gn == 1) {
					fileName = "." + slash + "logs" + slash + "cosrin" + slash + "KilCli-log-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.YEAR) + "(" + count + ").txt";
				} else if (gn == 2) {
					fileName = "." + slash + "logs" + slash + "wolfenburg" + slash + "KilCli-log-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.YEAR) + "(" + count + ").txt";
				} else {
					fileName = "." + slash + "logs" + slash + "KilCli-log-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.YEAR) + "(" + count + ").txt";
				}
				dstFile = new File(fileName);
				created = dstFile.createNewFile();
			}
		}
	}
}