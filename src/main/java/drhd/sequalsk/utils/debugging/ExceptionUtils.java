package drhd.sequalsk.utils.debugging;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {

    // https://stackoverflow.com/questions/6822968/print-the-stack-trace-of-an-exception
    public static String getStackTraceAsString(Throwable throwable) {
        StringBuilder sb = new StringBuilder(500);
        StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        sb.append(throwable.getClass().getName()).append(": ").append(throwable.getMessage()).append("\n");
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            sb.append("\t at ").append(stackTraceElement.toString()).append("\n");
        }
        return sb.toString();
    }

    // https://stackoverflow.com/questions/4812570/how-to-store-printstacktrace-into-a-string/4812589#4812589
    public static String printStacktraceAsString(Throwable throwable) {
        StringWriter errors = new StringWriter();
        throwable.printStackTrace(new PrintWriter(errors));
        throwable.getCause().printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }

}
