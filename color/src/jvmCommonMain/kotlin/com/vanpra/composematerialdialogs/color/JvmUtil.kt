package com.vanpra.composematerialdialogs.color

import java.util.Locale

internal actual fun Int.toHexString(): String {
    return Integer.toHexString(this)
        .uppercase(Locale.ROOT)
}