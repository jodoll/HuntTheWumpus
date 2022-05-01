package com.johannesdoll.huntthewumpus

import arrow.core.Either

sealed interface GameState<T : GameState<T>> {

    val inventory: Inventory
    val currentRoom: Room
    val map: GameMap
    val random: RandomGameLogic

    fun withInventory(inventory: Inventory): T
    fun inRoom(room: Room): T

    data class Lost(
        override val inventory: Inventory,
        override val currentRoom: Room,
        override val map: GameMap,
        override val random: RandomGameLogic
    ) : GameState<Lost> {
        override fun withInventory(inventory: Inventory) = copy(inventory = inventory)
        override fun inRoom(room: Room) = copy(currentRoom = room)
    }

    data class Won(
        override val inventory: Inventory,
        override val currentRoom: Room,
        override val map: GameMap,
        override val random: RandomGameLogic
    ) : GameState<Won> {
        override fun withInventory(inventory: Inventory): Won = copy(inventory = inventory)
        override fun inRoom(room: Room) = copy(currentRoom = room)
    }

    data class Idle(
        override val inventory: Inventory,
        override val currentRoom: Room,
        override val map: GameMap,
        override val random: RandomGameLogic
    ) : GameState<Idle> {
        override fun withInventory(inventory: Inventory): Idle = copy(inventory = inventory)
        override fun inRoom(room: Room) = copy(currentRoom = room)
    }

}

fun GameState<*>.loose() = GameState.Lost(inventory, currentRoom, map, random)
fun GameState<*>.win() = GameState.Won(inventory, currentRoom, map, random)

val Either<GameState<*>, GameState<*>>.state: GameState<*>
    get() = when (this) {
        is Either.Right -> value
        is Either.Left -> value
    }

fun GameState<*>.nextRandomRoom() = random.nextRandomRoom(map.rooms.toList())