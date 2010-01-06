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
import java.net.UnknownHostException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.IOException;

public class HTTPSocket extends Socket {

	private InputStream in;
	private OutputStream out;


	public HTTPSocket(String proxyHost, int proxyPort, String username, String password, String host, int port) throws UnknownHostException, IOException {
		super(proxyHost, proxyPort);

		in = this.getInputStream();
		out = this.getOutputStream();

		BufferedReader rd = new BufferedReader(new InputStreamReader(in));
		PrintStream wr = new PrintStream(out);

		// send the CONNECT request
		wr.print("CONNECT " + host + ":" + port + " HTTP/1.1\r\n");
		wr.print("Host: " + host + ":" + port + "\r\n");

		if(username != null && password != null) {
			wr.print("Proxy-Authorization: Basic " + toBase64(username + ":" + password) + "\r\n");
		}

		wr.print("\r\n");

		String line = rd.readLine();

		if(line == null) {
			throw new IOException("Proxy closed connection");
		}

		if(!line.startsWith("HTTP/1.0 200") && !line.startsWith("HTTP/1.1 200")) {
			throw new IOException("Cannot CONNECT, Proxy returned " + line);
		}

		// skip the remainings of the HTTP response
		while(line != null && !line.equals("")) {
			line = rd.readLine();
		}
	}

	/**
	 * Convert a string to base 64 encoded form. Used for
	 * proxy authentication.
	 *
	 * @param line The line to be converted.
	 * @return The base 64 encoded <code>String</code>.
	 * @throws NullPointerException If line is null
	 */
	private String toBase64(String line) {
		char[] alphabet = {
				'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
				'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
				'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
				'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
				'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
				'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', '+', '/'
		};

		int inlen = line.length();
		int outlen = 0;
		int index;
		for(index = 0; index < inlen; index += 3)
			outlen += 4;
		if(index != inlen)
			outlen += 4;

		char[] out = new char[outlen];
		byte[] c = line.getBytes();
		int tmp = 0;
		int len = 0;
		int n = 0;

		for(int i = 0; i < c.length; i++)
		{
			tmp = tmp << 8;
			tmp += c[i];
			n++;

			if(n == 3)
			{
				out[len] = alphabet[(tmp >>> 18) & 0x3f];
				out[len + 1] = alphabet[(tmp >>> 12) & 0x3f];
				out[len + 2] = alphabet[(tmp >>> 6) & 0x3f];
				out[len + 3] = alphabet[tmp & 0x3f];
				len += 4;
				tmp = 0;
				n = 0;
			}
		}

		switch(n)
		{
			case 2:
				tmp <<= 8;
				out[len] = alphabet[(tmp >>> 18) & 0x3f];
				out[len + 1] = alphabet[(tmp >>> 12) & 0x3f];
				out[len + 2] = alphabet[(tmp >>> 6) & 0x3f];
				out[len + 3] = '=';
				break;
			case 1:
				tmp <<= 16;
				out[len] = alphabet[(tmp >>> 18) & 0x3f];
				out[len + 1] = alphabet[(tmp >>> 12) & 0x3f];
				out[len + 2] = '=';
				out[len + 3] = '=';
				break;
		}
		return new String(out);
	}


}


