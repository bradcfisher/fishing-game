
package Fishing.drawable.text;

import Fishing.drawable.AnimationFrameSet;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

/**
 * A frame set where each frame represents a character of a bitmap font.
 * 
 * @author Brad
 */
public class BitmapFont
    extends AnimationFrameSet<BitmapFontFrame>
{

    /**
     * Specifies to align the left edge of rendered text with the left edge
     * of the rendering viewport.
     */
    public static final int ALIGN_LEFT = 0;

    /**
     * Specifies to center rendered text between the left and right edges
     * of the rendering viewport.
     */
    public static final int ALIGN_CENTER = 1;

    /**
     * Specifies to align the right edge of rendered text with the right
     * edge of the rendering viewport.
     */
    public static final int ALIGN_RIGHT = 2;

    /**
     * Units of additional spacing to add between each character rendered.
     */
    private double characterSpacing = 0;

    /**
     * Units of additional spacing to add between each line rendered.
     */
    private double lineSpacing = 0;

    /**
     * Retrieves the number of additional units of spacing added between each
     * rendered character.
     *
     * @return The number of additional units of spacing added between each
     *         rendered character.
     */
    public double getCharacterSpacing() {
        return characterSpacing;
    } // getCharacterSpacing()

    /**
     * Assigns a new value for the units of additional spacing to add between
     * each character rendered.
     * 
     * @param value The number of additional units of spacing to add between
     *              each rendered character.
     */
    public void setCharacterSpacing( double value ) {
        characterSpacing = value;
    } // setCharacterSpacing( double value )

    /**
     * Retrieves the number of additional units of spacing added between each
     * rendered line of text.
     *
     * @return The number of additional units of spacing added between each
     *         rendered line of text.
     */
    public double getLineSpacing() {
        return lineSpacing;
    } // getLineSpacing()

    /**
     * Assigns a new value for the units of additional spacing to add between
     * each rendered line of text.
     * 
     * @param value The number of additional units of spacing to add between
     *              each rendered line of text.
     */
    public void setLineSpacing( double value ) {
        lineSpacing = value;
    } // setLineSpacing( double value )

    /**
     * Represents a 2-dimensional size (width x height) using double-precision
     * floating point values.
     */
    public static class Dimension2DDouble
        extends Dimension2D
    {

        /**
         * The width.
         */
        private double width;
        
        /**
         * The height.
         */
        private double height;

        /**
         * Constructs a new Dimension2DDouble with an initial width and height
         * of 0.
         */
        public Dimension2DDouble() {
            width = 0;
            height = 0;
        } // Dimension2DDouble()

        /**
         * Constructs a new instance.
         * 
         * @param width The initial width value.
         * @param height The initial height value.
         */
        public Dimension2DDouble( double width, double height ) {
            this.width = width;
            this.height = height;
        } // Dimension2DDouble( double width, double height )

        /**
         * Returns a textual representation of this dimension (eg:
         * "Dimension2DDouble[width=1, height=2]").
         * 
         * @return a textual representation of this dimension
         */
        @Override
        public String toString() {
            return getClass().getName() + "[width=" + width + ",height=" + height + "]";
        } // toString()

        /**
         * Retrieves the width assigned to this dimension.
         * 
         * @return The width assigned to this dimension.
         */
        @Override
        public double getWidth() {
            return width;
        } // getWidth()

        /**
         * Retrieves the height assigned to this dimension.
         * 
         * @return The height assigned to this dimension.
         */
        @Override
        public double getHeight() {
            return height;
        } // getHeight()

        /**
         * Assigns new values to this dimension for both the width and height.
         * 
         * @param width The new width to assign.
         * @param height The new height to assign.
         */
        @Override
        public void setSize(double width, double height) {
            this.width = width;
            this.height = height;
        } // setSize(double width, double height)
    } // class Dimension2DDouble

    /**
     * Retrieves the computed size of the specified text string if it were to
     * be rendered using this font.
     * 
     * @param   text    The text string to determine the size for.
     * 
     * @return   The computed size of the specified text string if it were to
     *           be rendered using this font.
     */
    public Dimension2DDouble getTextSize( String text ) {
        Rectangle2D bounds = drawText( null, 0, 0, text, ALIGN_LEFT );
        return new Dimension2DDouble( bounds.getWidth(), bounds.getHeight() );
    } // getTextSize( String text )

    /**
     * Retrieves the sprite frame image assigned to the specified character.
     * 
     * @param   ch  The character to retrieve the sprite frame image for.
     * 
     * @return  The sprite frame image associated with the specified character.
     *          If the character is not defined for this font, will return the
     *          "Unknown" frame.
     */
    public synchronized BitmapFontFrame getFrameForChar( char ch ) {
        BitmapFontFrame f = null;

        try {
            f = getFrame(""+ch);
        } catch (IllegalArgumentException ex) {
            // No frame with that name
        }

        if (f == null) {
            f = getFrame("Unknown");
        }

        return f;
    } // getFrameForChar( char ch )

    /**
     * Retrieves the amount of descent below the baseline.
     * 
     * <p>The ({@code ascent} + {@code descent}) should be roughly equal to the
     * height of the font.
     * 
     * @return  The amount of descent below the baseline (typically 0 or
     *          positive).
     */
    public int getDescent() {
        return 0;
    } // getDescent()

    /**
     * Returns the amount of ascent above the baseline.
     * 
     * <p>The ({@code ascent} + {@code descent}) should be roughly equal to the
     * height of the font.
     * 
     * @return  The amount of ascent above the baseline (typically negative).
     */
    public int getAscent() {
        return getFrameHeight();
    } // getAscent()

    /**
     * Retrieves the height of the font.
     *
     * @return  The height of the font.
     */
    public int getFontSize() {
        return getFrameHeight();
    } // getFontSize()

    /**
     * Returns the horizontal offset of the specified character within the
     * given line, when rendered with this font.
     * 
     * <p>This only works with a single line of text.
     * 
     * @param line  The line of text to search.
     * @param index The index of the character to retrieve the offset for.
     * 
     * @return  The horizontal offset of the left side of the specified
     *          character within the given line, when rendered with this font.
     *          If the {@code index} is equal to the length of {@code line},
     *          returns the offset of the right edge of the last character in
     *          the line.
     * 
     * @throws  IllegalArgumentException if the {@code index} is less than 0
     *          or greater than the number of characters in {@code line}.
     */
    public double getCharacterOffset( String line, int index ) {
        if (line == null)
            line = "";

        if (index < 0)
            throw new IllegalArgumentException("The character index cannot be less than 0.");

        int l = line.length();
        if (index > l)
            throw new IllegalArgumentException("The character index exceeds the length of the string.");

        --l;

        double pos = 0;

        BitmapFontFrame f;
        if ((index == 0) && (l >= 0)) {
            char ch = line.charAt(0);
            f = getFrameForChar(ch);

            Rectangle2D bounds = f.getCharBounds();
            pos = (bounds.getX() < 0) ? -bounds.getX() : 0;
        } else {
            for (int i = 0; i < index; ++i) {
                char ch = line.charAt(i);

                f = getFrameForChar(ch);

                Rectangle2D bounds = f.getCharBounds();
                pos += (i == 0 && (bounds.getX() < 0) ? -bounds.getX() : 0) +
                     (i == l ? Math.max(bounds.getMaxX(), f.getCharAdvance()) : f.getCharAdvance()) +
                     characterSpacing;
            } // for
        }

        return pos;
    } // getCharacterOffset( String line, int index )

    /**
     * Computes the size of the specified single line of text if it were to
     * be rendered using this font.
     * 
     * @param   line    The line of text to determine the size for.
     * 
     * @return  The computed size of the specified line of text if it were to
     *          be rendered using this font.
     * 
     * @throws  NullPointerException if {@code line} is {@code null}.
     */
    private Dimension calcLineSize( String line ) {
        double w = 0;
        double h = 0;

        int l = line.length() - 1;
        double spacing = 0;

        for (int i = 0; i <= l; ++i) {
            char ch = line.charAt(i);

            BitmapFontFrame f = getFrameForChar(ch);

            Rectangle2D bounds = f.getCharBounds();
            w += (i == 0 && (bounds.getX() < 0) ? -bounds.getX() : 0) +
                 (i == l ? Math.max(bounds.getMaxX(), f.getCharAdvance()) : f.getCharAdvance()) +
                 spacing;
            spacing = characterSpacing;

            double t = f.getCharBounds().getHeight();
            if (t > h)
                h = t;
        } // for

        return new Dimension( (int)Math.round(w), (int)Math.round(h) );
    } // calcLineSize( String line, int charSpacing )

    /**
     * Draws the specified text using this font into a graphics context.
     * 
     * @param   g   The graphics context to draw the text into.  May be
     *              {@code null}, in which case only the bounding rectangle
     *              computation is performed.
     * @param   x   The horizontal position at which to draw the text.
     * @param   y   The vertical position at which to draw the text.
     * @param   text        The text to draw.  A {@code null} value is
     *              rendered as an empty string.
     * @param   alignment   Alignment to use when drawing the text.  Must be
     *              one of {@link #ALIGN_CENTER}, {@link #ALIGN_RIGHT} or
     *              {@link #ALIGN_LEFT}.  Unrecognized values will be rendered
     *              using the semantics of {@link #ALIGN_LEFT}.
     * 
     * @return  Returns the computed bounding rectangle which fully contains
     *          the drawn text.
     */
    public synchronized Rectangle2D drawText( Graphics2D g, double x, double y, String text, int alignment ) {
        if (text == null)
            text = "";

        int l = text.length();

        // Draw one line at a time
        int charPos = 0;
        
        double yPos = y;
        double minX = x;
        double width = 0;
        double height = 0;
        double lSpacing = 0;

        while (charPos < l) {
            boolean crFlag = true;

            int eol = text.indexOf('\r', charPos);
            if (eol ==  -1) {
                crFlag = false;
                eol = text.indexOf('\n', charPos);
                if (eol ==  -1)
                    eol = l;
            }

            if (eol > charPos) {
                String line = text.substring(charPos, eol);

                // Determine width/height of line
                Dimension size = calcLineSize( line );

                // Determine alignment position
                double xPos = x;
                switch (alignment) {
                    case ALIGN_CENTER:
                        xPos -= size.getWidth() / 2;
                        break;
                    case ALIGN_RIGHT:
                        xPos -= size.getWidth();
                        break;
                    case ALIGN_LEFT:
                    default:
                        // Nothing to do
                } // switch

                if (xPos < minX) {
                    width += (minX - xPos);
                    minX = xPos;
                }
                if (width < size.width)
                    width = size.width;

                // Draw the characters in the line
                AffineTransform m = (g != null ? g.getTransform() : null);

                int ll = line.length();
                double cSpacing = 0;

                for (int i = 0; i < ll; ++i) {
                    char ch = line.charAt(i);

                    BitmapFontFrame f = getFrameForChar(ch);

                    xPos += cSpacing;
                    cSpacing = characterSpacing;

                    if (g != null) {
                        double xAdj = f.getCharBounds().getX();
                        g.translate(xPos + ((i > 0 || xAdj < 0) ? xAdj : 0), yPos);
                        f.paint(g);
                    }

                    xPos += f.getCharAdvance();

                    if (g != null)
                        g.setTransform(m);
                } // for

                height += size.height + lSpacing;
                lSpacing = lineSpacing;

                yPos += size.height + lineSpacing;
            }

            charPos = eol + 1;
            if (crFlag && (charPos < l) && (text.charAt(charPos) == '\n'))
                ++charPos;
        } // while

        return new Rectangle2D.Double( minX, y, width, height );
    } // drawText( Graphics2D g, double x, double y, String text, int alignment )

} // BitmapFont
