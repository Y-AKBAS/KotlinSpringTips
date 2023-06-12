package com.yakbas.kotlinSpring.propertyDelegation

import kotlin.reflect.KProperty

internal class DefaultDelegator<T>(private var actual: T, private val default: T) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return actual ?: default
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.actual = value
    }
}
