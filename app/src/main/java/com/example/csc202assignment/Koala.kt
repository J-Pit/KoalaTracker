package com.example.csc202assignment

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class Koala(
    @PrimaryKey var id : UUID = UUID.randomUUID(),
    var title : String = "",
    var date : Date = Date(),
    var place : String = "",
    var latitude : Double = 0.0,
    var longitude: Double = 0.0,
    val photoFileName: String? = null
)
