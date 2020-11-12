package andme.integration.support.media

import andme.integration.media.MediaStore
import andme.integration.media.PictureSelector
import android.app.Activity
import androidx.annotation.IntRange
import androidx.fragment.app.Fragment
import com.luck.picture.lib.engine.ImageEngine

/**
 * Created by Lucio on 2020-11-10.
 */
class MediaStoreImpl : MediaStore {

    /**
     * 单图选择器
     */

    override fun singlePicSelector(activity: Activity): PictureSelector {
        return PictureSelectorImpl(activity, true, 1, 1)
    }

    /**
     * 单图选择器
     */

    override fun singlePicSelector(fragment: Fragment): PictureSelector {
        return PictureSelectorImpl(fragment, true, 1, 1)
    }

    /**
     * 正方形头像选取初始化
     * @param maxSize 图片大小限制
     */
    private fun setupForRectPic(selector: PictureSelectorImpl, maxSize: Int, isCircleDimmedLayer: Boolean) {
        selector.minCompressSize = 200
        selector.realSelector.withAspectRatio(1, 1)
                .cropImageWideHigh(maxSize, maxSize)
                .rotateEnabled(false)//禁用旋转
                .circleDimmedLayer(isCircleDimmedLayer)//是否开启圆形裁剪
                .showCropFrame(!isCircleDimmedLayer)//是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(!isCircleDimmedLayer)//是否显示裁剪矩形边框 圆形裁剪时建议设为false
    }

    /**
     * 正方形头像图片选择器
     * @param maxSize 图片大小限制
     */
    override fun squarePicSelector(activity: Activity, maxSize: Int): PictureSelector {
        val selector = singlePicSelector(activity)
        setupForRectPic(selector as PictureSelectorImpl, maxSize, false)
        return selector
    }

    /**
     * 正方形头像图片选择器
     * @param maxSize 图片大小限制
     */
    override fun squarePicSelector(fragment: Fragment, maxSize: Int): PictureSelector {
        val selector = singlePicSelector(fragment)
        setupForRectPic(selector as PictureSelectorImpl, maxSize, false)
        return selector
    }

    /**
     * 圆形头像图片选择器
     * @param maxSize 图片大小限制
     */
    override fun circlePicSelector(activity: Activity, maxSize: Int): PictureSelector {
        val selector = singlePicSelector(activity)
        setupForRectPic(selector as PictureSelectorImpl, maxSize, true)
        return selector
    }

    /**
     * 正方形头像图片选择器
     */

    override fun circlePicSelector(fragment: Fragment, maxSize: Int): PictureSelector {
        val selector = singlePicSelector(fragment)
        setupForRectPic(selector as PictureSelectorImpl, maxSize, true)
        return selector
    }

    /**
     * 多图选择器
     * @param minSelectCount 最少选择数量
     * @param maxSelectCount 最多选择数量
     */

    override fun multiPicSelector(activity: Activity, @IntRange(from = 0, to = Long.MAX_VALUE) minSelectCount: Int,
                                  @IntRange(from = 0, to = Long.MAX_VALUE) maxSelectCount: Int): PictureSelector {
        return PictureSelectorImpl(activity, false, minSelectCount, maxSelectCount)
    }

    /**
     * 多图选择器
     * @param minSelectCount 最少选择数量
     * @param maxSelectCount 最多选择数量
     */

    override fun multiPicSelector(fragment: Fragment, @IntRange(from = 0, to = Long.MAX_VALUE) minSelectCount: Int,
                                  @IntRange(from = 0, to = Long.MAX_VALUE) maxSelectCount: Int): PictureSelector {
        return PictureSelectorImpl(fragment, false, minSelectCount, maxSelectCount)
    }


    // 默认配置
    object Config {

        /**
         * 每行显示个数
         */
        var spanCount: Int = 3

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
         * 是否启用分页加载
         */
        var isPageStrategy: Boolean = true

        /**
         * 分页大小
         */
        var pageStrategySize: Int = 60

        /**
         * 是否过滤破损的文件
         */
        var isFilterInvalidFile: Boolean = true

        /**
         * 图片加载引擎
         */
        var imageEngine: ImageEngine = ImageLoaderEngine
//
//        /**
//         * 展位图
//         */
//        var placeHolder:Drawable = ColorDrawable()

    }
}