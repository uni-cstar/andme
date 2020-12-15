package andme.core.widget.coordinatorlayout

import andme.core.R
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.annotation.FloatRange
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

/**
 * Created by Lucio on 2020-08-31.
 * [CoordinatorLayout]与[AppBarLayout]的联动效果
 *
 * @param appBarLayout
 */
open class CoordinatorEffect(
        val appBarLayout: AppBarLayout,
        /**
         * 是否启用指定范围内的滑动监听：即如果滑动的偏移的距离与顶部的具体在指定的范围（topOffsetDistanceLimitScale * totalScrollRange），则触发回调
         */
        val enableTopOffsetDistanceLimit: Boolean = false,

        @FloatRange(from = 0.0, to = 1.0)
        val topOffsetDistanceLimitScale: Float = 0.2f
) :
        AppBarLayout.OnOffsetChangedListener {

    fun createTransOutAnim(): Animation {
        return TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f
        ).apply {
            duration = 200
        }
    }

    fun createTransInAnim(): Animation {
        return TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        ).apply {
            duration = 200
        }
    }

    /**
     * 距离顶部距离的临界限制
     */
    protected var topOffsetDistanceLimit: Int = 0

    init {
        appBarLayout.addOnOffsetChangedListener(this)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        //总共滚动的高度
        val scrollRange = appBarLayout.totalScrollRange
        val offsetAbs = abs(verticalOffset)

        if (enableTopOffsetDistanceLimit) {
            topOffsetDistanceLimit = (scrollRange * topOffsetDistanceLimitScale).toInt()
            val limitScale =
                    (topOffsetDistanceLimit - (scrollRange - offsetAbs)) / topOffsetDistanceLimit.toFloat()
            if (scrollRange - offsetAbs < topOffsetDistanceLimit) {
                //处于临界值中
                inAppBarOffsetLimit(limitScale)
            } else {
                //临界值之外
                outAppBarOffsetLimit(limitScale)
            }
        }

        var scale = offsetAbs * 1.0f / scrollRange
        if (scale > 0.99) {
            scale = 1f
        }
        onAppBarScrollScale(scale)

        if (offsetAbs == scrollRange) {
            //appbar已经收拢
            onAppBarCollapsed()
        } else {
            onAppBarScrolling()
        }
    }

    protected open fun outAppBarOffsetLimit(scale: Float) {

    }

    protected open fun inAppBarOffsetLimit(scale: Float) {

    }

    /**
     * appbar在滑动中
     */
    protected open fun onAppBarScrolling() {

    }

    /**
     * appBar已经收拢
     */
    protected open fun onAppBarCollapsed() {

    }

    /**
     * appbar 滑动的缩放比 1 -完全收拢 0-完全展开
     */
    protected open fun onAppBarScrollScale(scale: Float) {

    }
}

/**
 * [CoordinatorLayout]与[AppBarLayout]联动效果
 */
open class CoordinatorAppBarLayoutEffect(
        appBarLayout: AppBarLayout,
        scrollLimitOffsetScale: Float = 0.5f
) : CoordinatorEffect(appBarLayout, true, scrollLimitOffsetScale) {

    /**
     * 标题栏显示时显示的Widget
     */
    private val titleBarWidgets: MutableList<View> = mutableListOf()

    /**
     * 与标题栏控件动作相反的控件
     */
    private val titleBarReverseWidgets: MutableList<View> = mutableListOf()

    /**
     * 标题栏进行平移动作的控件
     */
    private val titleBarTranslateWidgets: MutableList<View> = mutableListOf()

    /**
     * 添加在标题栏中显示的控件
     */
    fun addTitleBarWidget(vararg widgets: View): CoordinatorAppBarLayoutEffect {
        titleBarWidgets.addAll(widgets)
        return this
    }

    /**
     * 添加标题栏中与标题栏控件效果相反的控件
     */
    fun addTitleBarReverseWidget(vararg widgets: View): CoordinatorAppBarLayoutEffect {
        titleBarReverseWidgets.addAll(widgets)
        return this
    }

    fun addTitleBarTranslateWidget(vararg widgets: View): CoordinatorAppBarLayoutEffect {
        titleBarTranslateWidgets.addAll(widgets)
        return this
    }

    /**
     * 在指定的临界值之外
     */
    override fun outAppBarOffsetLimit(scale: Float) {
        super.outAppBarOffsetLimit(scale)
        titleBarWidgets.forEach {
            if (it.alpha != 0f) {
                it.alpha = 0f
            }
        }

        titleBarReverseWidgets.forEach {
            if (it.alpha != 1f) {
                it.alpha = 1f
            }
        }

        titleBarTranslateWidgets.forEach {
            if (it.visibility != View.INVISIBLE) {
                var outAnim = (it.getTag(R.id.am_id_extra_coordinator_effect_out_anim) as? Animation)
                if (outAnim == null) {
                    outAnim = createTransOutAnim()
                    it.setTag(R.id.am_id_extra_coordinator_effect_out_anim, outAnim)
                }
                outAnim.reset()
                it.clearAnimation()
                it.startAnimation(outAnim)
                it.visibility = View.INVISIBLE
            }
        }
    }

    /**
     * 在指定的临界值内滑动
     */
    override fun inAppBarOffsetLimit(scale: Float) {
        super.inAppBarOffsetLimit(scale)
        println("inAppBarOffsetLimit ${scale}")
        titleBarWidgets.forEach {
            it.alpha = scale
        }

        titleBarTranslateWidgets.forEach {
            if (it.visibility != View.VISIBLE) {
                it.visibility = View.VISIBLE
                var inAnim = (it.getTag(R.id.am_id_extra_coordinator_effect_in_anim) as? Animation)
                if (inAnim == null) {
                    inAnim = createTransInAnim()
                    it.setTag(R.id.am_id_extra_coordinator_effect_in_anim, inAnim)
                }
                inAnim.reset()
                it.clearAnimation()
                it.startAnimation(inAnim)
            }
        }
    }

    /**
     * [AppBarLayout]已经闭合
     * 闭合时，将TitleBar上要显示的控件显示出来
     */
    override fun onAppBarCollapsed() {
        super.onAppBarCollapsed()
        titleBarWidgets.forEach {
            if (it.alpha != 1f)
                it.alpha = 1f
        }
    }
}