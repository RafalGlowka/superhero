package com.glowka.rafal.superhero.domain.repository

import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by Rafal on 17.04.2021.
 */
interface FavouritesRepository {
  fun loadFavorites(): Single<List<String>>
  fun saveFavourites(favourities: List<String>): Completable
}