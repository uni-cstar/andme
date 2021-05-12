package andme.media.player.widget

import andme.core.binding.bindTextOrGone
import andme.core.widget.setGone
import andme.core.widget.setVisible
import andme.media.player.R
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

/**
 * Created by Lucio on 2021/4/15.
 */
class PlayerBufferingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val titleText: TextView
    private val hintText: TextView
    private val loadingView: ProgressBar

    init {
        View.inflate(context, R.layout.am_player_buffering_view, this)
        titleText = findViewById<TextView>(R.id.am_buffering_title_tv)
        hintText = findViewById(R.id.am_buffering_hint_tv)
        loadingView = findViewById(R.id.am_buffering_loading)

        //设置默认背景
        if (background == null) {
            setBackgroundColor(ContextCompat.getColor(context, R.color.black_opacity_30))
        }
    }

    @JvmOverloads
    fun show(title: String? = null, hint: String? = null) {
        titleText.bindTextOrGone(title)
        hintText.bindTextOrGone(hint)
        setVisible()
    }

    fun hide() {
        setGone()
    }
}