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

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Graphics;


/**
 * HardenIndicator for KilCli is the class used to represent an<br>
 * item that is hardened in the harden armour panel<br>
 * Ver: 1.0.1
 */

public class HardenIndicator extends JComponent {

	private Color myColor = Color.RED;
	private Color immortals = Color.GREEN;
	private Color divinely = Color.BLUE;
	private Color strongly = new Color(0,255,255);
	private Color magiced = new Color(255,0,255);
	private Color slightly = Color.YELLOW;
	private Color disabled = Color.RED;
	private int level = -1;
	private String name = "empty";

    public HardenIndicator() {
		super();
		setToolTipText(name);
	}

    protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    // paint my contents next....
	    g.setColor(myColor);
	    g.fillRect(2,2,getWidth()-4,getHeight()-4);
    }

	public void setLevel(int l) {
		if (level != l) {
			level = l;
			if (level == -1) {
				myColor = disabled;
			} else if (level == 0) {
				myColor = slightly;
			} else if (level == 1) {
				myColor = magiced;
			} else if (level == 2) {
				myColor = strongly;
			} else if (level == 3) {
				myColor = divinely;
			} else if (level == 4) {
				myColor = immortals;
			}
		}
	}

	public int getLevel() {
		return level;
	}

	public void setName(String s) {
		name = s;
		setToolTipText(name);
	}

	public boolean isOpaque() {
		return false;
	}
}