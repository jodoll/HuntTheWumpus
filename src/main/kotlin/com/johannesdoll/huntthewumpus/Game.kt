package com.johannesdoll.huntthewumpus

import arrow.core.Either
import arrow.core.continuations.either

fun Room.enter(state: GameState<*>): GameState<*> = when (content) {
    RoomContent.Wumpus,
    RoomContent.BottomlessPit -> GameState.Lost(state.inventory, this)
    RoomContent.GiantBat -> GameState.Idle(state.inventory, Room())
    RoomContent.Empty -> GameState.Idle(state.inventory, this)
}

fun Room.shoot(state: GameState<*>): Either<GameState<*>, GameState<*>> = either.eager {
    val updatedInventory = state.inventory
        .consumeArrow()
        .mapLeft { GameState.Idle(state.inventory, state.currentRoom) }
        .bind()

    when (content) {
        is RoomContent.Wumpus -> GameState.Won(updatedInventory, state.currentRoom)
        else -> state.withInventory(updatedInventory)
    }
}

private fun Inventory.consumeArrow(): Either<Inventory, Inventory> = Either.conditionally(
    test = arrows > 0,
    ifFalse = { this },
    ifTrue = { Inventory.arrows.modify(this) { it - 1 } }
)

