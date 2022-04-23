package com.johannesdoll.huntthewumpus

import arrow.core.Either
import arrow.core.continuations.either

fun Room.enter(inventory: Inventory): GameState = when (content) {
    is RoomContent.Empty -> GameState.Idle(inventory)
    else -> GameState.Lost(inventory)
}

fun Room.shoot(inventory: Inventory): Either<GameState, GameState> = either.eager {
    val updatedInventory = inventory
        .consumeArrow()
        .mapLeft { GameState.Won(inventory) }
        .bind()

    GameState.Won(updatedInventory)
}

private fun Inventory.consumeArrow(): Either<Inventory, Inventory> = Either.conditionally(
    test = arrows > 0,
    ifFalse = { this },
    ifTrue = { Inventory.arrows.modify(this) { it - 1 } }
)

