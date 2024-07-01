package com.hs.test.maptest

import androidx.lifecycle.ViewModel

class UserViewModel: ViewModel() {
    private val _userId = "hanseul"
    val userId get() = _userId
}