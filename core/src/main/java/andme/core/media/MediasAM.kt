package andme.core.media

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import java.io.File

/**
 * Created by Lucio on 2021/6/18.
 */
object MediasAM {
    /**
     * 创建视频缩略图
     */
    fun createVideoThumbnail(videoPath: String): Bitmap? {
        if (!File(videoPath).exists()) throw IllegalStateException("file is not exists.")
        val media = MediaMetadataRetriever()
        media.setDataSource(videoPath)
        val bitmap = media.frameAtTime
        media.release()
        return bitmap
    }
}

