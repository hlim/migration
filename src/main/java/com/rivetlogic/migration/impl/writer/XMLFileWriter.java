package com.rivetlogic.migration.impl.writer;

import com.rivetlogic.migration.api.constant.MigrationConstants;
import com.rivetlogic.migration.api.creator.FileCreator;
import com.rivetlogic.migration.api.writer.Writer;
import com.rivetlogic.migration.api.model.TargetContent;
import com.rivetlogic.migration.impl.util.MigrationUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.BaseElement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * <p>XMLWriter</p>
 */
public class XMLFileWriter implements Writer {

    final static Logger LOGGER = Logger.getLogger(XMLFileWriter.class);

    public static final String EXTESNION = "xml";

    public void write(Properties configProperties, List<TargetContent> items, File file) throws IOException {
        throw new UnsupportedOperationException("Writing to a single XML file is not supported.");
    }

    public void write(Properties configProperties, List<TargetContent> items, String targetLocation, FileCreator creator) throws IOException {
        if (items != null) {
            for (TargetContent item : items) {
                try {
                    File file = creator.createFile(item, targetLocation, getExtension());
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    Document document = loadProtoType(configProperties, item.getContentKey());
                    this.updateDocument(item, document);
                    OutputFormat format = OutputFormat.createPrettyPrint();
                    FileWriter fileWriter = new FileWriter(file);
                    XMLWriter writer = new XMLWriter(fileWriter, format);
                    writer.write(document);
                    fileWriter.close();
                } catch (DocumentException e) {
                    LOGGER.error("Failed to create a file from " + item, e);
                }
            }
        }
    }

    public void updateDocument(TargetContent target, Document document) {
        Element root = document.getRootElement();
        if (target != null) {
            Map<String, Object> properties = target.getProperties();
            if (properties != null) {
                for (Map.Entry<String, Object> entry : properties.entrySet()) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Updating " + entry.getKey());
                    }
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    Node node = root.selectSingleNode(key);
                    if (node != null) {
                        if (value instanceof String) {
                            node.setText((String) value);
                        } else if (value instanceof List) {
                            List items = (List) value;
                            if (items != null) {
                                Element parentEl = (Element) node;
                                for (Object item : items) {
                                    if (item instanceof Map) {
                                        Map<String, String> itemMap = (Map<String, String>) item;
                                        Element itemEl = new BaseElement("item");
                                        for (Map.Entry<String, String> itemEntry : itemMap.entrySet()) {
                                            Element itemPropertyEl = new BaseElement(itemEntry.getKey());
                                            itemPropertyEl.setText(itemEntry.getValue());
                                            itemEl.add(itemPropertyEl);
                                        }
                                        parentEl.add(itemEl);
                                    } else {
                                        LOGGER.error("Expecting a List of Maps for " + key);
                                    }
                                }
                            }
                        } else {
                            LOGGER.error("Expecting a String or a List for " + key);
                        }
                    } else {
                        LOGGER.error(key + " is not defined in " + target.getContentKey());
                    }
                }
            }
        }
    }

    public String getExtension() {
        return EXTESNION;
    }

    /**
     * <p>load prototype XML</p>
     *
     * @param configProperties a {@link java.util.Properties} object
     * @param contentKey a {@link java.lang.String} object
     * @return a {@link org.dom4j.Document} object
     * @throws DocumentException
     */
    protected Document loadProtoType(Properties configProperties, String contentKey) throws DocumentException {
        String protoTypePath = MigrationUtils.getProtoTypePath(configProperties,
                configProperties.getProperty(MigrationConstants.CONFIG_LOCATION), contentKey);
        File inputFile = new File(protoTypePath);
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputFile);
        return document;
    }

}
