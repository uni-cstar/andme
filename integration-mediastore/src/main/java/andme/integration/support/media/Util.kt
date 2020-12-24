package andme.integration.support.media

import andme.integration.media.PictureSelector
import andme.integration.media.MediaFile
import andme.lang.Note
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
    fun obtainIntentResult(data: Intent?): List<MediaFile>? {
        val result = com.luck.picture.lib.PictureSelector.obtainMultipleResult(data)
        return mapResult(result)
    }

    /**
     * 映射选择结果
     */
    @JvmStatic
    fun mapResult(results: List<LocalMedia>?): List<MediaFile>? {
        return results?.map {
            PictureSelectorResult(it)
        }
    }

    @JvmStatic
    fun mapResultReverse(results: List<MediaFile>?): List<LocalMedia>? {
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
 *
 */
@Note("如果同时开启裁剪和压缩，则取压缩路径为准因为是先裁剪后压缩 参考链接：https://github.com/LuckSiege/PictureSelector/wiki/%E7%BB%93%E6%9E%9C%E5%9B%9E%E8%B0%83")
val LocalMedia.validPath: String
    get() {

        var filePath: String? = null
        if (isCompressed && !compressPath.isNullOrEmpty()) {
            filePath = compressPath
        }

        if (filePath.isNullOrEmpty() && isCut) {
            filePath = cutPath
        }

        if (filePath.isNullOrEmpty() && Build.VERSION.SDK_INT >= 29) {
            filePath = androidQToPath
        }

        if(filePath.isNullOrEmpty() && isOriginal){
            filePath = originalPath
        }

        if (filePath.isNullOrEmpty()) {
            filePath = path
        }
        return filePath.orEmpty()
    }

/**
 * 获取可用的路径地址
 */
@Note("如果同时开启裁剪和压缩，则取压缩路径为准因为是先裁剪后压缩 参考链接：https://github.com/LuckSiege/PictureSelector/wiki/%E7%BB%93%E6%9E%9C%E5%9B%9E%E8%B0%83")
val MediaFile.validPath: String
    get() {
        if(this is PictureSelectorResult)
            return real.validPath

        var result: String? = null

        if(isCompress && !compressPath.isNullOrEmpty()){
            result = compressPath
        }

        if(result.isNullOrEmpty() && isCrop && !cropPath.isNullOrEmpty()){
            result = cropPath
        }

        if(result.isNullOrEmpty() && isOriginal && !originalPath.isNullOrEmpty()){
            result = originalPath
        }

        if (result.isNullOrEmpty()) {
            result = filePath
        }
        return result.orEmpty()
    }