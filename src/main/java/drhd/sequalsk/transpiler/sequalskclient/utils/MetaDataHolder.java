package drhd.sequalsk.transpiler.sequalskclient.utils;

import java.util.HashMap;

/** Adds the possibility to add custom metadata to objects. Helps to describe and/or identify requests or files. */
public interface MetaDataHolder {

    HashMap<String, Object> getTags();

    /**
     * Add a single tag
     */
    default void addTag(String key, Object value) {
        getTags().put(key, value);
    }

    /**
     * Returns a tag by its key.
     */
    default Object getTag(String key) {
        return getTags().get(key);
    }

}
