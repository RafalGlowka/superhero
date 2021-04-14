package com.glowka.rafal.superheroes.application

import android.app.Application
import com.glowka.rafal.superhero.domain.utils.logD
import com.glowka.rafal.superheroes.modules.modulesList
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Created by Rafal on 13.04.2021.
 */
class HeroApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    initDI()
  }

  private fun initDI() {
    startKoin {
      androidLogger()
      androidContext(this@HeroApplication)
      modules(modulesList)
      createEagerInstances()
    }
    logD("Koin initialized")
  }
}