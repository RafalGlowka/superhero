package com.glowka.rafal.superhero.presentation.architecture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.glowka.rafal.superhero.presentation.BR
import org.koin.core.component.KoinComponent

/**
 * Created by Rafal on 13.04.2021.
 */
abstract class BaseFragment<VIEW_MODEL : ViewModelToViewInterface, VIEW_BINDING : ViewDataBinding> :
  Fragment() {
  protected val viewModel: VIEW_MODEL by injectViewModel()
  protected lateinit var viewBinding: VIEW_BINDING
  protected abstract val layoutResId: Int

  final override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    viewBinding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
    viewBinding.setVariable(BR.viewModel, viewModel)
    viewBinding.lifecycleOwner = viewLifecycleOwner
    return viewBinding.root
  }

  override fun onDestroy() {
    viewBinding.setVariable(BR.viewModel, null)
    viewBinding.lifecycleOwner = null
    super.onDestroy()
  }

  fun onBackPressed(): Boolean {
    return viewModel.onBackPressed()
  }

  companion object {
    const val ARG_SCOPE = "scope"
    const val ARG_SCREEN_TAG = "screenTag"
  }

}