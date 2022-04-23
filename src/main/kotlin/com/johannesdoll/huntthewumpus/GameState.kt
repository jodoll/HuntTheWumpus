package com.johannesdoll.huntthewumpus

import arrow.core.Either

sealed interface GameState<T : GameState<T>> {

    val inventory: Inventory
    val currentRoom: Room

    fun withInventory(inventory: Inventory): T

    data class Lost(
        override val inventory: Inventory,
        override val currentRoom: Room,
    ) : GameState<Lost> {
        override fun withInventory(inventory: Inventory): Lost = copy(inventory = inventory)
    }

    data class Won(
        override val inventory: Inventory,
        override val currentRoom: Room,
    ) : GameState<Won> {
        override fun withInventory(inventory: Inventory): Won = copy(inventory = inventory)
    }

    data class Idle(
        override val inventory: Inventory,
        override val currentRoom: Room,
    ) : GameState<Idle> {
        override fun withInventory(inventory: Inventory): Idle = copy(inventory = inventory)
    }

}

val Either<GameState<*>, GameState<*>>.state: GameState<*>
    get() = when (this) {
        is Either.Right -> value
        is Either.Left -> value
    }