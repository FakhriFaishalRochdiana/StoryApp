package com.zaniva.storyappv2.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.zaniva.storyappv2.R

class BTRegister : AppCompatButton {

    private lateinit var enabled: Drawable
    private lateinit var disabled: Drawable
    private var txtColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = if(isEnabled) enabled else disabled
        setTextColor(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER
        text = if(isEnabled) "Register" else "Requirement not met..."
    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enabled = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        disabled = ContextCompat.getDrawable(context, R.drawable.bg_button_disable) as Drawable
    }

}