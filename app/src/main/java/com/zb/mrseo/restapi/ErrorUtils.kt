package com.zb.mrseo.restapi




import retrofit2.Response
import java.io.IOException

object ErrorUtils {

    fun parseError(response: Response<*>): ApiStringError {
        val converter = ApiInitialize.initializes()
            .responseBodyConverter<ApiStringError>(ApiStringError::class.java, arrayOfNulls<Annotation>(0))
        val error: ApiStringError
        try {
            error = converter.convert(response.errorBody()!!)!!
        } catch (e: IOException) {
            return ApiStringError()
        }

        return error
    }
}
