@file:OptIn(BetaInteropApi::class)

package com.vanpra.composematerialdialogs.ios

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.window.ComposeUIViewController
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.vanpra.composematerialdialogdemos.ios.demos.BasicDialogDemoScreen
import com.vanpra.composematerialdialogdemos.ios.demos.BasicListDialogDemoScreen
import com.vanpra.composematerialdialogdemos.ios.demos.ColorDialogDemoScreen
import com.vanpra.composematerialdialogdemos.ios.demos.DateTimeDialogDemoScreen
import com.vanpra.composematerialdialogdemos.ios.demos.MultiSelectionDemoScreen
import com.vanpra.composematerialdialogdemos.ios.demos.SingleSelectionDemoScreen
import com.vanpra.composematerialdialogdemos.ui.ComposeMaterialDialogsTheme
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.autoreleasepool
import kotlinx.cinterop.cstr
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCValues
import platform.Foundation.NSStringFromClass
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationDelegateProtocol
import platform.UIKit.UIApplicationDelegateProtocolMeta
import platform.UIKit.UIApplicationMain
import platform.UIKit.UIResponder
import platform.UIKit.UIResponderMeta
import platform.UIKit.UIScreen
import platform.UIKit.UIWindow

@OptIn(ExperimentalForeignApi::class)
fun main() {
    Napier.base(DebugAntilog())

    val args = emptyArray<String>()
    memScoped {
        val argc = args.size + 1
        val argv = (arrayOf("skikoApp") + args).map { it.cstr.ptr }.toCValues()
        autoreleasepool {
            UIApplicationMain(argc, argv, null, NSStringFromClass(SkikoAppDelegate))
        }
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

class SkikoAppDelegate @OverrideInit constructor() : UIResponder(), UIApplicationDelegateProtocol {
    companion object : UIResponderMeta(), UIApplicationDelegateProtocolMeta

    private var _window: UIWindow? = null
    override fun window() = _window
    override fun setWindow(window: UIWindow?) {
        _window = window
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun application(application: UIApplication, didFinishLaunchingWithOptions: Map<Any?, *>?): Boolean {
        window = UIWindow(frame = UIScreen.mainScreen.bounds).apply {
            rootViewController = ComposeUIViewController {
                ComposeMaterialDialogsTheme {
                    DialogDemos()
                }
            }
            makeKeyAndVisible()
        }
        return true
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
                    },
                    modifier = Modifier.windowInsetsPadding(WindowInsets.displayCutout)
                )
            },
        ) {
            Box(Modifier.padding(it)) {
                CurrentScreen()
            }
        }
    }
}
