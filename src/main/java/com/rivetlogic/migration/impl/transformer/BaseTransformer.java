package com.rivetlogic.migration.impl.transformer;

import com.rivetlogic.migration.api.constant.MigrationConstants;
import com.rivetlogic.migration.api.exception.TransformationException;
import com.rivetlogic.migration.api.transformer.Transformer;
import com.rivetlogic.migration.impl.util.MigrationUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * <p>BaseTransformer</p>
 */
public abstract class BaseTransformer implements Transformer {

    /**
     * <p>get target properties</p>
     *
     * @param configProperties a {@link java.util.Properties} object
     * @param contentKey a {@link java.lang.String} object
     * @return a {@link java.util.Properties} object
     * @throws IOException
     */
    protected Properties getTargetProperties(Properties configProperties, String contentKey) throws TransformationException {
        try {
            return MigrationUtils.getProperties(configProperties, contentKey,
                    MigrationConstants.CONFIG_TARGET_FOLDER_NAME, MigrationConstants.CONFIG_TARGET_FILE_EXTENSION);
        } catch (IOException e) {
            throw new TransformationException(e);
        }
    }
}
