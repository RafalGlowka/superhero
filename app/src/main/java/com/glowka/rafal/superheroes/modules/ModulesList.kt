package com.glowka.rafal.superheroes.modules

import com.glowka.rafal.superheroes.modules.scene.dashboardSceneModule
import com.glowka.rafal.superheroes.modules.scene.introSceneModule
import org.koin.core.module.Module

/**
 * Created by Rafal on 13.04.2021.
 */

val modulesList = listOf<Module>(
  appModule,
  remoteModule,
  heroModule,
  introSceneModule,
  dashboardSceneModule
)