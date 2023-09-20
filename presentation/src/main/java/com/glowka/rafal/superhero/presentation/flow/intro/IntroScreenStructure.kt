package com.glowka.rafal.superhero.presentation.flow.intro

import com.glowka.rafal.superhero.domain.utils.EmptyParam
import com.glowka.rafal.superhero.presentation.architecture.ScreenStructure
import org.koin.core.scope.Scope

object IntroScreenStructure : ScreenStructure<EmptyParam, IntroViewModelToFlowInterface.Event,
    IntroViewModelToFlowInterface, IntroViewModelToViewInterface>(
  statusBarColor = 0xFFFFFFFF.toInt(),
  lightTextColor = false,
) {
  override val fragmentClass = IntroFragment::class
  override fun Scope.viewModelCreator() = IntroViewModelImpl(
    prepareCacheUseCase = get()
  )
}