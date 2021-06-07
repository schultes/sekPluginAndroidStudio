package drhd.sequalsk.platform.services.fileservice;

import drhd.sequalsk.utils.debugging.DebugLogger;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Utility class to provide a simple way to start and cancel timers/countdowns.
 * Used to track the delay time before a change event gets dispatched.
 * <p>
 * {@link FileSelectionSubscriber#onFileSelectionDelayExpired}
 * {@link FileContentChangeSubscriber#onContentChangeDelayExpired}
 */
public class DelayTimer {

    /** The timer that holds back the current timer task for the configured time. */
    private final Timer timer;

    /** The task that will dispatch call the change delay listener as soon as the configured delay is expired. */
    private TimerTask currentTimerTask;

    /** The listener that gets informed as soon as the delay has expired. */
    private final DelayTimerListener listener;

    /** Returns the {@link DelayTimeProvider} that provides the delay time for this timer. */
    private final DelayTimeProvider delayTimeProvider;

    /** Human readable name for debugging purposes. */
    private final String timerId;

    /**
     * @param listener {@link DelayTimerListener}
     * @param provider {@link DelayTimeProvider}
     * @param timerId  human readable identifier for the delay timer to generate debug messages
     */
    public DelayTimer(DelayTimerListener listener, DelayTimeProvider provider, String timerId) {
        this.listener = listener;
        this.delayTimeProvider = provider;
        this.timerId = timerId;
        timer = new Timer();
    }

    /**
     * Starts or restarts the timer.
     */
    public void restart() {
        DebugLogger.info(this, timerId + " (re)started");
        if (currentTimerTask != null) cancel();
        TimerTask task = this.generateTimerTask();
        timer.schedule(task, delayTimeProvider.getDelayTime());
        currentTimerTask = task;
    }

    /**
     * Cancels the current timer.
     */
    public void cancel() {
        if (currentTimerTask != null && currentTimerTask.cancel()) {
            DebugLogger.info(this, timerId + " cancelled");
        }
    }

    /**
     * Generates a new TimerTask that will dispatch a KotlinFileChangedEvent after the delay time has expired.
     */
    private TimerTask generateTimerTask() {
        DelayTimer timer = this;
        return new TimerTask() {
            @Override
            public void run() {
                DebugLogger.info(timer, timerId + ": delay of " + delayTimeProvider.getDelayTime() + " ms expired");
                listener.onChangeDelayExpired();
            }
        };
    }

    /**
     * Provides the delay time in milliseconds for the timer.
     */
    public interface DelayTimeProvider {
        /** Method that must provide the delay time in milliseconds from the plugin settings. */
        long getDelayTime();
    }

    /**
     * Listener for {@link DelayTimer}. Listens to the timer and gets called as soon as the timer expires.
     */
    public interface DelayTimerListener {

        /**
         * Called from the timer as soon as the delay time has expired.
         */
        void onChangeDelayExpired();

    }
}
