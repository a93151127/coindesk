package com.cathaybk.ddt.coindesk.base.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    // 回傳代碼, 例如 E9999
    @JsonProperty("retCode")
    private String retCode;

    // 回傳訊息
    @JsonProperty("retMsg")
    private String retMsg;

    // 回傳資料, 可為 Object, List
    @JsonProperty("data")
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(String retCode, String retMsg, T data) {
        this.retCode = retCode;
        this.retMsg = retMsg;
        this.data = data;
    }

    public ApiResponse(String retCode, String retMsg) {
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
