package com.example.csc202assignment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class KoalaListViewModel : ViewModel() {
   private val koalaRepository = KoalaRepository.get()
    private val _koalas: MutableStateFlow<List<Koala>> = MutableStateFlow(emptyList())
    val koalas: StateFlow<List<Koala>>
        get() = _koalas.asStateFlow()
    init {
        viewModelScope.launch {
            koalaRepository.getKoalas().collect{
                _koalas.value = it

            }
        }
    }
     fun addKoala(koala: Koala) {
        koalaRepository.addKoala(koala)
    }
}