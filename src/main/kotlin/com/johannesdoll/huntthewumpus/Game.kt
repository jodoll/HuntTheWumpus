package com.johannesdoll.huntthewumpus

fun Room.enter(inventory: Inventory): GameState {
    return GameState.Lost(inventory)
}

fun Room.shoot(inventory: Inventory): GameState {
    return GameState.Won(inventory.consumeArrow())
}

private fun Inventory.consumeArrow() =
    Inventory.arrows.modify(this) { it - 1 }

