package com.cathaybk.ddt.coindesk.currency.service.impl;

import com.cathaybk.ddt.coindesk.base.util.HttpUtil;
import com.cathaybk.ddt.coindesk.base.util.JsonParseUtil;
import com.cathaybk.ddt.coindesk.currency.dto.CoinDeskRes;
import com.cathaybk.ddt.coindesk.currency.dto.QueryExchangeRateRes;
import com.cathaybk.ddt.coindesk.currency.entity.CurrencyMapping;
import com.cathaybk.ddt.coindesk.currency.mapper.ExchangeRateMapper;
import com.cathaybk.ddt.coindesk.currency.repository.CurrencyMappingRepository;
import com.cathaybk.ddt.coindesk.currency.service.QueryExchangeRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

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

    private  ExchangeRateMapper mapper = new ExchangeRateMapper();

    @Override
    public List<QueryExchangeRateRes.RateItem> queryExchangeRates() {
        logger.info("queryExchangeRates start");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String apiResult = httpUtil.get(coindeskUrl, headers);
        CoinDeskRes coinDeskRes = jsonParseUtil.parse(apiResult, CoinDeskRes.class);

        // 外部 DTO 轉換為匯率對照資料
        Map<String, java.math.BigDecimal> rateMapping = mapper.extractCurrencyRates(coinDeskRes);

        // 從資料庫載入幣別輔助資訊
        Map<String, ExchangeRateMapper.CurrencyInfo> currencyInfoMap = obtainCurrencyInfo();

        // 將匯率資料與輔助資訊整合為回應物件
        return mapper.toRateItems(rateMapping, currencyInfoMap);
    }

    private Map<String, ExchangeRateMapper.CurrencyInfo> obtainCurrencyInfo() {
        List<CurrencyMapping> currencyList = currencyMappingRepository.findAll();
        Map<String, ExchangeRateMapper.CurrencyInfo> currencyInfoMap = new HashMap<>();

        for (CurrencyMapping currency : currencyList) {
            currencyInfoMap.put(
                currency.getCurrencyCode(),
                new ExchangeRateMapper.CurrencyInfo(
                        currency.getCurrencyNameZh(),
                        currency.getUpdatedAt()
                )
            );
        }
        return currencyInfoMap;
    }
}
