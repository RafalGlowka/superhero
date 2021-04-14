package com.glowka.rafal.superhero.domain.usecase

import com.glowka.rafal.superhero.domain.model.Hero
import com.glowka.rafal.superhero.domain.repository.FavouritesRepository
import com.glowka.rafal.superhero.domain.repository.HeroRepository
import com.glowka.rafal.superhero.domain.utils.EmptyParam
import io.reactivex.Single

/**
 * Created by Rafal on 17.04.2021.
 */
interface LoadFavouritesUseCase : SingleUseCase<EmptyParam, List<Hero>>

class LoadFavouritesUseCaseImpl(
  private val favouritesRepository: FavouritesRepository,
  private val heroRepository: HeroRepository,
) : LoadFavouritesUseCase {

  override fun invoke(param: EmptyParam): Single<List<Hero>> {

    return favouritesRepository.loadFavorites()
      .flattenAsObservable { list -> list }
      .flatMapSingle { heroId ->
        heroRepository.searchById(id = heroId).onErrorReturnItem(Hero.EMPTY)
      }
      .toList()
      .map { list -> list.filter { item -> item != Hero.EMPTY }.filterNotNull() }
  }

}