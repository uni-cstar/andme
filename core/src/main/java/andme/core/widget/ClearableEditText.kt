/*
 * Copyright (C) 2018 Lucio
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package andme.core.widget

import andme.core.R
import andme.lang.orDefault
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by Lucio on 2018/6/20.
 */

class ClearableEditText : AppCompatEditText {

    private var mClearDrawable: Drawable? = null
    private var mClearCallBack: (() -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        mClearDrawable = compoundDrawables[2]
        if (mClearDrawable == null) {
            val drawable = resources.getDrawable(R.drawable.ic_edit_text_clear_am)
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            mClearDrawable = drawable
        }
        // 默认隐藏图标
        setClearIconVisible(false)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        setClearIconVisible(focused && length() > 0)
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        setClearIconVisible(hasFocus() && text?.length.orDefault() > 0)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.run {
            if (event.action == MotionEvent.ACTION_DOWN) {
                val drawable = compoundDrawables[2]
                drawable?.let {
                    if (event.x <= (width - paddingRight) && event.x >= (width - paddingRight - drawable.bounds.width())) {
                        setText("")
                        mClearCallBack?.invoke()
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }


    fun setClearCallBack(callback: (() -> Unit)? = null) {
        mClearCallBack = callback
    }

    private fun setClearIconVisible(visible: Boolean) {
        val wasVisible = compoundDrawables[2] != null
        if (wasVisible == visible) return

        setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], compoundDrawables[1],
                if (visible) mClearDrawable else null, compoundDrawables[3])
    }

}