package com.glowka.rafal.superhero.presentation.flow.dashboard.details

import android.graphics.Color
import com.glowka.rafal.superhero.presentation.architecture.ScreenStructure
import org.koin.core.scope.Scope

object DetailsScreenStructure :
  ScreenStructure<DetailsViewModelToFlowInterface.Param, DetailsViewModelToFlowInterface.Event, DetailsViewModelToFlowInterface, DetailsViewModelToViewInterface>(
    statusBarColor = Color.TRANSPARENT,
    lightTextColor = false,
  ) {
  override val fragmentClass = DetailsFragment::class
  override fun Scope.viewModelCreator() = DetailsViewModelImpl(
    stringResolver = get(),
    changeIsHeroFavouriteUseCase = get()
  )
}