package com.johannesdoll.huntthewumpus

import arrow.core.NonEmptyList

class GameMap private constructor(private val map: Map<Int, ConnectedRoom>) {

    val rooms: List<Room>
        get() = map.values.map { it.room }

    val connectedRooms: List<ConnectedRoom>
        get() = map.values.toList()

    operator fun get(room: Room): NonEmptyList<Int>? = map.values.find { it.room.number == room.number }?.connections

    operator fun get(roomNumber: Int): ConnectedRoom = requireNotNull(map[roomNumber]) {
        "Invalid room number $roomNumber"
    }

    constructor(vararg rooms: ConnectedRoom) : this(rooms.associateBy { it.room.number })
    constructor(rooms: List<ConnectedRoom>) : this(rooms.associateBy { it.room.number })
}

data class ConnectedRoom(
    val room: Room,
    val connections: NonEmptyList<Int>
)

infix fun Room.withTunnelsTo(rooms: NonEmptyList<Int>) = ConnectedRoom(this, rooms)

fun Room.withTunnelsTo(firstRoom: Int, vararg otherRooms: Int) =
    ConnectedRoom(this, roomsWithNumber(firstRoom, *otherRooms))

fun roomsWithNumber(firstRoom: Int, vararg otherRooms: Int) = NonEmptyList(firstRoom, otherRooms.toList())

fun GameMap.emptyRooms() = rooms.filter { it.content is RoomContent.Empty }
