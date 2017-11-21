
package Fishing.drawable.events;

/**
 * Method invoked when a scheduled delay or interval registered with a
 * ControlTimer elapses.
 * 
 * @author Brad
 * @see Fishing.drawable.controls.ControlTimer
 */
@FunctionalInterface
public interface TimerListener {

    /**
     * Method invoked when a scheduled delay or interval registered with a
     * ControlTimer elapses.
     * 
     * @param e The event that occurred.
     */
    void timerFired( TimerEvent e );

} // interface TimerListener
