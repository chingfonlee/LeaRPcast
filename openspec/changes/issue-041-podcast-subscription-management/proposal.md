## Why

The Podcast tab already shows subscribed content, but it does not yet behave like a true management page. Users need a list that is ordered by the most recent episode, shows useful summary context, and supports the basic actions expected from a subscription manager.

## What Changes

- Refactor the existing Podcast list experience into a subscribed podcast management page.
- Keep the current podcast, episode, and subscription data model intact while adding a list-oriented summary read model.
- Sort the list by latest episode publish time from newest to oldest, with a defined fallback for podcasts that have no episodes yet.
- Show each podcast with square artwork, the podcast name, and the latest episode title.
- Add explicit loading, empty, and error states for the management page.
- Add item-level navigation and management actions, including opening details and unsubscribing.
- **BREAKING**: Podcast tab list behavior changes from a simple subscribed list to a sorted management view.

## Capabilities

### New Capabilities
- `podcast-subscription-management`: subscribed podcast management list, summary query, list ordering, item actions, and state handling.

### Modified Capabilities
- `podcast-ui`: Podcast list behavior changes to a management-oriented view with latest-episode summary, ordering, and action affordances.

## Impact

- `feature/podcast` will own the updated list UI, view model, and string resources.
- `domain` will need a subscription-summary style repository contract or equivalent query surface.
- `app/data/repository` will need to expose the new list summary data to the feature layer.
- `core/database` will need a query that can return subscribed podcast summaries with latest episode information.
- Existing Podcast detail navigation should remain intact, but the list page will become the primary management entry point.
