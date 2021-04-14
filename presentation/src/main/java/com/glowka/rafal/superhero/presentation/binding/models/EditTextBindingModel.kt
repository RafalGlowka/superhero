package com.glowka.rafal.superhero.presentation.binding.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.glowka.rafal.superhero.domain.utils.EMPTY

/**
 * Created by Rafal on 15.04.2021.
 */
class EditTextBindingModel(initialText: String = String.EMPTY, initialHint: String = String.EMPTY) {
  val text: MutableLiveData<String> = MutableLiveData(initialText)
  val hint: LiveData<String> = MutableLiveData(initialHint)
}