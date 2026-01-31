package com.cathaybk.ddt.coindesk.currency.controller;

import com.cathaybk.ddt.coindesk.base.constant.RetCode;
import com.cathaybk.ddt.coindesk.base.model.ApiResponse;
import com.cathaybk.ddt.coindesk.currency.dto.QueryCurrencyRes;
import com.cathaybk.ddt.coindesk.currency.service.QueryCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/currency")
public class CurrencyController {
    @Autowired
    private QueryCurrencyService queryCurrencyService;


    @GetMapping("/queryCurrency")
    public ApiResponse<QueryCurrencyRes> queryCurrency(
            @RequestParam(required = false) String currencyCode){
        QueryCurrencyRes res = new QueryCurrencyRes();
        List<QueryCurrencyRes.CurrencyItem> items = queryCurrencyService
                .queryCurrencies(currencyCode);
        res.setItems(items);
        return new ApiResponse<QueryCurrencyRes>(
                RetCode.S0000.getRetCode(),
                RetCode.S0000.getRetMsg(),
                res
        );
    }
}
