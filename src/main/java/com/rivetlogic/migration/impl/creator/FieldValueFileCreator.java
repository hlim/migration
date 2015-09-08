package com.rivetlogic.migration.impl.creator;

import com.rivetlogic.migration.api.creator.FileCreator;
import com.rivetlogic.migration.api.model.TargetContent;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * <p>FieldValueFileCreator</p>
 */
public class FieldValueFileCreator implements FileCreator {

    final static Logger LOGGER = Logger.getLogger(FieldValueFileCreator.class);

    private String fieldName;

    public File createFile(TargetContent item, String targetLocation, String extension) {
        Object field = item.getProperty(fieldName);
        if (field != null) {
            String filePath = targetLocation + "/" + field.toString() + "." + extension;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Creating " + filePath);
            }
            return new File(filePath);
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("No value found for " + fieldName);
            }
            return null;
        }
    }

    /**
     * <p>get <field>fieldName</field></p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getFieldName() { return this.fieldName; }

    /**
     * <p>set <field>fieldName</field></p>
     *
     * @param fieldName a {@link java.lang.String} object
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

}

