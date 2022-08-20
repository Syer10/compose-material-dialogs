package com.vanpra.composematerialdialogs

import android.util.Log
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import kotlin.math.min

@Composable
internal actual fun rememberScreenConfiguration(): ScreenConfiguration {
    val config = LocalConfiguration.current
    return remember(config.screenWidthDp, config.screenHeightDp) {
        ScreenConfiguration(
            screenWidthDp = config.screenWidthDp,
            screenHeightDp = config.screenHeightDp
        )
    }
}

@Composable
internal actual fun isSmallDevice(): Boolean {
    return LocalConfiguration.current.screenWidthDp <= 360
}

@Composable
internal actual fun isLargeDevice(): Boolean {
    return LocalConfiguration.current.screenWidthDp <= 600
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal actual fun DialogBox(
    onDismissRequest: () -> Unit,
    properties: MaterialDialogProperties,
    content: @Composable () -> Unit
) = Dialog(
    onDismissRequest = onDismissRequest,
    properties = DialogProperties(
        dismissOnBackPress = properties.dismissOnBackPress,
        dismissOnClickOutside = properties.dismissOnClickOutside,
        securePolicy = properties.securePolicy.toSecureFlagPolicy(),
        usePlatformDefaultWidth = properties.usePlatformDefaultWidth
    ),
    content = content
)

private fun SecurePolicy.toSecureFlagPolicy(): SecureFlagPolicy {
    return when (this) {
        SecurePolicy.Inherit -> SecureFlagPolicy.Inherit
        SecurePolicy.SecureOn -> SecureFlagPolicy.SecureOn
        SecurePolicy.SecureOff -> SecureFlagPolicy.SecureOff
    }
}

@Composable
internal actual fun getDialogShape(shape: Shape): Shape = shape

@Composable
internal actual fun ScreenConfiguration.getMaxHeight(): Dp {
    return if (isLargeDevice()) {
        screenHeightDp.dp - 96.dp
    } else {
        560.dp
    }
}

@Composable
internal actual fun ScreenConfiguration.getPadding(maxWidth: Dp): Dp {
    val isDialogFullWidth = screenWidthDp == maxWidth.value.toInt()
    return if (isDialogFullWidth) 16.dp else 0.dp
}

internal actual fun Modifier.dialogHeight(): Modifier = wrapContentHeight()

internal actual fun Modifier.dialogMaxSize(maxHeight: Dp): Modifier = sizeIn(maxHeight = maxHeight, maxWidth = 560.dp)

internal actual fun getLayoutHeight(maxHeightPx: Int, layoutHeight: Int): Int {
    return min(maxHeightPx, layoutHeight)
}
