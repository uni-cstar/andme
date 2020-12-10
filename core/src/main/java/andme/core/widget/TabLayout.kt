package andme.core.widget

import com.google.android.material.tabs.TabLayout

/**
 * Created by Lucio on 2020/12/9.
 */

fun OnTabSelectedListener(callback: (TabLayout.Tab) -> Unit): TabLayout.OnTabSelectedListener {
    return object : TabLayout.OnTabSelectedListener {

        override fun onTabSelected(tab: TabLayout.Tab) {
            callback.invoke(tab)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
        }
    }
}