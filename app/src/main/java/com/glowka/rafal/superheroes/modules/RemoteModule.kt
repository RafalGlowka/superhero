package com.glowka.rafal.superheroes.modules

import com.glowka.rafal.superhero.remote.JSONSerializer
import com.glowka.rafal.superhero.remote.JSONSerializerImpl
import com.glowka.rafal.superhero.remote.RestClient
import com.glowka.rafal.superhero.remote.RestClientImpl
import okhttp3.OkHttpClient
import org.koin.dsl.module

/**
 * Created by Rafal on 13.04.2021.
 */
val remoteModule = module {

  factory<OkHttpClient> {
    OkHttpClient.Builder().build()
  }

  single<JSONSerializer> {
    JSONSerializerImpl()
  }

  single<RestClient> {
    RestClientImpl(
      jsonSerializer = get(),
      okHttpClient = get(),
    )
  }

}