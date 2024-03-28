package com.sample.weatherapp.repository

import com.sample.weatherapp.domain.Resource
import retrofit2.Response
import timber.log.Timber

abstract class BaseRepository {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Resource<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()?.let {
                    return Resource.Success(it)
                }
            }
            error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        Timber.e("remoteDataSource", message)
        return Resource.Error("Network call has failed for the following reason: $message")
    }
}