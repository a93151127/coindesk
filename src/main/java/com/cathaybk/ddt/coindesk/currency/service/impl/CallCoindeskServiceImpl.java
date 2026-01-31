package com.cathaybk.ddt.coindesk.currency.service.impl;

import com.cathaybk.ddt.coindesk.base.util.HttpUtil;
import com.cathaybk.ddt.coindesk.currency.dto.CallCoindeskRes;
import com.cathaybk.ddt.coindesk.currency.dto.CoinDeskRes;
import com.cathaybk.ddt.coindesk.currency.service.CallCoindeskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CallCoindeskServiceImpl implements CallCoindeskService {

    private static final Logger logger = LoggerFactory.getLogger(CallCoindeskServiceImpl.class);

    @Value("${coindesk.url}")
    private String coindeskUrl;

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public CallCoindeskRes callCoindesk() {
        logger.info("callCoindesk start");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(java.util.Collections.singletonList(MediaType.APPLICATION_JSON));

        String apiResult = httpUtil.get(coindeskUrl, headers);
        CoinDeskRes coinDeskRes = parse(apiResult);

        return convertToCallRes(coinDeskRes);
    }

    /**
     * 將 CoinDesk API 回傳的 JSON 字串解析為 CoinDeskRes 物件。
     *
     * @param json CoinDesk API 回傳的原始 JSON 字串
     * @return 解析後的 CoinDeskRes 物件
     * @throws com.cathaybk.ddt.coindesk.base.exception.ExternalServiceException
     *         當 JSON 解析失敗或回傳格式異常時拋出
     */
    private CoinDeskRes parse(String json) {
        try {
            return objectMapper.readValue(json, CoinDeskRes.class);
        } catch (Exception e) {
            throw new com.cathaybk.ddt.coindesk.base.exception.ExternalServiceException(
                    "Failed to parse response from CoinDesk API", e
            );
        }
    }

    /**
     * 將外部 CoinDesk 回傳資料 CoinDeskRes} 轉換為系統內部使用的 CallCoindeskRes 回應物件。
     *
     *
     * @param src 外部 CoinDesk API 回傳的資料物件
     * @return 轉換後的 CallCoindeskRes 回應物件
     */
    private CallCoindeskRes convertToCallRes(CoinDeskRes src) {
        CallCoindeskRes target = new CallCoindeskRes();

        target.setDisclaimer(src.getDisclaimer());
        target.setChartName(src.getChartName());

        if (src.getTime() != null) {
            CallCoindeskRes.TimeInfo time = new CallCoindeskRes.TimeInfo();
            time.setUpdated(src.getTime().getUpdated());
            time.setUpdatedISO(src.getTime().getUpdatedISO());
            time.setUpdateduk(src.getTime().getUpdateduk());
            target.setTime(time);
        }

        if (src.getBpi() != null) {
            Map<String, CallCoindeskRes.BpiInfo> bpiMap = new HashMap<>();

            for (Map.Entry<String, CoinDeskRes.BpiInfo> entry : src.getBpi().entrySet()) {
                CoinDeskRes.BpiInfo srcBpi = entry.getValue();
                CallCoindeskRes.BpiInfo targetBpi = new CallCoindeskRes.BpiInfo();

                targetBpi.setCode(srcBpi.getCode());
                targetBpi.setSymbol(srcBpi.getSymbol());
                targetBpi.setRate(srcBpi.getRate());
                targetBpi.setDescription(srcBpi.getDescription());
                targetBpi.setRateFloat(srcBpi.getRateFloat());

                bpiMap.put(entry.getKey(), targetBpi);
            }

            target.setBpi(bpiMap);
        }

        return target;
    }
}

