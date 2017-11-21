
package Fishing.drawable.controls;

import Fishing.drawable.BehaviorUtil;
import Fishing.GraphicsUtil;
import Fishing.drawable.Drawable;
import Fishing.drawable.FocusManager;
import Fishing.drawable.events.DrawableMouseEvent;
import Fishing.drawable.text.BitmapFont;
import Fishing.drawable.text.BitmapText;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

/**
 * A focusable Button control with adjustable size, caption text, background
 * color and border width.
 *
 * @author Brad
 */
public class Button
    extends Drawable
{

    /**
     * Current caption text.
     */
    private final BitmapText caption;
    
    /**
     * The color to fill the button background with.
     */
    private Color backgroundColor;
    
    /**
     * The thickness of the button border, in local units.
     */
    private float borderWidth;

    /**
     * Constructs a new Button using the provided font for the caption.
     * 
     * Initially, the button will be assigned a background color of Color.GREY,
     * a border width of 3, and no caption text.
     *
     * @param font The BitmapFont to use for rendering the button's caption.
     */
    public Button( BitmapFont font ) {
        setUnscaledSize(100, 30);

        backgroundColor = Color.GRAY;
        borderWidth = 3;

        caption = new BitmapText(font);
        addDrawable(caption);

        BehaviorUtil.addMouseHoverBehavior(this);

        BehaviorUtil.addFocusableBehavior(this);

        BehaviorUtil.addButtonKeyboardBehavior(this, (obj) -> { obj.click(); });

        setMouseEnabled(true);
        setFocusable(true);
        setTabIndex(0);
    } // Button()

    /**
     * Retrieves the font used for rendering the caption.
     * 
     * @return The font used for rendering the caption.
     */
    public BitmapFont getFont() {
        return caption.getFont();
    } // getFont()

    /**
     * Assigns a new font to use for rendering the caption.
     * 
     * @param value The new font to assign.
     * 
     * @throws  NullPointerException if {@code value} is {@code null}.
     */
    public void setFont( BitmapFont value ) {
        caption.setFont(value);
        invalidate();
    } // setFont( BitmapFont value )

    /**
     * Retrieves the caption text.
     * @return The caption text.
     */
    public String getCaption() {
        return caption.getText();
    } // getCaption()

    /**
     * Sets the caption text.
     * @param   value   The new caption text.
     */
    public void setCaption( String value ) {
        caption.setText( value );
        invalidate();
    } // setCaption( String value )

    /**
     * Retrieves the color to fill the button background with.
     * 
     * @return  The color to fill the button background with.
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    } // getBackgroundColor()

    /**
     * Sets the color to fill the button background with.
     * 
     * @param   value   The new color to use for filling the button background,
     *                  or {@code null} if no background fill should be drawn.
     * 
     * @throws  NullPointerException if {@code value} is null.
     */
    public void setBackgroundColor( Color value ) {
        backgroundColor = Objects.requireNonNull(value, "The value cannot be null");
    } // getBackgroundColor( Color value )

    /**
     * Retrieves the thickness of the button border.
     * 
     * @return  The thickness of the button border, in local units.
     */
    public float getBorderWidth() {
        return borderWidth;
    } // getBorderWidth()

    /**
     * Sets the thickness of the button border.
     * 
     * @param   value   The new thickness for the button border, in local
     *                  units.
     */
    public void setBorderWidth( float value ) {
        borderWidth = value;
    } // setBorderWidth( float value )

    /**
     * Dispatches a mouse click event to this button.
     */
    public void click() {
        Point2D center = getCenter();
        Point2D screenPos = localToGlobal(center);

        getMouseListeners().notifyListeners(
            new DrawableMouseEvent(
                    this,
                    DrawableMouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(),
                    0, // no modifiers
                    center.getX(), center.getY(),
                    (int) screenPos.getX(), (int) screenPos.getY(),
                    1, // 1 click
                    DrawableMouseEvent.BUTTON1
            ),
            (listener, evt) -> { listener.drawableMouseClicked(evt); }
        );
    } // click()

    @Override
    public void validate() {
        double w = getUnscaledWidth();
        double h = getUnscaledHeight();
        double pad = caption.getFont().getFontSize() / 4;
        boolean updateSize = false;

        double t = caption.getWidth() + 2 * pad;
        if (t > w) {
            w = t;
            updateSize = true;
        }

        t = caption.getHeight() + 2 * pad;
        if (t > h) {
            h = t;
            updateSize = true;
        }

        if (updateSize)
            setUnscaledSize( w, h );

        caption.setPosition((w - caption.getWidth()) / 2, (h - caption.getHeight()) / 2);
    } // validate()

    @Override
    public void setUnscaledSize( double width, double height ) {
        super.setUnscaledSize(width, height);
        invalidate();
    } // setUnscaledSize( double width, double height )

    @Override
    public void setUnscaledWidth( double value ) {
        super.setUnscaledWidth(value);
        invalidate();
    } // setUnscaledWidth( double value )

    @Override
    public void setUnscaledHeight( double value ) {
        super.setUnscaledHeight(value);
        invalidate();
    } // setUnscaledHeight( double value )

    /**
     * Draws the background fill, border and possibly focus indicator for
     * the button.
     * 
     * @param   g   The graphics context to draw into.
     */
    @Override
    public void paint( Graphics2D g ) {
        Color overColor = backgroundColor.brighter();
        Color clr1 = overColor.brighter();
        Color clr2 = backgroundColor.darker();

        Rectangle2D bounds = new Rectangle2D.Double(
                    borderWidth / 2,
                    borderWidth / 2,
                    getUnscaledWidth() - borderWidth,
                    getUnscaledHeight() - borderWidth
                );

        double radius = Math.min(bounds.getWidth(), bounds.getHeight()) / 6;

        boolean mouseOver = getBehaviorProperty(BehaviorUtil.MOUSE_OVER_PROPERTY, Boolean.class, false);

        g.setStroke( new BasicStroke(borderWidth) );
        GraphicsUtil.draw3DRoundRect( g, clr1, clr2, (mouseOver ? overColor : backgroundColor), bounds, radius);

        super.paint(g);

        if (FocusManager.isFocused(this)) {
            GraphicsUtil.drawFocusRect( g, bounds );
        }
    } // paint( Graphics2D g )

} // class Button
