package com.leo.homeloan.util

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView


class CustomButton : AppCompatTextView {

    private val backgroundColor = Color.parseColor("#f07e2b")
    private val textColor = Color.WHITE
    private val cornerRadiusFactor = 2 * 8F

    companion object {
        internal var mTypeFace: Typeface? = null
    }

    constructor(context: Context) : super(context) {
        initTypeFace()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initTypeFace()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initTypeFace()
    }

    private fun initTypeFace() {
        setFont()
        setRectShape()
        setTextColor(textColor)
        gravity = Gravity.CENTER
    }

    private fun setFont(){
        if(mTypeFace == null)
            mTypeFace = Typeface.createFromAsset(context.applicationContext.assets, "fonts/ZURICH_CN_BT_0.TTF")

        typeface = mTypeFace
    }

    private fun setRectShape(){
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.cornerRadii = (floatArrayOf(cornerRadiusFactor, cornerRadiusFactor, cornerRadiusFactor, cornerRadiusFactor,
                cornerRadiusFactor, cornerRadiusFactor, cornerRadiusFactor, cornerRadiusFactor))
        gradientDrawable.setColor(backgroundColor)
        gradientDrawable.setStroke(2, backgroundColor)
        setBackgroundDrawable(gradientDrawable)
    }
}
