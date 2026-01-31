package com.cathaybk.ddt.coindesk.base.config;

import com.cathaybk.ddt.coindesk.base.prop.HttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(HttpClientProperties props){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(props.getConnectTimeout());
        factory.setReadTimeout(props.getReadTimeout());
        return new RestTemplate(factory);
    }
}
