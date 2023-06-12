package com.yakbas.kotlinSpring.operatorOverloading

internal data class Order(
    val items: MutableList<OrderItem>
) {
    fun sum(): Double = items.sumOf { it.amount * it.price }
}
