
package Fishing.drawable.events;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Base class for events dispatched within the application.
 * 
 * <p>Also contains utility methods for working with lists of event listeners.
 * 
 * @author Brad
 */
public class EventBase
    extends EventObject
{

    /**
     * Whether this event has been consumed or not.
     */
    private boolean consumed = false;

    /**
     * Whether the {@code stopPropagation()} method has been called on this
     * event instance.
     */
    private boolean propagationStopped = false;

    /**
     * Whether the {@code stopImmediatePropagation()} method has been called on
     * this event instance.
     */
    private boolean immediatePropagationStopped = false;

    /**
     * Constructs a new instance with the specified source.
     * 
     * @param   source  The object on which the Event initially occurred.
     */
    public EventBase( Object source ) {
        super(source);
    } // EventBase( Object source )

    /**
     * Determines whether the {@code consume()} method has been called on this
     * event.
     * 
     * @return  {@code true} if the {@code consume()} method has been called
     *          on this event, {@code false} otherwise.
     */
    public boolean isConsumed() {
        return consumed;
    } // isConsumed()

    /**
     * Marks this event as having been consumed, and any final default action
     * should be cancelled.
     */
    public void consume() {
        consumed = true;
    } // consume()

    /**
     * Determines whether either of the {@code stopPropagation()} or
     * {@code stopImmediatePropagation()} methods have been called on this
     * event.
     * 
     * @return  {@code true} either of the {@code stopPropagation()} or
     *          {@code stopImmediatePropagation()} methods have been called on
     *          this event, {@code false} otherwise.
     */
    public boolean isPropagationStopped() {
        return propagationStopped;
    } // isPropagationStopped()

    /**
     * Stops this event from propagating up through the display list hierarchy.
     * 
     * <p>Any remaining event listeners for the current target object will still
     * be processed.
     */
    public void stopPropagation() {
        propagationStopped = true;
    } // stopPropagation()

    /**
     * Determines whether the {@code stopImmediatePropagation()} method has
     * been called on this event.
     * 
     * @return  {@code true} if the {@code stopImmediatePropagation()} method
     *          has been called on this event, {@code false} otherwise.
     */
    public boolean isImmediatePropagationStopped() {
        return immediatePropagationStopped;
    } // isPropagationStopped()

    /**
     * Stops this event from propagating up through the display list hierarchy
     * or from being processed by any remaining event listeners for the current
     * target object.
     */
    public void stopImmediatePropagation() {
        propagationStopped = true;
        immediatePropagationStopped = true;
    } // stopImmediatePropagation()

    /**
     * Returns the properties of this event as a string for use by the
     * {@code toString()} method.
     * 
     * <p>Subclasses should override this method and add any new properties
     * that they define.
     * 
     * @return  The properties of this event as a string.
     */
    protected String getToStringProperties() {
        StringBuilder sb = new StringBuilder();

        if (isConsumed())
            sb.append("Consumed");

        if (isImmediatePropagationStopped()) {
            if (sb.length() == 0)
                sb.append(", ");

            sb.append("ImmediatePropagationStopped");
        } else if (isPropagationStopped()) {
            if (sb.length() == 0)
                sb.append(", ");

            sb.append("PropagationStopped");
        }

        return ((sb.length() > 0) ? "{"+ sb.toString() +"} " : "") +
                "source="+ getSource();
    } // getToStringProperties()

    @Override
    public String toString() {
        return this.getClass().getName() +"["+ getToStringProperties() +"]";
    } // toString()

    /**
     * Adds a new listener to the end of a list of listeners, if it is not
     * already found in the list.
     * 
     * <p>This method always returns a new list when a modification to the
     * list is needed, and does not modify the original list passed in.  This
     * is to reduce the possibility of concurrent modification issues in a
     * multi-threaded system, and permits potential modification by event
     * handlers while the prior list is being iterated over to dispatch an
     * event.  That said, it is still possible for the original list to be
     * returned if no modification is made (eg. the listener is already in the
     * original list).
     * 
     * @param <T>       The type of listener.
     * @param listeners The list to add the specified listener to.
     *                  May be {@code null}, in which case a new list will be
     *                  created.
     * @param listener  The listener to add.
     * 
     * @return  If {@code listeners} is not {@code null}, returns
     *          {@code listeners}.  Otherwise, returns a new list containing
     *          the specified {@code listener}.
     * 
     * @throws  IllegalArgumentException if {@code listener} is {@code null}.
     */
    public static <T> List<T> addListener(List<T> listeners, T listener) {
        if (listener == null)
            throw new IllegalArgumentException("The listener parameter cannot be null");

        if (listeners == null) {
            listeners = new ArrayList<>();
            listeners.add(listener);
        } else {
            List<T> newListeners = new ArrayList<>(listeners);

            if (!newListeners.contains(listener)) {
                newListeners.add(listener);
                return newListeners;
            }
        }

        return listeners;
    } // addListener(List<T> listeners, T listener)

    /**
     * Adds a new listener to the beginning of a list of listeners, if it is
     * not already found in the list.
     * 
     * <p>This method always returns a new list when a modification to the
     * list is needed, and does not modify the original list passed in.  This
     * is to reduce the possibility of concurrent modification issues in a
     * multi-threaded system, and permits potential modification by event
     * handlers while the prior list is being iterated over to dispatch an
     * event.  That said, it is still possible for the original list to be
     * returned if no modification is made (eg. the listener is already in the
     * original list).
     * 
     * @param <T>       The type of listener.
     * @param listeners The list to add the specified listener to.
     *                  May be {@code null}, in which case a new list will be
     *                  created.
     * @param listener  The listener to add.
     * 
     * @return  If {@code listeners} is not {@code null}, returns
     *          {@code listeners}.  Otherwise, returns a new list containing
     *          the specified {@code listener}.
     * 
     * @throws  IllegalArgumentException if {@code listener} is {@code null}.
     */
    public static <T> List<T> prependListener(List<T> listeners, T listener) {
        if (listener == null)
            throw new IllegalArgumentException("The listener parameter cannot be null");

        if (listeners == null) {
            listeners = new ArrayList<>();
            listeners.add(listener);
        } else {
            if (!listeners.contains(listener)) {
                List<T> oldListeners = listeners;

                listeners = new ArrayList<>(listeners.size() + 1);
                listeners.add(listener);
                listeners.addAll(oldListeners);
            }
        }

        return listeners;
    } // prependListener(List<T> listeners, T listener)

    /**
     * Removes a listener from a list of listeners, if it is found in the list.
     * 
     * <p>This method always returns a new list (or {@code null}) when a
     * modification to the list is needed, and does not modify the original
     * list passed in.  This is to reduce the possibility of concurrent
     * modification issues in a multi-threaded system, and permits potential
     * modification by event handlers while the prior list is being iterated
     * over to dispatch an event.  That said, it is still possible for the
     * original list to be returned if no modification is made (eg. the
     * listener wasn't in the original list).
     *
     * @param <T>       The type of listener.
     * @param listeners The list to remove the specified listener from.
     *                  May be {@code null}.
     * @param listener  The listener to remove.
     * 
     * @return  A new modified list of listeners, or {@code null} if the new
     *          list is empty.
     */
    public static <T> List<T> removeListener(List<T> listeners, T listener) {
        if (listeners != null) {
            List<T> newListeners = new ArrayList<>(listeners);

            newListeners.remove(listener);
            if (!newListeners.isEmpty()) {
                return newListeners;
            }
        }

        return null;
    } // removeListener(List<T> listeners, T listener)

    /**
     * Notifies all of the specified listeners of an event.
     * 
     * <p>Only listeners in the specified list are notified.  This method does
     * not traverse the display list hierarchy.
     * 
     * <p>This method assumes that the list passed in will not be modified
     * while the method executes.  This includes potential modifications made
     * to the list by event handler methods.  It is up to the caller to ensure
     * that the list passed in is not accessible for modification by any other
     * thread or the event handler methods.
     * 
     * <p>The notification will stop before notifying all listeners if one of
     * the {@code stopPropagation()} or {@code stopImmediatePropagation()}
     * methods of the event instance is called by a previously invoked
     * listener.
     * 
     * @param <T>       The type of the listeners List.
     * @param <E>       The type of the EventBase subclass to dispatch.
     * @param listeners List of listener instances to notify.
     * @param e         Event to send to each listener.
     * @param callback  Callback to invoke for each listener which will call
     *                  the appropriate method for the particular event.
     */
    public static <T, E extends EventBase> void notifyListeners(
        List<T> listeners,
        E e,
        BiConsumer<T, E> callback
    ) {
        if ((listeners != null) && !e.isImmediatePropagationStopped()) {
            for (T listener : listeners) {
                callback.accept(listener, e);
                if (e.isImmediatePropagationStopped())
                    break;
            } // for
        }
    } // notifyListeners()

    /**
     * Notifies all of the specified default listeners of an event.
     * 
     * <p>This method assumes that the list passed in will not be modified
     * while the method executes.  This includes potential modifications made
     * to the list by event handler methods.  It is up to the caller to ensure
     * that the list passed in is not accessible for modification by any other
     * thread or the event handler methods.
     * 
     * <p>The notification will stop before notifying all listeners if the
     * {@code consume()} methods of the event instance is called by a
     * previously invoked listener.
     * 
     * @param <T>       The type of the listeners List.
     * @param <E>       The type of the EventBase subclass to dispatch.
     * @param listeners List of listener instances to notify.
     * @param e         Event to send to each listener.
     * @param callback  Callback to invoke for each listener which will call
     *                  the appropriate method for the particular event.
     */
    public static <T, E extends EventBase> void notifyDefaultListeners(
        List<T> listeners,
        E e,
        BiConsumer<T, E> callback
    ) {
        if ((listeners != null) && !e.isConsumed()) {
            for (T listener : listeners) {
                callback.accept(listener, e);
                if (e.isConsumed())
                    break;
            } // for
        }
    } // notifyDefaultListeners()

} // EventBase
