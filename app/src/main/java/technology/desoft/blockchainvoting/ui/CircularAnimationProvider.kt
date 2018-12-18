package technology.desoft.blockchainvoting.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.View
import android.view.ViewAnimationUtils
import technology.desoft.blockchainvoting.R
import kotlin.math.hypot

object CircularAnimationProvider {
    class Settings(val centerX: Int, val centerY: Int, val width: Int, val height: Int)

    fun startBackgroundAnimation(view: View, startColor: Int, endColor: Int, duration: Int) {
        ValueAnimator().apply {
            setIntValues(startColor, endColor)
            setEvaluator(ArgbEvaluator())
            addUpdateListener { view.setBackgroundColor(animatedValue as Int) }
            setDuration(duration.toLong())
        }.start()
    }

    fun startExitAnimation(
        context: Context,
        view: View,
        settings: Settings,
        startColor: Int,
        endColor: Int,
        onEnd: () -> Unit
    ) {
        val cx = settings.centerX
        val cy = settings.centerY
        val width = settings.width
        val height = settings.height
        val duration = context.resources.getInteger(R.integer.circular_animation_duration)
        val radius = hypot(width.toFloat(), height.toFloat())
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, radius, 0f)
        anim.apply {
            setDuration(duration.toLong())
            interpolator = FastOutSlowInInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    view.visibility = View.GONE
                    onEnd()
                }
            })
        }
        anim.start()
    }

    interface Dismissible {
        fun dismiss(onEnd: () -> Unit)
    }
}