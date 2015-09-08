package com.rivetlogic.migration.api.creator;

import com.rivetlogic.migration.api.model.TargetContent;

import java.io.File;

/**
 * <p>FileCreator</p>
 */
public interface FileCreator {

    /**
     * <p>Create a file to output the item given</p>
     *
     * @param item a {@link com.rivetlogic.migration.api.model.TargetContent} object
     * @param targetLocation a {@link java.lang.String} object
     * @param extension a {@link java.lang.String} object
     * @return a {@link java.io.File} object
     */
    public File createFile(TargetContent item, String targetLocation, String extension);


}
