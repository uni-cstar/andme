package andme.arch.widget.vp

import androidx.viewpager.widget.ViewPager

/**
 * Created by Lucio on 2020/11/24.
 */

fun interface OnPageChangeListenerAM : ViewPager.OnPageChangeListener {
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }


    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageSelected(position: Int)
}