package com.glowka.rafal.superheroes.modules.scene

import com.glowka.rafal.superhero.presentation.architecture.scope
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

  scope(flowScope = DashboardFlow.SCOPE) {

    fragment { ListFragment() }
    screenViewModel(screen = DashboardFlow.Screens.List) {
      ListViewModelImpl(
        stringResolver = get(),
        loadFavouritesUseCase = get(),
        searchByNameUseCase = get(),
      )
    }
    fragment { DetailsFragment() }
    screenViewModel(screen = DashboardFlow.Screens.Details) {
      DetailsViewModelImpl(
        stringResolver = get(),
        changeIsHeroFavouriteUseCase = get()
      )
    }
  }

}