package com.rivetlogic.migration.impl.parser;

import com.rivetlogic.migration.api.exception.ParsingException;
import com.rivetlogic.migration.api.model.SourceContent;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * <p>XMLParser</p>
 */
public abstract class XMLParser extends BaseParser {

    final static Logger LOGGER = Logger.getLogger(XMLParser.class);

    public static final String XML_FILE = ".xml";
    // source's parent node path
    private String rootPath;

    public List<SourceContent> parse(Properties configProperties, File file) throws ParsingException, IOException, InvalidFormatException {
        if (file.getName().endsWith(XML_FILE)) {
            try {
                Document document = loadXml(file);
                String contentKey = getContentKey(document);
                Properties sourceProperties = getSourceProperties(configProperties, contentKey);
                Element root = document.getRootElement();
                List<Node> nodes = root.selectNodes(rootPath);
                List<SourceContent> parsedItems = new ArrayList();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Found " + nodes.size() + " source document(s) in " + file.getAbsolutePath());
                }
                for (Node node : nodes) {
                    if (sourceProperties != null) {
                        Map<String, Object> mappedProperties = new HashMap();
                        Enumeration sourceKeys = sourceProperties.propertyNames();
                        while (sourceKeys.hasMoreElements()) {
                            String sourceKey = (String) sourceKeys.nextElement();
                            String sourcePath = sourceProperties.getProperty(sourceKey);
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("Mapping " + sourcePath + " to " + sourceKey);
                            }
                            if (sourcePath.contains("//")) {
                                List<Node> propertyNodes = node.selectNodes(sourcePath);
                                List<String> propertyValues = new ArrayList();
                                for (Node propertyNode : propertyNodes) {
                                    propertyValues.add(propertyNode.getText());
                                }
                                mappedProperties.put(sourceKey, propertyValues);
                            } else {
                                String value = node.valueOf(sourcePath);
                                mappedProperties.put(sourceKey, value);
                            }
                        }
                        SourceContent sourceContent = new SourceContent(contentKey, mappedProperties);
                        parsedItems.add(sourceContent);
                    }
                }
                return parsedItems;
            } catch (DocumentException e) {
                throw new ParsingException(e);
            }
        } else {
            LOGGER.error(file.getAbsolutePath() + " is not an XML file.");
            return new ArrayList(0);
        }
    }

    /**
     * <p>get a content key from the source</p>
     *
     * @param source a {@link org.dom4j.Document} object
     * @return a {@link java.lang.String} object
     */
    protected abstract String getContentKey(Document source);

    /**
     * <p>load xml file</p>
     *
     * @param file a {@link java.io.File} object
     * @return a {@link org.dom4j.Document} object
     * @throws DocumentException
     */
    protected Document loadXml(File file) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        return document;
    }

    /**
     * <p>get <field>rootPath</field></p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getRootPath() { return this.rootPath; }

    /**
     * <p>set <field>rootPath</field></p>
     *
     * @param rootPath a {@link java.lang.String} object
     */
    public void setRootPath(String rootPath) { this.rootPath = rootPath; }

}
