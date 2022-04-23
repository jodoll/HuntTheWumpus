package com.johannesdoll.huntthewumpus

import arrow.core.Either
import arrow.core.continuations.either

fun Room.enter(state: GameState<*>): GameState<*> = when (content) {
    RoomContent.Wumpus,
    RoomContent.BottomlessPit -> state.inRoom(this).loose()
    RoomContent.GiantBat -> state.inRoom(state.random.nextRandomRoom(state.map.emptyRooms()))
    RoomContent.Empty -> state.inRoom(this)
}


fun Room.shoot(state: GameState<*>): Either<GameState<*>, GameState<*>> = either.eager {
    val updatedInventory = state.inventory
        .consumeArrow()
        .mapLeft { state }
        .bind()

    when (content) {
        is RoomContent.Wumpus -> state.win()
        else -> state
    }.withInventory(updatedInventory)
}

private fun Inventory.consumeArrow(): Either<Inventory, Inventory> = Either.conditionally(
    test = arrows > 0,
    ifFalse = { this },
    ifTrue = { Inventory.arrows.modify(this) { it - 1 } }
)

