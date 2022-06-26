package com.curri.castockapplication.utils

sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data: T?) : Resource<T>(data = data)
    class Error<T>(message: String) : Resource<T>(data = null, message = message)
    class Loading<T>(isLoading: Boolean = true) : Resource<T>(null)
}