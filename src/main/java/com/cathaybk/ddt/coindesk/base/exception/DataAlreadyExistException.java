package com.cathaybk.ddt.coindesk.base.exception;

public class DataAlreadyExistException extends RuntimeException{

    public DataAlreadyExistException() {
        super();
    }

    public DataAlreadyExistException(String message) {
        super(message);
    }

    public DataAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

}
