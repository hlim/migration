package com.rivetlogic.migration.sample.parser;

import com.rivetlogic.migration.impl.parser.XMLParser;
import org.dom4j.Document;

/**
 * <p>SampleXMLParser</p>
 */
public class SampleXMLParser extends XMLParser {

    @Override
    protected String getContentKey(Document source) {
        return "sample";
    }
}
