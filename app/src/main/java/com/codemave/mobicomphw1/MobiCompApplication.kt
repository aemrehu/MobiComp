package com.codemave.mobicomphw1

import android.app.Application

class MobiCompApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}