package andme.media.player.widget

import andme.core.widget.setGone
import andme.core.widget.setVisible
import andme.media.player.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView

/**
 * Created by Lucio on 2021/4/16.
 * 播放结束View：播放完成/播放错误
 */
class PlayerEndView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    val endImage: AppCompatImageView
    val endText: TextView

    init {
        inflate(context, R.layout.am_player_end_view, this)
        gravity = Gravity.CENTER
        orientation = LinearLayout.VERTICAL
        endImage = findViewById(R.id.end_image)
        endText = findViewById(R.id.end_tv)
    }

    @SuppressLint("RestrictedApi")
    fun setImageColor(@ColorInt color: Int) {
        endImage.supportImageTintList = ColorStateList.valueOf(color)
    }

    fun setImage(@DrawableRes res:Int){
        endImage.setImageResource(res)
    }

    fun setText(text:CharSequence?){
        endText.text = text
    }


    fun show(text:CharSequence? = endText.text){
        setText(text)
        setVisible()
    }

    fun hide(){
        setGone()
    }


}