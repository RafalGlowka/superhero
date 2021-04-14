package com.glowka.rafal.superhero.presentation

import android.os.Bundle
import com.glowka.rafal.superhero.domain.utils.EmptyParam
import com.glowka.rafal.superhero.domain.utils.logE
import com.glowka.rafal.superhero.presentation.architecture.BaseActivity
import com.glowka.rafal.superhero.presentation.flow.intro.IntroFlow
import com.glowka.rafal.superhero.presentation.flow.intro.IntroResult
import com.glowka.rafal.superhero.presentation.utils.exhaustive
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy

class MainActivity : BaseActivity() {

  private val disposable = CompositeDisposable()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    IntroFlow.start(
      param = EmptyParam.EMPTY,
    ).subscribeBy(
      onSuccess = { result ->
        when (result) {
          IntroResult.Terminated -> finish()
        }.exhaustive
      },
      onError = { error -> logE("error", error) }
    ).addTo(disposable)
  }

  override fun onDestroy() {
    disposable.dispose()
    super.onDestroy()
  }
}