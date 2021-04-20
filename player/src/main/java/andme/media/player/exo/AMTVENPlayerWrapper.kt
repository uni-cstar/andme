package andme.media.player.exo

import andme.media.player.R
import andme.media.player.widget.ENPlayView
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * Created by Lucio on 2021/4/16.
 */
class AMTVENPlayerWrapper @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var playOrPauseView:ENPlayView? = null

    init {
        isFocusable = false
        isFocusableInTouchMode = false
        isClickable = false
    }

    fun bindView(view:ENPlayView){
        playOrPauseView = view
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        ( parent as? ViewGroup)?.findViewById<ENPlayView>(R.id.playOrPause)?.let {
            bindView(it)
        }
    }

    override fun setVisibility(visibility: Int) {
        if(visibility == View.VISIBLE){
            if(id == R.id.exo_play){
                playOrPauseView?.pause()
            }else{
                playOrPauseView?.play()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(0, 0)
    }
}

