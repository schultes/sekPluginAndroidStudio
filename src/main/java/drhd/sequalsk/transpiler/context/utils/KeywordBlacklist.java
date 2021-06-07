package drhd.sequalsk.transpiler.context.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * The keyword blacklist is an experimental feature to reduce the amount of resolved references by filtering
 * kotlin specific elements.
 */
public class KeywordBlacklist {

    public final List<String> keywords = new ArrayList<>();

    private static KeywordBlacklist instance;
    private KeywordBlacklist () {

        // Numbers
        keywords.add("Byte");
        keywords.add("UByte");
        keywords.add("Short");
        keywords.add("UShort");
        keywords.add("Int");
        keywords.add("UInt");
        keywords.add("Long");
        keywords.add("ULong");
        keywords.add("Float");
        keywords.add("Double");

        // Others
        keywords.add("Boolean");
        keywords.add("String");
        keywords.add("Char");

        // Arrays
        keywords.add("Array");
        keywords.add("ByteArray");
        keywords.add("UByteArray");
        keywords.add("ShortArray");
        keywords.add("UShortArray");
        keywords.add("LongArray");
        keywords.add("ULongArray");
        keywords.add("FloatArray ");
        keywords.add("UFloatArray ");
        keywords.add("DoubleArray");
        keywords.add("UDoubleArray");

        // Collections
        keywords.add("Collection");
        keywords.add("MutableList");
        keywords.add("Set");
        keywords.add("LinkedHashSet");
        keywords.add("MutableSet");
        keywords.add("MutableCollection");
        keywords.add("Map");
        keywords.add("LinkedHashMap");
        keywords.add("MutableMap");
    }

    public static KeywordBlacklist getInstance () {
        if (KeywordBlacklist.instance == null) {
            KeywordBlacklist.instance = new KeywordBlacklist ();
        }
        return KeywordBlacklist.instance;
    }

    public static boolean isBlacklisted(String keyword) {
        return KeywordBlacklist.getInstance().keywords.contains(keyword);
    }
}
