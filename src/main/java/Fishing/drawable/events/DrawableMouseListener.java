
package Fishing.drawable.events;

/**
 * Interface that must be implemented by objects that listen for mouse events.
 * 
 * @author Brad
 */
public interface DrawableMouseListener {

    /**
     * Method invoked for mouse button clicked events.
     * 
     * @param e The event that occurred.
     */
    public void drawableMouseClicked(DrawableMouseEvent e);

    /**
     * Method invoked for mouse button pressed events.
     * 
     * @param e The event that occurred.
     */
    public void drawableMousePressed(DrawableMouseEvent e);

    /**
     * Method invoked for mouse button released events.
     * 
     * @param e The event that occurred.
     */
    public void drawableMouseReleased(DrawableMouseEvent e);

    /**
     * Method invoked for mouse entered events.
     * 
     * @param e The event that occurred.
     */
    public void drawableMouseEntered(DrawableMouseEvent e);

    /**
     * Method invoked for mouse exited events.
     * 
     * @param e The event that occurred.
     */
    public void drawableMouseExited(DrawableMouseEvent e);

    /**
     * Method invoked for mouse moved events.
     * 
     * @param e The event that occurred.
     */
    public void drawableMouseMoved(DrawableMouseEvent e);

    /**
     * Method invoked for mouse dragged events.
     * 
     * @param e The event that occurred.
     */
    public void drawableMouseDragged(DrawableMouseEvent e);

} // interface DrawableMouseListener
