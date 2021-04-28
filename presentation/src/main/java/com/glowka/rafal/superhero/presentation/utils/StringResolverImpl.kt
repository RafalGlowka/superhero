package com.glowka.rafal.superhero.presentation.utils

import android.content.Context
import com.glowka.rafal.superhero.domain.utils.StringResolver

/**
 * Created by Rafal on 17.04.2021.
 */
class StringResolverImpl(
  val context: Context,
) : StringResolver {

  override fun invoke(resId: Int): String {
    return context.getString(resId)
  }

  override fun invoke(resId: Int, vararg args: Any): String {
    return context.getString(resId, *args)
  }
}