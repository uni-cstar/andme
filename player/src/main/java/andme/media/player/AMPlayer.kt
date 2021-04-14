package andme.media.player

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Created by Lucio on 2021/4/13.
 */
class AMPlayer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        R.layout.exo_styled_player_control_view
    }

}