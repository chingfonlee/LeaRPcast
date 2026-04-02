package com.learpc.learpc.feature.settings.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.learpc.learpc.core.datastore.preferences.AutoDeleteMode
import com.learpc.learpc.core.datastore.preferences.UserPreferences
import com.learpc.learpc.feature.settings.R
import com.learpc.learpc.feature.settings.ui.viewmodel.SettingsViewModel
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settingsState.collectAsState()

    SettingsContent(
        settings = settings,
        onWifiOnlyChanged = viewModel::setWifiOnlyDownload,
        onAutoDeleteModeChanged = viewModel::setAutoDeleteMode,
        onResumeAfterCallChanged = viewModel::setResumeAfterCall,
        onPlaybackSpeedChanged = viewModel::setDefaultPlaybackSpeed,
        onRadioRetryEnabledChanged = viewModel::setRadioRetryEnabled,
        onRadioMaxRetriesChanged = viewModel::setRadioMaxRetries,
        modifier = modifier
    )
}

@Composable
private fun SettingsContent(
    settings: UserPreferences,
    onWifiOnlyChanged: (Boolean) -> Unit,
    onAutoDeleteModeChanged: (AutoDeleteMode) -> Unit,
    onResumeAfterCallChanged: (Boolean) -> Unit,
    onPlaybackSpeedChanged: (Float) -> Unit,
    onRadioRetryEnabledChanged: (Boolean) -> Unit,
    onRadioMaxRetriesChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            SectionTitle(stringResource(R.string.settings_section_downloads))
            Spacer(modifier = Modifier.height(12.dp))
            SettingsToggleRow(
                label = stringResource(R.string.settings_wifi_only),
                checked = settings.autoDownloadWifiOnly,
                onCheckedChange = onWifiOnlyChanged
            )
            Spacer(modifier = Modifier.height(12.dp))
            AutoDeleteModeRow(
                currentMode = settings.autoDeleteMode,
                onModeChanged = onAutoDeleteModeChanged
            )
        }

        item {
            SectionTitle(stringResource(R.string.settings_section_playback))
            Spacer(modifier = Modifier.height(12.dp))
            SettingsToggleRow(
                label = stringResource(R.string.settings_resume_after_call),
                checked = settings.resumeAfterCallEnabled,
                onCheckedChange = onResumeAfterCallChanged
            )
            Spacer(modifier = Modifier.height(12.dp))
            PlaybackSpeedRow(
                speed = settings.defaultPlaybackSpeed,
                onSpeedChanged = onPlaybackSpeedChanged
            )
        }

        item {
            SectionTitle(stringResource(R.string.settings_section_radio))
            Spacer(modifier = Modifier.height(12.dp))
            SettingsToggleRow(
                label = stringResource(R.string.settings_radio_retry_enabled),
                checked = settings.radioAutoRetryEnabled,
                onCheckedChange = onRadioRetryEnabledChanged
            )
            Spacer(modifier = Modifier.height(12.dp))
            IntegerSliderRow(
                label = stringResource(R.string.settings_radio_retry_max_attempts),
                value = settings.radioRetryMaxAttempts,
                valueRange = 1..10,
                onValueChanged = onRadioMaxRetriesChanged
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(
                    R.string.settings_radio_retry_base_delay_value,
                    settings.radioRetryBaseDelayMs
                ),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun SettingsToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AutoDeleteModeRow(
    currentMode: AutoDeleteMode,
    onModeChanged: (AutoDeleteMode) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val modes = remember { AutoDeleteMode.entries }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = modeLabel(currentMode),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.settings_auto_delete_mode)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            modes.forEach { mode ->
                DropdownMenuItem(
                    text = { Text(modeLabel(mode)) },
                    onClick = {
                        onModeChanged(mode)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun PlaybackSpeedRow(
    speed: Float,
    onSpeedChanged: (Float) -> Unit
) {
    Column {
        Text(stringResource(R.string.settings_playback_speed))
        Spacer(modifier = Modifier.height(8.dp))
        Slider(
            value = speed.coerceIn(0.5f, 2.0f),
            onValueChange = onSpeedChanged,
            valueRange = 0.5f..2.0f,
            steps = 5
        )
        Text(text = String.format("%.2fx", speed), style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun IntegerSliderRow(
    label: String,
    value: Int,
    valueRange: IntRange,
    onValueChanged: (Int) -> Unit
) {
    Column {
        Text(label)
        Spacer(modifier = Modifier.height(8.dp))
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChanged(it.roundToInt()) },
            valueRange = valueRange.first.toFloat()..valueRange.last.toFloat(),
            steps = (valueRange.last - valueRange.first - 1).coerceAtLeast(0)
        )
        Text(text = value.toString(), style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun modeLabel(mode: AutoDeleteMode): String {
    return when (mode) {
        AutoDeleteMode.NEVER -> stringResource(R.string.settings_auto_delete_never)
        AutoDeleteMode.AFTER_PLAYED -> stringResource(R.string.settings_auto_delete_after_played)
        AutoDeleteMode.AFTER_24_HOURS -> stringResource(R.string.settings_auto_delete_after_24_hours)
        AutoDeleteMode.AFTER_7_DAYS -> stringResource(R.string.settings_auto_delete_after_7_days)
    }
}
