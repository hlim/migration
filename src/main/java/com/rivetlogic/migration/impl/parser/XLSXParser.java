package com.rivetlogic.migration.impl.parser;

import com.rivetlogic.migration.api.exception.ParsingException;
import com.rivetlogic.migration.api.model.SourceContent;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * <p>Parses Microsoft Excel files</p>
 */
public abstract class XLSXParser extends BaseParser {

    final static Logger LOGGER = Logger.getLogger(XLSXParser.class);

    public List<SourceContent> parse(Properties configProperties, File file) throws ParsingException, IOException, InvalidFormatException {
        Workbook wb = WorkbookFactory.create(file);
        Sheet sheet = wb.getSheetAt(0);
        List<SourceContent> parsedItems = new ArrayList();
        Iterator<Row> rowIter = sheet.rowIterator();
        // get header names
        String [] columnHeaders = getColumHeaders(rowIter);
        if (rowIter.hasNext()) {
            while (rowIter.hasNext()) {
                Row row = rowIter.next();
                String contentKey = getContentKey(file, row);
                Properties sourceProperties = getSourceProperties(configProperties, contentKey);
                if (row.getPhysicalNumberOfCells() == columnHeaders.length) {
                    Map<String, Object> parsedProperties = parseCell(row, columnHeaders);
                    if (sourceProperties != null) {
                        Map<String, Object> mappedProperties = new HashMap();
                        Enumeration sourceKeys = sourceProperties.propertyNames();
                        while (sourceKeys.hasMoreElements()) {
                            String sourceKey = (String) sourceKeys.nextElement();
                            String sourceName = sourceProperties.getProperty(sourceKey);
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("Mapping " + sourceName + " to " + sourceKey);
                            }
                            Object sourceValue = parsedProperties.get(sourceName);
                            mappedProperties.put(sourceKey, sourceValue);
                        }
                        SourceContent sourceContent = new SourceContent(contentKey, mappedProperties);
                        parsedItems.add(sourceContent);
                    }
                } else {
                    throw new ParsingException("The max number of cells (" + columnHeaders.length + ") does not match in row "
                            + row.getRowNum());
                }
            }
        }
        return parsedItems;
    }

    /**
     * <p>get a content key from a row</p>
     *
     * @param file a {@link java.io.File} object
     * @param row a {@link org.apache.poi.ss.usermodel.Row} object
     * @return a {@link java.lang.String} object
     */
    protected abstract String getContentKey(File file, Row row);

    /**
     * <p>parse cells in a row</p>
     *
     * @param row a {@link org.apache.poi.ss.usermodel.Row} object
     * @param columnHeaders an array of a {@link java.lang.String} object
     * @return a {@link java.util.Map} object
     * @throws ParsingException
     */
    private Map<String, Object> parseCell(Row row, String [] columnHeaders) throws ParsingException {
        Iterator<Cell> cellIter = row.cellIterator();
        Map<String, Object> parsedProperties = new HashMap();
        while (cellIter.hasNext()) {
            Cell cell = cellIter.next();
            String key = columnHeaders[cell.getColumnIndex()];
            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                parsedProperties.put(key, cell.getDateCellValue());
            } else {
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_NUMERIC:
                        parsedProperties.put(key, cell.getNumericCellValue());
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        parsedProperties.put(key, cell.getBooleanCellValue());
                        break;
                    case Cell.CELL_TYPE_STRING:
                        parsedProperties.put(key, cell.getStringCellValue());
                        break;
                    case Cell.CELL_TYPE_BLANK:
                        parsedProperties.put(key, null);
                        break;
                    default:
                        throw new ParsingException("Invalid data type: " + cell.getCellType() + " found at row:"
                                + row.getRowNum() + " and cell: " + cell.getColumnIndex());
                }
            }
        }
        return parsedProperties;
    }


    /**
     * <p>get an array of header column names</p>
     *
     * @param rowIter a {@link java.util.Iterator} object
     * @return an array of {@link java.lang.String} object
     * @throws ParsingException
     */
    private String [] getColumHeaders(Iterator<Row> rowIter) throws ParsingException {
        if (rowIter.hasNext()) {
            Row titleRow = rowIter.next();
            String [] headerColumns = new String [titleRow.getPhysicalNumberOfCells()];
            Iterator<Cell> cellIter = titleRow.cellIterator();
            while (cellIter.hasNext()) {
                Cell cell = cellIter.next();
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_NUMERIC:
                        headerColumns[cell.getColumnIndex()] = String.valueOf(cell.getNumericCellValue());
                        break;
                    case Cell.CELL_TYPE_STRING:
                        headerColumns[cell.getColumnIndex()] = cell.getStringCellValue();
                        break;
                    default:
                        throw new ParsingException("Header name must be either String or Numeric at column "
                                + cell.getColumnIndex());
                }
            }
            return headerColumns;
        } else {
            throw new ParsingException("No row found");
        }
    }

}
