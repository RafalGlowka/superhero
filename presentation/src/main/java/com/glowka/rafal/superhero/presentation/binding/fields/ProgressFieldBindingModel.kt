package com.glowka.rafal.superhero.presentation.binding.fields

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.glowka.rafal.superhero.domain.utils.EMPTY

/**
 * Created by Rafal on 17.04.2021.
 */
data class ProgressPosition(val position: Int = 0, val maxPosition: Int = 100)

class ProgressFieldBindingModel(
  initialLabel: String = String.EMPTY,
  initialProgress: ProgressPosition = ProgressPosition()
) {
  val label: LiveData<String> = MutableLiveData(initialLabel)
  val progress: LiveData<ProgressPosition> = MutableLiveData(initialProgress)
}