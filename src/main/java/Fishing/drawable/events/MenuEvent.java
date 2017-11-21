
package Fishing.drawable.events;

import Fishing.drawable.Drawable;
import Fishing.drawable.controls.Menu;

/**
 * Event sent when the user interacts with a Menu control.
 * 
 * @see Menu
 * @author Brad
 */
public class MenuEvent
    extends DrawableEvent
{

    /**
     * The index of the menu item that the event relates to.
     */
    private final int menuIndex;

    /**
     * The name of the menu item that the event relates to.
     */
    private final String menuName;

    /**
     * Constructs a new event instance.
     * 
     * @param source    The object on which the event initially occurred.
     * @param menuIndex The index of the menu item that the event
     *                  relates to.
     * @param menuName  The name of the menu item that the event relates to.
     */
    public MenuEvent( Drawable source, int menuIndex, String menuName ) {
        super(source);
        this.menuIndex = menuIndex;
        this.menuName = menuName;
    } // MenuEvent( Drawable source, int menuIndex, String menuName )

    /**
     * Retrieves the index of the menu item that the event relates to.
     * 
     * @return  The index of the menu item that the event relates to.
     */
    public int getMenuIndex() {
        return menuIndex;
    } // getMenuIndex()

    /**
     * Retrieves the name of the menu item that the event relates to.
     * 
     * @return  The name of the menu item that the event relates to.
     */
    public String getMenuName() {
        return menuName;
    } // getMenuName()

    @Override
    protected String getToStringProperties() {
        return super.getToStringProperties() + "  menuIndex="+ getMenuIndex() +"  menuName="+ getMenuName();
    } // getToStringProperties()

} // class MenuEvent
