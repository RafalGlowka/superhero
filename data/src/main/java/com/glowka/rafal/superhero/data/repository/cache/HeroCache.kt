package com.glowka.rafal.superhero.data.repository.cache

import com.glowka.rafal.superhero.domain.model.Hero

/**
 * Created by Rafal on 17.04.2021.
 */
interface HeroCache {
  fun get(id: String): Hero?
  fun update(hero: Hero)
}

class HeroCacheImpl : HeroCache {

  val cache = HashMap<String, Hero>()

  override fun get(id: String): Hero? {
    return cache[id]
  }

  override fun update(hero: Hero) {
    cache[hero.id] = hero
  }

}