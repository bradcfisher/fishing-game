
package Fishing.drawable.text;

import Fishing.drawable.Drawable;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

/**
 * Text label object.
 * 
 * @author Brad
 */
public class BitmapText
    extends Drawable
{

    /**
     * The font to use when rendering this text.
     */
    private BitmapFont font;

    /**
     * The text string to render.
     */
    private String text = null;

    /**
     * The alignment to use when rendering the text.
     * 
     * @see BitmapFont#drawText(java.awt.Graphics2D, double, double, java.lang.String, int)
     */
    private int alignment;

    /**
     * The computed starting x coordinate to use when drawing the text, in
     * local coordinates.  This value is computed by the
     * {@link #invalidateSize()} method based on the assigned {@code alignment}.
     */
    private int xOfs = 0;

    /**
     * Constructs a new text label with no text content.
     * 
     * @param   font    The font to use when rendering this label.
     * 
     * @throws  NullPointerException if {@code font} is {@code null}.
     */
    public BitmapText(BitmapFont font) {
        setFont(font);
    } // BitmapText(BitmapFont font)

    /**
     * Constructs a new text label with the specified text content.
     * 
     * @param   font    The font to use when rendering this label.
     * @param   text    The text to display.
     * 
     * @throws  NullPointerException if {@code font} is {@code null}.
     */
    public BitmapText(BitmapFont font, String text) {
        setFont(font);
        setText(text);
    } // BitmapText(BitmapFont font, String text)
    /**
     * Retrieves the font used when rendering this label.
     * 
     * @return  The font used when rendering this label.
     */
    public BitmapFont getFont() {
        return font;
    } // setFont()

    /**
     * Sets the font to use when rendering this label.
     * 
     * @param   value   The new font to use when rendering this label.
     * 
     * @throws  NullPointerException if {@code value} is {@code null}.
     */
    public void setFont( BitmapFont value ) {
        font = Objects.requireNonNull(value, "The font cannot be null");
        invalidateSize();
    } // setFont( BitmapFont value )

    /**
     * Retrieves the text displayed by this label.
     * 
     * @return  The text displayed by this label, or {@code null} if no text
     *          is assigned.
     */
    public String getText() {
        return text;
    } // setText()

    /**
     * Sets the text displayed by this label.
     * 
     * @param   value   The new text to displayed for this label, or
     *                  {@code null} if no text is assigned.
     */
    public void setText( String value ) {
        text = value;
        invalidateSize();
    } // setText( String value )

    /**
     * Retrieves the alignment used when rendering the text.
     * 
     * @return  The alignment used when rendering the text.
     * 
     * @see BitmapFont#drawText(java.awt.Graphics2D, double, double, java.lang.String, int)
     */
    public int getAlignment() {
        return alignment;
    } // setAlignment()

    /**
     * Sets the alignment to use when rendering the text.
     * 
     * @param   value   The new alignment to use when rendering the text.
     * 
     * @see BitmapFont#drawText(java.awt.Graphics2D, double, double, java.lang.String, int)
     */
    public void setAlignment( int value ) {
        alignment = value;
        invalidateSize();
    } // setAlignment( int value )

    /**
     * Recalculates the unscaled size of this component based on the assigned
     * {@code font} and {@code text}.
     */
    private void invalidateSize() {
        // Calculate the width/height from the text here
        Rectangle2D size;

        if (text == null) {
            size = new Rectangle(0, 0, 0, 0);
        } else
            size = font.drawText(null, 0, 0, text, alignment);

        xOfs = (int) -size.getX();
        super.setUnscaledSize( size.getWidth(), size.getHeight() );
    } // invalidateSize()

    /**
     * Renders this text label to the specified graphics context.
     * 
     * @param   g   The graphics context to draw the label into.
     */
    @Override
    public void paint( Graphics2D g ) {
        if (text == null)
            return;

        font.drawText(g, xOfs, 0, text, alignment);
    } // paint( Graphics2D g )

} // class BitmapText
