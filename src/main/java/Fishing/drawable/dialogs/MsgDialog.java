
package Fishing.drawable.dialogs;

import Fishing.drawable.Drawable;
import Fishing.drawable.controls.ButtonMenu;
import Fishing.drawable.events.MenuAdapter;
import Fishing.drawable.events.MenuEvent;
import Fishing.drawable.events.MenuListener;
import Fishing.drawable.text.GeneratedFont;
import Fishing.drawable.text.BitmapFont;
import Fishing.drawable.text.BitmapText;
import java.awt.Font;
import java.util.ArrayList;

/**
 *
 * @author Brad
 */
public class MsgDialog
    extends Dialog
{

    private ButtonMenu buttons;
    private MenuListener buttonsListener;

    private BitmapText title;
    private BitmapText message;


    public MsgDialog( String titleStr, String messageStr, ButtonMenu buttons ) {
        GeneratedFont titleFont = new GeneratedFont( new Font("Arial Bold", Font.PLAIN, 30) );
        GeneratedFont messageFont = new GeneratedFont( new Font("Arial", Font.PLAIN, 24) );

        title = new BitmapText(titleFont);
        addDrawable(title);

        message = new BitmapText(messageFont);
        addDrawable(message);

        setButtons(buttons);
        setTitle(titleStr);
        setMessage(messageStr);
    } // MsgDialog( String title, String message, ButtonMenu buttons )


    public MsgDialog( String titleStr, String messageStr ) {
        this(titleStr, messageStr, null);
    } // MsgDialog( String titleStr, String messageStr )


    protected BitmapText getTitleControl() {
        return title;
    } // getTitleControl()


    protected BitmapText getMessageControl() {
        return message;
    } // getMessageControl()


    public String getTitle() {
        return title.getText();
    } // getTitle()


    public void setTitle( String value ) {
        title.setText( value );
        invalidate();
    } // setTitle( String value )


    public String getMessage() {
        return message.getText();
    } // getMessage()


    public void setMessage( String value ) {
        message.setText( value );
        invalidate();
    } // setMessage( String value )


    public void setTitleFont( BitmapFont font ) {
        if (font == null)
            throw new IllegalArgumentException("The font cannot be null.");

        title.setFont(font);

        invalidate();
    } // setTitleFont( BitmapFont font )


    public BitmapFont getTitleFont() {
        return title.getFont();
    } // getTitleFont()


    public void setMessageFont( BitmapFont font ) {
        if (font == null)
            throw new IllegalArgumentException("The font cannot be null.");

        message.setFont(font);

        invalidate();
    } // setTitleFont( BitmapFont font )


    public BitmapFont getMessageFont() {
        return message.getFont();
    } // getMessageFont()


    public void setButtons( ButtonMenu buttonMenu ) {
        // Remove listener from old buttons
        if (buttons != null) {
            buttons.removeMenuListener( buttonsListener );
            buttonsListener = null;
        }

        if (buttonMenu == null) {
            buttonMenu = new ButtonMenu(message.getFont());
            buttonMenu.addItem("OK", "OK");
        }

        buttons = buttonMenu;
        addDrawable(buttons);

        final MsgDialog self = this;
        buttonsListener = new MenuAdapter() {
                @Override
                public void activeMenuChanged(MenuEvent e) {
                    if (menuListeners != null) {
                        // Notify listeners
                        for (MenuListener listener : menuListeners) {
                            if (listener != this) {
                                listener.activeMenuChanged(e);
                                if (e.isImmediatePropagationStopped())
                                    break;
                            }
                        } // for
                    }
                } // activeMenuChanged(MenuEvent e)

                @Override
                public void menuSelected(MenuEvent e) {
                    // Notify other registered listeners of the event
                    if (menuListeners != null) {
                        MenuEvent evt = new MenuEvent( e.getSource(), e.getMenuIndex(), e.getMenuName() );
                        for (MenuListener listener : menuListeners) {
                            listener.menuSelected(evt);
                            if (evt.isImmediatePropagationStopped())
                                break;
                        } // for

                        // Don't close the dialog if the action was cancelled
                        if (evt.isConsumed())
                            return;
                    }

                    // Close the dialog if it's still open
                    Drawable p = getParent();
                    if (p != null)
                        p.removeDrawable(self);
                } // menuSelected(MenuEvent e)
            };

        buttons.addMenuListener( buttonsListener );
    } // setButtons( ButtonMenu buttons )


    public ButtonMenu getButtons() {
        return buttons;
    } // getButtons()


    @Override
    public void validate() {
        double padding = 20;
        double lineSpacing = title.getFont().getFontSize() * 0.75;

        buttons.setButtonSpacing(lineSpacing);

        // Measure and arrange the components
        double width = Math.max(Math.max(title.getWidth(), message.getWidth()), buttons.getWidth()) + padding * 2;

        double y = padding;
        title.setPosition((width - title.getWidth()) / 2, y);

        y += title.getHeight() + lineSpacing;
        message.setPosition(padding, y);

        y += message.getHeight() + lineSpacing;

        buttons.setPosition((width - buttons.getWidth()) / 2, y);

        y += buttons.getHeight() + padding;

        setUnscaledSize( width, y );

        Drawable p = getParent();
        if (p != null)
            setPosition( (p.getUnscaledWidth() - getWidth()) / 2, (p.getUnscaledHeight() - getHeight()) / 2 );
    } // validate()


    /** Storage for registered menu listeners. */
    private ArrayList<MenuListener> menuListeners;


    public synchronized void addMenuListener( MenuListener listener ) {
        if (listener == null)
            throw new IllegalArgumentException("The listener parameter cannot be null");

        if (menuListeners == null)
             menuListeners = new ArrayList<MenuListener>();
        else if (menuListeners.contains(listener))
            return;
        else
            menuListeners = (ArrayList<MenuListener>) menuListeners.clone();

        menuListeners.add(listener);
    } // addMenuListener( MenuListener listener )


    public synchronized void removeMenuListener( MenuListener listener ) {
        if (menuListeners == null)
            return;

        ArrayList<MenuListener> l = (ArrayList<MenuListener>) menuListeners.clone();
        if (l.remove(listener)) {
            if (l.isEmpty())
                menuListeners = null;
            else
                menuListeners = l;
        }
    } // removeMenuListener( MenuListener listener )


    public static MsgDialog show( Drawable root, String title, String message ) {
        return show(root, title, message, null);
    } // show( Drawable root, String title, String message )


    public static MsgDialog show( Drawable root, String title, String message, ButtonMenu buttons ) {
        MsgDialog dlg = new MsgDialog( title, message, buttons );
        dlg.show(root, true);
        return dlg;
    } // show( Drawable root, String title, String message )

} // class MsgDialog
