package andme.tv.leanback.widget

import andme.tv.arch.R
import android.animation.TimeAnimator
import android.animation.TimeAnimator.TimeListener
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.IntDef
import androidx.leanback.graphics.ColorOverlayDimmer
import androidx.leanback.widget.*
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Lucio on 2021/2/27.
 * 焦点帮助类
 * 复刻[androidx.leanback.widget.FocusHighlightHelper]
 */
class ScaleFocusHighlightHelper {

    @IntDef(
        ZoomFactor.ZOOM_FACTOR_NONE,
        ZoomFactor.ZOOM_FACTOR_XXSMALL,
        ZoomFactor.ZOOM_FACTOR_XSMALL,
        ZoomFactor.ZOOM_FACTOR_SMALL,
        ZoomFactor.ZOOM_FACTOR_MEDIUM,
        ZoomFactor.ZOOM_FACTOR_LARGE
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class ZoomIndex

    companion object {

        const val DEFAULT_ANIM_DURATION_MS = 150

        @JvmStatic
        internal fun isValidZoomIndex(@ZoomIndex zoomIndex: Int): Boolean {
            return zoomIndex == FocusHighlight.ZOOM_FACTOR_NONE || getResId(zoomIndex) > 0
        }

        @JvmStatic
        internal fun getResId(zoomIndex: Int): Int {
            return when (zoomIndex) {
                ZoomFactor.ZOOM_FACTOR_SMALL -> R.fraction.lb_focus_zoom_factor_small
                ZoomFactor.ZOOM_FACTOR_XSMALL -> R.fraction.lb_focus_zoom_factor_xsmall
                ZoomFactor.ZOOM_FACTOR_XXSMALL -> R.fraction.amtv_focus_zoom_factor_xxsmall
                ZoomFactor.ZOOM_FACTOR_MEDIUM -> R.fraction.lb_focus_zoom_factor_medium
                ZoomFactor.ZOOM_FACTOR_LARGE -> R.fraction.lb_focus_zoom_factor_large
                else -> 0
            }
        }

        /**
         * @param useDimmer Allow dimming browse item when unselected.未选中时是否将内容调按
         */
        @JvmStatic
        fun createScaleFocusHandler(
            @ZoomIndex zoomIndex: Int,
            useDimmer: Boolean
        ): FocusHighlightHandler {
            return BrowseItemFocusHighlight(zoomIndex, useDimmer)
        }

        @JvmStatic
        fun setupViewFocusHighlight(view: View) {
            setupViewFocusHighlight(view, ZoomFactor.ZOOM_FACTOR_XSMALL, false)
        }

        @JvmStatic
        fun setupViewFocusHighlight(
            view: View,
            @ZoomIndex zoomIndex: Int,
            useDimmer: Boolean
        ) {
            view.onFocusChangeListener = object : View.OnFocusChangeListener {
                val mFocusHandler: FocusHighlightHandler =
                    createScaleFocusHandler(zoomIndex, useDimmer)

                override fun onFocusChange(v: View?, hasFocus: Boolean) {
                    v?.let {
                        mFocusHandler.onItemFocused(v, hasFocus)
                    }
                }
            }
        }

        @JvmStatic
        fun setupHeaderItemViewFocusHighlight(view: View) {
            setupHeaderItemViewFocusHighlight(view, true)
        }

        @JvmStatic
        fun setupHeaderItemViewFocusHighlight(
            view: View,
            scaleEnabled: Boolean
        ) {
            view.onFocusChangeListener = object : View.OnFocusChangeListener {
                val mFocusHandler: FocusHighlightHandler = HeaderItemFocusHighlight(scaleEnabled)
                override fun onFocusChange(v: View?, hasFocus: Boolean) {
                    v?.let {
                        mFocusHandler.onItemFocused(v, hasFocus)
                    }
                }
            }
        }
    }


    internal open class FocusAnimator(
        private val mView: View,
        scale: Float,
        useDimmer: Boolean,
        private val mDuration: Int
    ) :
        TimeListener {
        private var mWrapper: ShadowOverlayContainer? = null
        private val mScaleDiff: Float
        private var mFocusLevel = 0f
        private var mFocusLevelStart = 0f
        private var mFocusLevelDelta = 0f
        private val mAnimator = TimeAnimator()
        private val mInterpolator: Interpolator = AccelerateDecelerateInterpolator()
        private var mDimmer: ColorOverlayDimmer? = null

        init {
            mScaleDiff = scale - 1f
            mWrapper = if (mView is ShadowOverlayContainer) {
                mView
            } else {
                null
            }
            mAnimator.setTimeListener(this)
            mDimmer = if (useDimmer) {
                ColorOverlayDimmer.createDefault(mView.context)
            } else {
                null
            }
        }

        internal fun animateFocus(select: Boolean, immediate: Boolean) {
            endAnimation()
            val end: Float = if (select) 1f else 0f
            if (immediate) {
                focusLevel = end
            } else if (mFocusLevel != end) {
                mFocusLevelStart = mFocusLevel
                mFocusLevelDelta = end - mFocusLevelStart
                mAnimator.start()
            }
        }

        open var focusLevel: Float
            get() = mFocusLevel
            set(level) {
                mFocusLevel = level
                val scale = 1f + mScaleDiff * level
                mView.scaleX = scale
                mView.scaleY = scale
                if (mWrapper != null) {
                    mWrapper!!.setShadowFocusLevel(level)
                } else {
                    ShadowOverlayHelper.setNoneWrapperShadowFocusLevel(mView, level)
                }

                mDimmer?.let {
                    it.setActiveLevel(level)
                    val color = it.getPaint().color
                    if (mWrapper != null) {
                        mWrapper!!.setOverlayColor(color)
                    } else {
                        ShadowOverlayHelper.setNoneWrapperOverlayColor(mView, color)
                    }
                }
            }

        internal fun endAnimation() {
            mAnimator.end()
        }

        override fun onTimeUpdate(animation: TimeAnimator, totalTime: Long, deltaTime: Long) {
            var fraction: Float
            if (totalTime >= mDuration) {
                fraction = 1f
                mAnimator.end()
            } else {
                fraction = (totalTime / mDuration.toDouble()).toFloat()
            }
            fraction = mInterpolator.getInterpolation(fraction)
            focusLevel = mFocusLevelStart + fraction * mFocusLevelDelta
        }
    }

    internal class BrowseItemFocusHighlight(@ZoomIndex zoomIndex: Int, useDimmer: Boolean) :
        FocusHighlightHandler {

        private val mScaleIndex: Int
        private val mUseDimmer: Boolean

        private fun getScale(res: Resources): Float {
            return if (mScaleIndex == ZoomFactor.ZOOM_FACTOR_NONE) 1f else res.getFraction(
                getResId(mScaleIndex), 1, 1
            )
        }

        override fun onItemFocused(view: View, hasFocus: Boolean) {
            view.isSelected = hasFocus
            getOrCreateAnimator(view).animateFocus(hasFocus, false)
        }

        override fun onInitializeView(view: View) {
            getOrCreateAnimator(view).animateFocus(false, true)
        }

        private fun getOrCreateAnimator(view: View): FocusAnimator {
            var animator = view.getTag(R.id.lb_focus_animator) as? FocusAnimator
            if (animator == null) {
                animator = FocusAnimator(
                    view, getScale(view.resources), mUseDimmer, DEFAULT_ANIM_DURATION_MS
                )
                view.setTag(R.id.lb_focus_animator, animator)
            }
            return animator
        }

        init {
            require(isValidZoomIndex(zoomIndex)) { "Unhandled zoom index" }
            mScaleIndex = zoomIndex
            mUseDimmer = useDimmer
        }
    }


    internal class HeaderItemFocusHighlight(var mScaleEnabled: Boolean) : FocusHighlightHandler {
        private var mInitialized = false
        private var mSelectScale = 0f
        private var mDuration = 0

        fun lazyInit(view: View) {
            if (!mInitialized) {
                val res = view.resources
                val value = TypedValue()
                mSelectScale = if (mScaleEnabled) {
                    res.getValue(R.dimen.lb_browse_header_select_scale, value, true)
                    value.float
                } else {
                    1f
                }
                res.getValue(R.dimen.lb_browse_header_select_duration, value, true)
                mDuration = value.data
                mInitialized = true
            }
        }

        internal class HeaderFocusAnimator(view: View, scale: Float, duration: Int) :
            FocusAnimator(view, scale, false, duration) {

            var mViewHolder: ItemBridgeAdapter.ViewHolder? = null

            override var focusLevel: Float
                get() = super.focusLevel
                set(value) {
                    val presenter = mViewHolder!!.presenter
                    if (presenter is RowHeaderPresenter) {
                        presenter.setSelectLevel(
                            mViewHolder!!.viewHolder as RowHeaderPresenter.ViewHolder, value
                        )
                    }
                    super.focusLevel = value
                }


            init {
                var parent = view.parent
                while (parent != null) {
                    if (parent is RecyclerView) {
                        break
                    }
                    parent = parent.parent
                }
                if (parent != null) {
                    mViewHolder = (parent as RecyclerView)
                        .getChildViewHolder(view) as ItemBridgeAdapter.ViewHolder
                }
            }
        }

        private fun viewFocused(view: View, hasFocus: Boolean) {
            lazyInit(view)
            view.isSelected = hasFocus
            var animator = view.getTag(R.id.lb_focus_animator) as? FocusAnimator
            if (animator == null) {
                animator = HeaderFocusAnimator(view, mSelectScale, mDuration)
                view.setTag(R.id.lb_focus_animator, animator)
            }
            animator.animateFocus(hasFocus, false)
        }

        override fun onItemFocused(view: View, hasFocus: Boolean) {
            viewFocused(view, hasFocus)
        }

        override fun onInitializeView(view: View) {}
    }


}