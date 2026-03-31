# ISSUE-002 — Design: Hilt DI 骨架

## Application Class

```kotlin
// app/src/main/java/com/learpc/learpc/app/LeaRPcastApplication.kt
@HiltAndroidApp
class LeaRPcastApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}
```

## MainActivity

```kotlin
// app/src/main/java/com/learpc/learpc/app/MainActivity.kt
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LeaRPcastTheme {
                // AppNavHost will be wired here in ISSUE-003
            }
        }
    }
}
```

## Hilt Module Skeletons (空殼，供後續 ISSUE 填充)

| File | Module | 說明 |
|------|--------|------|
| `AppModule.kt` | `:app/di` | App-level bindings (Context, etc.) |
| `RepositoryModule.kt` | `:app/di` | Repository interface → implementation bindings |
| `DatabaseModule.kt` | `:core:database/di` | Room DB / DAO 注入 |
| `MediaModule.kt` | `:core:media/di` | ExoPlayer / MediaSession 注入 |
| `WorkerModule.kt` | `:app/di` | HiltWorkerFactory binding |

## AndroidManifest 更新

```xml
<application
    android:name=".app.LeaRPcastApplication"
    ...>
    <activity android:name=".app.MainActivity" />
</application>
```

## 檔案路徑

```
app/src/main/java/com/learpc/learpc/app/
  LeaRPcastApplication.kt
  MainActivity.kt
  di/
    AppModule.kt
    RepositoryModule.kt
    WorkerModule.kt

core/database/src/main/java/com/learpc/learpc/core/database/
  di/
    DatabaseModule.kt

core/media/src/main/java/com/learpc/learpc/core/media/
  di/
    MediaModule.kt
```
