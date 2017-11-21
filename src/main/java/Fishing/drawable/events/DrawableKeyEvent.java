
package Fishing.drawable.events;

import Fishing.drawable.Drawable;
import java.awt.event.KeyEvent;

/**
 * Event sent to the currently focused object when a key is pressed.
 * 
 * <p>DrawableKeyEvents bubble up the display list.
 * 
 * @see KeyEvent
 * @author Brad
 */
public class DrawableKeyEvent
    extends DrawableInputEvent
{

    /**
     * Event ID sent for a key pressed event.
     * @see KeyEvent#KEY_PRESSED
     */
    public final int KEY_PRESSED = KeyEvent.KEY_PRESSED;

    /**
     * Event ID sent for a key released event.
     * @see KeyEvent#KEY_RELEASED
     */
    public final int KEY_RELEASED = KeyEvent.KEY_RELEASED;

    /**
     * Event ID sent for a key typed event.
     * @see KeyEvent#KEY_TYPED
     */
    public final int KEY_TYPED = KeyEvent.KEY_TYPED;

    /**
     * The integer keyCode associated with the key in this event
     * (For KEY_TYPED events, the keyCode is VK_UNDEFINED.)
     * @see java.awt.event.KeyEvent
     */
    private final int keyCode;

    /**
     * The character associated with the key in this event.
     * @see java.awt.event.KeyEvent
     */
    private final char keyChar;

    /**
     * The location of the key that was pressed or released.
     * Always KEY_LOCATION_UNKNOWN for KEY_TYPED events.
     */
    private final int keyLocation;

    /**
     * Constructs a new event instance.
     * 
     * @param source    The object on which the event initially occurred.
     * @param id        The event ID: One of {@link #KEY_PRESSED},
     *                  {@link #KEY_RELEASED} or {@link #KEY_TYPED}.
     * @param when      The timestamp when the event occurred.
     * @param modifiers The modifier mask for this event.
     * @param keyCode   The integer keyCode associated with the key in this
     *                  event.
     * @param keyChar   The character associated with the key in this event.
     * @param keyLocation   The location of the key that was pressed or released.
     */
    public DrawableKeyEvent(
                Drawable source, int id, long when, int modifiers,
                int keyCode, char keyChar, int keyLocation
            )
    {
        super(source, id, when, modifiers);
        this.keyCode = keyCode;
        this.keyChar = keyChar;
        this.keyLocation = keyLocation;
    } // DrawableKeyEvent(...)

    /**
     * Retrieves the integer keyCode associated with the key in this event.
     * 
     * @return  The integer keyCode associated with the key in this event
     *          (For KEY_TYPED events, the keyCode is VK_UNDEFINED.)
     * 
     * @see KeyEvent#getKeyCode()
     */
    public int getKeyCode() {
        return keyCode;
    } // getKeyCode()

    /**
     * Retrieves the character associated with the key in this event.
     * 
     * <p>KEY_PRESSED and KEY_RELEASED events are not intended for reporting of
     * character input. Therefore, the values returned by this method are
     * guaranteed to be meaningful only for KEY_TYPED events.
     * 
     * @return  The character associated with the key in this event.
     * 
     * @see KeyEvent#getKeyChar()
     */
    public char getKeyChar() {
        return keyChar;
    } // getKeyChar()

    /**
     * Retrieves the location of the key that originated this key event.
     * 
     * <p>Some keys occur more than once on a keyboard, e.g. the left and right
     * shift keys. Additionally, some keys occur on the numeric keypad.
     * This provides a way of distinguishing such keys.
     * 
     * @return  The location of the key that was pressed or released.
     *          Always returns KEY_LOCATION_UNKNOWN for KEY_TYPED events.
     * 
     * @see KeyEvent#getKeyLocation()
     */
    public int getKeyLocation() {
        return keyLocation;
    } // getKeyLocation()

} // class DrawableKeyEvent
