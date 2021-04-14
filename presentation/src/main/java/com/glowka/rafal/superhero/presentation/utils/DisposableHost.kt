package com.glowka.rafal.superhero.presentation.utils

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo

/**
 * Created by Rafal on 17.04.2021.
 */
interface DisposableHost {
  fun disposeAll()
  fun Disposable.disposedByHost()
}

class DisposableHostDelegate : DisposableHost {
  private val compositeDisposable = CompositeDisposable()

  override fun disposeAll() {
    compositeDisposable.clear()
  }

  override fun Disposable.disposedByHost() {
    addTo(compositeDisposable = compositeDisposable)
  }

}