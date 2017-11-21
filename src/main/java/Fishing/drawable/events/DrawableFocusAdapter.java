
package Fishing.drawable.events;

/**
 * Convenience class implementing the {@link DrawableFocusListener} interface
 * with all methods defined as NOPs.
 *
 * @author Brad
 */
public class DrawableFocusAdapter
    implements DrawableFocusListener
{

    @Override
    public void drawableFocusGained( DrawableFocusEvent e ) { }

    @Override
    public void drawableFocusLost( DrawableFocusEvent e ) { }
    
} // class DrawableFocusAdapter
