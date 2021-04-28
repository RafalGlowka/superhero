package com.glowka.rafal.superhero.domain.usecase

import com.glowka.rafal.superhero.domain.model.Hero
import com.glowka.rafal.superhero.domain.repository.HeroRepository
import io.reactivex.Single

/**
 * Created by Rafal on 14.04.2021.
 */
interface SearchByNameUseCase : SingleUseCase<String, List<Hero>>

class SearchByNameUseCaseImpl(
  private val heroRepository: HeroRepository
) : SearchByNameUseCase {
  override fun invoke(param: String): Single<List<Hero>> {
    return heroRepository
      .searchByName(name = param)
  }
}