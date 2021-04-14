package com.glowka.rafal.superhero.presentation.binding.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.glowka.rafal.superhero.domain.utils.EMPTY

/**
 * Created by Rafal on 15.04.2021.
 */
class ButtonBindingModel(
  initialLabel: String = String.EMPTY,
  initialIconResId: Int = 0,
  onClickAction: () -> Unit = {}
) {
  val label: LiveData<String> = MutableLiveData(initialLabel)
  val iconResId: LiveData<Int> = MutableLiveData(initialIconResId)
  val action: LiveData<() -> Unit> = MutableLiveData(onClickAction)
}