package com.rivetlogic.migration.sample.creator;

import com.rivetlogic.migration.api.creator.FileCreator;
import com.rivetlogic.migration.api.model.TargetContent;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * <p>SampleFileCreator</p>
 */
public class SamplePageCreator implements FileCreator {

    final static Logger LOGGER = Logger.getLogger(SamplePageCreator.class);

    private String dateFieldName;
    private String titleFieldName;

    public File createFile(TargetContent item, String targetLocation, String extension) {
        Object dateField = item.getProperty(getDateFieldName());
        Object titleField = item.getProperty(getTitleFieldName());
        if (dateField != null && titleField != null) {
            String folderName = String.valueOf(titleField).trim().toLowerCase().replaceAll("[^a-z0-9\\-]+", "-");
            // expecting MM/dd/yyyy hh:mm:ss format
            String dateTimeStr = (String) dateField;
            String[] dateTimeValues = dateTimeStr.split(" ");
            String[] dateValues = dateTimeValues[0].split("/");
            String filePath = targetLocation + "/" + dateValues[2] + "/" +
                    ((dateValues[0].length() == 1) ? "0" + dateValues[0] : dateValues[0])
                    + "/" + folderName + "/index.xml";
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Creating " + filePath);
            }
            return new File(filePath);
        } else {
            LOGGER.error("No value found for " + getDateFieldName() + ": " + dateField
                    + " or " + getTitleFieldName() + ": " + titleField);
        }
        return null;
    }

    /**
     * <p>get <field>dateFieldName</field></p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getDateFieldName() {
        return dateFieldName;
    }

    /**
     * <p>set <field>dateFieldName</field></p>
     *
     * @param dateFieldName a {@link java.lang.String} object
     */
    public void setDateFieldName(String dateFieldName) {
        this.dateFieldName = dateFieldName;
    }

    /**
     * <p>get <field>titleFieldName</field></p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getTitleFieldName() {
        return titleFieldName;
    }

    /**
     * <p>set <field>titleFieldName</field></p>
     *
     * @param titleFieldName a {@link java.lang.String} object
     */
    public void setTitleFieldName(String titleFieldName) {
        this.titleFieldName = titleFieldName;
    }


}
