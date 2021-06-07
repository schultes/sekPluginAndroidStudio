package drhd.sequalsk.transpiler.sequalskclient.utils;

import java.util.HashMap;

/**
 * Adds the possibility to add custom metadata to objects. Helps to describe and/or identify requests or files.
 * This class provides a lazy way to implement the interface {@link MetaDataHolder} and can be used if no other
 * class is extended.
 */
public class MetaDataHolderImpl implements MetaDataHolder {

    protected HashMap<String, Object> tags = new HashMap<>();

    @Override
    public HashMap<String, Object> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, Object> tags) {
        this.tags = tags;
    }
}
