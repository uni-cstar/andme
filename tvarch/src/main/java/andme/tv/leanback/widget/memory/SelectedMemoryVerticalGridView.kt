package andme.tv.leanback.widget.memory

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.leanback.widget.VerticalGridView
import java.util.*

/**
 * Created by Lucio on 2021/3/31.
 */
class SelectedMemoryVerticalGridView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : VerticalGridView(context, attrs, defStyleAttr) {


    @SuppressLint("RestrictedApi")
    override fun addFocusables(views: ArrayList<View>?, direction: Int, focusableMode: Int) {
        val focusStrategy = focusScrollStrategy
        if(focusStrategy != FOCUS_SCROLL_PAGE && focusStrategy != FOCUS_SCROLL_ITEM){
            invokeSuperAddFocusables(views, direction, focusableMode)
            return
        }

        if (SelectedMemoryHelper.addFocusables(this, views, direction, focusableMode))
            return
        invokeSuperAddFocusables(views, direction, focusableMode)
    }

    /**
     * 用于重写[addFocusables]但是无法调用super的情况
     */
    protected fun invokeSuperAddFocusables(views: ArrayList<View>?, direction: Int, focusableMode: Int){
        super.addFocusables(views, direction, focusableMode)
    }

}