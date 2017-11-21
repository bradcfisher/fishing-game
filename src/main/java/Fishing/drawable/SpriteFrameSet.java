
package Fishing.drawable;

import java.util.Objects;

/**
 * A set of frames from a SpriteSheet.
 * 
 * @author Brad
 */
public class SpriteFrameSet
    extends AnimationFrameSet<SpriteFrame>
{

    /**
     * The SpriteSheet that this animation is defined for.
     */
    private final SpriteSheet sheet;

    /**
     * Constructs a new instance with the specified SpriteSheet as the source
     * of the frame images.
     * 
     * @param   sheet   The SpriteSheet to import frames from.
     * 
     * @throws  NullPointerException if {@code sheet} is null.
     */
    public SpriteFrameSet(SpriteSheet sheet) {
        this.sheet = Objects.requireNonNull(sheet, "The sheet parameter cannot by null");
    } // SpriteFrameSet(SpriteSheet sheet)

    /**
     * Constructs a new instance with the specified SpriteSheet as the source
     * of the frame images.
     * 
     * @param sheet         The SpriteSheet to import frames from.
     * @param frameWidth    The width of the frames.
     * @param frameHeight   The height of the frames.
     * @param framesPerRow  Number of frames per row/column.
     * @param numFrames     Number of frames to create.
     * @param horizontal    Whether to process rows (true) or columns (false)
     * @param xOfs          Horizontal offset in the image where the frames start.
     * @param yOfs          Vertical offset in the image where the frames start.
     * 
     * @throws  IllegalArgumentException if any of {@code frameWidth},
     *          {@code frameHeight}, {@code framesPerRow} or {@code numFrames}
     *          is less than 0.
     * @throws  NullPointerException if {@code sheet} is null.
     */
    public SpriteFrameSet(SpriteSheet sheet, int frameWidth, int frameHeight, int framesPerRow, int numFrames, boolean horizontal, int xOfs, int yOfs) {
        this(sheet);
        addFrames(xOfs, yOfs, frameWidth, frameHeight, framesPerRow, numFrames, horizontal);
    } // SpriteFrameSet()

    /**
     * Returns the SpriteSheet used as the source of frames for this this
     * animation.
     * 
     * @return  The SpriteSheet that is the source of frames for this animation.
     */
    public SpriteSheet getSheet() {
        return this.sheet;
    } // getSheet()

    /**
     * Adds a sequence of frames to the end of the animation.
     * 
     * @param xOfs          Horizontal offset in the image where the frames start.
     * @param yOfs          Vertical offset in the image where the frames start.
     * @param frameWidth    The width of the frames.
     * @param frameHeight   The height of the frames.
     * @param framesPerRow  Number of frames per row/column.
     * @param numFrames     Number of frames to create.
     * @param horizontal    Whether to process rows (true) or columns (false)
     * 
     * @throws  IllegalArgumentException if any of {@code frameWidth},
     *          {@code frameHeight}, {@code framesPerRow} or {@code numFrames}
     *          is less than 0.
     */
    public synchronized final void addFrames( int xOfs, int yOfs, int frameWidth, int frameHeight, int framesPerRow, int numFrames, boolean horizontal ) {
        if (framesPerRow <= 0)
            throw new IllegalArgumentException("The framesPerRow parameter must be greater than 0.");

        if (numFrames <= 0)
            throw new IllegalArgumentException("The numFrames parameter must be greater than 0.");

        int n = 0;
        int x = xOfs;
        int y = yOfs;

        while (n < numFrames) {
            for (int i = 0; i < framesPerRow && (n < numFrames); ++i) {
                //this.frames.add( new SpriteFrame(this, x, y, frameWidth, frameHeight, 0) );
                addFrame(x, y, frameWidth, frameHeight);

                if (horizontal)
                    x += frameWidth;
                else
                    y += frameHeight;
                ++n;
            } // for

            if (horizontal)
                y += frameHeight;
            else
                x += frameWidth;

            x = xOfs;
        } // while
    } // addFrames( int xOfs, int yOfs, int frameWidth, int frameHeight, int framesPerRow, int numFrames, boolean horizontal )

    /**
     * Adds a new frame to the end of the animation.
     * 
     * @param xOfs          Horizontal offset in the image where the frame starts.
     * @param yOfs          Vertical offset in the image where the frame starts.
     * @param frameWidth    The width of the frame.
     * @param frameHeight   The height of the frame.
     * @param name          Name to assign to the frame.
     * 
     * @return  The new frame that was added.
     * 
     * @throws  IllegalArgumentException if any of {@code frameWidth},
     *          {@code frameHeight}, {@code xOfs} or {@code yOfs} is less
     *          than 0.
     */
    public SpriteFrame addFrame( int xOfs, int yOfs, int frameWidth, int frameHeight, String name ) {
        if (frameWidth <= 0)
            throw new IllegalArgumentException("The frameWidth parameter must be greater than 0.");

        if (frameHeight <= 0)
            throw new IllegalArgumentException("The frameHeight parameter must be greater than 0.");

        if (xOfs < 0)
            throw new IllegalArgumentException("The xOfs parameter cannot be less than 0.");

        if (sheet.getImage().getWidth() - xOfs < frameWidth)
            throw new IllegalArgumentException("The xOfs parameter cannot exceed "+ (sheet.getImage().getWidth() - frameWidth) +".");

        if (yOfs < 0)
            throw new IllegalArgumentException("The yOfs parameter cannot be less than 0.");

        if (sheet.getImage().getHeight() - yOfs < frameHeight)
            throw new IllegalArgumentException("The yOfs parameter cannot exceed "+ (sheet.getImage().getHeight() - frameHeight) +".");

        SpriteFrame frame = new SpriteFrame(this, xOfs, yOfs, frameWidth, frameHeight, 0);
        synchronized (this) {
            super.addFrame( frame );
        }

        if (name != null)
            setFrameName(frame, name);

        return frame;
    } // addFrame( int xOfs, int yOfs, int frameWidth, int frameHeight, String name )

    /**
     * Adds a new frame to the end of the animation.
     * 
     * @param xOfs          Horizontal offset in the image where the frame starts.
     * @param yOfs          Vertical offset in the image where the frame starts.
     * @param frameWidth    The width of the frame.
     * @param frameHeight   The height of the frame.
     * 
     * @return  The new frame that was added.
     * 
     * @throws  IllegalArgumentException if any of {@code frameWidth},
     *          {@code frameHeight}, {@code xOfs} or {@code yOfs} is less
     *          than 0.
     */
    public SpriteFrame addFrame( int xOfs, int yOfs, int frameWidth, int frameHeight ) {
        return addFrame(xOfs, yOfs, frameWidth, frameHeight, null);
    } // addFrame( int xOfs, int yOfs, int frameWidth, int frameHeight )

} // class SpriteFrameSet
