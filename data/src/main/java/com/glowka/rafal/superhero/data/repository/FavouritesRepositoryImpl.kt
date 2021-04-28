package com.glowka.rafal.superhero.data.repository

import com.glowka.rafal.superhero.data.remote.JSONSerializer
import com.glowka.rafal.superhero.data.repository.sharedpreferences.SharedPreferencesRepository
import com.glowka.rafal.superhero.domain.repository.FavouritesRepository
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by Rafal on 17.04.2021.
 */
class FavouritesRepositoryImpl(
  sharedPreferencesRepository: SharedPreferencesRepository,
  private val jsonSerializer: JSONSerializer
) : FavouritesRepository {

  companion object {
    const val FIELD_FAVOURITES = "favourites"
    val DEFAULT_FAVOURITES = listOf("649", "78", "9")
  }

  val sharedPreferences = sharedPreferencesRepository.getFavouritesSharedPreferences()

  class FavouritesFieldType : ArrayList<String>()

  override fun loadFavorites(): Single<List<String>> = Single.fromCallable {
    val data = sharedPreferences.getString(FIELD_FAVOURITES, null)
    if (data == null) {
      DEFAULT_FAVOURITES
    } else {
      jsonSerializer.fromJSON(data, FavouritesFieldType::class.java)
    }
  }

  override fun saveFavourites(favourities: List<String>) = Completable.fromCallable {
    val data = jsonSerializer.toJSON(favourities)
    val edit = sharedPreferences.edit()
    edit.putString(FIELD_FAVOURITES, data)
    edit.commit()
  }

}