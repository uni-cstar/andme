/**
 * Created by Lucio on 2020-03-07.
 */
@file:JvmName("StringsKm")
@file:JvmMultifileClass

package andme.lang

import kotlin.contracts.ExperimentalContracts

/**
 * null或空字符串时使用默认值
 */
inline fun String?.orDefaultIfNullOrEmpty(def: String = ""): String = if (this.isNullOrEmpty()) def else this

@JvmOverloads
fun String?.toLongOrDefault(defaultValue: Long = 0): Long {
    return try {
       this?.toLongOrNull().orDefault(defaultValue)
    } catch (_: NumberFormatException) {
        defaultValue
    }
}

@JvmOverloads
fun String?.toIntOrDefault(defaultValue: Int = 0): Int {
    return try {
        this?.toIntOrNull().orDefault(defaultValue)
    } catch (_: NumberFormatException) {
        defaultValue
    }
}


/**
 * 用于Java的工具函数(kt的直接用库函数[kotlin.text.isNullOrEmpty])
 */
@UseExperimental(ExperimentalContracts::class)
fun CharSequence?.isNullOrEmptyJava(): Boolean {
    kotlin.contracts.contract {
        returns(false) implies (this@isNullOrEmptyJava != null)
    }

    return this == null || this.isEmpty()
}

/**
 * Returns this String if it's not `null` and the "" otherwise.
 */
inline fun String?.orEmptyJava():String = orEmpty()

/**
 * 首字母大写
 */
fun String.toFirstLetterUpperCase(): String {
    if (this.isEmpty() || this.isBlank())
        return this

    val c = this[0]
    //如果第一个字符不是字母或者已经是大写字母，则返回原字符串
    if (!Character.isLetter(c) || Character.isUpperCase(c)) {
        return this
    }
    return StringBuilder(this.length)
            .append(Character.toUpperCase(c))
            .append(this.substring(1))
            .toString()
}

/**
 * 全拼转半拼
 * fullWidthToHalfWidth("") = "";
 * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
 * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
 * @return
 */
fun String.toHalfWidth(): String {
    if (this.isEmpty()) {
        return this
    }

    val source = this.toCharArray()
    for (i in source.indices) {
        if (source[i].toInt() == 12288) {
            source[i] = ' '
        } else if (source[i].toInt() in 65281..65374) {
            source[i] = (source[i].toInt() - 65248).toChar()
        } else {
            source[i] = source[i]
        }
    }
    return String(source)
}

/**
 * 半拼转全拼
 * halfWidthToFullWidth("") = "";
 * halfWidthToFullWidth(" ") = new String(new char[] {12288});
 * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
 * @param s
 * @return
 */
fun String.toFullWidth(): String {
    if (this.isEmpty()) {
        return this
    }
    val source = this.toCharArray()
    for (i in source.indices) {
        if (source[i] == ' ') {
            source[i] = 12288.toChar()
        } else if (source[i].toInt() in 33..126) {
            source[i] = (source[i].toInt() + 65248).toChar()
        } else {
            source[i] = source[i]
        }
    }
    return String(source)
}

/**
 * 获取扩展名（Returns the extension of this String (not including the [delimiter]), or a [defValue] string if it doesn't have one.）
 * @param delimiter 扩展符号
 * @param defValue 默认值；未找到扩展符号时使用的默认值
 */
@JvmOverloads
fun String.getExtension(delimiter: String = ".", defValue: String = "") = substringAfterLast(delimiter, defValue)

/**
 * 获取不包含扩展名的名字
 */
@JvmOverloads
fun String.getNameWithoutExtension(delimiter: String = "."): String = substringBeforeLast(delimiter)
