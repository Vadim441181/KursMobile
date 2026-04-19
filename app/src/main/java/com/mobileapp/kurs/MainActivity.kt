package com.mobileapp.kurs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mobileapp.kurs.navigation.AppNavGraph
import com.mobileapp.kurs.ui.theme.KursTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KursTheme {
                AppNavGraph()
            }
        }
    }
}