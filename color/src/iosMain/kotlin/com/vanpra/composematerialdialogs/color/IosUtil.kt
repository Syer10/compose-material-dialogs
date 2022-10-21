package com.vanpra.composematerialdialogs.color

import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

internal actual fun Int.toHexString(): String {
    return NSString.stringWithFormat(format = "%X", this)
}