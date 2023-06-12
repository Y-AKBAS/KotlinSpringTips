package com.yakbas.kotlinSpring.operatorOverloading

import java.time.LocalDate

fun main() {

    val water = Item(itemName = "Water", price = 0.50)
    val sandwich = Item(itemName = "Sandwich", price = 2.5)
    itemOperators(water, sandwich)

    val waterOrders = OrderItem(water, 2)
    val sandwichOrders = OrderItem(sandwich, 3)
    orderItemOperators(waterOrders, sandwichOrders)

    val orderItems = mutableListOf(waterOrders, sandwichOrders)
    val order = Order(orderItems)
    orderOperators(order)

    val startDate = LocalDate.of(2023, 4, 17)
    val currentDay = LocalDate.now()
    localDateOperators(startDate, currentDay)
}

private fun localDateOperators(startDate: LocalDate, currentDay: LocalDate) {
    val days = startDate..currentDay
    val howManyDaysPassed = days.iterator().asSequence().count()

    println("$howManyDaysPassed days have passed since my start")
}

private fun orderOperators(order: Order) {
    order.items.forEachIndexed { index, orderItem ->
        val itemFromOperator = order[index] // uses [] operator
        if (itemFromOperator != orderItem) throw IllegalStateException("Order items should have been the same!")
        println("$index. order item name: ${itemFromOperator.itemName}")
    }

    print("The order: ${order[1].itemName} has been replaced with: ")
    order[1] = OrderItem(itemName = "Doner", price = 6.5, 4)
    println(order[1].itemName)
}

private fun orderItemOperators(
    waterOrders: OrderItem,
    sandwichOrders: OrderItem
) {
    println()

    println("The initial amount of water orders: ${waterOrders.amount}")
    println("The initial amount of water orders: ${sandwichOrders.amount}")

    +waterOrders
    +waterOrders
    +sandwichOrders

    println("\nWe have some incoming guests and need more water and sandwiches...\n")

    println("The amount of water orders: ${waterOrders.amount}")
    println("The amount of water orders: ${sandwichOrders.amount}")
}

private fun itemOperators(water: Item, sandwich: Item) {
    val waterAndSandwich = water + sandwich
    println(waterAndSandwich.itemName + " = " + waterAndSandwich.price)

    val isWaterCheaper = water < sandwich
    println("is water cheaper than sandwich: $isWaterCheaper")
}

