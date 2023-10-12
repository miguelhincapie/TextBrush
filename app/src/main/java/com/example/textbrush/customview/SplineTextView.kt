package com.example.textbrush.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2


class SplineTextView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var gestureDetector: GestureDetector
    private var xPos = 0f
    private var yPos = 0f
    private var previousX = 0f
    private var previousY = 0f
    private var deltaX = 0f
    private var deltaY = 0f
    private val bound = Rect()

    init {
        gestureDetector = GestureDetector(context, GestureListener())
        paint.isAntiAlias = true
        paint.strokeWidth = 6f
        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 30 * resources.displayMetrics.density
    }

    override fun onDraw(canvas: Canvas) {
        val text = "Hello World"
        paint.getTextBounds(text, 0, text.length, bound)

        if (xPos == 0f || yPos == 0f) {
            xPos = (width / 2f) - (bound.width() / 2)
            yPos = (height / 2 - (paint.descent() + paint.ascent()) / 2)
        }

        val splineText = getTextPoints(xPos, yPos, text, paint)
        splineText.points.forEach {
            canvas.rotate(it.angle, it.x, it.y)
            canvas.drawText(it.char.toString(), it.x, it.y, paint)
            canvas.rotate(-it.angle, it.x, it.y)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (gestureDetector.onTouchEvent(event)) return true

        val eventX = event.x
        val eventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                previousX = eventX
                previousY = eventY
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                deltaX = eventX - previousX
                deltaY = eventY - previousY
            }

            MotionEvent.ACTION_UP -> {}
            else -> return false
        }
        invalidate()
        return true
    }

    private fun getTextPoints(
        xTextStart: Float,
        yTextStart: Float,
        text: String,
        paint: Paint
    ): SplineText {
        var xStart = xTextStart
        val points = mutableListOf<TextPointF>()
        val binomialMultiplier = getBinomialDistribution(text.length)
        val charBound = Rect()
        val letterSpacing = (5f * resources.displayMetrics.density)
        for (i in text.indices) {
            points.add(TextPointF(xStart, yTextStart + (deltaY * binomialMultiplier[i]), text[i]))
            paint.getTextBounds(text[i].toString(), 0, 1, charBound)
            xStart += charBound.width() + letterSpacing
        }
        for (i in 0..points.size - 2) {
            // angle = atan2( y2 - y1, x2 - x1 ) * ( 180 / PI )
            points[i].angle = (atan2(
                points[i + 1].y - points[i].y,
                points[i + 1].x - points[i].x
            ) * (180 / Math.PI)).toFloat()
        }
        return SplineText(points)
    }

    /**
     * https://introcs.cs.princeton.edu/java/14array/BinomialDistribution.java
     */
    private fun getBinomialDistribution(n: Int): List<Float> {
        val binomial = arrayOfNulls<DoubleArray>(n + 1)
        binomial[1] = DoubleArray(1 + 2)
        binomial[1]!![1] = 1.0

        for (i in 2..n) {
            binomial[i] = DoubleArray(i + 2)
            for (k in 1 until binomial[i]!!.size - 1) binomial[i]!![k] = 0.5 * (binomial[i - 1]!![k - 1] + binomial[i - 1]!![k])
        }

        val result = mutableListOf<Float>()
        for (k in 1 until binomial[n]!!.size - 1) {
            result.add(binomial[n]!![k].toFloat() * 2)
        }

        return result
    }

    private fun drawPoints(points: Array<PointF>, canvas: Canvas, paint: Paint) {
        for (point in points) {
            canvas.drawCircle(point.x, point.y, 10f, paint)
        }
    }

    private class GestureListener : SimpleOnGestureListener() {
        override fun onLongPress(e: MotionEvent) {
            Log.d("asdf", "long press!!")
        }
    }
}

data class SplineText(val points: List<TextPointF>, val isSelected: Boolean = false)

class TextPointF(textX: Float, textY: Float, val char: Char, var angle: Float = 0f) :
    PointF(textX, textY)