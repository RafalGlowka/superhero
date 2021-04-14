package com.glowka.rafal.superhero.domain.usecase

import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by Rafal on 14.04.2021.
 */
interface SingleUseCase<PARAM, RESULT> {
  operator fun invoke(param: PARAM): Single<RESULT>
}

interface CompletableUseCase<PARAM> {
  operator fun invoke(param: PARAM): Completable
}