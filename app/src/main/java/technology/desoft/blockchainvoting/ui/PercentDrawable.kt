package technology.desoft.blockchainvoting.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import technology.desoft.blockchainvoting.R

class PercentDrawable(context: Context, private val percent: Int) : Drawable() {
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {

        val color = when {
            percent >= 100 -> getColor(context, R.color.colorGray80)
            percent >= 75 -> getColor(context, R.color.colorGray60)
            percent >= 50 -> getColor(context, R.color.colorGray40)
            percent >= 25 -> getColor(context, R.color.colorGray20)
            percent > 0 -> getColor(context, R.color.colorGray5)
            else -> getColor(context, android.R.color.background_light)
        }

        this.paint.color = color
    }

    private fun getColor(context: Context, @ColorRes color: Int) = ContextCompat.getColor(context, color)

    override fun draw(canvas: Canvas) {
        canvas.drawRect(
            0f,
            0f,
            (percent * bounds.width() / 100).toFloat(),
            bounds.height().toFloat(),
            paint
        )
    }

    override fun setAlpha(i: Int) {}

    override fun setColorFilter(colorFilter: ColorFilter?) {}

    override fun getOpacity() = PixelFormat.TRANSPARENT
}
