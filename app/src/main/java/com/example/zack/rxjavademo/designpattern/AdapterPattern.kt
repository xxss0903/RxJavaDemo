package com.example.zack.rxjavademo.designpattern

/**
 * Created by zack zeng on 2018/4/17.
 */

interface Target {
    fun say()
}

class Client {
    fun sayEnglish(target: Target) {
        target.say()
    }
}

class Adaptee {
    fun sayChinese() {
        println("Say chinese")
    }
}

class Adapter(adaptee: Adaptee) : Target {

    private var adaptee: Adaptee

    init {
        this.adaptee = adaptee
    }

    override fun say() {
        adaptee.sayChinese()
    }
}

class Main {
    fun main() {
        val client = Client()
        val adaptee = Adaptee()
        val adapter = Adapter(adaptee)

        client.sayEnglish(adapter)
    }
}

