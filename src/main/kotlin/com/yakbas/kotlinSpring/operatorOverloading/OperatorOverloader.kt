package com.yakbas.kotlinSpring.operatorOverloading

import java.time.LocalDate


internal operator fun Item.plus(other: Item): Item =
    Item(itemName = "${this.itemName} + ${other.itemName}", price = this.price + other.price)

internal operator fun Item.compareTo(other: Item) = this.price.compareTo(other.price)

internal operator fun OrderItem.unaryPlus() = this.amount++
internal operator fun OrderItem.unaryMinus() = this.amount--

internal operator fun Order.get(index: Int) = this.items[index]
internal operator fun Order.set(index: Int, newItem: OrderItem) = this.items.set(index, newItem)

internal operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> {

    return object : Iterator<LocalDate> {
        var current = start

        override fun hasNext(): Boolean {
            return current <= endInclusive
        }

        override fun next() = current.apply { current = current.plusDays(1) }
    }

}