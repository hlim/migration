package com.rivetlogic.migration.api.parser;

import com.rivetlogic.migration.api.exception.ParsingException;
import com.rivetlogic.migration.api.model.SourceContent;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 *<p>Parser</p>
 */
public interface Parser {

    /**
     * <p>Parse the file given to get a list of items to migrate</p>
     *
     * @param configProperties a {@link java.util.Properties} object
     * @param file a {@link java.io.File} object
     * @return a {@link java.util.List} object
     * @throws ParsingException
     * @throws IOException
     * @throws InvalidFormatException
     */
    public List<SourceContent> parse(Properties configProperties, File file) throws ParsingException, IOException, InvalidFormatException;

}
