
package Fishing.drawable.events;

import Fishing.drawable.Drawable;

/**
 * Base class for events dispatched to a Drawable instance.
 * @author Brad
 */
public class DrawableEvent
    extends EventBase
{

    /**
     * If the event relates to another object, this will be set to that object.
     */
    private Drawable relatedObject;

    /**
     * Constructs a new instance with the specified source.
     * 
     * @param   source  The object on which the Event initially occurred.
     */
    public DrawableEvent( Drawable source ) {
        super(source);
    } // DrawableEvent( Drawable source )

    /**
     * Constructs a new instance with the specified source and related object.
     * 
     * @param   source  The object on which the Event initially occurred.
     * @param   relatedObject Another object related to this event being
     *                  dispatched.  For example, this may be set to a previous
     *                  parent object if this event indicates the source was
     *                  removed as a child.
     */
    public DrawableEvent( Drawable source, Drawable relatedObject ) {
        super(source);
        this.relatedObject = relatedObject;
    } // DrawableEvent( Drawable source, Drawable relatedObject )

    @Override
    public Drawable getSource() {
        return (Drawable) super.getSource();
    } // getSource()

    /**
     * Retrieves the value of the {@code relatedObject} associated with this
     * event.
     * 
     * @return  The value of the {@code relatedObject} associated with this
     *          event, or {@code null} if there is no {@code relatedObject}.
     */
    public Drawable getRelatedObject() {
        return relatedObject;
    } // getRelatedObject()

    @Override
    protected String getToStringProperties() {
        return super.getToStringProperties() + "  relatedObject="+ getRelatedObject();
    } // getToStringProperties()

} // class DrawableEvent
