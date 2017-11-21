
package Fishing.drawable;

import com.jhlabs.image.AbstractBufferedImageOp;
import Fishing.drawable.events.DrawableEvent;
import Fishing.drawable.events.DrawableFocusEvent;
import Fishing.drawable.events.DrawableFocusListener;
import Fishing.drawable.events.DrawableKeyEvent;
import Fishing.drawable.events.DrawableKeyListener;
import Fishing.drawable.events.DrawableListener;
import Fishing.drawable.events.DrawableListeners;
import Fishing.drawable.events.DrawableMouseEvent;
import Fishing.drawable.events.DrawableMouseListener;
import Fishing.drawable.events.DrawableTreeListener;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * Base class for all objects that can be represented in the display list and
 * rendered to the screen.
 *
 * @author Brad
 */
public class Drawable {

    /**
     * The name assigned to this Drawable.
     */
    private String name;

    /**
     * The stage this Drawable is being rendered to.
     */
    private Stage stage;

    /**
     * The parent object that this Drawable is a direct child of.
     */
    private Drawable parent;

    /**
     * Whether this object needs to be redrawn or not.
     */
    private boolean invalidated = true;

    /**
     * Flag indicating whether the {@link #validate()} method is currently
     * being invoked by the internal Drawable logic.
     *
     * While set, this flag prevents {@link #invalidate()} calls from setting
     * the {@link #invalidated} flag, and thus preventing recursive
     * re-validation.
     */
    private boolean validating = false;

    /**
     * Whether this object can acquire the input focus or not.
     */
    private boolean focusable = false;

    /**
     * The tab index assigned to the object.
     * 
     * <p>Only focusable objects participate in the tabbing order.
     * 
     * <p>An object with a negative tab index does not participate in the
     * tabbing order, even if it is focusable.
     * 
     * <p>All objects with a tab index of 0 are ordered after elements
     * with a positive tab index using their relative order in the display
     * list tree.
     * 
     * <p>Objects with the same tab index are ordered first by their tab
     * index, then relative to each other by their position in the display
     * list tree.
     */
    private int tabIndex = -1;

    /**
     * Whether this object is visible or not.
     */
    private boolean visible = true;

    /**
     * Whether this object can receive mouse input events or not.
     */
    private boolean mouseEnabled = false;

    /**
     * Whether children of this object can receive mouse input events or not.
     */
    private boolean mouseChildren = false;

    /**
     * The global opacity to apply when rendering this object.
     */
    private float opacity = 1.0f;

    /**
     * The horizontal position of this object, in the parent's coordinate system.
     */
    private double x = 0;

    /**
     * The vertical position of this object, in the parent's coordinate system.
     */
    private double y = 0;

// TODO: Document
    private double centerX = 0;

// TODO: Document
    private double centerY = 0;

    /**
     * The width of this object, in the object's own coordinate system.
     */
    private double unscaledWidth = 0;

    /**
     * The height of this object, in the object's own coordinate system.
     */
    private double unscaledHeight = 0;

    /**
     * Horizontal scaling transformation to apply to this object when rendering
     * to the parent's coordinate system.
     */
    private double scaleX = 1;

    /**
     * Vertical scaling transformation to apply to this object when rendering
     * to the parent's coordinate system.
     */
    private double scaleY = 1;

    /**
     * Rotation transformation to apply to this object when rendering to the
     * parent's coordinate system.
     */
    private double rotation = 0;

// TODO: Document
    private AffineTransform transform;

    /**
     * Whether to clip child objects to this object's bounds or not.
     */
    private boolean clipChildren = false;

    /**
     * List of filters to apply to this object.
     */
    private List<AbstractBufferedImageOp> filters = null;

    /**
     * List of direct child objects.
     */
    private List<Drawable> drawable = null;

    // Event listeners

// TODO: Document
    private DrawableListeners<DrawableListener> listeners = new DrawableListeners<>();

// TODO: Document
    private DrawableListeners<DrawableMouseListener> mouseListeners = new DrawableListeners<>();

// TODO: Document
    private DrawableListeners<DrawableKeyListener> keyListeners = new DrawableListeners<>();

// TODO: Document
    private DrawableListeners<DrawableFocusListener> focusListeners = new DrawableListeners<>();

// TODO: Document
    private DrawableListeners<DrawableTreeListener> treeListeners = new DrawableListeners<>();

    /**
     * Retrieves the name assigned to this Drawable.
     *
     * @return The name assigned to this Drawable, may be {@code null}.
     */
    public String getName() {
        return name;
    } // getName()

    /**
     * Updates the name assigned to this Drawable.
     *
     * @param   value   The new name to assign to this Drawable.  May be
     *                  {@code null}.
     */
    public void setName(String value) {
        name = value;
    } // setName(String value)

    /**
     * Retrieves the FocusManager associated with the root stage.
     *
     * @return  The FocusManager associated with the root stage, or
     *          {@code null} if this object is not currently attached to a
     *          root stage.
     */
    public FocusManager getFocusManager() {
        Stage root = getRoot();
        return (root != null)
                ? root.getFocusManager()
                : null;
    } // getFocusManager()

    /**
     * Retrieves the global stage.
     * 
     * @return  The global stage.  May be {@code null) if this instance is not
     *          currently associated with a root stage.
     */
    public Stage getRoot() {
        Stage stage = getStage();
        return (stage != null) ? stage.getRoot() : null;
    } // getRoot()

    /**
     * Retrieves the stage this Drawable is being rendered to.
     * 
     * @return  The stage this Drawable is being rendered to.
     */
    public Stage getStage() {
        return stage;
    } // getStage()

    /**
     * Updates the stage this Drawable is being rendered to.
     * 
     * <p>This method is called internally when this object's parent is changed.
     * 
     * <p>When the object is removed from a stage, this method will invoke
     * the {@code drawableRemovedFromStage()} method of any associated
     * {@link DrawableTreeListener}s.
     * 
     * <p>When the object is added to a new stage, this method will invoke
     * the {@code drawableAddedToStage()} method of any associated
     * {@link DrawableTreeListener}s.
     * 
     * <p>This method may invoke both {@code drawableRemovedFromStage()} and
     * {@code drawableAddedToStage()} (in that order) if the object was previously
     * associated to a stage and is assigned to a new stage.
     * 
     * @param   value   The new stage this Drawable will be rendered to.
     */
    private synchronized void setStage(Stage value) {
        if (stage == value)
            return;

        Stage previousStage = stage;

        stage = value;

        if (previousStage != null) {
            treeListeners.notifyListeners(
                new DrawableEvent(this, previousStage),
                DrawableTreeListener::drawableRemovedFromStage
            );

            if (previousStage.isRootStage()) {
                treeListeners.notifyListeners(
                    new DrawableEvent(this, previousStage),
                    DrawableTreeListener::drawableRemovedFromRootStage
                );
            }
        }

        if (stage != null) {
            treeListeners.notifyListeners(
                new DrawableEvent(this, stage),
                DrawableTreeListener::drawableAddedToStage
            );

            if (stage.isRootStage()) {
                treeListeners.notifyListeners(
                    new DrawableEvent(this, stage),
                    DrawableTreeListener::drawableAddedToRootStage
                );
            }
        }

        List<Drawable> children = this.drawable;
        if ((children != null) && !(this instanceof Stage)) {
            for (Drawable child : children)
                child.setStage( value );
        }
    } // setStage(Drawable value)

    /**
     * Retrieves the parent this Drawable is a child of.
     * 
     * @return  The parent this Drawable is a child of.
     */
    public Drawable getParent() {
        return parent;
    } // getParent()

    /**
     * Updates the parent this Drawable is a child of.
     * 
     * <p>This method is called internally when this object is added as a child
     * of another object.
     * 
     * <p>When the object is removed from a parent, this method will invoke
     * the {@code drawableChildRemoved()} method of any 
     * {@link DrawableTreeListener}s associated with the old parent and
     * the {@code drawableRemoved()} method of any {@link DrawableTreeListener}s
     * associated with this object.
     * 
     * <p>When the object is added to a new parent, this method will invoke
     * the {@code drawableChildAdded()} method of any 
     * {@link DrawableTreeListener}s associated with the new parent and
     * the {@code drawableAdded()} method of any {@link DrawableTreeListener}s
     * associated with this object.
     * 
     * <p>This method may invoke both if the object was previously associated
     * to a parent and is assigned to a new parent.
     * 
     * <p>Adding or removing an object from a parent may also trigger calls to
     * the {@code drawableShown()} or {@code drawableHidden()} methods on any
     * associated {@link DrawableListener}s on this object or any descendant
     * object.
     * 
     * @param   value   The new parent this Drawable is now a child of.
     */
    private synchronized void setParent( Drawable newParent ) {
        if (parent == newParent)
            return;

        if (this.getStage() == this) {
            throw new UnsupportedOperationException(
                "Objects that are their own Stage cannot be assigned a parent"
            );
        }

        Drawable oldParent = parent;
        parent = newParent;

        if (oldParent != null) {
            oldParent.treeListeners.notifyListeners(
                new DrawableEvent(oldParent, this),
                DrawableTreeListener::drawableChildRemoved
            );

            treeListeners.notifyListeners(
                new DrawableEvent(this, oldParent),
                DrawableTreeListener::drawableRemoved
            );
        }

        if (newParent != null) {
            treeListeners.notifyListeners(
                new DrawableEvent(this, newParent),
                DrawableTreeListener::drawableAdded
            );
        }

        Stage oldStage = stage;
        Stage newStage = (newParent != null ? newParent.getStage() : null);

        if (oldStage == null) {
            if (newStage != null) {
                // If a visible object is added to a new stage when it had no
                // previous stage, ensure the shown event is dispatched.
                setStage(newStage);
                if (isVisibleOnStage())
                    propagateShown(this);
            }
        } else if (newStage == null) {
            // If a visible object is removed from a stage when it had a
            // previous stage, ensure the hidden event is dispatched.
            setStage(newStage);
            if (!isVisibleOnStage())
                propagateHidden(this);
        }

        if (newParent != null) {
            newParent.treeListeners.notifyListeners(
                new DrawableEvent(newParent, this),
                DrawableTreeListener::drawableChildAdded
            );
        }
    } // setParent( Drawable value )

    /**
     * Sets a flag indicating that the validate() method should be called before
     * the next paint() call is made.
     * 
     * This flag is only set internally by the Drawable class when a new child
     * is added or removed.  Other than that, it's left up to the subclasses to
     * set the flag according to their needs.
     */
    public void invalidate() {
        if (!validating)
            invalidated = true;
    } // invalidate()

    /**
     * Determines whether the validate() method will be called before the next
     * paint() call is made.
     * 
     * @return  {@true} if the validate() method will be called before the next
     *          paint() call is made, {@code false} otherwise.
     */
    public boolean isInvalidated() {
        return invalidated;
    } // isInvalidated()

    /**
     * Whether this object can acquire the input focus or not.
     * 
     * @return  {@code true} if this object can acquire the input focus,
     *          {@code false} otherwise.
     */
    public boolean isFocusable() {
        return focusable;
    } // isFocusable()

    /**
     * Sets whether this object can acquire the input focus or not.
     * 
     * <p>If this object is currently focused, and a value of {@code false} is
     * passed in, the focus will be removed.
     * 
     * @param   value   {@code true} if this object can acquire the input focus,
     *                  {@code false} otherwise.
     */
    public void setFocusable( boolean value ) {
        if (focusable == value)
            return;

        focusable = value;

        // Remove the focus from this object if focusable was set to false
        // and this object is currently focused.
        FocusManager focusManager = getFocusManager();
        if (!value && (focusManager != null) && (focusManager.getCurrentFocus() == this)) {
            focusManager.setFocus(null, false);
        }
    } // setFocusable( boolean value )

    /**
     * Retrieves the tab index assigned to the object.
     * 
     * <p>Only focusable objects participate in the tabbing order.
     * 
     * <p>An object with a negative tab index does not participate in the
     * tabbing order, even if it is focusable.
     * 
     * <p>All objects with a tab index of 0 are ordered after elements
     * with a positive tab index using their relative order in the display
     * list tree.
     * 
     * <p>Objects with the same tab index are ordered first by their tab
     * index, then relative to each other by their position in the display
     * list tree.
     * 
     * @return  The tab index assigned to the object.
     */
    public int getTabIndex() {
        return tabIndex;
    } // getTabIndex()

    /**
     * Sets a new tab index for the object.
     * 
     * <p>Only focusable objects participate in the tabbing order.
     * 
     * <p>An object with a negative tab index does not participate in the
     * tabbing order, even if it is focusable.
     * 
     * <p>All objects with a tab index of 0 are ordered after elements
     * with a positive tab index using their relative order in the display
     * list tree.
     * 
     * <p>Objects with the same tab index are ordered first by their tab
     * index, then relative to each other by their position in the display
     * list tree.
     * 
     * @param   value   The new tab index value.
     */
    public void setTabIndex(int value) {
        tabIndex = value;
    } // setTabIndex()

// TODO: Deal with focus when the object is not visible.

    /**
     * Attempts to set the focus to this object, if the object is focusable.
     *
     * TODO: Is this properly checking focusable??
     * TODO: What can prevent the change?  An event handler?  Does that work as expected?
     */
    public void setFocus() {
        FocusManager focusManager = getFocusManager();
        if (focusManager != null) {
            focusManager.setFocus(this, false);
        } else {
            throw new IllegalStateException("Cannot focus objects that are not attached to a root stage");
        }
    } // setFocus()

    /**
     * Whether this object is visible and associated with a visible Stage.
     * 
     * @return  {@code true} if this object is visible and associated with a
     *          visible Stage, {@code false} otherwise.
     */
    public boolean isVisibleOnStage() {
        if (stage == null)
            return false;

        Drawable o = this;

        while (o != null) {
            if (!o.isVisible())
                return false;

            o = o.getParent();
        } // while

        return true;
    } // isVisibleOnStage()

    /**
     * Whether this object's visible flag is set.
     * 
     * @return  {@code true} if this object's visible flag is set, {@code false}
     *          otherwise.
     */
    public boolean isVisible() {
        return visible;
    } // isVisible()

    /**
     * Updates the object's visible flag.
     * 
     * <p>If the object is made visible, and it's {@code isVisibleOnStage()} method returns {@code true}, 
     * 
     * @param   value   {@code true} to set the object's visible flag and
     *                  indicate the object should be rendered if it's parent
     *                  is rendered, {@code false} to indicate this object is
     *                  not visible and should not be rendered.
     */
    public synchronized void setVisible( boolean value ) {
        if (value == visible)
            return;

        visible = value;

        if (stage != null) {
            if (value)
                propagateShown(this);
            else
                propagateHidden(this);
        }
    } // setVisible( boolean value )

    /**
     * Propagates calls to the {@code drawableShown()} method of any
     * {@link DrawableListener}s associated with the specified object or any
     * visible children.
     * 
     * <p>It is the responsibility of the caller to determine whether the
     * object's visibility actually changed.
     *
     * @param   obj The object whose visibility changed.
     */
    private void propagateShown(Drawable obj) {
        DrawableEvent e = new DrawableEvent(obj);

        // Shown events don't care if stopPropagation is called on them...
        // The events always propagate up the tree

        obj.listeners.notifyListeners(
            e,
            DrawableListener::drawableShown
        );

        List<Drawable> children = obj.drawable;
        if (children != null) {
            for (Drawable child : children) {
                if (child.isVisible()) {
                    propagateShown(child);
                }
            } // for
        }
    } // propagateShown(Drawable obj)

    /**
     * Propagates calls to the {@code drawableHidden()} method of any
     * {@link DrawableListener}s associated with the specified object or any
     * visible children.
     * 
     * <p>It is the responsibility of the caller to determine whether the
     * object's visibility actually changed.
     *
     * @param   obj The object whose visibility changed.
     */
    private void propagateHidden(Drawable obj) {
        DrawableEvent e = new DrawableEvent(obj);

        // Hidden events don't care if stopPropagation is called on them...
        // The events always propagate up the tree

        obj.listeners.notifyListeners(
            e,
            DrawableListener::drawableHidden
        );

        List<Drawable> children = obj.drawable;
        if (children != null) {
            for (Drawable child : children) {
                if (child.isVisible()) {
                    propagateHidden(child);
                }
            } // for
        }
    } // propagateHidden(Drawable obj)

    /**
     * Retrieves whether to clip child objects to this object's bounds or not.
     * 
     * @return  {@code true} if child objects should be clipped to this
     *          object's bounds, {@code false} otherwise.
     */
    public boolean getClipChildren() {
        return clipChildren;
    } // getClipChildren()
    
    /**
     * Sets whether to clip child objects to this object's bounds or not.
     * 
     * @param   value   {@code true} if child objects should be clipped to this
     *                  object's bounds, {@code false} otherwise.
     */
    public void setClipChildren( boolean value ) {
        clipChildren = value;
    } // setClipChildren( boolean value )

    /**
     * Moves the object to a new position within the parent's coordinate space.
     * 
     * <p>If the object's position is updated by this call, this method will
     * invoked the {@code drawableMoved} method of any {@link DrawableListener}s
     * associated with this object.
     * 
     * @param   newX    The new horizontal position in the parent's coordinate
     *                  space.
     * @param   newY    The new vertical position in the parent's coordinate
     *                  space.
     */
    public synchronized void setPosition( double newX, double newY ) {
        if ((x == newX) && (y == newY))
            return;

        x = newX;
        y = newY;
        transform = null;

        listeners.notifyListeners(
            new DrawableEvent(this),
            DrawableListener::drawableMoved
        );
    } // setPosition( double x, double y )

    /**
     * Moves the object to a new position relative to it's current position
     * within the parent's coordinate space.
     * 
     * <p>If the object's position is updated by this call, this method will
     * invoked the {@code drawableMoved} method of any {@link DrawableListener}s
     * associated with this object.
     * 
     * @param   tx  The amount to translate the object's horizontal position in
     *              the parent's coordinate space.
     * @param   ty  The amount to translate the object's vertical position in
     *              the parent's coordinate space.
     */
    public synchronized void translate( double tx, double ty ) {
        setPosition(x + tx, y + ty);
    } // translate( double tx, double ty )

    /**
     * Retrieves the horizontal position of this object, in the parent's
     * coordinate system.
     * 
     * <p>This coordinate represents the horizontal anchor of origin of the
     * object's local coordinate system.
     * 
     * @return  The horizontal position of this object, in the parent's
     *          coordinate system.
     */
    public double getX() {
        if (invalidated)
            validateDrawable();
        return x;
    } // getX()

    /**
     * Updates the horizontal position of this object, in the parent's
     * coordinate system.
     * 
     * <p>If the object's position is updated by this call, this method will
     * invoked the {@code drawableMoved} method of any {@link DrawableListener}s
     * associated with this object.
     * 
     * @param   value   The new horizontal position of this object, in the
     *                  parent's coordinate system.  This coordinate represents
     *                  the horizontal anchor of origin of the object's local
     *                  coordinate system.
     */
    public void setX( double value ) {
        setPosition(value, y);
    } // setX( double value )

    /**
     * Retrieves the vertical position of this object, in the parent's
     * coordinate system.
     * 
     * <p>This coordinate represents the vertical anchor of origin of the
     * object's local coordinate system.
     * 
     * @return  The vertical position of this object, in the parent's
     *          coordinate system.
     */
    public double getY() {
        if (invalidated)
            validateDrawable();
        return y;
    } // getY()

    /**
     * Updates the vertical position of this object, in the parent's
     * coordinate system.
     * 
     * <p>If the object's position is updated by this call, this method will
     * invoked the {@code drawableMoved} method of any {@link DrawableListener}s
     * associated with this object.
     * 
     * @param   value   The new vertical position of this object, in the
     *                  parent's coordinate system.  This coordinate represents
     *                  the vertical anchor of origin of the object's local
     *                  coordinate system.
     */
    public void setY( double value ) {
        setPosition(x, value);
    } // setY( double value )

// TODO: Document
    public double getCenterX() {
        if (invalidated)
            validateDrawable();
        return centerX;
    } // getCenterX()

// TODO: Document
    public synchronized void setCenterX( double value ) {
        setCenter( value, centerY );
    } // setCenterX( double value )

// TODO: Document
    public double getCenterY() {
        if (invalidated)
            validateDrawable();
        return centerY;
    } // getCenterY()

// TODO: Document
    public synchronized void setCenterY( double value ) {
        setCenter( centerX, value );
    } // setCenterY( double value )

// TODO: Document
    public synchronized void setCenter( double newX, double newY ) {
        centerX = newX;
        centerY = newY;
        transform = null;
    } // setCenter( double newX, double newY )

// TODO: Document
    public Point2D getCenter() {
        if (invalidated)
            validateDrawable();
        return new Point2D.Double( centerX, centerY );
    } // getCenter()

    /**
     * Retrieves the global opacity to apply when rendering this object.
     * 
     * @return  The global opacity to apply when rendering this object, in the
     *          range 0 (fully transparent) to 1 (fully opaque).
     */
    public float getOpacity() {
        return opacity;
    } // getOpacity()

    /**
     * Updates the global opacity to apply when rendering this object.
     * 
     * @param   value   The new opacity to apply when rendering this object,
     *                  in the range (fully transparent) to 1 (fully opaque).
     *                  Values less than 0 will be clamped to 0, and values
     *                  greater than 1 will be clamped to 1.
     */
    public void setOpacity( float value ) {
        if (value < 0)
            value = 0;
        else if (value > 1)
            value = 1;

        opacity = value;
    } // setOpacity( float value )

    /**
     * Retrieves whether this object can receive mouse input events or not.
     * 
     * @return  {@code true} if this object can receive mouse input events,
     *          {@code false} if mouse input events are disabled.
     */
    public boolean getMouseEnabled() {
        return mouseEnabled;
    } // getMouseEnabled()

    /**
     * Sets whether this object can receive mouse input events or not.
     * 
     * @param   value   {@code true} if this object should be sent mouse input
     *                  events, {@code false} if mouse input events should be
     *                  disabled.
     */
    public void setMouseEnabled(boolean value) {
        mouseEnabled = value;
    } // setMouseEnabled(boolean value)


    public boolean getMouseChildren() {
        return mouseChildren;
    } // getMouseChildren()


    public void setMouseChildren(boolean value) {
        mouseChildren = value;
    } // setMouseChildren(boolean value)

    /**
     * Sets the width and height of this object, in the object's own coordinate
     * system.
     * 
     * <p>If the object's size is updated by this call, this method will
     * invoked the {@code drawableResized} method of any
     * {@link DrawableListener}s associated with this object.
     * 
     * @param   w   The new height to assign to the object, in the object's
     *              own coordinate system.
     * @param   h   The new height to assign to the object, in the object's
     *              own coordinate system.
     * 
     * @throws  IllegalArgumentException if either of the {@code w} or
     *          {@code h} are less than 0.
     */
    public synchronized void setUnscaledSize( double w, double h ) {
        if ((w == unscaledWidth) && (h == unscaledHeight))
            return;

        if (w < 0)
            throw new IllegalArgumentException("The unscaled width cannot be less than 0.");

        if (h < 0)
            throw new IllegalArgumentException("The unscaled height cannot be less than 0.");

        unscaledWidth = w;
        unscaledHeight = h;

        transform = null;

        listeners.notifyListeners(
            new DrawableEvent(this),
            DrawableListener::drawableResized
        );
    } // setUnscaledSize( double w, double h )

    /**
     * Retrieves the width of this object, in the object's own coordinate system.
     * 
     * @return  The width of this object, in the object's own coordinate system.
     */
    public double getUnscaledWidth() {
        if (invalidated)
            validateDrawable();
        return unscaledWidth;
    } // getUnscaledWidth()

    /**
     * Sets the width of this object, in the object's own coordinate system.
     * 
     * <p>If the object's size is updated by this call, this method will
     * invoked the {@code drawableResized} method of any
     * {@link DrawableListener}s associated with this object.
     * 
     * @param   value   The new width to assign to the object, in the object's
     *                  own coordinate system.
     * 
     * @throws  IllegalArgumentException if the {@code value} is less than 0.
     */
    public void setUnscaledWidth( double value ) {
        setUnscaledSize( value, unscaledHeight );
    } // setUnscaledWidth( double value )

    /**
     * Retrieves the height of this object, in the object's own coordinate system.
     * 
     * @return  The height of this object, in the object's own coordinate system.
     */
    public double getUnscaledHeight() {
        if (invalidated)
            validateDrawable();
        return unscaledHeight;
    } // getUnscaledHeight()

    /**
     * Sets the height of this object, in the object's own coordinate system.
     * 
     * <p>If the object's size is updated by this call, this method will
     * invoked the {@code drawableResized} method of any
     * {@link DrawableListener}s associated with this object.
     * 
     * @param   value   The new height to assign to the object, in the object's
     *                  own coordinate system.
     * 
     * @throws  IllegalArgumentException if the {@code value} is less than 0.
     */
    public void setUnscaledHeight( double value ) {
        setUnscaledSize( unscaledWidth, value );
    } // setUnscaledHeight( double value )

    /**
     * Sets the width and height of this object, in the parent's coordinate
     * system by adjusting the {@code scaleX} and {@code scaleY} factors.
     * 
     * <p>If the object's size is updated by this call, this method will
     * invoked the {@code drawableResized} method of any
     * {@link DrawableListener}s associated with this object.
     * 
     * @param   w   The new height to assign to the object, in the parent's
     *              coordinate system.
     * @param   h   The new height to assign to the object, in the parent's
     *              coordinate system.
     */
    public synchronized void setSize( double w, double h ) {
        if ( (w == getWidth()) && (h == getHeight()) )
            return;

        if (unscaledWidth == 0)
            scaleX = 0;
        else
            scaleX = w / unscaledWidth;

        if (unscaledHeight == 0)
            scaleY = 0;
        else
            scaleY = h / unscaledHeight;

        transform = null;

        listeners.notifyListeners(
            new DrawableEvent(this),
            DrawableListener::drawableResized
        );
    } // setSize( double w, double h )

    /**
     * Returns the width of the object, in the parent's coordinate system.
     * 
     * @return  The width of the object, in the parent's coordinate system.
     */
    public synchronized double getWidth() {
        if (invalidated)
            validateDrawable();
        return unscaledWidth * scaleX;
    } // getWidth()

    /**
     * Sets the width of the object, in the parent's coordinate system.
     * 
     * <p>If the object's size is updated by this call, this method will
     * invoked the {@code drawableResized} method of any
     * {@link DrawableListener}s associated with this object.
     * 
     * @param   value   The new width for the object, in the parent's
     *                  coordinate system.
     */
    public void setWidth( double value ) {
        setSize( value, getHeight() );
    } // setWidth( double value )

    /**
     * Returns the height of the object, in the parent's coordinate system.
     * 
     * @return  The height of the object, in the parent's coordinate system.
     */
    public synchronized double getHeight() {
        if (invalidated)
            validateDrawable();
        return unscaledHeight * scaleY;
    } // getHeight()

    /**
     * Sets the height of the object, in the parent's coordinate system.
     * 
     * <p>If the object's size is updated by this call, this method will
     * invoked the {@code drawableResized} method of any
     * {@link DrawableListener}s associated with this object.
     * 
     * @param   value   The new height for the object, in the parent's
     *                  coordinate system.
     */
    public void setHeight( double value ) {
        setSize( getWidth(), value );
    } // setHeight( double value )


    public synchronized void setScale( double sx, double sy ) {
        if ((scaleX == sx) && (scaleY == sy))
            return;

        scaleX = sx;
        scaleY = sy;
        transform = null;

        listeners.notifyListeners(
            new DrawableEvent(this),
            DrawableListener::drawableResized
        );
    } // scale( double sx, double sy )


    public void setScale( double scale ) {
        setScale( scale, scale );
    } // setScale( double scale )


    public void scale( double sx, double sy ) {
        setScale( scaleX * sx, scaleY * sy );
    } // scale( double sx, double sy )


    public double getScaleX() {
        if (invalidated)
            validateDrawable();
        return scaleX;
    } // getScaleX()


    public void setScaleX( double value ) {
        setScale( value, scaleY );
    } // setScaleX( double value )


    public double getScaleY() {
        if (invalidated)
            validateDrawable();
        return scaleY;
    } // getScaleY()


    public void setScaleY( double value ) {
        setScale( scaleY, value );
    } // setScaleY( double value )


    public double getRotation() {
        if (invalidated)
            validateDrawable();
        return rotation;
    } // getRotation()


    public synchronized void setRotation( double value ) {
        if (rotation == value)
            return;

        rotation = value;
        transform = null;

        listeners.notifyListeners(
            new DrawableEvent(this),
            DrawableListener::drawableRotated
        );
    } // setRotation( double value )


    public void rotate( double theta ) {
        setRotation( rotation + theta );
    } // rotate( double theta )

    public List<AbstractBufferedImageOp> getFilters() {
        if (filters != null) {
            return new ArrayList<>(filters);
        } else {
            return Collections.emptyList();
        }
    } // getFilters()

    public synchronized void setFilters( ArrayList<AbstractBufferedImageOp> value ) {
        if ((value == null) || value.isEmpty())
            filters = null;
        else
            filters = new ArrayList<>(value);
    } // setFilters( ArrayList<AbstractBufferedImageOp> value )

    public synchronized void addFilter( AbstractBufferedImageOp flt ) {
        if (flt == null)
            throw new NullPointerException("The flt parameter cannot be null.");

        if (filters == null) {
            filters = new ArrayList<>();
        } else {
            filters = new ArrayList<>(filters);
        }

        filters.add(flt);
    } // addFilter( AbstractBufferedImageOp flt )

    public synchronized void removeFilter( AbstractBufferedImageOp flt ) {
        if (flt == null)
            throw new NullPointerException("The flt parameter cannot be null.");

        if (filters != null) {
            filters = new ArrayList<>(filters);

            if (filters.remove(flt) && filters.isEmpty()) {
                filters = null;
            }
        }
    } // removeFilter( AbstractBufferedImageOp flt )

    /**
     * Returns the transformation matrix applied to the animation.
     * @return  The transformation matrix applied to the animation.
     */
    public synchronized AffineTransform getTransform() {
        if (invalidated)
            validateDrawable();

        if (transform == null) {
            transform = new AffineTransform();
            //transform.translate((scaleX<0 ? centerX : -centerX), (scaleY<0 ? centerY : -centerY));
            transform.translate(-centerX * scaleX, -centerY * scaleY);
            transform.rotate(rotation);
            transform.translate(x, y);
            transform.scale( scaleX, scaleY );
        }

        return (AffineTransform) transform.clone();
    } // getTransform()


    public synchronized void addDrawable( Drawable obj ) {
        // Ensure that the obj is not 'this' and that adding the object will not create a cycle
        Drawable p = this;
        while (p != null) {
            if (p == obj)
                throw new IllegalArgumentException("A Drawable object cannot be added as a child of itself.");
            p = p.getParent();
        } // while

        if (drawable == null)
            drawable = new ArrayList<>();
        else
            drawable = new ArrayList<>(drawable);

        synchronized (obj) {
            if (obj.getParent() == this)
                drawable.remove( obj );

            drawable.add( obj );
            obj.setParent(this);
        }

        invalidate();
    } // addDrawable( Drawable obj )


    public synchronized void removeDrawable( Drawable obj ) {
        if (drawable == null)
            return;

        drawable = new ArrayList<>(drawable);

        synchronized (obj) {
            if (drawable.remove( obj ))
                obj.setParent(null);
        }

        invalidate();
    } // removeDrawable( Drawable obj )


    public synchronized void removeAllDrawables() {
        if (drawable != null) {
            for (Drawable obj : drawable) {
                obj.setParent(null);
            }
            drawable = null;
        }
        invalidate();
    } // removeAllDrawables()

    /**
     * Retrieves the number of direct children of this object.
     * 
     * @return  The number of direct children of this object.
     */
    public int getNumDrawables() {
        List<Drawable> d = drawable;
        return (d == null)
                ? 0
                : d.size();
    } // getNumDrawables()

    /**
     * Returns the first child with the given {@code name}, or {@code null} if
     * not found or {@code name} is {@code null}.
     * 
     * @param   name    The name to search for.
     * 
     * @return  The first child with the given {@code name}, or {@code null} if
     *          not found or {@code name} is {@code null}.
     */
    public synchronized Drawable getDrawable( String name ) {
        if ((drawable == null) || (name == null))
            return null;

        for (Drawable child : drawable) {
            if (name.equals(child.getName()))
                return child;
        } // for

        return null;
    } // getDrawable( String name )


    public synchronized int getDrawableIndex( Drawable obj ) {
        return drawable.indexOf(obj);
    } // getDrawableIndex( Drawable obj )


    public synchronized int getDrawableIndex( String name ) {
        if (name == null)
            return -1;

        int idx = 0;
        for (Drawable child : drawable) {
            if (name.equals(child.getName()))
                return idx;
            ++idx;
        } // for

        return -1;
    } // getDrawableIndex( Drawable obj )


    public synchronized Drawable getDrawableAt( int index ) {
        if (drawable == null)
            return null;

        return drawable.get(index);
    } // getDrawableAt( int index )


    /**
     * This method can be overridden by subclasses so they can perform layout, etc
     * required before being rendered. 
     */
    public void validate() {
        // Base class does nothing...
    } // validate()

    /**
     * Calls the validate() method, clears the invalidated flag, and prevents the validate()
     * method from triggering another validation.
     * 
     * If you want to force validation immediately, calling this method is preferrable
     * to directly calling a validate() method directly.
     * 
     * This method will call the validate() method, regardless of whether isInvalidated()
     * returns true or not.  Be sure to check isInvalidated() first if you don't want to
     * incur a performance hit.
     */
    protected void validateDrawable() {
        validating = true;
        invalidated = false;

        validate();

        validating = false;
    } // validateDrawable()


    public synchronized void draw( Graphics2D g ) {
        if (!visible)
            return;

        // Call to getUnscaledWidth() below will in turn call validateDrawable() if needed
        double w = getUnscaledWidth();
        double h = getUnscaledHeight();

        AffineTransform m = g.getTransform();
        g.transform( getTransform() );

        Rectangle oldClip = null;
        if (clipChildren) {
            oldClip = g.getClipBounds();

            Rectangle newClip = new Rectangle( 0, 0, (int) w, (int) h);
            if (oldClip != null)
                newClip = oldClip.intersection( newClip );

            g.setClip( newClip );
        }

        Graphics2D g2;
        BufferedImage img = null;
        boolean hasSize = true;
        if (((int)w == 0) || ((int)h == 0)) {
System.out.println("no size: "+ w +", "+ h +" obj="+ this);
            hasSize = false;
        }

        if (((opacity == 1.0) && (filters == null)) || !hasSize) {
            g2 = g;
        } else {
            // Draw to a in-memory image buffer if any filters are being applied
// TODO: This doesn't take into account any change to the image size as a result of applying the filter(s)...
// TODO: When a filter or opacity is applied, children will always be clipped.  Perhaps would be nice to not do that.
            img = g.getDeviceConfiguration().createCompatibleImage((int)Math.ceil(w), (int)Math.ceil(h), Transparency.TRANSLUCENT);
            //img = new BufferedImage((int) w, (int) h,  BufferedImage.TYPE_INT_ARGB);
            g2 = img.createGraphics();
        }

        // Paint this object first
        paint(g2);

        // Now paint all children
        if (drawable != null) {
            for (Drawable d : drawable) {
                d.draw(g2);
            } // for
        }

        // Apply filter(s) if needed
        if (img != null) {
            g2.dispose();

            // Apply filters
            if (filters != null) {
                for (AbstractBufferedImageOp f : filters) {
                    img = f.filter(img, null);
                } // for
            }

            // Draw the image to the destination context
            Composite oldComposite = null;
            if (opacity < 1.0) {
                oldComposite = g.getComposite();
                g.setComposite( AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity) );
            }

            g.drawImage(img, 0, 0, null);

            if (oldComposite != null)
                g.setComposite(oldComposite);
        }

        if (clipChildren)
            g.setClip(oldClip); 

/*
g.setColor(Color.RED);
g.draw(new Rectangle2D.Double(0,0,getUnscaledWidth(),getUnscaledHeight()));
//*/

        g.setTransform(m);
    } // draw( Graphics2D g )


    public void paint( Graphics2D g ) {
        //throw new IllegalAccessError("Subclasses must implement the paint() method");
    } // paint( Graphics2D g )

    /**
     * Retrieves the listeners for DrawableEvents.
     * 
     * <p>This method serves two purposes:
     * <ol>
     * <li>It's a mechanism for dispatching events to the registered listeners.
     * <li>It provides a means to assign a default listener which is called if
     * none of the other listeners consume the event.  This is useful for
     * defining default behavior for a control.
     * </ol>
     * 
     * @return  The listeners for DrawableEvents.
     */
    protected DrawableListeners<DrawableListener> getDrawableListeners() {
        return listeners;
    } // getDrawableListeners()

    /**
     * Adds a new DrawableListener to the list of registered listeners,
     * if it is not already found in the list.
     * 
     * @param listener  The listener to add.
     * 
     * @throws  IllegalArgumentException if {@code listener} is {@code null}.
     */
    public void addDrawableListener( DrawableListener listener ) {
        listeners.add(listener);
    } // addDrawableListener( DrawableListener listener )

    /**
     * Removes a DrawableListener from the list of registered listeners, if it
     * is found in the list.
     * 
     * @param listener  The listener to remove.
     */
    public void removeDrawableListener( DrawableListener listener ) {
        listeners.remove(listener);
    } // removeDrawableListener( DrawableListener listener )

    /**
     * Retrieves the listeners for DrawableMouseEvents.
     * 
     * <p>This method serves two purposes:
     * <ol>
     * <li>It's a mechanism for dispatching events to the registered listeners.
     * <li>It provides a means to assign a default listener which is called if
     * none of the other listeners consume the event.  This is useful for
     * defining default behavior for a control.
     * </ol>
     * 
     * @return  The listeners for DrawableMouseEvents.
     */
    protected DrawableListeners<DrawableMouseListener> getMouseListeners() {
        return mouseListeners;
    } // getMouseListeners()

    /**
     * Adds a new DrawableMouseListener to the list of registered listeners,
     * if it is not already found in the list.
     * 
     * @param listener  The listener to add.
     * 
     * @throws  IllegalArgumentException if {@code listener} is {@code null}.
     */
    public void addMouseListener( DrawableMouseListener listener ) {
        mouseListeners.add(listener);
    } // addMouseListener( DrawableMouseListener listener )

    /**
     * Removes a DrawableMouseListener from the list of registered listeners,
     * if it is found in the list.
     * 
     * @param listener  The listener to remove.
     */
    public void removeMouseListener( DrawableMouseListener listener ) {
        mouseListeners.remove(listener);
    } // removeMouseListener( DrawableMouseListener listener )

    /**
     * Retrieves the listeners for DrawableKeyEvents.
     * 
     * <p>This method serves two purposes:
     * <ol>
     * <li>It's a mechanism for dispatching events to the registered listeners.
     * <li>It provides a means to assign a default listener which is called if
     * none of the other listeners consume the event.  This is useful for
     * defining default behavior for a control.
     * </ol>
     * 
     * @return  The listeners for DrawableKeyEvents.
     */
    protected DrawableListeners<DrawableKeyListener> getKeyListeners() {
        return keyListeners;
    } // getKeyListeners()

    /**
     * Adds a new DrawableKeyListener to the list of registered listeners,
     * if it is not already found in the list.
     * 
     * @param listener  The listener to add.
     * 
     * @throws  IllegalArgumentException if {@code listener} is {@code null}.
     */
    public void addKeyListener( DrawableKeyListener listener ) {
        keyListeners.add(listener);
    } // addKeyListener( DrawableKeyListener listener )

    /**
     * Removes a DrawableKeyListener from the list of registered listeners, if
     * it is found in the list.
     * 
     * @param listener  The listener to remove.
     */
    public void removeKeyListener( DrawableKeyListener listener ) {
        keyListeners.remove(listener);
    } // removeKeyListener( DrawableKeyListener listener )

    /**
     * Retrieves the listeners for DrawableFocusEvents.
     * 
     * <p>This method serves two purposes:
     * <ol>
     * <li>It's a mechanism for dispatching events to the registered listeners.
     * <li>It provides a means to assign a default listener which is called if
     * none of the other listeners consume the event.  This is useful for
     * defining default behavior for a control.
     * </ol>
     * 
     * @return  The listeners for DrawableFocusEvents.
     */
    protected DrawableListeners<DrawableFocusListener> getFocusListeners() {
        return focusListeners;
    } // getFocusListeners()

    /**
     * Adds a new DrawableFocusListener to the list of registered listeners,
     * if it is not already found in the list.
     * 
     * @param listener  The listener to add.
     * 
     * @throws  IllegalArgumentException if {@code listener} is {@code null}.
     */
    public void addFocusListener( DrawableFocusListener listener ) {
        focusListeners.add(listener);
    } // addFocusListener( DrawableFocusListener listener )

    /**
     * Removes a DrawableFocusListener from the list of registered listeners,
     * if it is found in the list.
     * 
     * @param listener  The listener to remove.
     */
    public void removeFocusListener( DrawableFocusListener listener ) {
        focusListeners.remove(listener);
    } // removeFocusListener( DrawableFocusListener listener )

    /**
     * Retrieves the listeners for DrawableTreeEvents.
     * 
     * <p>This method serves two purposes:
     * <ol>
     * <li>It's a mechanism for dispatching events to the registered listeners.
     * <li>It provides a means to assign a default listener which is called if
     * none of the other listeners consume the event.  This is useful for
     * defining default behavior for a control.
     * </ol>
     * 
     * @return  The listeners for DrawableTreeEvents.
     */
    protected DrawableListeners<DrawableTreeListener> getTreeListeners() {
        return treeListeners;
    } // getTreeListeners()

    /**
     * Adds a new DrawableTreeListener to the list of registered listeners,
     * if it is not already found in the list.
     * 
     * @param listener  The listener to add.
     * 
     * @throws  IllegalArgumentException if {@code listener} is {@code null}.
     */
    public void addTreeListener( DrawableTreeListener listener ) {
        treeListeners.add(listener);
    } // addTreeListener( DrawableTreeListener listener )

    /**
     * Removes a DrawableTreeListener from the list of registered listeners, if
     * it is found in the list.
     * 
     * @param listener  The listener to remove.
     */
    public void removeTreeListener( DrawableTreeListener listener ) {
        treeListeners.remove(listener);
    } // removeTreeListener( DrawableTreeListener listener )

    /**
     * Retrieves the bounds of this object in the parents's coordinate system.
     * 
     * @return  The bounds of this object in the parent's coordinate system.
     */
    public synchronized Rectangle2D getBounds() {
        return new Rectangle2D.Double( getX(), getY(), getWidth(), getHeight() );
    } // getBounds()

    /**
     * Retrieves the bounds of this object in the object's local coordinate
     * system.
     * 
     * @return  The bounds of this object in the object's local coordinate
     *          system.
     */
    public synchronized Rectangle2D getUnscaledBounds() {
        return new Rectangle2D.Double( 0, 0, getUnscaledWidth(), getUnscaledHeight() );
    } // getUnscaledBounds()

    /**
     * Determines whether the specified point intersects the object or not.
     * 
     * <p>The default implementation only checks whether the point is inside the
     * bounding rectangle.</p>
     * 
     * <p>Subclasses may want to override this method to implement more
     * interesting intersection methods, such as testing whether the point is
     * over an opaque pixel.</p>
     * 
     * @param point The point to test, in the object's coordinate system.
     * 
     * @return Whether the specified point intersects the object or not.
     */
    public synchronized boolean pointIntersects( Point2D point ) {
        return new Rectangle2D.Double( 0, 0, getUnscaledWidth(), getUnscaledHeight() ).contains(point);
    } // pointIntersects( Point2D point )

    /**
     * Returns the coordinate transformation from the top level of the display
     * list to this object's coordinate system.
     * 
     * @return  The coordinate transformation from the top level of the display
     *          list to this object's coordinate system.
     */
    public synchronized AffineTransform getConcatenatedTransform() {
        AffineTransform rv;

        if (parent != null) {
            rv = parent.getConcatenatedTransform();
            rv.concatenate(getTransform());
        } else
            rv = getTransform();

        return rv;
    } // getConcatenatedTransform()

    public synchronized Point2D localToGlobal( Point2D pt ) {
        return getConcatenatedTransform().transform(pt, null);
    } // localToGlobal( Point2D pt )


    public synchronized Point2D globalToLocal( Point2D pt ) {
        if (parent != null)
            pt = parent.globalToLocal(pt);
        return getTransform().transform(pt, null);
    } // globalToLocal( Point2D pt )


    private static long clickTimeout = 350;
    private static Drawable lastClickObj;
    private static long lastClickTime = 0;
    private static int lastClickButton = 0;
    private static int lastClickCount = 0;


    static int countClicks( Drawable o, int eventID, long time, int button ) {
//System.out.println("countClicks: eventID="+ eventID +"  time="+ time +"  button="+ button +"  o="+ o);
        if (button != DrawableMouseEvent.NOBUTTON) {
            switch (eventID) {
                case DrawableMouseEvent.MOUSE_PRESSED:
//System.out.println("  Pressed: lastTime="+ lastClickTime +" ("+ (time - lastClickTime) +")  lastButton="+ lastClickButton +"  lastObj="+ lastClickObj +"  lastCount="+ lastClickCount);
                    if ((lastClickButton != button) || (time - lastClickTime > clickTimeout) || (lastClickObj != o)) {
                        lastClickObj = o;
                        lastClickButton = button;
                        lastClickCount = 0;
                    }
                    lastClickTime = time;
                    return lastClickCount;

                case DrawableMouseEvent.MOUSE_RELEASED:
//System.out.println("  Released: lastTime="+ lastClickTime +" ("+ (time - lastClickTime) +")  lastButton="+ lastClickButton +"  lastObj="+ lastClickObj +"  lastCount="+ lastClickCount);
                    if ((lastClickButton == button) && (lastClickObj == o)) {
                        if (time - lastClickTime <= clickTimeout)
                            ++lastClickCount;
                        else
                            lastClickCount = 1;
                    } else {
                        lastClickCount = 0;
                        lastClickObj = null;
                    }
                    lastClickTime = time;
                    return lastClickCount;
            } // switch
        }

        return 0;
    } // countClicks( Drawable o, int eventID, long time, int button )


    private static Drawable lastEnterLeaveObj;

    static void dispatchEnterLeaveEvents( Drawable o, MouseEvent e ) {
        if (o == lastEnterLeaveObj)
            return;

        Point2D pt = new Point2D.Double(e.getX(), e.getY());
        Point2D pt2;

        if ((lastEnterLeaveObj != null) && (lastEnterLeaveObj.mouseListeners != null)) {
            try {
                pt2 = lastEnterLeaveObj.getTransform().inverseTransform(pt, null);
            } catch (NoninvertibleTransformException ex) {
                pt2 = new Point2D.Double(0, 0);
            }

            lastEnterLeaveObj.mouseListeners.notifyListeners(
                new DrawableMouseEvent(
                    lastEnterLeaveObj,
                    DrawableMouseEvent.MOUSE_EXITED,
                    e.getWhen(), e.getModifiersEx(),
                    pt2.getX(), pt2.getY(),
                    e.getXOnScreen(), e.getYOnScreen(),
                    e.getClickCount(),
                    e.getButton()
                ),
                DrawableMouseListener::drawableMouseExited
            );
        }

        lastEnterLeaveObj = o;

        if ((lastEnterLeaveObj != null) && (lastEnterLeaveObj.mouseListeners != null)) {
            try {
                pt2 = lastEnterLeaveObj.getTransform().inverseTransform(pt, null);
            } catch (NoninvertibleTransformException ex) {
                pt2 = new Point2D.Double(0, 0);
            }

            lastEnterLeaveObj.mouseListeners.notifyListeners(
                new DrawableMouseEvent(
                    lastEnterLeaveObj,
                    DrawableMouseEvent.MOUSE_ENTERED,
                    e.getWhen(), e.getModifiersEx(),
                    pt2.getX(), pt2.getY(),
                    e.getXOnScreen(), e.getYOnScreen(),
                    e.getClickCount(),
                    e.getButton()
                ),
                DrawableMouseListener::drawableMouseEntered
            );
        }
    } // dispatchEnterLeaveEvents( MouseEvent e, Point2D pt )

    /**
     * Processes an AWT MouseEvent.
     * 
     * This method exists to allow an AWT/Swing component to pass on MouseEvents
     * for processing.  Currently, it is used by {@link DrawablePanel}.
     * 
     * @param pt            The mouse position within the target object (stage).
     * @param evt           The AWT MouseEvent that was received.
     * 
     * @return  An array of DrawableMouseEvent objects (null if the event has
     *          not yet been dispatched to a Drawable).  Can return multiple
     *          events in the case of click events being triggered by a mouse
     *          release event.
     */
    synchronized DrawableMouseEvent[] processMouseEvent( Point2D pt, MouseEvent evt ) {
        if (!isVisible())
            return null;

        DrawableMouseEvent rv[];

        // Process child objects first (depth-first to ensure the object drawn on top gets the event first)
        List<Drawable> children = drawable;
        if (mouseChildren && (children != null)) {
            // Check each child (in reverse order) to see if it wants the mouse event
            // Reverse order ensures the object drawn last gets the event first
            for (int c = children.size() - 1; c >= 0; --c) {
                Drawable d = children.get(c);
                Point2D p2;

                try {
                    p2 = d.getTransform().inverseTransform(pt, null);
                } catch (NoninvertibleTransformException | NullPointerException ex) {
                    //Logger.getLogger(Drawable.class.getName()).log(Level.SEVERE, null, ex);
                    continue;
                }
                //Logger.getLogger(Drawable.class.getName()).log(Level.SEVERE, null, ex);

                rv = d.processMouseEvent( p2, evt );
                if (rv != null) {
                    // Bubble the event(s)...
                    if (mouseListeners != null) {
                        int removed = 0;

                        for (int i = 0; i < rv.length; ++i) {
                            BiConsumer<DrawableMouseListener, DrawableMouseEvent> callback = null;

                            switch (rv[i].getID()) {
                                case DrawableMouseEvent.MOUSE_MOVED:
                                    callback = DrawableMouseListener::drawableMouseMoved;
                                    break;

                                case DrawableMouseEvent.MOUSE_DRAGGED:
                                    callback = DrawableMouseListener::drawableMouseDragged;
                                    break;

                                case DrawableMouseEvent.MOUSE_PRESSED:
                                    callback = DrawableMouseListener::drawableMousePressed;
                                    break;

                                case DrawableMouseEvent.MOUSE_RELEASED:
                                    callback = DrawableMouseListener::drawableMouseReleased;
                                    break;

                                case DrawableMouseEvent.MOUSE_CLICKED:
                                    callback = DrawableMouseListener::drawableMouseClicked;
                                    break;
                            } // switch

                            if (callback != null) {
                                mouseListeners.notifyListeners(rv[i], callback);
                            }

                            if (rv[i].isPropagationStopped()) {
                                rv[i] = null;   // Don't return event to caller
                                ++removed;
                            }
                        } // for

                        // Remove events with propagation stopped
                        if (rv.length == removed) {
                            rv = new DrawableMouseEvent[0];
                        } else if (removed > 0) {
                            DrawableMouseEvent t[] = new DrawableMouseEvent[rv.length - removed];

                            int p = 0;
                            for (int i = 0; i < rv.length; ++i) {
                                if (rv[i] != null) {
                                    t[p] = rv[i];
                                    ++p;
                                }
                            } // for

                            rv = t;
                        }
                    }

                    return rv;
                }
            } // for
        }

        if (mouseEnabled && pointIntersects(pt)) {
            int eventID = evt.getID();

            // Send event
            DrawableMouseEvent e = new DrawableMouseEvent(
                                        this, eventID, evt.getWhen(), evt.getModifiersEx(),
                                        pt.getX(), pt.getY(),
                                        evt.getXOnScreen(), evt.getYOnScreen(),
                                        0,
                                        evt.getButton()
                                    );
            DrawableMouseEvent e2 = null;

            switch (eventID) {
                case MouseEvent.MOUSE_MOVED:
                    dispatchEnterLeaveEvents(this, evt);
                    mouseListeners.notifyListeners(
                        e,
                        DrawableMouseListener::drawableMouseMoved
                    );
                    break;

                case MouseEvent.MOUSE_DRAGGED:
                    dispatchEnterLeaveEvents(this, evt);
                    mouseListeners.notifyListeners(
                        e,
                        DrawableMouseListener::drawableMouseDragged
                    );
                    break;

                case MouseEvent.MOUSE_PRESSED:
                    countClicks(this, evt.getID(), evt.getWhen(), evt.getButton());
                    mouseListeners.notifyListeners(
                        e,
                        DrawableMouseListener::drawableMousePressed
                    );
                    break;

                case MouseEvent.MOUSE_RELEASED:
                    mouseListeners.notifyListeners(
                        e,
                        DrawableMouseListener::drawableMouseReleased
                    );

                    int clickCount = countClicks(this, evt.getID(), evt.getWhen(), evt.getButton());
                    if (clickCount > 0) {
                        e2 = new DrawableMouseEvent(
                                        this, DrawableMouseEvent.MOUSE_CLICKED, evt.getWhen(), evt.getModifiersEx(),
                                        pt.getX(), pt.getY(),
                                        evt.getXOnScreen(), evt.getYOnScreen(),
                                        clickCount,
                                        evt.getButton()
                                    );

                        mouseListeners.notifyListeners(
                            e2,
                            DrawableMouseListener::drawableMouseClicked
                        );
                    }
                    break;
            } // switch

            if (!e.isPropagationStopped()) {
                if ((e2 != null) && (!e2.isPropagationStopped()))
                    rv = new DrawableMouseEvent[] { e, e2 };
                else
                    rv = new DrawableMouseEvent[] { e };
            } else if ((e2 != null) && (!e2.isPropagationStopped()))
                rv = new DrawableMouseEvent[] { e2 };
            else // Still need to return a non-null list here or the event bubbling won't be correct.
                rv = new DrawableMouseEvent[] { };

            return rv;
        }

        return null;
    } // processMouseEvent( Point2D pt, MouseEvent evt, boolean bubbleEvents )

    /**
     * Processes an AWT KeyEvent.
     * 
     * This method exists to allow an AWT/Swing component to pass on KeyEvents
     * for processing.  Currently, it is used by {@link DrawablePanel}.
     * 
     * @param evt           The AWT KeyEvent that was received.
     */
    void processKeyEvent( KeyEvent evt ) {
        if (!isVisible())
            return;

        // Dispatch the key event to the object with the focus
        Drawable focus = FocusManager.getCurrentFocus(this);
        if (focus == null)
            focus = this.getRoot();

        if (focus != null) {
            BiConsumer<DrawableKeyListener, DrawableKeyEvent> callback = null;

            switch (evt.getID()) {
                case KeyEvent.KEY_PRESSED:
                    callback = DrawableKeyListener::drawableKeyPressed;
                    break;

                case KeyEvent.KEY_RELEASED:
                    callback = DrawableKeyListener::drawableKeyReleased;
                    break;

                case KeyEvent.KEY_TYPED:
                    callback = DrawableKeyListener::drawableKeyTyped;
                    break;
            } // switch

            if (callback != null) {
                notifyListenersAndBubble(
                    focus,
                    (item) -> { return item.keyListeners; },
                    new DrawableKeyEvent(
                        focus,
                        evt.getID(), evt.getWhen(), evt.getModifiersEx(),
                        evt.getKeyCode(), evt.getKeyChar(), evt.getKeyLocation()
                    ),
                    callback
                );
            }
        }
    } // processKeyEvent( KeyEvent evt )

    /**
     * Bubbles a DrawableFocusEvent up through the display list.
     * 
     * @param   e   The event to process.
     */
    void processFocusEvent(DrawableFocusEvent e) {
        BiConsumer<DrawableFocusListener, DrawableFocusEvent> callback = null;

        switch (e.getID()) {
            case DrawableFocusEvent.FOCUS_LOST:
                callback = DrawableFocusListener::drawableFocusLost;
                break;

            case DrawableFocusEvent.FOCUS_GAINED:
                callback = DrawableFocusListener::drawableFocusGained;
                break;
        } // switch

        if (callback != null) {
            notifyListenersAndBubble(
                this,
                (item) -> { return item.focusListeners; },
                e,
                callback
            );
        }
    } // processFocusEvent( DrawableFocusEvent e )

    /**
     * Notifies listeners of an event, propagating the event up through the
     * display list tree starting at the given object.
     * 
     * <p>The notification will stop before notifying all listeners if one of
     * the {@code stopPropagation()} or {@code stopImmediatePropagation()}
     * methods of the event instance is called by previously invoked listener.
     * 
     * @param <T>       The type of the listeners List.
     * @param <E>       The type of the DrawableEvent to dispatch.
     * @param obj       The object at which to start dispatching the event.
     * @param listenersFunction Function which determines the appropriate
     *                  listeners to notifyListeners for each object processed.
     * @param e         Event to send to each listener.
     * @param callback  Callback to invoke for each listener which will call
     *                  the appropriate method for the particular event.
     */
    protected static <T, E extends DrawableEvent> void notifyListenersAndBubble(
        Drawable obj,
        Function<Drawable, DrawableListeners<T>> listenersFunction,
        E e,
        BiConsumer<T, E> callback
    ) {
        while ((obj != null) && !e.isPropagationStopped()) {
            listenersFunction.apply(obj).notifyListeners(
                e,
                callback
            );

            obj = obj.getParent();
        }
    } // notifyListenersAndBubble(...)

    /**
     * Traverses the subtree rooted at this object and returns a list of
     * all descendants matching the specified predicates.
     * 
     * <p>The tree is traversed in an order that ensures matching container
     * elements are added to the list before their children.
     * 
     * <p>The object this method is invoked on will not be added to the
     * returned list.  Only descendants of this object will be in the returned
     * list.
     * 
     * @param parentPredicate   Predicate to apply to parent objects to
     *                          determine whether their children should be
     *                          traversed.  If {@code null}, all children will
     *                          be considered.
     * @param childPredicate    Predicate to apply to child objects to
     *                          determine whether they should be added to
     *                          the {@code matched} list.  If (@code null),
     *                          all children of scanned parents will be
     *                          matched.
     * 
     * @return  A list of all descendants matching the specified predicates,
     *          in the order in which they are encountered in the display list
     *          tree.
     * 
     * @throws  NullPointerException if either of the predicates are
     *          {@code null}.
     */
    public List<Drawable> getMatchingDescendants(
        final Predicate<Drawable> parentPredicate,
        final Predicate<Drawable> childPredicate
    ) {
        final List<Drawable> matched = new ArrayList<>();

        // A single element array is used here so the local can be final,
        // but the first element can be reassigned to the lambda below.
        // This is a trick to allow recursion within the lambda expression,
        // since Java lambdas don't directly support recursion.
        @SuppressWarnings({ "unchecked", "rawtypes" })
        final Consumer<Drawable> treeWalker[] = new Consumer[1];

        treeWalker[0] =
            (obj) -> {
                if ((parentPredicate == null) || parentPredicate.test(obj)) {
//System.out.println("getMatchingDescendants: parent predicate matched: "+ obj);
                    List<Drawable> children = obj.drawable;
                    if (children != null) {
                        for (Drawable child : children) {
                            if ((childPredicate == null) || childPredicate.test(child)) {
//System.out.println("getMatchingDescendants: child predicate matched: "+ child);
                                matched.add(child);
                            }

                            treeWalker[0].accept(child);
                        }
                    }
                }
            }; // treeWalker

        treeWalker[0].accept(this);

        return matched;
    } // getMatchingDescendants()

    /**
     * Retrieves a list of all focusable descendants regardless of whether they
     * participate in the tabbing order or not.
     * 
     * <p>A descendant can participate in the tabbing order only if the
     * following conditions are true:
     * 
     * <ol>
     * <li>The object's {@code focusable} property is {@code true}.
     * <li>The object is visible (as are all of the containing objects between
     *      this container and the object being tested).
     * </ol>
     * 
     * @return  A list of all focusable descendants regardless of whether they
     *          participate in the tabbing order or not, in the order in which
     *          they are encountered in the display list tree.
     */
    public List<Drawable> getFocusableDescendants() {
        // Traverse the display list tree and retrieve matching elements
        return getMatchingDescendants(
            // Ensure all parent/ancestor nodes are visible
            (parentNode) -> {
                return parentNode.isVisible();
            },
            // Return descendants that are visible and focusable
            (childNode) -> {
                return childNode.isFocusable() && childNode.isVisible();
            }
        );
    } // getFocusableDescendants()

    /**
     * Retrieves a list of all focusable descendants (as defined ) that
     * participate in the tabbing order, ordered by their {@code tabIndex} values.
     * 
     * <p>An object is tabbable if it is focusable as defined by
     * {@code getFocusableDescendants()} and the object's {@code tabIndex}
     * property is 0 or greater.
     * 
     * <p>Objects with a {@code tabIndex} of 0 are ordered after elements
     * with a positive {@code tabIndex} using their relative order in the
     * display list tree.
     * 
     * <p>Objects with the same {@code tabIndex} value are ordered first by
     * {@code tabIndex}, then relative to each other by their position in the
     * display list tree.
     * 
     * @return  A list of all tabbable descendants which participate in the
     *          tabbing order, ordered by their effective {@code tabIndex}
     *          values.
     */
    public List<Drawable> getTabbableDescendants() {
        List<Drawable> matched = getFocusableDescendants();

        // Filter out elements that are not tabbable
        matched.removeIf((item) -> { return (item.getTabIndex() < 0); });

        // Sort by tabIndex
        matched.sort((o1, o2) -> {
            int o1ti = o1.getTabIndex();
            if (o1ti == 0)
                o1ti = Integer.MAX_VALUE;

            int o2ti = o2.getTabIndex();
            if (o2ti == 0)
                o2ti = Integer.MAX_VALUE;

            return (o1ti == o2ti ? 0 : (o1ti < o2ti ? -1 : 1));
        });

        return matched;
    } // getTabbableDescendants()

    private Map<String, Object> behaviorProperties = null;
    
    protected synchronized void setBehaviorProperty(String name, Object value) {
        if (behaviorProperties == null) {
            if (value == null)
                return;

            behaviorProperties = new HashMap<>();
        }

        behaviorProperties.put(name, value);
    } // setBehaviorProperty

    protected Object getBehaviorProperty(String name) {
        return getBehaviorProperty(name, Object.class, null);
    } // getBehaviorProperty

    protected synchronized <T> T getBehaviorProperty(String name, Class<T> clazz, T defaultValue) {
        T rv = null;

        if (behaviorProperties != null) {
            rv = clazz.cast(behaviorProperties.get(name));
        }
        
        if (rv == null) {
            rv = defaultValue;
        }
        
        return rv;
    } // getBehaviorProperty

} // class Drawable
