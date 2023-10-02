package com.vanpra.composematerialdialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

internal fun List<Pair<MaterialDialogButtonTypes, Placeable>>.buttons(type: MaterialDialogButtonTypes) =
    this.filter { it.first == type }.map { it.second }


// Screen Configuration

internal class ScreenConfiguration(val screenWidthDp: Int, val screenHeightDp: Int)

@Composable
internal expect fun rememberScreenConfiguration(): ScreenConfiguration

@Composable
internal expect fun isSmallDevice(): Boolean

@Composable
internal expect fun isLargeDevice(): Boolean

// Dialog
@Composable
internal expect fun DialogBox(
    onDismissRequest: () -> Unit,
    properties: MaterialDialogProperties,
    content: @Composable () -> Unit,
)


// SecureFlagPolicy For Android Dialogs
@Stable
enum class SecurePolicy {
    Inherit, //Forces [WindowManager.LayoutParams.FLAG_SECURE]
    SecureOn, // Sets [WindowManager.LayoutParams.FLAG_SECURE] on the window
    SecureOff // No [WindowManager.LayoutParams.FLAG_SECURE] will be set on the window
}

// Desktop Window Position

fun DesktopWindowPosition(x: Dp, y: Dp): DesktopWindowPosition = DesktopWindowPosition.Absolute(x = x, y = y)
fun DesktopWindowPosition(alignment: Alignment): DesktopWindowPosition = DesktopWindowPosition.Aligned(alignment)

@Stable
sealed class DesktopWindowPosition {
    abstract val x: Dp

    abstract val y: Dp

    abstract val isSpecified: Boolean

    @Immutable
    object PlatformDefault : DesktopWindowPosition() {
        override val x: Dp get() = Dp.Unspecified
        override val y: Dp get() = Dp.Unspecified
        override val isSpecified: Boolean get() = false
    }

    @Immutable
    class Absolute(override val x: Dp, override val y: Dp) : DesktopWindowPosition() {
        override val isSpecified: Boolean = true
    }

    @Immutable
    class Aligned(val alignment: Alignment) : DesktopWindowPosition() {
        override val x: Dp get() = Dp.Unspecified
        override val y: Dp get() = Dp.Unspecified
        override val isSpecified: Boolean get() = false
    }
}

@Immutable
data class MaterialDialogProperties(
    val dismissOnBackPress: Boolean = true,
    val dismissOnClickOutside: Boolean = true,
    val securePolicy: SecurePolicy = SecurePolicy.Inherit,
    val usePlatformDefaultWidth : Boolean = false,
    val decorFitsSystemWindows: Boolean = true,
    val isWindowDialog: Boolean = true,
    val windowPosition: DesktopWindowPosition = DesktopWindowPosition(Alignment.Center),
    val windowSize: DpSize = DpSize(400.dp, 300.dp),
    val windowTitle: String = "Untitled",
    val windowIcon: Painter? = null,
    val windowIsResizable: Boolean = true
)

@Composable
internal expect fun getDialogShape(isWindowDialog: Boolean, shape: Shape): Shape

@Composable
internal expect fun ScreenConfiguration.getMaxHeight(isWindowDialog: Boolean): Dp

@Composable
internal expect fun ScreenConfiguration.getPadding(isWindowDialog: Boolean, maxWidth: Dp): Dp

internal expect fun Modifier.dialogHeight(isWindowDialog: Boolean): Modifier

internal expect fun Modifier.dialogMaxSize(isWindowDialog: Boolean, maxHeight: Dp): Modifier

internal expect fun getLayoutHeight(isWindowDialog: Boolean, maxHeightPx: Int, layoutHeight: Int): Int

expect class AtomicInt(): Number {
    constructor(initialValue: Int)
    fun set(newValue: Int)
    fun getAndIncrement(): Int
}