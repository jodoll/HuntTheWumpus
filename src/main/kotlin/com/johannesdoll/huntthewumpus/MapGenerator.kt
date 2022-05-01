package com.johannesdoll.huntthewumpus

import arrow.core.NonEmptyList
import arrow.core.nonEmptyListOf

fun MapParams.generateMap(): GameMap {
    val numberOfRooms = 2 + giantBats + bottomlessPits
    val roomNumbers = (0 until numberOfRooms).asSequence().shuffled()

    val rooms = roomNumbers
        .mapIndexed { index, number ->
            val content = when {
                index < giantBats -> RoomContent.GiantBat
                index < giantBats + bottomlessPits -> RoomContent.BottomlessPit
                index < giantBats + bottomlessPits + 1 -> RoomContent.Wumpus
                else -> RoomContent.Empty
            }

            Room(number, content)
        }
        .sortedBy { it.number }
        .mapIndexed { index, room ->
            when (index) {
                0 -> room.withTunnelsTo(roomNumbers.minus(0).toList().toNelUnsafe())
                else -> room.withTunnelsTo(nonEmptyListOf(0))
            }
        }
        .toList()

    return GameMap(rooms)
}

fun <T> List<T>.toNelUnsafe() = NonEmptyList.fromListUnsafe(this)