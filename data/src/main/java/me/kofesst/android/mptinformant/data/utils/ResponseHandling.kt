package me.kofesst.android.mptinformant.data.utils

import retrofit2.Response

fun <T : Any> Response<T>.handle(): T? {
    val responseBody = body()
    return if (!isSuccessful || responseBody == null) {
        null
    } else {
        responseBody
    }
}