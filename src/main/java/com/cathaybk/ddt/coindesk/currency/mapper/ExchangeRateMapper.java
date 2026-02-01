package com.cathaybk.ddt.coindesk.currency.mapper;

import com.cathaybk.ddt.coindesk.base.util.DateUtil;
import com.cathaybk.ddt.coindesk.currency.dto.CoinDeskRes;
import com.cathaybk.ddt.coindesk.currency.dto.QueryExchangeRateRes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

public class ExchangeRateMapper {

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

    String formatRate(BigDecimal rate) {
        if (rate == null) {
            return null;
        }
        return rate.setScale(6, RoundingMode.HALF_UP).toPlainString();
    }

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
     * 給 mapper 用的輔助資料（DB 查回來後組成），故設為 package-private。
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
