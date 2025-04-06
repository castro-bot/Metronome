package com.metronomo.metronomo.utils

import android.util.Log

object Logger {
    private const val APP_TAG = "MetronomoApp"

    fun d(component: String, message: String) {
        Log.d("$APP_TAG:$component", "✅ $message")
    }

    fun e(component: String, message: String, error: Throwable? = null) {
        Log.e("$APP_TAG:$component", "❌ $message", error)
    }
}