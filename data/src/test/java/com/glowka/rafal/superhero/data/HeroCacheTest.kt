package com.glowka.rafal.superhero.data

import com.glowka.rafal.superhero.data.repository.cache.HeroCache
import com.glowka.rafal.superhero.data.repository.cache.HeroCacheImpl
import com.glowka.rafal.superhero.domain.model.Hero
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by Rafal on 27.04.2021.
 */
class HeroCacheTest {

  lateinit var cache: HeroCache

  @Before
  fun prepare() {
    MockKAnnotations.init(this)
    cache = HeroCacheImpl()
  }

  @After
  fun finish() {
    unmockkAll()
    clearAllMocks()
  }

  @Test
  fun cacheIsStoringValues() {
    // Given
    val HERO_ID = "123"
    val hero: Hero = mockk()

    every { hero.id } returns HERO_ID

    // When
    cache.update(hero = hero)

    // Then
    assertEquals(hero, cache.get(HERO_ID))
  }

}