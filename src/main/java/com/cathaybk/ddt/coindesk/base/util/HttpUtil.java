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

@Component
public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private final RestTemplate restTemplate;

    public HttpUtil(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

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
