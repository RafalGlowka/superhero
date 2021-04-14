package com.glowka.rafal.superhero.presentation.binding.models

import androidx.lifecycle.MutableLiveData
import com.glowka.rafal.superhero.domain.utils.EMPTY

/**
 * Created by Rafal on 17.04.2021.
 */
class TextBindingModel(initialText: String = String.EMPTY) {
  val text = MutableLiveData(initialText)
  val visible = MutableLiveData(true)
}