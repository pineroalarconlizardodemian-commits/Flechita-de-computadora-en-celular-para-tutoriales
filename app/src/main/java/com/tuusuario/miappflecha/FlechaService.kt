package com.tuusuario.miappflecha

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.ImageView

class FlechaService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var flechaView: ImageView
    private lateinit var params: WindowManager.LayoutParams

    private var initialX = 0
    private var initialY = 0
    private var touchX = 0f
    private var touchY = 0f

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        crearFlecha()
    }

    private fun crearFlecha() {
        // ... el código que ya te pasé ...
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
