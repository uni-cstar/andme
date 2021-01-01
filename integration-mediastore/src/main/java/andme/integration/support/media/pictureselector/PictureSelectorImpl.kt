package andme.integration.support.media.pictureselector

import andme.integration.media.MediaFile
import andme.integration.media.PictureSelector
import andme.integration.support.media.MediaStoreImpl
import andme.integration.support.media.Util
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
 * 基于PictureSelector 的一种图片选择集成实现
 */
open class PictureSelectorImpl : PictureSelector {

    val realSelector: PictureSelectionModel

    constructor(activity: Activity, isSingle: Boolean, minSelectCount: Int, maxSelectCount: Int) : super(activity, isSingle, minSelectCount, maxSelectCount) {
        realSelector = LibPicSelector.create(activity).openGallery(PictureMimeType.ofImage())
        applyDefaultConfigs()
    }

    constructor(fragment: Fragment, isSingle: Boolean, minSelectCount: Int, maxSelectCount: Int) : super(fragment, isSingle, minSelectCount, maxSelectCount) {
        realSelector = LibPicSelector.create(fragment).openGallery(PictureMimeType.ofImage())
        applyDefaultConfigs()
    }

    private fun applyDefaultConfigs(){
         val config = MediaStoreImpl.DefaultPictureSelectorConfig
        spanCount = MediaStoreImpl.DefaultPictureSelectorConfig.spanCount
        isCompressEnable = MediaStoreImpl.DefaultPictureSelectorConfig.isCompressEnable
        compressQuality = MediaStoreImpl.DefaultPictureSelectorConfig.compressQuality
        minCompressSize = MediaStoreImpl.DefaultPictureSelectorConfig.minCompressSize
        isCropEnable = MediaStoreImpl.DefaultPictureSelectorConfig.isCropEnable
        isCameraEnable = MediaStoreImpl.DefaultPictureSelectorConfig.isCameraEnable
        isOriginalEnable = MediaStoreImpl.DefaultPictureSelectorConfig.isOriginalEnable
        realSelector.isPageStrategy(MediaStoreImpl.DefaultPictureSelectorConfig.isPageStrategy, MediaStoreImpl.DefaultPictureSelectorConfig.pageStrategySize, MediaStoreImpl.DefaultPictureSelectorConfig.isFilterInvalidFile)
    }

    private fun applyConfigs(initializer: ((PictureSelectionModel) -> Unit)? = null) {
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
                .imageEngine(MediaStoreImpl.DefaultPictureSelectorConfig.imageEngine)

        //设置已选数据
        if (!selectedData.isNullOrEmpty()) {
            val selected = Util.mapResultReverse(selectedData)
            realSelector.selectionData(selected)
        }

        initializer?.invoke(realSelector)
    }

    override fun invoke(callback: Callback) {
        applyConfigs()
        realSelector.forResult(Util.mapCallback(callback))
    }

    override fun invoke(requestCode: Int) {
        applyConfigs()
        realSelector.forResult(requestCode)
    }

    fun invoke(callback: Callback, initializer: (PictureSelectionModel) -> Unit) {
        applyConfigs(initializer)
        realSelector.forResult(Util.mapCallback(callback))
    }

    fun invoke(requestCode: Int, initializer: (PictureSelectionModel) -> Unit) {
        applyConfigs(initializer)
        realSelector.forResult(requestCode)
    }

    override fun parseResult(data: Intent?): List<MediaFile>? {
        return Util.obtainMultipleResult(data)
    }

}


