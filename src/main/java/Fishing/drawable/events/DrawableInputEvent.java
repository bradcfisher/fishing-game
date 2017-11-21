
package Fishing.drawable.events;

import Fishing.drawable.Drawable;
import java.awt.event.InputEvent;

/**
 * A keyboard or mouse input event.
 * 
 * <p>DrawableInputEvents bubble up the display list.
 * 
 * @see InputEvent
 * @author Brad
 */
public class DrawableInputEvent
    extends DrawableEvent
{

    /**
     * Modifier mask for determining whether the shift modifier is set.
     * @see InputEvent#SHIFT_DOWN_MASK
     */
    public static final int SHIFT_DOWN_MASK = InputEvent.SHIFT_DOWN_MASK;

    /**
     * Modifier mask for determining whether the ctrl modifier is set.
     * @see InputEvent#CTRL_DOWN_MASK
     */
    public static final int CTRL_DOWN_MASK = InputEvent.CTRL_DOWN_MASK;

    /**
     * Modifier mask for determining whether the meta modifier is set.
     * @see InputEvent#META_DOWN_MASK
     */
    public static final int META_DOWN_MASK = InputEvent.META_DOWN_MASK;

    /**
     * Modifier mask for determining whether the alt modifier is set.
     * @see InputEvent#ALT_DOWN_MASK
     */
    public static final int ALT_DOWN_MASK = InputEvent.ALT_DOWN_MASK;

    /**
     * Modifier mask for determining whether the alt-graph modifier is set.
     * @see InputEvent#ALT_GRAPH_DOWN_MASK
     */
    public static final int ALT_GRAPH_DOWN_MASK = InputEvent.ALT_GRAPH_DOWN_MASK;

    /**
     * Modifier mask for determining whether any keyboard modifier is set.
     */
    public static int KEY_MODIFIERS_MASK =
            SHIFT_DOWN_MASK | CTRL_DOWN_MASK | META_DOWN_MASK |
            ALT_DOWN_MASK | ALT_GRAPH_DOWN_MASK;

    /**
     * Modifier mask for determining whether the button 1 modifier is set.
     * @see InputEvent#BUTTON1_DOWN_MASK
     */
    public static final int BUTTON1_DOWN_MASK = InputEvent.BUTTON1_DOWN_MASK;

    /**
     * Modifier mask for determining whether the button 2 modifier is set.
     * @see InputEvent#BUTTON2_DOWN_MASK
     */
    public static final int BUTTON2_DOWN_MASK = InputEvent.BUTTON2_DOWN_MASK;

    /**
     * Modifier mask for determining whether the button 3 modifier is set.
     * @see InputEvent#BUTTON3_DOWN_MASK
     */
    public static final int BUTTON3_DOWN_MASK = InputEvent.BUTTON3_DOWN_MASK;

    /**
     * Modifier mask for determining whether any button modifier is set.
     */
    public static int BUTTON_MODIFIERS_MASK =
            BUTTON1_DOWN_MASK | BUTTON2_DOWN_MASK | BUTTON3_DOWN_MASK;

    /**
     * The event type.
     */
    private final int id;

    /**
     * Timestamp when the event occurred.
     */
    private final long when;

    /**
     * Returns the modifier mask for this event.
     * @see java.awt.event.InputEvent#getModifiersEx() 
     */
    private final int modifiers;

    /**
     * Constructs a new event instance.
     * 
     * @param source    The object on which the event initially occurred.
     * @param id        The event ID: One of {@link KeyEvent#KEY_PRESSED},
     *                  {@link KeyEvent#KEY_RELEASED} or
     *                  {@link KeyEvent#KEY_TYPED}.
     * @param when      The timestamp when the event occurred.
     * @param modifiers The modifier mask for this event.
     */
    public DrawableInputEvent(
        Drawable source, int id, long when, int modifiers
    ) {
        super(source);

        this.id = id;
        this.when = when;
        this.modifiers = modifiers;
    } // DrawableInputEvent(...)

    /**
     * Retrieves the event type.
     * 
     * @return  The event type.
     * 
     * @see KeyEvent#getID()
     */
    public int getID() {
        return id;
    } // getID()

    /**
     * Retrieves the timestamp when the event occurred.
     * 
     * @return  The timestamp when the event occurred.
     * 
     * @see KeyEvent#getWhen()
     */
    public long getWhen() {
        return when;
    } // getWhen()

    /**
     * Retrieves the modifier mask for this event.
     * 
     * @return  The modifier mask for this event.
     * 
     * @see KeyEvent#getModifiersEx()
     */
    public int getModifiers() {
        return modifiers;
    } // getModifiers()

    /**
     * Returns whether or not the Shift modifier is down on this event.
     */
    public boolean isShiftDown() {
        return (modifiers & SHIFT_DOWN_MASK) != 0;
    } // isShiftDown()

    /**
     * Returns whether or not the Control modifier is down on this event.
     */
    public boolean isControlDown() {
        return (modifiers & CTRL_DOWN_MASK) != 0;
    } // isControlDown()

    /**
     * Returns whether or not the Meta modifier is down on this event.
     */
    public boolean isMetaDown() {
        return (modifiers & META_DOWN_MASK) != 0;
    } // isMetaDown()

    /**
     * Returns whether or not the Alt modifier is down on this event.
     */
    public boolean isAltDown() {
        return (modifiers & ALT_DOWN_MASK) != 0;
    } // isAltDown()

    /**
     * Returns whether or not the AltGraph modifier is down on this event.
     */
    public boolean isAltGraphDown() {
        return (modifiers & ALT_GRAPH_DOWN_MASK) != 0;
    } // isAltGraphDown()

    /**
     * Returns whether or not the button 1 is down on this event.
     */
    public boolean isButton1Down() {
        return (modifiers & BUTTON1_DOWN_MASK) != 0;
    } // isButton1Down()

    /**
     * Returns whether or not the button 2 is down on this event.
     */
    public boolean isButton2Down() {
        return (modifiers & BUTTON2_DOWN_MASK) != 0;
    } // isButton2Down()

    /**
     * Returns whether or not the button 3 is down on this event.
     */
    public boolean isButton3Down() {
        return (modifiers & BUTTON3_DOWN_MASK) != 0;
    } // isButton3Down()

} // class DrawableInputEvent
