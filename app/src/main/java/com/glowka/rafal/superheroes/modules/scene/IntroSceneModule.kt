package com.glowka.rafal.superheroes.modules.scene

import com.glowka.rafal.superhero.presentation.architecture.flowInstance
import com.glowka.rafal.superhero.presentation.architecture.scope
import com.glowka.rafal.superhero.presentation.architecture.screenViewModel
import com.glowka.rafal.superhero.presentation.flow.intro.IntroFlow
import com.glowka.rafal.superhero.presentation.flow.intro.IntroFlowImpl
import com.glowka.rafal.superhero.presentation.flow.intro.IntroFragment
import com.glowka.rafal.superhero.presentation.flow.intro.IntroViewModelImpl
import org.koin.androidx.fragment.dsl.fragment
import org.koin.dsl.module

/**
 * Created by Rafal on 14.04.2021.
 */
val introSceneModule = module {

  scope(IntroFlow) {
    flowInstance {
      IntroFlowImpl(flowScope = IntroFlow)
    }
    fragment { IntroFragment() }
    screenViewModel(screen = IntroFlow.Screens.Start) {
      IntroViewModelImpl(
        prepareCacheUseCase = get()
      )
    }
  }

}