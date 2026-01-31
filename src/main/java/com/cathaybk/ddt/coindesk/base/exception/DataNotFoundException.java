package com.cathaybk.ddt.coindesk.base.exception;

/**
 * 資料不存在例外。
 * <p>
 * 當系統依條件查詢資料，但資料庫中不存在對應資料時拋出。
 * 常用於查詢單筆資料（例如依幣別代碼查詢）且查無結果的情境。
 */
public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException() {
        super();
    }

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
