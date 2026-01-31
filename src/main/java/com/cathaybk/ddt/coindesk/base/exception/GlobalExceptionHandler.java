package com.cathaybk.ddt.coindesk.base.exception;

import com.cathaybk.ddt.coindesk.base.constant.RetCode;
import com.cathaybk.ddt.coindesk.base.model.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DataNotFoundException.class)
    public ApiResponse<Void> handleDataNotFound(DataNotFoundException ex) {
        return new ApiResponse<>(
                RetCode.D0001.getRetCode(),
                RetCode.D0001.getRetMsg()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(e ->
                        logger.warn("Validation failed: field={}, message={}",
                                e.getField(),
                                e.getDefaultMessage())
                );

        return new ApiResponse<>(
                RetCode.E5000.getRetCode(),
                RetCode.E5000.getRetMsg()
        );
    }

    @ExceptionHandler(DataAlreadyExistException.class)
    public ApiResponse<Void> handleDataAlreadyExist(DataAlreadyExistException ex){
        return new ApiResponse<>(
                RetCode.D0002.getRetCode(),
                RetCode.D0002.getRetMsg()
        );
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ApiResponse<Void> handleExternalService(ExternalServiceException ex){
        logger.error("External service error! ", ex);
        return new ApiResponse<>(
                RetCode.E5001.getRetCode(),
                RetCode.E5001.getRetMsg()
        );
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception ex){
        return new ApiResponse<>(
                RetCode.E9999.getRetCode(),
                RetCode.E9999.getRetMsg()
        );
    }
}
