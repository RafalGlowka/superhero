package com.glowka.rafal.superhero.data.repository

import com.glowka.rafal.superhero.data.remote.RestClient
import com.glowka.rafal.superhero.data.repository.cache.HeroCache
import com.glowka.rafal.superhero.data.requestFactory.HeroRequestFactory
import com.glowka.rafal.superhero.domain.model.Hero
import com.glowka.rafal.superhero.domain.repository.HeroRepository
import io.reactivex.Single

/**
 * Created by Rafal on 14.04.2021.
 */
class HeroRepositoryImpl(
  private val restClient: RestClient,
  private val cache: HeroCache,
  private val heroRequestFactory: HeroRequestFactory,
) : HeroRepository {

  override fun searchByName(name: String): Single<List<Hero>> {
    val request = heroRequestFactory.createSearchByName(name)
    return restClient.execute(request = request)
      .map { response ->
        response.results?.forEach { hero ->
          cache.update(hero = hero)
        }
        response.results ?: emptyList()
      }
  }
  override fun searchById(id: String): Single<Hero?> {
    cache.get(id)?.let { hero ->
      return Single.just(hero)
    }

    val request = heroRequestFactory.createSearchById(id = id)

    return restClient.execute(request = request).map { hero ->
      cache.update(hero = hero)
      hero
    }
  }
}

