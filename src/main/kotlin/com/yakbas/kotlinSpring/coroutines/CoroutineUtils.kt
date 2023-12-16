package com.yakbas.kotlinSpring.coroutines

import com.yakbas.kotlinSpring.common.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import org.slf4j.Logger

class AsyncResult<out T : Any?>(val value: T?, val isSuccess: Boolean)

class FailedCoroutineException(logger: Logger, message: String, throwable: Throwable?) : Exception(message, throwable)

object CoroutineUtils {

    val logger = createLogger()

    inline fun <T> launchAndProcessIO(
        blocks: List<suspend () -> AsyncResult<T>>,
        crossinline consumer: suspend (arg: AsyncResult<T>) -> Unit
    ) =
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

    suspend inline fun <T> sendChannelWrapper(
        channel: SendChannel<AsyncResult<T>?>,
        crossinline func: suspend () -> AsyncResult<T>
    ) {
        try {
            channel.send(func())
        } catch (sendException: ClosedSendChannelException) {
            throw IllegalStateException("Channel is Closed. Unable to send...")
        } catch (e: Exception) {
            channel.send(null)
        }
    }

    suspend inline fun <T> receiveChannelMapper(
        channel: ReceiveChannel<AsyncResult<T>?>,
        crossinline consumer: suspend (arg: AsyncResult<T>) -> Unit
    ) {
        try {
            channel.receive()?.also { consumer(it) }
        } catch (receiveException: ClosedReceiveChannelException) {
            throw IllegalStateException("Channel is Closed. Unable to receive...")
        } catch (e: Exception) {
        }
    }

    inline fun <R : Any?> consumeBlockingAsyncIO(
        blocks: List<suspend () -> AsyncResult<R>>,
        crossinline consumer: suspend (AsyncResult<R>) -> Unit,
        failFast: Boolean = false
    ) = runBlocking(Dispatchers.IO) {
        supervisorScope {

            val asyncResults = blocks
                .map { async { it() } }
                .mapNotNull { extractDeferred(it, failFast) }

            if (failFast) {
                runOrThrow({ asyncResults.forEach { launch { consumer(it) } } }) { throwable ->
                    FailedCoroutineException(
                        logger,
                        "Failed launch coroutine.",
                        throwable
                    )
                }
            } else {
                runOrHandle({ asyncResults.forEach { launch { consumer(it) } } }) { throwable ->
                    logger.error(
                        "",
                        throwable
                    )
                }
            }

        }
    }

    inline fun <R> supplyBlockingIO(crossinline supplier: suspend CoroutineScope.() -> R) =
        runBlocking(Dispatchers.IO) {
            return@runBlocking supervisorScope {
                return@supervisorScope supplier()
            }
        }

    fun <R : Any?> supplyBlockingAsyncIO(blocks: List<suspend () -> R?>, failFast: Boolean = false) =
        runBlocking(Dispatchers.IO) {
            val result = supervisorScope {
                val asyncBlocks = blocks.map { async { it() } }
                return@supervisorScope asyncBlocks.map { extractDeferred(it, failFast) }
            }

            return@runBlocking result
        }

    inline fun <F : Any?, S : Any?> supplyBlockingAsyncPairIO(
        crossinline first: suspend () -> F?,
        crossinline second: suspend () -> S?,
        failFast: Boolean = false
    ) =
        runBlocking(Dispatchers.IO) {
            val result = supervisorScope {
                val firstAsync = async { first() }
                val secondAsync = async { second() }

                val firstResult = extractDeferred(firstAsync, failFast)
                val secondResult = extractDeferred(secondAsync, failFast)

                return@supervisorScope firstResult to secondResult
            }

            return@runBlocking result
        }

    // not using supervisor job propagates the exception to parent and the whole coroutines block gets cancelled.
    // To prevent it use this function inside a supervisorScope{}.
    // If the failfast flag is set to true. The whole coroutine blocks get cancelled.
    suspend fun <R : Any?> extractDeferred(deferred: Deferred<R>, failFast: Boolean = false): R? {
        return if (failFast) {
            getOrThrow({ deferred.await() }) { throwable ->
                FailedCoroutineException(
                    logger,
                    "Failed async coroutine.",
                    throwable
                )
            }
        } else {
            getOrHandle({ deferred.await() }) { _ ->
                deferred.invokeOnCompletion { throwable ->
                    if (throwable != null) logger.error(
                        "",
                        throwable
                    )
                }
            }
        }
    }
}
