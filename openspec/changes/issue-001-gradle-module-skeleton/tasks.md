# ISSUE-001 — Tasks: 建立 Gradle Modules 與基本專案骨架

## Overview

建立 LeaRPcast 的 15 個 Gradle modules 骨架。全部任務估計 **L (3-8hr)**。

---

## Task 1: 建立 Version Catalog (libs.versions.toml)

**Effort**: M  
**File**: `gradle/libs.versions.toml`

建立集中版本管理檔，包含以下版本：

```toml
[versions]
agp = "8.7.3"
kotlin = "2.1.0"
ksp = "2.1.0-1.0.29"
hilt = "2.55"
compose-bom = "2025.03.01"
media3 = "1.6.1"
room = "2.7.0"
datastore = "1.1.4"
retrofit = "2.11.0"
okhttp = "4.12.0"
workmanager = "2.10.0"
coroutines = "1.10.2"
timber = "5.0.1"
navigation = "2.9.0"
lifecycle = "2.9.0"

[libraries]
# AndroidX Core
androidx-core-ktx = { module = "androidx.core:core-ktx", version = "1.16.0" }
androidx-lifecycle-runtime = { module = "androidx.lifecycle:lifecycle-runtime-ktx", version.ref = "lifecycle" }
androidx-lifecycle-viewmodel = { module = "androidx.lifecycle:lifecycle-viewmodel-ktx", version.ref = "lifecycle" }

# Compose
compose-bom = { module = "androidx.compose:compose-bom", version.ref = "compose-bom" }
compose-ui = { module = "androidx.compose.ui:ui" }
compose-ui-tooling = { module = "androidx.compose.ui:ui-tooling" }
compose-ui-tooling-preview = { module = "androidx.compose.ui:ui-tooling-preview" }
compose-material3 = { module = "androidx.compose.material3:material3" }
compose-activity = { module = "androidx.activity:activity-compose", version = "1.10.1" }
navigation-compose = { module = "androidx.navigation:navigation-compose", version.ref = "navigation" }

# Media3
media3-exoplayer = { module = "androidx.media3:media3-exoplayer", version.ref = "media3" }
media3-exoplayer-hls = { module = "androidx.media3:media3-exoplayer-hls", version.ref = "media3" }
media3-session = { module = "androidx.media3:media3-session", version.ref = "media3" }
media3-ui = { module = "androidx.media3:media3-ui", version.ref = "media3" }
media3-datasource-okhttp = { module = "androidx.media3:media3-datasource-okhttp", version.ref = "media3" }
media3-common = { module = "androidx.media3:media3-common", version.ref = "media3" }

# Room
room-runtime = { module = "androidx.room:room-runtime", version.ref = "room" }
room-ktx = { module = "androidx.room:room-ktx", version.ref = "room" }
room-compiler = { module = "androidx.room:room-compiler", version.ref = "room" }

# DataStore
datastore-preferences = { module = "androidx.datastore:datastore-preferences", version.ref = "datastore" }

# Hilt
hilt-android = { module = "com.google.dagger:hilt-android", version.ref = "hilt" }
hilt-compiler = { module = "com.google.dagger:hilt-android-compiler", version.ref = "hilt" }
hilt-navigation-compose = { module = "androidx.hilt:hilt-navigation-compose", version = "1.2.0" }
hilt-work = { module = "androidx.hilt:hilt-work", version = "1.2.0" }
hilt-work-compiler = { module = "androidx.hilt:hilt-compiler", version = "1.2.0" }

# Network
retrofit = { module = "com.squareup.retrofit2:retrofit", version.ref = "retrofit" }
retrofit-converter-scalars = { module = "com.squareup.retrofit2:converter-scalars", version.ref = "retrofit" }
okhttp = { module = "com.squareup.okhttp3:okhttp", version.ref = "okhttp" }
okhttp-logging = { module = "com.squareup.okhttp3:logging-interceptor", version.ref = "okhttp" }

# WorkManager
workmanager = { module = "androidx.work:work-runtime-ktx", version.ref = "workmanager" }

# Coroutines
coroutines-android = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-android", version.ref = "coroutines" }
coroutines-test = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-test", version.ref = "coroutines" }

# Logging
timber = { module = "com.jakewharton.timber:timber", version.ref = "timber" }

# Test
junit = { module = "junit:junit", version = "4.13.2" }
mockk = { module = "io.mockk:mockk", version = "1.13.17" }
turbine = { module = "app.cash.turbine:turbine", version = "1.2.0" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
android-library = { id = "com.android.library", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-jvm = { id = "org.jetbrains.kotlin.jvm", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
room = { id = "androidx.room", version.ref = "room" }
```

**Acceptance Criteria**:
- [x] `libs.versions.toml` 存在且格式正確
- [x] 所有版本均已定義

---

## Task 2: 更新 Root build.gradle.kts

**Effort**: S  
**File**: `build.gradle.kts` (根目錄)

```kotlin
// Top-level build file
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
}
```

**Acceptance Criteria**:
- [x] root build.gradle.kts 使用 Version Catalog 引用所有 plugin

---

## Task 3: 更新 settings.gradle.kts

**Effort**: S  
**File**: `settings.gradle.kts`

加入所有 15 個 module includes：

```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "LeaRPcast"

include(":app")
include(":core:common")
include(":core:ui")
include(":core:network")
include(":core:database")
include(":core:datastore")
include(":core:media")
include(":core:model")
include(":core:testing")
include(":domain")
include(":feature:radio")
include(":feature:podcast")
include(":feature:player")
include(":feature:downloads")
include(":feature:settings")
```

**Acceptance Criteria**:
- [x] 所有 15 個 module 已列入 settings.gradle.kts

---

## Task 4: 建立各 Module 骨架

**Effort**: L  

每個 module 需建立：
1. `build.gradle.kts`（依類型，參考 design.md）
2. `src/main/AndroidManifest.xml`（Android modules）
3. 最小 placeholder Kotlin 檔案（讓 module 可編譯）

### 各 Module 的 placeholder 檔案

| Module | Placeholder 檔案 | 內容 |
|--------|-----------------|------|
| `:core:common` | `CoreCommon.kt` | `package com.learpc.learpc.core.common` |
| `:core:ui` | `CoreUi.kt` | `package com.learpc.learpc.core.ui` |
| `:core:network` | `CoreNetwork.kt` | `package com.learpc.learpc.core.network` |
| `:core:database` | `CoreDatabase.kt` | `package com.learpc.learpc.core.database` |
| `:core:datastore` | `CoreDatastore.kt` | `package com.learpc.learpc.core.datastore` |
| `:core:media` | `CoreMedia.kt` | `package com.learpc.learpc.core.media` |
| `:core:model` | `CoreModel.kt` | `package com.learpc.learpc.core.model` |
| `:core:testing` | `CoreTesting.kt` | `package com.learpc.learpc.core.testing` |
| `:domain` | `Domain.kt` | `package com.learpc.learpc.domain` |
| `:feature:radio` | `FeatureRadio.kt` | `package com.learpc.learpc.feature.radio` |
| `:feature:podcast` | `FeaturePodcast.kt` | `package com.learpc.learpc.feature.podcast` |
| `:feature:player` | `FeaturePlayer.kt` | `package com.learpc.learpc.feature.player` |
| `:feature:downloads` | `FeatureDownloads.kt` | `package com.learpc.learpc.feature.downloads` |
| `:feature:settings` | `FeatureSettings.kt` | `package com.learpc.learpc.feature.settings` |

**Acceptance Criteria**:
- [x] 所有 15 個 module 目錄已建立
- [x] 每個 module 有正確的 build.gradle.kts
- [x] Android module 有 AndroidManifest.xml
- [x] 每個 module 有最小 placeholder Kotlin 檔案

---

## Task 5: 建立 app shell 字串管理檔 strings.xml（UTF-8）
**Effort**: S  
**File**: `app/src/main/res/values/strings.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- App shell -->
    <string name="app_name">LeaRPcast</string>

    <!-- Bottom Navigation -->
    <string name="nav_radio">Radio</string>
    <string name="nav_podcast">Podcast</string>
    <string name="nav_downloads">Downloads</string>
    <string name="nav_settings">Settings</string>
</resources>
```

> **注意**：這個 ISSUE 只建立 app shell 會用到的字串。feature-specific 的 placeholder / 功能字串，需在對應 feature module 的 `strings.xml` 中建立。任何 Composable 都要透過 `stringResource` 使用，禁止 hardcode。
**Acceptance Criteria**:
- [x] `strings.xml` 使用 `encoding="utf-8"`
- [x] 包含 `app_name` 與 4 個底部導覽列字串
- [x] app shell 字串集中在 `app/src/main/res/values/strings.xml`
- [x] feature-specific 字串規則已明確移交給對應 feature module

## Task 6: Gradle Sync 驗收

**Effort**: S

執行：
```bash
./gradlew help
./gradlew assembleDebug (app only)
```

**Acceptance Criteria**:
- [x] `./gradlew help` 成功（代表所有 module 可被解析）
- [x] 無 module 相依循環報錯
- [x] package 結構符合 Project Structure Spec v1
- [x] 所有 15 個 module 可正常解析

---

## Handoff Summary

```
[Handoff Summary]
Completed Issue: ISSUE-001

Changed Files:
- gradle/libs.versions.toml (NEW)
- build.gradle.kts (MODIFIED)
- settings.gradle.kts (MODIFIED)
- app/src/main/res/values/strings.xml (NEW — UTF-8, centralized string management)
- core/common/build.gradle.kts + placeholder (NEW)
- core/ui/build.gradle.kts + placeholder (NEW)
- core/network/build.gradle.kts + placeholder (NEW)
- core/database/build.gradle.kts + placeholder (NEW)
- core/datastore/build.gradle.kts + placeholder (NEW)
- core/media/build.gradle.kts + placeholder (NEW)
- core/model/build.gradle.kts + placeholder (NEW)
- core/testing/build.gradle.kts + placeholder (NEW)
- domain/build.gradle.kts + placeholder (NEW)
- feature/radio/build.gradle.kts + placeholder (NEW)
- feature/podcast/build.gradle.kts + placeholder (NEW)
- feature/player/build.gradle.kts + placeholder (NEW)
- feature/downloads/build.gradle.kts + placeholder (NEW)
- feature/settings/build.gradle.kts + placeholder (NEW)

What Is Ready For Next Step:
- 15-module project skeleton syncs successfully
- All placeholder packages exist at correct paths
- Version catalog ready for use in next issues
- app shell strings.xml (UTF-8) established as the place for app-level UI strings

Known Gaps:
- No Hilt setup yet (ISSUE-002)
- No Navigation setup yet (ISSUE-003)
- All feature/core modules are empty stubs
- Feature strings will be added per feature issue

Recommended Next Issue: ISSUE-002 (Hilt DI Skeleton)

String Management Note for all future agents:
  App shell strings → app/src/main/res/values/strings.xml (UTF-8)
  Feature strings → owning feature module's src/main/res/values/strings.xml (UTF-8)
  Use stringResource(R.string.key) in Compose, never hardcode.
```
