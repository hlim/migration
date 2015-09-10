package com.rivetlogic.migration.impl.parser;

import com.rivetlogic.migration.api.constant.MigrationConstants;
import com.rivetlogic.migration.api.parser.Parser;
import com.rivetlogic.migration.impl.util.MigrationUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * <p>BaseParser</p>
 */
public abstract class BaseParser implements Parser {

    /**
     * <p>get source properties</p>
     *
     * @param configProperties a {@link java.util.Properties} object
     * @param contentKey a {@link java.lang.String} object
     * @return a {@link java.util.Properties} object
     * @throws IOException
     */
    protected Properties getSourceProperties(Properties configProperties, String contentKey) throws IOException {
        Properties sourceProperties = MigrationUtils.getProperties(configProperties, contentKey,
                MigrationConstants.CONFIG_SOURCE_FOLDER_NAME, MigrationConstants.CONFIG_SOURCE_FILE_EXTENSION);
        return sourceProperties;
    }


}

