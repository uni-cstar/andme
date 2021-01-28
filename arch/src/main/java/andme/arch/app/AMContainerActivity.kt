package andme.arch.app

import andme.arch.R
import andme.core.ktx.applyImmersiveStatusBar
import andme.core.ktx.applyStatusBarColor
import andme.core.ktx.applyStatusBarLightMode
import andme.core.ktx.getThemeAttrValue
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat

/**
 * 用于装载Fragment的Activity容器，包含PigActivity所有功能特性
 *
 */
class AMContainerActivity : AMActivity<AMViewModel>() {

    private lateinit var mView: View

    private val fragmentId = R.id.am_id_fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyWindowSoftInputMode()

        mView = FrameLayout(this).apply {
            id = fragmentId
            layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        applyBackground()
        setContentView(mView)
        applyStatusBar()
        loadFragment()
    }

    //处理android:windowSoftInputMode
    private fun applyWindowSoftInputMode() {
        if (intent.hasExtra(EXTRA_WINDOW_SOFT_INPUT_MODE)) {
            window.setSoftInputMode(
                    intent.getIntExtra(
                            EXTRA_WINDOW_SOFT_INPUT_MODE,
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED
                    )
            )
        }
    }

    //背景
    private fun applyBackground() {
        if (intent.hasExtra(EXTRA_ACTIVITY_BACKGROUND_RES)) {
            mView.setBackgroundResource(
                    intent.getIntExtra(
                            EXTRA_ACTIVITY_BACKGROUND_RES,
                            0
                    )
            )
            return
        }

        if (intent.hasExtra(EXTRA_ACTIVITY_BACKGROUND_COLOR)) {
            mView.setBackgroundColor(
                    intent.getIntExtra(
                            EXTRA_ACTIVITY_BACKGROUND_COLOR,
                            Color.TRANSPARENT
                    )
            )
        }
    }

    //设置状态栏模式
    private fun applyStatusBar() {
        val statusBarMode = intent.getIntExtra(EXTRA_STATUS_BAR_MODE, STATUS_BAR_MODE_UNDEFINED)
        var statusBarColor: Int = VALUE_UNDEFINED

        if (intent.hasExtra(EXTRA_STATUS_BAR_COLOR_RES)) {
            val colorRes = intent.getIntExtra(EXTRA_STATUS_BAR_COLOR_RES, VALUE_UNDEFINED)
            statusBarColor = ContextCompat.getColor(this, colorRes)
        }

        if (intent.hasExtra(EXTRA_STATUS_BAR_COLOR)) {
            statusBarColor = intent.getIntExtra(EXTRA_STATUS_BAR_COLOR, VALUE_UNDEFINED)
        }

        when (statusBarMode) {
            STATUS_BAR_MODE_COLOR -> {
                if(statusBarColor == VALUE_UNDEFINED){
                    statusBarColor = getThemeAttrValue(R.attr.colorPrimaryDark)
                }
                applyStatusBarColor(statusBarColor)
            }
            STATUS_BAR_MODE_IMMERSIVE -> {
                if(statusBarColor == VALUE_UNDEFINED){
                    statusBarColor = Color.TRANSPARENT
                }
                applyImmersiveStatusBar(statusBarColor)
            }
            else -> {
                //nothing
            }
        }
        if (intent.hasExtra(EXTRA_ACTIVITY_LIGHT_MODE) && intent.getBooleanExtra(
                        EXTRA_ACTIVITY_LIGHT_MODE,
                        false
                )
        ) {
            applyStatusBarLightMode()
        }
    }

    //
    private fun loadFragment() {
        val fm = this.supportFragmentManager
        if (fm.findFragmentById(fragmentId) == null) {
            val fragmentName = intent.getStringExtra(EXTRA_FRAGMENT_NAME)!!
            val extras = intent.getBundleExtra(EXTRA_FRAGMENT_ARGUMENTS)
            val fragment = androidx.fragment.app.Fragment.instantiate(this, fragmentName, extras)
            fm.beginTransaction().replace(fragmentId, fragment).commit()
        }
    }

    class Builder(private val ctx: Context, private val fragmentName: String) {

        constructor(ctx: Context, fragmentClass: Class<out androidx.fragment.app.Fragment>)
                : this(ctx, fragmentClass.name)

        //传递给fragment的参数
        private var mFragmentArgs: Bundle? = null

        //Activity的背景颜色
        @ColorInt
        private var mBackgroundColor: Int = VALUE_UNDEFINED

        //背景资源
        private var mBackgroundResource: Int = VALUE_UNDEFINED

        //对应AndroidManifest中的android:windowSoftInputMode属性
        private var mWindowSoftInputMode: Int = VALUE_UNDEFINED

        //Activity是否是浅色模式：如果是浅色模式，则状态栏文字为深色（黑色）
        private var mIsLightMode: Boolean = false

        private var mStatusBarMode: Int = STATUS_BAR_MODE_UNDEFINED

        @ColorInt
        private var mStatusBarColor: Int = VALUE_UNDEFINED

        @ColorRes
        private var mStatusBarColorRes: Int = VALUE_UNDEFINED

        /**
         * 设置传递给Fragment的参数
         */
        fun setFragmentArgument(data: Bundle?): Builder {
            mFragmentArgs = data
            return this
        }

        /**
         * 设置背景颜色:优先级高于[setBackgroundResource],即同时调用这两个方法，此方法生效。
         */
        fun setBackgroundColor(@ColorInt color: Int): Builder {
            mBackgroundColor = color
            return this
        }

        /**
         * 设置背景资源
         */
        fun setBackgroundResource(res: Int): Builder {
            mBackgroundResource = res
            return this
        }

        /**
         * 设置android:windowSoftInputMode
         */
        fun setWindowSoftInputMode(mode: Int): Builder {
            mWindowSoftInputMode = mode
            return this
        }

        /**
         * Activity是否是浅色模式：如果是浅色模式（true），则状态栏文字为深色（黑色）
         */
        fun setLightMode(isLight: Boolean): Builder {
            mIsLightMode = isLight
            return this
        }

        /**
         * 设置沉浸式状态栏：状态栏会覆盖内容布局
         * @param color 状态栏背景颜色
         */
        @JvmOverloads
        fun setImmersiveStatusBar(@ColorInt color: Int = VALUE_UNDEFINED): Builder {
            mStatusBarMode = STATUS_BAR_MODE_IMMERSIVE
            mStatusBarColor = color
            return this
        }

        /**
         * 设置沉浸式状态栏：状态栏会覆盖内容布局
         * @param color 状态栏背景颜色
         */
        @JvmOverloads
        fun setImmersiveStatusBar2(@ColorRes colorId: Int = VALUE_UNDEFINED): Builder {
            mStatusBarMode = STATUS_BAR_MODE_IMMERSIVE
            mStatusBarColorRes = colorId
            return this
        }

        /**
         * 设置常规状态栏：即状态栏不会覆盖内容布局
         * @param color 状态栏颜色
         */
        fun setNormalStatusBar(@ColorInt color: Int): Builder {
            mStatusBarMode = STATUS_BAR_MODE_COLOR
            mStatusBarColor = color
            return this
        }

        /**
         * 设置状态栏颜色，优先级高于[setNormalStatusBar],即同时调用此方法与[setNormalStatusBar]时，此方法生效
         */
        fun setNormalStatusBar2(@ColorRes colorId: Int): Builder {
            mStatusBarMode = STATUS_BAR_MODE_COLOR
            mStatusBarColorRes = colorId
            return this
        }

        fun build(): Intent {
            val intent = Intent(ctx, AMContainerActivity::class.java)
            intent.putExtra(EXTRA_FRAGMENT_NAME, fragmentName)
            mFragmentArgs?.let {
                intent.putExtra(EXTRA_FRAGMENT_ARGUMENTS, it)
            }

            if (mBackgroundColor != VALUE_UNDEFINED) {
                intent.putExtra(EXTRA_ACTIVITY_BACKGROUND_COLOR, mBackgroundColor)
            }

            if (mBackgroundResource != VALUE_UNDEFINED) {
                intent.putExtra(EXTRA_ACTIVITY_BACKGROUND_RES, mBackgroundResource)
            }

            if (mWindowSoftInputMode != VALUE_UNDEFINED) {
                intent.putExtra(EXTRA_WINDOW_SOFT_INPUT_MODE, mWindowSoftInputMode)
            }

            //状态栏相关参数
            intent.putExtra(EXTRA_ACTIVITY_LIGHT_MODE, mIsLightMode)
            intent.putExtra(EXTRA_STATUS_BAR_MODE, mStatusBarMode)
            if (mStatusBarMode == STATUS_BAR_MODE_COLOR) {
                if (mStatusBarColor != VALUE_UNDEFINED)
                    intent.putExtra(EXTRA_STATUS_BAR_COLOR, mStatusBarColor)

                if (mStatusBarColorRes != VALUE_UNDEFINED)
                    intent.putExtra(EXTRA_STATUS_BAR_COLOR_RES, mStatusBarColorRes)
            }

            return intent
        }
    }

    companion object {

        //值未定义
        private const val VALUE_UNDEFINED = -404

        private const val EXTRA_FRAGMENT_NAME = "fragment_name"
        private const val EXTRA_FRAGMENT_ARGUMENTS = "fragment_arguments"

        private const val EXTRA_ACTIVITY_BACKGROUND_COLOR = "activity_bg_color"

        private const val EXTRA_ACTIVITY_BACKGROUND_RES = "activity_bg_res"

        private const val EXTRA_WINDOW_SOFT_INPUT_MODE = "window_soft_input_mode"

        private const val EXTRA_ACTIVITY_LIGHT_MODE = "light_mode"

        private const val EXTRA_STATUS_BAR_MODE = "status_bar_mode"

        private const val EXTRA_STATUS_BAR_COLOR = "status_bar_color"

        private const val EXTRA_STATUS_BAR_COLOR_RES = "status_bar_color_res"

        /**
         * 沉浸式模式
         */
        private const val STATUS_BAR_MODE_IMMERSIVE = 1

        /**
         * 修改状态栏颜色
         */
        private const val STATUS_BAR_MODE_COLOR = 0

        private const val STATUS_BAR_MODE_UNDEFINED = VALUE_UNDEFINED

        @JvmOverloads
        @JvmStatic
        fun newIntent(
                ctx: Context,
                fragmentClass: Class<out androidx.fragment.app.Fragment>, @Nullable args: Bundle? = null
        ): Intent {
            return newIntent(ctx, fragmentClass.name, args)
        }

        @JvmOverloads
        @JvmStatic
        fun newIntent(ctx: Context, fragmentName: String, @Nullable args: Bundle? = null): Intent {
            return Builder(ctx, fragmentName).setFragmentArgument(args).build()
        }
    }

}
