package com.cathaybk.ddt.coindesk.currency.service.impl;

import com.cathaybk.ddt.coindesk.base.exception.DataNotFoundException;
import com.cathaybk.ddt.coindesk.currency.repository.CurrencyMappingRepository;
import com.cathaybk.ddt.coindesk.currency.service.DeleteCurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteCurrencyServiceImpl implements DeleteCurrencyService {

    private final Logger logger = LoggerFactory.getLogger(DeleteCurrencyServiceImpl.class);

    @Autowired
    private CurrencyMappingRepository currencyMappingRepository;

    @Override
    @Transactional
    public void deleteCurrency(String currencyCode) {
        logger.info("deleteCurrency start, currencyCode : {}", currencyCode);

        int resultCount = currencyMappingRepository.delByCurrencyCode(currencyCode);

        if(resultCount == 0){
            throw new DataNotFoundException("Currency not found, code : " + currencyCode);
        }
        logger.info("deleteCurrency success, currencyCode: {}", currencyCode);
    }
}
