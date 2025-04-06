package com.metronomo.metronomo

import android.app.Application
import com.metronomo.metronomo.database.AppDatabase
import com.metronomo.metronomo.utils.Logger

class MetronomoApplication : Application() {
    val database: AppDatabase by lazy {
        Logger.d("Application", "Inicializando base de datos")
        AppDatabase.getDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()
        Logger.d("Application", "Aplicación iniciada")
    }
}
