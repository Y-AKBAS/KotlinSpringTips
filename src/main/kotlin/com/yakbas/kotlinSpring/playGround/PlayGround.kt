package com.yakbas.kotlinSpring.playGround

import java.util.function.Function

data class MyClass(
    val name: String,
    val age: Int,
    var balance: Double = 0.0
) {
    fun addBalance(sum: Double) {
        balance += sum
    }
}

fun createRunnable(): Runnable {
    return Runnable { println("All done!") }
}

fun createKotlinFunction(): Function<String, MyClass> {
    return Function { name -> MyClass(name, 28, 20.0) }
}

fun main() {
    val instance = MyClass("Yasin", 28, 20.0)

    val instanceMethod = instance::addBalance
    instanceMethod(100.9)
    println(instance.balance)

    val classMethod = MyClass::addBalance
    classMethod(instance, 35.0)
    println(instance.balance)
    val myClassSequence = sequenceOf(MyClass("Thomas", 25), instance)
    myClassSequence.forEach { println(it.name) }

    val sequence = generateSequence(0) { it + 25 }
    sequence.takeWhile { it <= 100 }.forEach { println(it) }

    val list = listOf(MyClass(name = "John", age = 28), MyClass("Jack", age = 27))
    println(list)
    val sortedList = list.sortedWith(Comparator.comparing(MyClass::age))
    println(sortedList)
}

