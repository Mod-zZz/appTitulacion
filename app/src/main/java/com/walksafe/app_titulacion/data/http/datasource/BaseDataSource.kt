package com.walksafe.app_titulacion.data.http.datasource

import com.walksafe.app_titulacion.utils.ResourceV2
import retrofit2.Response

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): ResourceV2<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return ResourceV2.success(body)
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): ResourceV2<T> {
        return ResourceV2.error("Network call has failed for a following reason: $message")
    }

}