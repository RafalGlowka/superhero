package com.glowka.rafal.superhero.presentation.flow.intro

import com.glowka.rafal.superhero.domain.utils.EmptyParam
import com.glowka.rafal.superhero.presentation.architecture.*

/**
 * Created by Rafal on 18.04.2021.
 */

object IntroFlow : FlowScope<EmptyParam, IntroResult>(scopeName = "Intro") {

  sealed class Screens<PARAM : Any, EVENT : ScreenEvent, VIEWMODEL_TO_VIEW : ViewModelToViewInterface, VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>> :
    Screen<PARAM, EVENT, VIEWMODEL_TO_VIEW, VIEWMODEL_TO_FLOW>(scope = this) {

    object Start :
      Screens<EmptyParam, IntroViewModelToFlowInterface.Event, IntroViewModelToViewInterface, IntroViewModelToFlowInterface>() {
      override val fragmentClass = IntroFragment::class
    }
  }
}