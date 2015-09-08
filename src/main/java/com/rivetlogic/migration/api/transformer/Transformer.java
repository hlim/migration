package com.rivetlogic.migration.api.transformer;

import com.rivetlogic.migration.api.exception.TransformationException;
import com.rivetlogic.migration.api.model.SourceContent;
import com.rivetlogic.migration.api.model.TargetContent;

import java.util.Properties;

/**
 * <p>Output Transformer</p>
 */
public interface Transformer {

    /**
     * <p>Transform source to target</p>
     *
     * @param configProperties a {@link java.util.Properties} object
     * @param item a {@link com.rivetlogic.migration.api.model.SourceContent} object
     * @return a {@link com.rivetlogic.migration.api.model.TargetContent} object
     * @throws TransformationException
     */
    public TargetContent transform(Properties configProperties, SourceContent item) throws TransformationException;

}
