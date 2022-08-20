package com.vanpra.composematerialdialogs.color

import platform.Foundation.NSString
import platform.Foundation.create

internal actual fun Int.toHexString(): String {
    return NSString.create(format = "%X", args = arrayOf(this)).toString()
}