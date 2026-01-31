package com.cathaybk.ddt.coindesk.currency.repository;

import com.cathaybk.ddt.coindesk.currency.entity.CurrencyMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyMappingRepository extends JpaRepository<CurrencyMapping, String> {

    /**
     * 依幣別代碼查詢
     * @param currencyCode
     * @return 對應幣別的資料
     */
    Optional<CurrencyMapping> findByCurrencyCode(String currencyCode);

    /**
     * 查詢所有幣別對應資料
     *
     * @return 幣別對應資料清單，若無資料則回傳空的 List（不為 null）
     */
    List<CurrencyMapping> findAll();

    /**
     * 判斷幣別代碼是否存在
     * @param currencyCode
     * @return 是否存在
     */
    boolean existsByCurrencyCode(String currencyCode);

    /**
     * 依據幣別代碼刪除對應的匯率資料。
     *
     * @param currencyCode 幣別代碼（例如：USD、TWD）
     * @return 實際刪除的資料筆數
     */
    @Modifying
    @Query(value = "DELETE FROM CURRENCY_MAPPING WHERE CURRENCY_CODE = :currencyCode", nativeQuery = true)
    int delByCurrencyCode(@Param("currencyCode") String currencyCode);
}
