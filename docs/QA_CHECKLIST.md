# QA Checklist

Use this checklist during manual verification before the bug bash. Mark each row `Pass` or `Fail` and add notes if needed.

## Playback Core

| ID | Test Steps | Expected Result | Pass/Fail |
| --- | --- | --- | --- |
| PC-1 | Open the app, start playback from a podcast episode, then pause and resume. | Playback starts, pauses, and resumes correctly without UI desync. |  |
| PC-2 | Seek forward and backward during playback. | Playback position updates correctly and audio continues from the new position. |  |
| PC-3 | Kill and relaunch the app while audio is playing. | Playback state is restored according to the service behavior and no crash occurs. |  |

## Radio

| ID | Test Steps | Expected Result | Pass/Fail |
| --- | --- | --- | --- |
| R-1 | Open Radio, select a station, and start playback. | The selected station begins streaming and the player state shows playing. |  |
| R-2 | While radio is playing, disconnect the network or force a stream drop. | The app enters the reconnect flow and retries according to the configured policy. |  |
| R-3 | Let the reconnect attempts exceed the retry limit. | Radio playback stops with a visible failure state instead of retrying forever. |  |
| R-4 | After a reconnect failure, restart the station manually. | Manual replay works and the stream starts again if the network is available. |  |

## Podcast

| ID | Test Steps | Expected Result | Pass/Fail |
| --- | --- | --- | --- |
| P-1 | Open the podcast list and enter a podcast detail page. | The list and detail views load without errors and show the expected episode content. |  |
| P-2 | Start playback from an episode in the list or detail screen. | The selected episode starts playing and the player reflects the correct title/artwork. |  |
| P-3 | Download an episode, then play it with the network disabled. | The downloaded episode plays offline from local storage. |  |
| P-4 | Trigger progress save by playing an episode for a while, then relaunch the app. | Playback resumes or restores progress from the last saved position. |  |

## Downloads

| ID | Test Steps | Expected Result | Pass/Fail |
| --- | --- | --- | --- |
| D-1 | Enqueue an episode download. | The episode enters the queue and a download record is created. |  |
| D-2 | Watch a download complete. | The download finishes and the episode is marked as downloaded. |  |
| D-3 | Cancel an in-progress download. | The download stops, the queue clears, and the episode is no longer marked as downloading. |  |
| D-4 | Run the cleanup flow after a download becomes eligible for removal. | Eligible downloaded files are deleted and the database state is updated accordingly. |  |

## Interruptions

| ID | Test Steps | Expected Result | Pass/Fail |
| --- | --- | --- | --- |
| I-1 | Start playback, then receive an incoming call or simulate audio focus loss. | Playback pauses and the interruption snapshot is stored. |  |
| I-2 | End the call or simulate audio focus gain. | Playback resumes only when the resume policy allows it. |  |
| I-3 | Start playback with wired headphones, then unplug them. | Playback pauses immediately when the device becomes noisy. |  |
| I-4 | Resume after interruption with the user manually paused before the interruption. | Playback stays paused and does not auto-resume over the user choice. |  |

## Settings

| ID | Test Steps | Expected Result | Pass/Fail |
| --- | --- | --- | --- |
| S-1 | Open Settings and toggle Wi-Fi only downloads. | The setting changes immediately and persists after app relaunch. |  |
| S-2 | Change the auto-delete mode. | The chosen mode is shown in the UI and persists after app relaunch. |  |
| S-3 | Toggle resume-after-call and restart the app. | The setting remains at the chosen value after relaunch. |  |
| S-4 | Adjust default playback speed and relaunch the app. | The selected playback speed persists and is used by playback. |  |
| S-5 | Enable radio retry and adjust max retry attempts. | The radio retry settings persist and affect the reconnect flow. |  |

## Coverage Notes

- Playback Core covers ISSUE-006, ISSUE-007, and ISSUE-008 user-facing behavior.
- Radio covers ISSUE-019, ISSUE-020, and ISSUE-021 reconnect behavior.
- Podcast covers ISSUE-016, ISSUE-022, ISSUE-023, and ISSUE-024 playback flows.
- Downloads covers ISSUE-025, ISSUE-026, ISSUE-027, ISSUE-033, and ISSUE-034 cleanup behavior.
- Interruptions covers ISSUE-030, ISSUE-031, and ISSUE-032 audio focus and noisy events.
- Settings covers ISSUE-035 persistence and live preference updates.
