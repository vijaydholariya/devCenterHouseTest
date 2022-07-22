package com.test.music

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    private val TAG: String = "splashActivity"

    private val SPLASH_SCREEN_TIME_OUT = 2000
    lateinit var appVersion: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_splash_screen)
        appVersion = findViewById(R.id.text_appversion)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        appVersion.text = "App version:".plus(getAppVersion())
        if (supportActionBar != null) {
            supportActionBar?.hide();
        }
        Handler().postDelayed(Runnable {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_SCREEN_TIME_OUT.toLong())

    }

    fun getAppVersion(): String? {
        var versionName: String? = null
        try {
            val pInfo = applicationContext.packageManager?.getPackageInfo(
                applicationContext.packageName,
                0
            )
            versionName = pInfo?.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionName
    }


}