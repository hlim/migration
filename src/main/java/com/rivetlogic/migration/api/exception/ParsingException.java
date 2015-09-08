package com.rivetlogic.migration.api.exception;

/**
 * <p>ParsingException</p>
 */
public class ParsingException extends Exception {

    public ParsingException() {
        super();
    }

    public ParsingException(String msg) {
        super(msg);
    }

    public ParsingException(String msg, Exception e) {
        super(msg, e);
    }

    public ParsingException(Exception e) {
        super(e);
    }
}
