package drhd.sequalsk.utils.debugging;

import java.util.HashMap;

public class SimpleStopwatch {

    private static final SimpleStopwatch instance = new SimpleStopwatch();

    private SimpleStopwatch() {
    }

    public static SimpleStopwatch getInstance() {
        return instance;
    }

    private final HashMap<String, Long> timeMap = new HashMap<>();


    public void start(String id) {
        timeMap.put(id + "start", System.currentTimeMillis());
    }

    public void stop(String id) {
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - timeMap.get(id + "start");
        DebugLogger.info(this, id + " stopped - time in ms: " + timeElapsed);
    }

}
