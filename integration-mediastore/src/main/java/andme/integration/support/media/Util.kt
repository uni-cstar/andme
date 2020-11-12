package andme.integration.support.media

import andme.integration.media.PictureSelector
import android.content.Intent
import android.os.Build
import com.luck.picture.lib.entity.LocalMedia

/**
 * Created by Lucio on 2020-11-11.
 */
object Util {

    /**
     * 解析Intent中的已选择数据，并转换结果
     */
    @JvmStatic
    fun obtainIntentResult(data: Intent?): List<PictureSelector.Result>? {
        val result = com.luck.picture.lib.PictureSelector.obtainMultipleResult(data)
        return mapResult(result)
    }

    /**
     * 映射选择结果
     */
    @JvmStatic
    fun mapResult(results: List<LocalMedia>?): List<PictureSelector.Result>? {
        return results?.map {
            PictureSelectorResult(it)
        }
    }

    @JvmStatic
    fun mapResultReverse(results: List<PictureSelector.Result>?): List<LocalMedia>? {
        return results?.map {
            if (it !is PictureSelectorResult)
                throw IllegalArgumentException("不支持的数据类型，只支持${PictureSelectorResult::class.java.name}类型.")
            it.real
        }
    }


    /**
     * 映射回调
     */
    @JvmStatic
    fun mapCallback(callback: PictureSelector.Callback): PictureSelectorCallback {
        return PictureSelectorCallback(callback)
    }
}

/**
 * 获取可用的路径地址
 */
val LocalMedia.validPath: String
    get() {
        var filePath: String? = null
        if (isCut) {
            filePath = cutPath
        }

        if (filePath.isNullOrEmpty() && isCompressed) {
            filePath = compressPath
        }

        if (filePath.isNullOrEmpty() && Build.VERSION.SDK_INT >= 29) {
            filePath = androidQToPath
        }

        if (filePath.isNullOrEmpty()) {
            filePath = path
        }
        return filePath.orEmpty()
    }

/**
 * 获取可用的路径地址
 */
val PictureSelector.Result.validPath: String
    get() {
        if(this is PictureSelectorResult)
            return real.validPath
        var result: String? = null
        if (isCrop) {
            result = cropPath
        }

        if (result.isNullOrEmpty() && isCompress) {
            result = compressPath
        }


        if (result.isNullOrEmpty()) {
            result = filePath
        }
        return result.orEmpty()

    }