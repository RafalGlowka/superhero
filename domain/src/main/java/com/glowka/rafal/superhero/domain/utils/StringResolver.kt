package com.glowka.rafal.superhero.domain.utils

/**
 * Created by Rafal on 17.04.2021.
 */
interface StringResolver {
  operator fun invoke(resId: Int): String
  operator fun invoke(resId: Int, vararg args: Any): String
}