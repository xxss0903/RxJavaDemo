package com.example.zack.rxjavademo.libphonenumberdemo

import android.util.Log
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import com.hsbc.mobileXNative.fps.util.ProxyIdValidator
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_libphonenumber.*
import java.util.*

/**
 * Created by zack zeng on 2018/5/11.
 */

enum class ProxyIdEnum {
    FPSID,
    PHONENUMBER,
    SEARCHCOUNTRY,
    EMAIL,
    UNKNOWN
}

class MatchResult {
    var content: String = ""
    var type: ProxyIdEnum = ProxyIdEnum.UNKNOWN
}

class SuperFieldMatcher {

    val TAG = "SuperFieldMatcher"
    val EMAIL_TAG = "@"

    companion object {
        val instance = SuperFieldMatcher()
    }

    fun parse(content: String): Observable<MatchResult> {
        val result = MatchResult()
        when {
            matchEmail(content) -> {
                Log.e(TAG, "is email $content")
                result.type = ProxyIdEnum.EMAIL
                result.content = content
            }
            matchPhoneNumber(content) -> {
                Log.e(TAG, "is phone number $content")
                val phoneNumber = matchAccuratePhoneNumber(content)
                if (phoneNumber == null) {
                    if (matchFpsId(content)) {
                        result.type = ProxyIdEnum.FPSID
                        result.content = content
                    } else {
                        result.type = ProxyIdEnum.UNKNOWN
                        result.content = content
                    }
                } else {
                    result.type = ProxyIdEnum.PHONENUMBER
                    result.content = getFormattedPhoneNumber(phoneNumber)
                }
            }
            else -> {
                // do nothing
            }
        }

        return Observable.just(result)
    }

    private fun matchAccuratePhoneNumber(content: String): Phonenumber.PhoneNumber? {
        if (content.startsWith("852") || content.startsWith("+852")) {
            Log.e(TAG, "hong kong number")
            if (ProxyIdValidator.isValidHKMobileNum(content)) {
                val number = content.replaceFirst("852", "").replace("+", "").replace("-", "")
                val phoneNumber = Phonenumber.PhoneNumber()
                phoneNumber.countryCode = 852
                phoneNumber.nationalNumber = number.toLong()
                return phoneNumber
            } else {
                return null
            }
        } else {
            if (ProxyIdValidator.isAllNumeric(content)) {
                if (!ProxyIdValidator.isFpsId(content) && ProxyIdValidator.isValidHKMobileNum(content)) {
                    return PhoneNumberUtil.getInstance().parse(content, "HK")
                } else if (content.length > 7) {
                    // show country code selection list
                    searchPhoneNumberCountry(content)
                }
            } else {
                val number = content.replace("+", "")
                val numberList = number.split("-")
                var countryCode: String = ""
                var nationalNumber: String = ""
                if (numberList.size == 1) {
                    nationalNumber = numberList[0]
                } else if (numberList.size == 2) {
                    countryCode = numberList[0]
                    nationalNumber = numberList[1]
                }
                if (countryCode.isNotBlank()) {
                    val phoneNumber = Phonenumber.PhoneNumber()
                    phoneNumber.countryCode = countryCode.toInt()
                    phoneNumber.nationalNumber = nationalNumber.toLong()
                    return phoneNumber
                } else {
                    // shouw country code selection list
                    searchPhoneNumberCountry(content)
                }
            }
        }
        return null
    }

    private fun matchPhoneNumber(content: String): Boolean {
        return ProxyIdValidator.isMayBePhoneNumber(content)
    }

    private fun matchFpsId(content: String): Boolean {
        return ProxyIdValidator.isFpsId(content)
    }

    private fun matchEmail(content: String): Boolean {
        return ProxyIdValidator.isValidEmail(content)
    }

    fun getFormattedPhoneNumber(phoneNumber: Phonenumber.PhoneNumber): String {
        return "+${phoneNumber.countryCode}-${phoneNumber.nationalNumber}"
    }

    private val countryList: MutableList<Country>
        get() {
            return getSupportedCountryList()
        }

    private fun getSupportedCountryList(): MutableList<Country> {
        val supportedRegions = PhoneNumberUtil.getInstance().supportedRegions
        val tmpCountryList: MutableList<Country> = mutableListOf()
        for (supportedRegion in supportedRegions) {
            val countryCode = PhoneNumberUtil.getInstance().getCountryCodeForRegion(supportedRegion)
            val fullName = Locale("en", supportedRegion).getDisplayCountry(Locale.ENGLISH) + "(+$countryCode)"
            tmpCountryList.add(Country(supportedRegion, fullName, countryCode))
        }

        // resort by country full name
        Collections.sort(tmpCountryList, object : Comparator<Country> {
            override fun compare(o1: Country?, o2: Country?): Int {
                if (o1 == null || o2 == null) {
                    return 0
                }
                return o1.fullName.compareTo(o2.fullName)
            }
        })
        return tmpCountryList
    }

    private fun searchPhoneNumberCountry(phone: String) {
        try {
            val phoneNumberLong = phone.toLong()
            val phoneNumber = Phonenumber.PhoneNumber()
            val fitCountryList: MutableList<Country> = mutableListOf()
            for (country in countryList) {
                phoneNumber.nationalNumber = phoneNumberLong
                phoneNumber.countryCode = country.codeInt
                if (PhoneNumberUtil.getInstance().isValidNumber(phoneNumber)) {
                    fitCountryList.add(country)
                }
            }
            var info = ""
            for (country in fitCountryList) {
                info += "当前号码:${country.fullName}  +${country.codeInt}-$phone" + "\n"
            }
            Log.d("PhoneNumber", info)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }


}