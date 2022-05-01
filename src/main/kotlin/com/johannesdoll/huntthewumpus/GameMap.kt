package com.johannesdoll.huntthewumpus

import arrow.core.NonEmptyList

class GameMap(map: Map<Room, NonEmptyList<Int>>) : Map<Room, NonEmptyList<Int>> by map {

    constructor(vararg rooms: ConnectedRoom) : this(rooms.associate { it.room to it.connections })
    constructor(rooms: List<ConnectedRoom>) : this(rooms.associate { it.room to it.connections })
}

data class ConnectedRoom(
    val room: Room,
    val connections: NonEmptyList<Int>
)

infix fun Room.withTunnelsTo(rooms: NonEmptyList<Int>) = ConnectedRoom(this, rooms)

fun Room.withTunnelsTo(firstRoom: Int, vararg otherRooms: Int) =
    ConnectedRoom(this, roomsWithNumber(firstRoom, *otherRooms))

fun roomsWithNumber(firstRoom: Int, vararg otherRooms: Int) = NonEmptyList(firstRoom, otherRooms.toList())

fun GameMap.emptyRooms() = keys.filter { it.content is RoomContent.Empty }

val GameMap.rooms: Set<Room>
    get() = keys