package com.cathaybk.ddt.coindesk.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdateCurrencyReq {

    @Size(max = 5, message = "size must be between 1 and 5")
    @NotBlank(message = "currencyCode cannot be blank")
    @JsonProperty("currencyCode")
    private String currencyCode;

    @Size(max = 10, message = "size must be between 1 and 10")
    @NotBlank(message = "currencyNameZh cannot be blank")
    @JsonProperty("currencyNameZh")
    private String currencyNameZh;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyNameZh() {
        return currencyNameZh;
    }

    public void setCurrencyNameZh(String currencyNameZh) {
        this.currencyNameZh = currencyNameZh;
    }
}
