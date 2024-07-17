package com.grow.nago.root

import android.app.Application
import android.content.Context

class NagoApplication: Application() {
    init{
        instance = this
    }

    companion object {
        lateinit var instance: NagoApplication
        fun applicationContext() : Context {
            return instance.applicationContext
        }
    }
}