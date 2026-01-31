package com.cathaybk.ddt.coindesk.currency.service.impl;

import com.cathaybk.ddt.coindesk.base.exception.DataNotFoundException;
import com.cathaybk.ddt.coindesk.currency.entity.CurrencyMapping;
import com.cathaybk.ddt.coindesk.currency.repository.CurrencyMappingRepository;
import com.cathaybk.ddt.coindesk.currency.service.UpdateCurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UpdateCurrencyServiceImpl implements UpdateCurrencyService {
    private final Logger logger = LoggerFactory.getLogger(UpdateCurrencyServiceImpl.class);

    @Autowired
    private CurrencyMappingRepository currencyMappingRepository;

    @Override
    @Transactional
    public void updateCurrency(String currencyCode, String currencyNameZh) {
        logger.info("updateCurrency start, currencyCode : {}, currencyNameZh : {}"
                , currencyCode, currencyNameZh);

        CurrencyMapping currency = currencyMappingRepository.findByCurrencyCode(currencyCode)
                .orElseThrow(() ->
                        new DataNotFoundException("Cannot find currency! currencyCode: " + currencyCode)
                );

        currency.setCurrencyNameZh(currencyNameZh);
        currency.setUpdatedAt(LocalDateTime.now());

        currencyMappingRepository.save(currency);

        logger.info("updateCurrency success, currencyCode: {}", currencyCode);
    }
}
