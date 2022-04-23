package com.johannesdoll.huntthewumpus

class GameMap(map: Map<Room, Set<Room>>) : Map<Room, Set<Room>> by map {
    constructor(vararg entries: Pair<Room, Set<Room>>) : this(entries.toMap())
}

fun GameMap.emptyRooms() = keys.filter { it.content is RoomContent.Empty }