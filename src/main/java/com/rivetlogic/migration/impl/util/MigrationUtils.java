package com.rivetlogic.migration.impl.util;

import com.rivetlogic.migration.api.constant.MigrationConstants;
import org.apache.poi.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * <p>MigrationUtils</p>
 */
public class MigrationUtils {

    /**
     * <p>get properties</p>
     *
     * @param configProperties a {@link java.util.Properties} object
     * @param key a {@link java.lang.String} object
     * @param folderKey a {@link java.lang.String} object
     * @param extKey a {@link java.lang.String} object
     * @return a {@link java.util.Properties} object
     * @throws IOException
     */
    public static Properties getProperties(Properties configProperties, String key,
                                           String folderKey, String extKey) throws IOException {
        String configLocation = (String) configProperties.get(MigrationConstants.CONFIG_LOCATION);
        String sourceFolder = (String) configProperties.get(folderKey);
        String sourcePropertiesExtension = (String) configProperties.get(extKey);
        String sourcePropertiesFile = configLocation + "/" + sourceFolder + "/" + key + sourcePropertiesExtension;
        return MigrationUtils.loadProperties(sourcePropertiesFile);
    }


    /**
     * <p>load transformation properties</p>
     *
     * @param path a {@link java.lang.String} object
     * @return a {@link java.util.Properties} object
     */
    public static Properties loadProperties(String path) throws IOException {
        File file = new File(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fis);
            return properties;
        } finally {
            IOUtils.closeQuietly(fis);
        }

    }

    /**
     * <p>get a prototype file path</p>
     *
     * @param configProperties a {@link java.util.Properties} object
     * @param configLocation a {@link java.lang.String} object
     * @param key a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String getProtoTypePath(Properties configProperties, String configLocation, String key) {
        String sourceFolder = (String) configProperties.get("config.modelFolder");
        String sourcePropertiesExtension = (String) configProperties.get("config.modelExtension");
        return configLocation + "/" + sourceFolder + "/" + key + sourcePropertiesExtension;
    }


}
