
package Fishing.drawable.controls;

import Fishing.drawable.events.TimerEvent;
import Fishing.drawable.events.TimerListener;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Utility class managing a Timer instance that can be shared between multiple
 * control instances.
 * 
 * <p>Using this class ensures that only one timer thread is created for the
 * timing needs of controls, reducing resources.
 * 
 * @author Brad
 */
public final class ControlTimer {

    /**
     * The shared timer thread.
     */
    private static Timer timer = new Timer();

    /**
     * Mapping of task handles to the TimerTask instance related to that handle.
     */
    private final static HashMap<Integer, ControlTimerTask> controlTimerTasks = new HashMap<>();

    /**
     * The last task handle/id that was generated.
     */
    private static int lastTaskHandle = 0;

    /**
     * TimerTask subclass that adds the concept of a unique integer ID
     * generated for each instance.
     */
    private static class ControlTimerTask extends TimerTask {

        /**
         * The TimerListener that should be invoked when this task is run.
         */
        private final TimerListener listener;

        /**
         * The unique integer ID assigned to this task.
         */
        private final int id;

        /**
         * Constructs a new instance.
         * 
         * @param listener  The TimerListener that should be invoked when
         *                  the new task is run.
         * 
         * @throws  IllegalArgumentException if the {@code listener} is
         *          {@code null}.
         */
        public ControlTimerTask(final TimerListener listener) {
            if (listener == null)
                throw new IllegalArgumentException("The listener parameter cannot be null.");

            this.listener = listener;

            synchronized (controlTimerTasks) {
                ++lastTaskHandle;
                id = lastTaskHandle;

                controlTimerTasks.put(id, this);
            }
        } // ControlTimerTask(final TimerListener listener)

        /**
         * Retrieves the unique integer ID assigned to this task.
         * 
         * @return The unique integer ID assigned to this task.
         */
        public int getId() {
            return id;
        } // getId()

        /**
         * Executes the associated TimerListener callback.
         */
        @Override
        public void run() {
            listener.timerFired( new TimerEvent( id ) );
        } // run()
    } // ControlTimerTask
    
    /**
     * Schedules the specified task for one-time execution after the specified
     * delay.
     * 
     * @param listener  The TimerListener callback that should be invoked when
     *                  the new timer elapses.
     * @param delay     Delay in milliseconds before the timer is to be
     *                  executed.
     * 
     * @return  A timer handle ID that uniquely identifies the timer created
     *          by the method invocation.
     * 
     * @see java.util.Timer#schedule(java.util.TimerTask, long) 
     */
    public static int schedule( final TimerListener listener, long delay ) {
        ControlTimerTask task = new ControlTimerTask(listener);
        timer.schedule(task, delay);

        return task.getId();
    } // schedule( final TimerListener listener, long delay )

    /**
     * Schedules the specified task for repeated fixed-rate execution,
     * beginning after the specified delay.
     * 
     * Subsequent executions take place at approximately regular intervals,
     * separated by the specified period.
     * 
     * @param listener  The TimerListener callback that should be invoked for
     *                  each execution of the new timer.
     * @param delay     Delay in milliseconds before the timer is to be
     *                  executed the first time.
     * @param period    Time in milliseconds between successive timer
     *                  executions.
     * 
     * @return  A timer handle ID that uniquely identifies the timer created
     *          by the method invocation.
     * 
     * @see java.util.Timer#scheduleAtFixedRate(java.util.TimerTask, long, long) 
     */
    public static int scheduleAtFixedRate( final TimerListener listener, long delay, long period ) {
        if (period <= 0)
            throw new IllegalArgumentException("The period cannot be less than or equal to 0.");

        ControlTimerTask task = new ControlTimerTask(listener);
        timer.scheduleAtFixedRate(task, delay, period);

        return task.getId();
    } // scheduleAtFixedRate( TimerListener listener, long delay, long period )

    /**
     * Schedules the specified task for repeated fixed-rate execution,
     * beginning after the specified delay.
     * 
     * Subsequent executions take place at approximately regular intervals,
     * separated by the specified period, with the first execution occurring
     * as soon as possible.
     * 
     * <p>This method is equivalent to
     * {@code scheduleAtFixedRate(listener, 0, period)}.
     * 
     * @param listener  The TimerListener callback that should be invoked for
     *                  each execution of the new timer.
     * @param period    Time in milliseconds between successive timer
     *                  executions.
     * 
     * @return  A timer handle ID that uniquely identifies the timer created
     *          by the method invocation.
     * 
     * @see java.util.Timer#scheduleAtFixedRate(java.util.TimerTask, long, long) 
     */
    public static int scheduleAtFixedRate( final TimerListener listener, long period ) {
        return scheduleAtFixedRate( listener, 0, period );
    } // scheduleAtFixedRate( TimerListener listener, long period )

    /**
     * Cancels the timer with the specified handle, if it hasn't already fired.
     * 
     * @param taskHandle    The time handle ID returned by a prior call to
     *                      {@code schedule} or {@code scheduleAtFixedRate}.
     */
    public static void cancelTimerListener( int taskHandle ) {
        TimerTask t;
        synchronized (controlTimerTasks) {
            t = controlTimerTasks.remove( taskHandle );
        }
        if (t != null)
            t.cancel();
    } // cancelTimerListener( TimerListener listener )

    /**
     * Prevent instantiation of this utility class.
     */
    private ControlTimer() {
    } // ControlTimer()

} // class ControlTimer
