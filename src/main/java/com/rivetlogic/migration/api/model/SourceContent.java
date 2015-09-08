package com.rivetlogic.migration.api.model;

import java.util.Map;

/**
 * <P>Source Model</P>
 */
public class SourceContent extends BaseContent {

    /**
     * <p>Default Constructor</p>
     */
    public SourceContent() {
    }

    /**
     * <p>Construct Source with properties</p>
     *
     * @param contentKey a {@link java.lang.String} object
     * @param properties a {@link java.util.Map} object
     */
    public SourceContent(String contentKey, Map<String, Object> properties) {
        super(contentKey, properties);
    }

}
