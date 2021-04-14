package com.glowka.rafal.superhero.presentation.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Rafal on 18.04.2021.
 */
class MarginItemDecorator(
  private val margin: Int
) : RecyclerView.ItemDecoration() {

  override fun getItemOffsets(
    outRect: Rect,
    view: View,
    parent: RecyclerView,
    state: RecyclerView.State
  ) {
    outRect.bottom = margin
    outRect.top = margin
    outRect.left = margin
    outRect.right = margin
  }

}
