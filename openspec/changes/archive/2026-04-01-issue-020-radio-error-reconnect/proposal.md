## Why

Radio streams can drop due to network failures. We need a systematic error classification (retryable vs permanent) and a backoff reconnect policy before integrating it into `PlaybackService`.

## What Changes

- Create `PlaybackErrorClassifier` distinguishing transient network errors from permanent source errors.
- Create `RadioReconnectPolicy` with configurable max retries and exponential backoff.

## Capabilities

### New Capabilities
- `radio-reconnect-policy`: Error classifier and backoff policy for radio reconnection logic.

### Modified Capabilities
-

## Impact

- Module: `:core:media`
- Unlocks ISSUE-021.
