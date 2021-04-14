package com.glowka.rafal.superhero.presentation.architecture

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject

open class BaseActivity : AppCompatActivity() {

  private val fragmentNavigatorAttachment: FragmentNavigatorAttachment by inject()

  override fun onStart() {
    super.onStart()
    fragmentNavigatorAttachment.attach(supportFragmentManager)
  }

  override fun onStop() {
    fragmentNavigatorAttachment.detach()
    super.onStop()
  }

  override fun onBackPressed() {
    val currentFragment = supportFragmentManager.fragments.lastBaseFragment()
    if (currentFragment?.onBackPressed() != false) {
      return
    }
    finish()
  }
}

fun List<Fragment>.lastBaseFragment(): BaseFragment<*, *>? {
  return lastOrNull { fragment -> fragment is BaseFragment<*, *> } as BaseFragment<*, *>
}