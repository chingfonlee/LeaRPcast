package com.learpc.learpc.core.model

data class PlayableItem(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val imageUri: String? = null,
    val mediaUri: String
)
