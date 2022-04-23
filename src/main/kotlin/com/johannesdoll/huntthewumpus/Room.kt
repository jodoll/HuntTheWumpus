package com.johannesdoll.huntthewumpus

data class Room(
    val content: RoomContent
)

data class Inventory(
    val arrows: Int
)

sealed class RoomContent {
    object Wumpus : RoomContent()
}