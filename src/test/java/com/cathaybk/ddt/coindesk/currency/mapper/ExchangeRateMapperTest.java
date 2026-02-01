package com.cathaybk.ddt.coindesk.currency.mapper;

import com.cathaybk.ddt.coindesk.currency.dto.CoinDeskRes;
import com.cathaybk.ddt.coindesk.currency.dto.QueryExchangeRateRes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ExchangeRateMapperTest {

    private ExchangeRateMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ExchangeRateMapper();
    }

    @Test
    void testSixDecimal() {
        BigDecimal rate = new BigDecimal("1.2");

        String result = mapper.formatRate(rate);

        assertEquals("1.200000", result);
    }

    @Test
    void testHalfUp() {
        BigDecimal rate = new BigDecimal("1.2345675");

        String result = mapper.formatRate(rate);

        assertEquals("1.234568", result);
    }

    @Test
    void testRateNull() {
        assertNull(mapper.formatRate(null));
    }

    @Test
    void testBpiNull() {
        CoinDeskRes res = new CoinDeskRes();
        res.setBpi(null);

        Map<String, BigDecimal> result = mapper.extractCurrencyRates(res);

        assertTrue(result.isEmpty());
    }

    @Test
    void testCodeAndRate() {
        CoinDeskRes.BpiInfo usd = new CoinDeskRes.BpiInfo();
        usd.setCode("USD");
        usd.setRateFloat(new BigDecimal("30.1234"));

        Map<String, CoinDeskRes.BpiInfo> bpiMap = new HashMap<>();
        bpiMap.put("USD", usd);

        CoinDeskRes res = new CoinDeskRes();
        res.setBpi(bpiMap);

        Map<String, BigDecimal> result = mapper.extractCurrencyRates(res);

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("30.1234"), result.get("USD"));
    }

    @Test
    void testCurrencyInfoConvert() {
        Map<String, BigDecimal> rateMap = new HashMap<>();
        rateMap.put("USD", new BigDecimal("30.5"));

        Map<String, ExchangeRateMapper.CurrencyInfo> currencyInfoMap = new HashMap<>();
        currencyInfoMap.put(
                "USD",
                new ExchangeRateMapper.CurrencyInfo("美元",
                        LocalDateTime.of(2026, 2, 1, 12, 0, 0))
        );

        List<QueryExchangeRateRes.RateItem> result =
                mapper.toRateItems(rateMap, currencyInfoMap);

        assertEquals(1, result.size());

        QueryExchangeRateRes.RateItem item = result.get(0);
        assertEquals("USD", item.getCurrencyCode());
        assertEquals("30.500000", item.getRate());
        assertEquals("美元", item.getCurrencyNameZh());
        assertEquals("2026/02/01 12:00:00", item.getUpdatedAt());
    }

    @Test
    void testMissingCurrencyInfo() {
        Map<String, BigDecimal> rateMap = new HashMap<>();
        rateMap.put("JPY", new BigDecimal("0.25"));

        List<QueryExchangeRateRes.RateItem> result =
                mapper.toRateItems(rateMap, new HashMap<>());

        assertEquals(1, result.size());
        QueryExchangeRateRes.RateItem item = result.get(0);

        assertEquals("JPY", item.getCurrencyCode());
        assertEquals("0.250000", item.getRate());
        assertNull(item.getCurrencyNameZh());
        assertNull(item.getUpdatedAt());
    }

    @Test
    void testEmptyData() {
        List<QueryExchangeRateRes.RateItem> result =
                mapper.toRateItems(new HashMap<>(), new HashMap<>());

        assertTrue(result.isEmpty());
    }
}
