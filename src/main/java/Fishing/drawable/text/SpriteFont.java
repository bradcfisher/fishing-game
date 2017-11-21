
package Fishing.drawable.text;

import Fishing.drawable.SpriteSheet;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

/**
 * A SpriteFrameSet specialization which interprets frames of a sprite sheet
 * image as characters of a font.
 * 
 * @author Brad
 */
public abstract class SpriteFont
    extends BitmapFont
{

    /**
     * The SpriteSheet to grab character frames from.
     */
    private final SpriteSheet sheet;

    /**
     * Constructs a new instance with the specified SpriteSheet as the source
     * of the frame images.
     * 
     * @param   sheet   The SpriteSheet to grab frames from.
     */
    public SpriteFont(SpriteSheet sheet) {
        this.sheet = Objects.requireNonNull(sheet, "The sheet parameter cannot be null");
    } // SpriteFont(SpriteFrameSet frameSet)

    /**
     * Adds a new character frame with the specified name/character, using the
     * bounds of the image for the character bounds and advance values.
     * 
     * @param x     The horizontal offset in the sprite image of the top-left
     *              of the frame subimage.
     * @param y     The vertical offset in the sprite image of the top-left
     *              of the frame subimage.
     * @param w     The width of the frame subimage.
     * @param h     The height of the frame subimage.
     * @param name  The name to assign to the frame, generally the character
     *              the frame represents or a special name such as "Unknown".
     */
    protected void addFrame( int x, int y, int w, int h, String name ) {
        addFrame(x, y, w, h, name, w, null );
    } // addFrame( int x, int y, int w, int h, String name )

    /**
     * Adds a new character frame with the specified name/character and bounds
     * rectangle.
     * 
     * @param x     The horizontal offset in the sprite image of the top-left
     *              of the frame subimage.
     * @param y     The vertical offset in the sprite image of the top-left
     *              of the frame subimage.
     * @param w     The width of the frame subimage.
     * @param h     The height of the frame subimage.
     * @param name  The name to assign to the frame, generally the character
     *              the frame represents or a special name such as "Unknown".
     * @param charAdvance   The character advance for the frame.  This value
     *                      is added to the current horizontal output position
     *                      after rendering the character frame.  It is often
     *                      the same as the width of the frame's image, but
     *                      may differ.
     * @param charBounds    The character bounds for the frame.  If
     *                      {@code null}, the bounds of the frame image will
     *                      be used.
     */
    protected synchronized void addFrame( int x, int y, int w, int h, String name, double charAdvance, Rectangle2D charBounds ) {
        BitmapFontFrame frame = new BitmapFontFrame( this, sheet.getImage().getSubimage(x, y, w, h), charAdvance, charBounds );
        addFrame(frame, name);
    } // addFrame( int x, int y, int w, int h, String name, double charAdvance, Rectangle2D charBounds )

} // class SpriteFont
