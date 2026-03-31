package com.learpc.learpc.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import com.learpc.learpc.app.ui.LeaRPcastApp
import com.learpc.learpc.core.media.PlaybackController
import timber.log.Timber
import javax.inject.Inject
import java.util.concurrent.Executor

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var sessionToken: SessionToken
    @Inject lateinit var playbackController: PlaybackController

    private var controllerFuture: ListenableFuture<MediaController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controllerFuture = MediaController.Builder(this, sessionToken).buildAsync().also { future ->
            future.addListener({
                runCatching {
                    playbackController.connectController(future.get())
                }.onFailure { throwable ->
                    Timber.e(throwable)
                }
            }, Executor { runnable -> runnable.run() })
        }
        setContent {
            LeaRPcastApp()
        }
    }

    override fun onDestroy() {
        controllerFuture?.let { MediaController.releaseFuture(it) }
        controllerFuture = null
        super.onDestroy()
    }
}
