package andme.tv.player.gsy

import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager

/**
 * Created by Lucio on 2021/6/1.
 */
object GsyPlayer {

    fun setup(){
        /*铺满模式*/
        GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL)
        //texture播放某些视频，会存在颜色分层。。
        GSYVideoType.setRenderType(GSYVideoType.SUFRACE)
        //使用硬解码
        GSYVideoType.enableMediaCodec()
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
    }
}