package com.glowka.rafal.superhero.domain.usecase

import com.glowka.rafal.superhero.domain.model.Hero
import com.glowka.rafal.superhero.domain.repository.FavouritesRepository
import com.glowka.rafal.superhero.domain.repository.HeroRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by Rafal on 27.04.2021.
 */
class ChangeIsHeroFavouriteUseCaseTest {

  @MockK
  private lateinit var favouritesRepository: FavouritesRepository

  @MockK
  private lateinit var heroRepository: HeroRepository

  lateinit var useCase: ChangeIsHeroFavouriteUseCase

  @Before
  fun prepare() {
    MockKAnnotations.init(this)
    useCase = ChangeIsHeroFavouriteUseCaseImpl(
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
    val params = ChangeIsHeroFavouriteUseCase.Param(
      hero = hero,
      isFavourite = true
    )

    every { hero.id } returns HERO_ID
    every { favouritesRepository.loadFavorites() } returns Single.just(listOf(HERO_ID))
    every { favouritesRepository.saveFavourites(favourities = listOf(HERO_ID)) } returns Completable.complete()
    every { heroRepository.searchById(id = HERO_ID) } returns Single.just(hero)

    // When
    val testObserver = useCase(param = params).test()

    // Than
    testObserver.assertComplete()

    verify { favouritesRepository.loadFavorites() }
    verify { favouritesRepository.saveFavourites(favourities = listOf(HERO_ID)) }
    verify { heroRepository.searchById(id = HERO_ID) }
  }
}