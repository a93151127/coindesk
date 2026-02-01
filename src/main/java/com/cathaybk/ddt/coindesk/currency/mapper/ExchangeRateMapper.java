package com.cathaybk.ddt.coindesk.currency.mapper;

import com.cathaybk.ddt.coindesk.base.util.DateUtil;
import com.cathaybk.ddt.coindesk.currency.dto.CoinDeskRes;
import com.cathaybk.ddt.coindesk.currency.dto.QueryExchangeRateRes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

public class ExchangeRateMapper {

    /**
     * 將匯率對照表與幣別輔助資訊轉換為 API 回傳的匯率項目清單。
     *
     *
     * @param rateMapping      幣別代碼與匯率的對照表
     * @param currencyInfoMap  幣別輔助資訊
     * @return 匯率項目清單，若無資料則回傳空集合
     */
    public List<QueryExchangeRateRes.RateItem> toRateItems(
            Map<String, BigDecimal> rateMapping,
            Map<String, CurrencyInfo> currencyInfoMap
    ) {
        if (rateMapping == null || rateMapping.isEmpty()) {
            return Collections.emptyList();
        }

        List<QueryExchangeRateRes.RateItem> rateItems = new ArrayList<>();

        for (Map.Entry<String, BigDecimal> entry : rateMapping.entrySet()) {
            QueryExchangeRateRes.RateItem item = new QueryExchangeRateRes.RateItem();

            String currencyCode = entry.getKey();
            BigDecimal rate = entry.getValue();

            item.setCurrencyCode(currencyCode);
            item.setRate(formatRate(rate));

            CurrencyInfo info = (currencyInfoMap == null) ? null : currencyInfoMap.get(currencyCode);
            if (info != null) {
                item.setCurrencyNameZh(info.getCurrencyNameZh());
                item.setUpdatedAt(DateUtil.transLocalDateTimeToString(
                        info.getUpdatedAt(),
                        DateUtil.FORMATTER_YYYYMMDD_HHMMSS
                ));
            }

            rateItems.add(item);
        }

        return rateItems;
    }

    /**
     * 將匯率數值統一格式化為字串。
     *
     * @param rate 原始匯率數值
     * @return 格式化後的匯率字串，若輸入為 null 則回傳 null
     */
    String formatRate(BigDecimal rate) {
        if (rate == null) {
            return null;
        }
        return rate.setScale(6, RoundingMode.HALF_UP).toPlainString();
    }

    /**
     * 從 CoinDesk API 回傳結果中擷取幣別與匯率資訊。
     *
     * @param src CoinDesk API 回傳的原始資料
     * @return 幣別代碼與匯率的對照表，若無資料則回傳空 Map
     */
    public Map<String, BigDecimal> extractCurrencyRates(CoinDeskRes src) {
        Map<String, BigDecimal> rateMapping = new HashMap<>();
        if (src == null || src.getBpi() == null) {
            return rateMapping;
        }

        for (CoinDeskRes.BpiInfo bpi : src.getBpi().values()) {
            if (bpi == null) {
                continue;
            }
            rateMapping.put(bpi.getCode(), bpi.getRateFloat());
        }
        return rateMapping;
    }

    /**
     * 提供 Mapper 使用的幣別輔助資料。
     */
    public static class CurrencyInfo {
        private final String currencyNameZh;
        private final LocalDateTime updatedAt;

        public CurrencyInfo(String currencyNameZh, LocalDateTime updatedAt) {
            this.currencyNameZh = currencyNameZh;
            this.updatedAt = updatedAt;
        }

        String getCurrencyNameZh() {
            return currencyNameZh;
        }

        LocalDateTime getUpdatedAt() {
            return updatedAt;
        }
    }
}
