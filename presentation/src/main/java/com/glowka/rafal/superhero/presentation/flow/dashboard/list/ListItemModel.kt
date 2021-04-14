package com.glowka.rafal.superhero.presentation.flow.dashboard.list

import com.glowka.rafal.superhero.domain.model.Hero
import com.glowka.rafal.superhero.presentation.R
import com.glowka.rafal.superhero.presentation.binding.ImageModel
import com.glowka.rafal.superhero.presentation.binding.models.ImageBindingModel

/**
 * Created by Rafal on 15.04.2021.
 */
sealed class ListItemModel {
  class HeroCard(val hero: Hero, val isFavourite: Boolean) : ListItemModel() {
    val heroName = hero.name
    val image = ImageBindingModel(
      initialImage = ImageModel(
        url = hero.image.url,
        fallbackImageResId = R.drawable.hero_fallback
      )
    )
    val favIcon = ImageBindingModel(
      initialImage = ImageModel(
        fallbackImageResId = R.drawable.ic_favorite_on
      )
    ).apply {
      visible.postValue(isFavourite)
    }
  }
}