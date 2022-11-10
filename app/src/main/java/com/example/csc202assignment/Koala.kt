package com.example.csc202assignment

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class Koala(
    @PrimaryKey var id : Int = 1,
    var title : String = "",
    var date : Date = Date(),
    var place : String = "",
)
