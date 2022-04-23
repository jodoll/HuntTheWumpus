package com.johannesdoll.huntthewumpus

import arrow.core.Either
import arrow.core.continuations.either

fun Room.enter(state: GameState<*>): GameState<*> = when (content) {
    is RoomContent.Empty -> GameState.Idle(state.inventory, this)
    else -> GameState.Lost(state.inventory, this)
}

fun Room.shoot(state: GameState<*>): Either<GameState<*>, GameState<*>> = either.eager {
    val updatedInventory = state.inventory
        .consumeArrow()
        .mapLeft { GameState.Idle(state.inventory, state.currentRoom) }
        .bind()

    when (content) {
        is RoomContent.Empty -> state.withInventory(updatedInventory)
        is RoomContent.Wumpus -> GameState.Won(updatedInventory, state.currentRoom)
    }
}

private fun Inventory.consumeArrow(): Either<Inventory, Inventory> = Either.conditionally(
    test = arrows > 0,
    ifFalse = { this },
    ifTrue = { Inventory.arrows.modify(this) { it - 1 } }
)

