package com.glowka.rafal.superhero.presentation.flow.intro

import com.glowka.rafal.superhero.domain.utils.EmptyParam
import com.glowka.rafal.superhero.domain.utils.logE
import com.glowka.rafal.superhero.presentation.architecture.BaseFlow
import com.glowka.rafal.superhero.presentation.architecture.Screen
import com.glowka.rafal.superhero.presentation.flow.dashboard.DashboardFlow
import com.glowka.rafal.superhero.presentation.flow.dashboard.DashboardResult
import com.glowka.rafal.superhero.presentation.utils.exhaustive
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by Rafal on 16.04.2021.
 */
sealed class IntroResult {
  object Terminated : IntroResult()
}

class IntroFlowImpl(
  val dashboardFlow: DashboardFlow,
) :
  BaseFlow<EmptyParam, IntroResult>(flowScope = IntroFlow.SCOPE), IntroFlow {

  override fun onStart(param: EmptyParam): Screen<*, *, *, *> {
    switchScreen(
      screen = IntroFlow.Screens.Start,
      param = EmptyParam.EMPTY,
      onEvent = ::onStartEvent
    )
    return IntroFlow.Screens.Start
  }

  private fun onStartEvent(event: IntroViewModelToFlowInterface.Event) {
    when (event) {
      IntroViewModelToFlowInterface.Event.Finished -> {
        dashboardFlow.start(navigator = navigator, param = EmptyParam.EMPTY).subscribeBy(
          onSuccess = { result ->
            when (result) {
              DashboardResult.Terminated -> finish(result = IntroResult.Terminated)
            }.exhaustive
          },
          onError = { error -> logE("error", error) }
        ).disposedByHost()
      }
    }.exhaustive
  }

}