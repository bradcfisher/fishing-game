
package Fishing.drawable;

import java.awt.image.BufferedImage;

/**
 * An AnimationFrame subclass which simply wraps a buffered image.
 * 
 * @author Brad
 */
public class ImageFrame
    extends AnimationFrame
{

    /**
     * The image to render for this frame.
     */
    private final BufferedImage image;

    /**
     * Constructs a new instance.
     * 
     * @param frameset  The frame set this frame belongs to.
     * @param image     The image to render for this frame.
     * @param delay     The number of milliseconds to show this frame when
     *                  displayed in an animation.
     */
    public ImageFrame(AnimationFrameSet<? extends AnimationFrame> frameset, BufferedImage image, long delay) {
        super(frameset, image.getWidth(), image.getHeight(), delay);
        this.image = image;
    }

    @Override
    BufferedImage getImage() {
        return image;
    } // getImage()

} // ImageFrame
