
package Fishing.drawable.events;

/**
 * Interface that must be implemented by objects that listen for focus events.
 * 
 * @author Brad
 */
public interface DrawableFocusListener {

    /**
     * Method invoked for focus gained events.
     * 
     * @param e The event that occurred.
     */
    public void drawableFocusGained( DrawableFocusEvent e );

    /**
     * Method invoked for focus lost events.
     * 
     * @param e The event that occurred.
     */
    public void drawableFocusLost( DrawableFocusEvent e );

} // interface DrawableFocusListener
