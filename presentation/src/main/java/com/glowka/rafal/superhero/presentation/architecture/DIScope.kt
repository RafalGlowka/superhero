package com.glowka.rafal.superhero.presentation.architecture

import com.glowka.rafal.superhero.domain.utils.logE
import org.koin.core.context.GlobalContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.ScopeDSL

/**
 * Created by Rafal on 14.04.2021.
 *
 * Business flow abstraction. Business flow contains few screens and a separate DI scope.
 *
 */

class DIScope(
  val scopeName: String,
) {

  fun createScope(): Scope {
    return GlobalContext.get().getOrCreateScope(scopeName, named(scopeName))
  }

  fun closeScope() {
    val scope = GlobalContext.get().getScopeOrNull(scopeName)
    if (scope == null) {
      logE("scope $scopeName do not exist !")
      return
    }
    scope.close()
  }

}

fun Module.scope(flowScope: DIScope, scopeSet: ScopeDSL.() -> Unit) =
  scope(named(flowScope.scopeName), scopeSet)