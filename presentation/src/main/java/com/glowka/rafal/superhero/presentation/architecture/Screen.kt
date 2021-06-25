package com.glowka.rafal.superhero.presentation.architecture

import kotlin.reflect.KClass

/**
 * Created by Rafal on 13.04.2021.
 */

interface ScreenEvent

abstract class Screen<
    PARAM : Any,
    EVENT : ScreenEvent,
    VIEWMODEL_TO_VIEW : ViewModelToViewInterface,
    VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>
    >(
  val scope: DIScope,
) {
  abstract val fragmentClass: KClass<out BaseFragment<VIEWMODEL_TO_VIEW, *>>
}

inline val Screen<*, *, *, *>.screenTag: String
  get() {
    return this::class.java.canonicalName?.takeIf { name -> name.isNotBlank() }
      ?: error("canonicalName is blank !")
  }

fun <PARAM : Any, EVENT : ScreenEvent> Screen<PARAM, EVENT, *, ViewModelToFlowInterface<PARAM, EVENT>>.flowDestination(
  param: PARAM
) = FlowDestination(
  screen = this,
  param = param
)