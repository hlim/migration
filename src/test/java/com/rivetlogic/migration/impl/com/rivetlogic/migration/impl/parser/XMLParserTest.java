package com.rivetlogic.migration.impl.com.rivetlogic.migration.impl.parser;

import com.rivetlogic.migration.api.model.SourceContent;
import com.rivetlogic.migration.impl.parser.XMLParser;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.testng.annotations.Test;

import javax.xml.transform.Source;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * <p>XMLParserTest</p>
 */
public class XMLParserTest {

    @Test
    public void testParse() {
        TestXMLParser parser = new TestXMLParser();
        parser.setRootPath("/site//article");
        Properties configProperties = mock(Properties.class);
        File file = new File("test.xml");
        try {
            List<SourceContent> sources = parser.parse(configProperties, file);
            assertEquals(sources.size(), 2);

            SourceContent source1 = sources.get(0);
            assertEquals(source1.getContentKey(), "sample");
            assertEquals(source1.getProperty("Title"), "First Article");
            List<String> categories1 = (List) source1.getProperty("Categories");
            assertEquals(categories1.size(), 2);
            assertEquals(categories1.get(0), "1");
            assertEquals(categories1.get(1), "5");

            SourceContent source2 = sources.get(1);
            assertEquals(source2.getContentKey(), "sample");
            assertEquals(source2.getProperty("Title"), "Second Article");
            List<String> categories2 = (List) source2.getProperty("Categories");
            assertEquals(categories2.size(), 1);
            assertEquals(categories2.get(0), "3");

        } catch (Exception e) {
            fail();
        }

    }

    class TestXMLParser extends XMLParser {

        @Override
        protected String getContentKey(Document source) {
            return "sample";
        }

        @Override
        protected Document loadXml(File file) {
            Document document = mock(Document.class);
            Element rootEl = mock(Element.class);

            List<Node> articleNodes = new ArrayList();

            Node articleNode1 = mock(Node.class);
            when(articleNode1.valueOf("/title")).thenReturn("First Article");
            List<Node> categoryNodes1 = new ArrayList();
            Node categoryNode11 = mock(Node.class);
            when(categoryNode11.getText()).thenReturn("1");
            Node categoryNode12 = mock(Node.class);
            when(categoryNode12.getText()).thenReturn("5");
            categoryNodes1.add(categoryNode11);
            categoryNodes1.add(categoryNode12);
            when(articleNode1.selectNodes("/categories//categoryId")).thenReturn(categoryNodes1);
            articleNodes.add(articleNode1);

            Node articleNode2 = mock(Node.class);
            when(articleNode2.valueOf("/title")).thenReturn("Second Article");
            List<Node> categoryNodes2 = new ArrayList();
            Node categoryNode21 = mock(Node.class);
            when(categoryNode21.getText()).thenReturn("3");
            categoryNodes2.add(categoryNode21);
            when(articleNode2.selectNodes("/categories//categoryId")).thenReturn(categoryNodes2);
            articleNodes.add(articleNode2);

            when(rootEl.selectNodes("/site//article")).thenReturn(articleNodes);
            when(document.getRootElement()).thenReturn(rootEl);

            return document;
        }

        @Override
        protected Properties getSourceProperties(Properties configProperties, String contentKey) throws IOException {
            Properties sourceProperties = new Properties();
            sourceProperties.put("Title", "/title");
            sourceProperties.put("Categories", "/categories//categoryId");
            return sourceProperties;
        }

    }

}
