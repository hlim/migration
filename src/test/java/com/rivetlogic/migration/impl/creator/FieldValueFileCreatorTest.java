package com.rivetlogic.migration.impl.creator;

import com.rivetlogic.migration.api.model.TargetContent;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <p>FieldValueFileCreatorTest</p>
 */
public class FieldValueFileCreatorTest {


    /**
     * <p>test a file creation based on a field in a target content</p>
     */
    @Test
    public void testFileCreation() {
        FieldValueFileCreator creator = new FieldValueFileCreator();
        creator.setFieldName("fileName");
        TargetContent item = mock(TargetContent.class);
        when(item.getProperty("fileName")).thenReturn("file1");
        File file = creator.createFile(item, "/folder1", "xml");
        assertEquals(file.getAbsolutePath(), "/folder1/file1.xml");
    }
}
