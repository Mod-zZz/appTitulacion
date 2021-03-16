package com.example.app_titulacion.utils

/**
 * Created by Gastón Saillén on 21 December 2019
 */
sealed class Resource<out T> {
    class Loading<out T> : Resource<T>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure<out T>(val throwable: Throwable) : Resource<T>()
}

data class ResourceV2<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T): ResourceV2<T> {
            return ResourceV2(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T? = null): ResourceV2<T> {
            return ResourceV2(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? = null): ResourceV2<T> {
            return ResourceV2(Status.LOADING, data, null)
        }
    }

}