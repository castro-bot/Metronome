package com.metronomo.metronomo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.metronomo.metronomo.dao.UserPreferenceDao
import com.metronomo.metronomo.entity.UserPreference
import com.metronomo.metronomo.utils.Logger

@Database(entities = [UserPreference::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userPreferenceDao(): UserPreferenceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Logger.d("Database", "Creando nueva instancia de base de datos")
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "metronomo_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Logger.d("Database", "Base de datos creada desde cero")
                            // Agregar preferencia inicial
                            db.execSQL("""
                            INSERT INTO user_preferences (compas, figuracion, usosTotales, ultimoUso)
                            VALUES ('4/4', 'Negra', 1, ${System.currentTimeMillis()})
                        """)
                            Logger.d("Database", "Preferencia inicial agregada")
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            Logger.d("Database", "Base de datos abierta. Verificando contenido...")
                            val cursor = db.query("SELECT COUNT(*) FROM user_preferences")
                            cursor.moveToFirst()
                            val count = cursor.getInt(0)
                            cursor.close()
                            Logger.d("Database", "Número de preferencias en la base de datos: $count")
                        }
                    })
                    .build()

                INSTANCE = instance
                Logger.d("Database", "Instancia de base de datos creada")
                instance
            }
        }
    }
}