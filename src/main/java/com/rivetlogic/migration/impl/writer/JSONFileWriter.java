package com.rivetlogic.migration.impl.writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rivetlogic.migration.api.creator.FileCreator;
import com.rivetlogic.migration.api.writer.Writer;
import com.rivetlogic.migration.api.model.TargetContent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
* <p>JSON Writer</p>
 */
public class JSONFileWriter implements Writer {

    public static final String EXTESNION = "json";

    public String getExtension() {
        return EXTESNION;
    }

    public void write(Properties configProperties, List<TargetContent> items, File file) throws IOException {
        java.io.FileWriter fileFileWriter = new java.io.FileWriter(file);
        try {
            List<Map<String, Object>> jsonItems = new ArrayList();
            if (items != null) {
                for (TargetContent item : items) {
                    jsonItems.add(item.getProperties());
                }
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            fileFileWriter.write(gson.toJson(jsonItems));
        } finally {
            fileFileWriter.close();
        }
    }

    public void write(Properties configProperties, List<TargetContent> items, String targetLocation, FileCreator creator) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (items != null) {
            for (TargetContent item : items) {
                File file = creator.createFile(item, targetLocation, getExtension());
                java.io.FileWriter fileFileWriter = new java.io.FileWriter(file);
                try {
                    fileFileWriter.write(gson.toJson(item.getProperties()));
                } finally {
                    fileFileWriter.close();
                }
            }
        }
    }

}
