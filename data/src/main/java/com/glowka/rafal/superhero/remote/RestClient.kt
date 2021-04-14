package com.glowka.rafal.superhero.remote

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

interface RestClient {
  fun <RESULT : Any> post(
    url: String,
    data: ByteArray? = null,
    resultClass: KClass<RESULT>
  ): Single<RESULT>

  fun <RESULT : Any> get(url: String, resultClass: KClass<RESULT>): Single<RESULT>
}

class RestClientImpl(
  private val okHttpClient: OkHttpClient,
  private val jsonSerializer: JSONSerializer
) : RestClient {

  override fun <RESULT : Any> post(
    url: String,
    data: ByteArray?,
    resultClass: KClass<RESULT>
  ): Single<RESULT> {
    val requestBody = data?.let { data -> RequestBody.create(null, data) }

    val request = Request.Builder()
      .url(url)
      .method("POST", requestBody)
      .build()

    return executeRequest(request, resultClass)
  }

  override fun <RESULT : Any> get(url: String, resultClass: KClass<RESULT>): Single<RESULT> {
    val request = Request.Builder()
      .url(url)
      .method("GET", null)
      .build()

    return executeRequest(request, resultClass)
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