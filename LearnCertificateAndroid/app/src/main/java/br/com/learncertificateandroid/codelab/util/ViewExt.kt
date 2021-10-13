package br.com.learncertificateandroid.codelab.util

import android.view.View
import android.view.animation.Animation

fun View.startAnimation(anim: Animation, onEnd: () -> Unit) {
    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationEnd(animation: Animation?) = onEnd()
        override fun onAnimationStart(animation: Animation?) = Unit
        override fun onAnimationRepeat(animation: Animation?) = Unit

    })
}

fun View.setVisible(show: Boolean) {
    if (show) this.visibility = View.VISIBLE else this.visibility = View.GONE
}