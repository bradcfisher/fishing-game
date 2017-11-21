
package Fishing.drawable.controls;

import Fishing.GraphicsUtil;
import Fishing.drawable.BehaviorUtil;
import Fishing.drawable.Drawable;
import Fishing.drawable.FocusManager;
import Fishing.drawable.events.DrawableKeyAdapter;
import Fishing.drawable.events.DrawableKeyEvent;
import Fishing.drawable.events.DrawableListeners;
import Fishing.drawable.events.DrawableMouseAdapter;
import Fishing.drawable.events.DrawableMouseEvent;
import Fishing.drawable.events.TimerEvent;
import Fishing.drawable.events.ValueChangedEvent;
import Fishing.drawable.events.ValueChangedListener;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * A horizontal slider control for selecting from a numeric range.
 * 
 * @author Brad
 */
public class Slider
    extends Drawable
{

    /**
     * Indicates the mouse is not currently hovering over the control.
     */
    private static final int ITEM_NONE  = -1;

    /**
     * Indicates the mouse is hovering over the thumb of the slider.
     */
    private static final int ITEM_THUMB =  0;

    /**
     * Indicates the mouse is hovering over the left arrow of the slider.
     */
    private static final int ITEM_LEFT  =  1;

    /**
     * Indicates the mouse is hovering over the right arrow of the slider.
     */
    private static final int ITEM_RIGHT =  2;

    /**
     * Indicates the mouse is hovering over the bar of the slider.
     */
    private static final int ITEM_BAR   =  3;

    /**
     * The height to draw the bar portion of the slider.
     */
    private double barHeight = 10;

    /**
     * The start of the value range for the slider.
     */
    private double rangeStart = 0;

    /**
     * The end of the value range for the slider.
     */
    private double rangeEnd = 1;

    /**
     * The current value of the control.
     */
    private double value;

    /**
     * The portion of the control that the mouse is currently hovering over.
     */
    private int mouseOverItem = ITEM_NONE;

    /**
     * Bounding rectangles of the various interactive portions of the slider
     * for determining the target of mouse interaction.
     */
    private Rectangle2D[] itemBounds = new Rectangle2D.Double[4];

    /**
     * Whether the user is currently dragging the thumb or not.
     */
    private boolean draggingThumb = false;

    /**
     * ControlTimer handler of the timer used for triggering repeated 
     */
    private int arrowTimer = 0;

    /**
     * Registered listeners for ValueChangedEvents.
     */
    private DrawableListeners<ValueChangedListener> valueChangedListeners = new DrawableListeners<>();

    /**
     * Constructs a new instance.
     */
    public Slider() {
        // Add default mouse listeners for dealing with custom hover behavior,
        // thumb dragging and clicks on the various portions of the control.
        getMouseListeners().addDefault(
            new DrawableMouseAdapter() {
                @Override
                public void drawableMouseMoved( DrawableMouseEvent e ) {
                    mouseOverItem = ITEM_NONE;
                    for (int i = 0; i < itemBounds.length; ++i) {
                        if (itemBounds[i].contains( e.getPosition() )) {
                            mouseOverItem = i;
                            break;
                        }
                    } // for
                } // drawableMouseMoved( DrawableMouseEvent e )

                @Override
                public void drawableMouseExited( DrawableMouseEvent e ) {
                    if (!draggingThumb)
                        mouseOverItem = ITEM_NONE;
                } // drawableMouseExited( DrawableMouseEvent e )

                @Override
                public void drawableMousePressed( DrawableMouseEvent e ) {
                    switch (mouseOverItem) {
                        case ITEM_LEFT:
                        case ITEM_RIGHT:
                            // Adjust the value immediately
                            arrowTimerFired( null );

                            // Start a timer triggering first in 500ms, then
                            // every 100ms thereafter which adjusts the value
                            // each time it fires.
                            arrowTimer = ControlTimer.scheduleAtFixedRate(Slider::arrowTimerFired, 500, 100);
                            break;

                        case ITEM_BAR:
                            setValueFromPoint( e.getPosition() );
                            mouseOverItem = ITEM_THUMB;
                            draggingThumb = true;
                            break;

                        case ITEM_THUMB:
                            draggingThumb = true;
                            break;
                    }
                } // drawableMousePressed( DrawableMouseEvent e )

                @Override
                public void drawableMouseReleased( DrawableMouseEvent e ) {
                    // Cancel the arrow timer if needed
                    if (arrowTimer != 0) {
                        ControlTimer.cancelTimerListener(arrowTimer);
                        arrowTimer = 0;
                    }
                    draggingThumb = false;
                    drawableMouseMoved( e );
                } // drawableMouseReleased( DrawableMouseEvent e )

                @Override
                public void drawableMouseDragged( DrawableMouseEvent e ) {
                    if (draggingThumb) {
                        setValueFromPoint( e.getPosition() );
                    }
                } // drawableMouseDragged( DrawableMouseEvent e )
            }
        );

        // Add default key listeners which increase/decrease the value when
        // the user presses the arrow keys.
        getKeyListeners().addDefault(
            new DrawableKeyAdapter() {
                @Override
                public void drawableKeyPressed(DrawableKeyEvent e) {
                    if ((e.getModifiers() & DrawableKeyEvent.KEY_MODIFIERS_MASK) == 0) {
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_LEFT:
                            case KeyEvent.VK_UP:
                                adjustValueTowardStart(1);
                                e.stopPropagation();
                                e.consume();
                                break;

                            case KeyEvent.VK_RIGHT:
                            case KeyEvent.VK_DOWN:
                                adjustValueTowardEnd(1);
                                e.stopPropagation();
                                e.consume();
                                break;
                        }
                    }
                } // drawableKeyPressed(DrawableKeyEvent e)
            }
        );

        BehaviorUtil.addFocusableBehavior(this);

        setMouseEnabled(true);
        setTabIndex(0);
        setFocusable(true);
        setUnscaledSize( barHeight * 15, barHeight * 3 );
    } // Slider()

    /**
     * Constructs a new instance.
     * 
     * @param rangeStart    The start of the value range for the slider.
     * @param rangeEnd      The end of the value range for the slider.
     */
    public Slider( double rangeStart, double rangeEnd ) {
        this();
        setRange( rangeStart, rangeEnd );
    } // Slider()

    /**
     * Retrieves the height to draw the bar portion of the slider.
     * 
     * @return  The height to draw the bar portion of the slider.
     */
    public double getBarHeight() {
        return barHeight;
    } // getBarHeight()

    /**
     * Sets the height to draw the bar portion of the slider.
     * 
     * @param   value   The height to draw the bar portion of the slider.
     * 
     * @throws  IllegalArgumentException if the {@code value} is not greater
     *          than 0.
     */
    public void setBarHeight( double value ) {
        if (barHeight == value)
            return;
        
        if (value <= 0)
            throw new IllegalArgumentException("The barHeight must be greater than 0");

        barHeight = value;

        setUnscaledHeight( barHeight * 3 );
    } // setBarHeight( double value )

    /**
     * Adjusts the value by the specified amount toward the start of the value
     * range.
     * 
     * @param amount    The amount to adjust the value by.  If the start of the
     *                  value range is less than the end of the range, this
     *                  amount will be subtracted from the current value.  If
     *                  the start of the value range is greater than the end of
     *                  the range, the amount will be added to the current
     *                  value.
     */
    public void adjustValueTowardStart(double amount) {
        if (rangeStart < rangeEnd) {
            if (value > rangeStart)
                setValue( value - amount );
        } else {
            if (value < rangeStart)
                setValue( value + amount );
        }
    } // adjustValueTowardStart(double amount)

    /**
     * Adjusts the value by the specified amount toward the end of the value
     * range.
     * 
     * @param amount    The amount to adjust the value by.  If the start of the
     *                  value range is less than the end of the range, this
     *                  amount will be added to the current value.  If the
     *                  start of the value range is greater than the end of the
     *                  range, the amount will be subtracted from the current
     *                  value.
     */
    public void adjustValueTowardEnd(double amount) {
        if (rangeStart < rangeEnd) {
            if (value < rangeEnd)
                setValue( value + amount );
        } else {
            if (value > rangeStart)
                setValue( value - amount );
        }
    } // adjustValueTowardEnd(double amount)

    /**
     * Callback invoked when the {@code arrowTimer} fires.
     * Adjusts the value toward the arrow the user is pressing.
     * @param e The TimerEvent
     */
    private void arrowTimerFired(TimerEvent e) {
        switch (mouseOverItem) {
            case ITEM_LEFT:
                adjustValueTowardStart(1);
                break;

            case ITEM_RIGHT:
                adjustValueTowardEnd(1);
                break;
        } // switch
    } // arrowTimerFired( TimerEvent e )

    /**
     * Sets the value of the slider by computing the relation between the
     * specified coordinate and the specified value range.
     * 
     * @param pt    The local coordinate to determine the value of.
     */
    private void setValueFromPoint( Point2D pt ) {
        Rectangle2D bounds = itemBounds[ITEM_BAR];
        double x = pt.getX() - bounds.getMinX();
        if (x < 0)
            x = 0;
        else if (x > bounds.getWidth())
            x = bounds.getWidth();

        setValue((x / bounds.getWidth()) * (rangeEnd - rangeStart) + rangeStart );
    } // setValueFromPoint( Point2D pt )

    /**
     * Retrieves the start of the value range for the slider.
     * 
     * @return  The start of the value range for the slider.
     */
    public double getRangeStart() {
        return rangeStart;
    } //getRangeStart()

    /**
     * Sets the start of the value range for the slider.
     * 
     * @param value The start of the value range for the slider.
     */
    public void setRangeStart( double value ) {
        setRange(value, rangeEnd );
    } //setRangeStart()

    /**
     * Retrieves the end of the value range for the slider.
     * 
     * @return  The end of the value range for the slider.
     */
    public double getRangeEnd() {
        return rangeEnd;
    } //getRangeEnd()

    /**
     * Sets the end of the value range for the slider.
     * 
     * @param value The end of the value range for the slider.
     */
    public void setRangeEnd( double value ) {
        setRange(rangeStart, value );
    } //setRangeEnd()

    /**
     * Sets the value range for the slider.
     * 
     * @param start The start of the value range.
     * @param end   The end of the value range.
     */
    public final void setRange( double start, double end ) {
        rangeStart = start;
        rangeEnd = end;

        if (start < end) {
            if (value < start)
                value = start;
            else if (value > end)
                value = end;
        } else {
            if (value > start)
                value = start;
            else if (value < end)
                value = end;
        }
    } // setRange( double start, double end )

    /**
     * Retrieves the current value for the slider.
     * 
     * @return  The current value for the slider.  This value will always be
     *          in the range defined by {@code rangeStart} and
     *          {@code rangeEnd}.
     */
    public double getValue() {
        return value;
    } // getValue()

    /**
     * Sets the current value for the slider.
     * 
     * @param   value   The new value for the slider.  This value must be  in
     *                  the range defined by {@code rangeStart} and
     *                  {@code rangeEnd}.
     * 
     * @throws  IllegalArgumentException if {@code value} is outside the range
     *          defined by {@code rangeStart} and {@code rangeEnd}.
     */
    public synchronized void setValue( double value ) {
        double start, end;
        if (rangeStart < rangeEnd) {
            start = rangeStart;
            end = rangeEnd;
        } else {
            start = rangeEnd;
            end = rangeStart;
        }

        if ((value < start) || (value > end))
            throw new IllegalArgumentException("The value is outside the range "+ start +" to "+ end);

        double oldValue = value;
        this.value = value;

        valueChangedListeners.notifyListeners(
            new ValueChangedEvent(this, oldValue, value),
            (listener, evt) -> { listener.valueChanged(evt); }
        );
    } // setValue( double value )

    /**
     * Adds a new ValueChangedListener to the end of list of registered
     * listeners, if it is not already found in the list.
     * 
     * @param listener  The listener to add.
     * 
     * @throws  IllegalArgumentException if {@code listener} is {@code null}.
     */
    public void addValueChangedListener( ValueChangedListener listener ) {
        valueChangedListeners.add(listener);
    } // addValueChangedListener( ValueChangedListener listener )

    /**
     * Removes a ValueChangedListener from the list of registered listeners,
     * if it is found in the list.
     * 
     * @param listener  The listener to remove.
     */
    public void removeValueChangedListener( ValueChangedListener listener ) {
        valueChangedListeners.remove(listener);
    } // removeValueChangedListener( ValueChangedListener listener )

    /**
     * Retrieves the registered ValueChangedListeners.
     * 
     * @return  The registered ValueChangedListeners.
     */
    protected DrawableListeners<ValueChangedListener> getValueChangedListeners() {
        return valueChangedListeners;
    } // getValueChangedListeners()

    @Override
    public void paint( Graphics2D g ) {
        int w = (int) getUnscaledWidth();

        double halfBarHeight = barHeight / 2;

        Color clr1 = Color.WHITE;
        Color clr2 = Color.GRAY;
        Color clrBody = Color.LIGHT_GRAY;
        Color clrBody2 = Color.GRAY;
        Path2D.Double path;

        double arrowWidth = 1.5 * barHeight;
        double arrowSpace = arrowWidth + barHeight;
        double barWidth = w - 2 * arrowSpace;

        g.setStroke( new BasicStroke( 2 ) );

        // Draw the bar
        itemBounds[ITEM_BAR] = new Rectangle2D.Double(arrowSpace, barHeight, barWidth, barHeight);
        GraphicsUtil.draw3DRoundRect( g, clr1, clr2, (mouseOverItem == ITEM_BAR) ? clrBody2 : clrBody, itemBounds[ITEM_BAR], halfBarHeight);

        // Draw the <
        path = new Path2D.Double();
        path.moveTo(halfBarHeight, 1.5 * barHeight);
        path.lineTo(halfBarHeight + arrowWidth, halfBarHeight);
        path.lineTo(halfBarHeight + arrowWidth, barHeight * 2.5);
        path.closePath();

        itemBounds[ITEM_LEFT] = path.getBounds2D();

        g.setColor( (mouseOverItem == ITEM_LEFT) ? clrBody2 : clrBody );
        g.fill(path);

        g.setColor( clr1 );
        g.draw(path);

        // Draw the >
        path.reset();
        path = new Path2D.Double();
        path.moveTo(w - halfBarHeight, 1.5 * barHeight);
        path.lineTo(w - (halfBarHeight + arrowWidth), halfBarHeight);
        path.lineTo(w - (halfBarHeight + arrowWidth), barHeight * 2.5);
        path.closePath();

        itemBounds[ITEM_RIGHT] = path.getBounds2D();

        g.setColor( (mouseOverItem == ITEM_RIGHT) ? clrBody2 : clrBody );
        g.fill(path);

        g.setColor( clr1 );
        g.draw(path);

        // Draw the thumb
        double range = Math.abs(rangeEnd - rangeStart);
        double relPos = Math.abs(value - rangeStart);
        double valuePos = (arrowSpace + (relPos / range) * (barWidth - barHeight));

        itemBounds[ITEM_THUMB] = new Rectangle2D.Double(valuePos, 0, barHeight, barHeight * 3);
        GraphicsUtil.draw3DRoundRect( g, clr1, clr2, (mouseOverItem == ITEM_THUMB) ? clrBody2 : clrBody, itemBounds[ITEM_THUMB], halfBarHeight);
        
        if (FocusManager.isFocused(this)) {
            GraphicsUtil.drawFocusRect( g, getUnscaledBounds() );
        }
    } // paint( Graphics2D g )

} // class Slider
