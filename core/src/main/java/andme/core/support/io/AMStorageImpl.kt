package andme.core.support.io

import andme.core.mApp
import andme.core.support.AMStorage
import android.os.Build
import android.os.Environment
import java.io.File

/**
 * Created by Lucio on 2019/6/30.
 */

internal object AMStorageImpl : AMStorage {

    private const val DIR_TYPE_DOWNLOAD = "Download"

    override val isExternalStorageAvailable: Boolean
        get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    private fun isStorageStateAvailable(file: File): Boolean {
        return (Build.VERSION.SDK_INT >= 21
                && Environment.getExternalStorageState(file) == Environment.MEDIA_MOUNTED)
                || Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    override fun ensureFileAvailable(file: File, recreateIfExists: Boolean) {
        require(!file.isFile) { "not support directory." }

        val parent = file.parentFile
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) {
                throw NoSuchFileException(parent, null, "create directory failed.")
            }
        }
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw FileSystemException(file, null, "create file failed.")
            }
        } else {
            if (recreateIfExists) {
                file.delete()
                if (!file.createNewFile()) {
                    throw FileSystemException(file, null, "create file failed.")
                }
            }
        }
    }

    override fun getCacheDirectory(): File {
        val extCacheFile = mApp.externalCacheDir//storage/emulated/0/Android/data/{packagename}/cache

        if (extCacheFile != null && isStorageStateAvailable(extCacheFile)) {
            return extCacheFile
        }
        return getInnerCacheDirectory()
    }

    override fun getInnerCacheDirectory(): File = mApp.cacheDir

    override fun getCacheDirectory(child: String): File {
        require(child.isNotEmpty()) { "child path is empty." }
        val parent = getCacheDirectory()
        val dir = File(parent, child)
        if (!dir.exists()) {
            if (!dir.mkdirs())
                throw NoSuchFileException(dir, null, "create directory failed")
        }
        return dir
    }

    override fun getFilesDirectory(): File {
        val extFileDir = mApp.getExternalFilesDir(null)
        if (extFileDir != null && isStorageStateAvailable(extFileDir)) {
            return extFileDir
        }
        return getInnerFilesDirectory()
    }

    override fun getInnerFilesDirectory() = mApp.filesDir!!

    override fun getFilesDirectory(child: String): File {
        require(child.isNotEmpty()) { "child path is empty." }
        val parent = getFilesDirectory()
        val dir = File(parent, child)
        if (!dir.exists()) {
            if (!dir.mkdirs())
                throw NoSuchFileException(dir, null, "create directory failed")
        }
        return dir
    }

    override fun getDownloadDirectory(): File {
        return getFilesDirectory(DIR_TYPE_DOWNLOAD)
    }

    override fun getInnerDownloadDirectory(): File {
        val dir = File(mApp.filesDir, DIR_TYPE_DOWNLOAD)
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                throw NoSuchFileException(dir, null, "create $DIR_TYPE_DOWNLOAD directory failed")
            }
        }
        return dir
    }

}