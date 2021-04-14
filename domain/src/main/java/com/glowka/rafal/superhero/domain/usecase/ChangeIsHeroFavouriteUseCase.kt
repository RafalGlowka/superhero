package com.glowka.rafal.superhero.domain.usecase

import com.glowka.rafal.superhero.domain.model.Hero
import com.glowka.rafal.superhero.domain.repository.FavouritesRepository
import com.glowka.rafal.superhero.domain.repository.HeroRepository
import com.glowka.rafal.superhero.domain.usecase.ChangeIsHeroFavouriteUseCase.Param
import io.reactivex.Single

/**
 * Created by Rafal on 17.04.2021.
 */
interface ChangeIsHeroFavouriteUseCase : SingleUseCase<Param, List<Hero>> {
  data class Param(
    val hero: Hero,
    val isFavourite: Boolean
  )
}

class ChangeIsHeroFavouriteUseCaseImpl(
  private val favouritesRepository: FavouritesRepository,
  private val heroRepository: HeroRepository,
) : ChangeIsHeroFavouriteUseCase {

  override fun invoke(param: Param): Single<List<Hero>> {

    return favouritesRepository.loadFavorites()
      .map { list ->
        val newList = list.toMutableList()
        if (param.isFavourite) {
          if (newList.contains(param.hero.id).not()) {
            newList.add(param.hero.id)
          }
        } else {
          newList.remove(param.hero.id)
        }
        newList
      }
      .flatMap { list -> favouritesRepository.saveFavourites(list).toSingleDefault(list) }
      .flattenAsObservable { list -> list }
      .flatMapSingle { heroId ->
        heroRepository.searchById(id = heroId).onErrorReturnItem(Hero.EMPTY)
      }
      .toList()
      .map { list -> list.filter { item -> item != Hero.EMPTY }.filterNotNull() }
  }

}