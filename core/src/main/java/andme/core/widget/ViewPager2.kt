package andme.core.widget

import android.view.ViewGroup
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

/**
 * Created by Lucio on 2021/4/30.
 */


val ViewPager2.recyclerView: RecyclerView
    get() {
        return this.findRecyclerView()!!
    }

private fun ViewGroup.findRecyclerView(): RecyclerView? {
    children.forEach {
        if (it is RecyclerView)
            return it
        if (it is ViewGroup) {
            val rv = it.findRecyclerView()
            if (rv != null)
                return rv
        }
    }
    return null
}