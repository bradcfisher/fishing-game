
package Fishing.drawable.events;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Wrapper around a list of registered listeners and a default listener.
 * 
 * @param <T>   The type of the listeners contained in this instance.
 * 
 * @author Brad
 */
public class DrawableListeners<T> {

    /**
     * The registered listeners to notifyListeners for this type of event.
     * These listeners are notified before the default listener.
     */
    private List<T> listeners;

    /**
     * The default listeners to notifyListeners when this type of event occurs
     * and the event is not consumed by the other registered listeners.
     * 
     * <p>Default listeners should only be registered internally by components.
     */
    private List<T> defaultListeners;

    /**
     * Notifies the registers listeners and/or default listener of an event.
     * 
     * @param <E>       The type of the event.
     * @param e         The event to dispatch.
     * @param callback  Callback method 
     */
    public <E extends EventBase> void notifyListeners(
        E e,
        BiConsumer<T, E> callback
    ) {
        EventBase.notifyListeners(listeners, e, callback);
        EventBase.notifyDefaultListeners(defaultListeners, e, callback);
    } // notifyListeners

    /**
     * Adds a new listener to the end of list of registered listeners,
     * if it is not already found in the list.
     * 
     * @param listener  The listener to add.
     * 
     * @throws  IllegalArgumentException if {@code listener} is {@code null}.
     */
    public void add(T listener) {
        listeners = EventBase.addListener(listeners, listener);
    } // add(T listener)

    /**
     * Removes a listener from the list of registered listeners, if it
     * is found in the list.
     * 
     * @param listener  The listener to remove.
     */
    public void remove(T listener) {
        listeners = EventBase.removeListener(listeners, listener);
    } // remove(T listener)

    /**
     * Adds a new default listener to the list of registered default listeners,
     * if it is not already found in the list.
     * 
     * <p>Unlike 
     * 
     * <p>The default listeners are notified if the dispatched event is not
     * consumed by the other registered listeners, regardless of whether
     * propagation of the event is prevented or not.
     * 
     * @param listener  The listener to add.
     * 
     * @throws  IllegalArgumentException if {@code listener} is {@code null}.
     */
    public void addDefault(T listener) {
        defaultListeners = EventBase.prependListener(defaultListeners, listener);
    } // prependDefault(T listener)

    /**
     * Removes a default listener from the list of registered default
     * listeners, if it is found in the list.
     * 
     * @param listener  The listener to remove.
     */
    public void removeDefault(T listener) {
        defaultListeners = EventBase.removeListener(defaultListeners, listener);
    } // remove(T listener)

} // DrawableListeners
