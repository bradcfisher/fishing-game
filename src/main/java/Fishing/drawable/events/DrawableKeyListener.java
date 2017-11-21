
package Fishing.drawable.events;

/**
 * Interface that must be implemented by objects that listen for focus events.
 * 
 * @author Brad
 */
public interface DrawableKeyListener {

    /**
     * Method invoked for key pressed events.
     * 
     * @param e The event that occurred.
     */
    public void drawableKeyPressed(DrawableKeyEvent e);

    /**
     * Method invoked for key released events.
     * 
     * @param e The event that occurred.
     */
    public void drawableKeyReleased(DrawableKeyEvent e);

    /**
     * Method invoked for key typed events.
     * 
     * @param e The event that occurred.
     */
    public void drawableKeyTyped(DrawableKeyEvent e);

} // class DrawableKeyListener
