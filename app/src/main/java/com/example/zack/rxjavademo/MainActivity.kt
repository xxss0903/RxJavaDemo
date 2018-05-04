package com.example.zack.rxjavademo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.example.zack.rxjavademo.delayhanlde.CheckContentDelay
import com.example.zack.rxjavademo.delayhanlde.CountdownCheck
import com.example.zack.rxjavademo.delayhanlde.OnCheckTextListener
import com.example.zack.rxjavademo.emvco.*
import com.example.zack.rxjavademo.libphonenumberdemo.LibPhoneNumberActivity
import com.example.zack.rxjavademo.location.MockLocationActivity
import com.example.zack.rxjavademo.merchantparser.CRC162
import com.example.zack.rxjavademo.merchantparser.CRC16Util
import com.example.zack.rxjavademo.merchantparser.CrcUtil
import com.example.zack.rxjavademo.merchantparser.EmvMerchant
import com.example.zack.rxjavademo.printscreen.LimitPrintScreenActivity
import com.example.zack.rxjavademo.qrscanner.QRScanTestActivity
import com.example.zack.rxjavademo.rxbus.MyEmitter
import com.example.zack.rxjavademo.rxbus.RxBus
import com.example.zack.rxjavademo.scheme.SchemeActivity
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

/**
 * main activity
 * Created by zack on 2018/1/22.
 */
class MainActivity : AppCompatActivity() {

    val delayMiniSeconds: Long = 3000
    lateinit var timerHandler: Handler
    lateinit var checkContent: Runnable
    lateinit var countdownCheck: CountdownCheck


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()

        testDelayEt()

        enterSecondActivity()

//        testRxJava()

//        testFlatMap()

        testSwitchMap()

//        testConcat()

        testSwitchMapBySwitchMap()

        testLimitPrintScreen()

        testSchemeOpen()

        parseQrcode()

        testCRC16()

        testEmvco()

        testMockLocation()

        testPhoneNumber()
    }

    private fun testPhoneNumber() {
        btn_libphonenumber.setOnClickListener {
            startActivity(Intent(this, LibPhoneNumberActivity::class.java))
        }
    }

    private fun testMockLocation() {
        btn_mock_location.setOnClickListener({
            startActivity(Intent(this, MockLocationActivity::class.java))
        })
    }

    private fun testEmvco() {
        testEncodeEmvco()
    }

    private fun testEncodeEmvco() {
        EmvMerchant.encode(null)
    }

    private fun testCRC16() {
//        val content = "00020101021229300012D156000000000510A93FO3230Q31280012D15600000001030812345678520441115802CN5914BEST TRANSPORT6007BEIJING64200002ZH0104最佳运输0202北京540523.7253031565502016233030412340603***0708A60086670902ME91320016A0112233449988770708123456786304"
        val content = "0002010102112600hk.com.hkicl01030030200030004000500520400005802NA5902NA6002NA5303344540522.220101-12345676304"
        val hexStr = HexUtil.strTo16(content)
//      val hexStr = "303030323031303130323132323933303030313244313536303030303030303030353130413933464F33323330513331323830303132443135363030303030303031303330383132333435363738353230343431313135383032434E3539313442455354205452414E53504F5254363030374245494A494E4736343230303030325A4830313034E69C80E4BDB3E8BF90E8BE9330323032E58C97E4BAAC3534303532332E373235333033313536353530323031363233333033303431323334303630332A2A2A303730384136303038363637303930324D4539313332303031364130313132323333343439393838373730373038313233343536373836333034"
//        val hexStr = "303030323031303130323132323933303030313244313536303030303030303030353130413933464F33323330513331323830303132443135363030303030303031303330383132333435363738353230343431313135383032434E3539313442455354205452414E53504F5254363030374245494A494E4736343230303030325A4830313034E69C80E4BDB3E8BF90E8BE9330323032E58C97E4BAAC3534303532332E373235333033313536353530323031363233333033303431323334303630332A2A2A303730384136303038363637303930324D4539313332303031364130313132323333343439393838373730373038313233343536373836333034"
        val hexArray = HexUtil.hexStringToByteArray(hexStr)

        Log.d("hexstr", "hex: " + hexStr)
        val result = CRC16Util.calcCrc16(hexArray)
        Log.d("hexstr", "crc16: " + result)
        val result2 = CRC16Util.makeCrc(hexArray)
        Log.d("hexstr 2", "crc16: " + result2)
//        val result3 = CRC16().computeCheckSum(hexStr.toByteArray(Charsets.UTF_8))
//        Log.d("hexstr 3", "crc16: " + result3)
        val result4 = CRC16Util.getCRC16(hexArray)
        Log.d("hexstr 4", "crc16: " + result4)
        val result5 = CrcUtil.crc_16_CCITT_False(hexArray)
        Log.d("hexstr 5", "crc16: " + result5)
        val result6 = CrcUtil.crc16(hexArray)
        Log.d("hexstr 6", "crc16: " + result6)

        val result7 = com.example.zack.rxjavademo.merchantparser.CRC16.computeCRC16ByNormalString(content)
        Log.d("hexstr 7", "crc6: " + result7)

        val result8 = CRC162.computeCrc16Normal(content)
        Log.d("hexstr 8", "crc16: " + result8)
    }

    fun toHexArray(inputStr: String): ByteArray {
        val result = ByteArray(inputStr.length / 2)
        for (i in 0 until inputStr.length / 2)
            result[i] = (Integer.parseInt(inputStr.substring(i * 2, i * 2 + 2), 16) and 0xff).toByte()
        return result
    }

    private fun parseQrcode() {
        val logger = object : IBerTlvLogger {
            override fun isDebugEnabled(): Boolean {
                return true
            }

            override fun debug(aFormat: String?, vararg args: Any?) {
                System.out.println("tlv " + aFormat + args)
            }
        }
//        val content = "00020101021226340012HK.COM.HKICL010300402070009999520400005303344540555.005802NA5902NA6002NA620063046734"
        val content = "hQVDUFYwMWETTwegAAAAVVVVUAhQcm9kdWN0MWETTwegAAAAZmZmUAhQcm9kdWN0MmJJWggSNFZ4kBI0WF8gDkNBUkRIT0xERVIvRU1WXy0IcnVlc2RlZW5kIZ8QBwYBCgMAAACfJghYT9OF+iNLzJ82AgABnzcEbVjvEw=="
        val bytes = HexUtil.decodeBase64(content)
//        val content = "50045649534157131000023100000033D44122011003400000481F"
//        val bytes = HexUtil.parseHex(content)
//        val decodeBase64 = HexUtil.decodeBase64(content)
        val parser = BerTlvParser(logger)
        val tlv = parser.parse(bytes, 0, bytes.size)
        BerTlvLogger.log("tlv ", tlv, logger)
    }

    private fun testSchemeOpen() {
        btnSchemeOpen.setOnClickListener({
            val intent = Intent(MainActivity@ this, SchemeActivity::class.java)
            startActivity(intent)
        })
    }

    fun testLimitPrintScreen() {
        btnLimitPrintScreen.setOnClickListener({
            startActivity(Intent(this, LimitPrintScreenActivity::class.java))
        })
    }

    private fun testSwitchMapBySwitchMap() {
        val datas = (1..4).map { it }
        Observable.fromIterable(datas)
                .switchMap {
                    Thread({
                        //                        println("before first switchMap sleep # " + it)
                        Thread.sleep((it) * 1000L)
                    }).start()
                    println("before switchMap 1# " + it)
                    // 这里如果每个observable不订阅在新线程里面那么
                    // 根据发射顺序，而不是每个的耗时来获取最后的一个被观察者对象
                    Observable.just("first s(witchMap: # " + it).subscribeOn(Schedulers.newThread())
                }
                .switchMap {
                    println("before switchMap 2# " + it)
                    Observable.just("second switchMap: # " + it)
                }
                .subscribe({
                    println("result switchMap by switchMap " + it)
                }, {
                    println(it.message)
                })
    }

    private fun enterSecondActivity() {
        val intent = Intent(this, SecondActivity::class.java)
        btn_countdown.setOnClickListener({
            startActivity(intent)
        })
    }

    private fun testDelayEt() {
        etDelay.setCheckTextListener(object : OnCheckTextListener() {
            override fun beforeCheckContent(s: CharSequence?, start: Int, count: Int, after: Int) {
                etCountainer.showBottomMessage = false
            }

            override fun checkContent(content: String) {
                println("delayet check content: $content")
                if (content.contains("error")) {
                    etCountainer.apply {
                        bottomMessage.text = "error input"
                        bottomMessage.contentDescription = "error error input"
                        bottomMessage.announceForAccessibility(bottomMessage.contentDescription)
                        showBottomMessage = true
                    }
                } else {
                    etCountainer.showBottomMessage = false
                }
                tv_content.text = content
            }
        })
    }

    private fun testConcat() {
        val ram = false
        val disk = false
        val network = "network"

        val ramOb = Observable.create(ObservableOnSubscribe<String> {
            when (ram) {
                true -> it.onNext("ram ok")
                else -> it.onComplete()
            }
        })
        val diskOb = Observable.create(ObservableOnSubscribe<String> {
            when (disk) {
                true -> it.onNext("disk ok")
                else -> it.onComplete()
            }
        })
        val netOb = Observable.just(network)

        Observable.concat(ramOb, diskOb, netOb)
                .subscribe({
                    println("on next: # $it")
                }, {
                    println("on error: # ${it.message}")
                })
    }

    private fun testSwitchMap() {
        val list = (1..3).toList()
        // switchmap在发射之后只会接收最后一个
        Observable.fromIterable(list)
                .switchMap {
                    println("before current thread: ${Thread.currentThread()}")
                    val sleep = (5 - it) * 1000.toLong()
                    Thread.sleep(sleep)
                    println("before after sleep")
                    Observable.just(it).subscribeOn(Schedulers.newThread())
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    println("before result $it")
                })
    }

    private fun testFlatMap() {
        val students = mutableListOf<Student>()
        for (i in 1..3) {
            val student = Student()
            for (j in 1..2) {
                val course = Course()
                course.name = "$student 's #$j"
                student.addCourse(course)
            }
            students.add(student)
        }

        Observable.fromIterable(students)
                .flatMap(object : Function<Student, Observable<Course>> {
                    override fun apply(t: Student): Observable<Course> {
                        println("before student $t")
                        return Observable.fromIterable(t.courses)
                    }
                })
                .subscribe({
                    println("before onnext ${it.name}")
                })

        // 1、测试flatMap
        // 结论：每个发射的都会从flatMap进行执行，flatMap能把里面的每个内容fromIterable发射
        // 成为observable对象然后操作，如果是map则只是单个数据发射后就不能进行observable操作了
        Observable.just("1", "2", "3", "4")
                .map { t ->
                    println("before map $t")
                    "result $t"
                }
                .flatMap {
                    println("before flatMap $it")
                    Observable.just(it)
                }
                .toList()
                .subscribe(Consumer<MutableList<String>> {
                    println("before onnext $it")
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        CheckContentDelay.instance.remove()
        etDelay.cancelCountTimer()
    }

    private fun initData() {
        initHandler()

        initCountDown()

        initRxBus()

        initListener()
    }

    private fun initRxBus() {
        RxBus.instance.toObservable(String::class.java)
                .delay(3, TimeUnit.SECONDS)
                .subscribe({
                    println("rxbus check $it")
                }, {
                    println("rxbus error $it")
                }, {
                    println("rxbus complete")
                })

        Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(e: ObservableEmitter<String>?) {
                // 订阅变化

            }

        })

        Observable.create(MyEmitter.instance)
                .debounce(3, TimeUnit.SECONDS)
                .subscribe({
                    println("before result #$it")
                })
    }

    private fun initCountDown() {
        countdownCheck = CountdownCheck(delayMiniSeconds, 1000.toLong())
    }

    private fun initHandler() {
        timerHandler = Handler()
        checkContent = Runnable {
            checkEtContent()
        }
    }

    fun post(t: String) {

        // handler type
//         CheckContentDelay.instance.post(t, object: CountdownCheckListener{
//             override fun checkResult(result: String) {
//                 println("check result: $result" + " at ${Thread.currentThread()}")
//                 tv_content.text = result
//             }
//         })

        // count down timer
        //countdownCheck.startCheck(t, object: CountdownCheckListener{
        //     override fun checkResult(result: String) {
        //        println("check result: $result")
        //        tv_content.text = result
        //     }
        // })

        // rxbus
        // RxBus.instance.post(t)

        // debounce type
        etChangeObservable
                // 使用防抖动进行延迟输入，只有一个参数的时候可以直接 { }
                .debounce(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .flatMap {
                    println("check before")
                    val result = checkContentByDelay(it)
                    Observable.just(result)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    tv_content.text = it
                }, {
                    println(it.message)
                }, {
                    println("completed")
                })

    }

    private fun checkFormat(it: String): Observable<String> {
        val result = "checked # $it"
        return Observable.just(result)
    }

    // ObservableOnSubscribe需要确定一个泛型，所以这里的参数需要填入一个 String
    // 如果没有泛型要求那么这里的参数可以不写，直接 { }
    private val etChangeObservable = Observable.create(ObservableOnSubscribe<String> {
        // 订阅edittext的输入监听，发生了更改就当观察者发送一个onNext
        et_input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // after text changed delay 1s trigger a rx target
                println("after et changed ${s.toString()}")
                it.onNext(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                println("before et changed")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                println("text cha nged")
            }
        })
    })

    private fun initListener() {
        btn_click.setOnClickListener({
            dismissEtFocus()
        })

        et_input.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
            // change focus
            println("release et's focus")
            checkEtContent()
        }

        et_input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                post(s.toString())
            }
        })

        btnSwitchMap.setOnClickListener({
            testSwitchMapBySwitchMap()
        })

        btnEnterScan.setOnClickListener({
            startActivity(Intent(this, QRScanTestActivity::class.java))
        })
    }

    fun checkContentByDelay(content: String): String {
        val result = "result: #$content"
        return result
    }

    private fun checkEtContent() {
        // check et content
        println("et content: " + et_input.text.toString())
    }

    private fun dismissEtFocus() {
        // test edittext loses focus
        // et_input.clearFocus()
        // et_input.isFocusable = false
    }

}

class Student {

    var courses: MutableList<Course> = mutableListOf()

    fun addCourse(course: Course) {
        courses.add(course)
    }
}

class Course {
    var name: String = ""
        set(value) {
            field = "course #$value"
        }
}
