package com.example.csc202assignment

import android.content.Context
import androidx.room.Room
import com.example.csc202assignment.database.KoalaDatabase

class KoalaRepository constructor(Context: Context) {
    val db = Room.databaseBuilder(
        Context.applicationContext,
        KoalaDatabase::class.java, "database-name"
    ).build()

    fun getKoalas(): List<Koala> {
        return db.koalaDao().getAll()

    }

    fun addKoala(Koala: Koala) {
        db.koalaDao().insertAll(Koala)
    }

    companion object {
        private var INSTANCE: KoalaRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = KoalaRepository(context)
            }
        }

        fun get(): KoalaRepository {
            return INSTANCE ?: throw IllegalStateException("KoalaRepository must be initialized")
        }
    }
}
