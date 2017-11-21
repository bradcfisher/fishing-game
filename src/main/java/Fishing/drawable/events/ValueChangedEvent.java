
package Fishing.drawable.events;

/**
 * Event dispatched when a property value associated with an object has changed.
 * 
 * @author Brad
 */
public class ValueChangedEvent
    extends EventBase
{

    /**
     * The old value of the property.
     */
    private final Object oldValue;

    /**
     * The new value of the property.
     */
    private final Object newValue;

    /**
     * Constructs a new instance.
     * 
     * @param   source      The object on which the Event initially occurred.
     * @param   oldValue    The old value of the property.
     * @param   newValue    The new value of the property.
     */
    public ValueChangedEvent( Object source, Object oldValue, Object newValue ) {
        super(source);
        this.oldValue = oldValue;
        this.newValue = newValue;
    } // ValueChangedEvent( Object source, Object oldValue, Object newValue )

    /**
     * Retrieves the new value of the property.
     * @return  The new value of the property.
     */
    public Object getNewValue() {
        return newValue;
    } // getNewValue()

    /**
     * Retrieves the old value of the property.
     * @return  The old value of the property.
     */
    public Object getOldValue() {
        return oldValue;
    } // getOldValue()

    @Override
    protected String getToStringProperties() {
        return super.getToStringProperties() + "  oldValue="+ getOldValue() +"  newValue="+ getNewValue();
    } // getToStringProperties()

} // class ValueChangedEvent
