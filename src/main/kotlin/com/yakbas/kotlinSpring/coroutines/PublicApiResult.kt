package com.yakbas.kotlinSpring.coroutines

data class PublicApiResult(val count: Int, val entries: List<PublicApi>) {

    operator fun plus(other: PublicApiResult): PublicApiResult {
        return PublicApiResult(
            count = this.count + other.count,
            entries = this.entries + other.entries
        )
    }
}
