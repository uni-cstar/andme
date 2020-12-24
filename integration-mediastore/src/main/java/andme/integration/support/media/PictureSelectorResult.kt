package andme.integration.support.media

import andme.integration.media.MediaFile
import com.luck.picture.lib.entity.LocalMedia

/**
 * Created by Lucio on 2020-11-11.
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
