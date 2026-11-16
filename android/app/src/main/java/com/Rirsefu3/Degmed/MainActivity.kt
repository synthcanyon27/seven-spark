package com.Rirsefu3.Degmed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.Rirsefu3.Degmed.core.di.ServiceLocator
import com.Rirsefu3.Degmed.core.theme.AppTheme
import com.Rirsefu3.Degmed.presentation.app.AppRoot

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ServiceLocator.initialize(applicationContext)
        setContent {
            AppTheme {
                AppRoot()
            }
        }
    }
}
