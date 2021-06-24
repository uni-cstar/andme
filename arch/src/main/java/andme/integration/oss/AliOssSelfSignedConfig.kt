package andme.integration.oss

/**
 * Created by Lucio on 2021/6/21.
 */
interface AliOssSelfSignedConfig {
    /**
     * 过期时间 ms
     */
    val expirationConfig: Long

    val accessKeyIdConfig: String
    val secretKeyIdConfig: String
    val securityTokenConfig: String

    val endpointConfig:String

    val bucketNameConfig:String
}