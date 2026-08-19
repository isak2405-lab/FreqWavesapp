package com.ishakai.babusradio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ishakai.babusradio.ui.RadioApp
import com.ishakai.babusradio.ui.theme.BabusRadioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BabusRadioTheme {
                RadioApp()
            }
        }
    }
}
