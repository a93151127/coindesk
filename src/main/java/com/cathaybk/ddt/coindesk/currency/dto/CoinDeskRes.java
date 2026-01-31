package com.cathaybk.ddt.coindesk.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

public class CoinDeskRes {

    @JsonProperty("time")
    private TimeInfo time;

    @JsonProperty("disclaimer")
    private String disclaimer;

    @JsonProperty("chartName")
    private String chartName;


    // key = USD, GBP, EUR
    @JsonProperty("bpi")
    private Map<String, BpiInfo> bpi;

    public TimeInfo getTime() {
        return time;
    }

    public void setTime(TimeInfo time) {
        this.time = time;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public Map<String, BpiInfo> getBpi() {
        return bpi;
    }

    public void setBpi(Map<String, BpiInfo> bpi) {
        this.bpi = bpi;
    }

    public static class TimeInfo {

        @JsonProperty("updated")
        private String updated;

        @JsonProperty("updatedISO")
        private String updatedISO;

        @JsonProperty("updateduk")
        private String updateduk;

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public String getUpdatedISO() {
            return updatedISO;
        }

        public void setUpdatedISO(String updatedISO) {
            this.updatedISO = updatedISO;
        }

        public String getUpdateduk() {
            return updateduk;
        }

        public void setUpdateduk(String updateduk) {
            this.updateduk = updateduk;
        }
    }

    public static class BpiInfo {

        @JsonProperty("code")
        private String code;

        @JsonProperty("symbol")
        private String symbol;

        @JsonProperty("rate")
        private String rate;

        @JsonProperty("description")
        private String description;

        @JsonProperty("rate_float")
        private BigDecimal rateFloat;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public BigDecimal getRateFloat() {
            return rateFloat;
        }

        public void setRateFloat(BigDecimal rateFloat) {
            this.rateFloat = rateFloat;
        }
    }
}

