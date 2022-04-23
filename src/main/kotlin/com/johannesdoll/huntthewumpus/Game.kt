package com.johannesdoll.huntthewumpus

import arrow.core.Either
import arrow.core.continuations.either

fun Room.enter(state: GameState<*>): GameState<*> = when (content) {
    RoomContent.Wumpus,
    RoomContent.BottomlessPit -> state.inRoom(this).loose()
    RoomContent.GiantBat -> state.inRoom(state.random.nextRandomRoom(state.map.emptyRooms()))
    RoomContent.Empty -> state.inRoom(this)
}


fun Room.shootInto(state: GameState<*>): Either<GameState<*>, GameState<*>> = either.eager {
    val updatedInventory = state.inventory
        .consumeArrow()
        .mapLeft { state }
        .bind()

    this@shootInto
        .shootInto(state.map)
        .fold(
            ifLeft = { state },
            ifRight = { state.win() }
        )
        .withInventory(updatedInventory)
}

private fun Room.shootInto(map: GameMap, velocityInRooms: Int = 3): ShootingResult {
    val velocityInRoomsLeft = velocityInRooms - 1
    val connectedRooms = map[this].orEmpty()
    return when {
        content is RoomContent.Wumpus -> Hit(Unit)
        velocityInRoomsLeft == 0 -> Miss(Unit)
        connectedRooms.isNotEmpty() -> connectedRooms.first().shootInto(map, velocityInRoomsLeft)
        else -> Miss(Unit)
    }
}

typealias ShootingResult = Either<Unit, Unit>
typealias Hit = Either.Right<Unit>
typealias Miss = Either.Left<Unit>

private fun Inventory.consumeArrow(): Either<Inventory, Inventory> = Either.conditionally(
    test = arrows > 0,
    ifFalse = { this },
    ifTrue = { Inventory.arrows.modify(this) { it - 1 } }
)

