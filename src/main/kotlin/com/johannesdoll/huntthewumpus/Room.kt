package com.johannesdoll.huntthewumpus

data class Room(
    val content: RoomContent
)

sealed class RoomContent {
    object Wumpus : RoomContent()
}