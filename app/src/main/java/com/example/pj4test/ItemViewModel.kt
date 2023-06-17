package com.example.pj4test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ItemViewModel: ViewModel() {
    private var mutIsPersonDetected: Boolean = false
    private var mutIsFootstepDetected: Boolean = false
    val isPersonDetected: Boolean get() = mutIsPersonDetected
    val isFootstepDetected: Boolean get() = mutIsFootstepDetected
    fun personDetected(item: Boolean) {
        mutIsPersonDetected = item
    }
    fun footstepDetected(item: Boolean) {
        mutIsFootstepDetected = item
    }
}