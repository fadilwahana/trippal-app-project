package com.example.trippal.CustomView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.method.TransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.trippal.R

class PasswordEditText:  AppCompatEditText, View.OnTouchListener{

    private var isPasswordVisible = false

    constructor( context: Context): super(context){ init() }
    constructor( context: Context, attrs: AttributeSet): super(context, attrs){ init() }
    constructor( context: Context, attrs: AttributeSet, defStyleAttrs: Int): super(context, attrs, defStyleAttrs){ init() }


    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val transformationMethod: TransformationMethod =
            if (isPasswordVisible){ HideReturnsTransformationMethod.getInstance() }
            else{
                PasswordTransformationMethod.getInstance()
            }
        transformationMethod.let { TransformationMethod->
            setTransformationMethod(TransformationMethod)
        }
    }

    private fun init(){

        addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(cs: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val passLength = cs?.length ?:0
                error =
                    if (passLength < 6){"password must greater than 6 character"}
                    else{ null }
            }

            override fun afterTextChanged(editable: Editable?) {}
        })

    }
}