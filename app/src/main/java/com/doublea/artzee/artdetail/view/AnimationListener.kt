package com.doublea.artzee.artdetail.view

import android.animation.Animator

class AnimationListener(
    private val startAction: () -> Unit = {},
    private val endAction: () -> Unit = {}
) :
    Animator.AnimatorListener {
    override fun onAnimationStart(animation: Animator?) {
        startAction.invoke()
    }

    override fun onAnimationEnd(animation: Animator?) {
        endAction.invoke()
    }

    override fun onAnimationRepeat(animation: Animator?) {}
    override fun onAnimationCancel(animation: Animator?) {}
}