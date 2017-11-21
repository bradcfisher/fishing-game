
package Fishing.drawable.text;

import Fishing.Resources;
import Fishing.drawable.SpriteSheet;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

/**
 * Multi-colored font used for rendering the game title, as well as several
 * dialog titles.
 * 
 * @author Brad
 */
public class TitleFont
    extends SpriteFont
{

    /**
     * Loads a SpriteSheet from the title font resource for the specified color.
     * 
     * @param   color   The color of the title font resource to use.  Valid
     *                  values are "green", "red" or "yellow".
     * 
     * @return  The SpriteSheet of the title font resource for the specified
     *          color.
     */
    private static SpriteSheet loadSheet(String color) {
        try {
            return new SpriteSheet( Resources.getStream("assets/fonts/title_"+ color +".png") );
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to load title font sprite resource", ex);
        } // try/catch
    } // loadSheet(String color)

    /**
     * Constructs a new TitleFont.
     *
     * @param   color   The color of the title font resource to use.  Valid
     *                  values are "green", "red" or "yellow".
     */
    public TitleFont( String color ) {
        super(loadSheet(color));

        // Add each letter

        // Unknown (use same as ".")
        addFrame(180, 170, 13, 47, "Unknown");

        // Space character...
        addFrame(350, 102, 35, 42, " ");

        // 1st Row
        addFrame(  4, 6, 43, 42, "A");
        addFrame( 51, 6, 42, 42, "B");
        addFrame( 97, 6, 39, 42, "C");
        addFrame(141, 6, 41, 42, "D");
        addFrame(186, 6, 42, 42, "E");
        addFrame(231, 6, 38, 42, "F");
        addFrame(273, 6, 39, 42, "G");
        addFrame(313, 6, 38, 42, "H");
        addFrame(353, 6, 27, 42, "I");

        // 2nd row
        addFrame(  5, 57, 32, 41, "J");
        addFrame( 40, 57, 40, 41, "K");
        addFrame( 81, 57, 38, 41, "L");
        addFrame(123, 57, 37, 41, "M");
        addFrame(164, 57, 37, 41, "N");
        addFrame(203, 57, 38, 41, "O");
        addFrame(244, 57, 38, 41, "P");
        addFrame(283, 57, 43, 41, "Q");
        addFrame(328, 57, 42, 41, "R");

        // 3rd row
        addFrame(  5, 102, 35, 42, "S");
        addFrame( 42, 102, 38, 42, "T");
        addFrame( 84, 102, 38, 42, "U");
        addFrame(126, 102, 42, 42, "V");
        addFrame(171, 102, 41, 42, "W");
        addFrame(215, 102, 42, 42, "X");
        addFrame(260, 102, 44, 42, "Y");
        addFrame(305, 102, 42, 42, "Z");

        // 4th row
        addFrame(  4, 148, 19, 42, "1");
        addFrame( 26, 148, 38, 42, "2");
        addFrame( 67, 148, 33, 42, "3");
        addFrame(103, 148, 34, 42, "4");
        addFrame(142, 148, 32, 42, "5");
        addFrame(178, 148, 37, 42, "6");
        addFrame(216, 148, 34, 42, "7");
        addFrame(253, 148, 37, 42, "8");
        addFrame(293, 148, 37, 42, "9");
        addFrame(332, 148, 33, 42, "0");

        // 5th row
        addFrame(  4, 193, 16, 43, ".");
        addFrame( 22, 193, 20, 43, ",");
        addFrame( 45, 193, 36, 43, "%");
        addFrame( 85, 193, 36, 43, "&");
        addFrame(125, 193, 35, 43, "@");
        addFrame(163, 193, 41, 43, "#");
        addFrame(208, 193, 33, 43, "$");
        addFrame(247, 193, 43, 43, "*");
        addFrame(292, 193, 31, 43, "-");
        addFrame(326, 193, 33, 43, "+");
    } // TitleFont()

    @Override
    public Rectangle2D drawText( Graphics2D g, double x, double y, String text, int alignment ) {
        return super.drawText(g, x, y, text.toUpperCase(), alignment);
    } // drawText( Graphics2D g, int x, int y, String text, int alignment )

} // class TitleFont
