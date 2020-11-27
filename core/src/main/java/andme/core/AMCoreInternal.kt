@file:JvmName("AMCore")
@file:JvmMultifileClass

package andme.core

import andme.integration.media.MediaStore
import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Lucio on 2020/11/23.
 */

internal var mMediaStore: MediaStore? = null

//默认集成库包名路径
internal const val INTEGRATION_PKG_NAME = "andme.integration.support"


private const val PREF_FILE_NAME = "pig_pref"

internal const val DEVICE_UNIQUE_ID = "pig_device_unique_id"

/**
 * 私有的
 */
internal val sharedPrefAM: SharedPreferences by lazy {
    mApp.applicationContext.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
}
