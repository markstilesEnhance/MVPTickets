package com.example.tickets_with_coroutines

import io.reactivex.Observable


fun exampleHowToEmitData() {
    val stringObservable = Observable.just("a", "b")

    stringObservable
        .subscribe(
            {value -> println("Received: $value")},
            {error -> println("Error: $error")},
            {println("Completed!")}
        )
}

fun main() {
    exampleHowToEmitData()
    Thread.sleep(3000)
}