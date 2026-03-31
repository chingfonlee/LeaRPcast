# AI Agent Execution Pack v1

## 專案名稱
Android 廣播與 Podcast 收聽 App

## 文件版本
v1.0

## 文件目的
本文件是給其他 AI Agent 使用的執行手冊。它的目的不是補充需求，而是規定：
- AI 應該先讀哪些文件
- AI 一次可以做多少事
- AI 應如何回報
- AI 不可擅自更改哪些內容
- AI 遇到 blocker 時如何停下與交接
- 多個 AI 如何分工而不互相打架

本文件適用於：
- ChatGPT / Codex 類 agent
- Claude Code / Claude 類 agent
- Cursor / Windsurf / Cline / OpenClaw 類 coding agent
- 任何會直接修改 Android 專案程式碼的 AI

---

# 1. 核心原則

## 1.1 AI Agent 的角色
AI Agent 的角色是：
- 依文件執行任務
- 嚴格遵守現有架構
- 產出可檢查、可驗收、可回退的變更

AI Agent 不是：
- 重新設計產品的人
- 任意重構整個專案的人
- 自己決定新需求的人
- 自行更換技術棧的人

## 1.2 執行哲學
1. **一次只做一張 issue，最多做一組明確批准的 issue。**
2. **先理解文件，再開始改碼。**
3. **不得擅自修正規格。**
4. **寧可停下回報，不可自行腦補需求。**
5. **每次輸出都必須可對照 issue 驗收。**
6. **任何與 module / schema / state model 有關的改動都必須高度保守。**

---

# 2. AI 必讀文件順序

AI 在開始任何 task 前，必須依序閱讀以下文件：

## Level 1：總體方向
1. **PRD + 技術架構清單 + MVP 功能表**
2. **Technical Design Spec v1**

## Level 2：實作邊界
3. **Data Schema v1**
4. **Project Structure Spec v1**

## Level 3：執行任務
5. **Issue List v1**
6. **只閱讀本次要執行的 ISSUE 與其依賴 ISSUE**

## Level 4：執行規則
7. **本文件：AI Agent Execution Pack v1**

### 閱讀原則
- AI 不可以只看單一 issue 就開始寫程式
- AI 必須先知道架構、schema、檔案擺放規則
- AI 必須以 Issue List 為最後的「任務邊界」依據

---

# 3. AI 每次允許執行的範圍

## 3.1 單次任務上限
預設規則：
- **一次只做 1 張 issue**
- 若使用者明確允許，最多可做 **一組強相依且同階段的 2~3 張 issue**

## 3.2 什麼情況可以合併做
可合併的情況：
- 同一功能骨架不可切斷
- 一張 issue 本身太小，拆開反而增加錯誤
- 使用者明確要求「做完整個小階段」

### 例子
可以合併：
- ISSUE-004 + ISSUE-005
- ISSUE-009 + ISSUE-010

不建議合併：
- ISSUE-006 + ISSUE-016
- ISSUE-021 + ISSUE-035
- ISSUE-029 + ISSUE-039

## 3.3 AI 不可自行擴做
即使 AI 發現「順手做一下比較方便」，也不可以擴做：
- 不可順手改 package 結構
- 不可順手補 feature UI
- 不可順手改 schema
- 不可順手重命名核心狀態

---

# 4. AI 接單後的標準流程

## Step 1：先確認理解
AI 必須先輸出：
1. 這張 issue 的目標
2. 它依賴哪些既有文件
3. 它預計新增/修改哪些檔案
4. 它不會碰哪些範圍

## Step 2：再開始實作
AI 才可開始產出：
- 檔案樹
- 程式碼
- 設定變更
- manifest / gradle 調整

## Step 3：完成後回報
AI 必須逐條對照：
- 本次 issue 的 deliverables
- acceptance criteria
- 是否有未完成項
- 是否留下 blocker / tech debt

---

# 5. AI 輸出格式標準

每次 AI 回覆都必須遵守以下格式。

## 5.1 實作前回覆格式
```text
[Task]
ISSUE-XXX - <標題>

[Understanding]
- 這次任務的核心目標是：...
- 這次只做的範圍是：...
- 這次不做的範圍是：...

[Input Docs]
- PRD
- TDS v1
- Data Schema v1
- Project Structure Spec v1
- Issue List v1（ISSUE-XXX）

[Planned File Changes]
- 新增：...
- 修改：...
- 不會修改：...

[Risks / Blockers]
- ...
```

## 5.2 實作後回覆格式
```text
[Task Completed]
ISSUE-XXX - <標題>

[Files Added/Changed]
- ...

[What Was Implemented]
- ...

[Acceptance Check]
- AC1: done / partial / not done
- AC2: done / partial / not done
- AC3: done / partial / not done

[Notes]
- 這次做的假設：...
- 尚未處理：...
- 建議下一張 issue：...
```

## 5.3 若為 code agent 直接改檔
即使 AI 已直接修改專案檔案，也必須同時輸出：
- 修改清單
- 原因
- 驗收對照
- 風險與未完成項

---

# 6. AI 嚴格禁止事項

## 6.1 禁止擅自修改下列內容
1. **不可擅自修改 PRD 需求邊界**
2. **不可擅自修改 TDS 中的核心架構方向**
3. **不可擅自修改 Data Schema**
4. **不可擅自改 module / package 結構**
5. **不可擅自替換 Media3 / ExoPlayer / Room / DataStore / WorkManager**
6. **不可把 DAO / Entity 塞進 feature module**
7. **不可在 Composable 直接 new ExoPlayer 或直接打 DAO**
8. **不可把一次任務做成大範圍重構**

## 6.2 禁止腦補需求
AI 不可自行新增：
- 推薦系統
- 雲端同步
- Login/Account system
- Analytics dashboard
- Android Auto 深度功能
- 第三方付費服務整合

除非使用者明確開新需求。

## 6.3 禁止隱性改名
AI 不可自行把這些名稱改掉：
- `PlaybackStateModel`
- `PlayableItem`
- `InterruptionSnapshot`
- 各種 schema enum 字串
- Issue List 中既定 module 命名

若真的需要改名，必須先停下回報。

---

# 7. Blocker 處理規則

## 7.1 什麼算 blocker
以下情況必須視為 blocker：
- 文件之間互相矛盾
- schema 缺欄位，導致 task 無法正確完成
- issue scope 不足以完成必要骨架
- 專案現有程式結構與文件衝突太大
- Android 平台限制導致既定作法不可行
- 缺 API 金鑰 / 外部服務 / 測試資源

## 7.2 發現 blocker 時 AI 必須怎麼做
AI 必須：
1. 停止擴做
2. 明確指出 blocker
3. 說明它阻礙哪個 acceptance criteria
4. 提出 **最多 2 個**保守替代方案
5. 等使用者或主控 AI 決定

## 7.3 Blocker 回報格式
```text
[Blocker]
ISSUE-XXX

[Problem]
...

[Why It Blocks This Task]
...

[Impacted Acceptance Criteria]
- AC1
- AC2

[Safe Options]
1. ...
2. ...

[Current Status]
- 已完成：...
- 未完成：...
```

---

# 8. 多 AI 協作規則

## 8.1 基本原則
若你同時使用多個 AI：
- 一個 AI 一次只負責一組固定 issue
- 不要讓兩個 AI 同時改同一層的同一批檔案
- 先定責任區，再分派任務

## 8.2 建議分工方式

### Agent A：骨架與播放核心
適合負責：
- ISSUE-001 ~ ISSUE-008
- ISSUE-020 ~ ISSUE-021
- ISSUE-030 ~ ISSUE-032

### Agent B：資料層與 Podcast 流程
適合負責：
- ISSUE-009 ~ ISSUE-016
- ISSUE-022 ~ ISSUE-029
- ISSUE-033

### Agent C：UI 與設定頁
適合負責：
- ISSUE-016
- ISSUE-019
- ISSUE-024
- ISSUE-027
- ISSUE-035

### Agent D：測試與驗收
適合負責：
- ISSUE-036
- ISSUE-037
- ISSUE-038
- ISSUE-039

## 8.3 不建議的平行方式
不要同時讓兩個 AI：
- 一起改 `PlaybackService`
- 一起改 `AppDatabase`
- 一起改 `RepositoryModule`
- 一起改 `MediaModule`

---

# 9. 檔案交接規則

## 9.1 AI 每次結束時必須交接
AI 任務結束後，必須留下：
- 修改檔案清單
- 本次完成的 issue
- 尚未完成的 acceptance criteria
- 對下一張 issue 的影響

## 9.2 交接摘要格式
```text
[Handoff Summary]
Completed Issue:
- ISSUE-XXX

Changed Files:
- ...

What Is Ready For Next Step:
- ...

Known Gaps:
- ...

Recommended Next Issue:
- ISSUE-YYY
```

## 9.3 交給下一個 AI 前，你應該提供
1. 本次完成的回報
2. 修改後最新程式碼
3. 本文件組
4. 下一張 issue 編號

---

# 10. 驗收與自我檢查規則

AI 在回報「完成」前，必須先自查：

## 10.1 架構檢查
- 檔案有沒有放錯 module？
- 有沒有違反 project structure spec？
- 有沒有在 feature 直接碰 DAO / ExoPlayer？

## 10.2 任務邊界檢查
- 有沒有超出 issue scope？
- 有沒有偷做別張 issue？
- 有沒有改到不該改的檔？

## 10.3 命名檢查
- 有沒有改壞既定命名？
- enum / state model 字串是否一致？

## 10.4 驗收條件檢查
- 是否逐條對照 acceptance criteria？
- 是否誠實標示 partial / not done？

---

# 11. 建議給 AI 的啟動提示詞

## 11.1 單張 issue 開始模板
```text
你現在是本專案的 Android Kotlin 開發代理。

請先閱讀以下文件：
1. PRD + 技術架構清單 + MVP 功能表
2. Technical Design Spec v1
3. Data Schema v1
4. Project Structure Spec v1
5. Issue List v1
6. AI Agent Execution Pack v1

本次只允許執行：ISSUE-XXX

要求：
- 只能做 ISSUE-XXX scope 內的內容
- 不可擅自修改 schema、module 結構、state model 命名
- 不可擴做其他 issue
- 若遇到 blocker，必須用 blocker format 回報
- 回覆格式必須遵守 execution pack

請先輸出：
1. 你理解的任務目標
2. 你要修改的檔案
3. 不會碰的範圍
4. 潛在 blocker
在我確認前，不要開始寫程式。
```

## 11.2 允許開始實作模板
```text
可以開始實作 ISSUE-XXX。

請直接輸出：
1. 修改檔案清單
2. 實作內容
3. 驗收對照
4. handoff summary

若有任何未完成項，必須明確標示，不可假裝完成。
```

## 11.3 Code review / 驗收模板
```text
你現在不是開發者，而是 reviewer。

請根據以下文件審查 ISSUE-XXX 的實作：
- PRD
- Technical Design Spec v1
- Data Schema v1
- Project Structure Spec v1
- Issue List v1
- AI Agent Execution Pack v1

請檢查：
1. 是否超出 scope
2. 是否違反 module/package 規則
3. 是否違反 schema / state naming
4. acceptance criteria 是否真的滿足
5. 是否留下 hidden tech debt

輸出格式：
- Pass / Needs changes
- 問題清單
- 建議修正項
```

---

# 12. 建議你的實際使用順序

## 12.1 第一批最適合丟給 AI 的任務
先丟：
- ISSUE-001
- ISSUE-002
- ISSUE-003

目的：
先驗證這個 AI 會不會遵守專案骨架與 module 規則。

## 12.2 第二批
再丟：
- ISSUE-004
- ISSUE-005
- ISSUE-006
- ISSUE-007

目的：
驗證它會不會把播放核心亂塞進 feature。

## 12.3 第三批
再丟：
- ISSUE-009 ~ ISSUE-015

目的：
開始建立資料層與 feed 流。

## 12.4 最後再進 UI 與 polish
因為 UI 比較容易改，核心架構應先穩。

---

# 13. AI 品質分級規則

你可以用這個簡單標準判斷一個 AI 值不值得繼續用。

## A 級
- 會先讀文件
- 不亂擴做
- 檔案放得正確
- 會老實標示 partial
- blocker 回報清楚

## B 級
- 大方向正確
- 偶爾小幅超 scope
- 但能修正

## C 級
- 常亂改結構
- 不照輸出格式
- 驗收對照敷衍
- 會自己腦補需求

## D 級
- 直接無視文件
- 重構一大堆不相干內容
- 假裝完成
- 模組/資料/schema 全亂

若 AI 連續兩次出現 C 或 D 級表現，建議換 agent 或只讓它做 reviewer。

---

# 14. 最小可運作執行流程

若你想今天就開始用這套文件驅動其他 AI，最短流程如下：

1. 把 6 份文件一起交給 AI
2. 指定只做 `ISSUE-001`
3. 先看它的「實作前回覆」是否有遵守 execution pack
4. 若 OK，再允許它開始寫
5. 完成後用另一個 AI 依 reviewer 模板做審查
6. 通過後再進下一張 issue

這樣雖然慢一點，但最穩。

---

# 15. 你目前已具備的完整文件組

截至本文件完成，你已經有：

1. **PRD + 技術架構清單 + MVP 功能表**
2. **Technical Design Spec v1**
3. **Data Schema v1**
4. **Project Structure Spec v1**
5. **Issue List v1**
6. **AI Agent Execution Pack v1**

這 6 份文件已經足夠支持：
- 多 AI 分工開發
- 分階段 issue 驅動實作
- reviewer 驗收
- 避免 AI 擅自改架構

---

# 16. v1 結論
這份 AI Agent Execution Pack v1 的核心價值是：
- 把「如何使用其他 AI」也文件化
- 讓 AI 不是憑感覺改碼，而是照制度執行
- 讓你可以逐張 issue 發包、逐張驗收、逐張交接

若前面 5 份文件是「做什麼、怎麼做、資料怎麼存、檔案怎麼放、任務怎麼拆」，
那這份文件就是「其他 AI 該怎麼開始做，做到哪裡停，怎麼交回來」。

