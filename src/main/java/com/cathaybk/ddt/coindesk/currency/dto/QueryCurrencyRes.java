package com.cathaybk.ddt.coindesk.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class QueryCurrencyRes {

    // 匯率資料列表
    @JsonProperty("items")
    private List<CurrencyItem> items;

    public List<CurrencyItem> getItems() {
        return items;
    }

    public void setItems(List<CurrencyItem> items) {
        this.items = items;
    }

    public static class CurrencyItem {

        // 幣別代碼
        @JsonProperty("currencyCode")
        private String currencyCode;

        // 幣別中文名稱，例如 美元、歐元
        @JsonProperty("currencyNameZh")
        private String currencyNameZh;

        // 建立時間
        @JsonProperty("createdAt")
        private String createdAt;

        // 更新時間
        @JsonProperty("updatedAt")
        private String updatedAt;

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

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
