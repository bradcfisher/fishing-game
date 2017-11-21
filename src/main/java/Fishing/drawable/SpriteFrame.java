
package Fishing.drawable;

import java.awt.image.BufferedImage;

/**
 * Represents an animation frame derived from a sprite sheet.
 *
 * @author Brad
 */
public class SpriteFrame
    extends AnimationFrame
{

    /**
     * The horizontal position of the frame image within the associated sprite
     * sheet.
     */
    private int x;

    /**
     * The vertical position of the frame image within the associated sprite
     * sheet.
     */
    private int y;

    /**
     * Constructs a new instance.
     * 
     * @param frameset  The frame set this frame belongs to.
     * @param x         The horizontal position of the frame image within
     *                  the associated sprite sheet.
     * @param y         The vertical position of the frame image within the
     *                  associated sprite sheet.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     * @param delay     The number of milliseconds to show this frame when
     *                  displayed in an animation.
     * 
     * @throws  IllegalArgumentException if either of {@code x} or {@code y}
     *          are less than 0.
     * @throws  NullPointerException if {@code frameset} is {@code nul}.
     */
    public SpriteFrame(SpriteFrameSet frameset, int x, int y, int width, int height, long delay) {
        super(frameset, width, height, delay);

        if (x < 0)
            throw new IllegalArgumentException("The x parameter cannot be less than 0");

        if (y < 0)
            throw new IllegalArgumentException("The y parameter cannot be less than 0");

        BufferedImage image = frameset.getSheet().getImage();

        if (x + width > image.getWidth()) {
            throw new IllegalArgumentException("The x parameter cannot exceed "+ (image.getWidth() - width) +" for the specified width.");
        }

        if (y + height > image.getHeight()) {
            throw new IllegalArgumentException("The y parameter cannot exceed "+ (image.getHeight() - height) +" for the specified height.");
        }

        this.x = x;
        this.y = y;
    } // SpriteFrame(...)

    @Override
    public SpriteFrameSet getFrameSet() {
        return (SpriteFrameSet) super.getFrameSet();
    } // getFrameSet()

    /**
     * Retrieves the horizontal position of the frame image within the sprite
     * sheet.
     * 
     * @return  The horizontal position of the frame image within the sprite
     *          sheet.
     */
    public int getX() {
        return x;
    } // getX()

    /**
     * Retrieves the vertical position of the frame image within the sprite
     * sheet.
     * 
     * @return  The vertical position of the frame image within the sprite
     *          sheet.
     */
    public int getY() {
        return y;
    } // getY()

    @Override
    public BufferedImage getImage() {
        SpriteSheet sheet = getFrameSet().getSheet();
        if (sheet == null)
            throw new IllegalArgumentException("The frameset doesn't have an associated SpriteSheet.");

        int width = getWidth();
        int height = getHeight();

        return sheet.getImage().getSubimage(x, y, width, height);
    } // getImage()

} // SpriteFrame
