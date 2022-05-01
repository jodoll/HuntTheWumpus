package com.johannesdoll.huntthewumpus

fun Room.sense(map: GameMap): List<Danger> = map[this]
    .orEmpty()
    .map { map[it] }
    .mapNotNull {
        when (it.room.content) {
            RoomContent.BottomlessPit -> Danger.ColdWind
            RoomContent.Empty -> null
            RoomContent.GiantBat -> Danger.RustlingSound
            RoomContent.Wumpus -> Danger.TerribleSmell
        }
    }

sealed interface Danger {
    object TerribleSmell : Danger
    object ColdWind : Danger
    object RustlingSound : Danger
}