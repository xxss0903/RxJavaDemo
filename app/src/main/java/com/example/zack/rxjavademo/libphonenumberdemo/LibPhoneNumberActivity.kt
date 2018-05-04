package com.example.zack.rxjavademo.libphonenumberdemo

import android.content.Context
import android.os.Bundle
import android.support.annotation.IntegerRes
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.zack.rxjavademo.R
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonemetadata
import com.google.i18n.phonenumbers.Phonenumber
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder
import com.hsbc.mobileXNative.fps.util.ProxyIdValidator
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.activity_libphonenumber.*
import kotlin.collections.LinkedHashMap

/**
 * Created by zack zeng on 2018/5/3.
 */
class LibPhoneNumberActivity : AppCompatActivity() {


    private var regionMap: LinkedHashMap<String, Int> = linkedMapOf()
    private var filterRegionMap: LinkedHashMap<String, Int> = linkedMapOf()
    private var countryRegionList: ArrayList<String> = arrayListOf()
//    private var filterRegionList: ArrayList<String> = arrayListOf()
//    private var countryCodeList: ArrayList<String> = arrayListOf()
//    private var countryDesList: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_libphonenumber)

        initDatas()
        initView()
    }

    private fun initDatas() {
        countryRegionList.addAll(PhoneNumberUtil.getInstance().supportedRegions.sorted())

        for (supportedRegion in countryRegionList) {
            val countryCode = PhoneNumberUtil.getInstance().getCountryCodeForRegion(supportedRegion)
            regionMap[supportedRegion] = countryCode
            Log.d("PhoneNumber", "Region: $supportedRegion  # Country Code: $countryCode")
        }
    }

    private lateinit var regionAdapter: ArrayAdapter<String>

    private lateinit var publishSubject: PublishSubject<String>
    private lateinit var compositeDisposable: CompositeDisposable

    private fun initView() {

        compositeDisposable = CompositeDisposable()
        publishSubject = PublishSubject.create()

        regionAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item)
        regionAdapter.addAll(regionMap.keys)
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_countrycode.adapter = regionAdapter
        sp_countrycode.setSelection(countryRegionList.indexOf("CN"))

        btn_parse.setOnClickListener {
            val phoneNumber = et_input.text
            if (phoneNumber.isNotBlank()) {
                parsePhoneNumber(phoneNumber)
            } else {
                Toast.makeText(this, "Please Input Phone Number", Toast.LENGTH_SHORT).show()
            }
        }

        et_country.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotBlank()) {
                    startMatch(s.toString())
                }
            }
        })
        val disposableObserver: DisposableObserver<Set<String>> = object : DisposableObserver<Set<String>>() {
            override fun onError(e: Throwable?) {
                Toast.makeText(this@LibPhoneNumberActivity, e?.message, Toast.LENGTH_SHORT).show()
            }

            override fun onNext(t: Set<String>?) {
                regionAdapter.clear()
                regionAdapter.addAll(t)
            }

            override fun onComplete() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
        publishSubject.debounce(200, TimeUnit.MILLISECONDS)
                .filter(object : Predicate<String> {
                    override fun test(t: String): Boolean {
                        return t.length > 0
                    }
                })
                .switchMap(object : Function<String, Observable<Set<String>>> {
                    override fun apply(t: String): Observable<Set<String>> {
                        filterRegionMap.clear()
                        for (entry in regionMap) {
                            if (judgeContainsRegion(t.toUpperCase().trim(), entry.key)) {
                                filterRegionMap[entry.key] = entry.value
                            }
                        }
                        return Observable.just(filterRegionMap.keys)
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver)

        compositeDisposable.add(disposableObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun startMatch(s: String) {
        publishSubject.onNext(s)
    }

    private fun judgeContainsRegion(key: String, target: String): Boolean {
        var countryName = Locale("en", target).getDisplayCountry(Locale.ENGLISH)
        countryName = countryName.replace(" ", "")

        Log.d("PhoneNumber", key + " compare : " + countryName + " #" + countryName.contains(key))

        return countryName.toUpperCase().replace(" ", "").contains(key.toUpperCase().replace(" ", ""))
    }

    private fun parsePhoneNumber(phoneNumberStr: Editable) {
        try {
            if (sp_countrycode.selectedItem.equals("HK")) {
                if (ProxyIdValidator.isValidMobileNum(phoneNumberStr.toString())) {
                    val phoneNumber = PhoneNumberUtil.getInstance().parse(phoneNumberStr, sp_countrycode.selectedItem as String?)
                    setPhoneNumber(phoneNumber)
                }
            } else {
                val phoneNumber = PhoneNumberUtil.getInstance().parse(phoneNumberStr, sp_countrycode.selectedItem as String?)
                val isValidPhoneNumber = validPhoneNumber(phoneNumber)
                if (isValidPhoneNumber) {
                    setPhoneNumber(phoneNumber)
                } else {
                    Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setPhoneNumber(phoneNumber: Phonenumber.PhoneNumber) {
        tv_international_format.text = "Phone Number: " + phoneNumber.nationalNumber
        tv_countrycode.text = "Country Code: " + filterRegionMap[sp_countrycode.selectedItem]
        tv_countryname.text = PhoneNumberOfflineGeocoder.getInstance().getDescriptionForValidNumber(phoneNumber, Locale.getDefault())
        if (phoneNumber.hasNumberOfLeadingZeros()) {
            tv_seperate_number.text = "+" + filterRegionMap[sp_countrycode.selectedItem] + " " + phoneNumber.numberOfLeadingZeros + phoneNumber.nationalNumber
        } else {
            tv_seperate_number.text = "+" + filterRegionMap[sp_countrycode.selectedItem] + " " + phoneNumber.nationalNumber
        }
    }

    private fun validPhoneNumber(phoneNumber: Phonenumber.PhoneNumber): Boolean {
        return PhoneNumberUtil.getInstance().isValidNumber(phoneNumber)
    }


}