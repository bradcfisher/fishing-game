
package Fishing.drawable;

import Fishing.drawable.events.DrawableAdapter;
import Fishing.drawable.events.DrawableEvent;
import Fishing.drawable.events.DrawableFocusEvent;
import Fishing.drawable.events.DrawableTreeAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import java.util.Objects;

/**
 * Utility class for dealing with focus events and managing which object in the
 * display list currently holds the focus.
 *
 * @author Brad
 */
public class FocusManager
    implements FocusListener
{

    /**
     * The root stage this FocusManager is managing the focus for.
     */
    private final Stage root;

    /**
     * Whether the root stage's panel has the system focus or not.
     */
    private boolean hasSystemFocus = false;

    /**
     * The drawable object under the root stage that has the focus.
     */
    private Drawable focus = null;

// TODO: Document
    private DrawableTreeAdapter focusTreeListener = null;

// TODO: Document
    private DrawableAdapter focusDrawableListener = null;

// TODO: Document
    private Drawable pendingFocus = null;

// TODO: Document
    private boolean pendingFocusTemporary = false;

// TODO: Document
    private DrawableAdapter pendingFocusDrawableListener = null;

    /**
     * Constructs a new instance for the specified root stage.
     * 
     * @param   root    The root stage to manage the focus for.
     * 
     * @throws  NullPointerException if the {@code root} is null.
     * @throws  IllegalArgumentException if the {@code root} is not a root
     *          stage.
     */
    public FocusManager(Stage root) {
        this.root = Objects.requireNonNull(root);

        if (!root.isRootStage())
            throw new IllegalArgumentException("The root parameter must refer to a root level stage");

        this.root.getPanel().addFocusListener(this);
    } // FocusManager()

    /**
     * Returns the object that is currently granted the focus, regardless of
     * where the system focus is.
     *
     * @return  The object that is currently granted the focus, regardless of
     *          where the system focus is.
     */
    public Drawable getEffectiveFocus() {
        if (focus == null || !focus.isFocusable() || !focus.isVisibleOnStage()) {
            return null;
        }

        return focus;
    } // getEffectiveFocus()

    /**
     * Returns the object that is currently granted the focus.
     * 
     * <p>If the system focus is currently elsewhere, returns null even if
     * there is an object assigned as the focus.  To retrieve the object
     * granted the focus, call the getEffectiveFocus() method.
     *
     * @return  The object that is currently granted the focus, or {@code null}
     *          if no object is currently focused.
     */
    public Drawable getCurrentFocus() {
        return hasSystemFocus
                ? getEffectiveFocus()
                : null;
    } // getCurrentFocus()

// TODO: Document
    public synchronized void setFocus( Drawable value, boolean temporary ) {
        if (focus == value)
            return;

System.out.println("FocusManager.setFocus: value="+ value +" temporary="+ temporary);

        if (value != null) {
            if (!value.isFocusable()) {
System.out.println("FocusManager.setFocus: not focusable");
                return;
            }

            if (pendingFocus != null) {
System.out.println("FocusManager.setFocus: had pending focus");
                pendingFocus.removeDrawableListener(pendingFocusDrawableListener);
                pendingFocus = null;
            }

            if ((value.getStage() == null) || !value.isVisibleOnStage()) {
System.out.println("FocusManager.setFocus: setting pending focus, since object is not visible");
                pendingFocus = value;
                pendingFocusTemporary = temporary;
                pendingFocusDrawableListener = new DrawableAdapter() {
                                @Override
                                public void drawableShown(DrawableEvent e) {
                                    setFocus(pendingFocus, pendingFocusTemporary);
                                } // drawableShown(DrawableEvent e)
                            };
                pendingFocus.addDrawableListener(pendingFocusDrawableListener);
                return;
            }
        }

        Drawable oldFocus = focus;
        focus = value;

        if (oldFocus != null) {
System.out.println("FocusManager.setFocus: updating old focus: oldFocus="+ oldFocus);
            oldFocus.removeTreeListener(focusTreeListener);
            oldFocus.removeDrawableListener(focusDrawableListener);

            focusTreeListener = null;
            focusDrawableListener = null;

            if (hasSystemFocus) {
System.out.println("FocusManager.setFocus: old focus: process focus event");
                oldFocus.processFocusEvent(
                    new DrawableFocusEvent(oldFocus, DrawableFocusEvent.FOCUS_LOST, temporary, focus )
                );
            }
        }

        if (focus != null) {
System.out.println("FocusManager.setFocus: updating new focus: oldFocus="+ focus);
            focusTreeListener = new DrawableTreeAdapter() {
                            @Override
                            public void drawableRemovedFromStage(DrawableEvent e) {
                                setFocus(null, false);
                            } // drawableRemovedFromStage(DrawableEvent e)
                        };

            focusDrawableListener = new DrawableAdapter() {
                            @Override
                            public void drawableHidden(DrawableEvent e) {
                                setFocus(null, false);
                            } // drawableHidden(DrawableEvent e)
                        };

            focus.addTreeListener(focusTreeListener);
            focus.addDrawableListener(focusDrawableListener);

            if (hasSystemFocus) {
System.out.println("FocusManager.setFocus: new focus: process focus event");
                focus.processFocusEvent(
                    new DrawableFocusEvent(focus, DrawableFocusEvent.FOCUS_GAINED, temporary, oldFocus )
                );
            }
        }

System.out.println("FocusManager.setFocus: done");
    } // setFocus( Drawable value )

    /**
     * Retrieves whether the root stage's panel has the system focus or not.
     *
     * @return  {@code true} if the root stage's panel has the system focus,
     *          {@code false} otherwise.
     */
    public boolean hasSystemFocus() {
        return hasSystemFocus;
    } // hasSystemFocus()

    /**
     * Captures AWT focus gained events and routes them to the currently
     * focused object on the associated stage.
     * 
     * @param   e   The AWT FocusEvent that occurred.
     */
    @Override
    public void focusGained(FocusEvent e) {
        hasSystemFocus = true;

        if (focus != null) {
            focus.processFocusEvent(
                new DrawableFocusEvent(focus, DrawableFocusEvent.FOCUS_GAINED, false, null )
            );
        }
    } // focusGained(FocusEvent e)

    /**
     * Captures AWT focus lost events and routes them to the currently
     * focused object on the associated stage.
     * 
     * @param   e   The AWT FocusEvent that occurred.
     */
    @Override
    public void focusLost(FocusEvent e) {
        hasSystemFocus = false;

        if (focus != null) {
            focus.processFocusEvent(
                new DrawableFocusEvent(focus, DrawableFocusEvent.FOCUS_LOST, false, null )
            );
        }
    } // focusLost(FocusEvent e)

    /**
     * Moves the focus to the next tabbable object in the specified direction.
     * 
     * @param   root    The Drawable that is to be used as the tabbing
     *                  container.  Only this container and any descendants
     *                  are considered when tabbing.
     * @param   forward {@code true} to move to the next tabbable object,
     *                  {@code false} to move to the previous tabbable object.
     * 
     * @throws  NullPointerException if {@code root} is null.
     */
    public static void focusNextTabbable(Drawable root, boolean forward) {
        FocusManager focusManager = root.getFocusManager();
        if (focusManager != null) {
            List<Drawable> tabbable = root.getTabbableDescendants();

            if (tabbable.isEmpty()) {
                if (root.getTabIndex() >= 0) {
System.out.println("FocusManager: focus the root object");
                    focusManager.setFocus(root, true);
                }
            } else {
                // Determine if the currently focused object is
                // in the list
                int index = tabbable.indexOf(focusManager.getEffectiveFocus());

                if (index == -1) {
System.out.println("FocusManager: use first tabbable object");
                    // If not found, just use first object in list
                    index = 0;
                } else {
                    // Move to the next object in the requested direction
                    int max = tabbable.size() - 1;
                    if (forward) {
                        ++index;
                        if (index > max)
                            index = 0;
System.out.println("FocusManager: next tabbable object index="+ index);
                    } else {
                        --index;
                        if (index < 0)
                            index = max;
System.out.println("FocusManager: previous tabbable object index="+ index);
                    }
                }

System.out.println("FocusManager: setting focus to: "+ tabbable.get(index));

                focusManager.setFocus(tabbable.get(index), true);
            }
        }
    } // focusNextTabbable(boolean reverse)

    /**
     * Moves the focus to the specified object.
     * 
     * @param   obj The Drawable to attempt to set the focus to.
// TODO: Document temporary parameter
     * 
     * 
     * @throws  NullPointerException if {@code obj} is null.
     */
    public static void focusDrawable(Drawable obj, boolean temporary) {
        FocusManager focusManager = obj.getFocusManager();
        if (focusManager != null) {
            focusManager.setFocus(obj, temporary);
        }
    } // focusDrawable(Drawable obj)

    /**
     * Determines the object that is currently focused within the root stage
     * the specified object is attached to.
     * 
     * @param   obj The context object to retrieve the focused object for.
     * 
     * @return  The object that is currently focused within the root stage
     *          the specified object is attached to.
     */
    public static Drawable getCurrentFocus(Drawable obj) {
        // Dispatch the key event to the object with the focus
        FocusManager focusManager = obj.getFocusManager();

        return (focusManager != null)
                ? focusManager.getCurrentFocus()
                : null;
    } // getCurrentFocus()

    /**
     * Determines whether the specified object is currently focused.
     * 
     * @param   obj The object to check to determine if it is focused.
     * 
     * @return  {@code true} if the specified object is currently focused,
     *          {@code false} if it is not focused.
     */
    public static boolean isFocused(Drawable obj) {
        return (getCurrentFocus(obj) == obj);
    } // isFocused()

    /**
     * Determines whether it, or any descendant of the specified object, is
     * currently focused.
     * 
     * @param   obj The object to check to determine if it, or any descendant,
     *              is currently focused.
     * 
     * @return  {@code true} if any descendant of the specified object is
     *          currently focused, {@code false} otherwise.
     */
    public static boolean isDescendantFocused(Drawable obj) {
        final Drawable currentFocus = getCurrentFocus(obj);

        return (currentFocus != null) && (
                (currentFocus == obj) ||
                !obj.getMatchingDescendants(null, (child) -> { return (child == currentFocus); }).isEmpty()
            );
    } // isDescendantFocused()

} // class FocusManager
