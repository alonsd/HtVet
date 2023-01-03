package com.ht_vet.core.extensions

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.ht_vet.ui.theme.HTvetTheme

fun ComponentActivity.setHTVetContent(content: @Composable () -> Unit){
    setContent {
        HTvetTheme {
            content()
        }
    }
}