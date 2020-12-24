package andme.integration.media

abstract class MediaFile {
    /**
     * 文件路径
     */
    abstract val filePath:String?

    /**
     * 压缩文件路径；设置了压缩的
     */
    abstract val compressPath:String?

    /**
     * 是否压缩
     */
    abstract val isCompress:Boolean

    /**
     * 裁剪文件的路径
     */
    abstract val cropPath:String?

    /**
     * 是否裁剪
     */
    abstract val isCrop:Boolean

    /**
     * 原图地址；勾选了原图选项时返回
     */
    abstract val originalPath:String?

    /**
     * 是否选中原图
     */
    abstract val isOriginal:Boolean

    /**
     * 文件名
     */
    abstract val fileName:String?

    /**
     * 目录名字
     */
    abstract val dirName:String?
}