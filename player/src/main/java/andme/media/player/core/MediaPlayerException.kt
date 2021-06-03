package andme.media.player.core

/**
 * Created by Lucio on 2021/4/21.
 */
class MediaPlayerException(val what:Int, val extra:Int) : Throwable("what=${what} extra=${extra}")