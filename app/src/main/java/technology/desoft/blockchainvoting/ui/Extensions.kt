package technology.desoft.blockchainvoting.ui

import android.animation.ObjectAnimator
import android.support.v7.widget.CardView

fun CardView.changeElevation(startElevation: Float, endElevation: Float){
    val animator = ObjectAnimator.ofFloat(
        this,
        "cardElevation",
        startElevation,
        endElevation
    ).setDuration(200L)
    animator.addUpdateListener { this.cardElevation = it.animatedValue as Float }
    animator.start()
}