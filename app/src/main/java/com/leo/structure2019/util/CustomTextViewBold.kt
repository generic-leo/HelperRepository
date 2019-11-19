package com.leo.homeloan.util

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomTextViewBold : AppCompatTextView {

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        // Load and Set Font
        initTypeFace()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        // Load and Set Font
        initTypeFace()
    }

    constructor(context: Context) : super(context) {

        // Load and Set Font
        initTypeFace()
    }

    private fun initTypeFace() {
        if (mTypeFace == null) {
            mTypeFace = Typeface.createFromAsset(context.applicationContext.assets, "fonts/ZURICH_BOLD_CONDENSED_BT_0.TTF")
        }

        typeface = mTypeFace
    }

    companion object {
        internal var mTypeFace: Typeface? = null
    }
}