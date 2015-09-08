package com.rivetlogic.migration.api.writer;

import com.rivetlogic.migration.api.creator.FileCreator;
import com.rivetlogic.migration.api.model.TargetContent;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * <p>Writer Interface</p>
 */
public interface Writer {

    /**
     * <p>write items into a single output file</p>
     *
     * @param configProperties a {@link java.util.Properties} object
     * @param items a {@link java.util.List} object
     * @param file a {@link java.io.File} object
     */
    public void write(Properties configProperties, List<TargetContent> items, File file) throws IOException;

    /**
     * <p>write items into individual output file</p>
     *
     * @param configProperties a {@link java.util.Properties} object
     * @param items a {@link java.util.List} object
     * @param targetLocation a {@link java.lang.String} object
     * @param creator a {@link FileCreator} object
     */
    public void write(Properties configProperties, List<TargetContent> items, String targetLocation, FileCreator creator) throws IOException;

}
