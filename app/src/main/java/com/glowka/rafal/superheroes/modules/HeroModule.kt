package com.glowka.rafal.superheroes.modules

import com.glowka.rafal.superhero.data.repository.FavouritesRepositoryImpl
import com.glowka.rafal.superhero.data.repository.HeroRepositoryImpl
import com.glowka.rafal.superhero.data.repository.cache.HeroCache
import com.glowka.rafal.superhero.data.repository.cache.HeroCacheImpl
import com.glowka.rafal.superhero.data.requestFactory.HeroRequestFactory
import com.glowka.rafal.superhero.data.requestFactory.HeroRequestFactoryImpl
import com.glowka.rafal.superhero.domain.repository.FavouritesRepository
import com.glowka.rafal.superhero.domain.repository.HeroRepository
import com.glowka.rafal.superhero.domain.usecase.*
import org.koin.dsl.module

/**
 * Created by Rafal on 13.04.2021.
 */
val heroModule = module {

  single<HeroCache> {
    HeroCacheImpl()
  }

  single<FavouritesRepository> {
    FavouritesRepositoryImpl(
      sharedPreferencesRepository = get(),
      jsonSerializer = get(),
    )
  }

  single<HeroRequestFactory> {
    HeroRequestFactoryImpl()
  }

  single<HeroRepository> {
    HeroRepositoryImpl(
      heroRequestFactory = get(),
      restClient = get(),
      cache = get(),
    )
  }

  factory<SearchByNameUseCase> {
    SearchByNameUseCaseImpl(
      heroRepository = get(),
    )
  }

  factory<PrepareCacheUseCase> {
    PrepareCacheUseCaseImpl(
      favouritesRepository = get(),
      heroRepository = get(),
    )
  }

  factory<LoadFavouritesUseCase> {
    LoadFavouritesUseCaseImpl(
      favouritesRepository = get(),
      heroRepository = get(),
    )
  }

  factory<ChangeIsHeroFavouriteUseCase> {
    ChangeIsHeroFavouriteUseCaseImpl(
      favouritesRepository = get(),
      heroRepository = get(),
    )
  }

}