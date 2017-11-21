
package Fishing.drawable.events;

/**
 * Listener for events that affect the display list tree, such as adding or
 * removing children.
 *
 * @author Brad
 */
public interface DrawableTreeListener {

    /**
     * Method invoked on listeners associated with a Drawable when the Drawable
     * is added to a new parent.
     * 
     * @param   e   The DrawableEvent.  The {@code relatedObject} property of
     *              the event is set to the new parent.
     */
    void drawableAdded( DrawableEvent e );

    /**
     * Method invoked on listeners associated with a Drawable when the Drawable
     * is removed from a previous parent.
     * 
     * @param   e   The DrawableEvent.  The {@code relatedObject} property of
     *              the event is set to the old parent.
     */
    void drawableRemoved( DrawableEvent e );

    /**
     * Method invoked on listeners associated with a parent Drawable when a
     * new child Drawable is added to it's list of children.
     * 
     * @param   e   The DrawableEvent.  The {@code relatedObject} property of
     *              the event is set to the new child object.
     */
    void drawableChildAdded( DrawableEvent e );

    /**
     * Method invoked on listeners associated with a parent Drawable when a
     * child Drawable is removed from it's list of children.
     * 
     * @param   e   The DrawableEvent.  The {@code relatedObject} property of
     *              the event is set to the removed child object.
     */
    void drawableChildRemoved( DrawableEvent e );

    /**
     * Method invoked on listeners associated with a Drawable when the Drawable
     * is added to a Stage.
     * 
     * <p>When this method is invoked, the {@code stage} property of the
     * Drawable has already been updated to the new value.
     * 
     * @param   e   The DrawableEvent.  The {@code relatedObject} property of
     *              the event is set to the new stage.
     */
    void drawableAddedToStage( DrawableEvent e );

    /**
     * Method invoked on listeners associated with a Drawable when the Drawable
     * is removed from a Stage.
     * 
     * <p>When this method is invoked, the {@code stage} property of the
     * Drawable has already been updated to a new value, and will no longer
     * refer to the prior stage.
     * 
     * @param   e   The DrawableEvent.  The {@code relatedObject} property of
     *              the event is set to the prior stage.
     */
    void drawableRemovedFromStage( DrawableEvent e );

    /**
     * Method invoked on listeners associated with a Drawable when the Drawable
     * is added to a new root Stage.
     * 
     * <p>When this method is invoked, the {@code root} property of the
     * Drawable has already been updated to the new value.
     * 
     * @param   e   The DrawableEvent.  The {@code relatedObject} property of
     *              the event is set to the new root stage.
     */
    void drawableAddedToRootStage( DrawableEvent e );

    /**
     * Method invoked on listeners associated with a Drawable when the Drawable
     * is removed from a root Stage.
     * 
     * <p>When this method is invoked, the {@code root} property of the
     * Drawable has already been updated to a new value, and will no longer
     * refer to the prior root stage.
     * 
     * @param   e   The DrawableEvent.  The {@code relatedObject} property of
     *              the event is set to the prior root stage.
     */
    void drawableRemovedFromRootStage( DrawableEvent e );

} // interface DrawableTreeListener
