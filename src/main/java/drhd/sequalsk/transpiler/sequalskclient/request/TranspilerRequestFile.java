package drhd.sequalsk.transpiler.sequalskclient.request;

import drhd.sequalsk.transpiler.sequalskclient.utils.MetaDataHolderImpl;
import java.util.HashMap;

/**
 * Represents a file or content that should be transpiled.
 */
public class TranspilerRequestFile extends MetaDataHolderImpl {

    /** absolute path to the file */
    private final String path;

    /** name with extension of the file */
    private final String name;

    /** content of the file */
    private final String content;

    public TranspilerRequestFile(String path, String name, String content) {
        this.path = path;
        this.name = name;
        this.content = content;
        this.tags = new HashMap<>();
    }

    public TranspilerRequestFile(String name, String content) {
        this.name = name;
        this.path = "-";
        this.content = content;
        this.tags = new HashMap<>();
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getContentWithSeparator() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("//<++++++++++>");
        stringBuilder.append(name);
        stringBuilder.append("||").append(path);
        stringBuilder.append("//<++++++++++>\n");
        stringBuilder.append(content);
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}
