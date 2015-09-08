package com.rivetlogic.migration.api.model;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>BaseContent</p>
 */
public class BaseContent {

    private String contentKey;
    private Map<String, Object> properties;

    /**
     * <p>Default Constructor</p>
     */
    public BaseContent() {}

    /**
     * <p>Construct Source with properties</p>
     *
     * @param contentKey a {@link java.lang.String} object
     * @param properties a {@link java.util.Map} object
     */
    public BaseContent(String contentKey, Map<String, Object> properties) {
        this.contentKey = contentKey;
        this.properties = properties;
    }

    /**
     * <p>get a property value</p>
     *
     * @param key a {@link java.lang.String} object
     * @return a {@link java.lang.Object} object
     */
    public Object getProperty(String key) {
        return (getProperties() != null) ? getProperties().get(key) : null;
    }

    /**
     * <p>set a property</p>
     *
     * @param key a {@link java.lang.String} object
     * @param value a {@link java.lang.Object} object
     */
    public void setProperty(String key, Object value) {
        if (getProperties() == null) {
            setProperties(new HashMap<String, Object>());
        }
        getProperties().put(key, value);
    }

    /**
     * <p>get <field>properties</field></p>
     * @return
     */
    public Map<String, Object> getProperties() {
        return this.properties;
    }

    /**
     * <p>set <field>properties</field></p>
     *
     * @param properties a {@link java.util.Map} object
     */
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    /**
     * <p>get <field>contentKey</field></p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getContentKey() {
        return this.contentKey;
    }

    /**
     * <p>set <field>contentKey</field></p>
     *
     * @param contentKey a {@link java.lang.String} object
     */
    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
    }

}
