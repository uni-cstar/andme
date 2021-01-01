package andme.integration.media

import android.app.Activity
import androidx.annotation.IntRange
import androidx.fragment.app.Fragment

/**
 * Created by Lucio on 2020-11-10.
 * 图片选取/拍摄支持
 */
interface MediaStore {
    /**
     * 单图选择器
     */
    
    fun singlePicSelector(activity: Activity): PictureSelector

    /**
     * 单图选择器
     */
    
    fun singlePicSelector(fragment: Fragment): PictureSelector

    /**
     * 正方形头像图片选择器
     */
    
    fun squarePicSelector(activity: Activity, maxSize: Int = 240): PictureSelector

    /**
     * 正方形头像图片选择器
     */
    
    fun squarePicSelector(fragment: Fragment, maxSize: Int = 240): PictureSelector

    /**
     * 圆形头像图片选择器
     */
    
    fun circlePicSelector(activity: Activity, maxSize: Int = 240): PictureSelector

    /**
     * 圆形头像图片选择器
     */
    
    fun circlePicSelector(fragment: Fragment, maxSize: Int = 240): PictureSelector

    /**
     * 多图选择器
     * @param minSelectCount 最少选择数量
     * @param maxSelectCount 最多选择数量
     */
    
    fun multiPicSelector(activity: Activity, @IntRange(from = 0, to = Long.MAX_VALUE) minSelectCount: Int,
                         @IntRange(from = 0, to = Long.MAX_VALUE) maxSelectCount: Int): PictureSelector

    /**
     * 多图选择器
     * @param minSelectCount 最少选择数量
     * @param maxSelectCount 最多选择数量
     */
    
    fun multiPicSelector(fragment: Fragment, @IntRange(from = 0, to = Long.MAX_VALUE) minSelectCount: Int,
                         @IntRange(from = 0, to = Long.MAX_VALUE) maxSelectCount: Int): PictureSelector
}