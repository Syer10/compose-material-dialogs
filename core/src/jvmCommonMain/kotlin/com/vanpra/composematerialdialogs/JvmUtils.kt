package com.vanpra.composematerialdialogs

import java.util.concurrent.atomic.AtomicInteger

actual class AtomicInt(private val value: AtomicInteger) {
    actual constructor(initialValue: Int) : this(AtomicInteger(initialValue))
    actual constructor() : this(AtomicInteger())
    actual fun set(newValue: Int) {
        value.set(newValue)
    }
    actual fun getAndIncrement(): Int = value.getAndIncrement()
}