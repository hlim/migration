package com.rivetlogic.migration;

import com.rivetlogic.migration.api.constant.MigrationConstants;
import com.rivetlogic.migration.api.creator.FileCreator;
import com.rivetlogic.migration.api.parser.Parser;
import com.rivetlogic.migration.api.transformer.Transformer;
import com.rivetlogic.migration.api.writer.Writer;
import com.rivetlogic.migration.api.exception.ParsingException;
import com.rivetlogic.migration.api.exception.TransformationException;
import com.rivetlogic.migration.api.model.SourceContent;
import com.rivetlogic.migration.api.model.TargetContent;
import com.rivetlogic.migration.impl.util.MigrationUtils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.IOUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 */
public class MigrationMain {

    final static Logger LOGGER = Logger.getLogger(MigrationMain.class);

    private Properties configProperties;
    private ApplicationContext context;

    public static void main (String [] args) throws Exception {
        if (args.length == 1) {
            String configLocation = args[0];
            MigrationMain migrationMain = new MigrationMain("classpath:migration-context.xml", configLocation);

            migrationMain.run();
        } else {
            LOGGER.error("MigrationMain configFolder");
        }
    }

    public MigrationMain(String contextPath, String configLocation) {
        try {
            this.configProperties = MigrationUtils.loadProperties(configLocation + "/" + MigrationConstants.CONFIG_FILE_NAME);
            this.configProperties.put(MigrationConstants.CONFIG_LOCATION, configLocation);
            this.context = new FileSystemXmlApplicationContext(contextPath);
        } catch (IOException e) {
            LOGGER.error("Error while loading " + configLocation + "/" + MigrationConstants.CONFIG_FILE_NAME, e);
        }
    }

    /**
     * <p>run </p>
     * @throws Exception
     */
    public void run() throws Exception {
        String sourceFolerName = (String) configProperties.get(MigrationConstants.CONFIG_SOURCE_FOLDER);
        if (sourceFolerName != null && sourceFolerName.length() > 0) {
            File folder = new File(sourceFolerName);
            run(folder);
        } else {
            LOGGER.error("No source folder is found.");
        }
    }

    /**
     * <p>run migration on a folder</p>
     *
     * @param sourceFolder a {@link java.io.File} object
     * @throws Exception
     */
    public void run(File sourceFolder) throws Exception {
        if (sourceFolder.isDirectory() && sourceFolder.exists()) {
            LOGGER.debug("Processing " + sourceFolder.getAbsolutePath());
            File [] files = sourceFolder.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    run(file);
                } else {
                    process(file);
                }
            }
        } else {
            throw new IOException(sourceFolder.getAbsolutePath() + " is not a directory or doesn't exist");
        }
    }

    /**
     * <p>run migration on a single source file</p>
     *
     * @param sourceFile a {@link java.io.File} object
     * @throws Exception
     */
    private void process(File sourceFile) throws Exception {
        List<SourceContent> sources = parse(sourceFile);
        List<TargetContent> targets = transform(sources);
        writeToFile(targets);
    }

    /**
     * <p>parse a source file</p>
     *
     * @param sourceFile a {@link java.io.File} object
     * @return a {@link java.util.List} object
     * @throws ParsingException
     */
    public List<SourceContent> parse(File sourceFile) throws ParsingException {
        Parser parser = (Parser) context.getBean((String) configProperties.get(MigrationConstants.CONFIG_PARSER));
        if (parser != null) {
            try {
                List<SourceContent> sources = parser.parse(configProperties, sourceFile);
                return sources;
            } catch (IOException e) {
                throw new ParsingException("Error while parsing " + sourceFile.getAbsolutePath(), e);
            } catch (InvalidFormatException e) {
                throw new ParsingException("Error while parsing " + sourceFile.getAbsolutePath(), e);
            }
        } else {
            throw new ParsingException("No parser defined with name: " + configProperties.get(MigrationConstants.CONFIG_PARSER));
        }
    }
    /**
     * <p>transform source to target</p>
     *
     * @param sources a {@link java.util.List} object
     * @return a {@link java.util.List} object
     * @throws TransformationException
     */
    public List<TargetContent> transform(List<SourceContent> sources) throws TransformationException {
        Transformer transformer = (Transformer) context.getBean((String) configProperties.get(MigrationConstants.CONFIG_TRANSFORMER));
        if (transformer != null) {
            List<TargetContent> targets = new ArrayList();
            for (SourceContent source : sources) {
                TargetContent target = transformer.transform(configProperties, source);
                targets.add(target);
            }
            return targets;
        } else {
            throw new TransformationException("No transformer defined with name: " + configProperties.get(MigrationConstants.CONFIG_TRANSFORMER));
        }
    }

    /**
     * <p>write output files</p>
     *
     * @param targets a {@link java.util.List} object
     * @throws Exception
     */
    public void writeToFile(List<TargetContent> targets) throws Exception {
        Writer xmlWriter = (Writer) context.getBean((String) configProperties.get(MigrationConstants.CONFIG_WRITER));
        FileCreator fileCreator = (FileCreator) context.getBean((String) configProperties.get(MigrationConstants.CONFIG_FILE_CREATOR));
        if (xmlWriter != null && fileCreator != null) {
            String targetFolder = (String) configProperties.get(MigrationConstants.CONFIG_TARGET_FOLDER);
            xmlWriter.write(configProperties, targets, targetFolder, fileCreator);
        } else {
            if (xmlWriter == null)
                throw new Exception("No writer defined with name: " + configProperties.get(MigrationConstants.CONFIG_WRITER));
            if (fileCreator == null)
                throw new Exception("No fileCreator defined with name: " + configProperties.get(MigrationConstants.CONFIG_FILE_CREATOR));

        }
    }


}
