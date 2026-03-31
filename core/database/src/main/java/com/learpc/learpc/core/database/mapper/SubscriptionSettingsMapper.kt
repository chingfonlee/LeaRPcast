package com.learpc.learpc.core.database.mapper

import com.learpc.learpc.core.database.entities.SubscriptionSettingsEntity
import com.learpc.learpc.core.model.podcast.SubscriptionSettings

object SubscriptionSettingsMapper {
    fun SubscriptionSettingsEntity.toDomain(): SubscriptionSettings {
        return SubscriptionSettings(
            podcastId = podcastId,
            autoDownloadEnabled = autoDownloadEnabled,
            wifiOnlyOverride = wifiOnlyOverride,
            downloadLimitOverride = downloadLimitOverride
        )
    }
}
