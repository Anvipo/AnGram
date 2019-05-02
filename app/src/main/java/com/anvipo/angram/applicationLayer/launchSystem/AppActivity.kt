package com.anvipo.angram.applicationLayer.launchSystem

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anvipo.angram.R

class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        changeThemeFromSplashToApp()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
    }


    /// PRIVATE


    private fun changeThemeFromSplashToApp() {
        setTheme(R.style.AppTheme)
    }

}
