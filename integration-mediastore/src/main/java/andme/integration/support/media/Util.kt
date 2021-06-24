package andme.integration.support.media

import andme.integration.media.MediaFile
import andme.integration.media.PictureSelector
import andme.integration.support.media.pictureselector.PictureSelectorCallback
import andme.integration.support.media.pictureselector.PictureSelectorImpl
import andme.integration.support.media.pictureselector.PictureSelectorResult
import andme.lang.Note
import android.content.Intent
import android.graphics.Color
import android.os.Build
import com.luck.picture.lib.R
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.style.PictureParameterStyle

/**
 * Created by Lucio on 2020-11-11.
 * 媒体库的相关工具类
 */
object Util {

    /**
     * 解析Intent中的已选择数据，并转换成自定义结果
     */
    @JvmStatic
    fun obtainMultipleResult(data: Intent?): List<MediaFile>? {
        val result = com.luck.picture.lib.PictureSelector.obtainMultipleResult(data)
        return mapResult(result)
    }

    /**
     * 映射选择结果：将第三方库的数据类型转换成自定义数据类型
     */
    @JvmStatic
    fun mapResult(results: List<LocalMedia>?): List<MediaFile>? {
        return results?.map {
            PictureSelectorResult(it)
        }
    }

    /**
     * 将自定义数据类型转换成第三方所需数据类型
     */
    @JvmStatic
    fun mapResultReverse(results: List<MediaFile>?): List<LocalMedia>? {
        return results?.map {
            if (it !is PictureSelectorResult)
                throw IllegalArgumentException("不支持的数据类型，只支持${PictureSelectorResult::class.java.name}类型.")
            it.real
        }
    }


    /**
     * 映射回调：将第三方库回调转换成自定义回调
     */
    @JvmStatic
    fun mapCallback(callback: PictureSelector.Callback): PictureSelectorCallback {
        return PictureSelectorCallback(callback)
    }


    /**
     * 微信图片选取样式
     */
    @JvmStatic
    fun newWeChatStyle(): PictureParameterStyle {
        val uiStyle = PictureParameterStyle()
        // 开启新选择风格
        // 开启新选择风格
//        uiStyle.isNewSelectStyle = true
        // 是否改变状态栏字体颜色(黑白切换)
        // 是否改变状态栏字体颜色(黑白切换)
        uiStyle.isChangeStatusBarFontColor = false
        // 是否开启右下角已完成(0/9)风格
        // 是否开启右下角已完成(0/9)风格
        uiStyle.isOpenCompletedNumStyle = false
        // 是否开启类似QQ相册带数字选择风格
        // 是否开启类似QQ相册带数字选择风格
        uiStyle.isOpenCheckNumStyle = true
        // 状态栏背景色
        // 状态栏背景色
        uiStyle.pictureStatusBarColor = Color.parseColor("#393a3e")
        // 相册列表标题栏背景色
        // 相册列表标题栏背景色
        uiStyle.pictureTitleBarBackgroundColor = Color.parseColor("#393a3e")
        // 相册父容器背景色
        // 相册父容器背景色
        uiStyle.pictureContainerBackgroundColor = Color.parseColor("#FFFFFF")
        // 相册列表标题栏右侧上拉箭头
        // 相册列表标题栏右侧上拉箭头
        uiStyle.pictureTitleUpResId = R.drawable.picture_icon_wechat_up
        // 相册列表标题栏右侧下拉箭头
        // 相册列表标题栏右侧下拉箭头
        uiStyle.pictureTitleDownResId = R.drawable.picture_icon_wechat_down
        // 相册文件夹列表选中圆点
        // 相册文件夹列表选中圆点
        uiStyle.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval
        // 相册返回箭头
        // 相册返回箭头
        uiStyle.pictureLeftBackIcon = R.drawable.picture_icon_close
        // 标题栏字体颜色
        // 标题栏字体颜色
        uiStyle.pictureTitleTextColor = Color.parseColor("#FFFFFF")
        // 相册右侧按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
        // 相册右侧按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
        uiStyle.pictureCancelTextColor = Color.parseColor("#53575e")
        // 相册右侧按钮字体默认颜色
        // 相册右侧按钮字体默认颜色
        uiStyle.pictureRightDefaultTextColor = Color.parseColor("#53575e")
        // 相册右侧按可点击字体颜色,只针对isWeChatStyle 为true时有效果
        // 相册右侧按可点击字体颜色,只针对isWeChatStyle 为true时有效果
        uiStyle.pictureRightSelectedTextColor = Color.parseColor("#FFFFFF")
        // 相册右侧按钮背景样式,只针对isWeChatStyle 为true时有效果
        // 相册右侧按钮背景样式,只针对isWeChatStyle 为true时有效果
        uiStyle.pictureUnCompleteBackgroundStyle = R.drawable.picture_send_button_default_bg
        // 相册右侧按钮可点击背景样式,只针对isWeChatStyle 为true时有效果
        // 相册右侧按钮可点击背景样式,只针对isWeChatStyle 为true时有效果
        uiStyle.pictureCompleteBackgroundStyle = R.drawable.picture_send_button_bg
        // 选择相册目录背景样式
        // 选择相册目录背景样式
        uiStyle.pictureAlbumStyle = R.drawable.picture_item_select_bg
        // 相册列表勾选图片样式
        // 相册列表勾选图片样式
        uiStyle.pictureCheckedStyle = R.drawable.picture_wechat_num_selector
        // 相册标题背景样式 ,只针对isWeChatStyle 为true时有效果
        // 相册标题背景样式 ,只针对isWeChatStyle 为true时有效果
        uiStyle.pictureWeChatTitleBackgroundStyle = R.drawable.picture_album_bg
        // 微信样式 预览右下角样式 ,只针对isWeChatStyle 为true时有效果
        // 微信样式 预览右下角样式 ,只针对isWeChatStyle 为true时有效果
        uiStyle.pictureWeChatChooseStyle = R.drawable.picture_wechat_select_cb
        // 相册返回箭头 ,只针对isWeChatStyle 为true时有效果
        // 相册返回箭头 ,只针对isWeChatStyle 为true时有效果
        uiStyle.pictureWeChatLeftBackStyle = R.drawable.picture_icon_back
        // 相册列表底部背景色
        // 相册列表底部背景色
        uiStyle.pictureBottomBgColor = Color.parseColor("#393a3e")
        // 已选数量圆点背景样式
        // 已选数量圆点背景样式
        uiStyle.pictureCheckNumBgStyle = R.drawable.picture_num_oval
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        uiStyle.picturePreviewTextColor = Color.parseColor("#FFFFFF")
        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        uiStyle.pictureUnPreviewTextColor = Color.parseColor("#9b9b9b")
        // 相册列表已完成色值(已完成 可点击色值)
        // 相册列表已完成色值(已完成 可点击色值)
        uiStyle.pictureCompleteTextColor = Color.parseColor("#FFFFFF")
        // 相册列表未完成色值(请选择 不可点击色值)
        // 相册列表未完成色值(请选择 不可点击色值)
        uiStyle.pictureUnCompleteTextColor = Color.parseColor("#53575e")
        // 预览界面底部背景色
        // 预览界面底部背景色
        uiStyle.picturePreviewBottomBgColor = Color.parseColor("#a0393a3e")
        // 外部预览界面删除按钮样式
        // 外部预览界面删除按钮样式
        uiStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete
        // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
        // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
        uiStyle.pictureOriginalControlStyle = R.drawable.picture_original_wechat_checkbox
        // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
        // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
        uiStyle.pictureOriginalFontColor = Color.parseColor("#FFFFFF")
        // 外部预览界面是否显示删除按钮
        // 外部预览界面是否显示删除按钮
        uiStyle.pictureExternalPreviewGonePreviewDelete = true
        // 设置NavBar Color SDK Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP有效
        // 设置NavBar Color SDK Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP有效
        uiStyle.pictureNavBarColor = Color.parseColor("#393a3e")
        return uiStyle
//        return PictureParameterStyle.ofNewStyle()
    }

    /**
     *
     */
    @JvmStatic
    fun newWeChatStyle2(): PictureParameterStyle {
        return newWeChatStyle().apply {
            pictureUnCompleteText = "完成"
            isCompleteReplaceNum = true
            pictureCompleteText = "完成(%d)"
        }
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

        if (filePath.isNullOrEmpty() && isOriginal) {
            filePath = originalPath
        }

        //原图path，但在Android Q版本上返回的是content:// Uri类型
        if (filePath.isNullOrEmpty()) {
            filePath = path
        }
        return filePath.orEmpty()
    }

val LocalMedia.validLargePath: String
    get() {
        //先尝试返回原图
        if (isOriginal && !originalPath.isNullOrEmpty()) {
            return originalPath
        }

        //The real path，But you can't get access from AndroidQ
        if (Build.VERSION.SDK_INT < 29 && !realPath.isNullOrEmpty()) {
            return realPath
        }

        //原图path，但在Android Q版本上返回的是content:// Uri类型
        if (!path.isNullOrEmpty()) {
            return path
        }

        if (!androidQToPath.isNullOrEmpty()) {
            return androidQToPath
        }

        if (isCompressed && !compressPath.isNullOrEmpty()) {
            return compressPath
        }

        if (isCut && !cutPath.isNullOrEmpty()) {
            return cutPath
        }

        return ""
    }

/**
 * 获取可用的路径地址
 */
@Note("如果同时开启裁剪和压缩，则取压缩路径为准因为是先裁剪后压缩 参考链接：https://github.com/LuckSiege/PictureSelector/wiki/%E7%BB%93%E6%9E%9C%E5%9B%9E%E8%B0%83")
val MediaFile.validPath: String
    get() {
        if (this is PictureSelectorResult)
            return real.validPath

        var result: String? = null

        if (isCompress && !compressPath.isNullOrEmpty()) {
            result = compressPath
        }

        if (result.isNullOrEmpty() && isCrop && !cropPath.isNullOrEmpty()) {
            result = cropPath
        }

        if (result.isNullOrEmpty() && isOriginal && !originalPath.isNullOrEmpty()) {
            result = originalPath
        }

        if (result.isNullOrEmpty()) {
            result = filePath
        }
        return result.orEmpty()
    }

val MediaFile.validLargePath: String
    get() {
        if (this is PictureSelectorResult)
            return real.validPath

        //先尝试返回原图
        if (isOriginal && !originalPath.isNullOrEmpty()) {
            return originalPath!!
        }
        if (!filePath.isNullOrEmpty()) {
            return filePath!!
        }

        if (isCompress && !compressPath.isNullOrEmpty()) {
            return compressPath!!
        }

        if (isCrop && !cropPath.isNullOrEmpty()) {
            return cropPath!!
        }
        return ""
    }

/**
 * 设置成微信图片选取效果
 */
fun PictureSelectorImpl.applyWeChatStyle() {
    realSelector.apply {
        isWeChatStyle(true)
        setPictureStyle(Util.newWeChatStyle())
    }
}

/**
 * 设置成微信图片选取效果，只是右上角的按钮不显示限定的数量
 */
fun PictureSelectorImpl.applyWeChatStyle2() {
    realSelector.apply {
        isWeChatStyle(true)
        setPictureStyle(Util.newWeChatStyle2())
    }
}