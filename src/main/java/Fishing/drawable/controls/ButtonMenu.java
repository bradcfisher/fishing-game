
package Fishing.drawable.controls;

import Fishing.drawable.Drawable;
import Fishing.drawable.text.BitmapFont;

/**
 * A Menu which consists of buttons arranged horizontally.
 * 
 * @author Brad
 */
public class ButtonMenu
    extends Menu
{

    /**
     * The amount of spacing to apply between each button.
     */
    private double buttonSpacing = 20;

    /**
     * Constructs a new instance.
     * 
     * @param font  The font to use for the button captions.
     */
    public ButtonMenu( BitmapFont font ) {
        super(font);
        setFocusable(false);
    } // ButtonMenu( BitmapFont font )

    /**
     * Retrieves the amount of spacing to apply between each button.
     * 
     * @return  The amount of spacing applied between each button.
     */
    public double getButtonSpacing() {
        return buttonSpacing;
    } // getButtonSpacing()

    /**
     * Sets the amount of spacing to apply between each button.
     * 
     * @param value The new amount of spacing to apply between each button.
     * 
     * @throws  IllegalArgumentException if {@code buttonSpacing} is less
     *          than 0.
     */
    public void setButtonSpacing( double value ) {
        if (buttonSpacing < 0)
            throw new IllegalArgumentException(
                "The buttonSpacing cannot be less than 0."
            );

        buttonSpacing = value;
    } // setButtonSpacing( double value )

    /**
     * Adds a new button to the menu with the specified name and caption text.
     * 
     * @param name  The name to assign to the new button.
     * @param text  The caption text for the new button.
     */
    public synchronized void addItem( String name, String text ) {
        Button item = new Button(getFont());
        item.setName(name);
        item.setCaption(text);

        addItem(item);
    } // addItem( String name, String text )

    @Override
    public synchronized void validate() {
        // Set all of the buttons to the same size and arrange them horizontally
        double maxWidth = 0;
        double maxHeight = 0;

        int n = getNumDrawables();
        for (int i = 0; i < n; ++i) {
            Drawable c = getDrawableAt(i);
            maxWidth = Math.max(maxWidth, c.getWidth());
            maxHeight = Math.max(maxHeight, c.getHeight());
        } // for

        double xPos = 0;
        for (int i = 0; i < n; ++i) {
            Drawable c = getDrawableAt(i);
            c.setUnscaledSize(maxWidth, maxHeight);
            c.setScale(1,1);
            c.setPosition(xPos, 0);
            //c.setVisible(true);
            xPos += maxWidth + buttonSpacing;
        } // for

        setUnscaledSize( xPos - buttonSpacing, maxHeight );
    } // validate()

} // class ButtonMenu
