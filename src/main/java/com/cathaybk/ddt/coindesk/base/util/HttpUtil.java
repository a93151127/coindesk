package com.cathaybk.ddt.coindesk.base.util;

import com.cathaybk.ddt.coindesk.base.exception.ExternalServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * HTTP 呼叫工具類，封裝 RestTemplate 的共用呼叫邏輯。
 *
 * <p>
 * 此類別負責統一處理外部系統 HTTP 呼叫行為，包含：
 * <ul>
 *   <li>HTTP 請求送出與回應取得</li>
 *   <li>非 2xx 回應狀態碼的判斷與處理</li>
 *   <li>外部系統呼叫失敗時的例外轉換</li>
 * </ul>
 * </p>
 *
 * <p>
 * 所有外部系統相關的呼叫錯誤，皆會轉換為 ExternalServiceException 避免將底層 HTTP 或
 * RestTemplate 的實作細節暴露至上層服務。
 * </p>
 *
 */
@Component
public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private final RestTemplate restTemplate;

    public HttpUtil(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    /**
     * 發送 HTTP GET 請求至指定的外部服務。
     *
     * @param url     外部服務的請求 URL
     * @param headers HTTP 請求標頭
     * @return 外部服務回傳的原始回應內容（字串格式）
     * @throws ExternalServiceException
     *         當外部服務回傳非 2xx 狀態碼或呼叫過程發生錯誤時拋出
     */
    public String get(String url, HttpHeaders headers) {
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return exchange(url, HttpMethod.GET, entity);
    }

    private String exchange(String url, HttpMethod method, HttpEntity<?> entity) {
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                logger.error("External service status code is not 2xx!  statusCode : {}", response.getStatusCodeValue());
                throw new ExternalServiceException("External service returned non-2xx: " + response.getStatusCodeValue());
            }

            return response.getBody();

        } catch (Exception e) {
            logger.error("External service error! ", e);
            throw new ExternalServiceException("External service call failed.", e);

        }
    }
}
