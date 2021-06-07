package drhd.sequalsk.utils.debugging;

public class DebugLogger {

    // see https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println for more colors

    public static final String RESET = "\033[0m";            // reset color
    public static final String RED_BOLD = "\033[1;31m";      // red
    public static final String RED_BRIGHT = "\033[0;91m";    // red/orange
    public static final String BLUE_BRIGHT = "\033[0;94m";   // blue
    private static final boolean debugMode = false;

    public static void info(Object source, String text){
        if(debugMode) System.out.println(BLUE_BRIGHT + identifier(source) + text + RESET);
    }

    public static void warning(Object source, String text){
        if(debugMode) System.out.println(RED_BRIGHT + identifier(source) + text + RESET);
    }

    public static void error(Object source, String text){
        if(debugMode) System.out.println(RED_BOLD + identifier(source) + text + RESET);
    }

    private static String identifier(Object source){
        return source.getClass().getSimpleName() + "@" + Integer.toHexString(source.hashCode()) + ": ";
    }
}
