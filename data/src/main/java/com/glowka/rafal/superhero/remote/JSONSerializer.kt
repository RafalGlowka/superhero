package com.glowka.rafal.superhero.remote

import com.google.gson.GsonBuilder
import java.lang.reflect.Type

/**
 * Created by Rafal on 14.04.2021.
 */
interface JSONSerializer {
  fun <DATA> toJSON(data: DATA): String
  fun <DATA> fromJSON(string: String, type: Type): DATA
}

class JSONSerializerImpl : JSONSerializer {

  private val gson = GsonBuilder()
    .disableHtmlEscaping()
    .create()

  override fun <DATA> toJSON(data: DATA): String {
    return gson.toJson(data)
  }

  override fun <DATA> fromJSON(string: String, type: Type): DATA {
    return gson.fromJson(string, type) as DATA
  }

}