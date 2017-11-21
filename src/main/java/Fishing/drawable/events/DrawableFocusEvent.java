
package Fishing.drawable.events;

import Fishing.drawable.Drawable;
import java.awt.event.FocusEvent;

/**
 * Event type dispatched when a focus change occurs.
 * 
 * <p>DrawableFocusEvents bubble up the display list.
 * 
 * @see FocusEvent
 * @author Brad
 */
public class DrawableFocusEvent
    extends DrawableEvent
{

    /**
     * Event ID sent when a control gains the focus.
     */
    public final static int FOCUS_GAINED    = FocusEvent.FOCUS_GAINED;

    /**
     * Event ID sent when a control loses the focus.
     */
    public final static int FOCUS_LOST      = FocusEvent.FOCUS_LOST;

    /**
     * The event ID: One of {@link #FOCUS_GAINED} or {@link #FOCUS_LOST}.
     */
    private final int id;

    /**
     * Whether this is a temporary loss of focus or not.
     * 
     * <p>If this property is {@code true}, then the focus may be returned to
     * the target control at a later time.
     */
    private final boolean temporary;

    /**
     * Creates a new instance.
     * 
     * @param source    The object on which the event initially occurred.
     * @param id        The event ID: One of {@link #FOCUS_GAINED} or
     *                  {@link #FOCUS_LOST}.
     * @param temporary Whether the focus change event is temporary or
     *                  permanent.
     * @param opposite  The other component involved in the focus change, or
     *                  {@code null} if this focus change occurs with a native
     *                  application, with a Java application in a different VM
     *                  or context, or with no other component.
     */
    public DrawableFocusEvent( Drawable source, int id, boolean temporary, Drawable opposite ) {
        super(source, opposite);
	this.id = id;
	this.temporary = temporary;
    } // DrawableFocusEvent( Drawable source, int id, boolean temporary, Drawable opposite )

    /**
     * Retrieves the event ID: One of {@link #FOCUS_GAINED} or
     * {@link #FOCUS_LOST}.
     * 
     * @return  The event ID: One of {@link #FOCUS_GAINED} or
     *          {@link #FOCUS_LOST}.
     */
    public int getID() {
        return id;
    } // getID()

    /**
     * Retrieves whether the focus change event is temporary or permanent.
     * 
     * @return  {@code true} if the focus change event is temporary,
     *          {@code false} if it is permanent.
     */
    public boolean isTemporary() {
        return temporary;
    } // isTemporary()

    @Override
    protected String getToStringProperties() {
        return super.getToStringProperties() + "  id="+ id +"  temporary="+ temporary;
    } // getToStringProperties()

} // class DrawableFocusEvent
