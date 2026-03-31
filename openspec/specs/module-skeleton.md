# Capability Spec: Module Skeleton

## What

建立 Android multi-module 專案骨架的能力規範。定義本專案如何初始化、命名並驗收所有 Gradle modules。

## Why

LeaRPcast 採 multi-module 架構以確保 feature/core/domain 清楚邊界。沒有統一的 module 骨架規範，AI agent 容易各自建立不一致的 module 結構。

## Canonical State

成功的 module skeleton 必須滿足：

- `settings.gradle.kts` 包含 15 個 module includes
- 每個 module 有獨立的 `build.gradle.kts`
- 所有 package root 為 `com.learpc.learpc`
- `gradle/libs.versions.toml` 集中管理所有版本

## Module 清單（15 個）

| Module | Type | Base Package |
|--------|------|-------------|
| `:app` | Android Application | `com.learpc.learpc.app` |
| `:core:common` | Android Library | `com.learpc.learpc.core.common` |
| `:core:ui` | Android Library | `com.learpc.learpc.core.ui` |
| `:core:network` | Android Library | `com.learpc.learpc.core.network` |
| `:core:database` | Android Library | `com.learpc.learpc.core.database` |
| `:core:datastore` | Android Library | `com.learpc.learpc.core.datastore` |
| `:core:media` | Android Library | `com.learpc.learpc.core.media` |
| `:core:model` | Android Library | `com.learpc.learpc.core.model` |
| `:core:testing` | Kotlin JVM | `com.learpc.learpc.core.testing` |
| `:domain` | Android Library | `com.learpc.learpc.domain` |
| `:feature:radio` | Android Library | `com.learpc.learpc.feature.radio` |
| `:feature:podcast` | Android Library | `com.learpc.learpc.feature.podcast` |
| `:feature:player` | Android Library | `com.learpc.learpc.feature.player` |
| `:feature:downloads` | Android Library | `com.learpc.learpc.feature.downloads` |
| `:feature:settings` | Android Library | `com.learpc.learpc.feature.settings` |

## Acceptance Check

- `./gradlew help` succeeds (all 15 modules resolve)
- No circular dependency warnings
- Each module compiles independently
- `strings.xml` exists with UTF-8 declaration

## Related Changes

- ISSUE-001: `issue-001-gradle-module-skeleton`
