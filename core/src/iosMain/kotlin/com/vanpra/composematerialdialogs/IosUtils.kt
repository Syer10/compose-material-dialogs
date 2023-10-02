package com.vanpra.composematerialdialogs

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.atomicfu.atomic
import kotlinx.cinterop.useContents
import platform.UIKit.UIScreen
import kotlin.math.min

@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This is internal API for Compose Material Dialogs that shouldn't be used"
)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY
)
annotation class InternalComposeMaterialDialogsApi

actual class AtomicInt actual constructor(initialValue: Int): Number() {
    private val value = atomic(initialValue)
    actual constructor() : this(0)

    actual fun set(newValue: Int) {
        value.value = newValue
    }
    actual fun getAndIncrement(): Int = value.getAndIncrement()
    override fun toByte(): Byte = value.value.toByte()

    override fun toChar(): Char = value.value.toChar()

    override fun toDouble(): Double = value.value.toDouble()

    override fun toFloat(): Float = value.value.toFloat()

    override fun toInt(): Int = value.value

    override fun toLong(): Long = value.value.toLong()

    override fun toShort(): Short = value.value.toShort()
}


private fun DpSize.toScreenConfiguration(): ScreenConfiguration {
    return ScreenConfiguration(
        width.value.toInt(),
        height.value.toInt()
    )
}

@Composable
internal actual fun isSmallDevice(): Boolean {
    return LocalScreenConfiguration.current.screenWidthDp <= 360
}

@Composable
internal actual fun isLargeDevice(): Boolean {
    return LocalScreenConfiguration.current.screenWidthDp <= 600
}

@Composable
internal actual fun rememberScreenConfiguration(): ScreenConfiguration {
    return LocalScreenConfiguration.current
}

@InternalComposeMaterialDialogsApi
@Composable
fun getDialogScreenWidthDp(): Int {
    return LocalScreenConfiguration.current.screenWidthDp
}

internal val LocalScreenConfiguration = compositionLocalOf<ScreenConfiguration>{ throw IllegalStateException("Unused") }

@Composable
internal actual fun DialogBox(
    onDismissRequest: () -> Unit,
    properties: MaterialDialogProperties,
    content: @Composable () -> Unit,
) {
    val size = remember {
        UIScreen.mainScreen.bounds.useContents {
            IntSize(size.width.toInt(), size.height.toInt())
        }
    }
    CompositionLocalProvider(
        LocalScreenConfiguration provides ScreenConfiguration(
            size.width,
            size.height
        )
    ) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                dismissOnBackPress = properties.dismissOnBackPress,
                dismissOnClickOutside = properties.dismissOnClickOutside,
                usePlatformDefaultWidth = properties.usePlatformDefaultWidth,
            ),
        ) {
            content()
        }
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
internal actual fun ScreenConfiguration.getPadding(isWindowDialog: Boolean, maxWidth: Dp): Dp {
    val isDialogFullWidth = screenWidthDp == maxWidth.value.toInt()
    return if (isDialogFullWidth) 16.dp else 0.dp
}

internal actual fun Modifier.dialogHeight(isWindowDialog: Boolean): Modifier = wrapContentHeight()

internal actual fun Modifier.dialogMaxSize(isWindowDialog: Boolean, maxHeight: Dp): Modifier = sizeIn(maxHeight = maxHeight, maxWidth = 560.dp)

internal actual fun getLayoutHeight(isWindowDialog: Boolean, maxHeightPx: Int, layoutHeight: Int): Int {
    return min(maxHeightPx, layoutHeight)
}
