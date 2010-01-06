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

import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.JOptionPane;
import java.awt.Toolkit;

/**
 * HexDocument for KilCli is the class used to limit the<br>
 * number of characters allowed in a textfield and limit<br>
 * the valid characters to be hexadecimal digits<br>
 * Ver: 1.0.0
 */

public class HexDocument extends PlainDocument {

	private int maxLength;

	public HexDocument(int maxLength) {
		this.maxLength = maxLength;
	}

	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		boolean ok = true;
		if (getLength() + str.length() > maxLength) {
        	int remainder = maxLength - getLength();
            if (remainder > 0) {
            	str = str.substring(0, remainder);
				for (int i = 0; i < str.length(); i++) {
					if (str.charAt(i) > 102) {
						ok = false;
					}
					if ((str.charAt(i) < 96) && (str.charAt(i) > 70)) {
						ok = false;
					}
					if ((str.charAt(i) < 64) && (str.charAt(i) > 57)) {
						ok = false;
					}
					if (str.charAt(i) < 48) {
						ok = false;
					}
				}
				if (ok) {
					super.insertString(offset, str, a);
				} else {
					new JOptionPane().showMessageDialog(null, "Error! Bad Hex value, valid digits are 0-9 and (a-f or A-F)");
				}
			}
			Toolkit.getDefaultToolkit().beep();
		} else {
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) > 102) {
					ok = false;
				}
				if ((str.charAt(i) < 96) && (str.charAt(i) > 70)) {
					ok = false;
				}
				if ((str.charAt(i) < 64) && (str.charAt(i) > 57)) {
					ok = false;
				}
				if (str.charAt(i) < 48) {
					ok = false;
				}
			}
			if (ok) {
				super.insertString(offset, str, a);
			} else {
				new JOptionPane().showMessageDialog(null, "Error! Bad Hex value, valid digits are 0-9 and (a-f or A-F)");
			}
		}
	}

}