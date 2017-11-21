
package Fishing.drawable;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A collection of animation frames.
 * 
 * @param <T>   AnimationFrame subclass that this frame set contains
 *              instances of.
 * 
 * @author Brad
 */
public class AnimationFrameSet<T extends AnimationFrame> {

    /**
     * The maximum width of the frames in this animation.
     */
    private int frameWidth = 0;

    /**
     * The maximum height of the frames in this animation.
     */
    private int frameHeight = 0;

    /**
     * The list of frames in this animation.
     */
    private final List<T> frames = new ArrayList<>();

    /**
     * Mapping of frame names to the frame assigned the name.
     */
    private Map<String, T> namedFrames;

    /**
     * Retrieves the maximum width of the frames in this animation.
     * 
     * @return  The maximum width of the frames in this animation.
     */
    public synchronized int getFrameWidth() {
        if (frameWidth <= 0) {
            for (T f : frames) {
                int w = f.getWidth();
                if (w > frameWidth)
                    frameWidth = w;
            } // for
        }
        return frameWidth;
    } // getFrameWidth()

    /**
     * Retrieves the maximum height of the frames in this animation.
     * 
     * @return  The maximum height of the frames in this animation.
     */
    public synchronized int getFrameHeight() {
        if (frameHeight <= 0) {
            for (T f : frames) {
                int h = f.getHeight();
                if (h > frameHeight)
                    frameHeight = h;
            } // for
        }
        return frameHeight;
    } // getFrameHeight()

    /**
     * Returns the number of frames in the animation.
     * 
     * @return  The number of frames in the animation.
     */
    public int size() {
        return this.frames.size();
    } // size()

    /**
     * Removes all of the defined frames from the animation.
     */
    protected synchronized void clearFrames() {
        frames.clear();
        namedFrames = null;

        frameWidth = 0;
        frameHeight = 0;
    } // clearFrames()

    /**
     * Adds the specified frame to the end of this frame set.
     * 
     * @param   frame   The new frame to add to this frame set.
     * 
     * @throws  IllegalArgumentException if the {@code frame} is not
     *          associated with this frame set.
     * @throws  NullPointerException if the {@code frame} is {@code null}.
     */
    protected synchronized void addFrame( T frame ) {
        if (frame.getFrameSet() != this) {
            throw new IllegalArgumentException(
                "The specified frame is not associated with this frame set"
            );
        }

        frames.add(frame);

        if (frameWidth < frame.getWidth())
            frameWidth = frame.getWidth();

        if (frameHeight < frame.getHeight())
            frameHeight = frame.getHeight();
    } // addFrame( T frame )

    /**
     * Adds the specified frame to the end of this frame set with the given
     * name.
     * 
     * @param   frame   The new frame to add to this frame set.
     * @param   name    The name to assign to the new frame.
     * 
     * @throws  IllegalArgumentException if the {@code frame} is not
     *          associated with this frame set or if the {@code name} has
     *          already been assigned to a different frame.
     * @throws  NullPointerException if the {@code frame} is {@code null}.
     */
    protected void addFrame(T frame, String name) {
        addFrame(frame);
        if (name != null) {
            setFrameName(frames.size() - 1, name);
        }
    } // addFrame(T frame, String name)

    /**
     * Retrieves a frame of the animation by index.
     * 
     * @param   index   The index of the frame to retrieve.
     * 
     * @return  The frame of the animation at the specified index.
     * 
     * @throws  IllegalArgumentException if the index is outside the range of
     *          defined frames.
     */
    public synchronized T getFrame( int index ) {
        if ((index < 0) || (index >= frames.size()))
            throw new IllegalArgumentException("Illegal frame index "+ index);

        return frames.get(index);
    } // getFrame( int index )

    /**
     * Retrieves a frame of the animation by name.
     * 
     * @param   name    The name of the frame to retrieve.
     * 
     * @return  The frame of the animation with the specified name.
     * 
     * @throws  IllegalArgumentException if no frame is assigned the
     *          specified {@code name}.
     */
    public synchronized T getFrame( String name ) {
        T frame = (namedFrames == null ? null : namedFrames.get(name));
        if (frame == null)
            throw new IllegalArgumentException("There is no frame named '"+ name +"'");

        return frame;
    } // getFrame( String name )

    /**
     * Sets a name for the frame at the specified index.
     * 
     * <p>Only one name can be assigned to a frame, and only one frame can have
     * any given name.
     * 
     * @param index The index of the frame to assign a name to.
     * @param name  The name to assign to the frame.  If (@code null),
     *              removes the name assignment from the frame.
     * 
     * @throws  IllegalArgumentException if the {@code name} has already been
     *          assigned to a different frame.
     */
    public synchronized void setFrameName( int index, String name ) {
        T frame = frames.get(index);

        if (name == null) {
            // Remove name assignment
            if (namedFrames != null) {
                String oldName = frame.getName();
                if (oldName != null) {
                    namedFrames.remove(oldName);
                }
            }
        } else {
            // Make sure the name isn't already assigned to a different frame
            if (namedFrames != null) {
                T f = namedFrames.get(name);
                if (f != null) {
                    if (f == frame)
                        return;  // Same frame/same name

                    throw new IllegalArgumentException(
                        "The name '"+ name +"' is already assigned to a different frame."
                    );
                }

                // Is this a rename?  If so, remove old entry.
                String oldName = frame.getName();
                if (oldName != null) {
                    namedFrames.remove(oldName);
                }
            } else {
                namedFrames = new HashMap<>();
            }

            namedFrames.put(name, frame);
        }
    } // setFrameName( int index, String name )

    /**
     * Sets the name for the specified frame.
     * 
     * @param frame     The frame to assign the name to.
     * @param name      The name to assign to the frame.
     * 
     * @throws  IllegalArgumentException if the {@code name} has already been
     *          assigned to a different frame or {@code frame} is not a member
     *          of this frame set.
     */
    public synchronized void setFrameName( T frame, String name ) {
        int idx = frames.indexOf(frame);
        if (idx == -1)
            throw new IllegalArgumentException(
                "The specified frame is not a member of this frameset"
            );

        setFrameName(idx, name);
    } // setFrameName( T frame, String name )

    /**
     * Returns the name of the frame at the specified index, or null if the frame
     * doesn't have a name.
     * 
     * @param index The index of the frame to retrieve the name for.
     * 
     * @return  The name of the frame at the specified index, or null if the frame
     *          doesn't exist or doesn't have a name.
     * 
     * @throws  IllegalArgumentException if the {@code index} is outside the
     *          range of valid frame indices.
     */
    public synchronized String getFrameName( int index ) {
        if ((index < 0) || (index >= frames.size()))
            throw new IllegalArgumentException("Illegal frame index "+ index);

        T f = frames.get(index);
        if (f != null)
            return getFrameName(f);

        return null;
    } // getFrameName( int index )

    /**
     * Returns the name of the specified frame in this frame set, or
     * {@code null} if the frame is not assigned a name.
     * 
     * @param   frame   The frame to retrieve the name for.
     * 
     * @return  Returns the name of the specified frame in this frame set, or
     *          {@code null} if the frame is not assigned a name.
     * 
     * @throws  IllegalArgumentException if the {@code frame} is not associated
     *          with this frame set.
     */
    public synchronized String getFrameName( T frame ) {
        if (frame.getFrameSet() != this)
            throw new IllegalArgumentException(
                "The specified frame is not a member of this frameset"
            );

        if (namedFrames == null)
            return null;

        for (Map.Entry<String,T> e : namedFrames.entrySet()) {
            if (e.getValue() == frame)
                return e.getKey();
        } // for

        return null;
    } // getFrameName( T frame )

    /**
     * Retrieves a set of all the defined frame names.
     * 
     * @return  A set of all the defined frame names.
     */
    public Set<String> getFrameNames() {
        if (namedFrames == null)
            return Collections.emptySet();

        return namedFrames.keySet();
    } // getFrameNames()

    /**
     * Returns the index of the specified frame within this frame set, or -1
     * if not found.
     * 
     * @param frame The frame to retrieve the index for.
     * 
     * @return  The index of the specified frame within this frame set, or -1
     *          if not found.
     */
    public int getFrameIndex( T frame ) {
        return frames.indexOf(frame);
    } // getFrameIndex( T frame )

    /**
     * Returns the index of the frame with the specified name within this frame
     * set, or -1 if not found.
     * 
     * @param name  The name of the frame to retrieve the index for.
     * 
     * @return  The index of the frame with the specified name within this
     *          frame set, or -1 if not found.
     */
    public synchronized int getFrameIndex( String name ) {
        if (namedFrames == null)
            return -2;

        T frame = namedFrames.get(name);
        if (frame == null)
            return -1;

        return frames.indexOf(frame);
    } // getFrameIndex( T frame )

    /**
     * Draws the image for the specified frame onto the provided context.
     * 
     * <p>To adjust the properties of the image, such as position or size,
     * apply a transformation matrix to the graphics context before calling
     * this method.</p>
     * 
     * @param index     The index of the frame to draw.
     * @param g         The graphics context to draw to.
     * 
     * @throws  NullPointerException if {@code g} is {@code null}.
     */
    public void drawFrame( int index, Graphics2D g ) {
        getFrame(index).paint(g);
    } // drawFrame( int index, Graphics2D g )

    /**
     * Draws the image for the specified frame onto the provided context.
     * 
     * <p>To adjust the properties of the image, such as position or size,
     * apply a transformation matrix to the graphics context before calling
     * this method.</p>
     * 
     * @param name  The name of the frame to draw.
     * @param g     The graphics context to draw to.
     * 
     * @throws  NullPointerException if {@code g} is {@code null}, {@code name}
     *          is {@code null} or no frame is assigned the specified
     *          {@code name}.
     */
    public void drawFrame( String name, Graphics2D g ) {
        getFrame(name).paint(g);
    } // drawFrame( String name, Graphics2D g )

} // AnimationFrameSet
