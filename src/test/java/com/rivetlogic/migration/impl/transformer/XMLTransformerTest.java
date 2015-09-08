package com.rivetlogic.migration.impl.transformer;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * <p>XMLTransformerTest</p>
 */
public class XMLTransformerTest {

    @Test
    public void testTransform() throws Exception {
        ExtendedXmlTransformer transformer = new ExtendedXmlTransformer();

        // call a method without param
        Object noParamValue = transformer.transform("transformWithoutParam");
        assertEquals(noParamValue, "Success");

        // call a method with a single param
        String singleValue = (String) transformer.transform("transformToString", new Integer(2));
        assertEquals(singleValue, "2");

        // call a method with mulitple params and return type is List
        List result = (List) transformer.transform("transformToList", "value1", "value2");
        assertEquals(result.size(), 2);
        assertEquals(result.get(0), "value1");
        assertEquals(result.get(1), "value2");
    }

    @Test
    public void testPropertySetting() throws Exception {
        ExtendedXmlTransformer transformer = new ExtendedXmlTransformer();
        String path = "transformation:transformWithoutParam:publishedDate";
        if (path.startsWith(ExtendedXmlTransformer.TRANSFORMATION)) {
            String [] params = path.split(":");
            if (params.length > 1) {
                String methodName = params[1];
                assertEquals(methodName, "transformWithoutParam");
                Object result = (params.length > 2) ?
                        transformer.transform(methodName, Arrays.copyOfRange(params, 2, (params.length - 1))) :
                        transformer.transform(methodName);
                assertEquals(result, "Success");
            } else {
                fail();
            }
        }

    }

    class ExtendedXmlTransformer extends XMLTransformer {

        public String transformToString(Integer value) {
            return String.valueOf(value);
        }

        public String transformWithoutParam() {
            return "Success";
        }

        public List transformToList(String value1, String value2) {
            List<String> result = new ArrayList();
            result.add(value1);
            result.add(value2);
            return result;
        }
    }

}
