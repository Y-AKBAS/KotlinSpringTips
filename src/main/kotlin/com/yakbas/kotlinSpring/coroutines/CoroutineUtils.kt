package com.yakbas.kotlinSpring.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import com.yakbas.kotlinSpring.common.createLogger

class AsyncResult<out T : Any?>(val value: T?, val isSuccess: Boolean)


object CoroutineUtils {

    val logger = createLogger()


inline fun <T> launchAndProcessIO(blocks: List<suspend () -> AsyncResult<T>>, crossinline consumer: suspend (arg: AsyncResult<T>) -> Unit) =
    runBlocking(Dispatchers.IO) {
        val channel = Channel<AsyncResult<T>?>()
        blocks.forEach { func ->

            launch {
                sendChannelWrapper(channel, func)
            }

            launch {
                receiveChannelMapper(channel, consumer)
            }
        }
    }

suspend inline fun <T> sendChannelWrapper(channel: SendChannel<AsyncResult<T>?>, func: suspend () -> AsyncResult<T>) {
    try {
        channel.send(func())
    } catch (sendException: ClosedSendChannelException) {
        throw IllegalStateException("Channel is Closed. Unable to send...")
    } catch (e: Exception) {
        channel.send(null)
    }
}

suspend inline fun <T> receiveChannelMapper(channel: ReceiveChannel<AsyncResult<T>?>, crossinline consumer: suspend (arg: AsyncResult<T>) -> Unit) {
    try {
        channel.receive()?.also { consumer(it) }
    } catch (receiveException: ClosedReceiveChannelException) {
        throw IllegalStateException("Channel is Closed. Unable to receive...")
    } catch (e: Exception) {
    }
}


    inline fun <R : Any?> consumeBlockingAsyncIO(blocks: List<suspend () -> AsyncResult<R>>, crossinline consumer: suspend (AsyncResult<R>) -> Unit) =
        runBlocking(Dispatchers.IO) {
            supervisorScope {
                blocks.map { async { it() } }
                    .mapNotNull { extractDeferredWithJoin(it) }
                    .forEach { launch { consumer(it) } }
            }
        }

    inline fun <R> supplyBlockingIO(crossinline supplier: suspend CoroutineScope.() -> R) = runBlocking(Dispatchers.IO) {
        return@runBlocking supervisorScope {
            return@supervisorScope supplier()
        }
    }

    fun <R : Any?> supplyBlockingAsyncIO(blocks: List<suspend () -> R?>) = runBlocking(Dispatchers.IO) {
        require(blocks.size >= 2) { "No need to block for less than 2 functions!" }
        val result = supervisorScope {
            val asyncBlocks = blocks.map { async { it() } }
            return@supervisorScope asyncBlocks.map { extractDeferredWithJoin(it) }
        }

        return@runBlocking result
    }

    inline fun <F : Any?, S : Any?> supplyBlockingAsyncPairIO(crossinline first: suspend () -> F?, crossinline second: suspend () -> S?) = runBlocking(Dispatchers.IO) {
        val result = supervisorScope {
            val firstAsync = async { first() }
            val secondAsync = async { second() }

            val firstResult = extractDeferredWithJoin(firstAsync)
            val secondResult = extractDeferredWithJoin(secondAsync)

            return@supervisorScope firstResult to secondResult
        }

        return@runBlocking result
    }

    // Calling directly await and not using supervisor job propagates the exception to parent and the whole coroutines block gets cancelled.
    // To prevent it use this function inside a supervisorScope{}
    suspend fun <R : Any?> extractDeferredWithJoin(deferred: Deferred<R>): R? {
        deferred.join()
        return if (!deferred.isCancelled) deferred.await() else {
            deferred.invokeOnCompletion { throwable -> if (throwable != null) logger.error("", throwable) }
            null
        }
    }
}
