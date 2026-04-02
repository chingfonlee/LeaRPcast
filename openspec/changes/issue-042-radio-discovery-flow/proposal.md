## Why

The Radio tab can already browse and manage stations, but users still need a faster, friendlier way to answer the real question: "What can I listen to right now?" This change adds a discovery-first entry flow so users can start from recommendations or search, preview a station immediately, and save it only if they want to keep it.

## What Changes

- Add a discovery-first quick-add flow for radio stations.
- Let users start from recommended stations, keyword search, or a minimal manual entry path depending on what they know.
- Allow stations to be previewed before saving so the user can hear something first.
- Bias recommendations using broad locale, language, or region signals when available, without requiring precise location access.
- Keep manual station name + stream URL entry as an advanced path for users who already know the station details.
- Treat time-based listening utilities such as scheduled start or stop as future follow-up capabilities, not part of the core add flow.

## Capabilities

### New Capabilities
- `radio-discovery-flow`: radio station discovery, quick add, preview-before-save, and suggestion biasing for the listening entry experience.

### Modified Capabilities
- 

## Impact

- `:feature:radio` needs a discovery-first entry point, recommendation surface, search path, and preview-before-save interaction.
- `:domain` may need use cases or query helpers for discovery suggestions and save-after-preview behavior.
- `:app/data/repository/DefaultRadioRepository.kt` may need discovery-oriented station selection helpers if recommendations are sourced locally.
- `feature/radio/src/main/res/values/strings.xml` will need new UI strings for discovery, recommendation, preview, and save flows.
