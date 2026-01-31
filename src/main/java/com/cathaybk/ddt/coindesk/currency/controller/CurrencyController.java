package com.cathaybk.ddt.coindesk.currency.controller;

import com.cathaybk.ddt.coindesk.base.constant.RetCode;
import com.cathaybk.ddt.coindesk.base.model.ApiResponse;
import com.cathaybk.ddt.coindesk.currency.dto.*;
import com.cathaybk.ddt.coindesk.currency.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/currency")
public class CurrencyController {
    @Autowired
    private QueryCurrencyService queryCurrencyService;

    @Autowired
    private UpdateCurrencyService updateCurrencyService;

    @Autowired
    private InsertCurrencyService insertCurrencyService;

    @Autowired
    private DeleteCurrencyService deleteCurrencyService;

    @Autowired
    private CallCoindeskService callCoindeskService;


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

    @PostMapping("/updateCurrency")
    public ApiResponse<Void> updateCurrency(@Valid @RequestBody UpdateCurrencyReq req){
        updateCurrencyService.updateCurrency(req.getCurrencyCode(), req.getCurrencyNameZh());

        return new ApiResponse<>(
                RetCode.S0000.getRetCode(),
                RetCode.S0000.getRetMsg()
        );
    }

    @PostMapping("/insertCurrency")
    public ApiResponse<Void> insertCurrency(@Valid @RequestBody InsertCurrencyReq req){
        insertCurrencyService.insertCurrency(req.getCurrencyCode(), req.getCurrencyNameZh());

        return new ApiResponse<>(
                RetCode.S0000.getRetCode(),
                RetCode.S0000.getRetMsg()
        );
    }

    @PostMapping("/deleteCurrency")
    public ApiResponse<Void> deleteCurrency(@Valid @RequestBody DeleteCurrencyReq req){
        deleteCurrencyService.deleteCurrency(req.getCurrencyCode());

        return new ApiResponse<>(
                RetCode.S0000.getRetCode(),
                RetCode.S0000.getRetMsg()
        );
    }

    @GetMapping("/api/coindesk")
    public ApiResponse<CallCoindeskRes> queryCoinDesk() {
        CallCoindeskRes res = callCoindeskService.callCoindesk();
        return new ApiResponse<>(
                RetCode.S0000.getRetCode(),
                RetCode.S0000.getRetMsg(),
                res
        );
    }
}
