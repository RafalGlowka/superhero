package com.glowka.rafal.superhero.presentation.flow.dashboard.list

import com.glowka.rafal.superhero.domain.utils.EmptyParam
import com.glowka.rafal.superhero.presentation.architecture.ScreenStructure
import org.koin.core.scope.Scope

object ListScreenStructure : ScreenStructure<EmptyParam, ListViewModelToFlowInterface.Event,
    ListViewModelToFlowInterface, ListViewModelToViewInterface>() {
  override val fragmentClass = ListFragment::class
  override fun Scope.viewModelCreator() = ListViewModelImpl(
    stringResolver = get(),
    loadFavouritesUseCase = get(),
    searchByNameUseCase = get(),
  )

}