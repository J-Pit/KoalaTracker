package com.example.csc202assignment.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.TypeConverters
import androidx.room.RoomDatabase
import com.example.csc202assignment.Koala


    @Database(entities = [Koala::class], version = 1)
    @TypeConverters(Converters::class)
    abstract class KoalaDatabase : RoomDatabase() {
        abstract fun koalaDao(): KoalaDao
    }