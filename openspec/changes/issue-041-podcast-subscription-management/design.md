## Context

The current Podcast feature already has a working list/detail/episode stack, but the list screen is still only a basic subscribed-podcast listing. The project also already has Podcast, Episode, and Subscription data in Room, so the main gap is not storage shape but the list-oriented read model and the UI behavior on top of it.

This change focuses on the Podcast tab as a subscription management surface. The goal is to improve the first screen users see when they open Podcast without introducing a new route or a new module.

## Goals / Non-Goals

**Goals:**
- Turn the existing `PodcastListScreen` into a management-oriented subscribed podcast page.
- Present podcasts sorted by latest episode publish time, newest first.
- Show square artwork, podcast title, and latest episode title in each row.
- Handle podcasts with no episodes using a clear fallback presentation.
- Support loading, empty, and error states.
- Keep the existing podcast detail and episode navigation model.
- Preserve the current database schema.

**Non-Goals:**
- Do not introduce a new feature module.
- Do not add a separate Podcast management route unless the information architecture later requires it.
- Do not redesign the Podcast detail or episode list screens in this change.
- Do not change the underlying Room schema unless a later migration is explicitly required.

## Decisions

1. Reuse the existing `PodcastListScreen` instead of creating a parallel management screen.
   - The current tab already represents the Podcast library entry point.
   - This keeps the navigation model simple and avoids duplicate list surfaces.
   - Alternative considered: add `SubscribedPodcastListScreen` as a new route. Rejected because the current list screen can carry the management behavior directly.

2. Add a list-oriented summary read model instead of letting the UI assemble episode data itself.
   - The list needs latest-episode metadata, ordering, and fallback state.
   - A dedicated summary model keeps the ViewModel thin and avoids N+1 UI composition logic.
   - Alternative considered: combine `PodcastRepository.observePodcasts()` with per-podcast episode flows in the UI layer. Rejected because it would make the screen harder to reason about and more expensive to maintain.

3. Keep the existing data truth unchanged and derive the list from Room queries.
   - Podcast, episode, and subscription records remain the source of truth.
   - The summary view model is a projection for display only.
   - Alternative considered: add denormalized fields to the podcast table. Rejected because it would duplicate episode-derived data and make refresh behavior harder to preserve.

4. Make the ordering rule explicit and deterministic.
   - Sort podcasts by latest episode publish time descending.
   - Podcasts with no episodes sort after podcasts with episodes.
   - Fallback sort for empty podcasts uses subscribed time descending, then title ascending.
   - Alternative considered: sort only by title. Rejected because it does not satisfy the management-page requirement to surface the most recently updated shows first.

5. Use explicit UI states for loading, empty, and error.
   - Loading should show skeleton cards instead of a single spinner.
   - Empty should explain that the user has not subscribed to any podcasts yet.
   - Error should preserve any existing list data when possible and offer retry.
   - Alternative considered: collapse empty and error into one generic state. Rejected because it hides actionable recovery behavior.

## Risks / Trade-offs

- [Risk] The summary query may become more complex than the current plain list query. → Mitigation: keep the projection narrowly scoped to list fields only.
- [Risk] Podcasts without episodes need careful fallback behavior to avoid confusing ordering. → Mitigation: define deterministic fallback sorting and placeholder copy up front.
- [Risk] The list page may accumulate too many actions if we keep extending it. → Mitigation: keep the MVP actions limited to open detail, refresh, and unsubscribe.
- [Risk] If the subscription naming in code differs from the canonical document name, implementation may drift. → Mitigation: normalize the change around `SubscriptionEntity` in the spec and reconcile implementation naming during apply.

## Migration Plan

- No database migration is expected for this change.
- Add the summary read model and query surface first.
- Refactor `PodcastListScreen` and `PodcastViewModel` to consume the new list projection.
- Add the list states and item actions.
- Validate that existing detail navigation and episode playback still work.
- Rollback is straightforward: keep the old list screen path available until the new list view is verified.

## Open Questions

- Should unsubscribe remove the podcast from the list immediately, or should it remain visible in a disabled state until the next refresh cycle?
- Should the list expose a manual refresh action per item, a page-level refresh action, or both?
- Should the fallback label for podcasts with no episodes be `尚無集數` or `等待首次同步`?
