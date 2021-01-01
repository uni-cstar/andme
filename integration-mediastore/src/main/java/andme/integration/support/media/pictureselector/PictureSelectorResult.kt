package andme.integration.support.media.pictureselector

import andme.integration.media.MediaFile
import com.luck.picture.lib.entity.LocalMedia

/**
 * Created by Lucio on 2020-11-11.
 * 自定义图片选择结果实现：对第三方库数据类型的代理
 */
class PictureSelectorResult(val real: LocalMedia) : MediaFile() {

    override val filePath: String?
        get() = real.path

    override val compressPath: String?
        get() = real.compressPath

    override val isCompress: Boolean
        get() = real.isCompressed

    override val cropPath: String?
        get() = real.cutPath

    override val isCrop: Boolean
        get() = real.isCut

    override val originalPath: String?
        get() = real.originalPath

    override val isOriginal: Boolean
        get() = real.isOriginal

    override val fileName: String?
        get() = real.fileName

    override val dirName: String?
        get() = real.parentFolderName

}
