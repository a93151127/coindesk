package com.cathaybk.ddt.coindesk.currency.service.impl;

import com.cathaybk.ddt.coindesk.base.util.DateUtil;
import com.cathaybk.ddt.coindesk.base.util.HttpUtil;
import com.cathaybk.ddt.coindesk.base.util.JsonParseUtil;
import com.cathaybk.ddt.coindesk.currency.dto.CoinDeskRes;
import com.cathaybk.ddt.coindesk.currency.dto.QueryExchangeRateRes;
import com.cathaybk.ddt.coindesk.currency.entity.CurrencyMapping;
import com.cathaybk.ddt.coindesk.currency.repository.CurrencyMappingRepository;
import com.cathaybk.ddt.coindesk.currency.service.QueryExchangeRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class QueryExchangeRateServiceImpl implements QueryExchangeRateService {
    private final Logger logger = LoggerFactory.getLogger(QueryExchangeRateServiceImpl.class);

    @Value("${coindesk.url}")
    private String coindeskUrl;

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private JsonParseUtil jsonParseUtil;

    @Autowired
    private CurrencyMappingRepository currencyMappingRepository;

    @Override
    public List<QueryExchangeRateRes.RateItem> queryExchangeRates() {
        logger.info("queryExchangeRates start");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(java.util.Collections.singletonList(MediaType.APPLICATION_JSON));

        // 呼叫外部 CoinDesk API 取得原始匯率資料（JSON 字串）
        String apiResult = httpUtil.get(coindeskUrl, headers);

        // 將外部系統回傳的 JSON 字串解析為 CoinDeskRes 物件
        CoinDeskRes coinDeskRes = jsonParseUtil.parse(apiResult, CoinDeskRes.class);

        // 從解析後的資料中抽取各幣別對應的匯率數值，建立幣別與匯率的對照 Map
        Map<String, BigDecimal> rateMapping = extractCurrencyRates(coinDeskRes);

        return convertToRateItems(rateMapping);
    }

    /**
     * 將匯率對照資料轉換為 API 回應所需的匯率項目清單。
     *
     * @param rateMapping 幣別代碼對應匯率數值的對照 Map
     * @return 匯率回應項目清單
     */
    private List<QueryExchangeRateRes.RateItem> convertToRateItems(Map<String, BigDecimal> rateMapping) {
        List<QueryExchangeRateRes.RateItem> rateItems = new ArrayList<>();

        // 取得幣別輔助資訊（中文名稱、更新時間），以幣別代碼作為 key
        Map<String, CurrencyInfo> currencyInfoMap = obtainCurrencyInfo();

        // 以外部系統回傳的匯率資料為主體進行資料整合
        for (Map.Entry<String, BigDecimal> entry : rateMapping.entrySet()) {
            QueryExchangeRateRes.RateItem item = new QueryExchangeRateRes.RateItem();

            item.setCurrencyCode(entry.getKey());
            item.setRate(formatRate(entry.getValue()));

            // 依幣別代碼補齊資料庫中維護的中文名稱與更新時間（輔助資料）
            CurrencyInfo currencyInfo = currencyInfoMap.get(entry.getKey());
            if (currencyInfo != null) {
                item.setCurrencyNameZh(currencyInfo.getCurrencyNameZh());
                item.setUpdatedAt(
                        DateUtil.transLocalDateTimeToString(
                            currencyInfo.getUpdatedAt(),
                            DateUtil.FORMATTER_YYYYMMDD_HHMMSS
                ));
            }else{
                logger.warn("Currency info not found in DB, currencyCode={}", entry.getKey());
            }

            rateItems.add(item);
        }
        return rateItems;
    }

    /**
     * 將匯率數值格式化為固定六位小數的字串表示，並採用四捨五入（HALF_UP）規則。
     *
     * @param rate 原始匯率數值
     * @return 格式化後的小數六位匯率字串，若輸入為 null 則回傳 null
     */
    private String formatRate(BigDecimal rate) {
        if (rate == null) {
            return null;
        }
        return rate.setScale(6, RoundingMode.HALF_UP)
                .toPlainString();
    }

    /**
     * 從 CoinDesk 回傳資料中抽取各幣別對應的匯率。
     *
     * @param src CoinDesk API 回傳資料
     * @return 幣別與匯率對應的 Map
     */
    private Map<String, BigDecimal> extractCurrencyRates(CoinDeskRes src) {
        Map<String, BigDecimal> rateMapping = new HashMap<>();

        if (src.getBpi() == null) {
            return rateMapping;
        }

        for (CoinDeskRes.BpiInfo bpi : src.getBpi().values()) {
            rateMapping.put(bpi.getCode(), bpi.getRateFloat());
        }

        return rateMapping;
    }

    /**
     * 從資料庫載入幣別相關輔助資訊，並轉換為以幣別代碼為 key 的對照 Map。
     *
     * @return 幣別代碼對應中文名稱與更新時間的 Map
     */
    private Map<String, CurrencyInfo> obtainCurrencyInfo(){
        List<CurrencyMapping> currencyList = currencyMappingRepository.findAll();
        Map<String, CurrencyInfo> currencyInfoMap = new HashMap<>();

        for (CurrencyMapping currency : currencyList) {
            currencyInfoMap.put(currency.getCurrencyCode(), new CurrencyInfo(currency.getCurrencyNameZh(), currency.getUpdatedAt()));
        }
        return currencyInfoMap;
    }

    private static class CurrencyInfo {
        private String currencyNameZh;
        private LocalDateTime updatedAt;

        public CurrencyInfo(String currencyNameZh, LocalDateTime updatedAt) {
            this.currencyNameZh = currencyNameZh;
            this.updatedAt = updatedAt;
        }

        public String getCurrencyNameZh() {
            return currencyNameZh;
        }

        public void setCurrencyNameZh(String currencyNameZh) {
            this.currencyNameZh = currencyNameZh;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
