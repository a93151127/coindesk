package com.cathaybk.ddt.coindesk.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class QueryExchangeRateRes {
    // 匯率資料列表
    @JsonProperty("items")
    private List<RateItem> items;

    public List<RateItem> getItems() {
        return items;
    }

    public void setItems(List<RateItem> items) {
        this.items = items;
    }

    public static class RateItem {

        // 幣別代碼
        @JsonProperty("currencyCode")
        private String currencyCode;

        // 幣別中文名稱，例如 美元、歐元
        @JsonProperty("currencyNameZh")
        private String currencyNameZh;

        // 匯率，小數點 6 位數
        @JsonProperty("rate")
        private String rate;

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

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
