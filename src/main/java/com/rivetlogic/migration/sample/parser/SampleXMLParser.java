package com.rivetlogic.migration.sample.parser;

import com.rivetlogic.migration.impl.parser.XMLParser;
import org.dom4j.Document;

import java.io.File;

/**
 * <p>SampleXMLParser</p>
 */
public class SampleXMLParser extends XMLParser {

    @Override
    protected String getContentKey(File file, Document source) {
        return "sample";
    }
}
