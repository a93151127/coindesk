package com.cathaybk.ddt.coindesk.currency.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "CURRENCY_MAPPING")
public class CurrencyMapping {

    // 幣別代碼
    @Id
    @Column(name = "CURRENCY_CODE", length = 5, nullable = false)
    private String currencyCode;


    // 幣別中文名稱，例如 美元、歐元
    @Column(name = "CURRENCY_NAME_ZH", length = 10, nullable = false)
    private String currencyNameZh;

    // 建立時間
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    // 更新時間
    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
