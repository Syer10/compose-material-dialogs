@file:OptIn(InternalComposeMaterialDialogsApi::class)

package com.vanpra.composematerialdialogs.datetime.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePaint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import com.vanpra.composematerialdialogs.InternalComposeMaterialDialogsApi
import com.vanpra.composematerialdialogs.getDialogScreenWidthDp
import org.jetbrains.skia.Font
import org.jetbrains.skia.Paint
import org.jetbrains.skia.PaintMode
import org.jetbrains.skia.TextBlobBuilder
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
internal actual fun isSmallDevice(): Boolean {
    return getDialogScreenWidthDp() <= 360
}

@Composable
internal actual fun isLargeDevice(): Boolean {
    return getDialogScreenWidthDp() <= 600
}

// todo This function needs to be corrected
internal actual fun Canvas.drawText(
    text: String,
    x: Float,
    y: Float,
    color: Color,
    textSize: Float,
    angle: Float,
    radius: Float,
    isCenter: Boolean?,
    alpha: Int,
) {
    val paint = Paint().apply {
        this.color = color.toArgb()
        this.isAntiAlias = true
    }

    val font = Font().apply {
        size = textSize
    }

    val bounds = font.measureText(text, paint)

    val textWidth = bounds.width
    val textHeight = bounds.height

    val xOffset = when (isCenter) {
        true -> -textWidth / 2f
        false -> 0f
        null -> -textWidth
    }

    val yOffset = textHeight / 2f

    nativeCanvas.drawString(
        text,
        x + (radius * cos(angle)) + xOffset,
        y + (radius * sin(angle)) + yOffset,
        font,
        paint
    )
}