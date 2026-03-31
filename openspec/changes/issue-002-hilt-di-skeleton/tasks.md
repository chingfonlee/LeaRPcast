# ISSUE-002 — Tasks: 建立 Hilt DI 骨架

## Overview

建立 LeaRPcast 的 Hilt DI 入口點與基礎 module 骨架。估計 **M (1-3hr)**。

---

## Task 1: 在 `:app` build.gradle.kts 加入 Hilt 相依

**Effort**: S  
**File**: `app/build.gradle.kts`

加入：
```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.work.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.work)
    implementation(libs.timber)
    // ... compose, lifecycle deps
}
```

**Acceptance Criteria**:
- [x] Hilt plugin 已加入
- [x] KSP 已配置
- [x] `./gradlew :app:assembleDebug` 不因 Hilt 報錯

---

## Task 2: 建立 LeaRPcastApplication

**Effort**: S  
**File**: `app/src/main/java/com/learpc/learpc/app/LeaRPcastApplication.kt`

```kotlin
package com.learpc.learpc.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class LeaRPcastApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
```

並更新 `AndroidManifest.xml` 加上 `android:name=".app.LeaRPcastApplication"`。

**Acceptance Criteria**:
- [x] `@HiltAndroidApp` 標註正確
- [x] Timber 在 debug 模式初始化
- [x] Manifest 引用正確（`.app.LeaRPcastApplication`）

---

## Task 3: 建立 MainActivity

**Effort**: S  
**File**: `app/src/main/java/com/learpc/learpc/app/MainActivity.kt`

```kotlin
package com.learpc.learpc.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // LeaRPcastTheme will be added in ISSUE-003
        }
    }
}
```

**Acceptance Criteria**:
- [x] `@AndroidEntryPoint` 標註正確
- [x] App 可啟動不 crash

---

## Task 4: 建立空殼 Hilt Modules

**Effort**: S  
**Files**:

```kotlin
// app/src/main/java/com/learpc/learpc/app/di/AppModule.kt
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Context bindings — to be filled in later issues
}

// app/src/main/java/com/learpc/learpc/app/di/RepositoryModule.kt
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    // Repository bindings — to be filled in ISSUE-014+
}

// app/src/main/java/com/learpc/learpc/app/di/WorkerModule.kt
@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {
    // WorkManager HiltWorkerFactory — to be filled in ISSUE-028
}

// core/database/src/main/java/com/learpc/learpc/core/database/di/DatabaseModule.kt
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    // Room DB + DAO — to be filled in ISSUE-009
}

// core/media/src/main/java/com/learpc/learpc/core/media/di/MediaModule.kt
@Module
@InstallIn(SingletonComponent::class)
object MediaModule {
    // ExoPlayer + MediaSession — to be filled in ISSUE-005
}
```

**Acceptance Criteria**:
- [x] 所有 5 個 Hilt module 空殼建立完成
- [x] KSP 可正常生成 Hilt code
- [x] 無循環相依報錯

---

## Task 5: Hilt 注入驗收

執行：
```bash
./gradlew :app:kspDebugKotlin
./gradlew :app:assembleDebug
```

**Acceptance Criteria**:
- [x] KSP 可成功生成 Hilt 注入代碼
- [x] App 可在模擬器/實機上啟動
- [x] Hilt 可注入 Activity（無 `@Inject` 報錯）

---

## Handoff Summary

```
[Handoff Summary]
Completed Issue: ISSUE-002

Changed Files:
- app/build.gradle.kts (MODIFIED — added Hilt, KSP plugins + deps)
- app/src/main/AndroidManifest.xml (MODIFIED — added application name)
- app/src/main/java/.../app/LeaRPcastApplication.kt (NEW)
- app/src/main/java/.../app/MainActivity.kt (NEW)
- app/src/main/java/.../app/di/AppModule.kt (NEW — skeleton)
- app/src/main/java/.../app/di/RepositoryModule.kt (NEW — skeleton)
- app/src/main/java/.../app/di/WorkerModule.kt (NEW — skeleton)
- core/database/src/main/java/.../database/di/DatabaseModule.kt (NEW — skeleton)
- core/media/src/main/java/.../media/di/MediaModule.kt (NEW — skeleton)

What Is Ready For Next Step:
- Hilt DI fully configured
- Application entry point established
- All Hilt module skeletons ready for content in future issues
- Verified with `./gradlew.bat :app:kspDebugKotlin`
- Verified with `./gradlew.bat :app:assembleDebug`

Known Gaps:
- No navigation yet (ISSUE-003)
- No theme applied (ISSUE-003)
- All Hilt modules are empty skeletons

Recommended Next Issue: ISSUE-003 (App Navigation Skeleton)
```
