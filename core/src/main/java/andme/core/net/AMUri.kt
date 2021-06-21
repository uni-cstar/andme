@file:JvmName("AMUri")

package andme.core.net

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.BaseColumns
import android.provider.DocumentsContract
import android.provider.DocumentsProvider
import android.provider.MediaStore

/**
 * Created by Lucio on 2018/7/3.
 */

private const val PATH_DOCUMENT = "document"

/**
 * 获取Uri对应的真实路径
 * 兼容4.4、5.0
 */
fun Uri.getRealPath(ctx: Context): String? {

    if (Build.VERSION.SDK_INT >= 19 && isDocumentUri) { // DocumentProvider
        if (isExternalStorageDocument) {    // ExternalStorageProvider
            val docId = documentId
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }
        } else if (isDownloadsDocument) {   // DownloadsProvider
            val id = documentId
            if (id.startsWith("raw:")) {
                /**
                 * 修正华为mate10上选择pdf文件，返回的uri形式为
                 * content://com.android.providers.downloads.documents/document/raw:/storage/emulated/0/Download/browser/xxx.pdf，
                 * 导致出现异常java.lang.NumberFormatException: For input string: raw:/storage/emulated/0/Download/browser/xxx.pdf
                 */
                //去掉raw:部分
                val tempId = id.replaceFirst("raw:", "")
                try {
                    //尝试转换成long型id操作，如果失败，直接返回后续的路径
                    val parseId = tempId.toLong()
                    val contentUri =
                        ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                            parseId)
                    return contentUri.queryDateColumn(ctx, null, null)
                } catch (e: Exception) {
                    return tempId
                }
            } else {
                val contentUri =
                    ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        id.toLong())
                return contentUri.queryDateColumn(ctx, null, null)
            }
        } else if (isMediaDocument) {   // MediaProvider
            val docId = documentId
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
            val selection = BaseColumns._ID + "=?"
            val selectionArgs = arrayOf(split[1])
            return contentUri?.queryDateColumn(ctx, selection, selectionArgs)
        }

    } else if (ContentResolver.SCHEME_CONTENT.equals(scheme, ignoreCase = true)) {
        // Return the remote address
        return if (isGooglePhotosUri) lastPathSegment else queryDateColumn(ctx, null, null)
    } else if ("file".equals(scheme, ignoreCase = true)) {  // File
        return path
    }
    // MediaStore (and general)

    return null
}

/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.
 * 获取Uri数据列对应的值，这个对于MediaStore Uri和其他基于文件的ContentProvider
 *
 * @param context       The context.
 * @param selection     (Optional) Filter used in the query.
 * @param selectionArgs (Optional) Selection arguments used in the query.
 */
fun Uri.queryDateColumn(
    context: Context,
    selection: String?,
    selectionArgs: Array<String>?,
): String? {
    val dataColumn = "_data"
    val projection = arrayOf(dataColumn)
    return context.contentResolver.query(this, projection, selection, selectionArgs, null)?.use {
        if (it.moveToFirst()) {
            return it.getString(it.getColumnIndexOrThrow(dataColumn))
        } else {
            null
        }
    }
}


/**
 * Test if the given URI represents a [DocumentsContract.Document] backed by a
 * [DocumentsProvider].
 */
private val Uri.isDocumentUri: Boolean
    get() {
        val paths = pathSegments
        return if (paths.size < 2) {
            false
        } else PATH_DOCUMENT == paths[0]
    }

private val Uri.documentId: String
    get() {
        val paths = pathSegments
        require(paths.size >= 2) { "Not a document: $this" }
        require(PATH_DOCUMENT == paths[0]) { "Not a document: $this" }
        return paths[1]
    }


/**
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
val Uri.isExternalStorageDocument: Boolean get() = "com.android.externalstorage.documents" == authority

/**
 * @return Whether the Uri authority is DownloadsProvider.
 */
val Uri.isDownloadsDocument: Boolean get() = "com.android.providers.downloads.documents" == authority

/**
 * @return Whether the Uri authority is MediaProvider.
 */
val Uri.isMediaDocument: Boolean get() = "com.android.providers.media.documents" == authority

/**
 * @return Whether the Uri authority is Google Photos.
 */
val Uri.isGooglePhotosUri: Boolean get() = "com.google.android.apps.photos.content" == authority
