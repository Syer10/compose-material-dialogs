package com.vanpra.composematerialdialogs

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
        usePlatformDefaultWidth = properties.usePlatformDefaultWidth,
        decorFitsSystemWindows = properties.decorFitsSystemWindows
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
internal actual fun getDialogShape(isWindowDialog: Boolean, shape: Shape): Shape = shape

@Composable
internal actual fun ScreenConfiguration.getMaxHeight(isWindowDialog: Boolean): Dp {
    return if (isLargeDevice()) {
        screenHeightDp.dp - 96.dp
    } else {
        560.dp
    }
}

@Composable
internal actual fun ScreenConfiguration.getPadding(isWindowDialog: Boolean): Dp {
    val isDialogFullWidth = screenWidthDp == LocalConfiguration.current.screenWidthDp
    return if (isDialogFullWidth) 16.dp else 0.dp
}

internal actual fun Modifier.dialogHeight(isWindowDialog: Boolean): Modifier = wrapContentHeight()

internal actual fun Modifier.dialogMaxSize(isWindowDialog: Boolean, maxHeight: Dp): Modifier =
    this.sizeIn(maxHeight = maxHeight, maxWidth = 560.dp)

internal actual fun getLayoutHeight(isWindowDialog: Boolean, maxHeightPx: Int, layoutHeight: Int): Int {
    return min(maxHeightPx, layoutHeight)
}
