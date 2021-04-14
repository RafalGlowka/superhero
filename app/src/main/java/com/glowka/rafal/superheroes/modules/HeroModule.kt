package com.glowka.rafal.superheroes.modules

import android.content.Context
import android.content.SharedPreferences
import com.glowka.rafal.superhero.data.repository.FavouritesRepositoryImpl
import com.glowka.rafal.superhero.data.repository.HeroRepositoryImpl
import com.glowka.rafal.superhero.data.repository.cache.HeroCache
import com.glowka.rafal.superhero.data.repository.cache.HeroCacheImpl
import com.glowka.rafal.superhero.domain.repository.FavouritesRepository
import com.glowka.rafal.superhero.domain.repository.HeroRepository
import com.glowka.rafal.superhero.domain.usecase.*
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Created by Rafal on 13.04.2021.
 */
val heroModule = module {

  single<HeroCache> {
    HeroCacheImpl()
  }

  single<SharedPreferences>(named<FavouritesRepository>()) {
    androidApplication().getSharedPreferences("favs", Context.MODE_PRIVATE)
  }

  single<FavouritesRepository> {
    FavouritesRepositoryImpl(
      sharedPreferences = get(named<FavouritesRepository>()),
      jsonSerializer = get(),
    )
  }

  single<HeroRepository> {
    HeroRepositoryImpl(
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