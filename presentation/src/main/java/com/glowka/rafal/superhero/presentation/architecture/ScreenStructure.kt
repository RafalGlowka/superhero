package com.glowka.rafal.superhero.presentation.architecture

import org.koin.core.scope.Scope
import kotlin.reflect.KClass

abstract class ScreenStructure<
    PARAM : Any,
    EVENT : ScreenEvent,
    VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>,
    VIEWMODEL_TO_VIEW : ViewModelToViewInterface,
    >(
  val statusBarColor : Int? = null,
  val lightTextColor : Boolean? = null,
    ) {
  abstract val fragmentClass: KClass<out BaseFragment<VIEWMODEL_TO_VIEW, *>>
  abstract fun Scope.viewModelCreator(): ViewModelInterface<PARAM, EVENT>
}