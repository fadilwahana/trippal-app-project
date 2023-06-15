package com.example.trippal.CustomView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.trippal.R

class SubmitButton: AppCompatButton {

    private lateinit var enableButton: Drawable
    private lateinit var disableButton: Drawable

    constructor(context: Context): super(context){ init()}
    constructor(context: Context, attrs: AttributeSet): super(context, attrs){ init() }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs,defStyleAttr){ init() }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = if (isEnabled){ enableButton}
                     else { disableButton }

        gravity = Gravity.CENTER
    }

    private fun init(){
        enableButton = ContextCompat.getDrawable(context, R.drawable.button_enable) as Drawable
        disableButton = ContextCompat.getDrawable(context, R.drawable.button_dissable) as Drawable
    }

}