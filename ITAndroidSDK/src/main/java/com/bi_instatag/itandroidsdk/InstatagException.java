package com.bi_instatag.itandroidsdk;

public class InstatagException extends Exception {
    public InstatagException() {
        super();
    }

    public InstatagException(String message) {
        super(message);
    }

    public InstatagException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstatagException(Throwable cause) {
        super(cause);
    }
}
