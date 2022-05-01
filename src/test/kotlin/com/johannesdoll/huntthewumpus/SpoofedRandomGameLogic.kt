package com.johannesdoll.huntthewumpus

class SpoofedRandomGameLogic(firstRoomNumber: Int = 0, vararg otherRoomNumbers: Int) : RandomGameLogic {
    private val roomSequence = sequence {
        while (true) {
            yield(firstRoomNumber)
            yieldAll(otherRoomNumbers.toList())
        }
    }.iterator()

    override fun nextRandomRoom(rooms: Collection<Room>): Room {
        val nextRoomNumber = roomSequence.next()
        return rooms.firstOrNull { nextRoomNumber == it.number }
            ?: throw IllegalStateException("Room $nextRoomNumber does not exist")
    }
}
