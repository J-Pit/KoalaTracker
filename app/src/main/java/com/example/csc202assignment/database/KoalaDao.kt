package com.example.csc202assignment.database

import androidx.room.*
import com.example.csc202assignment.Koala
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface KoalaDao {
    @Query("SELECT * FROM koala")
    fun getKoalas(): Flow<List<Koala>>

    @Query("SELECT * FROM koala WHERE id = (:id)")
   suspend fun getKoala(id:UUID): Koala

    @Insert
    fun insertAll(koala: Koala)
    @Update
   suspend fun updateKoala(crime: Koala)
    @Delete
    fun delete(koala: Koala)
}