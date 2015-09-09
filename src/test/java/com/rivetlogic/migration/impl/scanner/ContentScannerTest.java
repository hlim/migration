package com.rivetlogic.migration.impl.scanner;

import org.testng.annotations.Test;

import java.util.regex.Pattern;

import static org.testng.Assert.assertEquals;

/**
 * <p>ContentScannerTest</p>
 */
public class ContentScannerTest {

    @Test
    public void testScan() throws Exception {
        SampleConnectScanner scanner = new SampleConnectScanner();
        StringBuffer source = new StringBuffer("<img src=\"/wp-content/uploads/2015/09/test.jpg\"/><img src=\"/image-folder/2015/09/test.jpg\"/>");
        StringBuffer target = scanner.scan(source);
        String result = target.toString();
        assertEquals(result, "<img src=\"IMAGE_SCANNED\"/><img src=\"/image-folder/2015/09/test.jpg\"/>");
    }

    class SampleConnectScanner extends  ContentScanner {

        @Override
        protected String transform(String front, String target, String end, String match) {
            return "\"IMAGE_SCANNED\"";
        }

        @Override
        protected Pattern getPattern() {
            return Pattern.compile("\"(/wp-content/uploads/[0-9]{4}/[0-9]{2}/([^\"]+))\"");
        }

        @Override
        protected int getTargetGroup() {
            return 1;
        }
    }
}
