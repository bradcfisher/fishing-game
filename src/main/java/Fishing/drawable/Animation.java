
package Fishing.drawable;

import Fishing.drawable.events.DrawableListeners;
import Fishing.drawable.events.SpriteAnimationListener;
import Fishing.drawable.events.SpriteAnimationEvent;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class represents a frame-based animation.
 * 
 * @author Brad
 */
public class Animation
    extends Drawable
{

    /**
     * Enumeration for specifying the direction an animation should play
     * through its frames.
     */
    public static enum Direction {
        /**
         * Indicates the animation should play forward through its frames, in
         * order of increasing frame index.
         */
        FORWARD,
        
        /**
         * Indicates the animation should play backward through its frames, in
         * order of decreasing frame index.
         */
        BACKWARD
    }

    /**
     * Shared timer instance/thread shared between all animations.
     */
    static private Timer timer;

    /**
     * Whether the animation repeats or stops after the last frame.
     */
    private boolean repeat;

    /**
     * A recurrent timer task scheduled to execute at the specified
     * {@code frameRate} and which performs the iteration through the animation
     * frames.
     * 
     * <p>This property is {@code null} if the animation is not currently
     * running.
     */
    private TimerTask timerTask;

    /**
     * The frame rate to play the animation at, in frames per second.
     */
    private double frameRate = 15;

    /**
     * The direction of play.
     */
    private Direction playDirection = Direction.FORWARD;

    /**
     * The frame set associated with this animation.
     */
    private final AnimationFrameSet<? extends AnimationFrame> frameset;

    /**
     * The current frame index, or -1 if no current frame is set.
     */
    private int currentIndex = -1;

    /**
     * The index of the target frame set by a call to
     * {@link #runToFrameIndex(int)} or {@link #runToFrameName(java.lang.String)},
     * or -1 if no target frame is set.
     */
    private int targetFrameIndex = -1;

    /**
     * The registered animation listeners.
     */
    private DrawableListeners<SpriteAnimationListener> animationListeners
            = new DrawableListeners<>();

    /**
     * Constructs a new instance associated with the specified frame set.
     * 
     * @param frameset  The frame set to associate with this animation.
     */
    public Animation(AnimationFrameSet<? extends AnimationFrame> frameset) {
        this.frameset = Objects.requireNonNull(frameset, "The frameset cannot be null.");

        if (timer == null)
            timer = new Timer();

        setUnscaledSize( frameset.getFrameWidth(), frameset.getFrameHeight() );
        setCenterX(frameset.getFrameWidth() / 2);
        setCenterY(frameset.getFrameHeight() / 2);

        repeat = false;

        currentIndex = 0;
    } // Animation(AnimationFrameSet frameset)

    /**
     * Retrieves the current frame for the animation.
     * 
     * @return  The current frame for the animation.
     * 
     * @throws  IllegalArgumentException if the {@code currentIndex} value is
     *          outside the valid range of frames in the associated frame set.
     */
    public AnimationFrame getCurrentFrame() {
        return (AnimationFrame) frameset.getFrame(currentIndex);
    } // getCurrentFrame();

    /**
     * Adds a new SpriteAnimationListener to the list of registered listeners,
     * if it is not already found in the list.
     * 
     * @param listener  The listener to add.
     * 
     * @throws  IllegalArgumentException if {@code listener} is {@code null}.
     */
    public void addAnimationListener(SpriteAnimationListener listener) {
        animationListeners.add(listener);
    } // addAnimationListener(SpriteAnimationListener listener)

    /**
     * Removes a SpriteAnimationListener from the list of registered listeners,
     * if it is found in the list.
     * 
     * @param listener  The listener to remove.
     */
    public void removeAnimationListener(SpriteAnimationListener listener) {
        animationListeners.remove(listener);
    } // removeAnimationListener(SpriteAnimationListener listener)

    /**
     * Returns whether the animation repeats or stops after the last frame.
     * 
     * @return  Whether the animation repeats or stops after the last frame.
     */
    public boolean getRepeat() {
        return repeat;
    } // getRepeat()

    /**
     * Whether the animation is currently running or not.
     * 
     * @return  Whether the animation is currently running or not.
     */
    public boolean isRunning() {
        return (timerTask != null);
    } // isRunning()

    /**
     * Sets whether the animation repeats or stops after the last frame.
     * 
     * @param   value   Whether the animation should repeat (true) or stop after
     *                  the last frame (false).
     */
    public void setRepeat( boolean value ) {
        repeat = value;
    } // setRepeat( boolean value )

    /**
     * Retrieves the frame rate at which the animation will be played.
     *
     * @return  The frame rate at which the animation will be played, in frames
     *          per second.
     */
    public double getFrameRate() {
        return frameRate;
    } // getFrameRate()

    /**
     * Sets the frame rate to play the animation at.
     *
     * @param   value   The new frame rate to play the animation at, in frames
     *                  per second.
     * 
     * @throws  IllegalArgumentException if {@code value} is less than 0 or
     *          greater than 1000.
     */
    public synchronized void setFrameRate( double value ) {
        if ((value <= 0) || (value > 1000))
            throw new IllegalArgumentException(
                "The frameRate must be greater than 0 and no greater than 1000."
            );

        frameRate = value;

        if (isRunning())
            startTimerTask();
    } // setFrameRate( double value )

    /**
     * Starts a new recurrent timer task scheduled to execute at the specified
     * {@code frameRate} and which performs the iteration through the animation
     * frames.
     * 
     * <p>If the animation is currently running, the existing timer task is
     * canceled before assigning a new task.
     */
    private void startTimerTask() {
        long t = 0;

        if (isRunning()) {
            // Stop the existing timer
            t = System.currentTimeMillis() - timerTask.scheduledExecutionTime();
            timerTask.cancel();
        }

        // Start a new timer
        timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    boolean next = false;
                                    if (playDirection == Direction.FORWARD) {
                                        next = ((currentIndex != targetFrameIndex) &&
                                                (repeat || currentIndex < frameset.size() - 1));
                                    } else if (playDirection == Direction.BACKWARD) {
                                        next = ((currentIndex != targetFrameIndex) &&
                                                (repeat || currentIndex > 0));
                                    }

                                    if (next)
                                        nextFrame();
                                    else
                                        stop();
                                }
                            };

        long p = (long)(1000 / frameRate);

        t = (t < p) ? p - t : 0;

        timer.scheduleAtFixedRate(timerTask, t, p);
    } // startTimerTask()

    /**
     * Retrieves the number of frames in the animation.
     * 
     * @return  The number of frames in the animation.
     */
    public int getNumFrames() {
        return frameset.size();
    } // getNumFrames()

    @Override
    public boolean pointIntersects( Point2D point ) {
        AnimationFrame currentFrame = getCurrentFrame();

        if ((currentFrame != null) && super.pointIntersects( point )) {
            int x = (int) (point.getX() * currentFrame.getWidth() / getUnscaledWidth());
            int y = (int) (point.getY() * currentFrame.getHeight() / getUnscaledHeight());

            // Test whether the point contains an opaque (not 100% transparent)
            // pixel in the current frame
            int alpha = (currentFrame.getImage().getRGB(x, y) >>> 24);
            if (alpha != 0)
                return true;
        }
        return false;
    } // pointIntersects( Point2D point )

    /**
     * Draws the current animation frame into the specified graphics context.
     * 
     * <p>To adjust the properties of the image, such as position or size,
     * apply a transformation matrix to the graphics context before calling
     * this method.</p>
     * 
     * @param g The graphics context to draw the frame into.
     * 
     * @throws  NullPointerException if {@code g} is {@code null}.
     */
    @Override
    public synchronized void paint( Graphics2D g ) {
        getCurrentFrame().paint(g);
    } // draw( Graphics2D g )

    /**
     * Starts/re-starts the animation.
     * 
     * <p>If the animation is already running before the call to this method,
     * it is reset back to the first frame.
     * 
     * <p>This method will invoke the {@code spriteAnimationStarted()}
     * method of any associated {@link SpriteAnimationListener}s.
     */
    public synchronized void start() {
        targetFrameIndex = -1;

        startTimerTask();

        // Dispatch started event
        animationListeners.notifyListeners(
            new SpriteAnimationEvent(this),
            (listener, evt) -> { listener.spriteAnimationStarted(evt); }
        );
    } // start()

    /**
     * Stops/pauses the animation.
     * 
     * <p>This method will invoke the {@code spriteAnimationStopped()}
     * method of any associated {@link SpriteAnimationListener}s after the
     * animation is stopped.  If the animation is not running before the call
     * to this method, the listeners are not notified.
     */
    public synchronized void stop() {
        if (!isRunning())
            return;

        timerTask.cancel();
        timerTask = null;
        targetFrameIndex = -1;

        // Dispatch the stopped event
        animationListeners.notifyListeners(
            new SpriteAnimationEvent(this),
            (listener, evt) -> { listener.spriteAnimationStopped(evt); }
        );
    } // stop()

    /**
     * Returns the direction of play.
     * 
     * @return  The direction of play.
     */
    public Direction getPlayDirection() {
        return playDirection;
    } // getPlayDirection()

    /**
     * Sets the direction of play.
     * 
     * @param   value   The new direction of play value.
     */
    public void setPlayDirection( Direction value ) {
        playDirection = value;
    } // setPlayDirection( Direction value )

    /**
     * Plays the animation to the specified frame and then stops.
     * 
     * @param index     The index of the frame to stop at.
     * 
     * @throws  IllegalArgumentException if the {@code index} is outside the
     *          range of valid frame indices.
     */
    public void runToFrameIndex( int index ) {
        if ((index < 0) || (index >= frameset.size())) {
            throw new IllegalArgumentException("Illegal frame index "+ index);
        }

        if (!isRunning() && (currentIndex != index)) {
            start();
        }

        targetFrameIndex = index;
    } // runToFrameIndex( int index )

    /**
     * Plays the animation to the specified frame and then stops.
     * 
     * @param name  The name of the frame to stop at.
     * 
     * @throws  IllegalArgumentException if there is no frame assigned the
     *          specified {@code name}.
     */
    public void runToFrameName( String name ) {
        int idx = frameset.getFrameIndex(name);
        if (idx < 0) {
            throw new IllegalArgumentException("There is no frame named '"+ name +"'");
        }

        runToFrameIndex( idx );
    } // runToFrameName( String name )

    /**
     * Returns the index of the target frame set by a call to
     * {@link #runToFrameIndex(int)} or {@link #runToFrameName(java.lang.String)},
     * or -1 if no target frame is set.
     * 
     * <p>The target frame is cleared once the animation reaches the frame and
     * stops.</p>
     * 
     * @return  The index of the target frame set by a call to
     *          {@code runToFrameIndex(int)} or {@code runToFrameName()}, or -1
     *          if no target frame is set.
     * 
     * @see #runToFrameIndex(int)
     * @see #runToFrameName(java.lang.String)
     */
    public int getTargetFrameIndex() {
        return targetFrameIndex;
    } // getTargetFrameIndex()

    /**
     * Sets the current frame to the specified index.
     * 
     * <p>This method will invoke the {@code spriteAnimationEnteredFrame()}
     * method of any associated {@link SpriteAnimationListener}s after the
     * current frame is changed.  If the frame is not changed (that is, the
     * animation is already at the specified frame), the listeners are not
     * notified.
     * 
     * @param index     The index of the frame to display.
     * 
     * @throws  IllegalArgumentException if the index is outside the range of
     *          defined frames.
     */
    public synchronized void gotoFrameIndex( int index ) {
        if (index == currentIndex) {
            return;
        }

        // The following line is for the side-effect of the range check on the
        // index.
        frameset.getFrame(index);

        currentIndex = index;

        // Dispatch the enter frame event
        animationListeners.notifyListeners(
            new SpriteAnimationEvent(this),
            (listener, evt) -> { listener.spriteAnimationEnteredFrame(evt); }
        );
    } // gotoFrameIndex( int index )

    /**
     * Sets the current frame to the frame with the specified name.
     * 
     * @param name  The name of the frame to display.
     * 
     * @throws  IllegalArgumentException if no frame is assigned the specified
     *          {@code name}.
     */
    public void gotoFrameName( String name ) {
        int idx = frameset.getFrameIndex(name);
        if (idx < 0)
            throw new IllegalArgumentException("There is no frame named '"+ name +"'");

        gotoFrameIndex(idx);
    } // gotoFrameName( String frameName )

    /**
     * Advances to the next frame in the animation.
     * 
     * <p>If the play direction is BACKWARD, the meaning of "next" and
     * "previous" will be reversed in terms of the index of the frame displayed.
     * 
     * <p>If the animation is at the last frame (in the case of FORWARD, or
     * first frame if BACKWARD), and repeat is false, an error will be thrown.
     * 
     * @throws  IllegalStateException if the animation is already at the last
     *          frame (relative to the specified direction).
     */
    public void nextFrame() {
        int idx;
        if (playDirection == Direction.FORWARD) {
            idx = currentIndex + 1;
            if (idx == frameset.size()) {
                if (repeat)
                    idx = 0;
                else
                    throw new IllegalStateException("Already at last frame.");
            }
        } else {
            idx = currentIndex - 1;
            if (idx < 0) {
                if (repeat)
                    idx = frameset.size() - 1;
                else
                    throw new IllegalStateException("Already at last frame.");
            }
        }

        gotoFrameIndex(idx);
    } // nextFrame()

    /**
     * Returns to the previous frame.
     * 
     * <p>If the play direction is BACKWARD, the meaning of "next" and
     * "previous" will be reversed in terms of the index of the frame displayed.
     * 
     * <p>If the animation is at the first frame (in the case of FORWARD, or
     * last frame if BACKWARD), and repeat is false, an error will be thrown.
     * 
     * @throws  IllegalStateException if the animation is already at the last
     *          frame (relative to the specified direction).
     */
    public void previousFrame() {
        int idx;
        if (playDirection == Direction.BACKWARD) {
            idx = currentIndex - 1;
            if (idx < 0) {
                if (repeat)
                    idx = frameset.size() - 1;
                else
                    throw new IllegalStateException("Already at first frame.");
            }
        } else {
            idx = currentIndex + 1;
            if (idx == frameset.size()) {
                if (repeat)
                    idx = 0;
                else
                    throw new IllegalStateException("Already at first frame.");
            }
        }

        gotoFrameIndex(idx);
    } // previousFrame()

    /**
     * Returns the current frame index.
     * 
     * @return  The current frame index.
     */
    public int getCurrentFrameIndex() {
        return currentIndex;
    } // getCurrentFrameIndex()

    /**
     * Returns the name of the current frame, or {@code null} if there is no
     * current frame or the current frame doesn't have a name.
     * 
     * @return  The name of the current frame, or {@code null} if there is no
     *          current frame or the current frame doesn't have a name.
     */
    @SuppressWarnings("unchecked")
    public String getCurrentFrameName() {
        AnimationFrame currentFrame = getCurrentFrame();

        return (currentFrame == null)
                ? null
                // Cast below is safe, since frameset is final and
                // getCurrentFrame() is guaranteed to return a compatible frame.
                : ((AnimationFrameSet<AnimationFrame>)frameset).getFrameName(currentFrame);
    } // getCurrentFrameName()

} // class Animation
