
package Fishing.drawable.controls;

import Fishing.drawable.BehaviorUtil;
import Fishing.GraphicsUtil;
import Fishing.drawable.Drawable;
import Fishing.drawable.FocusManager;
import Fishing.drawable.events.DrawableListeners;
import Fishing.drawable.events.DrawableMouseAdapter;
import Fishing.drawable.events.DrawableMouseEvent;
import Fishing.drawable.events.ValueChangedEvent;
import Fishing.drawable.events.ValueChangedListener;
import Fishing.drawable.text.BitmapFont;
import Fishing.drawable.text.BitmapText;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;

/**
 * A focusable Checkbox control with adjustable size, caption text, background
 * boxColor and border width.
 *
 * @author Brad
 */
public class Checkbox
    extends Drawable
{

    /**
     * Whether the checkbox is currently selected.
     */
    private boolean selected = false;

    /**
     * The text label child created in the constructor.
     */
    private BitmapText label;

    /**
     * The size to use for rendering the box part of the checkbox, in local
     * units.
     */
    private double boxSize;

    /**
     * Additional padding to apply around the outside edges of the checkbox,
     * in local units.
     */
    private double padding;

    /**
     * Spacing to apply between the box and the label, in local units.
     */
    private double labelGap;

    /**
     * The color to use when drawing the box portion.
     */
    private Color boxColor = Color.BLACK;

    /**
     * The registered ValueChangedListeners.
     */
    private DrawableListeners<ValueChangedListener> valueChangedListeners =
            new DrawableListeners<>();

    /**
     * Constructs a new checkbox using the specified font.
     * 
     * @param   font    The font to use when rendering the text label.
     */
    public Checkbox( BitmapFont font ) {
        boxSize = font.getFontSize() * 0.8;
        padding = font.getFontSize() / 8;
        labelGap = font.getFontSize() / 4;
        double w = boxSize + padding * 2;

        label =  new BitmapText(font);
        label.setPosition( boxSize + padding + labelGap, padding );
        addDrawable(label);

        BehaviorUtil.addMouseHoverBehavior(this);

        getMouseListeners().addDefault(
            new DrawableMouseAdapter() {
                @Override
                public void drawableMouseClicked(DrawableMouseEvent e) {
                    setSelected( !isSelected() );
                    e.consume();
                } // drawableMouseClicked(DrawableMouseEvent e)
            }
        );

        BehaviorUtil.addFocusableBehavior(this);

        BehaviorUtil.addButtonKeyboardBehavior(this, (obj) -> {
            obj.setSelected( !obj.isSelected() );
        });

        setMouseEnabled(true);
        setFocusable(true);
        setTabIndex(0);

        setUnscaledSize( w, w );
    } // Checkbox( BitmapFont font )

    /**
     * Constructs a new checkbox using the specified font and label text.
     * 
     * @param   font    The font to use when rendering the text label.
     * @param   label   The text to display for the text label.
     */
    public Checkbox( BitmapFont font, String label ) {
        this(font);
        setLabel(label);
    } // Checkbox( BitmapFont font, String label )

    /**
     * Retrieves the color used when drawing the box portion.
     * @return  The color used when drawing the box portion.
     */
    public Color getBoxColor() {
        return boxColor;
    } // getBoxColor()

    /**
     * Sets the color used when drawing the box portion.
     * 
     * @param   value   The new color to use for drawing the box portion.
     * 
     * @throws  NullPointerException if {@code value} is {@code null}.
     */
    public void setBoxColor( Color value ) {
        boxColor = Objects.requireNonNull(value, "The boxColor cannot be null.");
    } // setBoxColor( Color value )

    /**
     * The text displayed by the text label associated with this checkbox.
     * 
     * @return  The text displayed by the text label associated with this
     *          checkbox.
     */
    public String getLabel() {
        return label.getText();
    }  // getLabel()

    /**
     * Sets the text to display in the text label associated with this checkbox.
     * 
     * @param   value   The new text to display in the text label associated
     *                  with this checkbox.
     */
    public void setLabel( String value ) {
        label.setText(value);

        double w = boxSize + padding * 2;
        if (value != null)
            w += labelGap + label.getWidth();

        setUnscaledSize( w, Math.max(boxSize, label.getFont().getFontSize()) + padding * 2 );
    } // setLabel( String value )

    /**
     * Retrieves the font used when rendering the text label.
     * 
     * @return  The font used when rendering the text label.
     */
    public BitmapFont getFont() {
        return label.getFont();
    } // getFont()

    /**
     * Sets the font to use when rendering the text label.
     * 
     * @param   value   The new font to use when rendering the text label.
     * 
     * @throws  NullPointerException if {@code value} is {@code null}.
     */
    public void setFont(BitmapFont value) {
        label.setFont(value);
    } // setFont(BitmapFont value)

    /**
     * Retrieves whether the checkbox is currently selected or not.
     * 
     * @return  {@code true} if the checkbox is currently selected,
     *          {@code false} otherwise.
     */
    public boolean isSelected() {
        return selected;
    } // isSelected()

    /**
     * Sets the selected status of the checkbox.
     * 
     * <p>If the selected state is changed by this method, it will invoke the
     * {@code valueChanged()} method of any associated
     * {@code ValueChangedListeners}.
     * 
     * @param  value    {@code true} if the checkbox should be set as selected,
     *                  {@code false} otherwise.
     */
    public synchronized void setSelected( boolean value ) {
        if (selected == value)
            return;

        boolean oldValue = selected;
        selected = value;

        valueChangedListeners.notifyListeners(
            new ValueChangedEvent(this, oldValue, value),
            (listener, evt) -> { listener.valueChanged(evt); }
        );
    } // setSelected( value boolean )

    /**
     * Draws the checkbox into the specified graphics context.
     * 
     * @param   g   The graphics context to draw the checkbox into.
     */
    @Override
    public void paint( Graphics2D g ) {
        double h = getUnscaledHeight();

        BitmapFont font = label.getFont();
        double cornerRadius = font.getFontSize() / 6;
        double boxLeft = padding;
        double boxTop = (h - boxSize - (font.getFontSize() - font.getAscent())) / 2;
        double bs = boxSize;

        // Draw the background fill if the mouse is over the box.
        if (getBehaviorProperty(BehaviorUtil.MOUSE_OVER_PROPERTY, Boolean.class, false))
            g.setColor( new Color( 0xD0ffffff, true ) );
        else
            g.setColor( new Color( 0x80ffffff, true ) );

        g.fill(new RoundRectangle2D.Double(boxLeft, boxTop, bs, bs, cornerRadius, cornerRadius));

        // Draw the box
        g.setStroke( new BasicStroke( (float)(boxSize * 0.15f) ) );
        g.setColor(boxColor );
        g.draw(new RoundRectangle2D.Double(boxLeft, boxTop, bs, bs, cornerRadius, cornerRadius));

        // Draw the check if selected
        if (isSelected()) {
            double r = 3.5;
            boxLeft += boxSize / r;
            boxTop += boxSize / r;
            bs = boxSize * (r - 2) / r;

            g.draw( new Line2D.Double( boxLeft, boxTop, boxLeft + bs, boxTop + bs ) );
            g.draw( new Line2D.Double( boxLeft, boxTop + bs, boxLeft + bs, boxTop ) );
        }

        super.paint(g);

        // Draw the focus rect around the control
        if (FocusManager.isFocused(this)) {
            Rectangle2D bounds = getUnscaledBounds();
            GraphicsUtil.drawFocusRect( g, bounds );
        }
    } // paint( Graphics2D g )

    /**
     * Adds a new ValueChangedListener to the list of registered listeners,
     * if it is not already found in the list.
     * 
     * @param listener  The listener to add.
     * 
     * @throws  IllegalArgumentException if {@code listener} is {@code null}.
     */
    public synchronized void addValueChangedListener( ValueChangedListener listener ) {
        valueChangedListeners.add(listener);
    } // addValueChangedListener( ValueChangedListener listener )

    /**
     * Removes a ValueChangedListener from the list of registered listeners, if
     * it is found in the list.
     * 
     * @param listener  The listener to remove.
     */
    public synchronized void removeValueChangedListener( ValueChangedListener listener ) {
        valueChangedListeners.remove(listener);
    } // removeValueChangedListener( ValueChangedListener listener )

} // class Checkbox
