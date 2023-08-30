package com.glowka.rafal.superhero.presentation.binding

import android.animation.Animator
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.glowka.rafal.superhero.domain.utils.logD
import com.glowka.rafal.superhero.domain.utils.logE
import com.glowka.rafal.superhero.presentation.binding.fields.ProgressPosition
import com.google.android.material.progressindicator.LinearProgressIndicator

@BindingAdapter("onAnimationFinished")
fun LottieAnimationView.onAnimationFinished(listener: (() -> Unit)?) {
  if (listener == null) {
    logE("missing listener")
    return
  }

  addAnimatorListener(object : Animator.AnimatorListener {
    override fun onAnimationStart(p0: Animator) {
      logD("animStarted")
    }

    override fun onAnimationEnd(p0: Animator) {
      logD("animFinished")
      listener()
    }

    override fun onAnimationCancel(p0: Animator) {}
    override fun onAnimationRepeat(p0: Animator) {}
  })
}

class ImageModel(
  val url: String? = null,
  val fallbackImageResId: Int = 0,
  val placeholderResId: Int = 0
)

@BindingAdapter("loadImage")
fun ImageView.loadImage(image: ImageModel) {
  if (image.placeholderResId != 0) setImageResource(image.placeholderResId)

  image.url?.let { imageUrl ->
    val glide = Glide.with(this).load(imageUrl)
    if (image.fallbackImageResId != 0) glide.fallback(image.fallbackImageResId)
    glide.into(this)
  } ?: if (image.fallbackImageResId != 0) {
    setImageResource(image.fallbackImageResId)
  } else {
    Unit
  }
}

@BindingAdapter("progressModel")
fun LinearProgressIndicator.setProgressModel(
  progress: ProgressPosition
) {
  max = progress.maxPosition
  setProgressCompat(progress.position, true)
}

@BindingAdapter("visible")
fun View.setVisible(visible: Boolean) {
  visibility = if (visible) {
    View.VISIBLE
  } else {
    View.GONE
  }
}

@BindingAdapter("onClick")
fun View.setOnClick(onClickAction: () -> Unit) {
  setOnClickListener { onClickAction() }
}