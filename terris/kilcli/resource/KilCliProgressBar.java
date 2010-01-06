/* KilCli, an OGC mud client program
 * Copyright (C) 2002 - 2004Jason Baumeister
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class KilCliProgressBar extends JProgressBar{

    private int type = 0;
    private int typeColor = 0;
    public static final int MULTICOLOR = 3;
    public static final int CUSTOM = 4;


    private int otherValue = -1;
    private Color otherForegroundColor = Color.red;
    private Color otherBackgroundColor = Color.blue;
    private Color customFull = Color.green;
    private Color customEmpty = Color.blue;


  public KilCliProgressBar(int type) {
  super();
  this.setForeground(Color.blue);
  this.setType(type);
  this.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
  setFont(new Font("SansSerif", 1, 13));
  }

  public KilCliProgressBar() {
  super();
  this.setForeground(Color.blue);
  this.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
  setFont(new Font("SansSerif", 1, 13));
  }

  public KilCliProgressBar(int min, int max) {
  super(min,max);
  this.setForeground(Color.blue);
  this.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
  setFont(new Font("SansSerif", 1, 13));
  }

  public void paint(Graphics g) {

    if (g==null) return;

    g.setColor(this.getBackground());
    g.fill3DRect(0,0,this.getWidth(),this.getHeight(),true);
	this.drawMulticolor(g);
  }

   public void setOtherValue(int value) {
    otherValue = value;
    this.repaint();
   }


   public int getOtherValue() {
    return otherValue;
   }

   public void setOtherForeground(Color value) {
    otherForegroundColor = value;
   }


   public Color getOtherForeground() {
    return otherForegroundColor;
   }

   public void setOtherBackground(Color value) {
    otherBackgroundColor = value;
   }


   public Color getOtherBackground() {
    return otherBackgroundColor;
   }

   public void setCustomFull(Color value) {
	   customFull = value;
   }

   public void setCustomEmpty(Color value) {
	   customEmpty = value;
   }

   public Color getCustomFull() {
	   return customFull;
   }

   public Color getCustomEmpty() {
	   return customEmpty;
   }

   public void setType(int type) {
    this.type = type;
   }

   public void setTypeColor(int typeColor) {
    this.typeColor = typeColor;
   }

    private void drawMulticolor(Graphics g) {
    	int h = this.getHeight();
    	int w = this.getWidth();

    	int value = this.getValue();
    	int maxV = this.getMaximum();
    	float perc = ((float)value)/((float)maxV);

    	int i=0;
    	for (i=0; i<w; i++) {
      		if ( perc>=((float)i)/((float)w) ) {
        		g.setColor(this.getMColor(i,w));
        		g.drawLine(i,1,i,h-1);
      		} else {
        		break;
      		}
    	}

    	g.setColor(this.getOtherForeground());
    	int tmp = this.getString().length();
    	tmp = (w/2)-(4*tmp);

		Insets b = this.getInsets(); // area for border
		int barRectWidth = this.getWidth() - (b.right + b.left);
		int barRectHeight = this.getHeight() - (b.top + b.bottom);
		int amountFull = getAmountFull(b, barRectWidth, barRectHeight);
		paintString(g, b.left, b.top, barRectWidth, barRectHeight, amountFull, b);
    }

    //GET COLOR
    private Color getMColor(int i, int N) {
    	float v = ((float)i)/((float)N);
    	if (this.typeColor == CUSTOM) {
			int tmp = 0, tmp2 = 0, tmp3 = 0, tmp4 = 0, tmp5 = 0, tmp6 =0;
			tmp = customEmpty.getRed();
			tmp2 = customEmpty.getGreen();
			tmp3 = customEmpty.getBlue();
			tmp4 = customFull.getRed();
			tmp5 = customFull.getGreen();
			tmp6 = customFull.getBlue();
			return new Color((int)((tmp-(tmp -1)*v) + ((tmp4-1)*v+1)) % 255,(int)((tmp2-(tmp2 -1)*v) + ((tmp5-1)*v+1)) % 255,(int)((tmp3-(tmp3 -1)*v) + ((tmp6-1)*v+1)) % 255);
		}

    	return new Color((int)(254*v+1),(int)(254*v+1), (int)(254*v+1));
    }

    protected void paintString(Graphics g, int x, int y, int width, int height, int amountFull, Insets b) {
    	paintString(g, x, y, width, height, x, amountFull, b);
    }

    /**
     * Paints the progress string.
     *
     * @param g Graphics used for drawing.
     * @param x x location of bounding box
     * @param y y location of bounding box
     * @param width width of bounding box
     * @param height height of bounding box
     * @param fillStart start location, in x or y depending on orientation,
     *        of the filled portion of the progress bar.
     * @param amountFull size of the fill region, either width or height
     *        depending upon orientation.
     * @param b Insets of the progress bar.
     */
    private void paintString(Graphics g, int x, int y, int width, int height, int fillStart, int amountFull, Insets b) {
        if (!(g instanceof Graphics2D)) {
            return;
        }

        Graphics2D g2 = (Graphics2D)g;
		String progressString = this.getString();
		g2.setFont(this.getFont());
		Point renderLocation = getStringPlacement(g2, progressString, x, y, width, height);
		Rectangle oldClip = g2.getClipBounds();

		g2.setColor(getOtherBackground());
	    g2.drawString(progressString, renderLocation.x, renderLocation.y);
	    g2.setColor(getOtherForeground());
        g2.clipRect(fillStart, y, amountFull, height);
	    g.drawString(progressString, renderLocation.x, renderLocation.y);
		g2.setClip(oldClip);
    }


    /**
     * Designate the place where the progress string will be painted.
     * Places it at the center of the bar (in both x and y).
     */
    protected Point getStringPlacement(Graphics g, String progressString, int x,int y,int width,int height) {
		FontMetrics fontSizer = getFontMetrics(getFont());
		int stringWidth = fontSizer.stringWidth(progressString);
		return new Point(x + Math.round(width/2 - stringWidth/2), y + ((height + fontSizer.getAscent() - fontSizer.getLeading() - fontSizer.getDescent()) / 2));
    }

    /**
     * This determines the amount of the progress bar that should be filled
     * based on the percent done gathered from the model. This is a common
     * operation so it was abstracted out. It assumes that your progress bar
     * is linear. That is, if you are making a circular progress indicator,
     * you will want to override this method.
     */
    protected int getAmountFull(Insets b, int width, int height) {
		int amountFull = 0;
		BoundedRangeModel model = this.getModel();

		if ( (model.getMaximum() - model.getMinimum()) != 0) {
			amountFull = (int)Math.round(width * this.getPercentComplete());
    	}
    	return amountFull;
	}
}