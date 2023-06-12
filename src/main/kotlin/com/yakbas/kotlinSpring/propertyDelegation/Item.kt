package com.yakbas.kotlinSpring.propertyDelegation

import kotlin.properties.Delegates

internal class Item(
    itemName: String?,
) {
    var itemName by DefaultDelegator(itemName, "*")
    var price by Delegates.notNull<Double>()
}
