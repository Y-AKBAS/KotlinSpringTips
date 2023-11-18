package com.yakbas.kotlinSpring.encryption.ecc

import java.math.BigDecimal

// a and b are the parameters of the curve
// y^2 = x^3 + ax + b
class ECC(private val a: BigDecimal, private val b: BigDecimal) {

    fun doubleAndAdd(n: Int, point: Point): Point {
        var temp = point.copy()
        val binaryN = Integer.toBinaryString(n)

        binaryN.forEach {
            temp = addPoints(temp, temp)
            if (it == '1') {
                temp = addPoints(temp, point)
            }
        }

        return temp
    }

    // if first == second, then we have to do point doubling
    // if not we can just do point adding
    fun addPoints(first: Point, second: Point): Point {
        val m = if (first == second) {
            ((BigDecimal(3) * first.x.pow(2) + a) / (BigDecimal(2) * first.y))
        } else {
            ((second.y - first.y) / (second.x - first.x))
        }

        val x3 = m.pow(2) - second.x - first.x
        val y3 = m * (first.x - x3) - first.y

        return Point(x3, y3)
    }
}
