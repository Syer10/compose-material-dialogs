package com.vanpra.composematerialdialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 *  Adds a title with the given text to the dialog
 * @param text title text from a string literal
 * @param res title text from a string resource
 * @param center text is aligned to center when true
 */
@Composable
fun MaterialDialogScope.title(
    text: String,
    center: Boolean = false
) {
    var modifier = Modifier
        .fillMaxWidth()
        .padding(start = 24.dp, end = 24.dp)
        .height(64.dp)
        .wrapContentHeight(Alignment.CenterVertically)

    modifier = modifier.then(
        Modifier.wrapContentWidth(
            if (center) {
                Alignment.CenterHorizontally
            } else {
                Alignment.Start
            }
        )
    )

    Text(
        text = text,
        color = MaterialTheme.colors.onSurface,
        style = MaterialTheme.typography.h6,
        modifier = modifier
    )
}

/**
 *  Adds a title with the given text and icon to the dialog
 * @param text title text from a string literal
 * @param textRes title text from a string resource
 * @param icon optional icon displayed at the start of the title
 */
@Composable
fun MaterialDialogScope.iconTitle(
    text: String,
    icon: @Composable () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp)
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(Modifier.width(14.dp))
        Text(
            text = text,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h6
        )
    }
}

/**
 *  Adds paragraph of text to the dialog
 * @param text message text from a string literal
 * @param res message text from a string resource
 */
@Composable
fun MaterialDialogScope.message(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colors.onSurface,
        style = MaterialTheme.typography.body1,
        modifier = Modifier
            .padding(bottom = 28.dp, start = 24.dp, end = 24.dp)
    )
}

/**
 * Create an view in the dialog with the given content and appropriate padding
 * @param content the content of the custom view
 */
@Composable
fun MaterialDialogScope.customView(content: @Composable () -> Unit) {
    Box(modifier = Modifier.padding(bottom = 28.dp, start = 24.dp, end = 24.dp)) {
        content()
    }
}
