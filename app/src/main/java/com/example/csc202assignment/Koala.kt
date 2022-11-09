package com.example.csc202assignment

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class Koala(
    @PrimaryKey val id : Int,
        val title : String,
        val date : String,
        val place : String,
)
