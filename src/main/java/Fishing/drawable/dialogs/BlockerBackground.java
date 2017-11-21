
package Fishing.drawable.dialogs;

import Fishing.drawable.Drawable;
import Fishing.drawable.events.DrawableTreeAdapter;
import Fishing.drawable.events.DrawableEvent;
import Fishing.drawable.events.DrawableMouseAdapter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;


/**
 *
 * @author Brad
 */
public class BlockerBackground
    extends Drawable
{

    private Color backgroundColor;

    public BlockerBackground( Color backgroundColor ) {
        setUnscaledSize( 100, 100 );
        setMouseEnabled(true);
        setBackgroundColor(backgroundColor);

        addTreeListener(new DrawableTreeAdapter() {
                    @Override
                    public void drawableAdded( DrawableEvent e ) {
                        Drawable p = getParent();
                        if (p != null)
                            setUnscaledSize( p.getUnscaledWidth(), p.getUnscaledHeight() );
                    } // drawableAdded( DrawableEvent e )
                });
    } // BlockerBackground( Color backgroundColor )


    public Color getBackgroundColor() {
        return backgroundColor;
    } // getBackgroundColor()


    public void setBackgroundColor( Color value ) {
        if (value == null)
            throw new IllegalArgumentException("The backgroundColor cannot be null.");

        backgroundColor = value;
    } // setBackgroundColor( Color value )


    @Override
    public void paint( Graphics2D g ) {
        g.setColor( backgroundColor);
        g.fill(new Rectangle2D.Double(0, 0, getUnscaledWidth(), getUnscaledHeight()));
    } // paint( Graphics2D g )

} // class BlockerBackground
