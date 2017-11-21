
package Fishing.drawable.events;

import java.util.EventObject;

/**
 * Event sent to a TimerListener when a timer fires.
 * 
 * @author Brad
 */
public class TimerEvent
    extends EventObject
{

    /**
     * 
     * @param source 
     */
    public TimerEvent( Integer source ) {
        super( source );
    } // TimerEvent( Integer source )

    @Override
    public Integer getSource() {
        return (Integer) super.getSource();
    } // getSource()

} // class TimerEvent
