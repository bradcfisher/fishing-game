
package Fishing.drawable.text;

import Fishing.drawable.ImageFrame;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * A frame of a bitmap font.
 * 
 * @author Brad
 */
public class BitmapFontFrame
    extends ImageFrame
{

    /**
     * The character advance for the frame.
     * 
     * <p>This value is added to the current horizontal output position after
     * rendering the character frame.  It is often the same as the width of
     * the frame's image, but may differ if needed.
     */
    private final double charAdvance;

    /**
     * The character bounds for the frame.
     */
    private final Rectangle2D charBounds;

    /**
     * Constructs a new instance.
     * 
     * @param frameset      The frame set this frame belongs to.
     * @param image         The image to use for this frame.
     * @param advance       The character advance for the frame.  This value
     *                      is added to the current horizontal output position
     *                      after rendering the character frame.  It is often
     *                      the same as the width of the frame's image, but may
     *                      differ if needed.
     * @param charBounds    The character bounds for the frame.  If
     *                      {@code null}, the bounds of the frame image will
     *                      be used.
     */
    public BitmapFontFrame( BitmapFont frameset, BufferedImage image, double advance, Rectangle2D charBounds ) {
        super(frameset, image, 0);
        
        this.charAdvance = advance;
        this.charBounds = (charBounds != null
                            ? charBounds
                            : new Rectangle2D.Double( 0, 0, image.getWidth(), image.getHeight() ));
    } // BitmapFontFrame( SpriteFrame f, double advance, Rectangle2D charBounds )


    /**
     * Retrieves the character advance for the frame.
     * 
     * <p>This value is added to the current horizontal output position after
     * rendering the character frame.  It is often the same as the width of the
     * frame's image, but may differ if needed.
     * 
     * @return  The character advance for the frame.
     */
    public double getCharAdvance() {
        return charAdvance;
    } // getCharAdvance()

    /**
     * Retrieves the character bounds for the frame.
     * 
     * @return  The character bounds for the frame.
     */
    public Rectangle2D getCharBounds() {
        return charBounds;
    } // getCharBounds()

} // class BitmapFontFrame
