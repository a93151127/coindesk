package com.cathaybk.ddt.coindesk.base.exception;

public class ExternalResponseParseException extends RuntimeException {

    public ExternalResponseParseException() {
        super();
    }

    public ExternalResponseParseException(String message) {
        super(message);
    }

    public ExternalResponseParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
