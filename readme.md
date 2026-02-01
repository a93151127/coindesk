# CoinDesk 匯率查詢與資料轉換服務

本專案為一個 Spring Boot 後端服務，負責串接外部 CoinDesk API，
並將取得的匯率資料轉換為系統對外統一的回應格式，同時結合資料庫中的幣別輔助資訊，
提供查詢匯率相關的 API。

---

## 專案功能說明

### 1. 串接外部 CoinDesk API
- 透過 HTTP GET 呼叫 CoinDesk 官方 API
- 解析回傳的 BPI（Bitcoin Price Index）資料
- 僅負責資料取得與解析，不包含顯示或商業邏輯

### 2. 匯率資料轉換
- 將 CoinDesk 回傳資料轉換為系統內部使用的匯率結構
- 匯率統一格式化為 **6 位小數（HALF_UP）**
- 結合資料庫中幣別對應的中文名稱與更新時間

### 3. API 對外回傳格式
- 統一使用 `ApiResponse<T>` 包裝回傳結果
- 提供 `retCode` / `retMsg` 以利前端或其他系統判斷結果狀態

---

## API 說明

本服務共提供 **6 支 API**，包含外部 CoinDesk 串接、匯率轉換，以及幣別資料的 CRUD 操作。

---

### 1. 查詢 CoinDesk 原始資料（實際串接外部 API）

**URL**
```
GET /currency/api/coindesk
```

**Request**
- 無

**Response**
```json
{
  "retCode": "S0000",
  "retMsg": "操作成功",
  "data": {
    "time": {
      "updated": "Sep 2, 2024 07:07:20 UTC",
      "updatedISO": "2024-09-02T07:07:20+00:00",
      "updateduk": "Sep 2, 2024 at 08:07 BST"
    },
    "disclaimer": "just for test",
    "chartName": "Bitcoin",
    "bpi": {
      "EUR": {
        "code": "EUR",
        "symbol": "&euro;",
        "rate": "52,243.287",
        "description": "Euro",
        "rate_float": 52243.2865
      },
      "GBP": {
        "code": "GBP",
        "symbol": "&pound;",
        "rate": "43,984.02",
        "description": "British Pound Sterling",
        "rate_float": 43984.0203
      },
      "USD": {
        "code": "USD",
        "symbol": "&#36;",
        "rate": "57,756.298",
        "description": "United States Dollar",
        "rate_float": 57756.2984
      }
    }
  }
}
```

---


### 2. 查詢轉換後匯率資料

**URL**
```
GET /currency/api/queryRate
```

**Request**
- 無

**Response**
```json
{
  "retCode": "S0000",
  "retMsg": "操作成功",
  "data": {
    "items": [
      {
        "currencyCode": "EUR",
        "currencyNameZh": "歐元",
        "rate": "52243.286500",
        "updatedAt": "2026/02/02 01:32:01"
      },
      {
        "currencyCode": "GBP",
        "currencyNameZh": "英鎊",
        "rate": "43984.020300",
        "updatedAt": "2026/02/02 01:32:01"
      },
      {
        "currencyCode": "USD",
        "currencyNameZh": "美元",
        "rate": "57756.298400",
        "updatedAt": "2026/02/02 01:32:01"
      }
    ]
  }
}
```

---


### 3. 查詢幣別資料清單

**URL**
```
GET /currency/api/queryCurrency
```

**Request**
- 不帶任何參數：查詢全部幣別資料
- 可選參數：`currencyCode`
    - 傳入時僅查詢指定幣別

**Request 範例（查詢單一幣別）**
```
GET /currency/api/queryCurrency?currencyCode=USD
```

**Response**
```json
{
  "retCode": "S0000",
  "retMsg": "操作成功",
  "data": {
    "items": [
      {
        "currencyCode": "USD",
        "currencyNameZh": "美元",
        "createdAt": "2026/02/02",
        "updatedAt": "2026/02/02"
      },
      {
        "currencyCode": "GBP",
        "currencyNameZh": "英鎊",
        "createdAt": "2026/02/02",
        "updatedAt": "2026/02/02"
      },
      {
        "currencyCode": "EUR",
        "currencyNameZh": "歐元",
        "createdAt": "2026/02/02",
        "updatedAt": "2026/02/02"
      }
    ]
  }
}
```

---


### 4. 新增幣別資料

**URL**
```
POST /currency/api/insertCurrency
```

**Request**
```json
{
  "currencyCode": "JPY",
  "currencyNameZh": "日圓"
}
```

**Response**
```json
{
  "retCode": "S0000",
  "retMsg": "操作成功"
}
```

---


### 5. 更新幣別資料

**URL**
```
PUT /currency/api/updateCurrency
```

**Request**
```json
{
  "currencyCode": "JPY",
  "currencyNameZh": "日圓（更新）"
}
```

**Response**
```json
{
  "retCode": "S0000",
  "retMsg": "操作成功"
}
```

---


### 6. 刪除幣別資料

**URL**
```
DELETE /currency/api/deleteCurrency
```

**Request**
```json
{
  "currencyCode": "JPY"
}
```

**Response**
```json
{
  "retCode": "S0000",
  "retMsg": "操作成功"
}
```

## 專案結構說明

```
com.cathaybk.ddt.coindesk
│ ├─ base
│ │ ├─ config # 系統與框架相關設定（Spring / 共用設定）
│ │ ├─ constant # 系統常數、回傳碼（RetCode 等）
│ │ ├─ exception # 自訂例外類別
│ │ ├─ model # 共用模型物件
│ │ ├─ prop # 設定屬性物件（@ConfigurationProperties）
│ │ └─ util # 共用工具類（HTTP、JSON、日期轉換等）
│ │
│ ├─ currency
│ │ ├─ controller # 匯率 / 幣別相關 API Controller
│ │ ├─ dto # API Request / Response DTO
│ │ ├─ entity # 資料庫 Entity
│ │ ├─ mapper # 外部資料與內部資料轉換 Mapper
│ │ ├─ repository # JPA Repository
│ │ └─ service # 業務邏輯介面與實作
│ │
│ └─ CoindeskApplication # Spring Boot 專案進入點
```

---

## 環境與設定

### 必要設定（application-prod.properties）

```properties
coindesk.url=https://api.coindesk.com/v1/bpi/currentprice.json
```

### 開發環境
- Java 8
- Spring Boot 2.7.18
- JPA
- H2 

