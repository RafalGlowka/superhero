package com.glowka.rafal.superhero.data

import com.glowka.rafal.superhero.data.remote.RestClient
import com.glowka.rafal.superhero.data.remote.RestRequest
import com.glowka.rafal.superhero.data.repository.HeroRepositoryImpl
import com.glowka.rafal.superhero.data.repository.cache.HeroCache
import com.glowka.rafal.superhero.data.requestFactory.HeroRequestFactory
import com.glowka.rafal.superhero.domain.model.Hero
import com.glowka.rafal.superhero.domain.repository.HeroRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by Rafal on 27.04.2021.
 */
class HeroRepositoryTest {

  @MockK
  private lateinit var restClient: RestClient

  @MockK
  private lateinit var cache: HeroCache

  @MockK
  private lateinit var heroRequestFactory: HeroRequestFactory

  lateinit var repository: HeroRepository

  @Before
  fun prepare() {
    MockKAnnotations.init(this)
    repository = HeroRepositoryImpl(
      heroRequestFactory = heroRequestFactory,
      restClient = restClient,
      cache = cache
    )
  }

  @After
  fun finish() {
    confirmVerified(
      restClient,
      cache
    )
    unmockkAll()
    clearAllMocks()
  }

  @Test
  fun checkingCache() {
    // Given
    val HERO_ID = "123"
    val hero: Hero = mockk()

    every { cache.get(HERO_ID) } returns hero

    // When
    val testObserver = repository.searchById(HERO_ID).test()

    // Then
    testObserver.assertComplete()
    verify { cache.get(HERO_ID) }
  }

  @Test
  fun checkingBE() {
    // Given
    val HERO_ID = "123"
    val hero: Hero = mockk()
    val searchRequest: RestRequest<Hero> = mockk()

    every { cache.get(HERO_ID) } returns null
    every { cache.update(hero) } returns Unit
    every { heroRequestFactory.createSearchById(id = HERO_ID) } returns searchRequest
    every { restClient.execute(request = searchRequest) } returns Single.just(hero)

    // When
    val testObserver = repository.searchById(HERO_ID).test()

    // Then
    testObserver.assertComplete()
    verify { cache.get(HERO_ID) }
    verify { cache.update(hero) }
    verify { heroRequestFactory.createSearchById(id = HERO_ID) }
    verify { restClient.execute(request = searchRequest) }
  }


}