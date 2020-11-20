package andme.core


import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File

/**
 * Created by Lucio on 2020-02-28.
 */
@RunWith(JUnit4::class)
class AMStoreTest {

    @Test
    fun getPaths() = runBlocking {
        mApp = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals(false, storageAM.isExternalStorageAvailable)
        println("CacheDirectory=${storageAM.getCacheDirectory().absolutePath}")
        println("InnerCacheDirectory=${storageAM.getInnerCacheDirectory().absolutePath}")
        println("ImageCacheDirectory=${storageAM.getCacheDirectory("Image").absolutePath}")

        println("FilesDirectory=${storageAM.getFilesDirectory().absolutePath}")
        println("InnerFilesDirectory=${storageAM.getInnerFilesDirectory().absolutePath}")

        println("DownloadDirectory=${storageAM.getDownloadDirectory().absolutePath}")
        println("InnerDownloadDirectory=${storageAM.getInnerDownloadDirectory().absolutePath}")
        teststorageAM()
    }

    fun teststorageAM(){
        println("isExternalStorageAvailable=$storageAM.isExternalStorageAvailable")
        println("CacheDirectory=${storageAM.getCacheDirectory().absolutePath}")
        println("InnerCacheDirectory=${storageAM.getInnerCacheDirectory().absolutePath}")
        println("ImageCacheDirectory=${storageAM.getCacheDirectory("Image").absolutePath}")
        println("CustomCacheDirectory=${storageAM.getCacheDirectory("Custom")}")
        println("CustomCachePath=${File( storageAM.getCacheDirectory("Custom"),"cache.txt")}")

        println("FilesDirectory=${storageAM.getFilesDirectory().absolutePath}")
        println("InnerFilesDirectory=${storageAM.getInnerFilesDirectory().absolutePath}")
        println("CustomFileDirectory=${storageAM.getFilesDirectory("Custom")}")
        println("CustomFilePath=${File( storageAM.getFilesDirectory("Custom"),"child.txt")}")

        println("DownloadDirectory=${storageAM.getDownloadDirectory().absolutePath}")
        println("InnerDownloadDirectory=${storageAM.getInnerDownloadDirectory().absolutePath}")
    }
}