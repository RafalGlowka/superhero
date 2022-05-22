package com.glowka.rafal.superhero.presentation.architecture

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.glowka.rafal.superhero.presentation.utils.setLightTextColor
import com.glowka.rafal.superhero.presentation.utils.setStatusBarBackgroundColor

/**
 * Created by Rafal on 13.04.2021.
 */

interface ScreenNavigator {
  fun push(screen: Screen<*, *, *>)
  fun popBack(screen: Screen<*, *, *>)
  fun popBackTo(screen: Screen<*, *, *>)
}

interface FragmentNavigatorAttachment {
  fun attach(fm: FragmentActivity)
  fun detach()
}

class FragmentNavigatorImpl(val containerId: Int) : FragmentNavigatorAttachment,
  ScreenNavigator {

  private var fragmentActivity: FragmentActivity? = null
  private var waitingOperation: (() -> Unit)? = null

  override fun attach(fm: FragmentActivity) {
    fragmentActivity = fm
    waitingOperation?.invoke()
    waitingOperation = null
  }

  override fun detach() {
    fragmentActivity = null
  }

  override fun push(screen: Screen<*, *, *>) {
    val fragmentTag = screen.screenTag
    val fm = fragmentActivity
    if (fm == null) {
      waitingOperation = {
        push(screen = screen)
      }
    } else {
      if (screen.screenStructure.lightTextColor!=null) {
        fm.setLightTextColor(screen.screenStructure.lightTextColor)
      }
      if (screen.screenStructure.statusBarColor!=null) {
        fm.setStatusBarBackgroundColor(screen.screenStructure.statusBarColor)
      }
      fm.supportFragmentManager.commit {
        val arguments = Bundle().apply {
          putString(BaseFragment.ARG_SCOPE, screen.flowScopeName)
          putString(BaseFragment.ARG_SCREEN_TAG, fragmentTag)
        }
        replace(containerId, screen.screenStructure.fragmentClass.java, arguments, fragmentTag)
        addToBackStack(fragmentTag)
      }
    }
  }

  override fun popBack(screen: Screen<*, *, *>) {
    val fm = fragmentActivity
    if (fm == null) {
      waitingOperation = {
        popBack(screen = screen)
      }
    } else {
      fm.supportFragmentManager.popBackStack(screen.screenTag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
  }

  override fun popBackTo(screen: Screen<*, *, *>) {
    val fm = fragmentActivity
    if (fm == null) {
      waitingOperation = {
        popBackTo(screen = screen)
      }
    } else {
      fm.supportFragmentManager.popBackStack(screen.screenTag, 0)
    }
  }

}