package com.glowka.rafal.superhero.presentation.flow.dashboard

import com.glowka.rafal.superhero.domain.utils.EmptyParam
import com.glowka.rafal.superhero.presentation.architecture.*
import com.glowka.rafal.superhero.presentation.flow.dashboard.details.DetailsFragment
import com.glowka.rafal.superhero.presentation.flow.dashboard.details.DetailsViewModelToFlowInterface
import com.glowka.rafal.superhero.presentation.flow.dashboard.details.DetailsViewModelToViewInterface
import com.glowka.rafal.superhero.presentation.flow.dashboard.list.ListFragment
import com.glowka.rafal.superhero.presentation.flow.dashboard.list.ListViewModelToFlowInterface
import com.glowka.rafal.superhero.presentation.flow.dashboard.list.ListViewModelToViewInterface

/**
 * Created by Rafal on 18.04.2021.
 */
@Suppress("MaxLineLength")
interface DashboardFlow : Flow<EmptyParam, DashboardResult> {

  companion object {
    val SCOPE = DIScope("Dashboard")
  }

  sealed class Screens<PARAM : Any, EVENT : ScreenEvent,
      VIEWMODEL_TO_VIEW : ViewModelToViewInterface,
      VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>> :
    Screen<PARAM, EVENT, VIEWMODEL_TO_VIEW, VIEWMODEL_TO_FLOW>(scope = SCOPE) {

    object List :
      Screens<EmptyParam, ListViewModelToFlowInterface.Event, ListViewModelToViewInterface, ListViewModelToFlowInterface>() {
      override val fragmentClass = ListFragment::class
    }

    object Details :
      Screens<DetailsViewModelToFlowInterface.Param, DetailsViewModelToFlowInterface.Event, DetailsViewModelToViewInterface, DetailsViewModelToFlowInterface>() {
      override val fragmentClass = DetailsFragment::class
    }
  }
}