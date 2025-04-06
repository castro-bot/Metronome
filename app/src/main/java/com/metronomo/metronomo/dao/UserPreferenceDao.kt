package com.metronomo.metronomo.dao

import androidx.room.*
import com.metronomo.metronomo.entity.UserPreference
import com.metronomo.metronomo.utils.Logger
import kotlinx.coroutines.flow.Flow

// Define las consultas a la base de datos
@Dao
interface UserPreferenceDao {
    @Query(
        """
        SELECT * FROM user_preferences 
        ORDER BY usosTotales DESC, ultimoUso DESC 
        LIMIT 1
    """
    )
    fun getMostUsedPreference(): Flow<UserPreference?>

    @Query("SELECT * FROM user_preferences WHERE compas = :compas AND figuracion = :figuracion")
    suspend fun getPreferencia(compas: String, figuracion: String): UserPreference?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPreferencia(preference: UserPreference)

    @Query("SELECT COUNT(*) FROM user_preferences")
    suspend fun getPreferenceCount(): Int

    @Query("SELECT * FROM user_preferences ORDER BY ultimoUso DESC")
    suspend fun getAllPreferencesList(): List<UserPreference>

    @Transaction
    suspend fun registrarUso(compas: String, figuracion: String) {
        Logger.d("DAO", "Iniciando registro de uso: $compas - $figuracion")
        val count = getPreferenceCount()
        Logger.d("DAO", "Número actual de preferencias: $count")

        val preferencia = getPreferencia(compas, figuracion)

        if (preferencia != null) {
            Logger.d("DAO", "Actualizando preferencia existente")
            val actualizada = preferencia.copy(
                usosTotales = preferencia.usosTotales + 1,
                ultimoUso = System.currentTimeMillis()
            )
            insertarPreferencia(actualizada)
            Logger.d("DAO", "Preferencia actualizada: $actualizada")
        } else {
            Logger.d("DAO", "Creando nueva preferencia")
            val nueva = UserPreference(
                compas = compas,
                figuracion = figuracion,
                usosTotales = 1,
                ultimoUso = System.currentTimeMillis()
            )
            insertarPreferencia(nueva)
            Logger.d("DAO", "Nueva preferencia creada: $nueva")
        }
        val nuevasPreferencias = getAllPreferencesList()
        Logger.d("DAO", "Preferencias actuales: $nuevasPreferencias")
    }
}
