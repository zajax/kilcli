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

package terris.kilcli.window;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import javax.swing.BorderFactory;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import javax.swing.plaf.basic.*;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import terris.kilcli.KilCli;

/**
 * EffectsPanel for KilCli is the class used to create, update, and<br>
 * display the effects panel<br>
 * Ver: 1.0.0
 */

public class EffectsPanel extends KilCliInternalFrame {
	private JScrollPane jScroll;
	private JEditorPane text;
	private static Object[] colors;
	private String[] effects = new String[31];
	private String effectString = "";
	private String effectString2 = "";

	public static void setColors(Object[] c) {
		colors = c;
	}

	public void updateTheme() {
		this.setBackground((java.awt.Color)(colors[0]));
		text.setBackground((java.awt.Color)(colors[0]));
		text.setText("<body bgcolor=#" + (String)(colors[1])+ "<H1 align=center color=#" + (String)(colors[2]) + "><b><u>Effects:<b></u></H1><font color=#" + (String)(colors[3]) + ">" + effectString2);
		popup = null;
		createMenu();
		repaint();
	}

	/**
	 * EffectsPanel constructor, with arguments for X,Y location and<br>
	 * height,width, creates a panel in the specified location<br>
	 * with the specified dimensions
	 * @param locX The starting X location
	 * @param locY The starting Y location
	 * @param width The width of the chats window
	 * @param height The height of the chats window
	 * @param OldStyle Flag to use old no title bar at all style for the commandline<br>
	 * 			Only works for Win/Lin/Sol
	 */

    public EffectsPanel(int locX, int locY, int width, int height, boolean oldStyle) {
        super("Effects Panel",
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable

		setBackground((java.awt.Color)(colors[0]));

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		text = new JEditorPane();
		text.setContentType("text/html");
		text.setEditable(false);
		text.setMargin(new Insets(0, 0, 0, 0));
		text.setBackground((java.awt.Color)(colors[0]));
		text.setText("<body bgcolor=#" + (String)(colors[1])+ "<H1 align=center color=#" + (String)(colors[2]) + "><b><u>Effects:<b></u></H1>");
		jScroll = new JScrollPane();
		jScroll.setViewportView(text);

        //add scroll pane to window and set visible
        getContentPane().add(jScroll);
        jScroll.setVisible(true);

        refocus(text);

		//set window size
		setSize(width, height);

        //Set the window's location.
		setLocation(locX, locY);

		//check oldStyle flag to remove title bar completely ***Only for Win/Lin/Sol
		if (oldStyle) {
			//remove the title bar
			ComponentUI frameUI = getUI();
			if (frameUI instanceof BasicInternalFrameUI) {
				((BasicInternalFrameUI) frameUI).setNorthPane(null);
			}
		}

		//creates the right click menu
		createMenu();
    	dwa = new DragWindowAdapter(this);
	    text.addMouseListener(dwa);
	    text.addMouseMotionListener(dwa);
		created = true;
	}

	/**
	 * Adds an effect to the list
	 * @param e - the effect to be added
	 */

	public void addEffect(char e) {
		if (e <= 70) {
			if (e == 65) {
				effects[16] = "Accurate, ";
			} else if (e == 66) {
				effects[27] = "Gilled, ";
			} else if (e == 67) {
				effects[10] = "Clumsy, ";
			} else if (e == 69) {
				effects[2] = "Entangled, ";
				KilCli.checkStun();
			} else if (e == 70) {
				effects[19] = "Flying, ";
			}
		} else if (e <= 78) {
			if (e == 72) {
				effects[17] = "Hasted, ";
			} else if (e == 73) {
				effects[20] = "Invisible, ";
			} else if (e == 75) {
				effects[28] = "Water Walking, ";
			} else if (e == 76) {
				effects[29] = "Levitating, ";
			} else if (e == 78) {
				effects[23] = "Entangle Resistant, ";
			}
		} else if (e <= 84) {
			if (e == 79) {
				effects[4] = "Slowed, ";
				KilCli.checkSlow();
			} else if (e == 80) {
				effects[15] = "Protected, ";
			} else if (e == 82) {
				effects[14] = "Sanctuaried, ";
			} else if (e == 83) {
				effects[13] = "See Invisible, ";
			} else if (e == 84) {
				effects[1] = "Stunned, ";
				KilCli.checkStun();
			}
		} else if (e <= 99) {
			if (e == 86) {
				effects[12] = "Vulnerable, ";
			} else if (e == 87) {
				effects[11] = "Weakened, ";
			} else if (e == 88) {
				effects[5] = "Pacified, ";
				KilCli.checkSlow();
			} else if (e == 89) {
				effects[8] = "Deaf, ";
			} else if (e == 99) {
				effects[25] = "Cold Resistant, ";
			}
		} else if (e <= 112) {
			if (e == 101) {
				effects[26] = "Elec Resistant, ";
			} else if (e == 102) {
				effects[24] = "Fire Resistant, ";
			} else if (e == 104) {
				effects[0] = "Held, ";
				KilCli.checkStun();
			} else if (e == 105) {
				effects[21] = "Hidden, ";
			} else if (e == 112) {
				effects[6] = "Sleeping, ";
				KilCli.checkStun();
			}
		} else {
			if (e == 114) {
				effects[22] = "Poison Resistant, ";
			} else if (e == 115) {
				effects[18] = "Strong, ";
			} else if (e == 116) {
				effects[30] = "Telepathic";
			} else if (e == 119) {
				effects[3] = "Webbed, ";
				KilCli.checkStun();
			} else if (e == 120) {
				effects[7] = "Confused, ";
				KilCli.checkSlow();
			} else if (e == 121) {
				effects[9] = "Mute, ";
			}
		}

		effectString = "<body bgcolor=#" + (String)(colors[1])+ "<H1 align=center color=#" + (String)(colors[2]) + "><b><u>Effects:<b></u></H1><font color=#" + (String)(colors[3]) + ">";
		effectString2 = "";
		for (int i = 0; i < effects.length; i++) {
			if (effects[i] != null) {
				effectString2 = effectString2 + effects[i];
			}
		}
		text.setText(effectString + effectString2);
	}

	/**
	 * Removes an effect from the list
	 * @param e - the effect to be removed
	 */

	public void removeEffect(char e) {
		if (e <= 70) {
			if (e == 65) {
				effects[16] = null;
			} else if (e == 66) {
				effects[27] = null;
			} else if (e == 67) {
				effects[10] = null;
			} else if (e == 69) {
				effects[2] = null;
			} else if (e == 70) {
				effects[19] = null;
			}
		} else if (e <= 78) {
			if (e == 72) {
				effects[17] = null;
			} else if (e == 73) {
				effects[20] = null;
			} else if (e == 75) {
				effects[28] = null;
			} else if (e == 76) {
				effects[29] = null;
			} else if (e == 78) {
				effects[23] = null;
			}
		} else if (e <= 84) {
			if (e == 79) {
				effects[4] = null;
			} else if (e == 80) {
				effects[15] = null;
			} else if (e == 82) {
				effects[14] = null;
			} else if (e == 83) {
				effects[13] = null;
			} else if (e == 84) {
				effects[1] = null;
			}
		} else if (e <= 99) {
			if (e == 86) {
				effects[12] = null;
			} else if (e == 87) {
				effects[11] = null;
			} else if (e == 88) {
				effects[5] = null;
			} else if (e == 89) {
				effects[8] = null;
			} else if (e == 90) {
				effects = new String[31];
			} else if (e == 99) {
				effects[25] = null;
			}
		} else if (e <= 112) {
			if (e == 101) {
				effects[26] = null;
			} else if (e == 102) {
				effects[24] = null;
			} else if (e == 104) {
				effects[0] = null;
			} else if (e == 105) {
				effects[21] = null;
			} else if (e == 112) {
				effects[6] = null;
			}
		} else {
			if (e == 114) {
				effects[22] = null;
			} else if (e == 115) {
				effects[18] = null;
			} else if (e == 116) {
				effects[30] = null;
			} else if (e == 119) {
				effects[3] = null;
			} else if (e == 120) {
				effects[7] = null;
			} else if (e == 121) {
				effects[9] = null;
			}
		}

		effectString = "<body bgcolor=#" + (String)(colors[1])+ "<H1 align=center color=#" + (String)(colors[2]) + "><b><u>Effects:<b></u></H1><font color=#" + (String)(colors[3]) + ">";
		effectString2 = "";
		for (int i = 0; i < effects.length; i++) {
			if (effects[i] != null) {
				effectString2 = effectString2 + effects[i];
			}
		}
		text.setText(effectString + effectString2);
	}

	public int getEffectStatus(int effect) {
		if (effects[effect] != null) {
			return 1;
		}
		return 0;
	}

}
