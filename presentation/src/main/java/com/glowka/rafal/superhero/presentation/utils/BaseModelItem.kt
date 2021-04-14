package com.glowka.rafal.superhero.presentation.utils

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.glowka.rafal.superhero.domain.utils.logD
import com.glowka.rafal.superhero.presentation.BR
import com.mikepenz.fastadapter.items.ModelAbstractItem

/**
 * Created by Rafal on 15.04.2021.
 */
open class BaseModelItem<MODEL, VIEWBINDING : ViewDataBinding>(
  model: MODEL,
  override val layoutRes: Int,
  override val type: Int
) : ModelAbstractItem<MODEL, BindingViewHolder<VIEWBINDING>>(model = model) {

  override var identifier = model.hashCode().toLong()

  override fun getViewHolder(v: View) = BindingViewHolder<VIEWBINDING>(v)

  override fun bindView(holder: BindingViewHolder<VIEWBINDING>, payloads: List<Any>) {
    super.bindView(holder, payloads)
    holder.viewBinding.setVariable(BR.itemModel, model)
    logD("binded")
  }
}

class BindingViewHolder<VIEWDINDING : ViewDataBinding>(itemView: View) :
  RecyclerView.ViewHolder(itemView) {
  val viewBinding: VIEWDINDING = DataBindingUtil.bind(itemView)!!
}