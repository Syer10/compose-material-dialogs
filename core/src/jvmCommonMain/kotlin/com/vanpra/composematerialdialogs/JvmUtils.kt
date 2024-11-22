package com.vanpra.composematerialdialogs

import java.util.concurrent.atomic.AtomicInteger

actual class AtomicInt(private val value: AtomicInteger): Number() {
    actual constructor(initialValue: Int) : this(AtomicInteger(initialValue))
    actual constructor() : this(AtomicInteger())
    actual fun set(newValue: Int) {
        value.set(newValue)
    }
    actual fun getAndIncrement(): Int = value.getAndIncrement()

    override fun toByte(): Byte = value.toByte()

    override fun toChar(): Char = value.toChar()

    override fun toDouble(): Double = value.toDouble()

    override fun toFloat(): Float = value.toFloat()

    override fun toInt(): Int = value.get()

    override fun toLong(): Long = value.toLong()

    override fun toShort(): Short = value.toShort()
}