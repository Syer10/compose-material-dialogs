package com.vanpra.composematerialdialogdemos.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.vanpra.composematerialdialogdemos.desktop.demos.BasicDialogDemoScreen
import com.vanpra.composematerialdialogdemos.desktop.demos.BasicListDialogDemoScreen
import com.vanpra.composematerialdialogdemos.desktop.demos.ColorDialogDemoScreen
import com.vanpra.composematerialdialogdemos.desktop.demos.DateTimeDialogDemoScreen
import com.vanpra.composematerialdialogdemos.desktop.demos.MultiSelectionDemoScreen
import com.vanpra.composematerialdialogdemos.desktop.demos.SingleSelectionDemoScreen
import com.vanpra.composematerialdialogdemos.ui.ComposeMaterialDialogsTheme

fun main() = singleWindowApplication {
    ComposeMaterialDialogsTheme {
        DialogDemos()
    }
}

data class DialogSectionData(val title: String, val screen: () -> Screen)

val sections = listOf(
    DialogSectionData("Basic Dialogs") { BasicDialogDemoScreen() },
    DialogSectionData("Basic List Dialogs") { BasicListDialogDemoScreen() },
    DialogSectionData("Single Selection List Dialogs") { SingleSelectionDemoScreen() },
    DialogSectionData("Multi-Selection List Dialogs") { MultiSelectionDemoScreen() },
    DialogSectionData("Date and Time Picker Dialogs") { DateTimeDialogDemoScreen() },
    DialogSectionData("Color Picker Dialogs") { ColorDialogDemoScreen() }
)

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        LazyColumn {
            items(sections) {
                TextButton(
                    onClick = { navigator push it.screen() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(MaterialTheme.colors.primaryVariant),
                ) {
                    Text(
                        it.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center),
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    }
}

/**
 * @brief Collection of Material Dialog Demos
 */
@Composable
fun DialogDemos() {
    Navigator(remember { HomeScreen() }) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(it::popUntilRoot) {
                            Icon(Icons.Default.Home, null)
                        }
                    },
                    title = {
                        Text("Demo")
                    }
                )
            }
        ) {
            Box(Modifier.padding(it)) {
                CurrentScreen()
            }
        }
    }
}
