package com.glowka.rafal.superhero.domain.usecase

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
class SearchByNameUseCaseTest {

  @MockK
  private lateinit var heroRepository: HeroRepository

  lateinit var useCase: SearchByNameUseCase

  @Before
  fun prepare() {
    MockKAnnotations.init(this)
    useCase = SearchByNameUseCaseImpl(
      heroRepository = heroRepository
    )
  }

  @After
  fun finish() {
    confirmVerified(heroRepository)

    unmockkAll()
    clearAllMocks()
  }

  @Test
  fun useCaseIsCallingRepositories() {
    // Given
    val query = "superman"
    val hero: Hero = mockk()

    every { heroRepository.searchByName(name = query) } returns Single.just(listOf(hero))

    // When
    val testObserver = useCase(param = query).test()

    // Than
    testObserver.assertComplete()
    verify { heroRepository.searchByName(name = query) }
  }
}