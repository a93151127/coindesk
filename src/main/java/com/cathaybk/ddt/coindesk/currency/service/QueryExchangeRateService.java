package com.cathaybk.ddt.coindesk.currency.service;

import com.cathaybk.ddt.coindesk.currency.dto.QueryExchangeRateRes;

import java.util.List;

public interface QueryExchangeRateService {
    public List<QueryExchangeRateRes.RateItem> queryExchangeRates();
}
