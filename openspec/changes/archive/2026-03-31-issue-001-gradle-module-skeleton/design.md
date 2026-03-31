# ISSUE-001 — Design: Gradle Modules 與基本專案骨架

## Architecture Decision

採用 Project Structure Spec v1 定義的 multi-module 結構，每個 module 在本 ISSUE 只建立最小可編譯骨架。

## Module 相依關係圖

```
:app
  ├──> :core:common
  ├──> :core:ui
  ├──> :core:network
  ├──> :core:database
  ├──> :core:datastore
  ├──> :core:media
  ├──> :core:model
  ├──> :domain
  ├──> :feature:radio
  ├──> :feature:podcast
  ├──> :feature:player
  ├──> :feature:downloads
  └──> :feature:settings

:domain
  └──> :core:common
  └──> :core:model

:feature:* (各 feature module)
  ├──> :domain
  ├──> :core:common
  ├──> :core:ui
  └──> :core:model

:core:media
  └──> :core:common
  └──> :core:model

:core:database
  └──> :core:common

:core:network
  └──> :core:common
  └──> :core:model
```

## 每個 Module 的最小骨架

每個 module 建立以下最小結構：
```
<module>/
  build.gradle.kts          ← 依類型配置相依
  src/main/
    AndroidManifest.xml     ← (Android modules only)
    kotlin/com/learpc/learpc/<module_path>/
      <Placeholder>.kt      ← 最小可編譯佔位檔
```

## 建立的檔案清單

### 根層級
| 檔案 | 說明 |
|------|------|
| `settings.gradle.kts` | 加入所有 15 個 module include |
| `build.gradle.kts` (root) | 更新版本管理 / plugin setup |
| `gradle/libs.versions.toml` | 版本目錄（Version Catalog） |

### Package Root
所有 module 使用 `com.learpc.learpc` 作為 base package。

| Module | Base Package |
|--------|-------------|
| `:app` | `com.learpc.learpc.app` |
| `:core:common` | `com.learpc.learpc.core.common` |
| `:core:ui` | `com.learpc.learpc.core.ui` |
| `:core:network` | `com.learpc.learpc.core.network` |
| `:core:database` | `com.learpc.learpc.core.database` |
| `:core:datastore` | `com.learpc.learpc.core.datastore` |
| `:core:media` | `com.learpc.learpc.core.media` |
| `:core:model` | `com.learpc.learpc.core.model` |
| `:core:testing` | `com.learpc.learpc.core.testing` |
| `:domain` | `com.learpc.learpc.domain` |
| `:feature:radio` | `com.learpc.learpc.feature.radio` |
| `:feature:podcast` | `com.learpc.learpc.feature.podcast` |
| `:feature:player` | `com.learpc.learpc.feature.player` |
| `:feature:downloads` | `com.learpc.learpc.feature.downloads` |
| `:feature:settings` | `com.learpc.learpc.feature.settings` |

## build.gradle.kts 分類

### Android Application (`:app`)
```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}
```

### Android Library (`:core:*`, `:feature:*`, `:domain`)
```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}
```

### Kotlin-only Library (`:core:testing`, `:core:model` 可選)
```kotlin
plugins {
    alias(libs.plugins.kotlin.jvm)
}
```

## 不在本 ISSUE 範圍

- Hilt 配置（ISSUE-002）
- Navigation 配置（ISSUE-003）
- 任何業務邏輯檔案
- Room / DataStore / Media3 設定
