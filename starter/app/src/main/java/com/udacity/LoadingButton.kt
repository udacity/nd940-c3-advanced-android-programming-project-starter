package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var cWidth = 0
    private var cHeight = 0
    private val buttonColor =
        ResourcesCompat.getColor(resources, R.color.colorPrimary, context.theme)
    private val loadingColor =
        ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, context.theme)
    private val loadingCircleColor =
        ResourcesCompat.getColor(resources, R.color.colorAccent, context.theme)
    private val loadingString = resources.getString(R.string.button_loading)
    private val buttonString = resources.getString(R.string.button_name)
    private val textColor = ResourcesCompat.getColor(resources, R.color.white, context.theme)
    private var buttonRect = Rect()
    private val loadingRect = Rect()
    private val loadingCircle = RectF()
    private lateinit var buttonText: String
    private var textHeight: Float = 0F

    // Current progress of the animation
    @Volatile
    private var progress = 0F
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        // As with the variables, these styles are initialized here to
        // help speed up the drawing step.
        style = Paint.Style.FILL
        color = buttonColor
    }
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = resources.getDimension(R.dimen.default_text_size)
        typeface = Typeface.create("", Typeface.BOLD)
    }
    private val paintLoading = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = loadingColor
    }
    private val paintLoadingCircle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = loadingCircleColor
    }
    private var valueAnimator = ValueAnimator()
    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { _, _, new ->
        when (new) {
            ButtonState.Clicked -> {}
            ButtonState.Loading -> {
                // Start Animation
                valueAnimator = ValueAnimator.ofFloat(0F,
                    1F)
                    .setDuration(2000)
                    .apply {
                        addUpdateListener {
                            progress = it.animatedValue as Float
                            repeatMode = ValueAnimator.RESTART
                            repeatCount = ValueAnimator.INFINITE
                            invalidate()
                        }
                    }
                valueAnimator.start()
            }
            // TODO Review after implementing button in MainActivity -> should stop animation
            ButtonState.Completed -> {
                // Cancel Animation
                valueAnimator.cancel()
                progress = 0F
                invalidate()
            }
        }
    }

    init {
        isClickable = true
        buttonState = ButtonState.Loading
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawRectangle(canvas)
        drawLoadingRectangle(canvas)
        drawButtonText(canvas)
        drawArc(canvas)
    }

    private fun drawRectangle(canvas: Canvas?) {
        buttonRect.set(0, 0, widthSize, heightSize)
        canvas?.clipRect(buttonRect.left, buttonRect.top, buttonRect.right, buttonRect.bottom)
        canvas?.drawRect(buttonRect, paint)
    }

    private fun drawLoadingRectangle(canvas: Canvas?) {
        loadingRect.set(buttonRect.left,
            buttonRect.top,
            ((widthSize * (progress)).toInt()),
            heightSize)
        canvas?.drawRect(loadingRect, paintLoading)
    }

    private fun drawButtonText(canvas: Canvas?) {
        buttonText = if (buttonState == ButtonState.Loading) {
            loadingString
        } else {
            buttonString
        }

        paintText.color = textColor
//        textHeight = paintText.fontMetrics.bottom - paintText.fontMetrics.top + paintText.fontMetrics.leading
        textHeight = paintText.fontMetrics.descent - paintText.fontMetrics.ascent
        canvas?.drawText(buttonText,
            (width / 2).toFloat(),
            (height + textHeight / 2) / 2,
            paintText)
    }

    private fun drawArc(canvas: Canvas?) {
        canvas?.getClipBounds(buttonRect)
        cHeight = buttonRect.height()
        cWidth = buttonRect.width()
        paintText.getTextBounds(loadingString, 0, loadingString.length, buttonRect)
        loadingCircle.set(
            (cWidth / 2F + buttonRect.width()) - buttonRect.height(),
            (cHeight / 2F) - buttonRect.height(),
            (cWidth / 2F + buttonRect.width()) + buttonRect.height(),
            (cHeight / 2F) + buttonRect.height()
        )

        canvas?.drawArc(loadingCircle,
            0F,
            progress * 360F,
            true,
            paintLoadingCircle)
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