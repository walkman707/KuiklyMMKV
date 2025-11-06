package com.kuikly.kuiklymmkv

import android.app.Application
import com.tencent.mmkv.MMKV

class KRApplication : Application() {

    init {
        application = this
    }

    companion object {
        lateinit var application: Application
    }


    override fun onCreate() {
        super.onCreate()

        MMKV.initialize(this)

        MMKV.defaultMMKV().run {
            encode("test", "abcdef")
        }
    }
}