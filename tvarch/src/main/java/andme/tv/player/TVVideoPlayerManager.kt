package andme.tv.player

import com.shuyu.gsyvideoplayer.player.PlayerFactory
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager


/**
 * Created by Lucio on 2021/4/1.
 */
object TVVideoPlayerManager {

    fun init() {
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
    }
}