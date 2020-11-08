package andme.core

import andme.core.app.AMAppManager
import andme.core.app.internal.AppManagerImpl
import andme.core.exception.CommonExceptionHandler
import andme.core.exception.ExceptionHandler
import andme.core.interaction.DefaultDialogHandler
import andme.core.interaction.DialogInteraction

/**
 * Created by Lucio on 2020-11-03.
 */

/**
 * 是否开启调试模式
 */
var isDebuggable: Boolean = true


val appManagerAM: AMAppManager = AppManagerImpl

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
var dialogHandler: DialogInteraction = DefaultDialogHandler