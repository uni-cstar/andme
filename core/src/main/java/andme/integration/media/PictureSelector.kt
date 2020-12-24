package andme.integration.media

import android.app.Activity
import android.content.Intent
import androidx.annotation.IntRange
import androidx.fragment.app.Fragment
import java.lang.ref.SoftReference

/**
 * Created by Lucio on 2020-11-09.
 * 图片选择器
 * @param isSingle 是否选择单图
 * @param maxCount 支持选择的最大个数
 */
abstract class PictureSelector private constructor(val isSingle: Boolean, val minSelectCount: Int, val maxSelectCount: Int) {

    private var mActivityRef: SoftReference<Activity>? = null
    private var mFragmentRef: SoftReference<Fragment>? = null

    constructor(activity: Activity, isSingle: Boolean, minSelectCount: Int, maxSelectCount: Int) : this(isSingle, minSelectCount, maxSelectCount) {
        this.mActivityRef = SoftReference(activity)
    }

    constructor(fragment: Fragment, isSingle: Boolean, minSelectCount: Int, maxSelectCount: Int) : this(isSingle, minSelectCount, maxSelectCount) {
        this.mFragmentRef = SoftReference(fragment)
    }

    val activity get() = mActivityRef?.get()

    val fragment get() = mFragmentRef?.get()

    /**
     * 是否支持压缩；默认开启压缩
     */
    var isCompressEnable: Boolean = true

    /**
     * 压缩质量；0-100，默认80；
     */
    @IntRange(from = 0, to = 100)
    var compressQuality: Int = 80

    /**
     * 最小压缩大小；即低于此设置的大小不进行压缩；单位kb，默认低于150kb不进行压缩
     */
    var minCompressSize: Int = 150

    /**
     * 是否支持裁剪；默认不开启
     */
    var isCropEnable: Boolean = false

    /**
     * 是否显示拍照按钮；默认显示
     */
    var isCameraEnable: Boolean = true

    /**
     * 是否支持原图
     */
    var isOriginalEnable: Boolean = false

    /**
     * 每行显示个数
     */
    var spanCount: Int = 3

    /**
     * 已选择数据
     */
    var selectedData:List<MediaFile>? = null

    /**
     * 执行
     */
    abstract fun invoke(callback: Callback)

    /**
     * 执行
     */
    abstract fun invoke(requestCode:Int)

    /**
     * 解析数据；用于使用requestCode请求方式时使用
     */
    abstract fun parseResult(data:Intent?):List<MediaFile>?

    interface Callback{

         fun onPictureSelectResult(result: List<MediaFile>?)


         fun onPictureSelectCanceled()
    }

}