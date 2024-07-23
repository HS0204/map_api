package com.hs.test.maptest.util

fun getCurrentDateTime(): String {
    return java.text.SimpleDateFormat("yyyyMMddHHmmss").format(java.util.Date())
}

fun String.formatTime(): String {
    return this.substring(0, 4) + "년 " + this.substring(4, 6) + "월 " + this.substring(6, 8) + "일 " +
            this.substring(8, 10) + ":" + this.substring(10, 12) + ":" + this.substring(12, 14)
}