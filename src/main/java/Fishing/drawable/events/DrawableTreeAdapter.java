
package Fishing.drawable.events;

/**
 * Convenience class implementing the {@link DrawableTreeListener} interface
 * with all methods defined as NOPs.
 *
 * @author Brad
 */
public class DrawableTreeAdapter
    implements DrawableTreeListener
{

    @Override
    public void drawableAdded( DrawableEvent e ) { }

    @Override
    public void drawableRemoved( DrawableEvent e ) { }

    @Override
    public void drawableChildAdded( DrawableEvent e ) { }

    @Override
    public void drawableChildRemoved( DrawableEvent e ) { }

    @Override
    public void drawableAddedToStage( DrawableEvent e ) { }

    @Override
    public void drawableRemovedFromStage( DrawableEvent e ) { }

    @Override
    public void drawableAddedToRootStage( DrawableEvent e ) { }

    @Override
    public void drawableRemovedFromRootStage( DrawableEvent e ) { }

} // class DrawableTreeAdapter
