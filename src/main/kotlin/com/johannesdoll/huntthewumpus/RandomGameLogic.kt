package com.johannesdoll.huntthewumpus


interface RandomGameLogic {
    fun nextRandomRoom(rooms: Collection<Room>): Room
}

class PseudoRandomGameLogic : RandomGameLogic {
    override fun nextRandomRoom(rooms: Collection<Room>): Room = rooms.random()
}