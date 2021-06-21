@file:JvmName("Encoders")

package andme.lang

import android.net.Uri

import java.security.KeyFactory
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.PublicKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

/**
 * Created by Lucio on 2020/12/13.
 */
/**
 *  转换成MD5字符串（标准MD5加密算法）
 */
fun String?.toMd5(): String {
    if (this.isNullOrEmpty())
        return ""
    var bytes = this.toByteArray(Charsets.UTF_8)
    val md5 = MessageDigest.getInstance("MD5")
    bytes = md5.digest(bytes)

    val result = StringBuilder()
    for (item in bytes) {
        val hexStr = Integer.toHexString(0xFF and item.toInt())
        if (hexStr.length < 2) {
            result.append("0")
        }
        result.append(hexStr)
    }
    return result.toString()
}


/**
 * url编码
 * @see [BestPerformance]
 * @param allow set of additional characters to allow in the encoded form,
 *  null if no characters should be skipped
 * @return an encoded version of s suitable for use as a URI component,
 *  or null if s is null
 */
@Note(
    "在url编码中，我门通常使用的java.net.URLEncoder.encode方法进行url编码，此方法存在一个问题就是会将空格转换成＋，导致iOS那边无法将＋解码成空格" +
            "详细请见：https://www.jianshu.com/p/4a7eb969235d"
)
@JvmOverloads
inline fun String.urlEncode(allow: String? = null): String {
    return Uri.encode(this, allow)
}

/**
 * url解码
 */
inline fun String.urlDecode(): String {
    return Uri.decode(this)
}


/**RSA 标准加密方式*/
const val RSA_TRANSFORMATION_ANDROID_DEFAULT =  "RSA/None/PKCS1Padding"
const val RSA_TRANSFORMATION_ECB =  "RSA/ECB/PKCS1Padding"//

/**
 * 格式化公钥
 */
private fun formatRSAPublicKey(publicKey: String):String{
    return publicKey.replace("\r\n", "").replace("\r", "").replace("\n", "")
}

/**
 * RSA 根据公钥加密
 * @param data 数据
 * @param publicKey 公钥
 * @param transformation 加密方式，见[Cipher.getInstance]
 */
@JvmOverloads
@Throws(Exception::class)
fun RSAEncrypt(
    data: ByteArray,
    publicKey: ByteArray,
    transformation: String = RSA_TRANSFORMATION_ANDROID_DEFAULT
): ByteArray {
    // 得到公钥对象
    val keySpec = X509EncodedKeySpec(publicKey)
    val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
    val pubKey: PublicKey = keyFactory.generatePublic(keySpec)
    // 加密数据
    val cp: Cipher = Cipher.getInstance(transformation)
    cp.init(Cipher.ENCRYPT_MODE, pubKey)
    return cp.doFinal(data)
}

/**
 * RSA 根据私钥解密
 */
@Throws(Exception::class)
fun RSADecrypt(
    encrypted: ByteArray,
    key: ByteArray,
    transformation: String = RSA_TRANSFORMATION_ANDROID_DEFAULT
): ByteArray {
    // 得到私钥对象
    val keySpec = PKCS8EncodedKeySpec(key)
    val kf = KeyFactory.getInstance("RSA")
    val privateKey: PrivateKey = kf.generatePrivate(keySpec)
    // 解密数据
    val cp = Cipher.getInstance(transformation)
    cp.init(Cipher.DECRYPT_MODE, privateKey)
    return cp.doFinal(encrypted)
}

/**
 * rsa加密
 */
fun ByteArray.toRSAEncrypt(
    publicKey: ByteArray,
    transformation: String = RSA_TRANSFORMATION_ANDROID_DEFAULT
): ByteArray {
    return RSAEncrypt(this, publicKey, transformation)
}

/**
 * rsa加密
 */
@JvmOverloads
fun String.toRSAEncrypt(
    publicKey: String,
    transformation: String = RSA_TRANSFORMATION_ANDROID_DEFAULT
): String {
    val keyBytes = android.util.Base64.decode(
        formatRSAPublicKey(publicKey),
        android.util.Base64.DEFAULT
    )
    val encrypted =  this.toByteArray().toRSAEncrypt(keyBytes, transformation)
    return android.util.Base64.encodeToString(encrypted, android.util.Base64.NO_WRAP)
}

/**
 * rsa解密
 */
fun ByteArray.toRSADecrypt(
    key: ByteArray,
    transformation: String = RSA_TRANSFORMATION_ANDROID_DEFAULT
): ByteArray {
    return RSADecrypt(this, key, transformation)
}

/**
 * rsa解密
 */
@JvmOverloads
fun String.toRSADecrypt(key: String, transformation: String = RSA_TRANSFORMATION_ANDROID_DEFAULT): String {
    return String(this.toByteArray().toRSADecrypt(key.toByteArray(), transformation))
}