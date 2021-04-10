package com.open.androidtvwidget.keyboard

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

/**
 * Created by Lucio on 2021/4/8.
 */
class SoftKeyboardViewImpl @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SkbContainer(context, attrs, defStyleAttr) {

    private lateinit var mAttach:TextView

    fun attach(view:TextView){
        this.mAttach = view
    }
}