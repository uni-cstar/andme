package andme.core.sysui

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by Lucio on 2019/6/6.
 * 伪装状态栏
 */
class FakeStatusBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec))
    }

    private fun measureWidth(measureSpec: Int): Int {
        return getMeasureSize(
                Math.max(resources.displayMetrics.widthPixels, suggestedMinimumWidth)
                , measureSpec
                , "measureWidth")
    }

    private fun measureHeight(measureSpec: Int): Int {
        if (Build.VERSION.SDK_INT < 19) {
            //19之前，无法fake状态栏
            return 0
        } else {
            return getMeasureSize(
                    Math.max(getAvailableStatusBarHeight(context), suggestedMinimumHeight)
                    , measureSpec
                    , "measureHeight")
        }
    }

    private fun getMeasureSize(size: Int, measureSpec: Int, tag: String): Int {
        var result = 0
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)
        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
            log("$tag:[EXACTLY] width=$result")
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(size, specSize)
            log("$tag:[AT_MOST] width=$result")
        } else {
            result = size
            log("$tag:[UNSPECIFIED] width=$result")
        }
        return result
    }

    private fun log(msg: String) {
        Log.d(TAG, msg)
    }

    companion object {

        private const val TAG = "FakeSystemBar"

        @JvmStatic
        private var _availableStatusBarHeight = 0

        /**
         * 获取状态栏高度
         */
        @JvmStatic
        fun getStatusBarHeight(ctx: Context): Int {
            return ctx.applicationContext.resources.run {
                var result = 0
                val resourceId = this.getIdentifier("status_bar_height", "dimen", "android")
                if (resourceId > 0) {
                    result = this.getDimensionPixelSize(resourceId)
                }
                result
            }
        }

        @JvmStatic
        fun getAvailableStatusBarHeight(ctx: Context): Int {
            if (_availableStatusBarHeight == 0) {
                _availableStatusBarHeight = getStatusBarHeight(ctx)
                if (_availableStatusBarHeight == 0 && Build.VERSION.SDK_INT >= 19) {
                    //如果获取系统状态栏高度失败，并且设备系统大于19，则默认25dp
                    _availableStatusBarHeight = (ctx.applicationContext.resources.displayMetrics.density * 25).toInt()
                }
            }
            return _availableStatusBarHeight
        }

    }
}