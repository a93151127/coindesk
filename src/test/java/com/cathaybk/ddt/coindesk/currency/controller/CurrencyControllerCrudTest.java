package com.cathaybk.ddt.coindesk.currency.controller;

import com.cathaybk.ddt.coindesk.currency.dto.*;
import com.cathaybk.ddt.coindesk.currency.repository.CurrencyMappingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * 測試幣別對應表 CRUD API，並顯示回傳內容
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CurrencyControllerCrudTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CurrencyMappingRepository currencyMappingRepository;

    @BeforeEach
    void cleanDb() {
        currencyMappingRepository.deleteAll();
    }

    @Test
    @Order(1)
    void testInsertCurrency() throws Exception {
        InsertCurrencyReq req = new InsertCurrencyReq();
        req.setCurrencyCode("USD");
        req.setCurrencyNameZh("美元");

        String resp = mockMvc.perform(post("/currency/insertCurrency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(r -> System.out.println(
                        "\n[INSERT RESPONSE]\n" +
                                r.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Assertions.assertTrue(resp.contains("S0000"));
    }

    @Test
    @Order(2)
    void testQueryCurrency() throws Exception {
        insertUsd();

        String resp = mockMvc.perform(get("/currency/queryCurrency")
                        .param("currencyCode", "USD")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items[0].currencyCode").value("USD"))
                .andExpect(jsonPath("$.data.items[0].currencyNameZh").value("美元"))
                .andDo(r -> System.out.println(
                        "\n[QUERY RESPONSE]\n" +
                                r.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Assertions.assertTrue(resp.contains("USD"));
    }

    @Test
    @Order(3)
    void testUpdateCurrency() throws Exception {
        insertUsd();

        UpdateCurrencyReq req = new UpdateCurrencyReq();
        req.setCurrencyCode("USD");
        req.setCurrencyNameZh("美金");

        mockMvc.perform(post("/currency/updateCurrency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(r -> System.out.println(
                        "\n[UPDATE RESPONSE]\n" +
                                r.getResponse().getContentAsString(StandardCharsets.UTF_8)));

        // 再查一次確認更新成功
        mockMvc.perform(get("/currency/queryCurrency")
                        .param("currencyCode", "USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items[0].currencyNameZh").value("美金"));
    }

    @Test
    @Order(4)
    void testDeleteCurrency() throws Exception {
        insertUsd();

        DeleteCurrencyReq req = new DeleteCurrencyReq();
        req.setCurrencyCode("USD");

        mockMvc.perform(post("/currency/deleteCurrency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.retCode").value("S0000"))
                .andDo(r -> System.out.println(
                        "\n[DELETE RESPONSE]\n" +
                                r.getResponse().getContentAsString(StandardCharsets.UTF_8)));

        // 刪除後再查，預期查無資料（D0001)
        mockMvc.perform(get("/currency/queryCurrency")
                        .param("currencyCode", "USD")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.retCode").value("D0001"))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(r -> System.out.println(
                        "\n[QUERY AFTER DELETE RESPONSE]\n" +
                                r.getResponse().getContentAsString(StandardCharsets.UTF_8)));
    }

    private void insertUsd() throws Exception {
        InsertCurrencyReq req = new InsertCurrencyReq();
        req.setCurrencyCode("USD");
        req.setCurrencyNameZh("美元");

        mockMvc.perform(post("/currency/insertCurrency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }
}
