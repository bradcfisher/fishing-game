
package Fishing.drawable;

import Fishing.drawable.events.DrawableKeyAdapter;
import Fishing.drawable.events.DrawableKeyEvent;
import Fishing.drawable.events.DrawableMouseAdapter;
import Fishing.drawable.events.DrawableMouseEvent;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

/**
 * Utility class for registering event handlers for implementing common
 * component behaviors.
 * 
 * @author Brad
 */
public class BehaviorUtil {

    /**
     * The name of the behavior property that contains the mouse over status
     * determined by the "mouse hover" behavior.
     */
    public static final String MOUSE_OVER_PROPERTY = "mouseOver";

    /**
     * Adds a default key pressed handler that cycles the focus through the
     * tabbable children when TAB or SHIFT-TAB are pressed.
     * 
     * @param target    The object to add the behavior to.
     */
    public static void addTabbableContainerBehavior(Drawable target) {
        target.getKeyListeners().addDefault(
            new DrawableKeyAdapter() {
                @Override
                public void drawableKeyPressed(DrawableKeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_TAB:
                            if ((e.getModifiers() & DrawableKeyEvent.KEY_MODIFIERS_MASK) == 0) {
                                // TAB
                                FocusManager.focusNextTabbable(target, true);
                                e.stopPropagation();
                            } else if ((e.getModifiers() & DrawableKeyEvent.KEY_MODIFIERS_MASK) == DrawableKeyEvent.SHIFT_DOWN_MASK) {
                                // Shift TAB
                                FocusManager.focusNextTabbable(target, false);
                                e.stopPropagation();
                            }
                            break;
                    }
                } // drawableKeyPressed(DrawableKeyEvent e)
            }
        );
    } // addTabbableContainerBehavior(Drawable target)

    /**
     * Adds a default mouse pressed handler that sets the focus to the target.
     * 
     * @param target    The object to add the behavior to.
     */
    public static void addFocusableBehavior(Drawable target) {
        target.getMouseListeners().addDefault(
            new DrawableMouseAdapter() {
                @Override
                public void drawableMousePressed(DrawableMouseEvent e) {
                    if (
                        (target == e.getSource()) &&
                        target.isFocusable() &&
                        (e.getButton() == DrawableMouseEvent.BUTTON1)
                    ) {
                        target.setFocus();
                    }
                } // drawableMouseDown
            }
        );
    } // addFocusableBehavior(Drawable target)

    /**
     * Adds default mouse entered and mouse exited handlers that set the
     * {@link #MOUSE_OVER_PROPERTY} behavior property to indicate when the
     * mouse is over the target or not.
     * 
     * <p>Example for determining whether the mouse is currently hovering over
     * an object with this behavior:
     * <code><pre>
     * boolean mouseOver = target.getBehaviorProperty(BehaviorUtil.MOUSE_OVER_PROPERTY, Boolean.class, false);
     * </pre></code>
     * 
     * @param target    The object to add the behavior to.
     * 
     * @see #MOUSE_OVER_PROPERTY
     */
    public static void addMouseHoverBehavior(Drawable target) {
        target.setBehaviorProperty(MOUSE_OVER_PROPERTY, false);

        // Add mouse listeners for hover effect
        target.getMouseListeners().addDefault(
            new DrawableMouseAdapter() {
                @Override
                public void drawableMouseEntered( DrawableMouseEvent e ) {
                    if (e.getSource() == target) {
                        target.setBehaviorProperty(MOUSE_OVER_PROPERTY, true);
                    }
                } // drawableMouseEntered( DrawableMouseEvent e )

                @Override
                public void drawableMouseExited( DrawableMouseEvent e ) {
                    if (e.getSource() == target) {
                        target.setBehaviorProperty(MOUSE_OVER_PROPERTY, false);
                    }
                } // drawableMouseExited( DrawableMouseEvent e )
            }
        );
    } // addMouseHoverBehavior(Drawable target)

    /**
     * Adds a default key pressed handler that executes an action when the user
     * pressed the ENTER or SPACE keys.
     * 
     * @param <D>       The Drawable sub-type of the target object.
     * @param target    The object to add the behavior to.
     * @param action    The callback to invoke when the user hits enter or the
     *                  space bar while the target is focused.
     */
    public static <D extends Drawable> void addButtonKeyboardBehavior(
        D target,
        Consumer<D> action
    ) {
        target.getKeyListeners().addDefault(
            new DrawableKeyAdapter() {
                @Override
                public void drawableKeyPressed(DrawableKeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_ENTER:
                        case KeyEvent.VK_SPACE:
                            if ((e.getModifiers() & DrawableKeyEvent.KEY_MODIFIERS_MASK) == 0) {
                                action.accept(target);
                                e.stopPropagation();
                            }
                            break;
                    }
                }
            }
        );
    } // addButtonKeyboardBehavior(Drawable, Consumer<Drawable>)

} // BehaviorUtil
