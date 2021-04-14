package com.glowka.rafal.superhero.data.repository

import com.glowka.rafal.superhero.data.repository.cache.HeroCache
import com.glowka.rafal.superhero.domain.model.Hero
import com.glowka.rafal.superhero.domain.repository.HeroRepository
import com.glowka.rafal.superhero.remote.RestClient
import io.reactivex.Single

/**
 * Created by Rafal on 14.04.2021.
 */
class HeroRepositoryImpl(
  private val restClient: RestClient,
  private val cache: HeroCache
) : HeroRepository {

  companion object {
    const val BASE_URL = "https://superheroapi.com/api.php/10216227941362245/"
  }

  override fun searchByName(name: String): Single<List<Hero>> {
    return restClient.get(
      url = BASE_URL + "search/$name",
      resultClass = SearchByNameResponse::class
    ).map { response ->
      response.results?.forEach { hero -> cache.update(hero = hero) }
      response.results ?: emptyList()
    }
  }

  class SearchByNameResponse(
    val response: String,
    val results: List<Hero>?
  )

  override fun searchById(id: String): Single<Hero?> {
    cache.get(id)?.let { hero -> return Single.just(hero) }

    return restClient.get(
      url = BASE_URL + "$id",
      resultClass = Hero::class
    ).map { hero ->
      cache.update(hero = hero)
      hero
    }
  }
}

