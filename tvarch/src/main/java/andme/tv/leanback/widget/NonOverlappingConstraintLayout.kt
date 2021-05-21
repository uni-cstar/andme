package andme.tv.leanback.widget

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * Created by Lucio on 2021/5/20.
 */
class NonOverlappingConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    override fun hasOverlappingRendering(): Boolean {
        return false
    }
}