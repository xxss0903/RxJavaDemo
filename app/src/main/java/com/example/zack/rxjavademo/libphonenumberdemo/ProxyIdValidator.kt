package com.hsbc.mobileXNative.fps.util

import java.util.regex.Pattern

/**
 * Created by 43974367 on 2018/1/9.
 */

object ProxyIdValidator {


    val numericPattern = Pattern.compile("[0-9]*")
    val emailPattern = Pattern.compile("^(([a-zA-Z0-9\\_\\-]+(\\.[a-zA-Z0-9\\_\\-]+)*)@([a-zA-Z0-9\\-]+\\.([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]{2,}))$")
    val hkMobilePattern1 = Pattern.compile("^852[456789]{1}[0-9]{7}")
    val hkMobilePattern2 = Pattern.compile("^852-[456789]{1}[0-9]{7}")
    val hkMobilePattern3 = Pattern.compile("^\\+852-[456789]{1}[0-9]{7}")
    val hkMobilePattern4 = Pattern.compile("^\\+852[456789]{1}[0-9]{7}")
    val hkMobilePattern5 = Pattern.compile("^[456789]{1}[0-9]{7}")
//    val newHkMobilePattern1 = Pattern.compile("^\\+(852[0-9]{8})|(852-[0-9]{8})$")
    val newHkMobilePattern = Pattern.compile("^\\+?(852(-?)[0-9]{8})$")
    val mobilePattern3 = Pattern.compile("^[0-9]*-[0-9]*$")
    val mobilePattern4 = Pattern.compile("^\\+[0-9]*-[0-9]*$")
    val mobilePattern5 = Pattern.compile("^\\+[0-9]*$")
    val alphanumericPattern = Pattern.compile("^[0-9a-zA-Z_@.]*$")
    val fpsIdPattern = Pattern.compile("^[0-9]{7}$")

    val MAX_LENGTH = 34

    enum class ValidationResult(val value: Int) {
        SUCCESS(0),
        INVALID_FPSID(1),
        INVALID_MOBILE_NUM(2),
        INVALID_EMAIL_FORMAT(3),
        EXCEED_MAXIMUM_LENGTH(4),
        ONLY_ALAPHA_NEMURIC_ALLOWED(5)
    }

    fun validateProxyId(input: String): ValidationResult {

        if (!isOnlyAlphaNumeric(input)) {
            return ValidationResult.ONLY_ALAPHA_NEMURIC_ALLOWED
        }

        if (input.length > MAX_LENGTH) {
            return ValidationResult.EXCEED_MAXIMUM_LENGTH

        }

        if (isAllNumeric(input)) {
            if (input.length < 7) {
                return ValidationResult.INVALID_FPSID
            } else if (input.length == 7) {
                return ValidationResult.SUCCESS
            } else {
                if (isValidHKMobileNum(input)) {
                    return ValidationResult.SUCCESS
                } else {
                    return ValidationResult.INVALID_MOBILE_NUM
                }
            }
        } else {
            if (isValidEmail(input)) {
                return ValidationResult.SUCCESS
            } else {
                return ValidationResult.INVALID_EMAIL_FORMAT
            }
        }
    }

    fun isMatchingExpectingPatterns(input: String, patterns: Array<Pattern>, matchAllRequired: Boolean): Boolean {

        if (matchAllRequired) {
            patterns.forEach { p ->
                val matchResult = p.matcher(input)
                if (!matchResult.matches()) {
                    return false
                }
            }
            return true
        } else {
            patterns.forEach { p ->
                val matchResult = p.matcher(input)
                if (matchResult.matches()) {
                    return true
                }
            }
            return false
        }
    }

    fun isOnlyAlphaNumeric(input: String): Boolean {
        return isMatchingExpectingPatterns(input, arrayOf(alphanumericPattern), true)
    }

    fun isAllNumeric(input: String): Boolean {
        return isMatchingExpectingPatterns(input, arrayOf(numericPattern), true)
    }

    fun isValidHKMobileNum(input: String): Boolean {
        return isMatchingExpectingPatterns(input, arrayOf(hkMobilePattern1, hkMobilePattern2, hkMobilePattern3, hkMobilePattern4, hkMobilePattern5), false)
    }

    fun isValidNewHkMobileNum(input: String): Boolean{
        return isMatchingExpectingPatterns(input, arrayOf(newHkMobilePattern), true)
    }

    fun isMayBePhoneNumber(input: String): Boolean {
        return isMatchingExpectingPatterns(input, arrayOf(mobilePattern3, mobilePattern4, mobilePattern5, numericPattern), false)
    }

    fun isValidEmail(input: String): Boolean {
        return isMatchingExpectingPatterns(input, arrayOf(emailPattern), true)
    }

    fun isFpsId(input: String): Boolean {
        return isMatchingExpectingPatterns(input, arrayOf(fpsIdPattern), true)
    }
}
