package com.johannesdoll.huntthewumpus

import arrow.optics.optics

data class Room(
    val content: RoomContent
)

@optics
data class Inventory(
    val arrows: Int = 0
) {
    companion object
}

sealed class RoomContent {
    object Wumpus : RoomContent()
    object Empty : RoomContent()
}