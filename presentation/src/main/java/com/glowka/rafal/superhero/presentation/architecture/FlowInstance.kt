package com.glowka.rafal.superhero.presentation.architecture

import com.glowka.rafal.superhero.domain.utils.logE
import com.glowka.rafal.superhero.presentation.utils.DisposableHost
import com.glowka.rafal.superhero.presentation.utils.DisposableHostDelegate
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.SingleSubject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.StringQualifier

/**
 * Created by Rafal on 15.04.2021.
 */
data class FlowDestination<PARAM : Any, EVENT : ScreenEvent, VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>>(
  val screen: Screen<PARAM, EVENT, *, VIEWMODEL_TO_FLOW>,
  val param: PARAM
)

abstract class FlowInstance<FLOW_PARAM, FLOW_RESULT : Any>(
  val flowScope: FlowScope<FLOW_PARAM, FLOW_RESULT>
) : KoinComponent, DisposableHost by DisposableHostDelegate() {

  private val fragmentNavigator: FragmentNavigator by lazy {
    get()
  }

  private lateinit var resultSubject: SingleSubject<FLOW_RESULT>

  private var firstScreen: Screen<*, *, *, *>? = null

  abstract fun onStart(param: FLOW_PARAM): Screen<*, *, *, *>

  fun start(param: FLOW_PARAM): Single<FLOW_RESULT> {
    resultSubject = SingleSubject.create()
    return resultSubject.doOnSubscribe {
      firstScreen = onStart(param = param)
    }.hide()
  }

  fun <PARAM : Any, EVENT : ScreenEvent, VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>> switchScreen(
    screen: Screen<PARAM, EVENT, *, VIEWMODEL_TO_FLOW>,
    param: PARAM,
    onEvent: (EVENT) -> Unit
  ) {
    val eventStream =
      initFlowDestination(flowDestination = FlowDestination(screen = screen, param = param))
    fragmentNavigator.push(screen)
    eventStream.subscribeBy(
      onNext = onEvent,
      onError = { error -> logE("switchScreen", error) }
    ).disposedByHost()
  }

  fun switchBackTo(screen: Screen<*, *, *, *>) {
    fragmentNavigator.popBackTo(screen = screen)
  }

  fun <PARAM, RESULT : Any> startFlow(
    flowScope: FlowScope<PARAM, RESULT>,
    param: PARAM,
  ) = flowScope.start(param = param)

  fun finish(result: FLOW_RESULT) {
    disposeAll()
    resultSubject.onSuccess(result)
    flowScope.closeScope()
  }
}

inline fun <PARAM : Any, EVENT : ScreenEvent, VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>> FlowInstance<*, *>.initFlowDestination(
  flowDestination: FlowDestination<PARAM, EVENT, VIEWMODEL_TO_FLOW>
): Observable<EVENT> {
  val qualifier = StringQualifier(flowDestination.screen.screenTag)
  val scope = flowScope.createScope()
  val viewModelToFlow =
    scope.get<ViewModelToFlowInterface<*, *>>(qualifier = qualifier) as? VIEWMODEL_TO_FLOW
  viewModelToFlow?.init(param = flowDestination.param)
    ?: throw IllegalStateException("Missing ${flowDestination.screen.screenTag} in the scope ${flowScope.scopeName}")
  return viewModelToFlow.screenEvents
}

inline fun <VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<out Any, out ScreenEvent>> FlowInstance<*, *>.getViewModelToFlow(
  screen: Screen<*, *, *, VIEWMODEL_TO_FLOW>
): VIEWMODEL_TO_FLOW {
  val qualifier = StringQualifier(screen.screenTag)
  val scope = flowScope.createScope()
  return scope.get<ViewModelToFlowInterface<*, *>>(qualifier = qualifier) as? VIEWMODEL_TO_FLOW
    ?: throw IllegalStateException("Missing ${screen.screenTag} in the scope ${flowScope.scopeName}")
}

