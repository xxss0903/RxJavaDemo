package com.example.zack.rxjavademo.gsonconverter

import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type

/**
 * Created by zack on 2018/1/22.
 */
class MultiResponseConverter<T>(t: T): Converter<ResponseBody, T>{

    val ve = t
    lateinit var gson: Gson
    lateinit var type: Type

    override fun convert(value: ResponseBody): T {
        value.let {
            val json = it.string()
            val jsonObject = JsonParser().parse(json).asJsonObject
            val jsonPrimitive = jsonObject.getAsJsonObject("code")
            var code = 0
            jsonPrimitive.let {
                code = it.asInt
            }
            val jsonElement = jsonObject.get("message")
            var message = ""
            jsonElement.let {
                message = it.asString
            }
            try{
                val clz:T

            } catch (e: Exception){
                e.printStackTrace()
            }
        }
        return ve
    }

}