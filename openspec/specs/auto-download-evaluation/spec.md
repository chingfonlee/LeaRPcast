# auto-download-evaluation Specification

## Purpose
TBD - created by archiving change issue-028-auto-download-usecase. Update Purpose after archive.
## Requirements
### Requirement: Respect Wi-Fi-Only Setting
`EvaluateAutoDownloadUseCase` MUST return `false` if `wifiOnly = true` and the device is on a metered (cellular) network.

#### Scenario: Wi-Fi-only on cellular
- **WHEN** `wifiOnly = true` and device is on cellular
- **THEN** use case returns `false`.

### Requirement: Per-Subscription Override
`EvaluateAutoDownloadUseCase` MUST honour per-subscription auto-download settings over the global default.

#### Scenario: Podcast has auto-download disabled
- **WHEN** global `autoDownload = true` but `SubscriptionSettings.autoDownload = false` for the podcast
- **THEN** use case returns `false` for episodes of that podcast.

