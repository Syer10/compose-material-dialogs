package com.vanpra.composematerialdialogdemos.ios.demos

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import com.vanpra.composematerialdialogdemos.demos.DateTimeDialogDemo

class DateTimeDialogDemoScreen : Screen {
    override val key: ScreenKey = uniqueScreenKey
    @Composable
    override fun Content() {
        Column {
            DateTimeDialogDemo()
        }
    }
}
