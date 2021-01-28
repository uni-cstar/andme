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

package andme.core.text

import android.graphics.Paint
import androidx.annotation.ColorInt
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.widget.TextView

/**
 * 获取文本高度
 */
fun getTextHeight(textSize: Float): Int {
    val replyPaint = TextPaint()
    replyPaint.isAntiAlias = true
    replyPaint.textSize = textSize
    return replyPaint.textHeight() + 2
}

/**
 * 获取画笔绘制的文本高度
 */
fun TextPaint.textHeight(): Int {
    val fm = fontMetrics
    return Math.ceil((fm.descent - fm.ascent).toDouble()).toInt()
}

/**
 * 添加删除线
 */
fun TextView.applyDeleteLine() {
    this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

/**
 * 添加下划线
 */
fun TextView.applyUnderLine() {
    this.paintFlags = this.paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

/**
 * 文字加粗
 */
fun TextView.applyBold() {
    this.paintFlags = this.paintFlags or Paint.FAKE_BOLD_TEXT_FLAG
}


