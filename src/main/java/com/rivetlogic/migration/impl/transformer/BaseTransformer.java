package com.rivetlogic.migration.impl.transformer;

import com.rivetlogic.migration.api.constant.MigrationConstants;
import com.rivetlogic.migration.api.exception.TransformationException;
import com.rivetlogic.migration.api.transformer.Transformer;
import com.rivetlogic.migration.impl.util.MigrationUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * <p>BaseTransformer</p>
 */
public abstract class BaseTransformer implements Transformer {

    /**
     * <p>transform by invoking a method specified</p>
     *
     * @param methodName a {@link java.lang.String} object
     * @param params an array of {@link java.lang.Object} object
     * @return a {@link java.lang.Object} object
     * @throws TransformationException
     */
    public Object transform(String methodName, Object... params) throws TransformationException {
        try {
            Method method;
            if (params != null) {
                Class[] paramTypes = new Class[params.length];
                for (int index = 0; index < params.length; index++) {
                    if (params[index] != null) {
                        paramTypes[index] = params[index].getClass();
                    } else {
                        paramTypes[index] = Object.class;
                    }
                }
                method = this.getClass().getMethod(methodName, paramTypes);
                return method.invoke(this, params);
            } else {
                method = this.getClass().getMethod(methodName);
                return method.invoke(this, params);
            }
        } catch (NoSuchMethodException e) {
            throw new TransformationException("Error while invoking " + methodName + " with " + params, e);
        } catch (InvocationTargetException e) {
            throw new TransformationException("Error while invoking " + methodName + " with " + params, e);
        } catch (IllegalAccessException e) {
            throw new TransformationException("Error while invoking " + methodName + " with " + params, e);
        }
    }

    /**
     * <p>get target properties</p>
     *
     * @param configProperties a {@link java.util.Properties} object
     * @param contentKey a {@link java.lang.String} object
     * @return a {@link java.util.Properties} object
     * @throws IOException
     */
    protected Properties getTargetProperties(Properties configProperties, String contentKey) throws TransformationException {
        try {
            return MigrationUtils.getProperties(configProperties, contentKey,
                    MigrationConstants.CONFIG_TARGET_FOLDER_NAME, MigrationConstants.CONFIG_TARGET_FILE_EXTENSION);
        } catch (IOException e) {
            throw new TransformationException(e);
        }
    }

}
