package com.glowka.rafal.superhero.domain.usecase

import com.glowka.rafal.superhero.domain.repository.FavouritesRepository
import com.glowka.rafal.superhero.domain.repository.HeroRepository
import com.glowka.rafal.superhero.domain.utils.EmptyParam
import io.reactivex.Completable

/**
 * Created by Rafal on 17.04.2021.
 */
interface PrepareCacheUseCase : CompletableUseCase<EmptyParam>

class PrepareCacheUseCaseImpl(
  private val favouritesRepository: FavouritesRepository,
  private val heroRepository: HeroRepository,
) : PrepareCacheUseCase {
  override fun invoke(param: EmptyParam): Completable {
    return favouritesRepository.loadFavorites()
      .flattenAsObservable { list -> list }
      .flatMapSingle { heroId -> heroRepository.searchById(id = heroId).onErrorReturnItem(null) }
      .toList().ignoreElement()
  }
}