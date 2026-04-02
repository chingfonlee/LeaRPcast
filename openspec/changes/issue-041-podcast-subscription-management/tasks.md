## 1. Data Layer

- [ ] 1.1 Add a subscribed-podcast summary read model in `core/database/src/main/java/com/learpc/learpc/core/database/dto/` that includes podcast metadata, latest episode summary, episode count, and subscription time.
- [ ] 1.2 Add a Room query in `core/database/src/main/java/com/learpc/learpc/core/database/dao/PodcastDao.kt` that returns subscribed podcast summaries ordered by latest episode publish time with the defined fallback rules.
- [ ] 1.3 Update `core/database/src/main/java/com/learpc/learpc/core/database/source/PodcastLocalDataSource.kt` and `domain/src/main/java/com/learpc/learpc/domain/repository/PodcastRepository.kt` to expose the summary list flow.
- [ ] 1.4 Add repository and mapper support in `app/src/main/java/com/learpc/learpc/app/data/repository/DefaultPodcastRepository.kt` and related mapper files.
- [ ] 1.5 Add tests for summary ordering, no-episode fallback, and unsubscribe behavior in the data or domain test packages.

## 2. Podcast UI

- [ ] 2.1 Refactor `feature/podcast/src/main/java/com/learpc/learpc/feature/podcast/ui/model/PodcastUiState.kt` to support summary rows, explicit loading, empty, error, and refresh states.
- [ ] 2.2 Update `feature/podcast/src/main/java/com/learpc/learpc/feature/podcast/ui/viewmodel/PodcastViewModel.kt` to consume the summary list flow and expose list actions.
- [ ] 2.3 Refactor `feature/podcast/src/main/java/com/learpc/learpc/feature/podcast/ui/screen/PodcastListScreen.kt` into the management page with square artwork, latest-episode text, and fallback copy for empty podcasts.
- [ ] 2.4 Add navigation and management interactions so tapping an item opens `PodcastDetailScreen` and unsubscribe uses a confirmation flow.
- [ ] 2.5 Add feature strings in `feature/podcast/src/main/res/values/strings.xml` for headings, states, fallback copy, action labels, and confirmation text.

## 3. Verification

- [ ] 3.1 Verify that subscribed podcasts appear in newest-latest-episode-first order and that podcasts with no episodes fall back correctly.
- [ ] 3.2 Verify loading, empty, and error states in the Podcast list UI.
- [ ] 3.3 Run the relevant feature and data-layer tests, then confirm the app builds cleanly.
