package com.cathaybk.ddt.coindesk.base.prop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HttpClientProperties {

    @Value("${http.client.connect-timeout}")
    private int connectTimeout;

    @Value("${http.client.read-timeout}")
    private int readTimeout;

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
