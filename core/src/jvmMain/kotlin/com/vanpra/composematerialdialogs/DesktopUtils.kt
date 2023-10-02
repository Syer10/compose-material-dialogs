package com.vanpra.composematerialdialogs

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState
import kotlin.math.min

private fun DpSize.toScreenConfiguration(): ScreenConfiguration {
    return ScreenConfiguration(
        width.value.toInt(),
        height.value.toInt()
    )
}

@Composable
internal actual fun rememberScreenConfiguration(): ScreenConfiguration {
    return LocalScreenConfiguration.current
}

@Composable
internal actual fun isSmallDevice(): Boolean {
    return false
}

@Composable
internal actual fun isLargeDevice(): Boolean {
    return true
}

internal val LocalScreenConfiguration = compositionLocalOf<ScreenConfiguration>{ throw IllegalStateException("Unused") }

@Composable
internal actual fun DialogBox(
    onDismissRequest: () -> Unit,
    properties: MaterialDialogProperties,
    content: @Composable () -> Unit
) = if (properties.inlineDialog) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = properties.dismissOnBackPress,
            dismissOnClickOutside = properties.dismissOnClickOutside,
            usePlatformDefaultWidth = properties.usePlatformDefaultWidth,
        ),
    ) {
        BoxWithConstraints {
            CompositionLocalProvider(
                LocalScreenConfiguration provides ScreenConfiguration(
                    maxWidth.value.toInt(),
                    maxHeight.value.toInt()
                ),
                content = content
            )
        }
    }
} else {
    DialogWindow(
        onCloseRequest = onDismissRequest,
        state = rememberDialogState(
            position = properties.position.toWindowPosition(),
            size = properties.size
        ),
        title = properties.title,
        icon = properties.icon,
        resizable = properties.resizable,
        content = {
            BoxWithConstraints {
                CompositionLocalProvider(
                    LocalScreenConfiguration provides ScreenConfiguration(
                        maxWidth.value.toInt(),
                        maxHeight.value.toInt()
                    ),
                    content = content
                )
            }
        }
    )
}

private fun DesktopWindowPosition.toWindowPosition(): WindowPosition {
    return when (this) {
        DesktopWindowPosition.PlatformDefault -> WindowPosition.PlatformDefault
        is DesktopWindowPosition.Absolute -> WindowPosition(x = x, y = y)
        is DesktopWindowPosition.Aligned -> WindowPosition(alignment)
    }
}

@Composable
internal actual fun getDialogShape(inlineDialog: Boolean, shape: Shape) = if (inlineDialog) {
    shape
} else {
    RectangleShape
}

@Composable
internal actual fun ScreenConfiguration.getMaxHeight(inlineDialog: Boolean): Dp {
    return if (inlineDialog) {
        if (isLargeDevice()) {
            screenHeightDp.dp - 96.dp
        } else {
            560.dp
        }
    } else {
        screenHeightDp.dp
    }
}

@Composable
internal actual fun ScreenConfiguration.getPadding(inlineDialog: Boolean, maxWidth: Dp): Dp {
    return if (inlineDialog) {
        val isDialogFullWidth = screenWidthDp == maxWidth.value.toInt()
        if (isDialogFullWidth) 16.dp else 0.dp
    } else {
        0.dp
    }
}

internal actual fun Modifier.dialogHeight(inlineDialog: Boolean): Modifier = if (inlineDialog) {
    wrapContentHeight()
} else {
    fillMaxHeight()
}

internal actual fun Modifier.dialogMaxSize(inlineDialog: Boolean, maxHeight: Dp): Modifier = if (inlineDialog) {
    sizeIn(maxHeight = maxHeight, maxWidth = 560.dp)
} else {
    this
}

internal actual fun getLayoutHeight(inlineDialog: Boolean, maxHeightPx: Int, layoutHeight: Int): Int {
    return if (inlineDialog) {
        min(maxHeightPx, layoutHeight)
    } else {
        maxHeightPx
    }
}
