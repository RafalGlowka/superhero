package com.glowka.rafal.superhero.presentation.binding.fields

import com.glowka.rafal.superhero.domain.utils.EMPTY
import com.glowka.rafal.superhero.presentation.R
import com.glowka.rafal.superhero.presentation.binding.ImageModel
import com.glowka.rafal.superhero.presentation.binding.models.ImageBindingModel
import com.glowka.rafal.superhero.presentation.binding.models.TextBindingModel

/**
 * Created by Rafal on 18.04.2021.
 */
class HeaderFieldBindingModel(initialLabel: String = String.EMPTY) {
  val label = TextBindingModel(initialText = initialLabel)
  val favIcon = ImageBindingModel().apply { visible.postValue(false) }
  val closeIcon =
    ImageBindingModel(initialImage = ImageModel(fallbackImageResId = R.drawable.ic_close))
}