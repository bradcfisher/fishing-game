
package Fishing.drawable.events;

/**
 * Convenience class implementing the {@link DrawableListener} interface
 * with all methods defined as NOPs.
 *
 * @author Brad
 */
public class DrawableAdapter
    implements DrawableListener
{

    @Override
    public void drawableHidden( DrawableEvent e ) { }

    @Override
    public void drawableShown( DrawableEvent e ) { }

    @Override
    public void drawableMoved( DrawableEvent e ) { }

    @Override
    public void drawableResized( DrawableEvent e ) { }

    @Override
    public void drawableScaled( DrawableEvent e ) { }

    @Override
    public void drawableRotated( DrawableEvent e ) { }

} // class DrawableAdapter
