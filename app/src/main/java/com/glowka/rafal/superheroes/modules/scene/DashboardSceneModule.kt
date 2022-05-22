package com.glowka.rafal.superheroes.modules.scene

import com.glowka.rafal.superhero.presentation.architecture.businessFlow
import com.glowka.rafal.superhero.presentation.architecture.screen
import com.glowka.rafal.superhero.presentation.architecture.screenViewModel
import com.glowka.rafal.superhero.presentation.flow.dashboard.DashboardFlow
import com.glowka.rafal.superhero.presentation.flow.dashboard.DashboardFlowImpl
import com.glowka.rafal.superhero.presentation.flow.dashboard.details.DetailsFragment
import com.glowka.rafal.superhero.presentation.flow.dashboard.details.DetailsViewModelImpl
import com.glowka.rafal.superhero.presentation.flow.dashboard.list.ListFragment
import com.glowka.rafal.superhero.presentation.flow.dashboard.list.ListViewModelImpl
import org.koin.androidx.fragment.dsl.fragment
import org.koin.dsl.module

/**
 * Created by Rafal on 14.04.2021.
 */
val dashboardSceneModule = module {

  single<DashboardFlow> {
    DashboardFlowImpl()
  }

  businessFlow(
    scopeName = DashboardFlow.SCOPE_NAME,
  ) {
    screen(screen = DashboardFlow.Screens.List)
    screen(screen = DashboardFlow.Screens.Details)
  }

}