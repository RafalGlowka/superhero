package com.glowka.rafal.superhero.presentation.architecture

import androidx.lifecycle.LifecycleOwner
import com.glowka.rafal.superhero.domain.utils.EmptyParam
import com.glowka.rafal.superhero.domain.utils.logE
import com.glowka.rafal.superhero.presentation.utils.DisposableHost
import com.glowka.rafal.superhero.presentation.utils.DisposableHostDelegate
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by Rafal on 13.04.2021.
 */

interface ViewModelToViewInterface {
  fun onBackPressed(): Boolean
}

interface ViewModelToFlowInterface<PARAM : Any, EVENT : ScreenEvent> {
  val screenEvents: Observable<EVENT>

  fun init(param: PARAM)

  fun clear()
}

interface ViewModelInterface<PARAM : Any, EVENT : ScreenEvent> :
  ViewModelToViewInterface,
  ViewModelToFlowInterface<PARAM, EVENT>

abstract class BaseViewModel<PARAM : Any, EVENT : ScreenEvent>(private val backPressedEvent: EVENT?) :
  ViewModelInterface<PARAM, EVENT>, DisposableHost by DisposableHostDelegate() {

  lateinit var lifecycleOwner: LifecycleOwner

  private val screenEventsSubject = PublishSubject.create<EVENT>()
  final override val screenEvents = screenEventsSubject.hide()

  override fun init(param: PARAM) {
    if (param !is EmptyParam) logE("Function init(param) should be overrided")
  }

  protected fun sendEvent(event: EVENT) {
    screenEventsSubject.onNext(event)
  }

  override fun onBackPressed(): Boolean {
    return backPressedEvent?.let { event ->
      sendEvent(event)
      true
    } ?: false
  }

  override fun clear() {
    disposeAll()
  }
}