package com.cathaybk.ddt.coindesk.currency.service.impl;

import com.cathaybk.ddt.coindesk.base.exception.DataAlreadyExistException;
import com.cathaybk.ddt.coindesk.currency.entity.CurrencyMapping;
import com.cathaybk.ddt.coindesk.currency.repository.CurrencyMappingRepository;
import com.cathaybk.ddt.coindesk.currency.service.InsertCurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class InsertCurrencyServiceImpl implements InsertCurrencyService {

    private final Logger logger = LoggerFactory.getLogger(InsertCurrencyServiceImpl.class);

    @Autowired
    private CurrencyMappingRepository currencyMappingRepository;

    @Override
    @Transactional
    public void insertCurrency(String currencyCode, String currencyNameZh) {
        logger.info("insertCurrency start, currencyCode : {}, currencyNameZh : {}"
                , currencyCode, currencyNameZh);

        if(currencyMappingRepository.existsByCurrencyCode(currencyCode)){
            throw new DataAlreadyExistException("Data already exist in DB! currencyCode : "
                    + currencyCode);
        }

        CurrencyMapping currency = new CurrencyMapping();
        currency.setCurrencyCode(currencyCode);
        currency.setCurrencyNameZh(currencyNameZh);
        currency.setCreatedAt(LocalDateTime.now());
        currency.setUpdatedAt(LocalDateTime.now());

        currencyMappingRepository.save(currency);
        logger.info("insertCurrency success, currencyCode: {}", currencyCode);
    }
}
