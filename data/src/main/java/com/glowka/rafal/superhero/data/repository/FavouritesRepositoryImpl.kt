package com.glowka.rafal.superhero.data.repository

import android.content.SharedPreferences
import com.glowka.rafal.superhero.data.remote.JSONSerializer
import com.glowka.rafal.superhero.domain.repository.FavouritesRepository
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by Rafal on 17.04.2021.
 */
class FavouritesRepositoryImpl(
  val sharedPreferences: SharedPreferences,
  val jsonSerializer: JSONSerializer
) : FavouritesRepository {

  companion object {
    const val FIELD_FAVOURITES = "favourites"
    val DEFAULT_FAVOURITES = listOf("649", "78", "9")
  }

  class FavouritesFieldType : ArrayList<String>()

  override fun loadFavorites(): Single<List<String>> = Single.fromCallable {
    val data = sharedPreferences.getString(FIELD_FAVOURITES, null)
    if (data == null) {
      DEFAULT_FAVOURITES
    } else {
      jsonSerializer.fromJSON<FavouritesFieldType>(data, FavouritesFieldType::class.java)
    }
  }

  override fun saveFavourites(favourities: List<String>) = Completable.fromCallable {
    val data = jsonSerializer.toJSON(favourities)
    val edit = sharedPreferences.edit()
    edit.putString(FIELD_FAVOURITES, data)
    edit.commit()
  }

}