package andme.core

import andme.core.app.AMAppManager
import andme.core.app.internal.AppManagerImpl
import andme.core.content.syncResourcesValues
import andme.core.exception.CommonExceptionHandler
import andme.core.exception.ExceptionHandler
import andme.core.kt.orDefault
import andme.core.ui.DefaultDialogHandler
import andme.core.ui.DefaultToastHandler
import andme.core.ui.DialogUI
import andme.core.ui.ToastUI
import andme.integration.imageloader.GlideImageLoader
import andme.integration.imageloader.ImageLoader
import andme.integration.media.MediaStore
import android.app.Application

/**
 * Created by Lucio on 2020-11-09.
 */

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
var isDebuggable: Boolean = true

internal lateinit var mApp: Application

/**
 * app管理器
 */
val appManagerAM: AMAppManager = AppManagerImpl

private var mMediaStore: MediaStore? = null

//默认集成库包名路径
private const val INTEGRATION_PKG_NAME = "andme.integration.support"

/**
 * 懒加载
 */
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
val DefaultExceptionHandler = CommonExceptionHandler()

/**
 * 全局异常处理器
 */
var exceptionHandler: ExceptionHandler = DefaultExceptionHandler

/**
 * 对话框交互
 */
var dialogHandler: DialogUI = DefaultDialogHandler

/**
 * Toast交互
 */
var toastHandler: ToastUI = DefaultToastHandler