package com.johannesdoll.huntthewumpus

fun Room.enter(inventory: Inventory): GameState {
    return GameState.Lost(inventory)
}

fun Room.shoot(inventory: Inventory): GameState {
    return GameState.Won(inventory.copy(arrows = inventory.arrows - 1))
}

