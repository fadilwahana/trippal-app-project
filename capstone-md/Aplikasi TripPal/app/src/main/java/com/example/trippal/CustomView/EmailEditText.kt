package com.example.trippal.CustomView

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.trippal.R

class EmailEditText: AppCompatEditText, View.OnTouchListener {



    constructor(context: Context): super(context){ init() }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs){ init() }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr){ init() }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }

    private fun init(){

        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(editable: Editable?) {
                val email = editable.toString()
                val validEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                if(validEmail){
                    error = null
                }
                else{
                    error = "invalid email format"
                }
            }

        })
    }

}