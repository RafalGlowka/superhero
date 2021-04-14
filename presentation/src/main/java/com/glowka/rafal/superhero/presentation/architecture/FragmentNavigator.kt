package com.glowka.rafal.superhero.presentation.architecture

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

/**
 * Created by Rafal on 13.04.2021.
 */

interface FragmentNavigator {
  fun push(screen: Screen<*, *, *, *>)
  fun popBack(screen: Screen<*, *, *, *>)
  fun popBackTo(screen: Screen<*, *, *, *>)
}

interface FragmentNavigatorAttachment {
  fun attach(fm: FragmentManager)
  fun detach()
}

class FragmentNavigatorImpl(val containerId: Int) : FragmentNavigatorAttachment,
  FragmentNavigator {

  private var fragmentManager: FragmentManager? = null
  private var waitingOperation: (() -> Unit)? = null

  override fun attach(fm: FragmentManager) {
    fragmentManager = fm
    waitingOperation?.invoke()
    waitingOperation = null
  }

  override fun detach() {
    fragmentManager = null
  }

  override fun push(screen: Screen<*, *, *, *>) {
    val fragmentTag = screen.screenTag
    val fm = fragmentManager
    if (fm == null) {
      waitingOperation = {
        push(screen = screen)
      }
    } else {
      fm.commit {
        val arguments = Bundle().apply {
          putString(BaseFragment.ARG_SCOPE, screen.scope.scopeName)
          putString(BaseFragment.ARG_SCREEN_TAG, fragmentTag)
        }
        replace(containerId, screen.fragmentClass.java, arguments, fragmentTag)
        addToBackStack(fragmentTag)
      }
    }
  }

  override fun popBack(screen: Screen<*, *, *, *>) {
    val fm = fragmentManager
    if (fm == null) {
      waitingOperation = {
        popBack(screen = screen)
      }
    } else {
      fm.popBackStack(screen.screenTag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
  }

  override fun popBackTo(screen: Screen<*, *, *, *>) {
    val fm = fragmentManager
    if (fm == null) {
      waitingOperation = {
        popBackTo(screen = screen)
      }
    } else {
      fm.popBackStack(screen.screenTag, 0)
    }
  }

}