package com.yakbas.kotlinSpring.operatorOverloading

internal class OrderItem(
    itemName: String?,
    price: Double,
    var amount: Int
) : Item(itemName, price) {

    constructor(item: Item, amount: Int) : this(item.itemName!!, item.price, amount)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OrderItem) return false

        if (amount != other.amount && itemName != other.itemName && price != other.price) return false

        return true
    }

    override fun hashCode(): Int {
        return amount
    }


}
