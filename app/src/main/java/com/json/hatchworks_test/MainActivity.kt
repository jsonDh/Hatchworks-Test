package com.json.hatchworks_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.json.hatchworks_test.ui.theme.HatchworksTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HatchworksTestTheme {
            }
        }
    }
}