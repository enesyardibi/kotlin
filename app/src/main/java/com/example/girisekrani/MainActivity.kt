package com.example.girisekrani

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.girisekrani.ui.theme.GirisEkraniTheme
import com.example.girisekrani.feature.splash.ui.SplashScreen
import com.example.girisekrani.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { false }
        }
        
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GirisEkraniTheme {
                AppNavigation()
            }
        }
    }
}

