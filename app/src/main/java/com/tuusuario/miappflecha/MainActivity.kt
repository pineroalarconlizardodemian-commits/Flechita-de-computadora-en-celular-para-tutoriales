package com.tuusuario.miappflecha

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_OVERLAY = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnPermiso).setOnClickListener {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, REQUEST_CODE_OVERLAY)
            } else {
                iniciarServicio()
            }
        }

        findViewById<Button>(R.id.btnIniciar).setOnClickListener {
            if (Settings.canDrawOverlays(this)) {
                iniciarServicio()
            }
        }

        findViewById<Button>(R.id.btnDetener).setOnClickListener {
            stopService(Intent(this, FlechaService::class.java))
        }
    }

    private fun iniciarServicio() {
        val intent = Intent(this, FlechaService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_OVERLAY) {
            if (Settings.canDrawOverlays(this)) {
                iniciarServicio()
            }
        }
    }
}
