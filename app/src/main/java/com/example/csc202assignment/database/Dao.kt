package com.example.csc202assignment.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.csc202assignment.Koala
@Dao
interface KoalaDao {
    @Query("SELECT * FROM koala")
    fun getAll(): List<Koala>

    @Query("SELECT * FROM koala WHERE id IN (:koalaIds)")
    fun loadAllByIds(koalaIds: IntArray): List<Koala>

    @Query("SELECT * FROM koala WHERE title LIKE :title LIMIT 1")
    fun findByTitle(title: String): Koala

    @Insert
    fun insertAll(koalas: Koala)

    @Delete
    fun delete(koala: Koala)
}