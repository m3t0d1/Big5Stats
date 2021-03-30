package com.amadeus.android.big5stats.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Empty<T> : Resource<T>()
    class Loading<T> : Resource<T>()
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}