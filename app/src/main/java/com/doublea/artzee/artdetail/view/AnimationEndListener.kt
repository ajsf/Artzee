package com.doublea.artzee.artdetail.view

import android.animation.Animator

class AnimationEndListener(private val action: () -> Unit) : Animator.AnimatorListener {
    override fun onAnimationStart(animation: Animator?) {}
    override fun onAnimationRepeat(animation: Animator?) {}
    override fun onAnimationCancel(animation: Animator?) {}

    override fun onAnimationEnd(animation: Animator?) {
        action.invoke()
    }
}