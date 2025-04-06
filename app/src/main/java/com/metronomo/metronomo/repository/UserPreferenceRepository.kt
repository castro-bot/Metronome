package com.metronomo.metronomo.repository

import com.metronomo.metronomo.dao.UserPreferenceDao
import com.metronomo.metronomo.entity.UserPreference
import com.metronomo.metronomo.utils.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach

// Intermediario entre el Dao y el ViewModel
class UserPreferenceRepository(private val userPreferenceDao: UserPreferenceDao) {
    val mostUsedPreference: Flow<UserPreference?> = userPreferenceDao.getMostUsedPreference()
        .onEach { preference ->
            Logger.d("Repository", "Preferencia más usada obtenida: $preference")
        }
        .catch { error ->
            Logger.e("Repository", "Error al obtener preferencia", error)
            throw error
        }

    suspend fun registrarUso(compas: String, figuracion: String) {
        try {
            Logger.d("Repository", "Registrando uso: $compas - $figuracion")
            userPreferenceDao.registrarUso(compas, figuracion)
            Logger.d("Repository", "Uso registrado exitosamente")
        } catch (e: Exception) {
            Logger.e("Repository", "Error al registrar uso", e)
            throw e
        }
    }
}