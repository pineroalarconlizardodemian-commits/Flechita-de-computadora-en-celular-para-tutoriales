package com.tuusuario.miappflecha

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.app.NotificationCompat

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

        // 1. Notificación obligatoria (foreground service)
        val canalId = "flecha_canal"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                canalId,
                "Flecha",
                NotificationManager.IMPORTANCE_LOW
            )
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(canal)
        }

        val notif = NotificationCompat.Builder(this, canalId)
            .setContentTitle("Flecha activa")
            .setContentText("Arrastra la flecha por la pantalla")
            .setSmallIcon(R.drawable.ic_flecha_raton)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(1, notif)

        // 2. WindowManager + crear la flecha
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        crearFlecha()
    }

    private fun crearFlecha() {
        flechaView = ImageView(this).apply {
            setImageResource(R.drawable.ic_flecha_raton)
            layoutParams = ViewGroup.LayoutParams(150, 150) // tamaño en px

            setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params.x
                        initialY = params.y
                        touchX = event.rawX
                        touchY = event.rawY
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val screenW = resources.displayMetrics.widthPixels
                        val screenH = resources.displayMetrics.heightPixels

                        params.x = (initialX + (event.rawX - touchX).toInt())
                            .coerceIn(0, screenW - width)
                        params.y = (initialY + (event.rawY - touchY).toInt())
                            .coerceIn(0, screenH - height)

                        windowManager.updateViewLayout(flechaView, params)
                        true
                    }
                    else -> false
                }
            }
        }

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        // Posición inicial (esquina superior izquierda)
        params.x = 100
        params.y = 300

        windowManager.addView(flechaView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::flechaView.isInitialized) {
            try {
                windowManager.removeView(flechaView)
            } catch (e: Exception) {
                // ya removida
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
