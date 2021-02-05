package com.udacity.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.StringRes
import androidx.core.animation.doOnCancel
import androidx.core.content.withStyledAttributes
import com.udacity.R
import com.udacity.domain.ButtonState
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progress: Float = 0f

    private var sweepAngle: Float = 0f

    private var widthSize = 0

    private var heightSize = 0

    private var loadingAnimator = ValueAnimator()

    private var circleLoadingAnimator = ValueAnimator()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 40.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }

    private var textResId: Int = R.string.button_name

    private var buttonColor: Int = 0

    private var loadingColor: Int = 0

    var state: ButtonState by Delegates.observable(ButtonState.Completed) { _, _, new ->
        when (new) {
            is ButtonState.Clicked -> {
                isEnabled = false
                updateText(R.string.button_clicked)
            }
            is ButtonState.Loading -> {
                updateText(R.string.button_loading)
                updateContentDescriptor(R.string.button_loading)
                startLoading()
            }
            is ButtonState.Completed -> {
                isEnabled = true
                stopLoading()
                updateText(R.string.button_name)
                updateContentDescriptor(R.string.button_name)
            }
        }
        invalidate()
    }

    private fun stopLoading() {
        loadingAnimator.cancel()
        circleLoadingAnimator.cancel()
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            loadingColor = getColor(R.styleable.LoadingButton_loadingColor, 0)
            buttonColor = getColor(R.styleable.LoadingButton_backgroundColor, 0)
        }
        //loadingColor = ColorUtils.setAlphaComponent(buttonColor, 128)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawButton(canvas)
        drawLoading(canvas)
        drawCircle(canvas)
        drawText(canvas)
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return false
        state = ButtonState.Clicked
        return true
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

    private fun startLoading() {
        animateLoading()
        animateCircleLoading()
    }

    private fun animateCircleLoading() {
        circleLoadingAnimator.cancel()
        circleLoadingAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 1000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                sweepAngle = valueAnimator.animatedValue as Float
            }
            doOnCancel {
                sweepAngle = 0f
            }
        }
        circleLoadingAnimator.start()
    }

    private fun animateLoading() {
        loadingAnimator.cancel()
        loadingAnimator = ValueAnimator.ofFloat(0F, widthSize.toFloat()).apply {
            duration = 1000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                progress = valueAnimator.animatedValue as Float
                invalidate()
            }
            doOnCancel {
                progress = 0f
            }
        }
        loadingAnimator.start()
    }

    private fun updateText(@StringRes stringResId: Int) {
        textResId = stringResId
    }

    private fun updateContentDescriptor(@StringRes stringResId: Int) {
        contentDescription = resources.getString(stringResId)
    }

    private fun drawText(canvas: Canvas?) {
        canvas ?: return
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.CENTER
        val label = resources.getString(textResId)
        val textHeight: Float = paint.descent() - paint.ascent()
        val textOffset: Float = textHeight / 2 - paint.descent()
        val x = widthSize.toFloat() / 2
        val y = heightSize.toFloat() / 2 + textOffset
        canvas.drawText(label, x, y, paint)
    }

    private fun drawLoading(canvas: Canvas?) {
        paint.color = loadingColor
        canvas?.drawRect(
                0.0f,
                0.0f,
                progress,
                height.toFloat(),
                paint
        )
    }

    private fun drawButton(canvas: Canvas?) {
        paint.color = buttonColor
        canvas?.drawRect(
                0.0f,
                0.0f,
                width.toFloat(),
                height.toFloat(),
                paint
        )
    }

    private fun drawCircle(canvas: Canvas?) {
        paint.color = Color.YELLOW
        val left = widthSize - 70f
        val top = (heightSize / 2) - 20f
        val right = widthSize - 30f
        val bottom = (heightSize / 2) + 20f
        val startAngle = 0F
        val useCenter = true
        canvas?.drawArc(
                left,
                top,
                right,
                bottom,
                startAngle,
                sweepAngle,
                useCenter,
                paint
        )
    }

}