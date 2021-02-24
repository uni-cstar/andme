package andme.core.media

import andme.core.mApp
import android.media.Ringtone
import android.media.RingtoneManager

/**
 * Created by Lucio on 2021/2/19.
 * 铃声管理
 */
object AMRingtoneManager {

    /**
     * 播放通知声音
     */
    @JvmStatic
    fun playNotification(): Ringtone? {
        var notificationTone = RingtoneManager.getActualDefaultRingtoneUri(mApp, RingtoneManager.TYPE_NOTIFICATION)
        if (notificationTone == null) {
            notificationTone = RingtoneManager.getValidRingtoneUri(mApp)
        }
        val r = RingtoneManager.getRingtone(mApp, notificationTone)
        r?.play()
        return r
    }

    /**
     * 播放系统默认通知声音
     */
    @JvmStatic
    fun playDefaultNotification(): Ringtone? {
        var notificationTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        if (notificationTone == null) {
            notificationTone = RingtoneManager.getValidRingtoneUri(mApp)
        }
        val r = RingtoneManager.getRingtone(mApp, notificationTone)
        r?.play()
        return r
    }
}