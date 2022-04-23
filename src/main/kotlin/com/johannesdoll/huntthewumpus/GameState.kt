package com.johannesdoll.huntthewumpus

import arrow.core.Either

sealed interface GameState {

    val inventory: Inventory

    data class Lost(override val inventory: Inventory) : GameState
    data class Won(override val inventory: Inventory) : GameState

}

val Either<GameState, GameState>.state: GameState
    get() = when (this) {
        is Either.Right -> value
        is Either.Left -> value
    }