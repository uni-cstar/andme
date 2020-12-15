@file:JvmName("StringsKm")
@file:JvmMultifileClass
//包名不能变
package andme.lang

import android.webkit.MimeTypeMap

/**
 * Created by Lucio on 2020-03-09.
 */

@JvmOverloads
fun String.getMimeType(defValue: String = "file/*"): String {
    val suffix = this.getExtension()
    if (suffix.isEmpty())
        return defValue
    val type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix)
    if (!type.isNullOrEmpty()) {
        return type
    }
    return defValue
}