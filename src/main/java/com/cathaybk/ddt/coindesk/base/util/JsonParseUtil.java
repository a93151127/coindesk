package com.cathaybk.ddt.coindesk.base.util;

import com.cathaybk.ddt.coindesk.base.exception.ExternalResponseParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonParseUtil {
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 將 JSON 字串解析為指定型別的物件。
     *
     * @param json  原始 JSON 字串
     * @param clazz 目標物件型別
     * @param <T>   回傳物件型別
     * @return 解析後的物件
     * @throws ExternalResponseParseException
     *         當 JSON 解析失敗或回傳格式異常時拋出
     */
    public <T> T parse(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new ExternalResponseParseException("JSON parse failed", e);
        }
    }
}
