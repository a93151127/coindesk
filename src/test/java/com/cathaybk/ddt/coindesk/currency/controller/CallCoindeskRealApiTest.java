package com.cathaybk.ddt.coindesk.currency.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class CallCoindeskRealApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void queryCoinDesk_should_call_real_coindesk_api_and_print_response() throws Exception {

        mockMvc.perform(get("/currency/api/coindesk")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.retCode").value("S0000"))
                .andExpect(jsonPath("$.retMsg").value("操作成功"))
                // 測試主要欄位
                .andExpect(jsonPath("$.data.bpi.USD.code").value("USD"))
                .andExpect(jsonPath("$.data.bpi.USD.rate_float").isNumber())
                .andDo(r -> System.out.println(
                        "\n[REAL COINDESK API RESPONSE]\n" +
                                r.getResponse().getContentAsString(StandardCharsets.UTF_8)
                ));
    }
}
