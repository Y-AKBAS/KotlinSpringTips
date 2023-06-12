package com.yakbas.kotlinSpring.propertyDelegation

fun main() {

    val newItem = Item(itemName = null)
    println(newItem.itemName)
    newItem.apply { itemName = "Changed item name" }.also { println(it.itemName) }

    newItem.apply {
        try {
            println(this.price)
        } catch (ex: IllegalStateException) {
            println(ex.message)
        }

        this.price = 100.0
        println("Price after setting it: ${this.price}")
    }
}
