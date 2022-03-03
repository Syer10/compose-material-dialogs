package com.vanpra.composematerialdialogdemos.demos

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.ImeAction
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import com.vanpra.composematerialdialogdemos.DialogAndShowButton
import com.vanpra.composematerialdialogdemos.Strings
import com.vanpra.composematerialdialogs.iconTitle
import com.vanpra.composematerialdialogs.input
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.title

class BasicDialogDemo : Screen {
    override val key: ScreenKey = uniqueScreenKey
    @Composable
    override fun Content() {
        Column {
            BasicDialogDemoContent()
        }
    }
}
/**
 * @brief Basic Dialog Demos
 */
@Composable
private fun BasicDialogDemoContent() {
    DialogAndShowButton(buttonText = "Basic Dialog") {
        title(text = Strings.location_dialog_title)
        message(text = Strings.location_dialog_message)
    }

    DialogAndShowButton(
        buttonText = "Basic Dialog With Buttons",
        buttons = {
            negativeButton("Disagree")
            positiveButton("Agree")
        }
    ) {
        title(text = Strings.location_dialog_title)
        message(text = Strings.location_dialog_message)
    }

    DialogAndShowButton(
        buttonText = "Basic Dialog With Buttons and Icon Title",
        buttons = {
            negativeButton("Disagree")
            positiveButton("Agree")
        }
    ) {
        iconTitle(
            icon = {
                Image(
                    Icons.Default.LocationOn,
                    contentDescription = "Location Icon",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                )
            },
            text = Strings.location_dialog_title
        )
        message(text = Strings.location_dialog_message)
    }

    DialogAndShowButton(
        buttonText = "Basic Dialog With Stacked Buttons",
        buttons = {
            negativeButton("No Thanks")
            positiveButton("Turn On Speed Boost")
        }
    ) {
        title(text = Strings.location_dialog_title)
        message(text = Strings.location_dialog_message)
    }

    DialogAndShowButton(
        buttonText = "Basic Input Dialog",
        buttons = {
            negativeButton("Cancel")
            positiveButton("Ok")
        }
    ) {
        title(text = Strings.input_dialog_title)
        input(label = "Name", hint = "Jon Smith") {
            println("SELECTION:$it")
        }
    }

    DialogAndShowButton(
        buttonText = "Basic Input Dialog With Immediate Focus",
        buttons = {
            negativeButton("Cancel")
            positiveButton("Ok")
        }
    ) {
        val focusRequester = remember { FocusRequester() }
        title(text = Strings.input_dialog_title)
        input(
            label = "Name",
            hint = "Jon Smith",
            focusRequester = focusRequester,
            focusOnShow = true
        ) {
            println("SELECTION:$it")
        }
    }

    DialogAndShowButton(
        buttonText = "Input Dialog with submit on IME Action",
        buttons = {
            negativeButton("Cancel")
            positiveButton("Ok")
        }
    ) {
        title(text = Strings.input_dialog_title)
        input(
            label = "Name", hint = "Jon Smith",
            keyboardActions = KeyboardActions(
                onDone = { submit() }
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        ) {
            println("SELECTION:$it")
        }
    }

    DialogAndShowButton(
        buttonText = "Input Dialog with input validation",
        buttons = {
            negativeButton("Cancel")
            positiveButton("Ok")
        }
    ) {
        title("Please enter your email")
        input(
            label = "Email",
            hint = "hello@example.com",
            errorMessage = "Invalid email",
            isTextValid = {
                it.contains('@')
            }
        ) {
            println("SELECTION:$it")
        }
    }
}