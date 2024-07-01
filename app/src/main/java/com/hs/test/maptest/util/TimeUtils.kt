package com.hs.test.maptest.util

fun getCurrentDateTime(): String {
    return java.text.SimpleDateFormat("yyyyMMddHHmmss").format(java.util.Date())
}