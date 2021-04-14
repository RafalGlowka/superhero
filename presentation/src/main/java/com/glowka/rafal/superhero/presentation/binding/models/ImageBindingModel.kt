package com.glowka.rafal.superhero.presentation.binding.models

import androidx.lifecycle.MutableLiveData
import com.glowka.rafal.superhero.presentation.binding.ImageModel

/**
 * Created by Rafal on 16.04.2021.
 */
class ImageBindingModel(initialImage: ImageModel = ImageModel(), onClickAction: () -> Unit = {}) {
  val image = MutableLiveData(initialImage)
  val action = MutableLiveData(onClickAction)
  val visible = MutableLiveData(true)
}