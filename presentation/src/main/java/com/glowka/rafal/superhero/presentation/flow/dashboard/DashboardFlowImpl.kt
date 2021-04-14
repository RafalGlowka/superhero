package com.glowka.rafal.superhero.presentation.flow.dashboard

import com.glowka.rafal.superhero.domain.utils.EmptyParam
import com.glowka.rafal.superhero.domain.utils.logD
import com.glowka.rafal.superhero.presentation.architecture.FlowInstance
import com.glowka.rafal.superhero.presentation.architecture.FlowScope
import com.glowka.rafal.superhero.presentation.architecture.Screen
import com.glowka.rafal.superhero.presentation.architecture.getViewModelToFlow
import com.glowka.rafal.superhero.presentation.flow.dashboard.details.DetailsViewModelToFlowInterface
import com.glowka.rafal.superhero.presentation.flow.dashboard.list.ListViewModelToFlowInterface
import com.glowka.rafal.superhero.presentation.utils.exhaustive

/**
 * Created by Rafal on 16.04.2021.
 */
sealed class DashboardResult {
  object Terminated : DashboardResult()
}

class DashboardFlowImpl(flowScope: FlowScope<EmptyParam, DashboardResult>) :
  FlowInstance<EmptyParam, DashboardResult>(flowScope) {
  override fun onStart(param: EmptyParam): Screen<*, *, *, *> {
    switchScreen(
      screen = DashboardFlow.Screens.List,
      param = EmptyParam.EMPTY,
      onEvent = ::onListEvent
    )
    return DashboardFlow.Screens.List
  }

  private fun onListEvent(event: ListViewModelToFlowInterface.Event) {
    when (event) {
      is ListViewModelToFlowInterface.Event.ShowDetails -> {
        logD("show Details")
        switchScreen(
          screen = DashboardFlow.Screens.Details,
          param = DetailsViewModelToFlowInterface.Param(
            hero = event.hero,
            isFavourite = event.isFavourite
          ),
          onEvent = ::onDetailsEvent
        )
      }
      ListViewModelToFlowInterface.Event.Back -> {
        finish(result = DashboardResult.Terminated)
      }
    }.exhaustive
  }

  private fun onDetailsEvent(event: DetailsViewModelToFlowInterface.Event) {
    when (event) {
      DetailsViewModelToFlowInterface.Event.Back -> {
        val viewModel = getViewModelToFlow(DashboardFlow.Screens.List)
        viewModel.refreshFavourites()
        switchBackTo(DashboardFlow.Screens.List)
      }
    }.exhaustive
  }
}