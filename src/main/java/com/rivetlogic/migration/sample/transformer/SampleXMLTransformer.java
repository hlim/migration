package com.rivetlogic.migration.sample.transformer;

import com.rivetlogic.migration.api.scanner.Scanner;
import com.rivetlogic.migration.api.exception.TransformationException;
import com.rivetlogic.migration.impl.transformer.XMLTransformer;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>SampleXMLTransformer</p>
 */
public class SampleXMLTransformer extends XMLTransformer {

    final static Logger LOGGER = Logger.getLogger(SampleXMLTransformer.class);

    // input date format
    private String sourceDateFormat;
    // output date format
    private String targetDataFormat;

    /**
     * <p>processing multiple params example</p></p>
     *
     * @param testValue a {@link java.lang.String} object
     * @param title a {@link java.lang.String} object
     * @param dateStr a {@link java.lang.String} object
     * @return a {@link java.lang.Object} object
     */
    public Object processMultiParams(Object testValue, String title, String dateStr) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[" + title + ", " + dateStr + "]");
        }
        return "";
    }

    public Object transformContent(String body) throws TransformationException {
        return scanContent(body);
    }

    /**
     * <p>transform categories into new key and value properties</p>
     *
     * @param categoryIds a {@link java.util.ArrayList} object
     * @return a {@link java.lang.String} object
     */
    public Object transformCategories(ArrayList<String> categoryIds) {
        // TODO: find a way to avoid implementation type
        // this is due to the limitation of method invocation by param classes
        List<Map<String, String>> categories = new ArrayList();
        for (String categoryId : categoryIds) {
            Map<String, String> category = new HashMap();
            category.put("key", categoryId);
            category.put("value", categoryId);
            categories.add(category);
        }
        return categories;
    }

    /**
     * <p>convert a date to a string format</p>
     *
     * @param dateStr a {@link java.util.Date} object
     * @return a {@link java.lang.String} object
     */
    public Object formatDate(String dateStr) {
        if (dateStr != null) {
            try {
                SimpleDateFormat sourceFormat = new SimpleDateFormat(getSourceDateFormat());
                Date date = sourceFormat.parse(dateStr);
                SimpleDateFormat format = new SimpleDateFormat(getTargetDateFormat());
                return format.format(date);
            } catch (ParseException e) {
                LOGGER.error("Error while formatting a date: " + dateStr, e);
            }
        }
        return "";
    }

    /**
     * <p>get a file name</p>
     *
     * @param value a {@link java.lang.Object} object
     * @return a {@link java.lang.Object} object
     * @throws TransformationException
     */
    public Object getFolderName(String value) throws TransformationException {
        if (value != null) {
            return String.valueOf(value).trim().toLowerCase().replaceAll("[^a-z0-9\\-]+", "-");
        } else {
            throw new TransformationException("File name cannot be null.");
        }
    }

    /**
     * <p>set <field>sourceDataFormat</field></p>
     *
     * @param sourceDateFormat a {@link java.lang.String} object
     */
    public void setSourceDateFormat(String sourceDateFormat) { this.sourceDateFormat = sourceDateFormat; }

    /**
     * <p>get <field>sourceDateFormat</field></p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getSourceDateFormat() { return this.sourceDateFormat; }

    /**
     * <p>set <field>targetDataFormat</field></p>
     *
     * @param targetDataFormat a {@link java.lang.String} object
     */
    public void setTargetDateFormat(String targetDataFormat) { this.targetDataFormat = targetDataFormat; }

    /**
     * <p>get <field>targetDataFormat</field></p>
     * @return a {@link java.lang.String} object
     */
    public String getTargetDateFormat() { return this.targetDataFormat; }
}
