package com.glowka.rafal.superhero.presentation.binding.fields

import com.glowka.rafal.superhero.domain.utils.EMPTY
import com.glowka.rafal.superhero.presentation.binding.models.TextBindingModel

/**
 * Created by Rafal on 17.04.2021.
 */
class TextFieldBindingModel(
  initialLabel: String = String.EMPTY,
  initialValue: String = String.EMPTY
) {
  val label = TextBindingModel(initialText = initialLabel)
  val value = TextBindingModel(initialText = initialValue)
}