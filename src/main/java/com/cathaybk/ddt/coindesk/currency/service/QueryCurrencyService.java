package com.cathaybk.ddt.coindesk.currency.service;

import com.cathaybk.ddt.coindesk.currency.dto.QueryCurrencyRes;

import java.util.List;

public interface QueryCurrencyService {
    public List<QueryCurrencyRes.CurrencyItem> queryCurrencies(String currencyCode);
}
