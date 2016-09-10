/* KilCli, an OGC mud client program
 * Copyright (C) 2002, 2003 Jason Baumeister
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

package terris.kilcli.resource;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.net.Socket;

/**
 * SMTPClient for KilCli is the class used to send bug reports<br>
 * Ver: 1.0.0
 */

public class SMTPClient {

    private static Socket smtpSocket = null;
    private static BufferedOutputStream os = null;
   	private static BufferedReader is = null;
   	private static StackTraceElement[] trace;

     public static void sendError(String name, Exception e) { }

    /*
    public static void sendError(String name, Exception e) {
    	
		int n = JOptionPane.showConfirmDialog(KilCliThread.getKilCli(), "A " + e.toString() + " occurred\nDo you wish to send a error report?", "Exception Occured", JOptionPane.YES_NO_OPTION);
		if (n != JOptionPane.YES_OPTION) {
			return;
		}
    	try {
    	    smtpSocket = new Socket("www.kilcli.com", 25);
    	    os = new BufferedOutputStream(smtpSocket.getOutputStream());
    	    is = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
    	} catch (Exception eee) {}


		if (smtpSocket != null && os != null && is != null) {
    		try {

				String temp = "HELO\n";
				os.write(temp.getBytes("ISO-8859-1"), 0, temp.length());
		        temp = "MAIL From: bugs@kilcli.com\n";
		        os.write(temp.getBytes("ISO-8859-1"), 0, temp.length());
		        temp = "RCPT To: bugs@kilcli.com\n";
		        os.write(temp.getBytes("ISO-8859-1"), 0, temp.length());
		        temp = "DATA\n";
		        os.write(temp.getBytes("ISO-8859-1"), 0, temp.length());
		        temp = "From: " + name + "\n";
		        os.write(temp.getBytes("ISO-8859-1"), 0, temp.length());
		        temp = "To: " + name + "\n";
		        os.write(temp.getBytes("ISO-8859-1"), 0, temp.length());
		        temp = "Subject: Error Report\n";
		        os.write(temp.getBytes("ISO-8859-1"), 0, temp.length());
		        temp = "I am: " + smtpSocket.getLocalAddress() + "\n";
		        os.write(temp.getBytes("ISO-8859-1"), 0, temp.length());
		        temp = "KilCli Version: " + KilCli.getVersion() + "\n";
		        os.write(temp.getBytes("ISO-8859-1"), 0, temp.length());
		        temp = "Game Number: " + KilCli.getGameNumber() + "\n";
		        os.write(temp.getBytes("ISO-8859-1"), 0, temp.length());
		        temp = "User: " + KilCli.getUsedCharacter() + "\n";
		        os.write(temp.getBytes("ISO-8859-1"), 0, temp.length());
		        temp = "\n";
		        os.write(temp.getBytes("ISO-8859-1"), 0, temp.length());
		        temp = "Exception:\n\n";
		        os.write(temp.getBytes("ISO-8859-1"), 0, temp.length());
		        temp = e + "\n";
		        os.write(temp.getBytes("ISO-8859-1"), 0, temp.length());
		        trace = e.getStackTrace();
		        for (int i = 0; i < trace.length; i++) {
					temp = trace[i] + "\n";
					os.write(temp.getBytes("ISO-8859-1"), 0, temp.length());
				}
		        temp = "\n.\n";
		        os.write(temp.getBytes("ISO-8859-1"), 0, temp.length());
				temp = "QUIT";
				os.write(temp.getBytes("ISO-8859-1"), 0, temp.length());
				os.flush();

    	        String responseLine;
    	        while ((responseLine = is.readLine()) != null) {
    	            if (responseLine.indexOf("Ok") != -1) {
    	            	break;
    	            }
    	        }

				os.close();
    	        is.close();
    	        smtpSocket.close();
    	    } catch (Exception ee) {}
    	    
    	}
    
	}
	*/
}
