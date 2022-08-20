package com.vanpra.composematerialdialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Application
import kotlinx.atomicfu.atomic
import kotlin.math.min

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

internal val LocalScreenConfiguration = compositionLocalOf<ScreenConfiguration>{ throw IllegalStateException("Unused") }


@Composable
internal actual fun DialogBox(
    onDismissRequest: () -> Unit,
    properties: MaterialDialogProperties,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(
        Modifier.fillMaxSize()
            .let {
                if (properties.dismissOnClickOutside) {
                    it.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDismissRequest
                    )
                } else it
            }
    ) {
        CompositionLocalProvider(
            LocalScreenConfiguration provides ScreenConfiguration(
                maxWidth.value.toInt(),
                maxHeight.value.toInt()
            ),
            content = content
        )
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
