
package Fishing.drawable;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * A frame of an animation.
 * 
 * @author Brad
 */
public abstract class AnimationFrame {

    /**
     * The frame set this frame belongs to.
     */
    private final AnimationFrameSet<? extends AnimationFrame> frameset;

    /**
     * The width of the frame.
     */
    private int width;

    /**
     * The height of the frame.
     */
    private int height;

    /**
     * The number of milliseconds to show this frame when displayed in an
     * animation.
     */
    private long delay;

    /**
     * Constructs a new instance.
     * 
     * @param frameset  The frame set this frame belongs to.
     * @param width     The width of the frame, in pixels.
     * @param height    The height of the frame, in pixels.
     * @param delay     The number of milliseconds to show this frame when
     *                  displayed in an animation.
     */
    public AnimationFrame(
        AnimationFrameSet<? extends AnimationFrame> frameset,
        int width,
        int height,
        long delay
    ) {
        this.frameset = Objects.requireNonNull(frameset, "The frameset cannot be null.");

        if (width < 1)
            throw new IllegalArgumentException("The width must be greater than 0.");
        this.width = width;

        if (height < 1)
            throw new IllegalArgumentException("The width must be greater than 0.");
        this.height = height;

        setDelay(delay);
    } // AnimationFrame(...)

    /**
     * Retrieves the frame set this frame belongs to.
     * 
     * @return  The frame set this frame belongs to.
     */
    public AnimationFrameSet<? extends AnimationFrame> getFrameSet() {
        return frameset;
    } // getFrameSet()

    /**
     * Retrieves the width of the frame.
     * 
     * @return  The width of the frame, in pixels.
     */
    public int getWidth() {
        return width;
    } // getWidth()

    /**
     * Retrieves the height of the frame.
     * 
     * @return  The height of the frame.
     */
    public int getHeight() {
        return height;
    } // getHeight()

    /**
     * Retrieves the number of milliseconds to show this frame when displayed
     * in an animation.
     * 
     * @return  The number of milliseconds to show this frame when displayed
     *          in an animation.
     */
    public long getDelay() {
        return delay;
    } // getDelay()

    /**
     * Sets the number of milliseconds to show this frame when displayed in
     * an animation.
     * 
     * @param value     The frame delay, in milliseconds.  If 0, the default
     *                  display frame rate will be used for the frame.
     * 
     * @throws  IllegalArgumentException if {@code value} is less than 0.
     */
    public void setDelay( long value ) {
        if (delay < 0)
            throw new IllegalArgumentException("The delay cannot be less than 0.");

        delay = value;
    } // setDelay( long value )

    /**
     * Retrieves the index of this frame within the containing frame set.
     * 
     * @return  The index of this frame within the containing frame set.
     */
    @SuppressWarnings("unchecked")
    public int getIndex() {
        // Find the frame's index in the frameset's frame collection.
        return ((AnimationFrameSet<AnimationFrame>)frameset).getFrameIndex(this);
    } // getIndex()

    /**
     * Retrieves the name of this frame within the containing frame set.
     * 
     * @return  The name of this frame within the containing frame set.
     */
    @SuppressWarnings("unchecked")
    public String getName() {
        // Find the frame's name in the frameset's frame collection.
        return ((AnimationFrameSet<AnimationFrame>)frameset).getFrameName(this);
    } // getName()

    /**
     * Draws this frame's image into the specified graphics context.
     * 
     * <p>To adjust the properties of the image, such as position or size,
     * apply a transformation matrix to the graphics context before calling
     * this method.</p>
     * 
     * @param g The graphics context to draw the frame into.
     * 
     * @throws  NullPointerException if {@code g} is {@code null}.
     */
    public void paint(Graphics2D g) {
        g.drawImage(
            getImage(),
            0, 0,
            null
        );
    } // paint(Graphics2D g)

    /**
     * Retrieves the image to render for this frame.
     * 
     * @return  The image to render for this frame.
     */
    abstract BufferedImage getImage();

} // AnimationFrame
