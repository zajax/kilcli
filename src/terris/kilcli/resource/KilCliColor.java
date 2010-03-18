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


/**
 * KilCliColor for KilCli is the class stored in the hashtable<br>
 * to translate OGC ^* codes into html colors<br>
 * Ver: 1.0.0
 */

public class KilCliColor {

	private int colorNumber;
	private boolean isNormal;
	private String startCommand;

	public KilCliColor(int num) {
		this(num, false, null);
	}

	public KilCliColor(int num, boolean normal, String command1) {
		colorNumber = num;
		isNormal = normal;
		startCommand = command1;
	}

	public int getNumber() {
		return colorNumber;
	}

	public boolean isNormal() {
		return isNormal;
	}

	public String getStartCommand() {
		return startCommand;
	}

	public boolean isBold() {
		return (colorNumber == -2);
	}

	public boolean isAntiBold() {
		return (colorNumber == -3);
	}

	public boolean isItalic() {
		return (colorNumber == -4);
	}

	public boolean isAntiItalic() {
		return (colorNumber == -5);
	}

	public boolean isFontIncrease() {
		return (colorNumber == -6);
	}

	public boolean isFontDecrease() {
		return (colorNumber == -7);
	}

	public boolean isFontChange() {
		return (colorNumber < -5);
	}

}
