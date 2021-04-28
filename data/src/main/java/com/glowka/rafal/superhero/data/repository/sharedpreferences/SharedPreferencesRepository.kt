package com.glowka.rafal.superhero.data.repository.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Rafal on 28.04.2021.
 */
interface SharedPreferencesRepository {
  fun getFavouritesSharedPreferences(): SharedPreferences
}

class SharedPreferencesRepositoryImpl(
  private val context: Context,
) : SharedPreferencesRepository {

  companion object {
    const val SHARED_PREFERENCES_FAVOURITES = "favs"
  }

  override fun getFavouritesSharedPreferences(): SharedPreferences {
    return context.getSharedPreferences(SHARED_PREFERENCES_FAVOURITES, Context.MODE_PRIVATE)
  }

}