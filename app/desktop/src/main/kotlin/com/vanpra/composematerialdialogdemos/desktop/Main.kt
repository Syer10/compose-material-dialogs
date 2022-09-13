package com.vanpra.composematerialdialogdemos.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.vanpra.composematerialdialogdemos.desktop.demos.BasicDialogDemo
import com.vanpra.composematerialdialogdemos.desktop.demos.BasicListDialogDemo
import com.vanpra.composematerialdialogdemos.desktop.demos.ColorDialogDemo
import com.vanpra.composematerialdialogdemos.desktop.demos.DateTimeDialogDemo
import com.vanpra.composematerialdialogdemos.desktop.demos.MultiSelectionDemo
import com.vanpra.composematerialdialogdemos.desktop.demos.SingleSelectionDemo
import com.vanpra.composematerialdialogdemos.ui.ComposeMaterialDialogsTheme

fun main() = singleWindowApplication {
    ComposeMaterialDialogsTheme {
        DialogDemos()
    }
}

data class DialogSectionData(val title: String, val screen: () -> Screen)

val sections = listOf(
    DialogSectionData("Basic Dialogs") { BasicDialogDemo() },
    DialogSectionData("Basic List Dialogs") { BasicListDialogDemo() },
    DialogSectionData("Single Selection List Dialogs") { SingleSelectionDemo() },
    DialogSectionData("Multi-Selection List Dialogs") { MultiSelectionDemo() },
    DialogSectionData("Date and Time Picker Dialogs") { DateTimeDialogDemo() },
    DialogSectionData("Color Picker Dialogs") { ColorDialogDemo() }
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
    Navigator(remember { HomeScreen() })
}
