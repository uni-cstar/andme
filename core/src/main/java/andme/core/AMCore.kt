@file:JvmName("AMCore")
@file:JvmMultifileClass
/**
 * Created by Lucio on 2020-11-09.
 */
package andme.core

import andme.core.app.AMAppManager
import andme.core.app.internal.AppManagerImpl
import andme.core.content.syncResourcesValues
import andme.core.exception.CommonExceptionHandler
import andme.core.exception.ExceptionHandler
import andme.core.kt.orDefault
import andme.core.support.io.AMStorage
import andme.core.support.io.AMStorageImpl
import andme.core.support.ui.DefaultDialogHandler
import andme.core.support.ui.DefaultToastHandler
import andme.core.support.ui.DialogUI
import andme.core.support.ui.ToastUI
import andme.core.sysui.AMSystemUI
import andme.core.sysui.AMSystemUIImpl
import andme.integration.imageloader.GlideImageLoader
import andme.integration.imageloader.ImageLoader
import andme.integration.media.MediaStore
import android.app.Application
import android.content.Context

lateinit var mApp: Context

/**
 * 初始化Core Lib
 */
fun initCore(app: Application) {
    mApp = app
    AppManagerImpl.init(app)
    syncResourcesValues()
}

/**
 * 是否开启调试模式
 */
var isDebuggable: Boolean = BuildConfig.DEBUG

/**
 * app管理器
 */
val appManagerAM: AMAppManager = AppManagerImpl



//媒体相关功能支持器懒加载函数
var mediaStoreCreator: (() -> MediaStore)? = null

/**
 * 媒体相关功能支持器：用于媒体操作的统一管理
 */
var mediaStoreAM: MediaStore
    get() {
        return mMediaStore.orDefault {
            mediaStoreCreator?.invoke()
        }.orDefault {
            val clzName = Class.forName("$INTEGRATION_PKG_NAME.media.MediaStoreImpl")
            clzName.getDeclaredConstructor().newInstance() as MediaStore
        }
    }
    set(value) {
        mMediaStore = value
    }

/**
 * 图片加载器
 */
var imageLoaderAM: ImageLoader = GlideImageLoader

/**
 * 默认异常处理器
 */
val defaultExceptionHandler = CommonExceptionHandler()

/**
 * 全局异常处理器
 */
var exceptionHandlerAM: ExceptionHandler = defaultExceptionHandler

/**
 * 对话框交互
 */
var dialogHandlerAM: DialogUI = DefaultDialogHandler

/**
 * Toast交互
 */
var toastHandlerAM: ToastUI = DefaultToastHandler

/**
 * 文件存储统一管理
 */
var storageAM: AMStorage = AMStorageImpl

/**
 * 系统UI交互：状态栏，虚拟导航栏
 */
var systemUIAM: AMSystemUI = AMSystemUIImpl