package com.glowka.rafal.superhero.domain.utils

import android.util.Log

/**
 * Created by Rafal on 14.04.2021.
 */
inline val Any.logTag: String
  get() {
    val tag = this::class.java.simpleName
    if (tag.isEmpty()) return "AnonymousObject"
    return tag
  }

inline fun Any.logD(message: String) {
  Log.d(logTag, message)
}

inline fun Any.logD(message: String, throwable: Throwable) {
  Log.d(logTag, message, throwable)
}

inline fun Any.logE(message: String) {
  Log.e(logTag, message)
}

inline fun Any.logE(message: String, throwable: Throwable) {
  Log.e(logTag, message, throwable)
}