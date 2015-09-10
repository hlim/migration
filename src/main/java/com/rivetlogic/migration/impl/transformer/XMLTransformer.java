package com.rivetlogic.migration.impl.transformer;

import com.rivetlogic.migration.api.exception.TransformationException;
import com.rivetlogic.migration.api.model.SourceContent;
import com.rivetlogic.migration.api.model.TargetContent;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * <p>XMLTransformer</p>
 */
public abstract class XMLTransformer extends BaseTransformer {

    final static Logger LOGGER = Logger.getLogger(XMLTransformer.class);

    public static final String TRANSFORMATION = "transformation:";

    public TargetContent transform(Properties configProperties, SourceContent item) throws TransformationException {
        Properties targetProperties = getTargetProperties(configProperties, item.getContentKey());
        Map<String, Object> properties = new HashMap();
        if (targetProperties != null) {
            Enumeration pathNames = targetProperties.propertyNames();
            while (pathNames.hasMoreElements()) {
                String pathName = (String) pathNames.nextElement();
                String path = targetProperties.getProperty(pathName);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Transforming " + pathName + " with " + path);
                }
                if (path.startsWith(TRANSFORMATION)) {
                    String [] params = path.split(":");
                    if (params.length > 2) {
                        String methodName = params[1];
                        String targetPath = params[2];
                        Object sourceParam = item.getProperty(pathName);
                        List paramList = new ArrayList();
                        paramList.add(sourceParam);
                        for (int index = 3; index < params.length; index++) {
                            paramList.add(item.getProperty(params[index]));
                        }
                        Object result = transform(methodName, paramList.toArray());
                        properties.put(targetPath, result);
                    } else {
                        throw new TransformationException("Transformation must define a method name and a target path in " + path);
                    }
                } else {
                    properties.put(targetProperties.getProperty(pathName), item.getProperty(pathName));
                }
            }
        }
        TargetContent target = new TargetContent(item.getContentKey(), properties);
        return target;
    }

}
