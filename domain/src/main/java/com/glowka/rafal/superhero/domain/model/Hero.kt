package com.glowka.rafal.superhero.domain.model

import com.glowka.rafal.superhero.domain.utils.EMPTY

/**
 * Created by Rafal on 14.04.2021.
 */

class HeroPowerStats(
  val inteligence: String?,
  val strength: String?,
  val speed: String?,
  val durability: String?,
  val power: String?,
  val combat: String?,
)

class HeroBiography(
  val `full-name`: String,
  val `alter-egos`: String,
  val publisher: String,
)

class HeroImage(
  val url: String,
)

class Hero(
  val id: String,
  val name: String,
  val powerstats: HeroPowerStats?,
  val biography: HeroBiography?,
  val image: HeroImage,
) {
  companion object {
    val EMPTY = Hero(
      id = String.EMPTY,
      name = String.EMPTY,
      powerstats = null,
      biography = null,
      image = HeroImage(url = String.EMPTY)
    )
  }
}

fun Hero.isDC(): Boolean {
  return biography?.publisher == "DC Comics"
}
