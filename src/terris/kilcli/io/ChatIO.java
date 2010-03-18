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

package terris.kilcli.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.Socket;
import java.net.NoRouteToHostException;
import java.net.UnknownHostException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.io.File;
import javax.swing.Timer;
import terris.kilcli.*;
import terris.kilcli.io.*;
import terris.kilcli.window.*;
import terris.kilcli.thread.*;
import terris.kilcli.resource.*;

/**
 * ChatIO for KilCli is the class used to send and receive<br>
 * data from the chat server<br>
 * Ver: 1.0.2
 */

public class ChatIO implements Runnable {

	private static int netBufferSize = 2048;
	private String newLine;

	private Socket connSock;
	private BufferedOutputStream outStream;
	private BufferedInputStream inStream;
	private boolean connected;
	private String input = "";
	private Thread receiveThread = null;
	private byte netBuffer[] = null;
    private String tempString1 = "";
    private String carryOver = "";
    private boolean running = false;
    private static boolean enableTriggers = false;

	/**
	 * Creates a ChatIO object set to connect to the
	 * chat server
	 */

	public ChatIO() {
		byte nextLine[] = new byte[2];
		nextLine[0] = (byte)13;
		nextLine[1] = (byte)10;
		newLine = new String(nextLine);
	}

	/**
	 * Prepares a thread to do all of the receiving in
	 */

    public void start() {
        if (receiveThread == null) {
            receiveThread = new Thread(this, "Receive");
            receiveThread.start();
        }
    }


	/**
	 * Checks the thread references and establishes the connection
	 * to the remove server
	 */

    public void run() {
        Thread myThread = Thread.currentThread();
        establishConnection();
    }

	/**
	 * Toggle for if we are to analyze incoming data for triggers
	 *
	 * @param t - the new value of enableTriggers
	 */

	public static void setTrigger(boolean t) {
		enableTriggers = t;
	}

	/**
	 * Method to connect to server and prepare to receive data
	 */

	public void establishConnection() {
		//attempt to connect
		if (connect()) {
			//if we get connected, run program
			running = true;
			Receive();
		} else {
			//else write, could not connect
			KilCli.gameWrite("Could not connect");
		}
		running = false;
		receiveThread = null;
	}

	public boolean isRunning() {
		return connected;
	}

	/**
	 * Connects to the needed remote server
	 *
	 * @return boolean true/false depending on if connection was successful
	 */

	public boolean connect() {
		connSock = null;
		String server = "";
		int port = 0;
		//attempts to establish a connection
		try {
			server = "chat.kilcli.com";
			port = 1374;
			connectToServer(server, port);
		    outStream = new BufferedOutputStream(connSock.getOutputStream());

		} catch (Exception oops) {
			KilCli.gameWrite("Exception while connecting to chat server:" + oops);
			KilCli.gameWrite("Trying to connect via IP address...");
			try {
				connSock.close();
				server = "69.11.208.105";
				connectToServer(server, port);
				outStream = new BufferedOutputStream(connSock.getOutputStream());
			} catch (Exception oops2) {
				KilCli.gameWrite("IP Address failed, cannot connect to chat server.");
				connected = false;
				return connected;
			}
		}

	    try {
			inStream = new BufferedInputStream(connSock.getInputStream());
        	//set connected flag to true
        	connected = true;
	    	//outputs string to authenticate with remote server
        	return connected;

	    } catch (Exception oops) {
	        KilCli.gameWrite("No inStream, could not complete connection");
	        connected = false;
	        inStream = null;
	        return connected;
	    }
	}


	/**
	 * Attempts to connect to the given server, port.
	 * Uses the proxy settings if needed
	 */
	private void connectToServer(String server, int port) throws IOException {
		if (KilCli.getProxy().toLowerCase().equals("true")) {
           	String sockHost = KilCli.getProxyHost();
           	int sockPort = KilCli.getProxyPort();
           	if (KilCli.getProxyType().equalsIgnoreCase("Socks4/4A")) {
				System.getProperties().put("proxySet", "true" );
				System.getProperties().put("socksProxyHost", sockHost);
				System.getProperties().put("socksProxyPort", sockPort + "");
               	connSock = new Socket(server, port);
               	connSock.setTcpNoDelay(true);
			} else if (KilCli.getProxyType().equalsIgnoreCase("Socks5")) {
				connSock = (Socket)new SocksSocket(sockHost, sockPort, KilCli.getProxyUsername(), KilCli.getProxyPassword(), server, port);
				connSock.setTcpNoDelay(true);
			} else if (KilCli.getProxyType().equalsIgnoreCase("HTTP Tunnel")) {
				connSock = (Socket)new HTTPSocket(sockHost, sockPort, KilCli.getProxyUsername(), KilCli.getProxyPassword(), server, port);
				connSock.setTcpNoDelay(true);
			} else {
				KilCli.gameWrite("Unrecognized proxy type, using direct connection");
				connSock = new Socket(server, port);
				connSock.setTcpNoDelay(true);
			}
		} else {
			connSock = new Socket(server, port);
			connSock.setTcpNoDelay(true);
		}
	}

	/**
	 * Receives data from the server and figures out what to do with it
	 */

	public void Receive() {

		//while we are connected....
		try {
			inputCheck(netRead());
			//send the output to the game window
			KilCli.chatsWrite(input);
			input = "";
			String temp = "";
			if (KilCli.getGameNumber() == 0) {
				temp = "-Terris";
			} else if (KilCli.getGameNumber() == 1) {
				temp = "-Cosrin";
			} else if (KilCli.getGameNumber() == 2) {
				temp = "-Wolfenburg";
			} else {
				temp = "-unknown";
			}
			write("/nick " + KilCli.getUsedCharacter() + temp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (connected) {
			//try to get text from the server
			try {
				inputCheck(netRead());
			} catch (SocketException se) {
				//socket says connection reset
				connected = false;
				receiveThread = null;
				System.err.println(se);
				se.printStackTrace();
			} catch (IOException ioe) {
				//netRead says the stream is dead
				connected = false;
				receiveThread = null;
				System.err.println(ioe);
				ioe.printStackTrace();
			} catch (Exception e) {
				// We've run into some other problem, try to keep the connection
				// alive.
				System.err.println(e);
				e.printStackTrace();
        		SMTPClient.sendError("receiveNonIO", e);
        	}

			//send the output to the game window
			//KilCli.chatsWrite(input);
			input = "";
		}
	}


	/**
	 * Adds a blank line to the output
	 */

	public void addBlank() {
		if ((input.length() > 3) && (!input.substring(input.length()-4, input.length()).equals("<br>"))) {
			input += "<br>";
		}
	}

	/**
	 * Disconnects from remote server and cleans up the socket
	 */

	public boolean disconnect(String quit) {
		String exit = "";

		try {
			if (connected) {
				write("/quit");
			}
			connected = false;
			receiveThread = null;
			Thread.sleep(1000);
			exit = netRead();
			exit = exit.trim();

		} catch (Exception e) {
			System.err.println("Error while closing socket.");
			System.err.println(e);
		}
		if (exit.indexOf("Bye bye") > -1) {
			try {
				inStream.close();
				outStream.close();
				connSock.close();
			} catch (Exception e) {}
			connSock = null;
			inStream = null;
			outStream = null;
			receiveThread = null;
			return true;
		} else {
			System.err.println("error while disconnecting");
			System.err.println("exit=" + exit);
		}
		return true;

	}

	/**
	 * Sends a stream of data to the remote server
	 *
	 * @param output - the string to be sent
	 */

	public synchronized void write(String output) {
		if (connected) {
			if (output.length() < 1) {
				return;
			}


			//attempt to send the string
			if (output.length() > 123) {
				output = output.substring(0, 122);
			}

			output += "\r\n";
			try {
				outStream.write(output.getBytes("ISO-8859-1"), 0, output.length());
				outStream.flush();
			//otherwise, drop the connection and say we have an error
			} catch (Exception e) {
				connected = false;
				System.err.println("SendText exception: " + e);
				SMTPClient.sendError("sending", e);
        	}
		}
	}


	/**
	 * Changes the read buffer size
	 *
	 * @param s - the new size of the read buffer, max is 8704, min is 1024
	 */

	public void setNetBufferSize(int s) {
		int oldSize = netBufferSize;
		//check to see if the size changed
		if (oldSize != s) {
			//make sure the new size is valid
			if (s < 1024) {
				netBufferSize = 1024;
			} else if (s > 8704) {
				netBufferSize = 8704;
			} else {
				netBufferSize = s;
			}

			//copy old buffer to the new buffer
			if (netBuffer != null) {
				oldSize = java.lang.Math.min(oldSize, netBufferSize);
				byte tmpBuffer[] = new byte[netBufferSize];
				System.arraycopy(netBuffer, 0, tmpBuffer, 0, oldSize);
				netBuffer = tmpBuffer;
			} else {
				netBuffer = new byte[netBufferSize];
			}
		}
	}

	/**
	 * Reads available data from socket in chunks
	 * whose size varies with the size of the current buffer
	 *
	 * @return String - the string representation data that was received
	 */

	private String netRead() throws Exception {
    	int len;

    	//Check to make sure the buffer has been allocated
    	if (netBuffer == null) {
    		netBuffer = new byte[netBufferSize];
		}

    	//Read any waiting input for the connection...
        len = inStream.read(netBuffer, 0, netBufferSize);

		//see if we need to throw an exception because we read -1 bytes
		//this basically means that the inStream doesn't exist anymore
		if (len == -1) {
			throw new IOException();
		}

     	// Convert the buffer into a String and return it
		return new String(netBuffer, 0, len, "ISO-8859-1");
	}

	/**
	 * Checks the received data to see what needs to be done with it
	 *
	 * @param tempInput - the string that was received from the server
	 */

	public void inputCheck(String tempInput) throws Exception {
    	int stringSearchIndex = 0;
    	int lastSearchIndex = 0;
    	//Go through the block of text we got and filter it
     	//appropriately.
     	if (tempInput != null) {
			if (carryOver.length() > 0) {
				tempInput = carryOver + tempInput;
				carryOver = "";
			}
			//run through the text looking for any newlines, so we can analyze each of them
    		stringSearchIndex = tempInput.indexOf(newLine);
    		while (stringSearchIndex != -1) {
				if (stringSearchIndex > lastSearchIndex) {
					//otherwise, let's analyze the line
					inputLineCheck(tempInput.substring(lastSearchIndex, stringSearchIndex));
				} else if (input.length() > 0) {
					addBlank();
				}
				//then remove the newline and continue the search
				//tempInput = tempInput.substring(stringSearchIndex + 2, tempInput.length());
				lastSearchIndex = stringSearchIndex+2;
				stringSearchIndex = tempInput.indexOf(newLine, lastSearchIndex);
    		}
    		//analyze what's left.
			carryOver = tempInput.substring(lastSearchIndex, tempInput.length());
		}
	}

	/**
	 * Method to check a line input from the game.<br>
	 * to see what should be done with it
	 */

	public void inputLineCheck(String tempInput) {
		int stringSearchIndex = 0;
		tempString1 = "";

		//set the initial search index
		stringSearchIndex = tempInput.indexOf("<");
		//check to see if we need to convert any < (to avoid the html parser)
		while (stringSearchIndex != -1) {
			tempString1 = tempInput.substring(0, stringSearchIndex);
			tempInput = tempString1 + "&lt;" + tempInput.substring(stringSearchIndex + 1, tempInput.length());
			stringSearchIndex = tempInput.indexOf("<", stringSearchIndex);
		}
		//reset search index
		stringSearchIndex = 0;

		//get the first character of the line
		if (tempInput.length() > 0) {

			//check if line has characters that need to be translated
			tempInput = KilCli.characterTranslate(tempInput);
			if (enableTriggers) {
				new TriggerThread(tempInput).start();
			}
			KilCli.log(tempInput);

			tempInput = KilCli.pickWindow(tempInput);
			if (tempInput.charAt(0) != '[') {
				KilCli.gameWrite(tempInput);
			}
		}
	}

}