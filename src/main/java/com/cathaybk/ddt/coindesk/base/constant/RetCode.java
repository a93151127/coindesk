package com.cathaybk.ddt.coindesk.base.constant;

public enum RetCode {

    S0000("S0000", "操作成功"),
    D0001("D0001", "查無資料"),
    D0002("D0002", "資料已存在"),
    E5000("E5000", "參數錯誤"),
    E9999("E9999", "未知錯誤");

    private final String retCode;
    private final String retMsg;

    RetCode(String retCode, String retMsg) {
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    public String getRetCode() {
        return retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }
}

