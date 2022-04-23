package com.johannesdoll.huntthewumpus

import kotlin.random.Random


interface RandomGameLogic {
    fun nextRandomRoom(rooms: List<Room>): Room
}

class PseudoRandomGameLogic : RandomGameLogic {
    override fun nextRandomRoom(rooms: List<Room>): Room {
        val nextIndex = Random.Default.nextInt(rooms.size)
        return rooms[nextIndex]
    }
}