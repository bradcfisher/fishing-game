
package Fishing.drawable.dialogs;

import Fishing.drawable.BehaviorUtil;
import Fishing.drawable.Drawable;
import Fishing.drawable.FocusManager;
import Fishing.drawable.events.DrawableAdapter;
import Fishing.drawable.events.DrawableEvent;
import Fishing.drawable.events.DrawableKeyAdapter;
import Fishing.drawable.events.DrawableKeyEvent;
import Fishing.drawable.events.DrawableTreeAdapter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * TODO: Document
 * 
 * @author Brad
 */
public class Dialog
    extends Drawable
{

    /**
     * The color to use for the blocker layer displayed behind modal dialogs.
     */
    private Color blockerColor = new Color(0xA0ffffff, true);

    /**
     * The color to fill the dialog background with.
     */
    private Color backgroundColor = new Color(0xA0606060, true);

    /**
     * The color to use when drawing the dialog border.
     */
    private Color borderColor = new Color(0xA0FF0000, true);

    /**
     * The thickness of the dialog border, in local units.
     */
    private float borderWidth = 4;

    /**
     * The corner radius to use when drawing the dialog background and border.
     */
    private int cornerRadius = 20;

    /**
     * Whether the dialog should be displayed in a modal fashion or not.
     */
    private boolean modal = false;

    /**
     * The element that previously held the focus before this dialog was
     * displayed modally.
     */
    private Drawable oldFocus = null;

    /**
     * The blocker layer displayed behind the dialog when it is modal.
     */
    private BlockerBackground blockerBackground;

    /**
     * Constructs a new instance.
     */
    public Dialog() {
        setFocusable(true);
        setMouseEnabled(true);
        setMouseChildren(true);

        final Dialog self = this;

        addTreeListener(
            new DrawableTreeAdapter() {
                /**
                 * When this Dialog is added to a new parent, update the modal
                 * blocker if needed.
                 * 
                 * @param   e   The event that was dispatched.
                 */
                @Override
                public void drawableAdded( DrawableEvent e ) {
                    if (isVisibleOnStage())
                        updateBlocker( true );
                } // drawableAdded( DrawableEvent e )

                /**
                 * When this Dialog is removed from a parent, update the modal
                 * blocker if needed.
                 * 
                 * @param   e   The event that was dispatched.
                 */
                @Override
                public void drawableRemoved( DrawableEvent e ) {
                    if (blockerBackground != null)
                        updateBlocker( false );
                } // drawableRemoved( DrawableEvent e )
            }
        );

        addDrawableListener(
            new DrawableAdapter() {
                /**
                 * When this Dialog is shown, show a blocker layer and ensure
                 * that the first focusable object is focused or focus this
                 * dialog if no focusable objects are contained in the dialog.
                 * 
                 * @param   e   The event that was dispatched.
                 */
                @Override
                public void drawableShown(DrawableEvent e) {
// TODO: Should this dialog only take the focus when it is modal?  Perhaps a non-modal dialog should leave the focus as-is?
                    if (e.getSource() == self) {
                        FocusManager focusManager = self.getFocusManager();
                        if (focusManager != null) {
                            // Remember the previously focused object so it
                            // can be restored when the dialog is hidden.
                            if (oldFocus == null)
                                oldFocus = focusManager.getEffectiveFocus();

                            List<Drawable> focusable = getTabbableDescendants();

                            Drawable focusObject;
                            if (focusable.isEmpty()) {
                                focusObject = self;
                            } else {
                                // Determine if the currently focused object is
                                // in the list
                                int index = focusable.indexOf(focusManager.getEffectiveFocus());

                                // If not found, just use first object in list
                                if (index == -1)
                                    index = 0;

                                focusObject = focusable.get(index);
                            }

                            focusManager.setFocus(focusObject, true);
                        }

                        updateBlocker(true);
                    }
                } // drawableShown(DrawableEvent e)

                /**
                 * When this Dialog is hidden, ensure that the previously
                 * focused object (if any) is re-focused and remove the blocker
                 * layer.
                 * 
                 * @param   e   The event that was dispatched.
                 */
                @Override
                public void drawableHidden(DrawableEvent e) {
                    if (e.getSource() == self) {
                        if (oldFocus != null) {
                            FocusManager.focusDrawable(oldFocus, false);
                            oldFocus = null;
                        }

                        updateBlocker(false);
                    }
                } // drawableHidden(DrawableEvent e)
            }
        );
        
        BehaviorUtil.addTabbableContainerBehavior(this);

        getKeyListeners().addDefault(
            /**
             * Handle default actions for escape, tab and shift-tab.
             * 
             * <p>When escape is pressed, invokes the {@code cancel()} method
             * of the dialog.
             * 
             * <p>Tab and shift-tab move the focus between tabbable elements
             * within the dialog.
             * 
             * @param   e   The event that was dispatched.
             */
            new DrawableKeyAdapter() {
                @Override
                public void drawableKeyPressed(DrawableKeyEvent e) {
                    int modifiersMask = KeyEvent.SHIFT_DOWN_MASK | KeyEvent.CTRL_DOWN_MASK | KeyEvent.META_DOWN_MASK | KeyEvent.ALT_DOWN_MASK;

System.out.println("Dialog: key listener: keyCode="+ e.getKeyCode() +" modifiers="+ e.getModifiers() + " modifiersMask="+ modifiersMask);

                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_ESCAPE:
                            if ((e.getModifiers() & modifiersMask) == 0) {
System.out.println("Dialog: key listener: ESCAPE");
                                if (cancel()) {
                                    e.stopPropagation();
                                    e.consume();
                                }
                            }
                            break;
                    }
                } // drawableKeyTyped(DrawableKeyEvent e)
            }
        );
    } // Dialog()

    /**
     * Determines whether the dialog should be displayed in a modal fashion
     * or not.
     *
     * @return  {@code true} if the dialog should be displayed in a modal
     *          fashion, {@code false} if not.
     */
    public boolean isModal() {
        return modal;
    } // isModal()

    /**
     * Sets whether the dialog should be displayed in a modal fashion
     * or not.
     * 
     * <p>Visible modal dialogs do not allow interaction with elements behind
     * themselves.
     *
     * @param   value   {@code true} if the dialog should be displayed in a
     *                  modal fashion, {@code false} if not.
     */
    public void setModal( boolean value ) {
        modal = value;
    } // setModal( boolean value )

    /**
     * Retrieves the color to fill the dialog background with.
     * 
     * @return  The color to fill the dialog background with.
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    } // getBackgroundColor()

    /**
     * Sets the color to fill the dialog background with.
     * 
     * @param   value   The new color to use for filling the dialog background,
     *                  or {@code null} if no background fill should be drawn.
     */
    public void setBackgroundColor( Color value ) {
        backgroundColor = value;
    } // setBackgroundColor( Color value )

    /**
     * Retrieves the color to use when drawing the dialog border.
     * 
     * @return  The color to use when drawing the dialog border.
     */
    public Color getBorderColor() {
        return borderColor;
    } // getBorderColor()

    /**
     * Sets the color to use when drawing the dialog border.
     * 
     * @param   value   The new color to use when drawing the dialog border,
     *                  or {@code null} if no border should be drawn.
     */
    public void setBorderColor( Color value ) {
        borderColor = value;
    } // setBorderColor( Color value )

    /**
     * Retrieves the thickness of the dialog border.
     * 
     * @return  The thickness of the dialog border, in local units.  A value
     *          of 0 indicates no border will be drawn.
     */
    public float getBorderWidth() {
        return borderWidth;
    } // getBorderWidth()


    /**
     * Sets the thickness of the dialog border.
     * 
     * @param   value   The new thickness for the dialog border, in local
     *                  units.  A value of 0 or less will be clamped to 0 and
     *                  indicates no border should be drawn.
     */
    public void setBorderWidth( float value ) {
        if (value < 0) {
            value = 0;
        }

        borderWidth = value;
    } // setBorderWidth( float value )

    /**
     * Shows this dialog centered in the specified Drawable or in the dialog's
     * current parent if no new parent is provided.
     * 
     * @param   root    The parent Drawable to display this dialog in.  If
     *                  {@code null}, the dialog will be displayed within it's
     *                  current parent.
     * @param   modal   Whether to display as a modal dialog.  Modal dialogs do
     *                  not permit interacting with other elements outside the
     *                  dialog.
     * 
     * @throws  IllegalArgumentException if {@code root} is {@code null} or if
     *          the dialog's parent is not visible on a stage.
     */
    public void show( Drawable root, boolean modal ) {
        if (root == null)
            root = getParent();

        if (root == null)
            throw new IllegalArgumentException("You must pass in a non-null value for the root argument, or assign the dialog to a parent first.");

        if (!root.isVisibleOnStage())
            throw new IllegalArgumentException("Cannot show dialog on a non-visible parent element.");

        setModal(modal);

        setPosition( (root.getUnscaledWidth() - getWidth()) / 2, (root.getUnscaledHeight() - getHeight()) / 2 );

        // Bring to top/reparent
        root.addDrawable( this );
    } // show()

    /**
     * Perform the default cancel action.
     * 
     * <p>The base class implementation hides the dialog if it is modal,
     * and does nothing for non-modal dialogs.  Subclasses may override
     * this method to perform additional validation or different cancel
     * action.
     * 
     * @return  {@code true} if a cancel action was performed, {@code false}
     *          if no action was taken.
     */
    public boolean cancel() {
        if (isModal()) {
            setVisible(false);
            return true;
        }

        return false;
    } // cancel()

    /**
     * Adds or removes a modal blocker background if needed when this dialog
     * is hidden or shown.
     * 
     * <p>When the dialog is shown, this method also moves it to the top of
     * it's parent's child list.
     *
     * @param   shown   Pass {@code true} to indicate this dialog was shown,
     *                  {@code false} if it was hidden.
     */
    private void updateBlocker( boolean shown ) {
        Drawable p;

        if (shown) {
            p = getParent();
            if (p != null) {
                if (isModal()) {
                    // Add a blocker background
                    blockerBackground = new BlockerBackground( blockerColor );
                    p.addDrawable(blockerBackground);
                }

                // Move to top of display list
                p.addDrawable(this);
            }
        } else if (blockerBackground != null) {
            // Remove the blocker background
            p = blockerBackground.getParent();
            if (p != null)
                p.removeDrawable(blockerBackground);
            blockerBackground = null;
        }
    } // updateBlocker()

    /**
     * Draws the background fill and border for the dialog.
     * 
     * @param   g   The graphics context to draw into.
     */
    @Override
    public void paint( Graphics2D g ) {
        double w = getUnscaledWidth();
        double h = getUnscaledHeight();

        if (backgroundColor != null) {
            // Draw the background
            g.setColor( backgroundColor );
            g.fillRoundRect(0, 0, (int) w, (int) h, cornerRadius, cornerRadius);
        }

        if (borderColor != null) {
            g.setColor( borderColor );
            g.setStroke( new BasicStroke(borderWidth) );
            g.drawRoundRect(0, 0, (int) w, (int) h, cornerRadius, cornerRadius);
        }

        super.paint(g);
    } // paint( Graphics2D g )

} // class Dialog
