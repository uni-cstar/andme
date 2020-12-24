package andme.integration.support.media

import andme.integration.media.PictureSelector
import andme.integration.media.MediaFile
import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.luck.picture.lib.PictureSelectionModel
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.PictureSelector as LibPicSelector

/**
 * Created by Lucio on 2020-11-10.
 * https://github.com/LuckSiege/PictureSelector
 * PictureSelector 的一种集成实现
 */

internal class PictureSelectorImpl : PictureSelector {

    //初始化默认配置
    private val config = MediaStoreImpl.Config

    internal val realSelector: PictureSelectionModel

    init {

        spanCount = config.spanCount
        isCompressEnable = config.isCompressEnable
        compressQuality = config.compressQuality
        minCompressSize = config.minCompressSize
        isCropEnable = config.isCropEnable
        isCameraEnable = config.isCameraEnable
        isOriginalEnable = config.isOriginalEnable
    }

    constructor(activity: Activity, isSingle: Boolean, minSelectCount: Int, maxSelectCount: Int) : super(activity, isSingle, minSelectCount, maxSelectCount) {
        realSelector = LibPicSelector.create(activity).openGallery(PictureMimeType.ofImage())
        realSelector.isPageStrategy(config.isPageStrategy, config.pageStrategySize, config.isFilterInvalidFile)
    }

    constructor(fragment: Fragment, isSingle: Boolean, minSelectCount: Int, maxSelectCount: Int) : super(fragment, isSingle, minSelectCount, maxSelectCount) {
        realSelector = LibPicSelector.create(fragment).openGallery(PictureMimeType.ofImage())
        realSelector.isPageStrategy(config.isPageStrategy, config.pageStrategySize, config.isFilterInvalidFile)
    }

    private fun applyConfigs() {
        realSelector
                .imageSpanCount(spanCount)
                .selectionMode(if (isSingle) PictureConfig.SINGLE else PictureConfig.MULTIPLE)
                //单图时是否直接返回结果：即点击图片即为选中，不用点击确认按钮确认选择动作
                .isSingleDirectReturn(true)
                .isCompress(isCompressEnable)
                .minimumCompressSize(minCompressSize)
                .compressQuality(compressQuality)
                .isCamera(isCameraEnable)
                .isEnableCrop(isCropEnable)
                .isOriginalImageControl(isOriginalEnable)
                .minSelectNum(minSelectCount)
                .maxSelectNum(maxSelectCount)
                .imageEngine(config.imageEngine)

        //设置已选数据
        if (!selectedData.isNullOrEmpty()) {
            val selected = Util.mapResultReverse(selectedData)
            realSelector.selectionData(selected)
        }
    }

    override fun invoke(callback: Callback) {
        applyConfigs()
        realSelector.forResult(Util.mapCallback(callback))
    }

    override fun invoke(requestCode: Int) {
        applyConfigs()
        realSelector.forResult(requestCode)
    }

    override fun parseResult(data: Intent?): List<MediaFile>? {
        return LibPicSelector.obtainMultipleResult(data)
                ?.map {
                    PictureSelectorResult(it)
                }
    }

}