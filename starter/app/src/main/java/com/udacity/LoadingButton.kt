package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0
    private var buttonText: String = context.getString(R.string.label_download)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 50f
        color = context.getColor(R.color.white)
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private val progressBarPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.colorPrimaryDark)
    }
    private val circleProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = context.getColor(R.color.colorAccent)
    }

    private var valueAnimator = ValueAnimator()
    private var animateValue: Float = 0.0f
    private val rectF = RectF()

    // private var rectF = RectF(50f, 20f, 100f, 80f)

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        if (old != new) {
            when (new) {
                ButtonState.Loading -> {
                    buttonText = context.getString(R.string.button_loading)
                    valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f).apply {
                        addUpdateListener {
                            animateValue = it.animatedValue as Float
                            Log.d(VIEW_LOG_TAG, "animateValue $animateValue")
                            invalidate()
                        }
                        doOnEnd {
                            disableLoading()
                        }
                        duration = 1000
                        start()
                    }

                }
                ButtonState.Completed -> {
                    buttonText = context.getString(R.string.label_download)
                    valueAnimator.cancel()
                    invalidate()
                }
                ButtonState.Clicked -> {
                    buttonText = context.getString(R.string.label_download)
                    invalidate()
                }
            }
        }
    }

    fun startLoading() {
        buttonState = ButtonState.Loading
    }

    fun disableLoading() {
        buttonState = ButtonState.Completed
    }

    init {
        isClickable = true
        setBackgroundColor(context.getColor(R.color.colorPrimary))
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // text position
        val xPos = width / 2
        val yPos = (height / 2 - (paint.descent() + paint.ascent()) / 2)

        if (buttonState == ButtonState.Loading) {
            canvas?.drawRect(
                0f,
                0f,
                animateValue * widthSize.toFloat(),
                heightSize.toFloat(),
                progressBarPaint
            )
            val startArcX = xPos / 2f + width / 2f
            val startArcY = yPos / 2 + 15
            rectF.set(startArcX, startArcY, startArcX + 50, startArcY + 50)
            canvas?.drawArc(rectF, 0f, animateValue * 360f, true, circleProgressPaint)
        } else {
            canvas?.drawColor(context.getColor(R.color.colorPrimary))
        }

        // draw text
        canvas?.drawText(
            buttonText,
            xPos.toFloat(),
            yPos,
            paint
        )


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}