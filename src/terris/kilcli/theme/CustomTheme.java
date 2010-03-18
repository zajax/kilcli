/**
 * @(#)CustomTheme.java 1.0 02/06/29
 *
 *
 */

package terris.kilcli.theme;

import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import com.jgoodies.plaf.plastic.*;

public class CustomTheme extends PlasticTheme
{
	private String szThemeName = "Custom Theme";

    private ColorUIResource primary1;
    private ColorUIResource primary2;
    private ColorUIResource primary3;

    private ColorUIResource secondary1;
    private ColorUIResource secondary2;
    private ColorUIResource secondary3;

    private ColorUIResource black;
    private ColorUIResource white;

    private ColorUIResource controlDisabled;
    private ColorUIResource controlHighlight = new ColorUIResource(154, 154, 154);
    private ColorUIResource controlInfo;
    private ColorUIResource inactiveSystemTextColor;
    private ColorUIResource menuDisabledForeground;
    private ColorUIResource menuItemSelectedBackground;
    private ColorUIResource menuItemSelectedForeground;
    private ColorUIResource menuSelectedBackground;
    private ColorUIResource menuSelectedForeground;
    private ColorUIResource primaryControlHighlight = new ColorUIResource(154, 154, 154);
    private ColorUIResource separatorBackground;
    private ColorUIResource titleTextColor;
    private ColorUIResource toggleButtonCheckColor;
    private ColorUIResource focusColor;
    private ColorUIResource simpleInternalFrameForeground;
    private ColorUIResource simpleInternalFrameBackground;
    private ColorUIResource menuForeground;
    private ColorUIResource menuBackground;
    private ColorUIResource systemTextColor;

    private FontUIResource windowTitleFont;
    private FontUIResource controlFont;
    private FontUIResource titleFont;
    private FontUIResource systemFont;
    private FontUIResource userFont;
    private FontUIResource smallFont;

    private Boolean internalFrameBumpsEnabled;

//******************************************************************************************
// Function Name : CustomTheme(InputStream is) => the default constructor
// Parameter : InputStream is => since the custom theme will be loaded from a properties file,
//			   the constructor will take an i/p stream as a parameter.
//
// Initially sets colors and fonts to their default values, and then reads these values from
// the theme file's input stream
//
//******************************************************************************************

    public CustomTheme(InputStream is)
    {
    	defaultColors();
    	defaultFonts();
    	loadProperties(is);
    }

//******************************************************************************************
// Function Name : defaultFonts()
// Parameter : None
// Returns : None
//
// sets fonts to that of Super's(DefaultMetalTheme) default
//
//******************************************************************************************

    private void defaultFonts()
    {
    	windowTitleFont = super.getWindowTitleFont();
    	controlFont		= super.getControlTextFont();
    	smallFont		= super.getSubTextFont();
    	userFont		= super.getUserTextFont();
    	systemFont		= super.getSystemTextFont();
		titleFont		= super.getTitleTextFont();
    }

//******************************************************************************************
// Function Name : defaultColors()
// Parameter : None
// Returns : None
//
// sets colors to that of Super's(DefaultMetalTheme) default
//
//******************************************************************************************

    private void defaultColors()
    {
    	primary1 		= super.getPrimary1();
        primary2 		= super.getPrimary2();
        primary3 		= super.getPrimary3();

        secondary1 		= super.getSecondary1();
        secondary2 		= super.getSecondary2();
        secondary3 		= super.getSecondary3();

    	controlDisabled	= super.getControlDisabled();

    	controlInfo		= super.getControlInfo();
    	inactiveSystemTextColor	= super.getInactiveSystemTextColor();
    	menuDisabledForeground	= super.getMenuDisabledForeground();
    	menuItemSelectedBackground = super.getMenuItemSelectedBackground();
    	menuItemSelectedForeground = super.getMenuItemSelectedForeground();
    	menuSelectedBackground	= super.getMenuSelectedBackground();
    	menuSelectedForeground	= super.getMenuSelectedForeground();

    	separatorBackground	= super.getSeparatorBackground();
    	titleTextColor	= super.getTitleTextColor();
    	toggleButtonCheckColor	= super.getToggleButtonCheckColor();
    	focusColor		= super.getFocusColor();
    	simpleInternalFrameForeground = super.getSimpleInternalFrameForeground();
    	simpleInternalFrameBackground = super.getSimpleInternalFrameBackground();
    	menuForeground	= super.getMenuForeground();
    	menuBackground	= super.getMenuBackground();
    	systemTextColor	= super.getSystemTextColor();
		internalFrameBumpsEnabled = super.getInternalFrameBumpsEnabled();

		black = super.getBlack();
		white = super.getWhite();
    }

//******************************************************************************************
// Function Name : defaultColors()
// Parameter : None
// Returns : None
//
// Read colors and fonts from the theme file's(which is a property file) input stream and set
// values of primary colors, secondary colors and configurable fonts accordingly. If any value
// is missing default value of the DefaultMetalTheme is used
//
//******************************************************************************************

    private void loadProperties(InputStream is)
    {
    	Properties prop = new Properties();

    	try{
    			prop.load(is);
    	}catch(IOException e)
    	{
    			System.out.println(e);
    	}

    		// get theme name
    	String szTemp = prop.getProperty("name");
    	if(null != szTemp)
    	{
    		szThemeName = szTemp;
    	}

    		//get primary colors
    	szTemp = prop.getProperty("primary1");
    	if(null != szTemp)
    	{
    		primary1 = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("primary2");
    	if(null != szTemp)
    	{
    		primary2 = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("primary3");
    	if(null != szTemp)
    	{
    		primary3 = parseColor(szTemp);
    	}
    		//get secondary colors
    	szTemp = prop.getProperty("secondary1");
    	if(null != szTemp)
    	{
    		secondary1 = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("secondary2");
    	if(null != szTemp)
    	{
    		secondary2 = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("secondary3");
    	if(null != szTemp)
    	{
    		secondary3 = parseColor(szTemp);
    	}
    		//get black
    	szTemp = prop.getProperty("black");
    	if(null != szTemp)
    	{
    		black = parseColor(szTemp);
    	}

    		//get white
    	szTemp = prop.getProperty("white");
    	if(null != szTemp)
    	{
    		white = parseColor(szTemp);
    	}

    	szTemp = prop.getProperty("controldisabled");
    	if(null != szTemp)
    	{
    		controlDisabled = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("controlhighlight");
    	if(null != szTemp)
    	{
    		controlHighlight = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("controlinfo");
    	if(null != szTemp)
    	{
    		controlInfo = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("inactivesystemtextcolor");
    	if(null != szTemp)
    	{
    		inactiveSystemTextColor = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("menudisabledforeground");
    	if(null != szTemp)
    	{
    		menuDisabledForeground = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("menuitemselectedbackground");
    	if(null != szTemp)
    	{
    		menuItemSelectedBackground = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("menuitemselectedforeground");
    	if(null != szTemp)
    	{
    		menuItemSelectedForeground = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("menuselectedbackground");
    	if(null != szTemp)
    	{
    		menuSelectedBackground = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("menuselectedforeground");
    	if(null != szTemp)
    	{
    		menuSelectedForeground = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("primarycontrolhighlight");
    	if(null != szTemp)
    	{
    		primaryControlHighlight = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("separatorbackground");
    	if(null != szTemp)
    	{
    		separatorBackground = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("titletextcolor");
    	if(null != szTemp)
    	{
    		titleTextColor = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("togglebuttoncheckcolor");
    	if(null != szTemp)
    	{
    		toggleButtonCheckColor = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("focuscolor");
    	if(null != szTemp)
    	{
    		focusColor = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("simpleinternalframeforeground");
    	if(null != szTemp)
    	{
    		simpleInternalFrameForeground = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("simpleinternalframebackground");
    	if(null != szTemp)
    	{
    		simpleInternalFrameBackground = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("menuforeground");
    	if(null != szTemp)
    	{
    		menuForeground = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("systemtextcolor");
    	if(null != szTemp)
    	{
    		systemTextColor = parseColor(szTemp);
    	}
    	szTemp = prop.getProperty("menubackground");
    	if(null != szTemp)
    	{
    		menuBackground = parseColor(szTemp);
    	}

    		//get window title font
    	szTemp = prop.getProperty("internalframebumpsenabled");
    	if(null != szTemp)
    	{
    		internalFrameBumpsEnabled = parseBoolean(szTemp);
    	}

    		//get window title font
    	szTemp = prop.getProperty("windowtitlefont");
    	if(null != szTemp)
    	{
    		windowTitleFont = parseFont(szTemp);
    	}

    		//get control font, for menu, and other controls (buttons, list etc)
    	szTemp = prop.getProperty("controlfont");
    	if(null != szTemp)
    	{
    		controlFont = parseFont(szTemp);
    	}

    		//get control font, for menu, and other controls (buttons, list etc)
    	szTemp = prop.getProperty("smallfont");
    	if(null != szTemp)
    	{
    		smallFont = parseFont(szTemp);
    	}

    		//get control font, for menu, and other controls (buttons, list etc)
    	szTemp = prop.getProperty("userfont");
    	if(null != szTemp)
    	{
    		userFont = parseFont(szTemp);
    	}

    		//get control font, for menu, and other controls (buttons, list etc)
    	szTemp = prop.getProperty("systemfont");
    	if(null != szTemp)
    	{
    		systemFont = parseFont(szTemp);
    	}

    		//get control font, for menu, and other controls (buttons, list etc)
    	szTemp = prop.getProperty("titlefont");
    	if(null != szTemp)
    	{
    		titleFont = parseFont(szTemp);
    	}
    }

//******************************************************************************************
// Function Name : parseColor(String color)
// Parameter : String color => the color from the theme file
// Returns : ColorUIResource , parsed color
//
// Parses the color String, which is of the form => RED, GREEN, BLUE in decimals and returns
// a ColorUIResource value constructed from this
//
//******************************************************************************************

    private ColorUIResource parseColor(String color)
    {
    	int r,g,b;
    	r = g = b = 0;
    	try{
    			StringTokenizer stok = new StringTokenizer(color, ",");
    			r = Integer.parseInt(stok.nextToken());
    			g = Integer.parseInt(stok.nextToken());
    			b = Integer.parseInt(stok.nextToken());
    	}catch(Exception e)
    	{
    		System.out.println(e);
	    	System.out.println("Couldn't parse color :" + color);
    	}

    	return new ColorUIResource(r, g, b);
    }

//******************************************************************************************
// Function Name : parseFont(String font)
// Parameter : String font => the font from the theme file
// Returns : FontUIResource , parsed font
//
// Parses the color String, which is of the form => FONT_NAME, FONT_STYLE, FONT_SIZE and
// returns a FontUIResource value constructed from this
//
//******************************************************************************************

    private FontUIResource parseFont(String font)
    {
    	String szName = "Dialog";
    	int iSize=12, iStyle=Font.PLAIN;

    	try{
    			StringTokenizer stok = new StringTokenizer(font, ",");
    			szName 	= stok.nextToken();
    			String tmp 	= stok.nextToken();
    			iSize 	= Integer.parseInt(stok.nextToken());

    				//get font style
    			if(tmp.equalsIgnoreCase("Font.BOLD"))
    			{
    				iStyle = Font.BOLD;
    			}
    			else if(tmp.equalsIgnoreCase("Font.ITALIC"))
    			{
    				iStyle = Font.ITALIC;
    			}
    			else if(tmp.equalsIgnoreCase("Font.BOLD|Font.ITALIC") ||
    					tmp.equalsIgnoreCase("Font.ITALIC|Font.BOLD"))
    			{
    				iStyle = Font.BOLD|Font.ITALIC;
    			}

    	}catch(Exception e)
    	{
    		System.out.println(e);
	    	System.out.println("Couldn't parse font :" + font);
    	}

    	return new FontUIResource(szName,iStyle,iSize);

    }

    private Boolean parseBoolean(String text) {
		return Boolean.valueOf(text);
	}

		// the functions overridden from the base class => DefaultMetalTheme


    public String getName() { return szThemeName; }

    protected ColorUIResource getPrimary1() { return primary1; }
    protected ColorUIResource getPrimary2() { return primary2; }
    protected ColorUIResource getPrimary3() { return primary3; }

    protected ColorUIResource getSecondary1() { return secondary1; }
    protected ColorUIResource getSecondary2() { return secondary2; }
    protected ColorUIResource getSecondary3() { return secondary3; }

    protected ColorUIResource getBlack() { return black; }
    protected ColorUIResource getWhite() { return white; }

    public ColorUIResource getSystemTextColor() { return systemTextColor; }
    public ColorUIResource getTitleTextColor() { return titleTextColor; }
    public ColorUIResource getMenuForeground() { return menuForeground; }
    public ColorUIResource getMenuBackground() { return menuBackground; }
    public ColorUIResource getMenuItemBackground() { return menuBackground; }
    public ColorUIResource getMenuItemSelectedBackground() { return menuItemSelectedBackground; }
    public ColorUIResource getMenuItemSelectedForeground() { return menuItemSelectedForeground; }
    public ColorUIResource getSimpleInternalFrameForeground() { return simpleInternalFrameForeground; }
    public ColorUIResource getSimpleInternalFrameBackground() { return simpleInternalFrameBackground; }
    public ColorUIResource getToggleButtonCheckColor() { return toggleButtonCheckColor; }
    public ColorUIResource getControlDisabled() { return controlDisabled; }
    public ColorUIResource getControlHighlight() { return controlHighlight; }
    public ColorUIResource getControlInfo() { return controlInfo; }
	public ColorUIResource getInactiveSystemTextColor() { return inactiveSystemTextColor; }
	public ColorUIResource getMenuDisabledForeground() { return menuDisabledForeground; }
	public ColorUIResource getMenuSelectedBackground() { return menuSelectedBackground; }
	public ColorUIResource getMenuSelectedForeground() { return menuSelectedForeground; }
	public ColorUIResource getPrimaryControlHighlight() { return primaryControlHighlight; }
	public ColorUIResource getSeparatorBackground() { return separatorBackground; }
	public ColorUIResource getFocusColor() { return focusColor; }
	public Boolean getInternalFrameBumpsEnabled() { return internalFrameBumpsEnabled; }

    public FontUIResource getWindowTitleFont() { return windowTitleFont;}
    public FontUIResource getMenuTextFont() { return controlFont;}
    public FontUIResource getControlTextFont() { return controlFont;}
	public FontUIResource getSubTextFont() { return smallFont; }
	public FontUIResource getTitleTextFont() { return titleFont; }
	public FontUIResource getSystemTextFont() { return systemFont; }
	public FontUIResource getUserTextFont() { return userFont; }

}