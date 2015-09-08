package com.rivetlogic.migration.api.exception;

/**
 * <p>TransformationException</p>
 */
public class TransformationException extends Exception {

    public TransformationException() {
        super();
    }

    public TransformationException(String msg) {
        super(msg);
    }

    public TransformationException(String msg, Exception e) {
        super(msg, e);
    }

    public TransformationException(Exception e) {
        super(e);
    }

}
