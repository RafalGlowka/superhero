package com.glowka.rafal.superhero.domain.usecase

import com.glowka.rafal.superhero.domain.model.Hero
import com.glowka.rafal.superhero.domain.repository.FavouritesRepository
import com.glowka.rafal.superhero.domain.repository.HeroRepository
import com.glowka.rafal.superhero.domain.utils.EmptyParam
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by Rafal on 27.04.2021.
 */
class LoadFavouritesUseCaseTest {

  @MockK
  private lateinit var favouritesRepository: FavouritesRepository

  @MockK
  private lateinit var heroRepository: HeroRepository

  lateinit var useCase: LoadFavouritesUseCase

  @Before
  fun prepare() {
    MockKAnnotations.init(this)
    useCase = LoadFavouritesUseCaseImpl(
      favouritesRepository = favouritesRepository,
      heroRepository = heroRepository
    )
  }

  @After
  fun finish() {
    confirmVerified(favouritesRepository, heroRepository)

    unmockkAll()
    clearAllMocks()
  }

  @Test
  fun useCaseIsCallingRepositories() {
    // Given
    val HERO_ID = "432"
    val hero: Hero = mockk()
    val params = EmptyParam.EMPTY

    every { hero.id } returns HERO_ID
    every { favouritesRepository.loadFavorites() } returns Single.just(listOf(HERO_ID))
    every { heroRepository.searchById(id = HERO_ID) } returns Single.just(hero)

    // When
    val testObserver = useCase(param = params).test()

    // Than
    testObserver.assertComplete()
    verify { favouritesRepository.loadFavorites() }
    verify { heroRepository.searchById(id = HERO_ID) }
  }
}