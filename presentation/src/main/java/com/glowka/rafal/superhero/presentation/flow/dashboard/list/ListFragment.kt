package com.glowka.rafal.superhero.presentation.flow.dashboard.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.glowka.rafal.superhero.domain.utils.logD
import com.glowka.rafal.superhero.presentation.R
import com.glowka.rafal.superhero.presentation.architecture.BaseFragment
import com.glowka.rafal.superhero.presentation.databinding.ListFragmentBinding
import com.glowka.rafal.superhero.presentation.databinding.ListItemHeroBinding
import com.glowka.rafal.superhero.presentation.utils.BindingViewHolder
import com.glowka.rafal.superhero.presentation.utils.MarginItemDecorator
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.listeners.addClickListener

/**
 * Created by Rafal on 13.04.2021.
 */

class ListFragment : BaseFragment<ListViewModelToViewInterface, ListFragmentBinding>() {

  companion object {
    private const val COL_COUNT = 2
  }

  override val layoutResId = R.layout.list_fragment

  private val modelAdapter = ListModelAdapter()
  private val adapter = FastAdapter.with(modelAdapter)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewBinding.list.layoutManager = GridLayoutManager(context, COL_COUNT)
    viewBinding.list.adapter = adapter
    viewBinding.list.addItemDecoration(
      MarginItemDecorator(
        view.resources.getDimension(R.dimen.margin_small).toInt()
      )
    )

    adapter.addClickListener<BindingViewHolder<*>, GenericItem>(
      resolveView = { bindingViewHolder ->
        (bindingViewHolder.viewBinding as? ListItemHeroBinding)?.background
      },
      onClick = { _, _, _, item ->
        when (item) {
          is HeroModelItem -> viewModel.onHeroPick(item.model)
        }
      }
    )

    viewModel.items.observe(viewLifecycleOwner) { list ->
      logD("updating adapter ${list.size}")
      FastAdapterDiffUtil[modelAdapter] = modelAdapter.intercept(list)
    }
  }

}