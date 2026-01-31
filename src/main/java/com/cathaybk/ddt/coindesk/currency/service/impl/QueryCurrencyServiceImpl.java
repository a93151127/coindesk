package com.cathaybk.ddt.coindesk.currency.service.impl;

import com.cathaybk.ddt.coindesk.base.exception.DataNotFoundException;
import com.cathaybk.ddt.coindesk.base.util.DateUtil;
import com.cathaybk.ddt.coindesk.currency.dto.QueryCurrencyRes;
import com.cathaybk.ddt.coindesk.currency.entity.CurrencyMapping;
import com.cathaybk.ddt.coindesk.currency.repository.CurrencyMappingRepository;
import com.cathaybk.ddt.coindesk.currency.service.QueryCurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class QueryCurrencyServiceImpl implements QueryCurrencyService {
    private final Logger logger = LoggerFactory.getLogger(QueryCurrencyServiceImpl.class);

    @Autowired
    private CurrencyMappingRepository currencyMappingRepository;

    @Override
    public List<QueryCurrencyRes.CurrencyItem> queryCurrencies(String currencyCode) {
        logger.info("queryCurrencies start, currencyCode : {}", currencyCode);
        List<CurrencyMapping> currencyList = null;
        if(StringUtils.isEmpty(currencyCode)){
            currencyList = queryAllCurrency();
        }else{
            currencyList = querySingleCurrency(currencyCode);
        }
        return transToItems(currencyList);
    }

    private List<QueryCurrencyRes.CurrencyItem> transToItems(List<CurrencyMapping> currencyList){
        List<QueryCurrencyRes.CurrencyItem> items =
                new ArrayList<>();
        for(CurrencyMapping currency : currencyList){
            QueryCurrencyRes.CurrencyItem item = new QueryCurrencyRes.CurrencyItem();
            item.setCurrencyCode(currency.getCurrencyCode());
            item.setCurrencyNameZh(currency.getCurrencyNameZh());
            item.setCreatedAt(DateUtil.transLocalDateTimeToString(currency.getCreatedAt(),
                    DateUtil.FORMATTER_YYYYMMDD));
            item.setUpdatedAt(DateUtil.transLocalDateTimeToString(currency.getUpdatedAt(),
                    DateUtil.FORMATTER_YYYYMMDD));
            items.add(item);
        }
        return items;
    }

    private List<CurrencyMapping> querySingleCurrency(String currencyCode){
        List<CurrencyMapping> currencyList = new ArrayList<>();

        CurrencyMapping currency = currencyMappingRepository.findByCurrencyCode(currencyCode)
                .orElseThrow(() ->new DataNotFoundException("Currency not found, code : " + currencyCode));

        currencyList.add(currency);
        return currencyList;
    }

    private List<CurrencyMapping> queryAllCurrency(){
        return currencyMappingRepository.findAll();
    }
}
