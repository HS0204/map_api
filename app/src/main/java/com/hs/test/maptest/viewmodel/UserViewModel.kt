package com.hs.test.maptest.viewmodel

import androidx.lifecycle.ViewModel

class UserViewModel: ViewModel() {
    private val _userId = "hanseul"     // todo: 회원가입 프로세스 만들어야 함
    val userId get() = _userId
}