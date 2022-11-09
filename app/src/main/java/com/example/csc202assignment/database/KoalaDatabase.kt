package com.example.csc202assignment.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.csc202assignment.Koala


    @Database(entities = arrayOf(Koala::class), version = 1)
    @androidx.room.TypeConverters(TypeConverters::class)
    abstract class KoalaDatabase : RoomDatabase() {
        abstract fun koalaDao(): KoalaDao
    }