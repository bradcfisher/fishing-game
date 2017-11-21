
package Fishing.drawable.controls;

import Fishing.drawable.text.BitmapText;
import Fishing.drawable.text.BitmapFont;
import com.jhlabs.image.InvertFilter;
import Fishing.drawable.Drawable;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Brad
 */
public class ListMenu
    extends Menu
{

    private int horizontalPadding = 15;
    private int verticalPadding = 10;
    private int itemSpacing = 0;


    public ListMenu( BitmapFont font ) {
        super(font);
    } // ListMenu( BitmapFont font )

    public synchronized void addItem( String name, String text ) {
        BitmapText item = new BitmapText(getFont());
        item.setName(name);
        item.setText(text);
        item.setMouseEnabled(true);

        addItem(item);
    } // addItem( String name, String text )

    @Override
    public synchronized void setActiveItem( int item ) {
        int oldActiveItem = getActiveItem();

        super.setActiveItem(item);

        Drawable o;
        if (oldActiveItem >= 0) {
            o = getDrawableAt(oldActiveItem);
            o.setFilters(null);
        }

        if (item >= 0) {
            o = getDrawableAt(item);
            o.setFilters(null);
            o.addFilter( new InvertFilter() );
        }
    } // setActiveItem( int item )


    @Override
    public synchronized void validate() {
        // Calculate the width/height from the menu here

        double w = 0;
        double h = 0;

        int n = getNumDrawables();
        int iSpacing = 0;
        for (int i = 0; i < n; ++i) {
            BitmapText mi = (BitmapText) getDrawableAt(i);

            double w2 = mi.getWidth();
            if (w2 > w)
                w = w2;

            h += mi.getHeight() + iSpacing;
            iSpacing = itemSpacing;
        } // for

        w += horizontalPadding;
        h += verticalPadding;

        super.setUnscaledSize( w, h );

        double y = verticalPadding / 2;
        for (int i = 0; i < n; ++i) {
            BitmapText mi = (BitmapText) getDrawableAt(i);

            mi.setPosition( (w - mi.getWidth()) / 2, y );

            y += mi.getHeight() + iSpacing;
            iSpacing = itemSpacing;
        }  // for
    } // validate()


    @Override
    public void paint( Graphics2D g ) {
        double w = getUnscaledWidth();
        double h = getUnscaledHeight();

        // Draw the menu background
        g.setColor( new Color(0xA0606060, true) );

        g.fillRoundRect(0, 0, (int) w, (int) h, 20, 20);

        g.setColor( new Color(0xA0FF0000, true) );
        g.setStroke( new BasicStroke( 4 ) );
        g.drawRoundRect(0, 0, (int) w, (int) h, 20, 20);
    } // paint( Graphics2D g )

} // class ListMenu
