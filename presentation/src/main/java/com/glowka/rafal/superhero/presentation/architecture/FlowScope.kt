package com.glowka.rafal.superhero.presentation.architecture

import com.glowka.rafal.superhero.domain.utils.logE
import io.reactivex.Single
import org.koin.core.component.KoinComponent
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.ScopeDSL

/**
 * Created by Rafal on 14.04.2021.
 *
 * Business flow abstraction. Business flow contains few screens and a separate DI scope.
 *
 */

abstract class FlowScope<FLOW_PARAM, FLOW_RESULT : Any>(
  val scopeName: String,
) : KoinComponent {

  companion object {
    const val FLOW_INSTANCE = "flow"
  }

  private fun getFlowInstance(): FlowInstance<FLOW_PARAM, FLOW_RESULT> {
    val qualifier = StringQualifier(FLOW_INSTANCE)
    val scope = createScope()
    return scope.get<FlowInstance<*, *>>(qualifier = qualifier) as? FlowInstance<FLOW_PARAM, FLOW_RESULT>
      ?: throw IllegalStateException("Missing flow instance in the scope $scopeName")
  }

  fun createScope(): Scope {
    return getKoin().getOrCreateScope(scopeName, named(scopeName))
  }

  fun closeScope() {
    val scope = getKoin().getScopeOrNull(scopeName)
    if (scope == null) {
      logE("scope $scopeName do not exist !")
      return
    }
    scope.close()
  }

  fun start(
    param: FLOW_PARAM,
  ): Single<FLOW_RESULT> {
    val flowInstance = getFlowInstance()
    return flowInstance.start(param = param)
  }

}

fun Module.scope(scene: FlowScope<*, *>, scopeSet: ScopeDSL.() -> Unit) =
  scope(named(scene.scopeName), scopeSet)