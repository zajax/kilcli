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

import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import terris.kilcli.KilCli;

/**
 * SocksSocket for KilCli is the class used to establish a Socks5 Proxy<br>
 * connection, with or without username authentication<br>
 * Ver: 1.0.0
 */

public class SocksSocket extends Socket {

	private InputStream in;
	private OutputStream out;

	/**
	 * Creates a Socks5 connection
	 * @param socksHost - proxy host
	 * @param socksPort - proxy port
	 * @param username - username
	 * @param password - password
	 * @param host - final host to connect to
	 * @param port - port on final host to connect to
	 */

	public SocksSocket(String socksHost, int socksPort, String username, String password, String host, int port) throws UnknownHostException, IOException {
		super(socksHost, socksPort);

		// Allocate a bytebuffer for negotiation.
		byte[] octets = new byte[1024];
		out = this.getOutputStream();
		in = this.getInputStream();

		// Set up the ID-String for our client.
		octets[0] = 5;    // SOCKS Version
		octets[1] = 3;    // Number of authentification methods
		octets[2] = 0;    // None - no authentification
		octets[3] = 1;    // GSSAPI - authentification via Kerberos (not implemented!)
		octets[4] = 2;    // Username/Passwd

		// Send the id to the SOCKS server
		out.write(octets, 0, 5);
		out.flush();

		in.read(octets, 0, 2);
		int version = octets[0];
		int allowedMethod = octets[1];
		switch (allowedMethod) {
			case 0:
				//KilCli.gameWrite("No Authentication Required");
				break;
			case 1:
				//KilCli.gameWrite("Authentification via GSSAPI");
				throw new java.io.IOException("Socks5 GSSAPI not implemented");
			case 2:
				//KilCli.gameWrite("Authentification via Username/Password");
		      	octets[0] = (byte) 1;

				byte[] namebuff = username.getBytes();
				octets[1] = (byte) namebuff.length;
				System.arraycopy(namebuff,0,octets,2,namebuff.length);

				byte[] passbuff = password.getBytes();
				octets[2+namebuff.length] = (byte) passbuff.length;
      			System.arraycopy(passbuff,0,octets,3+namebuff.length,passbuff.length);

				// Send authentification to the server and cross fingers...
				out.write(octets, 0, 3 + namebuff.length + passbuff.length);

				//KilCli.gameWrite("Sent " + username + " with passwd " + password + "!");

				// Get the answer.
				in.read(octets, 0, 2);
				if (octets[1] != 0) {
					//KilCli.gameWrite("Authentification failed with " + octets[1]);
					throw new java.io.IOException("U/P authentification failed!");
				}
				break;
			case -1:
				//KilCli.gameWrite("Authentification denied!");
				throw new java.io.IOException("Socks5 authentification denied!");
			default:
				//KilCli.gameWrite("Unknown answer " + octets[1]);
				throw new java.io.IOException("Socks5 unknown authentification required!");
		}


		// After the handshake was completed, we'll ask for a connection to
		// the remote host.
		octets[0] = 5;    // Socks version
		octets[1] = 1;    // command: Connect
		octets[2] = 0;    // Reserved 0
		octets[3] = 3;    // Domainname is following:


		byte[] namebuff = host.getBytes();

		octets[4] = (new Integer(namebuff.length)).byteValue();
		for (int i = 0; i < namebuff.length; i++) {
			octets[5 + i] = namebuff[i];
		}

		// since the port may be 16Bit wide and we're working with bytes we
		// need to split the port accordingly.
		int uport = port / 256;
		int lport = port % 256;

		//KilCli.gameWrite("Portid: " + port + "=" + uport + "*256+" + lport);
		octets[5 + namebuff.length + 0] = (new Integer(uport)).byteValue();
		octets[5 + namebuff.length + 1] = (new Integer(lport)).byteValue();

		// request connection...
		out.write(octets, 0, 5 + namebuff.length + 2);
		//KilCli.gameWrite("Connection Request sent");

		// retrieve the answer
		in.read(octets);
		switch (octets[1]) {
			case 0:
				//KilCli.gameWrite("Success!");
				break;
			case 1:
				//KilCli.gameWrite("general failure");
				throw new java.io.IOException("Socks5 server failure");
			case 2:
				//KilCli.gameWrite("connection refused by ruleset");
				throw new java.io.IOException("Socks5 refused by ruleset");
			case 3:
				//KilCli.gameWrite("network unreachable");
				throw new java.io.IOException("Socks5 network unreachable");
			case 4:
				//KilCli.gameWrite("host unreachable");
				throw new java.io.IOException("Socks5 host unreachable");
			case 5:
				//KilCli.gameWrite("connection refused");
				throw new java.io.IOException("Socks5 connection refused");
			case 6:
				//KilCli.gameWrite("TTL expired");
				throw new java.io.IOException("Socks5 TTL expired");
			case 7:
				//KilCli.gameWrite("Command not supported");
				throw new java.io.IOException("Socks5 command not supported");
			case 8:
				//KilCli.gameWrite("Address type not supported");
				throw new java.io.IOException("Socks5 address type not supported");
			default:
				//KilCli.gameWrite("Unknown answer " + octets[1]);
				throw new java.io.IOException("Socks5 unknown error!");
		}
		// hopefully we now get an IP-address and a port which describes the
		// connection which is routed through the SOCKS server to our
		// destination.
		switch (octets[3]) {
			case 1:
				//KilCli.gameWrite("IP V4 address");
				break;
			case 3:
				//KilCli.gameWrite("DOMAINNAME");
				throw new java.io.IOException("Socks5 Domainname encoding not implemented");
			case 4:
				//KilCli.gameWrite("IP V6 address");
				throw new java.io.IOException("Socks5 IP V6 encoding not implemented");
			default:
				//KilCli.gameWrite("Unknown hostname encoding!");
				throw new java.io.IOException("Socks5 server sent unknown hostname!");
		}
	}

}


