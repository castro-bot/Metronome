package com.metronomo.metronomo.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreference(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val compas: String,
    val figuracion: String,
    val usosTotales: Int = 1,
    val ultimoUso: Long = System.currentTimeMillis()
) {
    override fun toString(): String {
        return "UserPreference(id=$id, compas='$compas', figuracion='$figuracion', usos=$usosTotales)"
    }
}