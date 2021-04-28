package com.glowka.rafal.superhero.data.remote

import com.google.gson.GsonBuilder

/**
 * Created by Rafal on 14.04.2021.
 */
interface JSONSerializer {
  fun <DATA> toJSON(data: DATA): String
  fun <DATA> fromJSON(string: String, clazz: Class<DATA>): DATA
}

class JSONSerializerImpl : JSONSerializer {

  private val gson = GsonBuilder()
    .disableHtmlEscaping()
    .create()

  override fun <DATA> toJSON(data: DATA): String {
    return gson.toJson(data)
  }

  override fun <DATA> fromJSON(string: String, clazz: Class<DATA>): DATA {
    return gson.fromJson(string, clazz) as DATA
  }

}