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

package terris.kilcli.resource;

import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import java.awt.Toolkit;

/**
 * LimitedDocument for KilCli is the class used to limit the<br>
 * number of characters allowed in the command line<br>
 * Ver: 1.0.0
 */

public class LimitedDocument extends PlainDocument {

	private int maxLength;
	private int modifier = 0;
	private int index = 0;
	private int count = 0;
	private boolean enableModifier = true;

	public LimitedDocument(int maxLength) {
		this.maxLength = maxLength;
	}

	public LimitedDocument(int maxLength, boolean modifier) {
		this.maxLength = maxLength;
		enableModifier = modifier;
	}

	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		if (enableModifier) {
			if (getLength() == 0) {
				modifier = 0;
			} else {
				modifier += count(str, "\\") * 3;
				modifier += count(str, "/") * 3;
				modifier += count(str, "[") * 3;
			}
		}

		if((getLength() + str.length() + modifier) > maxLength) {
        	int remainder = maxLength - getLength() - modifier;
            if (remainder > 0) {
            	super.insertString (offset, str.substring (0, remainder), a);
			}
			Toolkit.getDefaultToolkit().beep();
		} else {
			super.insertString(offset, str, a);
		}
	}

	private int count(String text, String searchFor) {
		index = 0;
		count = 0;
		while ((index = text.indexOf(searchFor, index)) > -1) {
			count++;
			index++;
		}
		return count;
	}
}