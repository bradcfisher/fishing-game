
package Fishing.drawable.events;

/**
 * Convenience class implementing the {@link DrawableKeyListener} interface
 * with all methods defined as NOPs.
 *
 * @author Brad
 */
public class DrawableKeyAdapter
    implements DrawableKeyListener
{

    @Override
    public void drawableKeyPressed(DrawableKeyEvent e) { }

    @Override
    public void drawableKeyReleased(DrawableKeyEvent e) { }

    @Override
    public void drawableKeyTyped(DrawableKeyEvent e) { }

} // class DrawableKeyAdapter