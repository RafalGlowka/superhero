package com.glowka.rafal.superhero.presentation.architecture

import com.glowka.rafal.superhero.domain.utils.logE
import com.glowka.rafal.superhero.presentation.utils.DisposableHost
import com.glowka.rafal.superhero.presentation.utils.DisposableHostDelegate
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.SingleSubject
import org.koin.core.qualifier.StringQualifier

/**
 * Created by Rafal on 15.04.2021.
 */
data class FlowDestination<PARAM : Any, EVENT : ScreenEvent,
    VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>>(
  val screen: Screen<PARAM, EVENT, VIEWMODEL_TO_FLOW>,
  val param: PARAM
)

interface Flow<FLOW_PARAM, FLOW_RESULT : Any> {
  val flowScopeName: String
  fun start(navigator: ScreenNavigator, param: FLOW_PARAM): Single<FLOW_RESULT>
  fun finish(result: FLOW_RESULT)
}

abstract class BaseFlow<FLOW_PARAM, FLOW_RESULT : Any>(override val flowScopeName: String) :
  Flow<FLOW_PARAM, FLOW_RESULT>, DisposableHost by DisposableHostDelegate() {

  protected lateinit var navigator: ScreenNavigator

  private lateinit var resultSubject: SingleSubject<FLOW_RESULT>

  private var firstScreen: Screen<*, *, *>? = null

  abstract fun onStart(param: FLOW_PARAM): Screen<*, *, *>

  override fun start(navigator: ScreenNavigator, param: FLOW_PARAM): Single<FLOW_RESULT> {
    this.navigator = navigator
    resultSubject = SingleSubject.create()
    return resultSubject.doOnSubscribe {
      firstScreen = onStart(param = param)
    }.hide()
  }

  fun <PARAM : Any, EVENT : ScreenEvent, VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>> switchScreen(
    screen: Screen<PARAM, EVENT, VIEWMODEL_TO_FLOW>,
    param: PARAM,
    onEvent: (EVENT) -> Unit
  ) {
    val eventStream =
      initFlowDestination(flowDestination = FlowDestination(screen = screen, param = param))
    navigator.push(screen)
    eventStream.subscribeBy(
      onNext = onEvent,
      onError = { error -> logE("switchScreen", error) }
    ).disposedByHost()
  }

  fun switchBackTo(screen: Screen<*, *, *>) {
    navigator.popBackTo(screen = screen)
  }

  override fun finish(result: FLOW_RESULT) {
    disposeAll()
    resultSubject.onSuccess(result)
    closeScope()
  }
}

@Suppress("UNCHECKED_CAST")
fun <PARAM : Any, EVENT : ScreenEvent,
    VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>> BaseFlow<*, *>.initFlowDestination(
  flowDestination: FlowDestination<PARAM, EVENT, VIEWMODEL_TO_FLOW>
): Observable<EVENT> {
  val qualifier = StringQualifier(flowDestination.screen.screenTag)
  val scope = createScope()
  val viewModelToFlow =
    scope.get<ViewModelToFlowInterface<*, *>>(qualifier = qualifier) as? VIEWMODEL_TO_FLOW
  viewModelToFlow?.init(param = flowDestination.param)
    ?: throw IllegalStateException("Missing ${flowDestination.screen.screenTag} in the scope $flowScopeName")
  return viewModelToFlow.screenEvents
}

@Suppress("UNCHECKED_CAST")
fun <VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<out Any, out ScreenEvent>>
    BaseFlow<*, *>.getViewModelToFlow(
  screen: Screen<*, *, VIEWMODEL_TO_FLOW>
): VIEWMODEL_TO_FLOW {
  val qualifier = StringQualifier(screen.screenTag)
  val scope = createScope()
  return scope.get<ViewModelToFlowInterface<*, *>>(qualifier = qualifier) as? VIEWMODEL_TO_FLOW
    ?: throw IllegalStateException("Missing ${screen.screenTag} in the scope $flowScopeName")
}

