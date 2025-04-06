package com.metronomo.metronomo.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.metronomo.metronomo.database.AppDatabase
import com.metronomo.metronomo.entity.UserPreference
import com.metronomo.metronomo.repository.UserPreferenceRepository
import com.metronomo.metronomo.utils.Logger
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Gestiona el estado de la UI y la lógica de negocio
class MetronomoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserPreferenceRepository
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _compas = MutableStateFlow("4/4")
    val compas: StateFlow<String> = _compas.asStateFlow()

    private val _figuracion = MutableStateFlow("Negra")
    val figuracion: StateFlow<String> = _figuracion.asStateFlow()

    private val _mostUsedPreference = MutableStateFlow<UserPreference?>(null)
    val mostUsedPreference: StateFlow<UserPreference?> = _mostUsedPreference.asStateFlow()

    init {
        Logger.d("ViewModel", "Inicializando ViewModel")
        val database = AppDatabase.getDatabase(application)
        repository = UserPreferenceRepository(database.userPreferenceDao())

        viewModelScope.launch {
            try {
                repository.mostUsedPreference.collect { preference ->
                    Logger.d("ViewModel", "Nueva preferencia recibida: $preference")
                    _mostUsedPreference.value = preference
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                Logger.e("ViewModel", "Error al cargar preferencias", e)
                _isLoading.value = false
            }
        }
    }

    fun actualizarPreferencias(compas: String, figuracion: String) {
        viewModelScope.launch {
            try {
                Logger.d("ViewModel", "Actualizando preferencias: $compas - $figuracion")
                repository.registrarUso(compas, figuracion)
                _compas.value = compas
                _figuracion.value = figuracion
                Logger.d("ViewModel", "Preferencias actualizadas exitosamente")
            } catch (e: Exception) {
                Logger.e("ViewModel", "Error al actualizar preferencias", e)
            }
        }
    }

    fun cargarPreferenciasGuardadas() {
        _mostUsedPreference.value?.let { preferencia ->
            Logger.d("ViewModel", "Cargando preferencias guardadas")
            _compas.value = preferencia.compas
            _figuracion.value = preferencia.figuracion
            Logger.d("ViewModel", "Preferencias cargadas: ${preferencia.compas} - ${preferencia.figuracion}")
        }
    }

    fun hayPreferenciasGuardadas(): Boolean {
        val resultado = _mostUsedPreference.value != null
        Logger.d("ViewModel", "Verificando preferencias guardadas: $resultado")
        return resultado
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MetronomoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MetronomoViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}