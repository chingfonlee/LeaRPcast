# Capability Spec: DI Skeleton

## What

建立 Hilt Dependency Injection 基礎設施的能力規範，包含 Application 入口、Activity 入口點、以及所有核心 Hilt module 空殼。

## Why

LeaRPcast 使用 Hilt 作為全專案 DI 框架。所有 ViewModel、Repository、Service 都透過 Hilt 注入，不可手動建構。沒有正確的 DI 骨架，後續所有核心元件都無法按 Clean Architecture 接線。

## Canonical State

成功的 DI 骨架必須滿足：

- Application class: `LeaRPcastApplication` (加上 `@HiltAndroidApp`)
- MainActivity: 加上 `@AndroidEntryPoint`
- 所有 Hilt module 骨架存在（空殼，可填充）

## 命名規則（AUTHORITATIVE）

| 角色 | 正確命名 |
|------|---------|
| Application class | `LeaRPcastApplication` |
| Application file | `LeaRPcastApplication.kt` |
| Manifest entry | `android:name=".app.LeaRPcastApplication"` |

> ⚠︎ `RadioCastApplication` 是舊文件遺留名稱，禁止使用。

## Hilt Module 空殼清單

| File | Module | 填充 Issue |
|------|--------|----------|
| `AppModule.kt` | `:app/di` | 多個 Issues |
| `RepositoryModule.kt` | `:app/di` | ISSUE-014+ |
| `WorkerModule.kt` | `:app/di` | ISSUE-028 |
| `DatabaseModule.kt` | `:core:database/di` | ISSUE-009 |
| `MediaModule.kt` | `:core:media/di` | ISSUE-005 |

## Acceptance Check

- `./gradlew :app:kspDebugKotlin` succeeds
- `./gradlew :app:assembleDebug` succeeds
- App launches without Hilt injection errors

## Related Changes

- ISSUE-002: `issue-002-hilt-di-skeleton`
