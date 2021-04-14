package com.glowka.rafal.superhero.domain.repository

import com.glowka.rafal.superhero.domain.model.Hero
import io.reactivex.Single

/**
 * Created by Rafal on 14.04.2021.
 */
interface HeroRepository {
  fun searchByName(name: String): Single<List<Hero>>
  fun searchById(id: String): Single<Hero?>
}