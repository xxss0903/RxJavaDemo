package com.example.zack.rxjavademo

import com.example.zack.rxjavademo.delayhanlde.CountdownCheck
import com.example.zack.rxjavademo.delayhanlde.CountdownCheckListener
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * delay check unit test
 * Created by zack on 2018/1/23.
 */
class DelayHandleTest{

    private val strInput = "check"
    private lateinit var countTimer:CountdownCheck
    private val duration: Long = 2000
    private val interval: Long= 1000

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        countTimer = CountdownCheck(duration, interval)
    }

    @Test
    fun testCountdownTimerDemo(){
        // test countdown check by countdowntimer
        countTimer.startCheck(strInput, object: CountdownCheckListener{
            override fun checkResult(result: String) {
                Assert.assertEquals(result, "after#check")
            }
        })
    }
}