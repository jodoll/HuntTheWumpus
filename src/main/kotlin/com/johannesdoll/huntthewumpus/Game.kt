package com.johannesdoll.huntthewumpus

fun Room.enter(): GameState {
    return GameState.Lost
}

fun Room.shoot(inventory: Inventory): GameState {
    return GameState.Won
}

