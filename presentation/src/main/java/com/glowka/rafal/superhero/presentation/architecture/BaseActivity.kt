package com.glowka.rafal.superhero.presentation.architecture

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.glowka.rafal.superhero.presentation.R

open class BaseActivity : AppCompatActivity() {

  protected val navigator = FragmentNavigatorImpl(containerId = R.id.fragment_container)

  override fun onStart() {
    super.onStart()
    navigator.attach(supportFragmentManager)
  }

  override fun onStop() {
    navigator.detach()
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