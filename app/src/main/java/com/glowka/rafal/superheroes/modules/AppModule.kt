package com.glowka.rafal.superheroes.modules

import com.glowka.rafal.superhero.domain.utils.StringResolver
import com.glowka.rafal.superhero.presentation.architecture.FragmentNavigator
import com.glowka.rafal.superhero.presentation.architecture.FragmentNavigatorAttachment
import com.glowka.rafal.superhero.presentation.architecture.FragmentNavigatorImpl
import com.glowka.rafal.superhero.presentation.utils.StringResolverImpl
import com.glowka.rafal.superheroes.R
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.binds
import org.koin.dsl.module

/**
 * Created by Rafal on 13.04.2021.
 */
val appModule = module {

  single {
    FragmentNavigatorImpl(containerId = R.id.fragment_container)
  } binds arrayOf(FragmentNavigatorAttachment::class, FragmentNavigator::class)

  single<StringResolver> {
    StringResolverImpl(context = androidContext())
  }
}