package com.glowka.rafal.superhero.data.repository.sharedpreferences

import android.content.SharedPreferences

class FakeSharedPreferencesRepository(private val sharedPreferences: SharedPreferences) :
  SharedPreferencesRepository {
  override fun getFavouritesSharedPreferences(): SharedPreferences {
    return sharedPreferences
  }
}