## 1. Create DAO Interfaces

- [x] 1.1 Create `PodcastDao.kt` with `insert`, `upsert`, `delete`, `observeAll(): Flow<List<PodcastEntity>>`, `getById(id)`.
- [x] 1.2 Create `EpisodeDao.kt` with `insert`, `upsert`, `deleteByPodcastId`, `observeByPodcastId(id): Flow<List<EpisodeEntity>>`.
- [x] 1.3 Create `PlaybackProgressDao.kt` with `upsert`, `getByEpisodeId`.
- [x] 1.4 Create `DownloadRecordDao.kt` with `upsert`, `delete`, `observeAll(): Flow<List<DownloadRecordEntity>>`.
- [x] 1.5 Create `RadioStationDao.kt` with `upsert`, `delete`, `observeAll(): Flow<List<RadioStationEntity>>`.

## 2. Create Relation DTOs

- [x] 2.1 Create `PodcastWithEpisodes` data class annotated with `@Embedded` / `@Relation`.
- [x] 2.2 Add `@Transaction` function to `PodcastDao` returning `Flow<PodcastWithEpisodes>`.

## 3. Update DatabaseModule

- [x] 3.1 Add `@Provides` functions for each DAO in `DatabaseModule` delegating to `AppDatabase.podcastDao()` etc.

## 4. Verification

- [x] 4.1 Run `./gradlew :core:database:kspDebugKotlin`.
- [x] 4.2 All DAO queries compile; no Room annotation errors.
