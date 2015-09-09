package com.rivetlogic.migration.impl.writer;

import com.rivetlogic.migration.api.creator.FileCreator;
import com.rivetlogic.migration.api.model.TargetContent;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.fail;

/**
 * <p>XMLFileWriterTest</p>
 */
public class XMLFileWriterTest {

    @Test
    public void testWrite() throws Exception {
        Document document = mock(Document.class);
        Element rootEl = mock(Element.class);
        when(document.getRootElement()).thenReturn(rootEl);
        Node titleNode = mock(Node.class);
        when(rootEl.selectSingleNode("title")).thenReturn(titleNode);
        Element categoriesNode = mock(Element.class);
        when(rootEl.selectSingleNode("categories")).thenReturn(categoriesNode);

        SampleXMLFileWriter writer = new SampleXMLFileWriter(document);
        Properties configProperties = new Properties();
        File file = new File("");

        List<TargetContent> items = new ArrayList();
        String [] categoryIds1 =  {"1", "5"};
        TargetContent item1 = createItem("First Article", categoryIds1);
        items.add(item1);
        String [] categoryIds2 =  {"3", "5"};
        TargetContent item2 = createItem("Second Article", categoryIds2);
        items.add(item2);


        try {
            writer.write(configProperties, items, file);
            fail();
        } catch (UnsupportedOperationException e) {
            // expecting this exception
        }

        FileCreator fileCreator = mock(FileCreator.class);
        File targetFile = mock(File.class);
        File parentFile = mock(File.class);
        when(fileCreator.createFile(any(TargetContent.class), any(String.class), any(String.class))).thenReturn(targetFile);
        when(targetFile.getParentFile()).thenReturn(parentFile);
        when(parentFile.exists()).thenReturn(true);
        writer.write(configProperties, items, "/folder", fileCreator);

        verify(parentFile, times(2)).exists();
        verifyNoMoreInteractions(parentFile);
        verify(titleNode, times(1)).setText("First Article");
        verify(titleNode, times(1)).setText("Second Article");
        verify(categoriesNode, times(4)).add(any(Element.class));


    }

    /**
     * <p>create a target item</p>
     * @param title a {@link java.lang.String} object
     * @param categoryIds a {@link java.util.List} object
     * @return a {@link com.rivetlogic.migration.api.model.TargetContent} object
     */
    private TargetContent createItem(String title, String [] categoryIds) {
        Map<String, Object> properties = new HashMap();
        properties.put("title", title);
        // add categories
        List<Map<String, String>> categories = new ArrayList();
        for (String categoryId : categoryIds) {
            Map<String, String> categoryMap = new HashMap();
            categoryMap.put("key", categoryId);
            categoryMap.put("value", categoryId);
            categories.add(categoryMap);
        }
        properties.put("categories", categories);
        TargetContent item = new TargetContent("sample", properties);
        return item;
    }


    class SampleXMLFileWriter extends XMLFileWriter {

        private Document document;

        public SampleXMLFileWriter(Document document) {
            this.document = document;
        }

        @Override
        protected Document loadProtoType(Properties configProperties, String contentKey) throws DocumentException {
            return this.document;
        }

        @Override
        protected void writeToFile(Document document, File file) throws IOException {
            // do nothing
        }
    }


}
