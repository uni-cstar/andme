package andme.integration.oss

import andme.core.ctxAM
import andme.lang.orDefault
import androidx.annotation.WorkerThread
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.common.OSSLog
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.model.ObjectMetadata
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult

/**
 * Created by Lucio on 2021/6/21.
 * 阿里OSS 自签模式
 */
abstract class AliOssSelfSignedClient : AliOssClient() {

    @Volatile
    protected var config: AliOssSelfSignedConfig? = null
        private set

    private val isConfigInvalidate: Boolean get() = config?.expirationConfig.orDefault() <= System.currentTimeMillis()

    override suspend fun initClientSync() {
        //在有效时间内不刷新
        if (isConfigInvalidate) { //本地不存在缓存配置 或 缓存配置过期失效
            config = requestOSSConfig()
        }
        val credentialProvider = OSSStsTokenCredentialProvider(config!!.accessKeyIdConfig,
            config!!.secretKeyIdConfig,
            config!!.securityTokenConfig)
        val conf = ClientConfiguration()
        conf.connectionTimeout = getClientConnectionTimeout()
        conf.socketTimeout = getClientSocketTimeout()
        conf.maxConcurrentRequest = getMaxConcurrentRequest() //
        conf.maxErrorRetry = getMaxErrorRetry()
        OSSLog.enableLog()
        if (oss == null) {
            oss = OSSClient(ctxAM, config!!.endpointConfig, credentialProvider, conf)
        } else {
            oss!!.updateCredentialProvider(credentialProvider)
        }
    }

    /**
     * 请求凭证配置信息
     */
    @WorkerThread
    protected abstract suspend fun requestOSSConfig(): AliOssSelfSignedConfig


//    /**
//     * 同步方式上传文件
//     */
//    @WorkerThread
//    suspend fun upload(
//        objectName: String,
//        uploadFilePath: String,
//        metadata: ObjectMetadata? = null,
//    ): PutObjectResult {
//        val client = getClient()
//        // 构造上传请求。
//        val put = PutObjectRequest(config!!.bucketNameConfig, objectName, uploadFilePath)
//        metadata?.let {
//            // 文件元信息的设置是可选的。
//            // ObjectMetadata metadata = new ObjectMetadata();
//            // metadata.setContentType("application/octet-stream"); // 设置content-type。
//            // metadata.setContentMD5(BinaryUtil.calculateBase64Md5(uploadFilePath)); // 校验MD5。
//            // put.setMetadata(metadata);
//            put.metadata = it
//        }
//        return client.putObject(put)
//    }
//
//
//    //阻塞方法
//    fun createResumableUploadTask(
//        bucket: String?,
//        `object`: String?,
//        uploadFilePath: String?,
//    ): OSSAsyncTask? {
//        // 断点上传
//        val uploadRecordDirectory: String = AliOssUtil.getOssRecordDirPath(CoreApp.instance())
//        if (oss == null) {
//            throw RuntimeException("please call initOSSClientSync first")
//        }
//        return ResuambleUpload(oss,
//            bucket,
//            `object`,
//            uploadFilePath,
//            uploadRecordDirectory).resumableUploadWithRecordPathSetting()
//    }
//
//    fun getBucketName(): String? {
//        if (isConfigInvalidate()) {
//            if (Looper.myLooper() == Looper.getMainLooper()) {
//                //主线程不做阻塞操作
//                throw RuntimeException("do not call it in the main thread when oss hasn't initialized")
//            } else {
//                initSync()
//            }
//        }
//        return config.getBucketName()
//    }
//
//    fun getRootUrl(): String? {
//        if (isConfigInvalidate()) {
//            if (Looper.myLooper() == Looper.getMainLooper()) {
//                //主线程不做阻塞操作
//                throw RuntimeException("do not call it in the main thread when oss hasn't initialized")
//            } else {
//                initSync()
//            }
//        }
//        return config.getRootUrl()
//    }
//
//    private val DEFAULT_MAX_SIZE = 10 * 1024 * 1024
//
//    fun getUploadMaxSize(): Long {
//        if (isConfigInvalidate()) {
//            if (Looper.myLooper() == Looper.getMainLooper()) {
//                return DEFAULT_MAX_SIZE.toLong()
//            } else {
//                initSync()
//            }
//        }
//        return config.getMaxLenth() * 1024
//    }
//
//    /**
//     * ObjectKey 由服务端随机生成后返回
//     */
//    fun getObjectKey(): Observable<String?>? {
//        return BaseApi.getOSSObjectKey(getModulePath(), "", 1)
//            .flatMap(object : Func1<List<String?>?, Observable<String?>?>() {
//                fun call(list: List<String?>): Observable<String?>? {
//                    return if (CollectionsKm.isNullOrEmptyJava(list)) {
//                        Observable.empty()
//                    } else Observable.just(list[0])
//                }
//            })
//    }
}