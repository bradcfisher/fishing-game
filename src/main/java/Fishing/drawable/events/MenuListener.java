
package Fishing.drawable.events;

/**
 * Interface that must be implemented by objects that listen for menu events.
 * 
 * @author Brad
 */
public interface MenuListener {

    /**
     * Method invoked when the active menu item changes.
     * 
     * @param e The event that occurred.
     */
    public void activeMenuChanged( MenuEvent e );

    /**
     * Method invoked when a menu item is selected.
     * 
     * @param e The event that occurred.
     */
    public void menuSelected( MenuEvent e );

} // interface MenuListener
