package com.example.spirelab_pankajverma.ui.screen

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember

@Composable
fun rememberDebounceClick(
    debounceTime: Long = 1000L,
    onDoubleClick: () -> Unit
): () -> Unit {
    val lastClickTime = remember { mutableLongStateOf(0L) }
    return {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastClickTime.longValue >= debounceTime) {
            lastClickTime.longValue = currentTime
            onDoubleClick()
        }
    }
}