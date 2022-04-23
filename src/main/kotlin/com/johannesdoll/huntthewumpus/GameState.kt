package com.johannesdoll.huntthewumpus

sealed class GameState {

    data class Lost(override val inventory: Inventory) : GameState()
    data class Won(override val inventory: Inventory) : GameState()

    abstract val inventory: Inventory
}