@file:JvmName("AMBitmap")

package andme.core.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Base64
import androidx.annotation.ColorInt
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max

/**
 * 获取缩放压缩之后的图片数据
 * @quality Hint to the compressor, 0-100. 0 meaning compress for
 *                 small size, 100 meaning compress for max quality. Some
 *                 formats, like PNG which is lossless, will ignore the
 *                 quality setting
 */
@JvmOverloads
fun ScaledCompressedBitmapData(path: String, reqWidth: Int, reqHeight: Int, quality: Int = 100, format: CompressFormat = CompressFormat.JPEG): ByteArray? {
    //创建用于缩放的Options
    val options = BitmapOptions(path, reqWidth, reqHeight)
    //解析出图片
    return DecodedBitmap(path, options)?.toByteArray(true, format, quality)
}

/**
 * 获取缩放压缩、角度修正之后的图片数据
 * @param context
 * @param path
 * @return
 */
fun CorrectedScaledCompressedBitmapData(path: String,
                                        maxWidth: Int,
                                        maxHeight: Int,
                                        quality: Int = 100,
                                        format: CompressFormat = CompressFormat.JPEG): ByteArray? {
    //获取偏转角度
    val degree = ImageRotateDegree(path)
    if (degree == 0) {
        return ScaledCompressedBitmapData(path, maxWidth, maxHeight, quality)
    } else {
        //创建用于缩放的Options
        val options = BitmapOptions(path, maxWidth, maxHeight)
        //解析出图片
        val bitmap = DecodedBitmap(path, options) ?: return null
        //旋转图片
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val bitmapFixed = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return bitmapFixed.toByteArray(true, format, quality)
    }
}

/**
 * 通过需求的宽和高简单计算适当的InSampleSize
 *
 * @param imgWidth 图片宽
 * @param imgHeight 图片高
 * @param reqWidth  需求宽
 * @param reqHeight 需求高
 * @return
 */
fun InSampleSizeSimple(imgWidth: Int, imgHeight: Int, reqWidth: Int, reqHeight: Int): Int {
    val calResult = max(imgWidth * 1.0 / reqWidth, imgHeight * 1.0 / reqHeight).toInt()
    return max(1, calResult)
}


/**
 * 转换成字节数组
 * @param recycleSelf 是否需要在转换之后回收bitmap,如果bitmap在转换之后不需要再使用，则建议设置为true
 * @param format      转换格式，默认JPEG，这样转换之后的图片不包含透明像素，所占空间会更小（如果jpeg图片使用PNG格式转换，会导致转换之后的空间更大）
 * @param quality     转换质量（0-100取值），默认80
 * @return
 */
@JvmOverloads
fun Bitmap.toByteArray(recycleSelf: Boolean = false,
                       format: CompressFormat = CompressFormat.JPEG,
                       quality: Int = 80): ByteArray? {
    var output: ByteArrayOutputStream? = null
    var result: ByteArray? = null
    try {
        output = ByteArrayOutputStream()
        this.compress(format, quality, output)
        if (recycleSelf) {
            recycle()
        }
        result = output.toByteArray()
    } finally {
        output?.close()
    }
    return result
}

/**
 * 转换成Base64编码Stringx
 * @param format      转换格式，默认JPEG，这样转换之后的图片不包含透明像素，所占空间会更小（如果jpeg图片使用PNG格式转换，会导致转换之后的空间更大）
 * @param quality     转换质量（0-100取值），默认80
 * @return
 */
fun Bitmap.toBase64String(format: CompressFormat, quality: Int): String {
    return Base64.encodeToString(this.toByteArray(false, format, quality), Base64.DEFAULT)
}

/**
 * 转换成Drawable
 */
fun Bitmap.toDrawable(res: Resources): BitmapDrawable {
    return BitmapDrawable(res, this)
}

/**
 * 解析Bitmap的公用方法. 数据源只需提供一种
 */
fun DecodedBitmap(path: String, options: BitmapFactory.Options): Bitmap? {
    return BitmapFactory.decodeFile(path, options)
}

/**
 * 获取缩放之后的图片
 * @param maxWidth 目标宽度
 * @param maxHeight 目标高度
 * @param imagePath 图片路径
 * @param config 压缩配置，默认[Bitmap.Config.RGB_565],所占内存最少
 */
fun DecodedBitmap(imagePath: String,
                  maxWidth: Int,
                  maxHeight: Int,
                  config: Bitmap.Config = Bitmap.Config.RGB_565): Bitmap? {
    val bmOptions = BitmapOptions(imagePath, maxWidth, maxHeight, config)
    return DecodedBitmap(imagePath, bmOptions)
}

/**
 * 解析Bitmap的公用方法. 数据源只需提供一种
 */
fun DecodedBitmap(data: ByteArray, options: BitmapFactory.Options): Bitmap? {
    return BitmapFactory.decodeByteArray(data, 0, data.size, options)
}

/**
 * 解析Bitmap的公用方法. 数据源只需提供一种
 */
fun DecodedBitmap(ctx: Context, uri: Uri, options: BitmapFactory.Options): Bitmap? {
    return ctx.contentResolver.openInputStream(uri)?.use {
        BitmapFactory.decodeStream(it, null, options)
    }
}

/**
 * 获取图片的BitmapFactory.Options
 */
fun BitmapOptions(path: String): BitmapFactory.Options {
    val optionsInfo = BitmapFactory.Options()
    // 这里设置true的时候，decode时候Bitmap返回的为空，
    // 将图片宽高读取放在Options里.
    optionsInfo.inJustDecodeBounds = true
    DecodedBitmap(path, optionsInfo)
    return optionsInfo
}

/**
 * 获取图片的BitmapFactory.Options
 */
fun BitmapOptions(data: ByteArray): BitmapFactory.Options {
    val optionsInfo = BitmapFactory.Options()
    // 这里设置true的时候，decode时候Bitmap返回的为空，
    // 将图片宽高读取放在Options里.
    optionsInfo.inJustDecodeBounds = true
    DecodedBitmap(data, optionsInfo)
    return optionsInfo
}

/**
 * 获取图片的BitmapFactory.Options
 */
fun BitmapOptions(ctx: Context, imageUri: Uri): BitmapFactory.Options {
    val optionsInfo = BitmapFactory.Options()
    // 这里设置true的时候，decode时候Bitmap返回的为空，
    // 将图片宽高读取放在Options里.
    optionsInfo.inJustDecodeBounds = true
    DecodedBitmap(ctx, imageUri, optionsInfo)
    return optionsInfo
}

/**
 * 创建一个合适的用于获取图片的BitmapFactory.Options
 *
 * @param context
 * @param path
 * @param width
 * @param height
 * @param jpegQuality
 * @return
 */
fun BitmapOptions(path: String,
                  width: Int,
                  height: Int,
                  config: Bitmap.Config = Bitmap.Config.ARGB_8888): BitmapFactory.Options {
    val options = BitmapOptions(path)
    options.inSampleSize = InSampleSizeSimple(options.outWidth, options.outHeight, width, height)
    options.inJustDecodeBounds = false
    if (Build.VERSION.SDK_INT < 21) {
        options.inPurgeable = true
        options.inInputShareable = true
    }
    return options
}

/**
 * 获取图片的旋转角度
 * @param path 图片路径
 * @return 图片旋转的角度值（0,90,180,270）
 */
fun ImageRotateDegree(path: String): Int {
    return try {
        val exifInterface = ExifInterface(path)
        val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        when (orientation) {
            ExifInterface.ORIENTATION_TRANSPOSE, ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180, ExifInterface.ORIENTATION_FLIP_VERTICAL -> 180
            ExifInterface.ORIENTATION_TRANSVERSE, ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    } catch (e: Exception) {
        0
    }
}

/**
 * 混合logo(居中混合，小图放在大图中间)
 * 使用场景：二维码中间贴上logo
 * @param markBmp 小图
 * @param config 建议[Bitmap.Config.RGB_565],占用的内存更少
 * @return 混合之后的图片
 */
fun Bitmap.mixtureCenterBitmap(markBmp: Bitmap,
                               config: Bitmap.Config): Bitmap? {

    val sW = this.width
    val sH = this.height
    val mW = markBmp.width
    val mH = markBmp.height
    val newBmp = Bitmap.createBitmap(sW, sH, config)
    val cv = Canvas(newBmp)
    cv.drawBitmap(this, 0f, 0f, null)
    cv.drawBitmap(markBmp, (sW / 2 - mW / 2).toFloat(), (sH / 2 - mH / 2).toFloat(), null)
    cv.save()
    cv.restore()
    return newBmp
}

/**
 * 生成文字图片
 * @param texString 文本内容
 * @param textSize 文本大小
 * @param textColor 文本颜色
 */
fun TextBitmap(texString: String,
               textSize: Float,
               @ColorInt textColor: Int): Bitmap {
    val paint = TextPaint()
    paint.isAntiAlias = true
    paint.textSize = textSize
    paint.color = textColor

    val fm = paint.fontMetrics
    // 获取文本宽高
    val textHeight = Math.ceil((fm.descent - fm.ascent).toDouble()).toInt() + 2
    val textWidth = paint.measureText(texString).toInt() + 6
    return TextBitmap(textWidth, textHeight, texString, paint)
}

/**
 * 生成文字图片
 * @param width 图片宽度
 * @param height 图片高度
 * @param text 图片内容
 * @param paint 文字画笔
 * @param config 图片设置
 */
@JvmOverloads
fun TextBitmap(width: Int, height: Int, text: String, paint: TextPaint, config: Bitmap.Config = Bitmap.Config.ARGB_4444): Bitmap {
    val newBitmap = Bitmap.createBitmap(width, height, config)
    val canvas = Canvas(newBitmap)
    val sl = StaticLayout(text, paint, newBitmap.width,
            Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false)
    sl.draw(canvas)
    return newBitmap
}

/**
 * 保存为文件
 *
 * @param bitmap
 * @param path                    文件路径
 * @param format                  压缩格式
 * @param quality                 压缩质量
 * @return true:保存成功
 */
@JvmOverloads
fun Bitmap.saveToFile(path: String,
                      format: CompressFormat = CompressFormat.JPEG,
                      quality: Int = 100): Boolean {

    val file = File(path)

    val prentFile = file.parentFile
    if (prentFile != null && !prentFile.exists()) {
        prentFile.mkdirs()
    }
    //删除原有文件
    if (file.exists()) {
        file.delete()
    }
    file.createNewFile()

    return try {
        val out = FileOutputStream(file)
        compress(format, quality, out)
        out.flush()
        out.close()
        true
    } catch (e: Exception) {
        if (file.exists())
            file.deleteOnExit()
        false
    }

}
