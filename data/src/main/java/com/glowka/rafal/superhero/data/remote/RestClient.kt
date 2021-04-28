package com.glowka.rafal.superhero.data.remote

import com.glowka.rafal.superhero.domain.utils.EMPTY
import com.glowka.rafal.superhero.domain.utils.logD
import io.reactivex.Single
import okhttp3.*
import java.io.IOException
import java.net.ConnectException
import kotlin.reflect.KClass

/**
 * Created by Rafal on 14.04.2021.
 */

enum class RequestType(val method: String) {
  POST("POST"),
  GET("GET")
}

class RestRequest<RESULT : Any>(
  val type: RequestType,
  val url: String,
  val resultClass: KClass<RESULT>,
  val data: ByteArray? = null,
)

interface RestClient {

  fun <RESULT : Any> execute(request: RestRequest<RESULT>): Single<RESULT>
}

class RestClientImpl(
  private val okHttpClient: OkHttpClient,
  private val jsonSerializer: JSONSerializer
) : RestClient {

  override fun <RESULT : Any> execute(request: RestRequest<RESULT>): Single<RESULT> {
    val requestBody = request.data?.let { data -> RequestBody.create(null, data) }

    val okHttpRequest = Request.Builder()
      .url(request.url)
      .method(request.type.method, requestBody)
      .build()

    return executeRequest(okHttpRequest, request.resultClass)
  }

  private fun <RESULT : Any> executeRequest(
    request: Request,
    resultClass: KClass<RESULT>
  ): Single<RESULT> {
    return Single.create<Response> { emitter ->
      okHttpClient.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
          emitter.onError(e)
        }

        override fun onResponse(call: Call, response: Response) {
          emitter.onSuccess(response)
        }
      })
    }.map { response ->
      if (response.isSuccessful) {
        val responseData = response.body?.string() ?: String.EMPTY
        logD("response: $responseData")
        jsonSerializer.fromJSON<RESULT>(responseData, resultClass.java)
      } else throw ConnectException()
    }
  }


}