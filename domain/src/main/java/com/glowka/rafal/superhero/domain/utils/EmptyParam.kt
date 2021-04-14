package com.glowka.rafal.superhero.domain.utils

/**
 * Created by Rafal on 16.04.2021.
 */
class EmptyParam private constructor() {
  companion object {
    val EMPTY = EmptyParam()
  }
}