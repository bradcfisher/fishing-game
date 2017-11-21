
package Fishing.drawable.controls;

import Fishing.drawable.events.DrawableMouseEvent;
import Fishing.drawable.events.MenuListener;
import Fishing.drawable.events.MenuEvent;
import Fishing.drawable.text.BitmapFont;
import Fishing.drawable.Drawable;
import Fishing.drawable.events.DrawableKeyAdapter;
import Fishing.drawable.events.DrawableKeyEvent;
import Fishing.drawable.events.DrawableMouseAdapter;
import Fishing.drawable.events.EventBase;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 *
 * @author Brad
 */
public abstract class Menu
    extends Drawable
{

    private BitmapFont font;
    private int activeItem = -1;

    /**
     * The registered menu listeners.
     */
    private List<MenuListener> menuListeners;


    public Menu( BitmapFont font ) {
        if (font == null)
            throw new IllegalArgumentException("The font parameter cannot be null.");

        this.font = font;

        setMouseChildren(true);
        setFocusable(true);

        getKeyListeners().addDefault(
            new DrawableKeyAdapter() {
                @Override
                public void drawableKeyPressed(DrawableKeyEvent e) {
                    int n = getNumDrawables();

                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                        case KeyEvent.VK_LEFT:
                            e.consume();
                            if (activeItem <= 0)
                                setActiveItem( n - 1 );
                            else
                                setActiveItem( activeItem - 1 );
                            break;

                        case KeyEvent.VK_DOWN:
                        case KeyEvent.VK_RIGHT:
                            e.consume();
                            if (activeItem >= n - 1)
                                setActiveItem( 0 );
                            else
                                setActiveItem( activeItem + 1 );
                            break;

                        case KeyEvent.VK_ENTER:
                        case KeyEvent.VK_SPACE:
                            if (activeItem != -1) {
                                e.consume();
                                executeActiveItem();
                            }
                    } // switch
                } // drawableKeyPressed(DrawingKeyEvent e)
            }
        );

        getMouseListeners().addDefault(
            new DrawableMouseAdapter() {
                @Override
                public void drawableMousePressed(DrawableMouseEvent e) {
                    setFocus();
                } // drawableMousePressed(DrawableMouseEvent e)
            }
        );
    } // Menu( BitmapFont font )


    public BitmapFont getFont() {
        return font;
    } // getFont()

    public void setFont( BitmapFont value ) {
        if (font == null)
            throw new IllegalArgumentException("The font cannot be null.");

        this.font = value;
    } // setFont( BitmapFont value )


    public void addMenuListener( MenuListener listener ) {
        menuListeners = EventBase.addListener(menuListeners, listener);
    } // addMenuListener( MenuListener listener )


    public void removeMenuListener( MenuListener listener ) {
        menuListeners = EventBase.removeListener(menuListeners, listener);
    } // removeMenuListener( MenuListener listener )


    protected void addItem(final Drawable item) {
        item.addMouseListener(
            new DrawableMouseAdapter() {
                @Override
                public void drawableMouseClicked(DrawableMouseEvent e) {
                    setActiveItem(getDrawableIndex(item));
                    executeActiveItem();
                } // drawableMouseClicked(DrawableMouseEvent e)

                @Override
                public void drawableMouseEntered(DrawableMouseEvent e) {
                    setActiveItem(getDrawableIndex(item));
                } // drawableMouseEntered(DrawableMouseEvent e)

                @Override
                public void drawableMouseExited(DrawableMouseEvent e) {
                    setActiveItem(-1);
                } // drawableMouseExited(DrawableMouseEvent e)
            }
        );

        super.addDrawable(item);

        invalidate();
    } // addItem(Drawable item)


    public int getActiveItem() {
        return activeItem;
    } // getActiveItem()


    public synchronized String getActiveItemName() {
        if (activeItem == -1)
            return null;

        return getDrawableAt(activeItem).getName();
    } // getActiveItemName()


    public void setActiveItem( int item ) {
        String itemName;

        synchronized (this) {
            if (activeItem == item)
                return;

            if ((item < -1) || (item >= getNumDrawables()))
                throw new IllegalArgumentException("Invalid item");

            activeItem = item;
            itemName = getActiveItemName();
        }

        EventBase.notifyListeners(
            menuListeners,
            new MenuEvent(this, item, itemName),
            MenuListener::activeMenuChanged
        );
    } // setActiveItem( int item )


    public synchronized void setActiveItem( String name ) {
        int idx = getDrawableIndex(name);
        if (idx == -1)
            throw new IllegalArgumentException("There is no item named "+ name);

        setActiveItem(idx);
    } // setActiveItem( String name )


    public void executeActiveItem() {
        int item;
        String name;
        synchronized (this) {
            item = activeItem;
            name = getActiveItemName();
        }

        if (item == -1)
            return;

        EventBase.notifyListeners(
            menuListeners,
            new MenuEvent(this, item, name),
            MenuListener::menuSelected
        );
    } // executeActiveItem()

    /**
     * Throws an IllegalAccessError if invoked, since Menu objects do not
     * support arbitrary child objects.
     * 
     * @param   obj The child object to add.
     * 
     * @throws  IllegalAccessError if invoked, since Menu objects do not
     *          support arbitrary child objects.
     */
    @Override
    public void addDrawable( Drawable obj ) {
        throw new IllegalAccessError("Cannot add children to a Menu object");
    } // addDrawable( Drawable obj )

    @Override
    public abstract void validate();


    public abstract void addItem( String name, String text );


} // class Menu
