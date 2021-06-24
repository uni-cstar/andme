package andme.integration.oss

import androidx.annotation.WorkerThread
import com.alibaba.sdk.android.oss.OSS
import com.alibaba.sdk.android.oss.model.ObjectMetadata
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult

/**
 * Created by Lucio on 2021/6/21.
 */
abstract class AliOssClient {

    @Volatile
    protected var oss: OSS? = null

    @Synchronized
    suspend fun getClient(): OSS {
        if (oss == null) {
            initClientSync()
        }
        return oss!!
    }

    /**
     * 初始化或者更新客户端凭证
     */
    abstract suspend fun initClientSync()

    /**
     * 连接超时，默认15秒
     */
    protected open fun getClientConnectionTimeout(): Int {
        return 15 * 1000
    }

    /**
     * socket超时，默认15秒
     */
    protected open fun getClientSocketTimeout(): Int {
        return 15 * 1000
    }

    /**
     * 最大并发请求书，默认5个
     */
    protected open fun getMaxConcurrentRequest(): Int {
        return 5
    }

    /**
     * 失败后最大重试次数，默认2次
     */
    protected open fun getMaxErrorRetry(): Int {
        return 2
    }

    /**
     * 同步方式上传文件
     */
    @WorkerThread
    suspend fun upload(
        bucketName: String,
        objectName: String,
        uploadFilePath: String,
        metadata: ObjectMetadata? = null,
    ): PutObjectResult {
        val client = getClient()
        // 构造上传请求。
        val put = PutObjectRequest(bucketName, objectName, uploadFilePath)
        metadata?.let {
            // 文件元信息的设置是可选的。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setContentType("application/octet-stream"); // 设置content-type。
            // metadata.setContentMD5(BinaryUtil.calculateBase64Md5(uploadFilePath)); // 校验MD5。
            // put.setMetadata(metadata);
            put.metadata = it
        }
        return client.putObject(put)
    }


}