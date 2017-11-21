
package Fishing.drawable.events;

/**
 * Convenience class implementing the {@link DrawableMouseListener} interface
 * with all methods defined as NOPs.
 *
 * @author Brad
 */
public class DrawableMouseAdapter
    implements DrawableMouseListener
{

    @Override
    public void drawableMouseClicked(DrawableMouseEvent e) { }

    @Override
    public void drawableMousePressed(DrawableMouseEvent e) { }

    @Override
    public void drawableMouseReleased(DrawableMouseEvent e) { }

    @Override
    public void drawableMouseEntered(DrawableMouseEvent e) { }

    @Override
    public void drawableMouseExited(DrawableMouseEvent e) { }

    @Override
    public void drawableMouseMoved(DrawableMouseEvent e) { }

    @Override
    public void drawableMouseDragged(DrawableMouseEvent e) { }

} // class DrawableMouseAdapter
