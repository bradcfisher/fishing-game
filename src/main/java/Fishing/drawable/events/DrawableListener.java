
package Fishing.drawable.events;

/**
 * Interface that must be implemented by objects that listen for Drawable
 * status events.
 * 
 * @author Brad
 */
public interface DrawableListener {

    /**
     * Method invoked when a Drawable's visible status changes to {@code false}.
     * 
     * @param e The event that occurred.
     */
    void drawableHidden( DrawableEvent e );

    /**
     * Method invoked when a Drawable's visible status changes to {@code true}.
     * 
     * @param e The event that occurred.
     */
    void drawableShown( DrawableEvent e );

    /**
     * Method invoked when a Drawable's position is changed.
     * 
     * @param e The event that occurred.
     */
    void drawableMoved( DrawableEvent e );

    /**
     * Method invoked when a Drawable's size is changed.
     * 
     * @param e The event that occurred.
     */
    void drawableResized( DrawableEvent e );

    /**
     * Method invoked when a Drawable's scale is changed.
     * 
     * @param e The event that occurred.
     */
    void drawableScaled( DrawableEvent e );

    /**
     * Method invoked when a Drawable's rotation is changed.
     * 
     * @param e The event that occurred.
     */
    void drawableRotated( DrawableEvent e );

} // interface DrawableListener
