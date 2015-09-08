package com.rivetlogic.migration.api.scanner;

/**
 * <p>ContentScanner</p>
 */
public interface Scanner {

    /**
     * <p>scan content</p>
     *
     * @param content a {@link java.lang.StringBuffer} object
     * @return a {@link java.lang.StringBuffer} object
     */
    public StringBuffer scan(StringBuffer content);

}
