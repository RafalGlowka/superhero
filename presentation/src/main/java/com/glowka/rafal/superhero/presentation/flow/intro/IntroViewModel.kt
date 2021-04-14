package com.glowka.rafal.superhero.presentation.flow.intro

import com.glowka.rafal.superhero.domain.usecase.PrepareCacheUseCase
import com.glowka.rafal.superhero.domain.utils.EmptyParam
import com.glowka.rafal.superhero.domain.utils.logD
import com.glowka.rafal.superhero.domain.utils.logE
import com.glowka.rafal.superhero.presentation.architecture.BaseViewModel
import com.glowka.rafal.superhero.presentation.architecture.ScreenEvent
import com.glowka.rafal.superhero.presentation.architecture.ViewModelToFlowInterface
import com.glowka.rafal.superhero.presentation.architecture.ViewModelToViewInterface
import com.glowka.rafal.superhero.presentation.flow.intro.IntroViewModelToFlowInterface.Event
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by Rafal on 13.04.2021.
 */

interface IntroViewModelToFlowInterface : ViewModelToFlowInterface<EmptyParam, Event> {
  sealed class Event : ScreenEvent {
    object Finished : Event()
  }
}

interface IntroViewModelToViewInterface : ViewModelToViewInterface {
  fun introFinished()
}

class IntroViewModelImpl(
  private val prepareCacheUseCase: PrepareCacheUseCase
) : IntroViewModelToFlowInterface, IntroViewModelToViewInterface, BaseViewModel<EmptyParam, Event>(
  backPressedEvent = null
) {

  override fun init(param: EmptyParam) {
    prepareCacheUseCase(param = EmptyParam.EMPTY).subscribeBy(
      onComplete = { /* nop */ },
      onError = { error -> logE("prepare cache", error) }
    ).disposedByHost()
  }

  override fun introFinished() {
    logD("Finished")
    sendEvent(event = Event.Finished)
  }
}