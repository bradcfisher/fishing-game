
package Fishing.drawable.text;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * A bitmap font generated from a Java system font.
 * 
 * @author Brad
 */
public class GeneratedFont
    extends BitmapFont
{

    /**
     * The font to use for rendering characters.
     */
    private Font font;

    /**
     * FontRenderContext to use for computing font metrics.
     */
    private final FontRenderContext fontRenderContext = new FontRenderContext(null, true, false);

    /**
     * The amount of ascent above the baseline.
     */
    private int ascent;

    /**
     * The amount of descent below the baseline.
     */
    private int descent;

    /**
     * The color to use when rendering characters.
     */
    private Color color = Color.WHITE;

    /**
     * The color used when rendering character outlines, or {@code null} to
     * disable outlines.
     */
    private Color outlineColor = null;

    /**
     * The width used when drawing character outlines, or 0 to disable outlines.
     */
    private float outlineWidth;

    /**
     * Constructs a new instance utilizing the specified font, a default
     * color of white and no outline.
     * 
     * @param font  The font to use for rendering characters.
     * 
     * @throws  NullPointerException if {@code font} is {@code null}.
     */
    public GeneratedFont( Font font ) {
        this(font, Color.WHITE);
    } // GeneratedFont( Font font )

    /**
     * Constructs a new instance utilizing the specified font, color and no
     * outline.
     * 
     * @param font  The font to use for rendering characters.
     * @param color The color to use when rendering characters.
     * 
     * @throws  NullPointerException if either of {@code font} or {@code font}
     *          is {@code null}.
     */
    public GeneratedFont( Font font, Color color ) {
        super();

        setFont(font);
        setOutlineWidth(font.getSize2D() * 0.2f);
        setColor(color);
    } // GeneratedFont( Font font )

    /**
     * Retrieves the font used for rendering characters.
     * 
     * @return  The font used for rendering characters.
     */
    public Font getFont() {
        return font;
    } // getFont()

    /**
     * Sets the font to use for rendering characters.
     * 
     * @param value The new font to use for rendering characters.
     * 
     * @throws  NullPointerException if {@code value} is {@code null}.
     */
    public void setFont( Font value ) {
        font = Objects.requireNonNull(value, "The font cannot be null");

        LineMetrics lm = font.getLineMetrics("X", fontRenderContext);
        ascent = (int) lm.getAscent();
        descent = (int) lm.getDescent();

        // Force regeneration of the font glyphs
        clearFrames();
    } // setFont( Font font )

    /**
     * Retrieves the color used when rendering characters.
     * 
     * @return  The color used when rendering characters.
     */
    public Color getColor() {
        return color;
    } // getColor()

    /**
     * Sets the color to use when rendering characters.
     * 
     * @param   value   The new color to use when rendering characters.
     * 
     * @throws  NullPointerException if {@code value} is {@code null}.
     */
    public void setColor( Color value ) {
        if (color.equals(value))
            return;

        color = Objects.requireNonNull(value, "The color cannot be null");

        // Force reallocation of sheet on next request for character
        clearFrames();
    } // setColor( Color value )

    /**
     * Retrieves the color used when rendering character outlines.
     * 
     * @return  The color used when rendering character outlines.
     *          If {@code null}, then no outline is drawn.
     */
    public Color getOutlineColor() {
        return outlineColor;
    } // getOutlineColor()

    /**
     * Sets the color to use when rendering character outlines.
     * 
     * @param   value   The new color to use when rendering character outlines.
     *                  If {@code null}, then no outlines will be drawn.
     */
    public void setOutlineColor( Color value ) {
        if (outlineColor == null) {
            if (value == null) {
                return;
            }
        } else if (outlineColor.equals(value)) {
            return;
        }

        outlineColor = value;

        // Force reallocation of sheet on next request for character
        clearFrames();
    } // setOutlineColor( Color value )

    /**
     * Retrieves the width used when drawing character outlines.
     * 
     * @return  The width used when drawing character outlines.
     */
    public float getOutlineWidth() {
        return outlineWidth;
    } // getOutlineWidth()

    /**
     * Sets the width to use when drawing character outlines.
     * 
     * @param   value   The new width to use when drawing character outlines.
     * 
     * @throws  IllegalArgumentException if the {@code value} is less than 0.
     */
    public void setOutlineWidth( float value ) {
        if (outlineWidth == value)
            return;

        if (value < 0) {
            throw new IllegalArgumentException(
                "The outlienWidth cannot be less than 0"
            );
        }

        outlineWidth = value;

        // Force reallocation of sheet on next request for character
        clearFrames();
    } // setOutlineWidth( float value )

    @Override
    protected void clearFrames() {
        super.clearFrames();

        // Need to add the "Unknown" frame
        newCharFrame('?', "Unknown");
    } // clearFrames()

    /**
     * Creates a new frame for the specified character and assigns it the
     * given name.
     * 
     * @param ch    The character to create the frame for.
     * @param name  The name to assign the frame.  Typically a string
     *              containing the single character specified in {@code ch},
     *              though can be different for special characters like
     *              "Unknown".
     * 
     * @return  The new frame that was created.
     */
    private synchronized BitmapFontFrame newCharFrame( char ch, String name ) {
//System.out.println("Allocate char: ch="+ ch +" (frame="+ (getNumFrames() + 1) +")");

        // Draw the glyph to a new image.

        GlyphVector v = font.createGlyphVector(fontRenderContext, ""+ch);

        double ow = outlineWidth / 2;
        Shape s;

        s = v.getOutline();
        Rectangle2D charBounds = s.getBounds2D();
        Rectangle2D logBounds = v.getLogicalBounds();
        charBounds.setRect(
                    charBounds.getX(),
                    Math.min(logBounds.getY(), charBounds.getY()) + ascent,
                    charBounds.getWidth() + outlineWidth,
                    logBounds.getHeight()
                );

        s = v.getOutline((int)(ow - charBounds.getX()), (int)(ascent - ow));

        BufferedImage image = new BufferedImage(
            (int) Math.ceil(charBounds.getWidth()),
            (int) Math.ceil(charBounds.getHeight()),
            BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g = image.createGraphics();

        if ((outlineColor != null) && (outlineWidth > 0)) {
            g.setColor( outlineColor );
            g.setStroke( new BasicStroke(outlineWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL) );
            g.draw(s);
        }

        if (color != null) {
            g.setColor( color );
            g.fill(s);
        }
        
        BitmapFontFrame frame = new BitmapFontFrame(
            this,
            image,
            v.getGlyphPosition(1).getX(),
            charBounds
        );

        addFrame(frame, name);
        
        return frame;
    } // newCharFrame( char ch, String name )

    @Override
    public int getDescent() {
        return descent;
    } // getDescent()

    @Override
    public int getAscent() {
        return ascent;
    } // getAscent()

    @Override
    public int getFontSize() {
        return font.getSize();
    } // getFontSize()

    @Override
    public synchronized BitmapFontFrame getFrameForChar( char ch ) {
        BitmapFontFrame f = null;
        try {
            f = getFrame(""+ch);
        } catch (IllegalArgumentException ex) {
            // No frame with that name
        }

        if (f == null) {
            // Create new frame for the specified character
            f = newCharFrame(ch, ""+ch);
        }

        return f;
    } // getFrameForChar( char ch )

} // class GeneratedFont
