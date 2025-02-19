package com.vanpra.composematerialdialogs.color

import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanpra.composematerialdialogs.MaterialDialogScope

private val itemSizeDp = 55.dp
private val tickSize = 35.dp

private enum class ColorPickerScreen {
    Palette,
    ARGB
}

/**
 * Defines the behaviour of ARGB Picker
 *
 * @property allowCustomARGB custom ARGB sliders will be shown when true and hidden when false
 * @property showAlphaSelector alpha slider will be shown when true and hidden when false
 */
data class ARGBPickerState internal constructor(
    val allowCustomARGB: Boolean = true,
    val showAlphaSelector: Boolean = true
) {
    companion object {
        val None = ARGBPickerState(allowCustomARGB = false)
        val WithAlphaSelector = ARGBPickerState(showAlphaSelector = true)
        val WithoutAlphaSelector = ARGBPickerState(showAlphaSelector = false)
    }
}

/**
 * @brief Adds a color chooser to the dialog
 *
 * @param colors a list of colors for the user to choose. See [ColorPalette] for predefined colors
 * @param subColors a list of subsets of [colors] for the user to choose from once a main color from
 * colors has been chosen. See [ColorPalette] for predefined sub-colors colors
 * @param initialSelection the index of the color which is selected initially
 * @param argbPickerState controls the behaviour of custom argb picker
 * @param waitForPositiveButton if true the [onColorSelected] callback will only be called when the
 * positive button is pressed, otherwise it will be called when the a new color is selected
 * @param onColorSelected a function which is called with a [Color]. The timing of this call is
 * dictated by [waitForPositiveButton]
 */
@ExperimentalFoundationApi
@Composable
fun MaterialDialogScope.colorChooser(
    colors: List<Color>,
    subColors: List<List<Color>> = listOf(),
    initialSelection: Int = 0,
    argbPickerState: ARGBPickerState = ARGBPickerState.None,
    waitForPositiveButton: Boolean = true,
    onColorSelected: (Color) -> Unit = {}
) {
    BoxWithConstraints {
        val selectedColor = remember { mutableStateOf(colors[initialSelection]) }
        val anchors: DraggableAnchors<ColorPickerScreen> = remember(argbPickerState.allowCustomARGB) {
            if (argbPickerState.allowCustomARGB) {
                DraggableAnchors {
                    ColorPickerScreen.Palette at 0f
                    ColorPickerScreen.ARGB at constraints.maxWidth.toFloat()
                }
            } else {
                DraggableAnchors {
                    ColorPickerScreen.Palette at 0f
                }
            }
        }
        val density = LocalDensity.current
        val swipeState = remember {
            AnchoredDraggableState(
                ColorPickerScreen.Palette,
                anchors = anchors,
                positionalThreshold = { distance -> distance * 0.5f },
                velocityThreshold = { with(density) { 125.dp.toPx() } },
                snapAnimationSpec = tween(),
                decayAnimationSpec = exponentialDecay(), // todo verify if looks the same as previous
            )
        }

        if (waitForPositiveButton) {
            DialogCallback { onColorSelected(selectedColor.value) }
        } else {
            DisposableEffect(selectedColor.value) {
                onColorSelected(selectedColor.value)
                onDispose { }
            }
        }

        Column(
            Modifier
                .padding(bottom = 8.dp)
                .anchoredDraggable(
                    swipeState,
                    orientation = Orientation.Horizontal,
                    reverseDirection = true,
                    enabled = argbPickerState.allowCustomARGB
                )
        ) {
            if (argbPickerState.allowCustomARGB) {
                PageIndicator(swipeState, this@BoxWithConstraints.constraints)
            }

            Layout(
                content = {
                    ColorGridLayout(
                        Modifier.width(this@BoxWithConstraints.maxWidth),
                        colors = colors,
                        selectedColor = selectedColor,
                        subColors = subColors,
                        initialSelection = initialSelection
                    )

                    if (argbPickerState.allowCustomARGB) {
                        Box(Modifier.width(this@BoxWithConstraints.maxWidth)) {
                            CustomARGB(selectedColor, argbPickerState.showAlphaSelector)
                        }
                    }
                },
                modifier = Modifier.testTag("dialog_color_picker")
            ) { measurables, constraints ->
                val placeables = measurables.map { it.measure(constraints) }
                val height = placeables.maxByOrNull { it.height }?.height ?: 0

                layout(constraints.maxWidth, height) {
                    placeables.forEachIndexed { index, placeable ->
                        placeable.place(
                            x = -swipeState.offset.toInt() + index * constraints.maxWidth,
                            y = 0
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun PageIndicator(swipeState: AnchoredDraggableState<ColorPickerScreen>, constraints: Constraints) {
    BoxWithConstraints {
        val indicatorRadius = remember { constraints.maxWidth * 0.01f }
        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(top = 8.dp, bottom = 16.dp)
        ) {
            val ratio = remember(constraints.maxWidth, swipeState.offset) {
                swipeState.offset / constraints.maxWidth.toFloat()
            }
            val color = MaterialTheme.colors.onBackground
            Canvas(modifier = Modifier) {
                val offset = Offset(3 * indicatorRadius, 0f)
                drawCircle(
                    color.copy(0.7f + 0.3f * (1 - ratio)),
                    radius = (indicatorRadius + indicatorRadius * (1 - ratio)).coerceAtMost(15f),
                    center = center - offset
                )
                drawCircle(
                    color.copy(0.7f + 0.3f * ratio),
                    radius = (indicatorRadius + indicatorRadius * ratio).coerceAtMost(15f),
                    center = center + offset
                )
            }
        }
    }
}

@Composable
private fun CustomARGB(selectedColor: MutableState<Color>, showAlphaSelector: Boolean) {
    LazyColumn(Modifier.padding(start = 24.dp, end = 24.dp)) {
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.drawBehind {
                    var whiteColor = true
                    val boxSize = 10
                    var y = 0f
                    for (i in 0..(this.size.width / boxSize).toInt()) {
                        drawRect(
                            if (whiteColor) Color.White else Color.Gray,
                            topLeft = Offset(x = i.toFloat() * boxSize, y = y)
                        )
                        whiteColor = !whiteColor
                        y += boxSize
                    }
                })

                val hexString = remember(selectedColor.value) {
                    val rawHex = selectedColor.value.toArgb().toHexString()
                        .padStart(8, '0')

                    if (!showAlphaSelector) rawHex.substring(2) else rawHex
                }

                Text(
                    text = "#$hexString",
                    color = selectedColor.value.foreground(),
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    textDecoration = TextDecoration.Underline,
                    fontSize = 18.sp
                )
            }
            SliderLayout(selectedColor, showAlphaSelector)
        }
    }
}

@Composable
private fun SliderLayout(selectedColor: MutableState<Color>, showAlpha: Boolean) {
    if (showAlpha) {
        LabelSlider(
            modifier = Modifier.padding(top = 16.dp),
            sliderTestTag = "dialog_color_picker_alpha_slider",
            label = "A",
            value = selectedColor.value.alpha * 255,
            sliderColor = Color.DarkGray
        ) {
            selectedColor.value = selectedColor.value.copy(alpha = it / 255f)
        }
    }

    LabelSlider(
        modifier = Modifier.padding(top = 16.dp),
        sliderTestTag = "dialog_color_picker_red_slider",
        label = "R",
        value = selectedColor.value.red * 255,
        sliderColor = Color.Red
    ) {
        selectedColor.value = selectedColor.value.copy(red = it / 255f)
    }

    LabelSlider(
        modifier = Modifier.padding(top = 16.dp),
        sliderTestTag = "dialog_color_picker_green_slider",
        label = "G",
        value = selectedColor.value.green * 255,
        sliderColor = Color.Green
    ) {
        selectedColor.value = selectedColor.value.copy(green = it / 255f)
    }

    LabelSlider(
        modifier = Modifier.padding(top = 16.dp),
        sliderTestTag = "dialog_color_picker_blue_slider",
        label = "B",
        value = selectedColor.value.blue * 255,
        sliderColor = Color.Blue
    ) {
        selectedColor.value = selectedColor.value.copy(blue = it / 255f)
    }
}

@Composable
private fun LabelSlider(
    modifier: Modifier = Modifier,
    label: String,
    value: Float,
    sliderColor: Color,
    sliderTestTag: String,
    onSliderChange: (Float) -> Unit
) {
    BoxWithConstraints {
        Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                label,
                style = MaterialTheme.typography.h6,
                fontSize = 16.sp,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.width(10.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Slider(
                value = value,
                onValueChange = onSliderChange,
                valueRange = 0f..255f,
                steps = 255,
                modifier = Modifier
                    .width(this@BoxWithConstraints.maxWidth - 56.dp)
                    .testTag(sliderTestTag),
                colors = SliderDefaults.colors(
                    activeTickColor = Color.Unspecified,
                    activeTrackColor = sliderColor,
                    thumbColor = sliderColor,
                    inactiveTickColor = Color.Unspecified
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                Modifier
                    .width(40.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    value.toInt().toString(),
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}

@Composable
private fun ColorGridLayout(
    modifier: Modifier = Modifier,
    colors: List<Color>,
    selectedColor: MutableState<Color>,
    subColors: List<List<Color>> = listOf(),
    initialSelection: Int
) {
    var mainSelectedIndex by remember { mutableStateOf(initialSelection) }
    var showSubColors by remember { mutableStateOf(false) }

    val itemSize = with(LocalDensity.current) { itemSizeDp.toPx().toInt() }

    GridView(modifier, itemSize = itemSize) {
        if (!showSubColors) {
            colors.forEachIndexed { index, item ->
                ColorView(
                    modifier = Modifier.testTag("dialog_color_selector_$index"),
                    color = item,
                    selected = index == mainSelectedIndex
                ) {
                    if (mainSelectedIndex != index) {
                        mainSelectedIndex = index
                        selectedColor.value = item
                    }

                    if (subColors.isNotEmpty()) {
                        showSubColors = true
                    }
                }
            }
        } else {
            Box(
                Modifier
                    .testTag("dialog_sub_color_back_btn")
                    .size(itemSizeDp)
                    .clip(CircleShape)
                    .clickable(
                        onClick = {
                            showSubColors = false
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go back to main color page",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(tickSize)
                )
            }

            subColors[mainSelectedIndex].forEachIndexed { index, item ->
                ColorView(
                    modifier = Modifier.testTag("dialog_sub_color_selector_$index"),
                    color = item,
                    selected = selectedColor.value == item
                ) {
                    selectedColor.value = item
                }
            }
        }
    }
}

@Composable
private fun ColorView(
    modifier: Modifier = Modifier,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier
            .size(itemSizeDp)
            .clip(CircleShape)
            .background(color)
            .border(1.dp, MaterialTheme.colors.onBackground, CircleShape)
            .clickable(
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Image(
                Icons.Default.Done,
                contentDescription = null,
                colorFilter = ColorFilter.tint(color.foreground()),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(tickSize)
            )
        }
    }
}

@Composable
private fun GridView(
    modifier: Modifier = Modifier,
    itemsInRow: Int = 4,
    itemSize: Int,
    content: @Composable () -> Unit
) {
    BoxWithConstraints(modifier) {
        LazyColumn {
            item {
                Layout(
                    { content() },
                    Modifier
                        .padding(top = 8.dp, start = 24.dp, end = 24.dp)
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) { measurables, constraints ->
                    val spacing =
                        (constraints.maxWidth - (itemSize * itemsInRow)) / (itemsInRow - 1)
                    val additionalRow = measurables.size % 2
                    val rows = maxOf((measurables.size / itemsInRow) + additionalRow, 1)
                    val layoutHeight = (rows * itemSize) + ((rows - 1) * spacing)

                    layout(constraints.maxWidth, layoutHeight) {
                        measurables
                            .map {
                                it.measure(
                                    Constraints(
                                        maxHeight = itemSize,
                                        maxWidth = itemSize
                                    )
                                )
                            }
                            .forEachIndexed { index, it ->
                                it.place(
                                    x = (index % itemsInRow) * (itemSize + spacing),
                                    y = (index / itemsInRow) * (itemSize + spacing)
                                )
                            }
                    }
                }
            }
        }
    }
}

private fun Color.foreground(): Color {
    return if (luminance() > 0.5f) Color.Black else Color.White
}