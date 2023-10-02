package com.vanpra.composematerialdialogs

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
) = if (properties.isWindowDialog) {
    DialogWindow(
        onCloseRequest = onDismissRequest,
        state = rememberDialogState(
            position = properties.windowPosition.toWindowPosition(),
            size = properties.windowSize
        ),
        title = properties.windowTitle,
        icon = properties.windowIcon,
        resizable = properties.windowIsResizable,
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
} else {
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
}

private fun DesktopWindowPosition.toWindowPosition(): WindowPosition {
    return when (this) {
        DesktopWindowPosition.PlatformDefault -> WindowPosition.PlatformDefault
        is DesktopWindowPosition.Absolute -> WindowPosition(x = x, y = y)
        is DesktopWindowPosition.Aligned -> WindowPosition(alignment)
    }
}

@Composable
internal actual fun getDialogShape(isWindowDialog: Boolean, shape: Shape) = if (isWindowDialog) {
    RectangleShape
} else {
    shape
}

@Composable
internal actual fun ScreenConfiguration.getMaxHeight(isWindowDialog: Boolean): Dp {
    return if (isWindowDialog) {
        screenHeightDp.dp
    } else {
        if (isLargeDevice()) {
            screenHeightDp.dp - 96.dp
        } else {
            560.dp
        }
    }
}

@Composable
internal actual fun ScreenConfiguration.getPadding(isWindowDialog: Boolean, maxWidth: Dp): Dp {
    return if (isWindowDialog) {
        0.dp
    } else {
        val isDialogFullWidth = screenWidthDp == maxWidth.value.toInt()
        if (isDialogFullWidth) 16.dp else 0.dp
    }
}

internal actual fun Modifier.dialogHeight(isWindowDialog: Boolean): Modifier = if (isWindowDialog) {
    fillMaxHeight()
} else {
    wrapContentHeight()
}

internal actual fun Modifier.dialogMaxSize(isWindowDialog: Boolean, maxHeight: Dp): Modifier = if (isWindowDialog) {
    fillMaxWidth()
} else {
    sizeIn(maxHeight = maxHeight, maxWidth = 560.dp)
}

internal actual fun getLayoutHeight(isWindowDialog: Boolean, maxHeightPx: Int, layoutHeight: Int): Int {
    return if (isWindowDialog) {
        maxHeightPx
    } else {
        min(maxHeightPx, layoutHeight)
    }
}
