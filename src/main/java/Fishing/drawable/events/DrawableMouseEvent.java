
package Fishing.drawable.events;

import Fishing.drawable.Drawable;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

/**
 * Event sent to an object when a mouse interaction occurs related to the
 * object or one of its descendants.
 * 
 * <p>DrawableMouseEvents bubble up the display list.
 * 
 * @see MouseEvent
 * @author Brad
 */
public class DrawableMouseEvent
    extends DrawableInputEvent
{

    /**
     * Event ID sent for a mouse pressed event.
     * @see MouseEvent#MOUSE_PRESSED
     */
    public static final int MOUSE_PRESSED = MouseEvent.MOUSE_PRESSED;

    /**
     * Event ID sent for a mouse released event.
     * @see MouseEvent#MOUSE_RELEASED
     */
    public static final int MOUSE_RELEASED = MouseEvent.MOUSE_RELEASED;

    /**
     * Event ID sent for a mouse clicked event.
     * @see MouseEvent#MOUSE_CLICKED
     */
    public static final int MOUSE_CLICKED = MouseEvent.MOUSE_CLICKED;

    /**
     * Event ID sent for a mouse moved event.
     * @see MouseEvent#MOUSE_MOVED
     */
    public static final int MOUSE_MOVED = MouseEvent.MOUSE_MOVED;

    /**
     * Event ID sent for a mouse dragged event.
     * @see MouseEvent#MOUSE_DRAGGED
     */
    public static final int MOUSE_DRAGGED = MouseEvent.MOUSE_DRAGGED;

    /**
     * Event ID sent for a mouse entered event.
     * @see MouseEvent#MOUSE_ENTERED
     */
    public static final int MOUSE_ENTERED = MouseEvent.MOUSE_ENTERED;

    /**
     * Event ID sent for a mouse exited event.
     * @see MouseEvent#MOUSE_EXITED
     */
    public static final int MOUSE_EXITED = MouseEvent.MOUSE_EXITED;

    /**
     * Button ID sent when no button has changed state.
     * @see MouseEvent#NOBUTTON
     */
    public static final int NOBUTTON = MouseEvent.NOBUTTON;

    /**
     * Button ID sent when button #1 has changed state.
     * @see MouseEvent#BUTTON1
     */
    public static final int BUTTON1 = MouseEvent.BUTTON1;

    /**
     * Button ID sent when button #2 has changed state.
     * @see MouseEvent#BUTTON2
     */
    public static final int BUTTON2 = MouseEvent.BUTTON2;

    /**
     * Button ID sent when button #3 has changed state.
     * @see MouseEvent#BUTTON3
     */
    public static final int BUTTON3 = MouseEvent.BUTTON3;

    /**
     * The local horizontal coordinate of the mouse pointer in the source
     * object when the event occurred.
     */
    private double x;

    /**
     * The local vertical coordinate of the mouse pointer in the source object
     * when the event occurred.
     */
    private double y;

    /**
     * The global horizontal coordinate of the mouse pointer in the root stage
     * when the event occurred.
     */
    private int screenX;

    /**
     * The global vertical coordinate of the mouse pointer in the root stage
     * when the event occurred.
     */
    private int screenY;

    /**
     * The number of times the specified button has been clicked.
     */
    private int clickCount;

    /**
     * The button that changed state.
     */
    private int button;

    /**
     * Creates a new event instance.
     * 
     * @param source    The object on which the event initially occurred.
     * @param id        The event ID: One of {@link #MOUSE_PRESSED},
     *                  {@link #MOUSE_RELEASED}, {@link #MOUSE_CLICKED},
     *                  {@link #MOUSE_MOVED}, {@link #MOUSE_DRAGGED},
     *                  {@link #MOUSE_ENTERED} or {@link #MOUSE_EXITED}.
     * @param when      The timestamp when the event occurred.
     * @param modifiers The modifier mask for this event.
     * @param x         The local horizontal coordinate of the mouse pointer in
     *                  the source object when the event occurred.
     * @param y         The local vertical coordinate of the mouse pointer in
     *                  the source object when the event occurred.
     * @param screenX   The global horizontal coordinate of the mouse pointer
     *                  in the root stage when the event occurred.
     * @param screenY   The global vertical coordinate of the mouse pointer
     *                  in the root stage when the event occurred.
     * @param clickCount    The number of times the specified button has been
     *                  clicked.
     * @param button    The button that changed state.  One of:
     *                  {@link #NOBUTTON}, {@link #BUTTON1}, {@link #BUTTON2}
     *                  or {@link #BUTTON3}.
     */
    public DrawableMouseEvent(
                Drawable source, int id, long when, int modifiers,
                double x, double y, int screenX, int screenY,
                int clickCount, int button
            )
    {
        super(source, id, when, modifiers);

        if (button < NOBUTTON || button > BUTTON3)
            throw new IllegalArgumentException("Invalid button value");

        this.x = x;
        this.y = y;
        this.screenX = screenX;
        this.screenY = screenY;
        this.button = button;
        this.clickCount = clickCount;
    } // DrawableMouseEvent()

    /**
     * Retrieves the local horizontal coordinate of the mouse pointer in the
     * source object when the event occurred.
     * 
     * @return  The local horizontal coordinate of the mouse pointer in the
     *          source object when the event occurred.
     */
    public double getX() {
        return x;
    } // getX()

    /**
     * Retrieves the local vertical coordinate of the mouse pointer in the
     * source object when the event occurred.
     * 
     * @return  The local vertical coordinate of the mouse pointer in the
     *          source object when the event occurred.
     */
    public double getY() {
        return y;
    } // getY()

    /**
     * Retrieves the local coordinates of the mouse pointer in the source
     * object when the event occurred.
     * 
     * @return  The local coordinates of the mouse pointer in the source
     *          object when the event occurred.
     */
    public Point2D getPosition() {
      return new Point2D.Double(x, y);
    } // getPosition()

    /**
     * Retrieves the global horizontal coordinate of the mouse pointer in the
     * root stage when the event occurred.
     * 
     * @return  The global horizontal coordinate of the mouse pointer in the
     *          root stage when the event occurred.
     */
    public int getXOnScreen() {
        return screenX;
    } // getXOnScreen()

    /**
     * Retrieves the global vertical coordinate of the mouse pointer in the
     * root stage when the event occurred.
     * 
     * @return  The global vertical coordinate of the mouse pointer in the
     *          root stage when the event occurred.
     */
    public int getYOnScreen() {
        return screenY;
    } // getYOnScreen()

    /**
     * Retrieves the global coordinates of the mouse pointer in the root stage
     * when the event occurred.
     * 
     * @return  The global coordinates of the mouse pointer in the root stage
     *          when the event occurred.
     */
    public Point2D getLocationOnScreen() {
      return new Point2D.Double(screenX, screenY);
    } // getLocationOnScreen()

    /**
     * Retrieves the number of times the specified button has been clicked.
     * 
     * @return  The number of times the specified button has been clicked.
     */
    public int getClickCount() {
        return clickCount;
    } // getClickCount()

    /**
     * Retrieves the button that changed state.
     * 
     * @return  The button that changed state.  One of: {@link #NOBUTTON},
     *          {@link #BUTTON1}, {@link #BUTTON2} or {@link #BUTTON3}.
     */
    public int getButton() {
        return button;
    } // getButton()

} // class DrawableMouseEvent
