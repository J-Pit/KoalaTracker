package com.example.csc202assignment

import androidx.lifecycle.ViewModel
import java.util.*

class KoalaListViewModel : ViewModel() {
    val koalas = mutableListOf<Koala>()

    init {
    for (i in 0 until 100){
        val koala = Koala()

        koala.id = i
        koala.title = "koala found"
        koala.date = Date()
        koala.place = "forest"
        koalas += koala
    }


    }
}